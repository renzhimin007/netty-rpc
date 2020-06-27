package com.rzm.rpc.comsume.codec;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class JSONDECODE extends LengthFieldBasedFrameDecoder {



    public JSONDECODE() {
        super(65535, 0, 4,0,4);
    }


    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        ByteBuf decode = (ByteBuf) super.decode(ctx, in);
        if (decode==null){
            return null;
        }
        int i = decode.readableBytes();


        byte[] bytes = new byte[i];

        ByteBuf byteBuf = decode.readBytes(bytes);
        Object parse = JSON.parse(bytes);
        return parse;


    }
}
