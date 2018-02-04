package com.tazine.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.util.CharsetUtil;

/**
 * Created by lina on 2018/2/4.
 *
 * @author frank
 * @since 1.0.0
 */
public class MyWebSocketHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handshaker;

    private static final String WEB_SOCKET_URL = "ws://127.0.0.1:8888/websocket";


    /**
     * 出现异常时调用
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 服务端接收客户端发来的数据结束之后调用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     * 客户端与服务端建立连接时调用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyConfig.group.add(ctx.channel());
        System.out.println("客户端与服务端建立连接...");
    }

    /**
     * 客户端与服务端断开连接时调用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyConfig.group.remove(ctx.channel());
        System.out.println("客户端与服务端关闭连接...");
    }

    /**
     * 核心方法：服务端接收客户端信息
     *
     * @param channelHandlerContext
     * @param o
     * @throws Exception
     */
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        // 处理客户端向服务端发起HTTP握手请求的任务
        if (o instanceof FullHttpRequest) {

        } else if (o instanceof WebSocketFrame) {
            // 处理 WebSocket 连接业务
        }
    }

    /**
     * 处理客户端向服务端发起 Http 请求任务
     *
     * @param ctx
     * @param request
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        /**
         * 客户端向服务端发起 Http 请求时，会带上特殊的请求头  Upgrade != websocket 就不是握手请求
         */
        if (!request.getDecoderResult().isSuccess() || !"websocket".equalsIgnoreCase(request.headers().get("Upgrade"))) {

        }
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse resp) {
        if (resp.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(resp.getStatus().toString(), CharsetUtil.UTF_8);
            resp.content().writeBytes(buf);
            buf.clear();
        }
        // 服务端向客户端发送数据
        ChannelFuture f = ctx.channel().writeAndFlush(resp);
    }
}
