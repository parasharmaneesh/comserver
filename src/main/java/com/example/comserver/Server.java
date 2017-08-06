package com.example.comserver;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.example.comserver.config.GlobalConfiguration;
import com.example.comserver.handler.ServerChannelInitializer;
import com.example.comserver.tcp.TCPServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import static com.example.comserver.Main.logger;
import static com.example.comserver.Main.context;

@Component
public class Server {

	@Autowired
	@Qualifier("serverChannelInitializer")
	private ServerChannelInitializer serverChannelInitializer;

	@Autowired
	private GlobalConfiguration globalConfiguration;

	@SuppressWarnings("unchecked")
	@Bean(name = "serverBootstrap")
	public ServerBootstrap bootstrap() {
		ServerBootstrap b = new ServerBootstrap();
		b.group(globalConfiguration.bossGroup(), globalConfiguration.workerGroup())
				.channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(serverChannelInitializer);
		Map<ChannelOption<?>, Object> channelOptions = globalConfiguration.channelOptions();
		Set<ChannelOption<?>> keySet = channelOptions.keySet();
		for (@SuppressWarnings("rawtypes")
		ChannelOption option : keySet) {
			b.option(option, channelOptions.get(option));
		}
		return b;
	}
	
	public void init(){
		TCPServer tcpServer = context.getBean(TCPServer.class);
		try {
			tcpServer.start(globalConfiguration.tcpPort);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
