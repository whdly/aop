package com.whd.aop.nty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;


public class ClientHandler extends ChannelHandlerAdapter {
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		try {
           /* ByteBuf bb = (ByteBuf)msg;
            byte[] respByte = new byte[bb.readableBytes()];
            bb.readBytes(respByte);
            String respStr = new String(respByte,CharsetUtil.UTF_8);
            System.out.println("client--收到响应：" + respStr);   */   
			 Student student = (Student)msg;     
		     System.out.println("server 获取信息："+student.getId()+student.getName());
           
        } finally{
            // 必须释放msg数据
            ReferenceCountUtil.release(msg);
            
        }
	}
	
	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端读取数据完毕");
    }
	private void handlerObject(ChannelHandlerContext ctx, Object msg) {
        
        Student student = (Student)msg;
        System.out.println("server 获取信息："+student.getId()+student.getName());
    }
}
