package com.ngsky.ice.data.server;

/**
 * <dl>
 * <dt>ServerChannelInitializer</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 7/9/2019 8:32 AM</dd>
 * </dl>
 *
 * @author ngsky
 */

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CellChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private CellServerHandler cellServerHandler;

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline cp = channel.pipeline();
        // 文本解码
//        cp.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
        // 文本编码
//        cp.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
        // 二进制流编码
//        cp.addLast("objencoder", new ObjectEncoder());
//        // 二进制流解码
//        cp.addLast("objdecoder", new ObjectDecoder(10 * 1024 * 1024, ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
        cp.addLast("decoder", new MultiProtobufDecoder());
        cp.addLast("encoder", new MultiProtobufEncoder());
        // 自定义处理器
        cp.addLast(cellServerHandler);
    }
}