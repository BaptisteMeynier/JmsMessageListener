package org.activemq.jms.messagelistener.suite;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
public class TestRunnerJms {
	 public static void main(String[] args) {
	      Result result = JUnitCore.runClasses(JmsTestSuite.class);

	      for (Failure failure : result.getFailures()) {
	         System.out.println(failure.toString());
	      }
			
	      System.out.println(result.wasSuccessful());
	   }
}

