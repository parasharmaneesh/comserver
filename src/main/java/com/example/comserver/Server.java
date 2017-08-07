package com.example.comserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.example.comserver.config.GlobalConfiguration;
import com.example.comserver.tcp.TCPServerChannelInitializer;
import com.example.comserver.udp.UDPServer;
import com.example.comserver.udp.UDPServerChannelInitializer;
import com.example.comserver.tcp.TCPServer;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import static com.example.comserver.Main.context;

@Component
public class Server {

	public static HashMap<String, ChannelHandlerContext> channelCache = new HashMap<String, ChannelHandlerContext>();

	@Autowired
	@Qualifier("serverChannelInitializerTCP")
	private TCPServerChannelInitializer serverChannelInitializerTCP;

	@Autowired
	@Qualifier("serverChannelInitializerUDP")
	private UDPServerChannelInitializer serverChannelInitializerUDP;

	@Autowired
	private GlobalConfiguration globalConfiguration;

	@SuppressWarnings("unchecked")
	@Bean(name = "serverBootstrapTCP")
	public ServerBootstrap bootstrapTCP() {
		ServerBootstrap b = new ServerBootstrap();
		b.group(globalConfiguration.bossGroup(), globalConfiguration.workerGroup())
				.channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(serverChannelInitializerTCP);
		Map<ChannelOption<?>, Object> channelOptions = globalConfiguration.channelOptions();
		Set<ChannelOption<?>> keySet = channelOptions.keySet();
		for (@SuppressWarnings("rawtypes")
		ChannelOption option : keySet) {
			b.option(option, channelOptions.get(option));
		}
		return b;
	}

	@SuppressWarnings("unchecked")
	@Bean(name = "serverBootstrapUDP")
	public Bootstrap bootstrapUDP() {
		Bootstrap b = new Bootstrap();
		b.group(globalConfiguration.workerGroup()).channel(NioDatagramChannel.class)
				.handler(serverChannelInitializerUDP);
		Map<ChannelOption<?>, Object> channelOptions = globalConfiguration.channelOptions();
		Set<ChannelOption<?>> keySet = channelOptions.keySet();
		for (@SuppressWarnings("rawtypes")
		ChannelOption option : keySet) {
			b.option(option, channelOptions.get(option));
		}
		return b;
	}

	public void init() {
		try {
			TCPServer tcpServer = context.getBean(TCPServer.class);
			tcpServer.setTcpPort(globalConfiguration.tcpPort);
			new Thread(tcpServer).start();
			
			UDPServer udpServer = context.getBean(UDPServer.class);
			udpServer.setUdpPort(globalConfiguration.udpPort);
			new Thread(udpServer).start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
