package com.learn.springboot.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

/**
 * Created by liutao on 17/4/26.
 *
 */
@Controller
public class WebSocketController {
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/chat")
	// 在springmvc 中可以直接获得principal,principal 中包含当前用户的信息
	public void handleChat(Principal principal, Message message) {

		/**
		 * 此处是一段硬编码。如果发送人是wyf 则发送给 wisely 如果发送人是wisely 就发送给 wyf。
		 */
		if (principal.getName().equals("wyf")) {
			// 通过convertAndSendToUser 向用户发送信息,
			// 第一个参数是接收消息的用户,第二个参数是浏览器订阅的地址,第三个参数是消息本身

			messagingTemplate.convertAndSendToUser("wisely", "/queue/notifications",
					principal.getName() + "-send:" + message.getName());
		} else {
			messagingTemplate.convertAndSendToUser("wyf", "/queue/notifications",
					principal.getName() + "-send:" + message.getName());
		}
	}

	// http://localhost:8080/Welcome1
	@RequestMapping(value = "/Welcome1")
	@ResponseBody
	public String say2(Principal principal,@RequestParam("msg") String msg) throws Exception {
		messagingTemplate.convertAndSendToUser("wisely", "/queue/notifications",
				principal.getName() + "-send:" + msg);
		return "is ok";
	}
}