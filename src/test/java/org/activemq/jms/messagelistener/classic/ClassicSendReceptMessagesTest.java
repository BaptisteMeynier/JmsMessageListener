package org.activemq.jms.messagelistener.classic;

import java.net.URI;
import java.net.URISyntaxException;
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
import javax.jms.TextMessage;

import org.activemq.jms.messagelistener.constants.JmsConst;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.Test;
import org.junit.Assert;



public class ClassicSendReceptMessagesTest {

	private String  PAYLOAD = "HELLO";
	
	@Test
	public void trigerMessageListener() throws URISyntaxException, Exception{
		BrokerService broker = BrokerFactory.createBroker(new URI("broker:("+JmsConst.BROCKER_URI+")"));
		broker.start();
		Connection connection = null;
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(JmsConst.BROCKER_URI);
			connection = connectionFactory.createConnection();
			connection.start();

			sendMessage(connection);

			receptMessage(connection); 

		} finally {
			if (connection != null) {
				connection.close();
			}
			broker.stop();
		}
	}

	private void sendMessage(Connection connection) throws JMSException, InterruptedException {
		Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("customerQueue");
		MessageProducer producer = session.createProducer(queue);
		Message msg = session.createTextMessage(PAYLOAD);
		producer.send(msg);
		TimeUnit.SECONDS.sleep(1);
		session.close();
	}

	private void receptMessage(Connection connection) throws JMSException {
		Session sessionRecept = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
		Queue queueRecept = sessionRecept.createQueue("customerQueue");
		MessageConsumer consumer = sessionRecept.createConsumer(queueRecept);
		Message recept =consumer.receive(1000);
		String result =((TextMessage) recept).getText();
		System.out.println("Reception:"+result);
		sessionRecept.close();
	}

}
