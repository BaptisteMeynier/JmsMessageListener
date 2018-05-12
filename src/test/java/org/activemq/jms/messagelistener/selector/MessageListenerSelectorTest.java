package org.activemq.jms.messagelistener.selector;

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


public class MessageListenerSelectorTest {

	/*ActiveMQTextMessage {
	commandId = 5, responseRequired = true, 
	messageId = ID:DESKTOP-FUI7H3K-45995-1526110873708-7:1:1:1:1, 
	originalDestination = null, originalTransactionId = null, 
	producerId = ID:DESKTOP-FUI7H3K-45995-1526110873708-7:1:1:1, 
	destination = queue://customerQueue, transactionId = null, expiration = 0, 
	timestamp = 1526110879092, arrival = 0, brokerInTime = 1526110879092, 
	brokerOutTime = 1526111235097, correlationId = null, replyTo = null, 
	persistent = true, type = null, priority = 4, 
	groupID = null, 
	groupSequence = 0, 
	targetConsumerId = null, compressed = false, 
	userID = null, 
	content = org.apache.activemq.util.ByteSequence@359f7cdf, 
	marshalledProperties = null, 
	dataStructure = null, 
	redeliveryCounter = 0, 
	size = 0, 
	properties = null, readOnlyProperties = true, 
	readOnlyBody = true, droppable = false, 
	jmsXGroupFirstForConsumer = false, text = 11111}
	*/
	@Test
    public void trigerMessageListener_with_selector() throws URISyntaxException, Exception{
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
            String payload = "Thomas";
            Message msg = session.createTextMessage(payload);
            MessageProducer producer = session.createProducer(queue);
            System.out.println("Sending text '" + payload + "'");
            producer.send(msg);

            // Consumer
            MessageConsumer consumer = session.createConsumer(queue,"JMSPriority = 4");
            consumer.setMessageListener(new FooMessageListener("Consumer"));
            connection.start();
            TimeUnit.SECONDS.sleep(5);
            session.close();
        }
        finally {
            if (connection != null) {
                connection.close();
            }
            broker.stop();
        }
    }

}