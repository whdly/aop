package com.whd.aop.nty;





import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.CharsetUtil;

public class Client {
	
	private ChannelFuture cf;
	
	private Bootstrap bs;
	
	private static class ClientInstance{
		 
		public static Client singlton  = new Client();
	 }
	public static Client getInstance(){
		
		return ClientInstance.singlton;
	}
	public ChannelFuture getconect(String ip,int port) throws InterruptedException{
		if(cf!=null){
			return cf;
		}else{
			Client.getInstance();
			cf=bs.connect(ip, port).sync();
			return cf;
		}
		
		
	}
	
	public Client() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		
		
		bs = new Bootstrap();
		bs.group(bossGroup)
		.channel(NioSocketChannel.class)
		.option(ChannelOption.SO_KEEPALIVE,true)
		.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				// TODO Auto-generated method stub
				ch.pipeline().addLast(new ObjectEncoder());
				ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE,ClassResolvers.cacheDisabled(null)));
				
				ch.pipeline().addLast(new ClientHandler());
			}
			
		});
	
		
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		Client c=  Client.getInstance();
		ChannelFuture cf = c.cf;
		if(cf==null){
			cf = c.getconect("127.0.0.1", 7777);
		}
		Thread.sleep(4000);
		Student student = new Student();
      student.setId(3);
      student.setName("张三");
      cf.channel().writeAndFlush(student);
      cf.channel().closeFuture().sync(); 
		 //cf.channel().writeAndFlush(Unpooled.copiedBuffer("我是客户端请求2$_---".getBytes(CharsetUtil.UTF_8)));
		 //cf.channel().closeFuture().sync(); 
		/* Thread.sleep(2000);
		 Client c1= new Client();
		  ChannelFuture cf1 = c1.cf;
		 if(cf1==null){
				cf1 = c1.getconect("127.0.0.1", 7777);
			} 
		 cf1.channel().writeAndFlush(Unpooled.copiedBuffer("我是客户端请求3$_".getBytes(CharsetUtil.UTF_8)));
		
		 cf1.channel().closeFuture().sync(); */
	}
}
