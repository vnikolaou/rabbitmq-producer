package com.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class TestController {
	@Autowired
	private Producer producer;
	
    @RequestMapping(value="/send", method=RequestMethod.GET)
    public @ResponseBody void sendMessage() {
    	System.out.println("TestController::sendMessage()");
    	try {
    		producer.send("Hi Vagelis");
    	} catch(Exception ex) {
    		ex.printStackTrace();
    	}
    }
}
