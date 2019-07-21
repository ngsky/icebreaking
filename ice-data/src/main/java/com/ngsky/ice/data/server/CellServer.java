package com.ngsky.ice.data.server;

/**
 * <dl>
 * <dt>NettyServer</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 7/9/2019 8:30 AM</dd>
 * </dl>
 *
 * @author ngsky
 */

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Slf4j
@Component
public class CellServer {

    @Autowired
    private CellChannelInitializer cellChannelInitializer;

    private static EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private static EventLoopGroup workerGroup = new NioEventLoopGroup();
    private static ServerBootstrap bootstrap = new ServerBootstrap();

    public void start(InetSocketAddress address) {
        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(address)
                    .childHandler(cellChannelInitializer)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind(address).sync();
            log.info("服务端开始监听端口:{}  ", address.getPort());
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @PreDestroy
    public void close() {
        log.info("关闭服务器...");
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}
