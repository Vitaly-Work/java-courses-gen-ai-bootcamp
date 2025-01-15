# MODULE 3 Work with different models. 
## Task 3. 

## Configuration parameters
### Environment variables:
```
AZURE_OPEN_AI_KEY = <AzureOpenAI API key>
AZURE_OPEN_AI_ENDPOINT = <AzureOpenAI API endpoint>
AZURE_OPEN_AI_DEPLOYMENT_NAME = <AzureOpenAI model name>
AZURE_OPEN_AI_EMBEDDINGS_DEPLOYMENT_NAME = <AzureOpenAI model name for embeddings>
AZURE_OPEN_AI_CHAT_TEMPERATURE = <AzureOpenAI default chat completions temperature (defailt 0.5)
QDRANT_HOST = <Qdrant server host (default localhost)>
QDRANT_PORT = <Qdrant server port (default 6334)>
QDRANT_COLLECTION_NAME = <Collection name on Qdrant server (default demo_collection)>
QDRANT_SEARCH_RESULT_LIMIT = <Limit of entries returned in search result (default 15)>
```
### Prompt execution settings for different deployments
JSON file [deployments-settings.json](src/main/resources/config/deployments-settings.json)
contains set of settings for several deployments.

## Upload documents example:
```
### upload pdf document 
# @timeout 600
POST http://localhost:8080/api/v2/document/upload-pdf
Content-Type: multipart/form-data; boundary=boundary

--boundary
Content-Disposition: form-data; name="pdfFile"; filename="document.pdf"

// The 'document.pdf' file will be uploaded
< ./gen-ai-bootcamp/tasks/gen_ai_training_module6_task6/src/main/resources/documents/artefacts.pdf
```

## Prompt examples and outputs:
```
### prompt
POST http://localhost:8080/api/v2/chat/prompts
Content-Type: application/json

{
  "input": "Please tell me about artefacts discovered in 2028"
}
```
```
{
  "output": [
    "In the year 2028, an artefact called \"The Headdress of Power\" was unearthed. It is a ceremonial headdress made from feathers, beads, and gold. This discovery is significant and provides insights into the cultural practices and beliefs of the past. Unfortunately, I don't have any further information about other artefacts found in 2028."
  ]
}
```
```
{
  "input": "Please tell me about artefacts found in year 1995"
}
```
```
{
  "output": [
    "In 1995, I couldn't find specific information about artifacts discovered. However, here are some artifacts found in other years:\n\n1. \"The Ceremonial Vessel of the Ancients\" - Discovered in 1999, this beautifully crafted ceramic pot is adorned with intricate geometric patterns.\n\n2. \"The Jaguar Stone\" - Unearthed in 2003, this artifact holds significance, although I don't have further details about it.\n\nThese artifacts, along with others, provide a fascinating glimpse into the lives, culture, and beliefs of the people who once thrived in the depths of the Amazon. Each artifact tells a unique story, offering insights into the daily lives and spiritual beliefs of these ancient civilizations.\n\nI apologize for not having specific information about artifacts found in 1995."
  ]
}
```
