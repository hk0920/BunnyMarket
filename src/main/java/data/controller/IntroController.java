package data.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/intro")
public class IntroController {
	
	@GetMapping("/main")
	public String intro()
	{
		return "/intro/main";
	}

}