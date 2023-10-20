# -*- coding:utf-8 -*-
from langchain.llms import OpenAI
from provider.huggingface import HuggingFaceTextGenInference
from langchain.llms import AzureOpenAI
from run_config import MODEL_NAME, OPENAI_API_KEY, TEMPERATURE,ENV_CONFIG
import json

# llm = OpenAI(openai_api_key=OPENAI_API_KEY, model_name=MODEL_NAME,
#              temperature=TEMPERATURE)
llm = AzureOpenAI(
        openai_api_key=ENV_CONFIG['OPENAI_API_KEY'],
        openai_api_base=ENV_CONFIG['OPENAI_API_BASE'],
        openai_api_type=ENV_CONFIG['OPENAI_API_TYPE'],
        openai_api_version=ENV_CONFIG['OPENAI_API_VERSION'],
        deployment_name=ENV_CONFIG['AZURE_DEPLOYMENT_NAME'],
        temperature=ENV_CONFIG['OPENAI_API_TEMPERATURE'],
        stop=json.loads(ENV_CONFIG['OPENAI_API_STOP'])
    )


# llm = HuggingFaceTextGenInference(
#         inference_server_url="http://123.249.78.135:8528/",
#         max_new_tokens=600,
#         top_k=50,
#         top_p=0.7,
#         temperature=0.1,
#         do_sample=False,
# #         truncate=800,
#         repetition_penalty=1.0,
#         stop_sequences=["\n\nTable", "\n\n问题"]
#     )