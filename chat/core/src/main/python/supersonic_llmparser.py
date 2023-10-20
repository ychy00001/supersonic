# -*- coding:utf-8 -*-
import os
import logging
import sys
import uvicorn

sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from typing import Any, List, Mapping, Optional, Union, Dict

from fastapi import FastAPI, HTTPException, status

from sql.run import text2sql_agent

from preset_retrieval.run import preset_query_retrieval_run, collection as preset_query_collection
from preset_retrieval.preset_query_db import (add2preset_query_collection, update_preset_query_collection,
                                              empty_preset_query_collection, delete_preset_query_by_ids,
                                              update_preset_query_collection, get_preset_query_by_ids,
                                              preset_query_collection_size)
from vec_call.run import similarity_search as _vec_similarity_search, add as _vec_add, clean as _vec_clean
from vec_call.run_dim_val import similarity_search as _dim_val_similarity_search, add as _dim_val_add, \
    clean as _dim_val_clean
from vec_call.run_func import similarity_search as _func_similarity_search, add as _func_add, clean as _func_clean
from vec_call.vec_item_info import VecItemInfo
from vec_call.dim_val_item_info import DimValItemInfo
from vec_call.func_item_info import FuncItemInfo
from plugin_call.run import plugin_selection_run

from run_config import LLMPARSER_HOST, LLMPARSER_PORT
from util.api_response import return_msg, ret_error, ret_success

app = FastAPI()


@app.post("/query2sql/")
async def din_query2sql(query_body: Mapping[str, Any]):
    if 'queryText' not in query_body:
        raise HTTPException(status_code=400,
                            detail="query_text is not in query_body")
    else:
        query_text = query_body['queryText']

    if 'schema' not in query_body:
        raise HTTPException(status_code=400, detail="schema is not in query_body")
    else:
        schema = query_body['schema']

    if 'currentDate' not in query_body:
        raise HTTPException(status_code=400, detail="currentDate is not in query_body")
    else:
        current_date = query_body['currentDate']

    if 'linking' not in query_body:
        linking = None
    else:
        linking = query_body['linking']

    resp = text2sql_agent.query2sql(query_text=query_text,
                                    schema=schema, current_date=current_date, linking=linking)

    return resp


@app.post("/query2sql_setting_update/")
async def query2sql_setting_update(query_body: Mapping[str, Any]):
    if 'sqlExamplars' not in query_body:
        raise HTTPException(status_code=400,
                            detail="sqlExamplars is not in query_body")
    else:
        sql_examplars = query_body['sqlExamplars']

    if 'exampleNums' not in query_body:
        raise HTTPException(status_code=400, detail="exampleNums is not in query_body")
    else:
        example_nums = query_body['exampleNums']

    text2sql_agent.update_examples(sql_examplars=sql_examplars, example_nums=example_nums)

    return "success"


@app.post("/preset_query_retrival/")
async def preset_query_retrival(query_text_list: List[str], n_results: int = 5):
    parsed_retrieval_res_format = preset_query_retrieval_run(preset_query_collection, query_text_list, n_results)

    return parsed_retrieval_res_format


@app.post("/preset_query_add/")
async def preset_query_add(preset_info_list: List[Mapping[str, str]]):
    preset_queries = []
    preset_query_ids = []

    for preset_info in preset_info_list:
        preset_queries.append(preset_info['preset_query'])
        preset_query_ids.append(preset_info['preset_query_id'])

    add2preset_query_collection(collection=preset_query_collection,
                                preset_queries=preset_queries,
                                preset_query_ids=preset_query_ids)

    return "success"


@app.post("/preset_query_update/")
async def preset_query_update(preset_info_list: List[Mapping[str, str]]):
    preset_queries = []
    preset_query_ids = []

    for preset_info in preset_info_list:
        preset_queries.append(preset_info['preset_query'])
        preset_query_ids.append(preset_info['preset_query_id'])

    update_preset_query_collection(collection=preset_query_collection,
                                   preset_queries=preset_queries,
                                   preset_query_ids=preset_query_ids)

    return "success"


@app.get("/preset_query_empty/")
async def preset_query_empty():
    empty_preset_query_collection(collection=preset_query_collection)

    return "success"


@app.post("/preset_delete_by_ids/")
async def preset_delete_by_ids(preset_query_ids: List[str]):
    delete_preset_query_by_ids(collection=preset_query_collection, preset_query_ids=preset_query_ids)

    return "success"


@app.post("/preset_get_by_ids/")
async def preset_get_by_ids(preset_query_ids: List[str]):
    preset_queries = get_preset_query_by_ids(collection=preset_query_collection, preset_query_ids=preset_query_ids)

    return preset_queries


@app.get("/preset_query_size/")
async def preset_query_size():
    size = preset_query_collection_size(collection=preset_query_collection)

    return size


@app.post("/plugin_selection/")
async def tool_selection(query_body: Mapping[str, Any]):
    if 'queryText' not in query_body:
        raise HTTPException(status_code=400, detail="query_text is not in query_body")
    else:
        query_text = query_body['queryText']

    if 'pluginConfigs' not in query_body:
        raise HTTPException(status_code=400, detail="pluginConfigs is not in query_body")
    else:
        plugin_configs = query_body['pluginConfigs']

    resp = plugin_selection_run(query_text=query_text, plugin_configs=plugin_configs)

    return resp


