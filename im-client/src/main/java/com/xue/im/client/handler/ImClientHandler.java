package com.xue.im.client.handler;

import com.xue.im.common.constant.MessageConstant;
import com.xue.im.common.protocol.MessageProto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ImClientHandler extends ChannelInboundHandlerAdapter {


/*
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {

//    byte[] req = "user[01]加入聊天室".getBytes();
//    ByteBuf msg = Unpooled.buffer(req.length);
//    msg.writeBytes(req);
//    ctx.writeAndFlush(msg);

    MessageProto.MessageProtocol message = MessageProto.MessageProtocol.newBuilder()
        .setCommand(MessageConstant.CHAT)
        .setTime(System.currentTimeMillis())
        .setUserId(1001)
        .setContent("user[01]加入聊天室")
        .build();
    ctx.writeAndFlush(message);
  }
*/

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

  }
}
