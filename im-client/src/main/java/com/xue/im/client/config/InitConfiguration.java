package com.xue.im.client.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class InitConfiguration {

  @Value("${im.user.userId}")
  private Integer userId;
  @Value("${im.user.userName}")
  private String userName;
  @Value("${im.route.login}")
  private String routeLoginUrl;
}
