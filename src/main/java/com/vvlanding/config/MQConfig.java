//package com.vvlanding.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class MQConfig {
//
////    Local
//    public static final String QUEUE = "message_queue_Local12x12321";
//    public static final String EXCHANGE = "message_exchange_Local13x12312";
//    public static final String ROUTING_KEY = "message_routingKey_Loal1231123x2";
//
//    public static final String QUEUE_TEXT = "message_queue_text_Loca212313x2";
//    public static final String EXCHANGE_TEXT = "message_exchange_tex_Local1112323x2";
//    public static final String ROUTING_KEY_TEXT = "message_routingKeytext_Local1232123x2";
//
//    public static final String QUEUE_IMAGE = "message_queue_image_Local144231x2";
//    public static final String EXCHANGE_IMAGE = "message_exchange_image_Lcal1231332x2";
//    public static final String ROUTING_KEY_IMAGE = "message_routingKey_imag_Local122123x2";
//
//    public static final String QUEUE_WEBHOOKSHOPEE = "message_queue_webhookShopee_Lcal1233112x2";
//    public static final String EXCHANGE_WEBHOOKSHOPEE = "message_exchange_webhookSopee_Local1123323x2";
//    public static final String ROUTING_KEY_WEBHOOKSHOPEE = "message_routingKey_webhokShopee_Local1121233x2";
//
//    //Server
////    public static final String QUEUE = "message_queue_Server167";
////    public static final String EXCHANGE = "message_exchange_Server167";
////    public static final String ROUTING_KEY = "message_routingKey_Server167";
////
////    public static final String QUEUE_TEXT = "message_queue_text_Server167";
////    public static final String EXCHANGE_TEXT = "message_exchange_text_Server167";
////    public static final String ROUTING_KEY_TEXT = "message_routingKey_text_Server167";
////
////    public static final String QUEUE_IMAGE = "message_queue_image_Server167";
////    public static final String EXCHANGE_IMAGE = "message_exchange_image_Server167";
////    public static final String ROUTING_KEY_IMAGE = "message_routingKey_image_Server167";
////
////    public static final String QUEUE_WEBHOOKSHOPEE = "message_queue_webhookShopee_Server167";
////    public static final String EXCHANGE_WEBHOOKSHOPEE = "message_exchange_webhookShopee_Server167";
////    public static final String ROUTING_KEY_WEBHOOKSHOPEE = "message_routingKey_webhookShopee_Server167";
//
//    @Bean
//    public Queue queue() {
//        return  new Queue(QUEUE);
//    }
//
//    @Bean
//    public TopicExchange exchange() {
//        return new TopicExchange(EXCHANGE);
//    }
//
//    @Bean
//    public Binding binding(Queue queue, TopicExchange exchange) {
//        return BindingBuilder
//                .bind(queue)
//                .to(exchange)
//                .with(ROUTING_KEY);
//    }
//
//
//    @Bean
//    public Queue queueText() {
//        return  new Queue(QUEUE_TEXT);
//    }
//
//    @Bean
//    public TopicExchange exchangeText() {
//        return new TopicExchange(EXCHANGE_TEXT);
//    }
//
//    @Bean
//    public Binding bindingText(Queue queueText, TopicExchange exchangeText) {
//        return BindingBuilder
//                .bind(queueText)
//                .to(exchangeText)
//                .with(ROUTING_KEY_TEXT);
//    }
//
//
//    @Bean
//    public Queue queueImage() {
//        return  new Queue(QUEUE_IMAGE);
//    }
//
//    @Bean
//    public TopicExchange exchangeImage() {
//        return new TopicExchange(EXCHANGE_IMAGE);
//    }
//
//    @Bean
//    public Binding bindingImage(Queue queueImage, TopicExchange exchangeImage) {
//        return BindingBuilder
//                .bind(queueImage)
//                .to(exchangeImage)
//                .with(ROUTING_KEY_IMAGE);
//    }
//
//
//    @Bean
//    public Queue queueWebhook() {
//        return  new Queue(QUEUE_WEBHOOKSHOPEE);
//    }
//
//    @Bean
//    public TopicExchange exchangeWebhook() {
//        return new TopicExchange(EXCHANGE_WEBHOOKSHOPEE);
//    }
//
//    @Bean
//    public Binding bindingWebhook(Queue queueWebhook, TopicExchange exchangeWebhook) {
//        return BindingBuilder
//                .bind(queueWebhook)
//                .to(exchangeWebhook)
//                .with(ROUTING_KEY_WEBHOOKSHOPEE);
//    }
//
//
//    @Bean
//    public MessageConverter messageConverter() {
//        return  new Jackson2JsonMessageConverter();
//    }
//
//    @Bean
//    public AmqpTemplate template(ConnectionFactory connectionFactory) {
//        RabbitTemplate template = new RabbitTemplate(connectionFactory);
//        template.setMessageConverter(messageConverter());
//        return  template;
//    }
//}
