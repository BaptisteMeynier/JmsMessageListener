package org.activemq.jms.messagelistener.full;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.activemq.jms.messagelistener.FooMessageListener;
import org.activemq.jms.messagelistener.FooMessageListenerException;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.Test;

public class MessageListenerTest {

	private String BROCKER_URI ="tcp://localhost:61616";


	@Test
    public void trigerMessageListener() throws URISyntaxException, Exception{
        BrokerService broker = BrokerFactory.createBroker(new URI("broker:("+BROCKER_URI+")"));
        broker.start();
        Connection connection = null;
        try {
            // Producer
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROCKER_URI);
            connection = connectionFactory.createConnection();
            connection.setExceptionListener(new FooMessageListenerException());
            Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("customerQueue");
            String payload = "Important Task";
            Message msg = session.createTextMessage(payload);
            MessageProducer producer = session.createProducer(queue);
            System.out.println("Sending text '" + payload + "'");
            producer.send(msg);

            // Consumer
            MessageConsumer consumer = session.createConsumer(queue);
            consumer.setMessageListener(new FooMessageListener("Consumer"));
            connection.start();
            TimeUnit.SECONDS.sleep(5);
            session.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
            broker.stop();
        }
    }

}