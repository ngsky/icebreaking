package com.ngsky.ice.data.server;

import com.google.protobuf.MessageLite;
import com.ngsky.ice.comm.bean.CellDownReq;
import com.ngsky.ice.comm.bean.CellDownResp;
import com.ngsky.ice.comm.bean.CellReqBody;
import com.ngsky.ice.comm.bean.CellRespBody;
import com.ngsky.ice.comm.utils.BinaryUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * <dl>
 * <dt>MultiProtobufEncoder</dt>
 * <dd>Description: 支持多种不同类型 protobuf 数据在一个 socket 上进行传输</dd>
 * <dd>CreateDate: 7/9/2019 10:46 PM</dd>
 * </dl>
 * <p>
 * 数据包编码协议: 分为2部分,首部header 8 byte, 前5个byte为body长度,6、7byte为空,8byte为类型;body为数据流，记录对象快信息及对象数据[4MB]
 * 1.header: byte[8] 8 个字节
 * -------------------------------------------
 * | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |
 * -------------------------------------------
 * | body len[5个byte] |   |   |type|
 * -------------------------------------------
 * 2.body: CellReqBody 二进制数据流
 *
 * @author ngsky
 */
@Slf4j
@ChannelHandler.Sharable
public class MultiProtobufEncoder extends MessageToByteEncoder<MessageLite> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageLite messageLite, ByteBuf byteBuf) throws Exception {
        byte[] body = messageLite.toByteArray();
        log.info("client--------------------- body.len:{}", body.length);
        byte[] header = encodeHeader(messageLite, body.length);
        log.info("client--------------------- header.len:{}", header.length);
        byteBuf.writeBytes(header);
        byteBuf.writeBytes(body);
    }

    private byte[] encodeHeader(MessageLite msg, int bodyLen) {
        byte messageType = 0x0f;
        // 判断是 CellReqBody 还是 CellRespBody
        if (msg instanceof CellReqBody.Request) {
            messageType = 0x00;
        } else if (msg instanceof CellRespBody.Response) {
            messageType = 0x01;
        } else if (msg instanceof CellDownResp.RespDown) {
            messageType = 0x02;
        }
        byte[] bodyLenHeader = BinaryUtil.encodeInt(bodyLen);
        byte[] header = new byte[8];
        header[5] = 0;
        header[6] = 0;
        header[7] = messageType;
        System.arraycopy(bodyLenHeader, 0, header, 0, 5);
        return header;
    }
}
