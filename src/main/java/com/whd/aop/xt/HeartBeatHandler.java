package com.whd.aop.xt;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatHandler extends SimpleChannelInboundHandler<String> {

    int readIdleTimes = 0;
    
   
   
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent)evt;

        String eventType = null;
        switch (event.state()){
            case READER_IDLE:
                eventType = "读空闲";
                readIdleTimes ++; // 读空闲的计数加1
                break;
            case WRITER_IDLE:
                eventType = "写空闲";
                // 不处理
                break;
            case ALL_IDLE:
                eventType ="读写空闲";
                // 不处理
                break;
        }
        System.out.println(ctx.channel().remoteAddress() + "超时事件：" +eventType);
        if(readIdleTimes > 2){
            System.out.println(" [server]读空闲超过2次，关闭连接");
            ctx.channel().writeAndFlush("you are out");
            ctx.channel().close();
        }
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("=== " + ctx.channel().remoteAddress() + " is active ===");
    }

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String msg)
			throws Exception {
		// TODO Auto-generated method stub
		 System.out.println(" ====== > [server] message received : " + msg);
	       if("I am alive".equals(msg)){
	            ctx.channel().writeAndFlush("copy that");
	            readIdleTimes = 0;
	        }else {
	           System.out.println(" 其他信息处理 ... ");
	       }
	}
}
