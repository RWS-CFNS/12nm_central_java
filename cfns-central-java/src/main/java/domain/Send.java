package domain;

import com.rabbitmq.client.Channel; //rabbitMQ, not on the server side
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.fasterxml.jackson.databind.ObjectMapper; //JSON parser conversion
import java.io.*; //for using File objects, print functions

import java.nio.charset.StandardCharsets;

public class Send {

	
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
    	ObjectMapper objectMapper = new ObjectMapper(); //mapper for JSON conversion
    	Measuringbox_CFNS box = new Measuringbox_CFNS(); //create example box
    	box.setValue1(1);
    	box.setValue2(2);
    	
    	objectMapper.writeValue(new File("target/box.json"), box); //convert box to JSON
    	String boxAsString = objectMapper.writeValueAsString(box); //convert object to string in JSON format
    	//System.out.println(boxAsString ); //display box in console in JSON format
    	
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //String message = "Hello World!";
            channel.basicPublish("", QUEUE_NAME, null, boxAsString.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + boxAsString + "'");
        }
    }
}
