package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @GetMapping("/ping")
    public String sendEvent() {
        log.info("Sending event to SQS queue....");
        MyEvent event = new MyEvent("ping");
        queueMessagingTemplate.convertAndSend("test-queue-trigger", event);
        return "OK";
    }
}
