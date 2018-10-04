package hugbunadar_verkefni_hopur_1_veryWowchat.Very_Wow_Chat;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@RequestMapping("/hello")
	public String hello() {
		return "{\"content\":\"Hello nub\"}";
	}
}
