package com.whd.aop.cl;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class FileMoveWithCamel {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		CamelContext context = new DefaultCamelContext();
		
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				// TODO Auto-generated method stub
				/*from("file:C:/Users/whd/Desktop/工作任务/test/a/").choice()
				.when(header("CamelFileName").endsWith(".pdf")).to("file:C:/Users/whd/Desktop/工作任务/test/b")
				.when(header("CamelFileName").endsWith("rar")).to("file:C:/Users/whd/Desktop/工作任务/test/c")
				.otherwise().to("file:C:/Users/whd/Desktop/工作任务/test/d");*/
				
				from("file:C:/Users/whd/Desktop/工作任务/test/a/").to("file:C:/Users/whd/Desktop/工作任务/test/e").threads(5);
				
			}
		});
		context.start();
		
		
        boolean loop =true;
        while(loop){
            Thread.sleep(300000);
        }        
        context.stop();
        
        
	}

}
