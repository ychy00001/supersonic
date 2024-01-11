# -*- coding:utf-8 -*-
from typing import Any, List, Mapping, Optional, Union

import chromadb
from chromadb.api import Collection
from chromadb.config import Settings
from langchain.vectorstores import Chroma
from langchain.embeddings.sentence_transformer import SentenceTransformerEmbeddings
from config.run_config import CHROMA_DB_PERSIST_PATH, HF_TEXT2VEC_MODEL_NAME

import os
import sys
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from config.config_parse import CHROMA_DB_PERSIST_PATH


client = chromadb.Client(
    Settings(
        chroma_db_impl="duckdb+parquet",
        persist_directory=CHROMA_DB_PERSIST_PATH,  # Optional, defaults to .chromadb/ in the current directory
    )
)

##################cw_chroma配置，上面的代码为supersonic的对象##########################
def reload_vec_db():
    return Chroma(collection_name="cw_vec", persist_directory=CHROMA_DB_PERSIST_PATH + "_cw",
                    embedding_function=embedding_function)

def reload_func_db():
    return Chroma(collection_name="cw_func", persist_directory=CHROMA_DB_PERSIST_PATH + "_cw",
                    embedding_function=embedding_function)

def reload_dim_val_db():
    return Chroma(collection_name="cw_dim_val", persist_directory=CHROMA_DB_PERSIST_PATH + "_cw",
                    embedding_function=embedding_function)

embedding_function = SentenceTransformerEmbeddings(model_name=HF_TEXT2VEC_MODEL_NAME)

# 维度指标数据库
cw_vec_db = reload_vec_db()
# 运算符数据库
cw_func_db = reload_func_db()
# 维度值数据库
cw_dim_val_db = reload_dim_val_db()

# db.add_texts(['彩虹汽车', '性别', "年龄", "销售价格", "购买时间", "访问历史记录"],
#              [{"meta": "1_1"}, {"meta": "1_2_3"}, {"meta": "1_2_1"}, {"meta": "1_1_3"}, {"meta": "1_2_2"},
#               {"meta": "2_1_1"}])
