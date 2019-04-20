package com.xue.im.client;

import com.xue.im.client.scanner.Scan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImClientApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ImClientApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		Thread thread = new Thread(new Scan());
		thread.setName("im-client-thread");//定义线程名称
		thread.start();
	}
}
