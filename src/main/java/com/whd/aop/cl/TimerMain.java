package com.whd.aop.cl;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;


public class TimerMain {

	public static void main(String[] args) throws Exception {
		new TimerMain().run();
		//new TimerMain().run();
	}

	void run() throws Exception {
		final CamelContext camelContext = new DefaultCamelContext();
		camelContext.addRoutes(createRouteBuilder());
		camelContext.setTracing(true);
		camelContext.start();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					camelContext.stop();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		waitForStop();
	}

	RouteBuilder createRouteBuilder() {
		return new TimerRouteBuilder();
	}

	void waitForStop() {
		while (true) {
			try {
				Thread.sleep(Long.MAX_VALUE);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}