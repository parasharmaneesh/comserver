package com.example.comserver.udp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.DatagramChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Component
@Qualifier("serverChannelInitializerUDP")
public class UDPServerChannelInitializer extends ChannelInitializer<DatagramChannel> {

	@Autowired
	@Qualifier("serverDataDecoder")
	private ByteToMessageDecoder serverDataDecoder;

	@Autowired
	@Qualifier("serverDataHandler")
	private ChannelHandlerAdapter serverDataHandler;
	
	@Override
	protected void initChannel(DatagramChannel datagramChannel) throws Exception {
		ChannelPipeline pipeline = datagramChannel.pipeline();
		pipeline.addLast(new LoggingHandler(LogLevel.INFO));
		pipeline.addLast(serverDataDecoder);
		pipeline.addLast(serverDataHandler);
	}

}
