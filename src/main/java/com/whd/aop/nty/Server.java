package com.whd.aop.nty;



import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.CharsetUtil;

public class Server {
	
	
	
	private ChannelFuture cf;
	
	private ServerBootstrap sbs;
	
	private static class ServerInstance{
		 
		public static Server singlton  = new Server();
	 }
	
	public static Server getInstance(){
		
		return ServerInstance.singlton;
	}
	
	
	public ChannelFuture getconect(int port) throws InterruptedException{
		if(cf!=null){
			return cf;
		}else{
			Server.getInstance();
			cf=sbs.bind(port).sync();
			return cf;
		}
		
		
	}
	
	
	
	public Server()  {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		sbs = new ServerBootstrap();
		sbs.group(bossGroup, workerGroup)
		.channel(NioServerSocketChannel.class)
		.option(ChannelOption.SO_BACKLOG, 1024)
		.childOption(ChannelOption.SO_KEEPALIVE,true)
		.handler(new LoggingHandler(LogLevel.INFO))
		.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				// TODO Auto-generated method stub
				//ByteBuf bb = Unpooled.copiedBuffer("$_".getBytes(CharsetUtil.UTF_8));
				//ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, bb));
				//ch.pipeline().addLast(new StringDecoder());
				ch.pipeline().addLast(new ObjectEncoder());
				ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE,ClassResolvers.cacheDisabled(null)));
				ch.pipeline().addLast(new ReadTimeoutHandler(5));
				ch.pipeline().addLast(new ServerHandler());
			}
			
		});
		System.out.println("server 开启--------------");
		
		
		 //cf.channel().closeFuture().sync();
	}
	
	public static void main(String[] args) throws InterruptedException {
		Server s=Server.getInstance();   
		ChannelFuture cf = s.cf;
		if(cf==null){
			cf= s.getconect(7777);
		}
		cf.channel().closeFuture().sync();
	}
}
