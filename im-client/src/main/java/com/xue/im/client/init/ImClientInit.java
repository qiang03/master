package com.xue.im.client.init;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xue.im.client.config.InitConfiguration;
import com.xue.im.client.handler.ImClientHandler;
import com.xue.im.common.pojo.ServerInfo;
import com.xue.im.common.protocol.MessageProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import java.io.IOException;
import javax.annotation.PostConstruct;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 启动客户端（绑定服务端启动端口）
 */
@Component
public class ImClientInit {

  private final static Logger logger = LoggerFactory.getLogger(ImClientInit.class);

  public Channel channel;

  private ServerInfo serverInfo;
  @Autowired
  private OkHttpClient okHttpClient;
  @Autowired
  private InitConfiguration config;

  @PostConstruct
  public void start() throws Exception{
    //1、从Rout得到服务端的IP+port
    getServerInfo();
    //2、启动服务
    startClient();
    //3、

 /*   //用于进行SockerChannel的网络读写
    EventLoopGroup group = new NioEventLoopGroup();

    try {
      Bootstrap bs = new Bootstrap();
      bs.group(group)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.TCP_NODELAY,true)
          //设置绑定IO事件的处理类
          .handler(new ChannelInitializer<SocketChannel>() {
            //创建NIOSocketChannel成功后，进行初始化时，使它的ChannelHandler设置到ChannelPipeline中，用于处理网络IO事件
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
              ChannelPipeline pipeline = socketChannel.pipeline();

              //google protobuf 编解码
              pipeline.addLast(new ProtobufVarint32FrameDecoder());
              pipeline.addLast(new ProtobufDecoder(MessageProto.MessageProtocol.getDefaultInstance()));
              pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
              pipeline.addLast(new ProtobufEncoder());

              pipeline.addLast(new ImClientHandler()); //处理IO


            }
          });
      ChannelFuture cf = bs.connect("127.0.0.1",8088).sync();
      if (cf.isSuccess()){
        logger.info("Netty客户端启动成功，连接端口：8088");
        channel = cf.channel();
      }
//      cf.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }*/
  }

  private void getServerInfo(){
    try {
      JSONObject json = new JSONObject();
      json.put("userId", config.getUserId());
      json.put("userName", config.getUserName());

      MediaType mediaType = MediaType.parse("application/json");
      RequestBody requestBody = RequestBody.create(mediaType, json.toString());
      Request request = new Request.Builder()
          .url(config.getRouteLoginUrl())
          .post(requestBody)
          .build();
      Response response = okHttpClient.newCall(request).execute();
      if(!response.isSuccessful()){
        logger.error("---客户端获取server节点失败！");
        throw new IOException("---客户端获取server节点失败！");
      }

        ResponseBody body = response.body();
      try {
        String responseStr  = body.string();
        this.serverInfo = JSON.parseObject(responseStr,ServerInfo.class);
        logger.info("--得到服务端Server节点   ip:"+serverInfo.getIp());
      } finally {
        body.close();
        response.close();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void startClient() {
    //用于进行SockerChannel的网络读写
    EventLoopGroup group = new NioEventLoopGroup();

    try {
      Bootstrap bs = new Bootstrap();
      bs.group(group)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.TCP_NODELAY,true)
          //设置绑定IO事件的处理类
          .handler(new ChannelInitializer<SocketChannel>() {
            //创建NIOSocketChannel成功后，进行初始化时，使它的ChannelHandler设置到ChannelPipeline中，用于处理网络IO事件
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
              ChannelPipeline pipeline = socketChannel.pipeline();

              //google protobuf 编解码
              pipeline.addLast(new ProtobufVarint32FrameDecoder());
              pipeline.addLast(new ProtobufDecoder(MessageProto.MessageProtocol.getDefaultInstance()));
              pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
              pipeline.addLast(new ProtobufEncoder());

              pipeline.addLast(new ImClientHandler()); //处理IO


            }
          });
      ChannelFuture cf = bs.connect(serverInfo.getIp(),serverInfo.getNettyPort()).sync();
      if (cf.isSuccess()){
        logger.info("Netty客户端启动成功，连接ip:"+serverInfo.getIp()+"端口："+serverInfo.getNettyPort());
        channel = cf.channel();
      }
//      cf.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
