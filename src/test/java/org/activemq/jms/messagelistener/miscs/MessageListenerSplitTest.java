package org.activemq.jms.messagelistener.miscs;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessageListenerSplitTest {


	private static BrokerService broker;

	private Connection connection;
	@BeforeClass
	public static void init() throws Exception {
		broker = BrokerFactory.createBroker(new URI("broker:("+JmsConst.BROCKER_URI+")"));
		broker.start();

	}

	@AfterClass
	public static void tearDown() throws Exception {
		broker.stop();
	}

	@Before
	public void initConnection() throws JMSException {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(JmsConst.BROCKER_URI);
		connection = connectionFactory.createConnection();
		connection.setExceptionListener(new FooMessageListenerException());
		connection.start();
	}

	@After
	public void closeConnection() throws JMSException {
		if (connection != null) {
			connection.close();
		}
	}

	@Test
	public void test1_sendMessage() throws URISyntaxException, Exception{
		Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("customerQueue");
		String payload = "Important Task";
		Message msg = session.createTextMessage(payload);
		MessageProducer producer = session.createProducer(queue);
		System.out.println("Sending text '" + payload + "'");
		producer.send(msg);

	}

	@Test
	public void test2_receiveMessage() throws URISyntaxException, Exception{
		Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("customerQueue");
		MessageConsumer consumer = session.createConsumer(queue);
		consumer.setMessageListener(new FooMessageListener("Consumer"));
		TimeUnit.SECONDS.sleep(5);
	}


	


}