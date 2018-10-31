package project.controller;

<<<<<<< HEAD
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import project.model.ChatInMessage;
import project.model.ChatOutMessage;

@Controller
public class ChatroomController {

	/**
	 * 
	 * 
	 * @param message
	 * @return
	 */
	@MessageMapping("/guestchat")  // here
    @SendTo("/topic/guestchats")  // here
	public ChatOutMessage test(ChatInMessage message) {
		// simulate delay
		
		System.out.println(message);
		System.out.println("!!!!!");
		
		try {
			Thread.sleep((long) (Math.random() * 500 + 50));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ChatOutMessage("(server) " + message.getMessage());
	}
=======
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author Vilhelml
 *
 */
@RestController
@RequestMapping("/chatroom")
public class ChatroomController {

>>>>>>> 19385cde6581dff0eb9c3d83daf2eccdccf76f5a
}
