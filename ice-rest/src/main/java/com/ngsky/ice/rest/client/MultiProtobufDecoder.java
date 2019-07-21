package com.ngsky.ice.rest.client;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.ngsky.ice.comm.bean.CellDownReq;
import com.ngsky.ice.comm.bean.CellDownResp;
import com.ngsky.ice.comm.bean.CellReqBody;
import com.ngsky.ice.comm.bean.CellRespBody;
import com.ngsky.ice.comm.utils.BinaryUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <dl>
 * <dt>MultiProtobufDecoder</dt>
 * <dd>Description: 响应编码器</dd>
 * <dd>CreateDate: 7/9/2019 11:14 PM</dd>
 * </dl>
 *
 * @author ngsky
 */
@Slf4j
public class MultiProtobufDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 如果数据长度 小于 header，则忽略
        while (byteBuf.readableBytes() > 8) {
            byteBuf.markReaderIndex();
            // 解析header
            byte[] bodyLenHeader = new byte[5];
            byteBuf.readBytes(bodyLenHeader, 0, 5);
            byteBuf.readByte();
            byteBuf.readByte();
            int bodyLen = BinaryUtil.decodeByte(bodyLenHeader);
            byte dataType = byteBuf.readByte();
            log.info("--- decode--- bodyLen:{}, dataType:{}", bodyLen, dataType);

            // protobuf 数据长度校验
            if (byteBuf.readableBytes() < bodyLen) {
                byteBuf.resetReaderIndex();
                return;
            }

            ByteBuf body = byteBuf.readBytes(bodyLen);
            byte[] array;
            int offset;

            int readableLen = body.readableBytes();
            if (body.hasArray()) {
                array = body.array();
                offset = body.arrayOffset() + body.readerIndex();
            } else {
                array = new byte[readableLen];
                body.getBytes(body.readerIndex(), array, 0, readableLen);
                offset = 0;
            }

            // 反系列化
            MessageLite result = decodeBody(dataType, array, offset, readableLen);

            list.add(result);
        }
    }

    public MessageLite decodeBody(byte dataType, byte[] array, int offset, int readableLen) throws InvalidProtocolBufferException {

        if (dataType == 0x00) {
            return CellReqBody.Request.getDefaultInstance()
                    .getParserForType().parseFrom(array, offset, readableLen);
        } else if (dataType == 0x01) {
            return CellRespBody.Response.getDefaultInstance()
                    .getParserForType().parseFrom(array, offset, readableLen);
        } else if (dataType == 0x02 || dataType == 0x0f) {
            return CellDownResp.RespDown.getDefaultInstance()
                    .getParserForType().parseFrom(array, offset, readableLen);
        }
        return null;
    }
}
