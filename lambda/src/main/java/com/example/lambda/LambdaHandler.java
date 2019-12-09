package com.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
public class LambdaHandler implements RequestHandler<SQSEvent, Void> {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    AmazonSQS amazonSqs;

    @SneakyThrows
    public Void handleRequest(SQSEvent event, Context context) {
        final SQSEvent.SQSMessage sqsMessage = event.getRecords().get(0);
        final MyEvent input = objectMapper.readValue(sqsMessage.getBody(), MyEvent.class);
        log.info("Received input={}", input);
        log.info("Processing...");

        MyEvent output = new MyEvent("pong");
        log.info("Sending output={}", output);

        GetQueueUrlResult queueUrl = amazonSqs.getQueueUrl("test-queue-result");
        String messageBody = objectMapper.writeValueAsString(output);
        SendMessageResult resultMessage = amazonSqs.sendMessage(queueUrl.getQueueUrl(), messageBody);
        log.info("Event sent! {}", resultMessage);
        return null;
    }
}
