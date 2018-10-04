package project.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import project.services.HelloReturner;

@RestController
public class HelloController {
	private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    
	@RequestMapping("/hello")
    @ResponseBody
    public HelloReturner sayHello(@RequestParam(name="name", required=false, defaultValue="Stranger") String name) {
        return new HelloReturner(counter.incrementAndGet(), String.format(template, name));
    }
}
