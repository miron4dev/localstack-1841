package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class LambdaSqsListener {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @SqsListener(value = "test-queue-result", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void threatModelResultArrived(MyEvent result) {
        log.info("Received event={} from lambda", result);
    }
}
