package com.xue.im.common.pojo;

import java.io.Serializable;
import lombok.Data;

@Data
public class ServerInfo implements Serializable {

  private static final long serialVersionUID = -1610803139396676632L;

  private String ip;
  private Integer nettyPort;
  private Integer httpPort;

   public ServerInfo(String ip,Integer nettyPort,Integer httpPort){
     super();
     this.ip =ip;
     this.nettyPort=nettyPort;
     this.httpPort=httpPort;
   }

  public ServerInfo() {
    super();
    // TODO Auto-generated constructor stub
  }
}
