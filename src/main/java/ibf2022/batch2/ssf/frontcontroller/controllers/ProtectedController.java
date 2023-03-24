package ibf2022.batch2.ssf.frontcontroller.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/protected")
public class ProtectedController {

	// TODO Task 5
	// Write a controller to protect resources rooted under /protected


	@GetMapping("/view1.html")
	public String accessProtected(){
		return "view1";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session){
		session.invalidate();
		return "redirect:/";
	}
}
