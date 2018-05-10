package org.activemq.jms.messagelistener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class FooMessageListener implements MessageListener {
	private String consumerName;
	public FooMessageListener(String consumerName) {
		this.consumerName = consumerName;
	}

	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			String payload = textMessage.getText();
			if(payload.matches("-?\\d+")) {
				throw new IllegalStateException();
			}
			System.out.println(consumerName + " received "	+ textMessage.getText());
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}