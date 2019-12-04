package com.whd.aop.xt;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class ClientStarter {
	
	 private Bootstrap bootstrap;
	    private int times = 0;

	    public ClientStarter(Bootstrap bootstrap) {
	        this.bootstrap = bootstrap;
	        ClientStarter clientStarter = this;
	        bootstrap.group(new NioEventLoopGroup())
	                .channel(NioSocketChannel.class)
	                .handler(new ChannelInitializer<SocketChannel>() {
	                    @Override
	                    protected void initChannel(SocketChannel ch) throws Exception {
	                        ch.pipeline().addLast(new StringEncoder());
	                        ch.pipeline().addLast(new StringDecoder());
	                        ch.pipeline().addLast(new IdleStateHandler(0, 0, 4));
	                        ch.pipeline().addLast(new HeartBeatClientHandler(clientStarter));
	                    }
	                });
	    }

	    public static void main(String[] args) {
	        ClientStarter starter = new ClientStarter(new Bootstrap());
	        starter.connect();
	    }

	    public void connect() {
	        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("localhost", 1111));
	        channelFuture.addListener(future ->{
	                if (future.isSuccess()) {
	                    System.out.println("connect to server success");
	                } else {
	                    System.out.println("connect to server failed,try times:" + ++times);
	                    connect();
	                }
	        });
	    }

}
