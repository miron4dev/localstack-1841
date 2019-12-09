#!/bin/bash

aws --endpoint-url=http://localhost:4574 lambda create-function \
    --function-name TestLambda \
    --zip-file fileb://lambda/target/lambda-0.0.1-SNAPSHOT-runner.jar \
    --role role-arn \
    --handler io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest --runtime java8

# aws --endpoint-url http://localhost:4576 sqs get-queue-attributes --queue-url http://localhost:4576/queue/test-queue-trigger --attribute-names All

aws --endpoint-url http://localhost:4574 lambda create-event-source-mapping --function-name TestLambda --batch-size 1 --event-source-arn arn:aws:sqs:us-east-1:000000000000:test-queue-trigger