@app.post("/vec_similarity_search")
async def vec_similarity_search(query_text_list: List[str], query_filter: Optional[Dict[str, str]] = None,
                                n_results: int = 1):
    '''
    Args:
        query_text_list: ['销售额', "年龄"]
        query_filter: {"item_type": "METRIC"}
        n_results: 1

    Returns:
        [
          {
            'key': '销售额',
            'search': [
               Document(page_content='销售价格', metadata={'item_type': 'DIMENSION', 'domain_id': 1, 'model_id': 1, 'metric_id': 2, 'dimension_id': 0})
            ]
          },
          {
            'key': '性别',
            'search': [
              Document(page_content='性别', metadata={'item_type': 'DIMENSION', 'domain_id': 1, 'model_id': 1, 'metric_id': 0, 'dimension_id': 1})
            ]
          }
        ]

    '''
    from chromadb.errors import NoIndexException
    try:
        parsed_retrieval_res_format = _vec_similarity_search(query_text_list, query_filter, n_results=n_results)
    except NoIndexException:
        return ret_error(message="索引未找到，数据库可能已被删除")
    return ret_success(parsed_retrieval_res_format)


@app.post("/vec_insert")
async def vec_insert(insert_list: List[VecItemInfo]):
    '''
    插入数据
    Args:
        [
          {'text': '销售额', 'item_type': 'DIMENSION', 'domain_id': 0, 'model_id': 0, 'metric_id': 0, 'dimension_id': 1}
        ]

    Returns: "success"
    '''
    _vec_add(insert_list)
    return ret_success()


@app.post("/vec_clean")
async def vec_clean():
    '''
    清空cw向量库
    Returns:

    '''
    _vec_clean()
    return ret_success()


@app.post("/dim_val_similarity_search")
async def dim_val_similarity_search(query_text_list: List[str], query_filter: Optional[Dict[str, str]] = None, n_results: int = 1):
    '''
    Args:
        query_text_list: ['SUV车辆', "越野"]
        query_filter: { "dim_column" : "type"}
        n_results: 1

    Returns:
        [
          {
            'key': 'SUV车辆',
            'search': [
               Document(page_content='SUV车辆', metadata={'dim_column': 'type', 'dim_value': 'SUV', 'model_id': 3, 'data_source_id': 5})
            ]
          },
          {
            'key': '越野车',
            'search': [
              Document(page_content='越野车', metadata={'dim_column': 'type', 'dim_value': '越野车', 'model_id': 3, 'data_source_id': 5})
            ]
          }
        ]

    '''
    from chromadb.errors import NoIndexException
    try:
        parsed_retrieval_res_format = _dim_val_similarity_search(query_text_list, query_filter, n_results=n_results)
    except NoIndexException:
        return ret_error(message="索引未找到，数据库可能已被删除")
    return ret_success(parsed_retrieval_res_format)


@app.post("/dim_val_insert")
async def dim_val_insert(insert_list: List[DimValItemInfo]):
    '''
    插入数据
    Args:
        [
          {
            "text": "SUV车辆",
            "dim_column": "type",
            "dim_value": "SUV",
            "model_id": 3,
            "data_source_id" 5
          }
        ]
    Returns: "success"
    '''
    _dim_val_add(insert_list)
    return ret_success()


@app.post("/dim_val_clean")
async def dim_val_clean():
    '''
    清空dim_val向量库
    Returns:

    '''
    _dim_val_clean()
    return ret_success()


@app.post("/func_similarity_search")
async def dim_val_similarity_search(query_text_list: List[str], n_results: int = 1):
    '''
    Args:
        query_text_list: ['总数', "最大值"]
        n_results: 1

    Returns:
        [
          {
            'key': '数量',
            'search': [
               Document(page_content='求数量', metadata={'func_name': 'COUNT', 'func_format': 'COUNT(%s)', 'func_content': '', 'arg_count': 1})
            ]
          },
          {
            'key': '最大值',
            'search': [
              Document(page_content='求最大值', metadata={'func_name': 'MAX', 'func_format': 'MAX(%s)', 'func_content': '', 'arg_count': 1})
            ]
          }
        ]

    '''
    from chromadb.errors import NoIndexException
    try:
        parsed_retrieval_res_format = _func_similarity_search(query_text_list, n_results=n_results)
    except NoIndexException:
        return ret_error(message="索引未找到，数据库可能已被删除")
    return ret_success(parsed_retrieval_res_format)


@app.post("/func_insert")
async def dim_val_insert(insert_list: List[FuncItemInfo]):
    '''
    插入数据
    Args:
        [
              {
                "text": "求平均值",
                "func_name": "AVG",
                "func_format": "AVG(%s)",
                "arg_count": 1,
                "func_content": ""
              }
        ]

    Returns: "success"
    '''
    _func_add(insert_list)
    return ret_success()


@app.post("/func_clean")
async def dim_val_clean():
    '''
    清空cw向量库
    Returns:

    '''
    _func_clean()
    return ret_success()


if __name__ == "__main__":
    uvicorn.run(app, host=LLMPARSER_HOST, port=LLMPARSER_PORT)
