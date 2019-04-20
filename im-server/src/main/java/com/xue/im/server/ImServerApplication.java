package com.xue.im.server;

import com.xue.im.server.zk.RegiestToZk;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sun.tools.jar.CommandLine;

@SpringBootApplication
public class ImServerApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ImServerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Thread thread = new Thread(new RegiestToZk());
		thread.setName("im-server-regist2Zk-thread");//定义线程名称
		thread.start();
	}
}
