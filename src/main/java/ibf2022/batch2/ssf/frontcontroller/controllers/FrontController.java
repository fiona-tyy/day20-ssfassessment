package ibf2022.batch2.ssf.frontcontroller.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import ibf2022.batch2.ssf.frontcontroller.model.User;
import ibf2022.batch2.ssf.frontcontroller.services.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class FrontController {

	@Autowired
	private AuthenticationService aSvc;
	// TODO: Task 2, Task 3, Task 4, Task 6

	private String captcha;

	@GetMapping(path="/")
	public String showLanding(HttpSession session, Model model){
		User user = (User) session.getAttribute("user");
		if(null == user){
			session.setAttribute("user", new User());
		}
		captcha = aSvc.generateCaptcha();
		System.out.println(">>>captcha" + captcha);
		model.addAttribute("user", new User());
		model.addAttribute("captcha", captcha);

		return "view0";
	}

	@PostMapping(path="/login", 
				consumes="application/x-www-form-urlencoded", 
				produces="text/html")
	public String login(HttpSession session, Model model, @ModelAttribute @Valid User user, BindingResult bindings) throws Exception{
		if(bindings.hasErrors()){
			return "view0";
		}

		// if user disabled, display view 2
		if(user.isDisabled()){
			model.addAttribute("user", user);
			return "view2";
		} 

		List<ObjectError> errors = aSvc.validateCaptcha(captcha, user.getCaptchaAnswer());
		if(!errors.isEmpty()){
			for(ObjectError e: errors){
				bindings.addError(e);
			}
			user.setLoginAttempts(user.getLoginAttempts()+1);
			return "view0";
		}


		aSvc.authenticate(user.getUsername(), user.getPassword());
		

		System.out.println(user);

		//authenticate
		return "view1";
	}
}
