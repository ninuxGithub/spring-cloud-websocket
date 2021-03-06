package com.example.demo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.bean.SocketMessage;

@SpringBootApplication
@EnableScheduling
@Controller
public class SpringBootWebsoketApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebsoketApplication.class, args);
	}

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/scrollLoading")
	public String scrollLoading() {
		return "scrollLoading";
	}

	@MessageMapping("/send")
	@SendTo("/topic/send")
	public SocketMessage send(SocketMessage message) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		message.setDate(df.format(new Date()));
		return message;
	}

	@Scheduled(fixedRate = 1000)
	@SendTo("/topic/callback")
	public Object callback() throws Exception {
		// 发现消息
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		messagingTemplate.convertAndSend("/topic/callback", df.format(new Date()));
		return "callback";
	}
}
