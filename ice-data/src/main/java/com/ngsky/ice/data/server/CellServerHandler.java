package com.ngsky.ice.data.server;

/**
 * <dl>
 * <dt>ServerHandler</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 7/9/2019 8:33 AM</dd>
 * </dl>
 *
 * @author ngsky
 */

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import com.ngsky.ice.comm.bean.CellDownReq;
import com.ngsky.ice.comm.bean.CellDownResp;
import com.ngsky.ice.comm.bean.CellReqBody;
import com.ngsky.ice.comm.bean.CellRespBody;
import com.ngsky.ice.comm.utils.Base64Util;
import com.ngsky.ice.data.service.ChunkedService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@ChannelHandler.Sharable
@Component
public class CellServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ChunkedService chunkedService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("服务端 channel active ------> ");
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("服务端接受消息: remote address:{}", ctx.channel().remoteAddress());
        // 写数据
        if(msg instanceof CellReqBody.Request){
            String objKey = null;
            CellReqBody.Request body = (CellReqBody.Request) msg;
            chunkedService.storage(body);
            objKey = body.getObjKey();

            JSONObject partition = new JSONObject();
            partition.put("objKey", objKey);
            partition.put("host", "localhost");
            partition.put("port", "8899");
            partition.put("whatChunk", body.getWhatChunk());
            partition.put("countChunk", body.getCountChunk());

            CellRespBody.Response respBody = CellRespBody.Response
                    .newBuilder()
                    .setSuccess(true)
                    .setDataMsg(Base64Util.encode(partition.toJSONString()))
                    .build();


            sendMsg(ctx, respBody);
        }
        // 读数据
        if (msg instanceof CellDownReq.ReqDown) {
            log.info("读取数据快");
            CellDownReq.ReqDown body = (CellDownReq.ReqDown) msg;
            byte[] data = chunkedService.extractive(body.getObjKey());
            ByteString objData = null;
            if(null == data) {
                objData = ByteString.EMPTY;
            } else {
                objData = ByteString.copyFrom(data);
            }

            CellDownResp.RespDown resp = CellDownResp.RespDown
                    .newBuilder()
                    .setSuccess(true)
                    .setObjKey(body.getObjKey())
                    .setWhatChunk(body.getWhatChunk())
                    .setCountChunk(body.getCountChunk())
                    .setObjData(objData)
                    .build();

            ctx.writeAndFlush(resp);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private void sendMsg(ChannelHandlerContext ctx, CellRespBody.Response body) {
        if (null == body || null == ctx) return;
        log.info("服务端向客户端响应消息...");
        ctx.writeAndFlush(body);
        log.info("服务端向客户端响应成功...");
    }
}