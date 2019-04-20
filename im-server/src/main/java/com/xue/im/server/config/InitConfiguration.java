package com.xue.im.server.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class InitConfiguration {

  @Value("${im.zk.switch}")
  private boolean zkSwitch;
  @Value("${im.zk.addr}")
  private String addr;
  @Value("${im.zk.root}")
  private String root;

  @Value("${server.port}")
  private Integer httpPort;
  @Value("${im.server.port}")
  private Integer nettyPort;

}
