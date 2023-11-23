package com.example.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    // 수신된 데이터를 처리하는 역할
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 수신된 데이터를 ByteBuf 로 형변환
        ByteBuf in = (ByteBuf) msg;
        try {
            // ByteBuf 에서 데이터를 읽어와서 출력
            while (in.isReadable()) {
                System.out.print((char) in.readByte());
                System.out.flush();
            }
        } finally {
            // 메모리 누수 방지를 위해 메모리 해제
            // Netty 에서는 ByteBuf 등의 리소스를 명시적으로 해제해야 합니다.
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 예외 출력 및 연결 닫음
        cause.printStackTrace();
        ctx.close();
    }
}
