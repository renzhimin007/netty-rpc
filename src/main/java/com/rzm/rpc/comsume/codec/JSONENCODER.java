package com.rzm.rpc.comsume.codec;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class JSONENCODER extends MessageToMessageEncoder {


    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        byte[] bytes = JSON.toJSONBytes(o);
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
        list.add(buffer);
    }
}
