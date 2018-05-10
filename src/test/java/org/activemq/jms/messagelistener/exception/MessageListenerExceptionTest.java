package org.activemq.jms.messagelistener.exception;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.activemq.jms.messagelistener.FooMessageListener;
import org.activemq.jms.messagelistener.FooMessageListenerException;
import org.activemq.jms.messagelistener.constants.JmsConst;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;

import org.junit.runners.MethodSorters;


public class MessageListenerExceptionTest {

	@Test
    public void trigerMessageListener() throws URISyntaxException, Exception{
        BrokerService broker = BrokerFactory.createBroker(new URI("broker:("+JmsConst.BROCKER_URI+")"));
        broker.start();
        Connection connection = null;
        try {
            // Producer
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(JmsConst.BROCKER_URI);
            connection = connectionFactory.createConnection();
            connection.setExceptionListener(new FooMessageListenerException());
            Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("customerQueue");
            String payload = "11111";
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
        }catch(Exception e) {e.printStackTrace();System.out.println("coucou");}
        finally {
            if (connection != null) {
                connection.close();
            }
            broker.stop();
        }
    }

}