package com.xue.im.server.handle;

import com.xue.im.common.protocol.MessageProto;
import com.xue.im.common.protocol.MessageProto.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImServerHander extends ChannelInboundHandlerAdapter {

  private final static  Logger logger = LoggerFactory.getLogger(ImServerHander.class);

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    //读数据
  /*  ByteBuf buf = (ByteBuf) msg;
    byte[] req = new byte[buf.readableBytes()];
    buf.readBytes(req);
    String body = new String(req,"UTF-8");
    logger.info("服务端接收到数据："+body);*/

    MessageProto.MessageProtocol message= (MessageProto.MessageProtocol) msg;
    logger.info("服务端接收到数据:["+message.getUserId()+"发送："+message.getContent()+"]");
  }
}
