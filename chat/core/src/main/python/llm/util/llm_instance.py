# -*- coding:utf-8 -*-
from langchain.llms import OpenAI
from provider.huggingface import HuggingFaceTextGenInference
from langchain.llms import AzureOpenAI
from run_config import MODEL_NAME, OPENAI_API_KEY, TEMPERATURE
from dotenv import load_dotenv
import json

load_dotenv()

# llm = OpenAI(openai_api_key=OPENAI_API_KEY, model_name=MODEL_NAME,
#              temperature=TEMPERATURE)
llm = AzureOpenAI(
        openai_api_key=OPENAI_API_KEY,
        openai_api_base=OPENAI_API_BASE,
        openai_api_type=OPENAI_API_TYPE,
        openai_api_version=OPENAI_API_VERSION,
        deployment_name=AZURE_DEPLOYMENT_NAME,
        temperature=OPENAI_API_TEMPERATURE,
        stop=json.loads(OPENAI_API_STOP)
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