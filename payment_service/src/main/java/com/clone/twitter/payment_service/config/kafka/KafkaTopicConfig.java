package com.clone.twitter.payment_service.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.data.channel.payment.new.name}")
    private String newPaymentTopicName;

    @Value("${spring.data.channel.payment.cancel.name}")
    private String cancelPaymentTopicName;

    @Value("${spring.data.channel.payment.clear.name}")
    private String clearPaymentTopicName;

    @Bean
    public NewTopic newPaymentTopic() {
        return new NewTopic(newPaymentTopicName, 1, (short) 1);
    }

    @Bean
    public NewTopic cancelPaymentTopic() {
        return new NewTopic(cancelPaymentTopicName, 1, (short) 1);
    }

    @Bean
    public NewTopic clearPaymentTopic() {
        return new NewTopic(clearPaymentTopicName, 1, (short) 1);
    }
}
