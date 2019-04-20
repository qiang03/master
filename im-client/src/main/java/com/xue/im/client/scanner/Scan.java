package com.xue.im.client.scanner;

import com.xue.im.client.config.InitConfiguration;
import com.xue.im.client.config.SpringBeanFactory;
import com.xue.im.client.init.ImClientInit;

import com.xue.im.common.constant.MessageConstant;
import com.xue.im.common.protocol.MessageProto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.Scanner;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

/**
 * 通过客户端线程单独new处理，不被spring容器管理
 */
public class Scan implements Runnable {

  private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ImClientInit.class);

  private ImClientInit client;
  private InitConfiguration config;

  public Scan() {
    //因为是new出来的无法直接从spring容器中获取bean信息
    this.client = SpringBeanFactory.getBean(ImClientInit.class);
    this.config = SpringBeanFactory.getBean(InitConfiguration.class);
  }

  @Override
  public void run() {
    //接收客户端输入
    Scanner scan = new Scanner(System.in);
    while (true) {
      String msg = scan.nextLine();
      if (StringUtils.isEmpty(msg)) {
        logger.info("不允许发送消息");
        continue;
      }

      //直接发送
/*      byte[] req = msg.getBytes();
      ByteBuf buf = Unpooled.buffer(req.length);
      buf.writeBytes(req);
      client.channel.writeAndFlush(buf);*/

      //二：通过protobuf封装发送
      MessageProto.MessageProtocol message = MessageProto.MessageProtocol.newBuilder()
          .setCommand(MessageConstant.CHAT)
          .setTime(System.currentTimeMillis())
          .setUserId(config.getUserId())
          .setContent(msg)
          .build();

      client.channel.writeAndFlush(message);
    }
  }
}
