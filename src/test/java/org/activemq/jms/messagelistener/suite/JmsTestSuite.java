package org.activemq.jms.messagelistener.suite;

import org.activemq.jms.messagelistener.ReceptMessageTest;
import org.activemq.jms.messagelistener.SendMessagesTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
   SendMessagesTest.class,
   ReceptMessageTest.class
})
public class JmsTestSuite {

}
