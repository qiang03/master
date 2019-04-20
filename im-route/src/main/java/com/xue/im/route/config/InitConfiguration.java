package com.xue.im.route.config;

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


}
