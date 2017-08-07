package com.example.comserver.udp;

import java.util.HashMap;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import static com.example.comserver.Main.logger;

@Component
public class UDPServer implements Runnable{
	@Autowired
	@Qualifier("serverBootstrapUDP")
	private Bootstrap serverBootstrap;

	private Channel serverChannel;
	
	private int udpPort;
	
	public static HashMap<String, ChannelHandlerContext> channelCache = new HashMap<String, ChannelHandlerContext>();

	@PreDestroy
	public void stop() throws Exception {
		serverChannel.close();
		serverChannel.parent().close();
	}

	public Bootstrap getServerBootstrap() {
		return serverBootstrap;
	}

	public void setServerBootstrap(Bootstrap serverBootstrap) {
		this.serverBootstrap = serverBootstrap;
	}

	@Override
	public void run() {
		try {
			serverChannel = serverBootstrap.bind(udpPort).sync().channel().closeFuture().sync().channel();
		} catch (InterruptedException e) {
			logger.error(e.getMessage(),e);
		}
	}

	public int getUdpPort() {
		return udpPort;
	}

	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

}
