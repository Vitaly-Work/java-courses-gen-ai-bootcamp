# MODULE 3 Work with different models. 
## Task 3. 

## Configuration parameters
### Environment variables:
```
AZURE_OPEN_AI_KEY = <AzureOpenAI API key>
AZURE_OPEN_AI_ENDPOINT = <AzureOpenAI API endpoint>
AZURE_OPEN_AI_DEPLOYMENT_NAME = <AzureOpenAI model name>
AZURE_OPEN_AI_CHAT_TEMPERATURE = <AzureOpenAI default chat completions temperature (defailt 0.5)
```
### Prompt execution settings for different deployments
JSON file [deployments-settings.json](src/main/resources/config/deployments-settings.json)
contains set of settings for several deployments.

## Implementation of Model Switching. Deployments Controller

The `DeploymentsController` is a RESTful controller to manage deployment-related operations. 
It provides a set of APIs under the `/api/v2/deployments` path, allowing clients to interact with deployment configurations and operations.

### Endpoints
#### 1. GET `/api/v2/deployments`
This endpoint retrieves a list of all current deployments. 
It returns a collection of `DeploymentDto` objects containing id and model of deployment.

**Usage:**
```bash
curl -X GET http://localhost:8080/api/v2/deployments
```

#### 2. GET `/api/v2/deployments/update`
This endpoint triggers an update operation on the deployments from external API source to in-memory storage.

**Usage:**
```bash
curl -X GET http://localhost:8080/api/v2/deployments/update
```

#### 3. GET `/api/v2/deployments/switch`
This endpoint allows switching the active deployment by specifying a deployment name. 
It takes a query parameter deploymentName which specifies the name of the deployment to be activated.
Given deployment name is validated against current list of deployments stored in memory. 
Current chat history is cleared.

**Usage:**
```bash
curl -X GET "http://localhost:8080/api/v2/deployments/switch?deploymentName=<name>"
```

### Comparison of results generated with different models and **PromptExecutionSettings**
### Test1. Model **gpt-35-turbo**
#### PromptExecutionSettings:
```
    "gpt-35-turbo": {
      "temperature": 1.0,
      "maxTokens": 150
    }
```
#### Prompt:
```
{
"input": "Continue this story: An old sailor returns to the sea to catch a mythical fish that escaped him years ago..."
}
```
#### AI Response:
```
{
  "output": [
    "Once upon a time, in a small village surrounded by lush, green fields, there lived a curious little cat named Willow. Willow had always dreamt of exploring the world beyond the village, yearning for something more magical. One cloudy morning, as the first rays of sunlight peeked through the dense fog, Willow stumbled upon a hidden entrance to an enchanted garden.\n\nAs soon as her paws touched the mystical garden's soft, velvety grass, she noticed an otherworldly glow emanating"
  ]
}
```

### Test2. Model **gemini-pro**
#### PromptExecutionSettings:
```
    "gemini-pro": {
      "temperature": 0.3,
      "maxTokens": 50
    }
```
#### Prompt:
```
{
"input": "Continue this story: An old sailor returns to the sea to catch a mythical fish that escaped him years ago..."
}
```
#### AI Response:
```
{
  "output": [
    "An old sailor named Captain Jonas stood on the weathered wooden dock, his eyes fixed on the vast expanse of the sea. The salty breeze tousled his silver hair, and his hands, calloused from years of gripping the helm, clenched"
  ]
}
```
