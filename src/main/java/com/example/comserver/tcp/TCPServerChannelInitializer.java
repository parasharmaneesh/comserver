package com.example.comserver.tcp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

@Component
@Qualifier("serverChannelInitializerTCP")
public class TCPServerChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Autowired
	@Qualifier("serverDataDecoder")
	private ByteToMessageDecoder serverDataDecoder;

	@Autowired
	@Qualifier("serverDataHandler")
	private ChannelHandlerAdapter serverDataHandler;
	
	@Autowired
	@Qualifier("readTimeoutHandler")
	private ReadTimeoutHandler readTimeoutHandler;

	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		ChannelPipeline pipeline = socketChannel.pipeline();
		pipeline.addLast(readTimeoutHandler);
		pipeline.addLast(serverDataDecoder);
		pipeline.addLast(serverDataHandler);
	}

}
