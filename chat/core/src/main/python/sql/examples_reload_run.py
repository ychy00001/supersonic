# -*- coding:utf-8 -*-
from typing import Any, List, Mapping, Optional, Union
import os
import sys
import requests
import json

sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from run_config import TEXT2DSL_FEW_SHOTS_EXAMPLE_NUM
from few_shot_example.sql_exampler import examplars as sql_examplars
from run_config import LLMPARSER_HOST
from run_config import LLMPARSER_PORT


def text2dsl_setting_update(llm_parser_host:str, llm_parser_port:str,
        sql_examplars:List[Mapping[str, str]], example_nums:int):

    url = f"http://{llm_parser_host}:{llm_parser_port}/query2sql_setting_update/"
    print("url: ", url)
    payload = {"sqlExamplars":sql_examplars, "exampleNums":example_nums}
    headers = {'content-type': 'application/json'}
    response = requests.post(url, data=json.dumps(payload), headers=headers)
    print(response.text)


if __name__ == "__main__":
    arguments = sys.argv
    text2dsl_setting_update(LLMPARSER_HOST, LLMPARSER_PORT,
                            sql_examplars, TEXT2DSL_FEW_SHOTS_EXAMPLE_NUM)
