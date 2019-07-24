package com.ngsky.ice.rest.client;

/**
 * <dl>
 * <dt>ClientHandler</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 7/9/2019 8:48 AM</dd>
 * </dl>
 *
 * @author ngsky
 */

import com.alibaba.fastjson.JSONObject;
import com.ngsky.ice.comm.bean.CellDownResp;
import com.ngsky.ice.comm.bean.CellRespBody;
import com.ngsky.ice.comm.utils.Base64Util;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CellClientHandler extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext ctx;
    private ChannelPromise promise;
    private CellDownResp.RespDown respDown;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端 channel active ------> ");
        super.channelActive(ctx);
        this.ctx = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
//            log.info("客户端接受消息: remote address:{}, msg:{}", ctx.channel().remoteAddress(), msg);
            if (msg instanceof CellRespBody.Response) {
                CellRespBody.Response body = (CellRespBody.Response) msg;
                if (body.getSuccess()) {
//                    log.info("对象存储成功, 对象名称及分区信息:{}", body.getDataMsg());
                    String dataJson = Base64Util.decode(body.getDataMsg());
                    JSONObject dataMsg = JSONObject.parseObject(dataJson);
                    if (null != dataMsg && null != dataMsg.get("whatChunk")
                            && dataMsg.get("whatChunk").equals(dataMsg.get("countChunk"))) {
                        log.info("该文件的所有数据块存储成功!!!");
                        // 关闭此连接
                        ctx.close();
                        log.info("连接关闭");
                    }
                }
            } else if (msg instanceof CellDownResp.RespDown) {
                CellDownResp.RespDown body = (CellDownResp.RespDown) msg;
                if (body.getSuccess()) {
                    respDown = body;
                    promise.setSuccess();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("客户端接受服务端消息，解包异常");
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public ChannelPromise downloadObj(Object req) {
        if (ctx == null)
            throw new IllegalStateException();
        promise = ctx.writeAndFlush(req).channel().newPromise();
        return promise;
    }

    public CellDownResp.RespDown getRespDown(){
        return respDown;
    }
}
