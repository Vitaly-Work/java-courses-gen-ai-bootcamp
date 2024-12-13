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
#### 1. GET `/api/v2/chat/prompts`

**Usage:**
POST http://localhost:8080/api/v2/chat/prompts
Content-Type: application/json
```
{
"input": "Please calculate the age based on the birth date: year 2001, month 09, and day 12"
}
```

### Testing function calling
#### Application logs:
```

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.3.5)

INFO 45176 --- [           main] c.e.t.gen.ai.GenAiTrainingApplication    : Starting GenAiTrainingApplication using Java 18.0.1.1 with PID 45176 (C:\Projects\Learn\GetAiJava\git\java-courses\gen-ai-bootcamp\tasks\gen_ai_training_module4_task4\target\classes started by Vitalii_Adoievtsev in C:\Projects\Learn\GetAiJava\git\java-courses)
INFO 45176 --- [           main] c.e.t.gen.ai.GenAiTrainingApplication    : No active profile set, falling back to 1 default profile: "default"
INFO 45176 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
INFO 45176 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
INFO 45176 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.31]
INFO 45176 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
INFO 45176 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 2110 ms
INFO 45176 --- [           main] c.a.c.h.n.implementation.NettyUtility    : {"az.sdk.message":"The following Netty versions were found on the classpath and have a mismatch with the versions used by azure-core-http-netty. If your application runs without issue this message can be ignored, otherwise please align the Netty versions used in your application. For more information, see https://aka.ms/azsdk/java/dependency/troubleshoot.","azure-netty-version":"4.1.110.Final","azure-netty-native-version":"2.0.65.Final","classpath-netty-version-io.netty:netty-common":"4.1.114.Final","classpath-netty-version-io.netty:netty-handler":"4.1.114.Final","classpath-netty-version-io.netty:netty-handler-proxy":"4.1.114.Final","classpath-netty-version-io.netty:netty-buffer":"4.1.114.Final","classpath-netty-version-io.netty:netty-codec":"4.1.114.Final","classpath-netty-version-io.netty:netty-codec-http":"4.1.114.Final","classpath-netty-version-io.netty:netty-codec-http2":"4.1.114.Final","classpath-netty-version-io.netty:netty-transport-native-unix-common":"4.1.114.Final","classpath-netty-version-io.netty:netty-transport-native-epoll":"4.1.114.Final","classpath-netty-version-io.netty:netty-transport-native-kqueue":"4.1.114.Final","classpath-native-netty-version-io.netty:netty-tcnative-boringssl-static":"2.0.66.Final"}
INFO 45176 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
INFO 45176 --- [           main] c.e.t.gen.ai.GenAiTrainingApplication    : Started GenAiTrainingApplication in 4.804 seconds (process running for 6.216)
INFO 45176 --- [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
INFO 45176 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
INFO 45176 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 14 ms
INFO 45176 --- [nio-8080-exec-1] c.e.t.g.a.s.PromptWithHistoryService     : Request>>>>>>>> 
 What lights are on?
INFO 45176 --- [oundedElastic-1] c.e.t.g.ai.service.plugins.LightsPlugin  : Function 'get_lights' called with result: LightsList(lights=[LightModel(id=1, name=Table Lamp, isOn=false), LightModel(id=2, name=Porch light, isOn=false), LightModel(id=3, name=Chandelier, isOn=true)])
INFO 45176 --- [nio-8080-exec-1] c.e.t.g.a.s.PromptWithHistoryService     : BEGIN Protocol of prompt =========================================== 

INFO 45176 --- [nio-8080-exec-1] c.e.t.g.a.s.PromptWithHistoryService     : Assistant requested tool: 
 Plugin function: LightsPlugin.get_lights 
 with arguments: {}
INFO 45176 --- [nio-8080-exec-1] c.e.t.g.a.s.PromptWithHistoryService     : Tool response: 
 LightsList(lights=[LightModel(id=1, name=Table Lamp, isOn=false), LightModel(id=2, name=Porch light, isOn=false), LightModel(id=3, name=Chandelier, isOn=true)])
INFO 45176 --- [nio-8080-exec-1] c.e.t.g.a.s.PromptWithHistoryService     : Response<<<<<<<< 
 The Chandelier light is currently on.
INFO 45176 --- [nio-8080-exec-1] c.e.t.g.a.s.PromptWithHistoryService     : END   Protocol of prompt =========================================== 

INFO 45176 --- [nio-8080-exec-2] c.e.t.g.a.s.PromptWithHistoryService     : Request>>>>>>>> 
 What lights are off?
INFO 45176 --- [nio-8080-exec-2] c.e.t.g.a.s.PromptWithHistoryService     : BEGIN Protocol of prompt =========================================== 

INFO 45176 --- [nio-8080-exec-2] c.e.t.g.a.s.PromptWithHistoryService     : Response<<<<<<<< 
 The Table Lamp and Floor Lamp lights are currently off.
INFO 45176 --- [nio-8080-exec-2] c.e.t.g.a.s.PromptWithHistoryService     : END   Protocol of prompt =========================================== 

INFO 45176 --- [nio-8080-exec-3] c.e.t.g.a.s.PromptWithHistoryService     : Request>>>>>>>> 
 Change the state of the light with name = "Chandelier" to off
INFO 45176 --- [oundedElastic-1] c.e.t.g.ai.service.plugins.LightsPlugin  : Function 'get_lights' called with result: LightsList(lights=[LightModel(id=1, name=Table Lamp, isOn=false), LightModel(id=2, name=Porch light, isOn=false), LightModel(id=3, name=Chandelier, isOn=true)])
INFO 45176 --- [oundedElastic-1] c.e.t.g.ai.service.plugins.LightsPlugin  : Function 'change_state_of_light_by_id' called with arguments: 3, false
INFO 45176 --- [nio-8080-exec-3] c.e.t.g.a.s.PromptWithHistoryService     : BEGIN Protocol of prompt =========================================== 

INFO 45176 --- [nio-8080-exec-3] c.e.t.g.a.s.PromptWithHistoryService     : Assistant requested tool: 
 Plugin function: LightsPlugin.get_lights 
 with arguments: {}
INFO 45176 --- [nio-8080-exec-3] c.e.t.g.a.s.PromptWithHistoryService     : Tool response: 
 LightsList(lights=[LightModel(id=1, name=Table Lamp, isOn=false), LightModel(id=2, name=Porch light, isOn=false), LightModel(id=3, name=Chandelier, isOn=true)])
INFO 45176 --- [nio-8080-exec-3] c.e.t.g.a.s.PromptWithHistoryService     : Assistant requested tool: 
 Plugin function: LightsPlugin.change_state_of_light_by_id 
 with arguments: {ison=false, id=3}
INFO 45176 --- [nio-8080-exec-3] c.e.t.g.a.s.PromptWithHistoryService     : Tool response: 
 LightModel(id=3, name=Chandelier, isOn=false)
INFO 45176 --- [nio-8080-exec-3] c.e.t.g.a.s.PromptWithHistoryService     : Response<<<<<<<< 
 The state of the Chandelier light has been changed to off.
INFO 45176 --- [nio-8080-exec-3] c.e.t.g.a.s.PromptWithHistoryService     : END   Protocol of prompt =========================================== 

INFO 45176 --- [nio-8080-exec-4] c.e.t.g.a.s.PromptWithHistoryService     : Request>>>>>>>> 
 Turn on all the lights one by one and tell me the resulting status of lights
INFO 45176 --- [oundedElastic-1] c.e.t.g.ai.service.plugins.LightsPlugin  : Function 'get_lights' called with result: LightsList(lights=[LightModel(id=1, name=Table Lamp, isOn=false), LightModel(id=2, name=Porch light, isOn=false), LightModel(id=3, name=Chandelier, isOn=false)])
INFO 45176 --- [oundedElastic-1] c.e.t.g.ai.service.plugins.LightsPlugin  : Function 'change_state_of_light_by_id' called with arguments: 1, true
INFO 45176 --- [oundedElastic-1] c.e.t.g.ai.service.plugins.LightsPlugin  : Function 'change_state_of_light_by_id' called with arguments: 2, true
INFO 45176 --- [oundedElastic-1] c.e.t.g.ai.service.plugins.LightsPlugin  : Function 'change_state_of_light_by_id' called with arguments: 3, true
INFO 45176 --- [oundedElastic-1] c.e.t.g.ai.service.plugins.LightsPlugin  : Function 'get_lights' called with result: LightsList(lights=[LightModel(id=1, name=Table Lamp, isOn=true), LightModel(id=2, name=Porch light, isOn=true), LightModel(id=3, name=Chandelier, isOn=true)])
INFO 45176 --- [nio-8080-exec-4] c.e.t.g.a.s.PromptWithHistoryService     : BEGIN Protocol of prompt =========================================== 

INFO 45176 --- [nio-8080-exec-4] c.e.t.g.a.s.PromptWithHistoryService     : Assistant requested tool: 
 Plugin function: LightsPlugin.get_lights 
 with arguments: {}
INFO 45176 --- [nio-8080-exec-4] c.e.t.g.a.s.PromptWithHistoryService     : Tool response: 
 LightsList(lights=[LightModel(id=1, name=Table Lamp, isOn=false), LightModel(id=2, name=Porch light, isOn=false), LightModel(id=3, name=Chandelier, isOn=false)])
INFO 45176 --- [nio-8080-exec-4] c.e.t.g.a.s.PromptWithHistoryService     : Assistant requested tool: 
 Plugin function: LightsPlugin.change_state_of_light_by_id 
 with arguments: {ison=true, id=1}
INFO 45176 --- [nio-8080-exec-4] c.e.t.g.a.s.PromptWithHistoryService     : Tool response: 
 LightModel(id=1, name=Table Lamp, isOn=true)
INFO 45176 --- [nio-8080-exec-4] c.e.t.g.a.s.PromptWithHistoryService     : Assistant requested tool: 
 Plugin function: LightsPlugin.change_state_of_light_by_id 
 with arguments: {ison=true, id=2}
INFO 45176 --- [nio-8080-exec-4] c.e.t.g.a.s.PromptWithHistoryService     : Tool response: 
 LightModel(id=2, name=Porch light, isOn=true)
INFO 45176 --- [nio-8080-exec-4] c.e.t.g.a.s.PromptWithHistoryService     : Assistant requested tool: 
 Plugin function: LightsPlugin.change_state_of_light_by_id 
 with arguments: {ison=true, id=3}
INFO 45176 --- [nio-8080-exec-4] c.e.t.g.a.s.PromptWithHistoryService     : Tool response: 
 LightModel(id=3, name=Chandelier, isOn=true)
INFO 45176 --- [nio-8080-exec-4] c.e.t.g.a.s.PromptWithHistoryService     : Assistant requested tool: 
 Plugin function: LightsPlugin.get_lights 
 with arguments: {}
INFO 45176 --- [nio-8080-exec-4] c.e.t.g.a.s.PromptWithHistoryService     : Tool response: 
 LightsList(lights=[LightModel(id=1, name=Table Lamp, isOn=true), LightModel(id=2, name=Porch light, isOn=true), LightModel(id=3, name=Chandelier, isOn=true)])
INFO 45176 --- [nio-8080-exec-4] c.e.t.g.a.s.PromptWithHistoryService     : Response<<<<<<<< 
 The resulting status of the lights is as follows:
- Table Lamp: On
- Porch light: On
- Chandelier: On
INFO 45176 --- [nio-8080-exec-4] c.e.t.g.a.s.PromptWithHistoryService     : END   Protocol of prompt =========================================== 

INFO 45176 --- [nio-8080-exec-6] c.e.t.g.a.s.PromptWithHistoryService     : Request>>>>>>>> 
 Please calculate the age based on the birth date: year 2001, month 09, and day 12
INFO 45176 --- [oundedElastic-1] c.e.t.g.a.s.plugins.AgeCalculatorPlugin  : Function 'calculate_age' called with arguments: 2001, 9, 12
INFO 45176 --- [nio-8080-exec-6] c.e.t.g.a.s.PromptWithHistoryService     : BEGIN Protocol of prompt =========================================== 

INFO 45176 --- [nio-8080-exec-6] c.e.t.g.a.s.PromptWithHistoryService     : Assistant requested tool: 
 Plugin function: AgeCalculatorPlugin.calculate_age 
 with arguments: {month=09, year=2001, day=12}
INFO 45176 --- [nio-8080-exec-6] c.e.t.g.a.s.PromptWithHistoryService     : Tool response: 
 Calculated age: 23 years, 3 months, and 1 days
INFO 45176 --- [nio-8080-exec-6] c.e.t.g.a.s.PromptWithHistoryService     : Response<<<<<<<< 
 The calculated age based on the birth date (year 2001, month 09, and day 12) is 23 years, 3 months, and 1 day.
INFO 45176 --- [nio-8080-exec-6] c.e.t.g.a.s.PromptWithHistoryService     : END   Protocol of prompt =========================================== 
```

#### Prompt1:
```
{
  "input": "What lights are on?"
}
```
#### Prompt2:
```
{
  "input": "What lights are off?"
}
```
#### Prompt3:
```
{
  "input": "Change the state of the light with name = \"Chandelier\" to off"
}
```
#### Prompt4:
```
{
  "input": "Turn on all the lights one by one and tell me the resulting status of lights"
}
```
#### Prompt5:
```
{
  "input": "Please calculate the age based on the birth date: year 2001, month 09, and day 12"
}
```

