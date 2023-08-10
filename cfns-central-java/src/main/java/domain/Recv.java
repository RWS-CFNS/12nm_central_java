package domain;

import com.rabbitmq.client.Channel;

import com.rabbitmq.client.Connection; //rabbitmq receiver side libraries
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper; //JSON parser conversion
import java.io.*; //for using File objects, print functions

public class Recv {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
    	ObjectMapper objectMapper = new ObjectMapper(); //mapper for JSON conversion
    	Measuringbox_CFNS box = new Measuringbox_CFNS(); //create example box
    	
        ConnectionFactory factory = new ConnectionFactory(); //create connection with rabbitmq
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null); //queue declaration and settings
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> { //callback activated when message available
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8); //receive message
            Measuringbox_CFNS receivedBox = objectMapper.readValue(message, Measuringbox_CFNS.class);	//convert JSON string to class object
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }
}
