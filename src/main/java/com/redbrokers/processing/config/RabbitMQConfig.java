package com.redbrokers.processing.config;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public
class RabbitMQConfig {

    private String exchangeName = "Red-Brokers-ReportingMQ";
    private String queueName = "ReportingMQ";
    public static String routeKey = "routeKey";

    @Bean
    CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("rattlesnake.rmq.cloudamqp.com");
        connectionFactory.setUsername("rsfzsoxe");
        connectionFactory.setPassword("G6LYnMqokgkvo1uG7rLDtfRWDyf_a2u7");
        connectionFactory.setVirtualHost("rsfzsoxe");

        //Recommended settings
        connectionFactory.setRequestedHeartBeat(30);
        connectionFactory.setConnectionTimeout(30000);

        return connectionFactory;
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(exchangeName, false, false);
    }

    @Bean
    RabbitTemplate rabbitTemplate(){
        return new RabbitTemplate(connectionFactory());
    }

}