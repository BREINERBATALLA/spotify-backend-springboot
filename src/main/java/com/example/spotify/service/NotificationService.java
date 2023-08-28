package com.example.spotify.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final SnsClient snsClient;
    @Value("${aws.topic-arn}")
    private String topicArn;

    public void subscribeUser(String userEmail) {
        SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                .topicArn(topicArn)
                .protocol("email")
                .endpoint(userEmail)
                .build();
        snsClient.subscribe(subscribeRequest);
    }
//ver SS clase para enviar los mensajes...
    public void publishMessage(String message, String subject) {
        PublishRequest publishRequest = PublishRequest.builder()
                .topicArn(topicArn)
                .message(message)
                .subject(subject)
                .build();
        snsClient.publish(publishRequest);
    }

}
