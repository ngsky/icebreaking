package com.ngsky.ice.rest.client;

/**
 * <dl>
 * <dt>ClientChannelInitializer</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 7/9/2019 8:48 AM</dd>
 * </dl>
 *
 * @author ngsky
 */

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class CellChannelInitializer extends ChannelInitializer<SocketChannel> {

    private CellClientHandler cellClientHandler;

    public CellChannelInitializer(CellClientHandler cellClientHandler){
        this.cellClientHandler = cellClientHandler;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline cp = channel.pipeline();
//        // 二进制流编码
//        cp.addLast("encoder", new ObjectEncoder());
//        // 二进制流解码
//        cp.addLast("decoder", new ObjectDecoder(10 * 1024 * 1024, ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));

        cp.addLast("encoder", new MultiProtobufEncoder());
        cp.addLast("decoder", new MultiProtobufDecoder());

        // 自定义处理器
        cp.addLast(cellClientHandler);
    }
}
