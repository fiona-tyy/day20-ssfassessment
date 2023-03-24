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

	private String captcha = null;

	@GetMapping(path="/")
	public String showLanding(HttpSession session, Model model){
		User user = (User) session.getAttribute("user");
		if(null == user){
			user = new User();
			session.setAttribute("user", user);

		model.addAttribute("user", new User());
		}
		System.out.println(">>>Login attempts " + user.getLoginAttempts());
		if (user.getLoginAttempts() > 0){
			captcha = aSvc.generateCaptcha();

			model.addAttribute("captcha", captcha);
		}
		model.addAttribute("user", user);
		session.setAttribute("user", user);

		return "view0";
	}

	@PostMapping(path="/login", 
				consumes="application/x-www-form-urlencoded", 
				produces="text/html")
	public String login(HttpSession session, Model model, @ModelAttribute @Valid User user, BindingResult bindings) throws Exception{
		if(bindings.hasErrors()){
			return "view0";
		}

		User u = (User) session.getAttribute("user");
		u.setUsername(user.getUsername());
		u.setPassword(user.getPassword());
		u.setCaptchaAnswer(user.getCaptchaAnswer());
		System.out.println(u);
		// if user disabled, display view 2
		if(aSvc.isLocked(u.getUsername())){
			model.addAttribute("user", u);
			return "view2";
		} 
		try {
			aSvc.authenticate(u.getUsername(), u.getPassword());

		} catch (Exception e) {
			System.out.println(">>>> exceptionthrowns");
			// TODO: getmessage
			System.out.println(">before "+ u.getLoginAttempts());
			u.setLoginAttempts(u.getLoginAttempts() + 1);
			System.out.println(">> After " + u.getLoginAttempts());
			session.setAttribute("user", u);
			if (u.getLoginAttempts() >=3){
				// System.out.println(u.getUsername());
				aSvc.disableUser(u.getUsername());
				return "view2";
			} else {
				return "redirect:/";
			}
		}

			//validate captcha
		if (u.getLoginAttempts()>0){
		
			List<ObjectError> errors = aSvc.validateCaptcha(captcha, u.getCaptchaAnswer());
			if(!errors.isEmpty()){
				for(ObjectError e: errors){
					bindings.addError(e);
				}
				u.setLoginAttempts(u.getLoginAttempts()+1);

				if (u.getLoginAttempts() >=3){
					// System.out.println(u.getUsername());
					aSvc.disableUser(u.getUsername());
					return "view2";
				} else {
					return "redirect:/";
				}
	
			}
		}
		
		return "redirect:/protected/view1.html";

	

	}

	@GetMapping("/protected/view1.html")
	public String accessProtected(){
		return "view1";
	}

	@GetMapping("/protected/logout")
	public String logout(HttpSession session){
		session.invalidate();
		return "redirect:/";
	}
}
