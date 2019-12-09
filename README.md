## Test project to reproduce https://github.com/localstack/localstack/issues/1841

### How to reproduce

- Execute `docker-compose up` to build and deploy Localstack container with AWS Lambda and SQS services (with `test-queue-trigger` and `test-queue-result` SQS queues)
- Execute `build.sh` to build communicator and lambda applications
- Execute `deploy_lambda.sh` to deploy lambda to Localstack and create event source mapping to forward the SQS messages from `test-queue-trigger` queue to lambda
- Execute `deploy_communicator.sh` to deploy java application that sends the messages to `test-queue-trigger` queue and listens the messages from `test-queue-result` queue.
- Open http://localhost:9999/ping via any browser.

### Expected behavior

- `/ping` service sends "ping" message to `test-queue-trigger` queue. 
- The message has to be forwarded to deployed lambda because of created event source mapping
- Lambda has to accept this message and send "pong" message to `test-queue-result` queue
- The listener on `communicator` side has to accept this message and write to server log

### Actual behavior

```
com.amazonaws.AmazonClientException: MD5 returned by SQS does not match the calculation on the original request. (MD5 calculated by the message attributes: "5d60536a7c478bb7ab29a6d3978e9e20", MD5 checksum returned: "1298670363fdc69559e2ae7e52a35bc7")
	at com.amazonaws.services.sqs.MessageMD5ChecksumHandler.sendMessageOperationMd5Check(MessageMD5ChecksumHandler.java:120) ~[aws-java-sdk-sqs-1.11.415.jar:na]
	at com.amazonaws.services.sqs.MessageMD5ChecksumHandler.afterResponse(MessageMD5ChecksumHandler.java:80) ~[aws-java-sdk-sqs-1.11.415.jar:na]
	at com.amazonaws.handlers.RequestHandler2Adaptor.afterResponse(RequestHandler2Adaptor.java:49) ~[aws-java-sdk-core-1.11.415.jar:na]
	at com.amazonaws.http.AmazonHttpClient$RequestExecutor.afterResponse(AmazonHttpClient.java:991) ~[aws-java-sdk-core-1.11.415.jar:na]
	at com.amazonaws.http.AmazonHttpClient$RequestExecutor.doExecute(AmazonHttpClient.java:749) ~[aws-java-sdk-core-1.11.415.jar:na]
	at com.amazonaws.http.AmazonHttpClient$RequestExecutor.executeWithTimer(AmazonHttpClient.java:719) ~[aws-java-sdk-core-1.11.415.jar:na]
	at com.amazonaws.http.AmazonHttpClient$RequestExecutor.execute(AmazonHttpClient.java:701) ~[aws-java-sdk-core-1.11.415.jar:na]
	at com.amazonaws.http.AmazonHttpClient$RequestExecutor.access$500(AmazonHttpClient.java:669) ~[aws-java-sdk-core-1.11.415.jar:na]
	at com.amazonaws.http.AmazonHttpClient$RequestExecutionBuilderImpl.execute(AmazonHttpClient.java:651) ~[aws-java-sdk-core-1.11.415.jar:na]
	at com.amazonaws.http.AmazonHttpClient.execute(AmazonHttpClient.java:515) ~[aws-java-sdk-core-1.11.415.jar:na]
	at com.amazonaws.services.sqs.AmazonSQSClient.doInvoke(AmazonSQSClient.java:2147) ~[aws-java-sdk-sqs-1.11.415.jar:na]
	at com.amazonaws.services.sqs.AmazonSQSClient.invoke(AmazonSQSClient.java:2116) ~[aws-java-sdk-sqs-1.11.415.jar:na]
	at com.amazonaws.services.sqs.AmazonSQSClient.invoke(AmazonSQSClient.java:2105) ~[aws-java-sdk-sqs-1.11.415.jar:na]
	at com.amazonaws.services.sqs.AmazonSQSClient.executeSendMessage(AmazonSQSClient.java:1692) ~[aws-java-sdk-sqs-1.11.415.jar:na]
	at com.amazonaws.services.sqs.AmazonSQSClient.sendMessage(AmazonSQSClient.java:1664) ~[aws-java-sdk-sqs-1.11.415.jar:na]
	at org.springframework.cloud.aws.messaging.core.QueueMessageChannel.sendMessageAndWaitForResult(QueueMessageChannel.java:135) ~[spring-cloud-aws-messaging-2.2.0.RELEASE.jar:2.2.0.RELEASE]
	at org.springframework.cloud.aws.messaging.core.QueueMessageChannel.sendInternal(QueueMessageChannel.java:77) ~[spring-cloud-aws-messaging-2.2.0.RELEASE.jar:2.2.0.RELEASE]
	at org.springframework.messaging.support.AbstractMessageChannel.send(AbstractMessageChannel.java:136) ~[spring-messaging-5.2.1.RELEASE.jar:5.2.1.RELEASE]
	at org.springframework.messaging.support.AbstractMessageChannel.send(AbstractMessageChannel.java:122) ~[spring-messaging-5.2.1.RELEASE.jar:5.2.1.RELEASE]
	at org.springframework.cloud.aws.messaging.core.support.AbstractMessageChannelMessagingSendingTemplate.doSend(AbstractMessageChannelMessagingSendingTemplate.java:67) ~[spring-cloud-aws-messaging-2.2.0.RELEASE.jar:2.2.0.RELEASE]
	at org.springframework.cloud.aws.messaging.core.support.AbstractMessageChannelMessagingSendingTemplate.doSend(AbstractMessageChannelMessagingSendingTemplate.java:44) ~[spring-cloud-aws-messaging-2.2.0.RELEASE.jar:2.2.0.RELEASE]
	at org.springframework.messaging.core.AbstractMessageSendingTemplate.send(AbstractMessageSendingTemplate.java:109) ~[spring-messaging-5.2.1.RELEASE.jar:5.2.1.RELEASE]
	at org.springframework.messaging.core.AbstractMessageSendingTemplate.convertAndSend(AbstractMessageSendingTemplate.java:151) ~[spring-messaging-5.2.1.RELEASE.jar:5.2.1.RELEASE]
	at org.springframework.messaging.core.AbstractMessageSendingTemplate.convertAndSend(AbstractMessageSendingTemplate.java:129) ~[spring-messaging-5.2.1.RELEASE.jar:5.2.1.RELEASE]
	at org.springframework.messaging.core.AbstractMessageSendingTemplate.convertAndSend(AbstractMessageSendingTemplate.java:122) ~[spring-messaging-5.2.1.RELEASE.jar:5.2.1.RELEASE]
	at org.springframework.cloud.aws.messaging.core.support.AbstractMessageChannelMessagingSendingTemplate.convertAndSend(AbstractMessageChannelMessagingSendingTemplate.java:81) ~[spring-cloud-aws-messaging-2.2.0.RELEASE.jar:2.2.0.RELEASE]
	at demo.Controller.sendEvent(Controller.java:22) ~[classes/:na]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_231]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_231]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_231]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_231]
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190) ~[spring-web-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138) ~[spring-web-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:106) ~[spring-webmvc-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:888) ~[spring-webmvc-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:793) ~[spring-webmvc-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87) ~[spring-webmvc-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1040) ~[spring-webmvc-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:943) ~[spring-webmvc-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006) ~[spring-webmvc-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898) ~[spring-webmvc-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:634) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883) ~[spring-webmvc-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:741) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53) ~[tomcat-embed-websocket-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[spring-web-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93) ~[spring-web-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[spring-web-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:526) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:139) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:367) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:860) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1591) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149) [na:1.8.0_231]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624) [na:1.8.0_231]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at java.lang.Thread.run(Thread.java:748) [na:1.8.0_231]


```