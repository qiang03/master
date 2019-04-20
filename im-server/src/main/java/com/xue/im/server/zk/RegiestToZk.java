package com.xue.im.server.zk;

import com.xue.im.server.config.InitConfiguration;
import com.xue.im.server.config.SpringBeanFactory;
import com.xue.im.server.init.ImServerInit;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegiestToZk implements Runnable {
  private final static Logger logger = LoggerFactory.getLogger(ImServerInit.class);

  public InitConfiguration initconfig;
  private ZKUtil zk;
  public RegiestToZk(){
    this.initconfig= SpringBeanFactory.getBean(InitConfiguration.class);
    this.zk = SpringBeanFactory.getBean(ZKUtil.class);
  }

  @Override
  public void run() {
    try {
      String ip = InetAddress.getLocalHost().getHostAddress();
//      String ip = "192.168.79.128";
      Integer httpPort = initconfig.getHttpPort();
      Integer nettyPort = initconfig.getNettyPort();
      //注册到zookeeper上
      logger.info("服务端注册到zookeeper中，ip:"+ip+";httpPort:"+httpPort+";nettyPort:"+nettyPort);
      //尝试创建跟节点
      zk.createRootNode();
      if(initconfig.isZkSwitch()){
        String path=initconfig.getRoot()+"/"+ip+"-"+nettyPort+"-"+httpPort;
        zk.createNode(path);
        logger.info("服务器注册成功。");
      }
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }
}
