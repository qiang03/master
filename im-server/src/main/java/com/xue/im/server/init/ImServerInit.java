package com.xue.im.server.init;

import com.xue.im.common.protocol.MessageProto;
import com.xue.im.server.config.InitConfiguration;
import com.xue.im.server.handle.ImServerHander;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImServerInit {

  private final static Logger logger = LoggerFactory.getLogger(ImServerInit.class);

  @Autowired
  private InitConfiguration initConfig;
  @PostConstruct
  public void start() throws Exception{
    EventLoopGroup parentGroup = new NioEventLoopGroup();
    //用于进行SockerChannel的网络读写
    EventLoopGroup childGroup = new NioEventLoopGroup();

    try {
      //Netty用于启动NIO服务器的辅助启动类
      ServerBootstrap sb = new ServerBootstrap();
      //将两个NIO线程组传入辅助启动类中
      sb.group(parentGroup,childGroup)
          //设置创建的Channel为NioServerSocketChannel类型
          .channel(NioServerSocketChannel.class)
          //配置NioServerSocketChannel的TCP参数
          .option(ChannelOption.SO_KEEPALIVE,true)
          //设置绑定IO事件的处理类
          .childHandler(new ChannelInitializer<SocketChannel>() {
            //创建NIOSocketChannel成功后，进行初始化时，使它的ChannelHandler设置到ChannelPipeline中，用于处理网络IO事件
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
              ChannelPipeline pipeline = socketChannel.pipeline();

              //google protobuf 编解码
              pipeline.addLast(new ProtobufVarint32FrameDecoder());
              pipeline.addLast(new ProtobufDecoder(MessageProto.MessageProtocol.getDefaultInstance()));
              pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
              pipeline.addLast(new ProtobufEncoder());

              pipeline.addLast(new ImServerHander()); //处理IO
            }
          });
      ChannelFuture cf = sb.bind(initConfig.getNettyPort()).sync();
      if (cf.isSuccess()){
          logger.info("服务端Netty启动成功，端口:"+initConfig.getNettyPort());
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
