package com.example.comserver.tcp;

import java.net.InetSocketAddress;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;

import static com.example.comserver.Main.logger;

@Component
public class TCPServer implements Runnable{

	@Autowired
	@Qualifier("serverBootstrapTCP")
	private ServerBootstrap serverBootstrap;

	private InetSocketAddress tcpPort;

	private Channel serverChannel;

	public void start(int port) throws Exception {
		tcpPort = new InetSocketAddress(port);
		
	}

	@PreDestroy
	public void stop() throws Exception {
		serverChannel.close();
		serverChannel.parent().close();
	}

	public ServerBootstrap getServerBootstrap() {
		return serverBootstrap;
	}

	public void setServerBootstrap(ServerBootstrap serverBootstrap) {
		this.serverBootstrap = serverBootstrap;
	}

	public InetSocketAddress getTcpPort() {
		return tcpPort;
	}

	public void setTcpPort(int tcpPort) {
		this.tcpPort = new InetSocketAddress(tcpPort);
	}

	@Override
	public void run() {
		try {
			serverChannel = serverBootstrap.bind(tcpPort).sync().channel().closeFuture().sync().channel();
		} catch (InterruptedException e) {
			logger.error(e.getMessage(),e);
		}
	}

}
