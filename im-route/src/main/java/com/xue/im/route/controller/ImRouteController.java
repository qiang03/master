package com.xue.im.route.controller;

import com.xue.im.common.pojo.ServerInfo;
import com.xue.im.common.pojo.UserInfo;
import com.xue.im.common.untils.BasicConstant;
import com.xue.im.route.zk.ZKUtil;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class ImRouteController {

  private final static Logger logger = LoggerFactory.getLogger(ImRouteController.class);

  @Autowired
  private ZKUtil zk;
  @Autowired
  private RedisTemplate<String ,String> redisTemplate;
//
  //zk节点下标，高并发下
  private AtomicLong index = new AtomicLong();
  /**
   * 客户端用户发现服务端的接口
   * 1、获取所有的ZK上的server节点
   * 2、自己实现一个轮询算法，得到一个Server节点
   * 3、返回客户端与server的映射关系（redis）
   * 3、返回这个Server节点的信息（ip+port）
   */
  @PostMapping("/login")
  public ServerInfo login(@RequestBody UserInfo user){
    String serverStr ="";
    List<String> all = zk.getAllNode();
    if (all.size()<=0){
      logger.info("没有可用server节点");
      return null;
    }
    Long idx = index.incrementAndGet()%all.size();
    serverStr = all.get(idx.intValue());

    redisTemplate.opsForValue().set(BasicConstant.ROUTE_PREFIX+user.getUserId(),serverStr);
    String[] split = serverStr.split("-");
    ServerInfo serverInfo = new ServerInfo(split[0],Integer.parseInt(split[1]),Integer.parseInt(split[2]));
    return serverInfo;
  }
}
