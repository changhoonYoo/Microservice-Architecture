package com.example.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {

    private int port; //포트번호

    public DiscardServer(int port) {
        this.port = port;
    }
    public void run() throws Exception {
        // 이벤트 루프 그룹 정의
        // 이벤트를 비동기적으로 처리,
        // bossGroup 은 들어오는 연결을 받아들이는 역할
        // workerGroup 은 연결된 클라이언트와의 데이터 교환을 담당합니다.
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            // 서버를 설정하고 부트스트랩합니다. 부트스트랩은 서버를 시작하는 데 사용됩니다.
            ServerBootstrap b = new ServerBootstrap();
            // 부트스트랩에 이벤트 루프 그룹을 할당합니다.
            b.group(bossGroup, workerGroup)
                    // 서버 소켓 채널의 종류를 설정합니다. 여기서는 NIO 소켓 채널을 사용합니다.
                    .channel(NioServerSocketChannel.class)
                    // 클라이언트 소켓 채널을 초기화하는 핸들러를 설정합니다. 여기서는 DiscardServerHandler 를 추가합니다.
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new DiscardServerHandler()); //1
                            //ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    });
            // 서버를 시작하고 연결을 대기합니다.
            ChannelFuture f = b.bind(port).sync(); //2
            //  서버 채널이 닫힐 때까지 대기합니다.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new DiscardServer(port).run();
    }
}
