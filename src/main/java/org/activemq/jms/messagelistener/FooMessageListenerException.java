package org.activemq.jms.messagelistener;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;


public class FooMessageListenerException implements ExceptionListener {

	@Override
	public void onException(JMSException arg0) {
		System.out.println("An error occure:"+arg0.toString());
	}

}
