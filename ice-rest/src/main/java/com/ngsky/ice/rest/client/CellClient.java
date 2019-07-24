package com.ngsky.ice.rest.client;

import com.ngsky.ice.comm.bean.CellDownReq;
import com.ngsky.ice.comm.bean.CellDownResp;
import com.ngsky.ice.comm.bean.CellReqBody;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * <dl>
 * <dt>CellClient</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 7/9/2019 8:49 AM</dd>
 * </dl>
 *
 * @author ngsky
 */

@Slf4j
public class CellClient {

    private CellClientHandler cellClientHandler = new CellClientHandler();

    /**
     * 建立连接
     */
    public ChannelFuture connectCellServer(@NonNull String cellServerHost, int cellServerPort) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new CellChannelInitializer(cellClientHandler));

        return bootstrap.connect(cellServerHost, cellServerPort).sync();
    }


    // 数据块存储
    public void sendMessage(@NonNull ChannelFuture future, @NonNull CellReqBody.Request cellReqBody) {
        // 向服务端发起请求
        log.info("========================");
        future.channel().writeAndFlush(cellReqBody);
    }

    // 读取数据快
    public CellDownResp.RespDown sendDownMsg(@NonNull ChannelFuture future, @NonNull CellDownReq.ReqDown reqDown) {
        // 向服务端发起下载请求
        log.info("========================");
        ChannelPromise promise = cellClientHandler.downloadObj(reqDown);
        try {
            promise.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return cellClientHandler.getRespDown();
    }

}
