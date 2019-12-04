package com.whd.aop.nty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;



public class ServerHandler extends ChannelHandlerAdapter {
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		//simpleRead(ctx, msg);
		//delimiterread(ctx, msg);
		handlerObject(ctx, msg);
	}
	
	public void simpleRead(ChannelHandlerContext ctx, Object msg){
		ByteBuf bb = (ByteBuf) msg;
		byte[] reqByte = new byte[bb.readableBytes()];
		bb.readBytes(reqByte);
		String reqstr = new String(reqByte,CharsetUtil.UTF_8);
		System.out.println("server 接收到客户端的请求： " + reqstr);
		String respStr = new StringBuilder("来自服务器的响应").append(reqstr).append("$_").toString();
	
	}
	
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端读取数据完毕");
    }
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		 System.out.println("服务端激活");
        
    }
	
	private void delimiterread(ChannelHandlerContext ctx, Object msg) {
        // 如果把msg直接转成字符串，必须在服务中心添加 socketChannel.pipeline().addLast(new StringDecoder());
        String reqStr = (String)msg;
        System.out.println("server 接收到请求信息是："+reqStr);
        String respStr = new StringBuilder("来自服务器的响应").append(reqStr).append("$_").toString();
        
        // 返回给客户端响应                                                                                                                                                       和客户端链接中断即短连接，当信息返回给客户端后中断
        ctx.writeAndFlush(Unpooled.copiedBuffer(respStr.getBytes())).addListener(ChannelFutureListener.CLOSE);
    }
	
	/**
     * 将请求信息直接转成对象
     * @param ctx
     * @param msg
     */
    private void handlerObject(ChannelHandlerContext ctx, Object msg) {
        // 需要序列化 直接把msg转成对象信息，一般不会用，可以用json字符串在不同语言中传递信息
        Student student = (Student)msg;     
        System.out.println("server 获取信息："+student.getId()+student.getName());
        student.setName("李四");      
        ctx.writeAndFlush(student);
    }
    
}
