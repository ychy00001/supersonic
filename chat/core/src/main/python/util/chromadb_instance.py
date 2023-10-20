# -*- coding:utf-8 -*-
from typing import Any, List, Mapping, Optional, Union

import chromadb
from chromadb.api import Collection, Documents, Embeddings
from chromadb.config import Settings
from langchain.embeddings.sentence_transformer import SentenceTransformerEmbeddings
from langchain.vectorstores import Chroma

from run_config import CHROMA_DB_PERSIST_PATH, HF_TEXT2VEC_MODEL_NAME

client = chromadb.Client(Settings(
    chroma_db_impl="duckdb+parquet",
    persist_directory=CHROMA_DB_PERSIST_PATH  # Optional, defaults to .chromadb/ in the current directory
))


def empty_chroma_collection_2(collection: Collection):
    collection_name = collection.name
    client = collection._client
    metadata = collection.metadata
    embedding_function = collection._embedding_function

    client.delete_collection(collection_name)

    new_collection = client.get_or_create_collection(name=collection_name,
                                                     metadata=metadata,
                                                     embedding_function=embedding_function)

    size_of_new_collection = new_collection.count()

    print(f'Collection {collection_name} emptied. Size of new collection: {size_of_new_collection}')

    return new_collection


def empty_chroma_collection(collection: Collection):
    collection.delete()


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
