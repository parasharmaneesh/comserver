package com.example.comserver.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.timeout.ReadTimeoutHandler;

@Configuration
@PropertySource("classpath:global.properties")
public class GlobalConfiguration {
	@Value("${tcp.port}")
	public int tcpPort;

	@Value("${udp.port}")
	public int udpPort;
	
	@Value("${boss.thread.count}")
	private int bossCount;

	@Value("${worker.thread.count}")
	private int workerCount;

	@Value("${so.keepalive}")
	private boolean keepAlive;

	@Value("${so.backlog}")
	private int backlog;

	@Value("${so.readtimeout}")
	private int readtimeout;

	@Bean(name = "channelOptions")
	public Map<ChannelOption<?>, Object> channelOptions() {
		Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
		options.put(ChannelOption.SO_KEEPALIVE, keepAlive);
		options.put(ChannelOption.SO_BACKLOG, backlog);
		return options;
	}

	@Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
	public EventLoopGroup bossGroup() {
		return new NioEventLoopGroup(bossCount);
	}

	@Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
	public EventLoopGroup workerGroup() {
		return new NioEventLoopGroup(workerCount);
	}

	@Bean(name = "readTimeoutHandler")
	public ReadTimeoutHandler readTimeoutHandler() {
		return new ReadTimeoutHandler(readtimeout);
	}

}
