package org.activemq.jms.messagelistener;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.IntStream;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.activemq.jms.messagelistener.FooMessageListenerException;
import org.activemq.jms.messagelistener.constants.JmsConst;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.Test;

public class SendMessagesTest {


	@Test
	public void trigerMessageListener() throws URISyntaxException, Exception{
		BrokerService broker = BrokerFactory.createBroker(new URI("broker:("+JmsConst.BROCKER_URI+")"));
		broker.start();
		Connection connection = null;
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(JmsConst.BROCKER_URI);
			connection = connectionFactory.createConnection();
			connection.setExceptionListener(new FooMessageListenerException());
			connection.start();
			Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue("customerQueue");
			MessageProducer producer = session.createProducer(queue);
			String payload = "HELLO";
			IntStream.range(0,5).forEach( i->{
				try {
					Message msg = session.createTextMessage(payload+i);
					producer.send(msg);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			});

			session.close();
		} finally {
			if (connection != null) {
				connection.close();
			}
			broker.stop();
		}
	}

}
