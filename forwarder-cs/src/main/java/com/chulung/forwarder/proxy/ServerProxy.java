package com.chulung.forwarder.proxy;

import java.net.InetSocketAddress;

import com.chulung.forwarder.codec.KryoDecoder;
import com.chulung.forwarder.codec.KryoEncoder;
import com.chulung.forwarder.codec.KryoPool;
import com.chulung.forwarder.common.Config;
import com.chulung.forwarder.proxy.handler.ServerProxyHandler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ServerProxy extends AbstractProxy {
	private ServerProxyHandler serverProxyHandler = new ServerProxyHandler();

	public void startSync() {
		startBoot(new InetSocketAddress(Config.getInstance().getForwarderHost(),
				Config.getInstance().getForwaderChannelPort()));
	}

	@Override
	protected ChannelHandler getChannelInitializer() {
		return new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new KryoEncoder(KryoPool.getInstance()));
				ch.pipeline().addLast(new KryoDecoder(KryoPool.getInstance()));
				ch.pipeline().addLast(serverProxyHandler);
			}
		};
	}
	
	public static void main(String[] args) {
		new ServerProxy().startSync();
	}

}
