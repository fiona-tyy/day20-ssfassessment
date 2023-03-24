package ibf2022.batch2.ssf.frontcontroller.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.client.RestTemplate;

import ibf2022.batch2.ssf.frontcontroller.respositories.AuthenticationRepository;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@Service
public class AuthenticationService {

	@Autowired
	private AuthenticationRepository aRepo;

	private String apiUrl = "https://auth.chuklee.com/api/authenticate";

	// TODO: Task 2
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write the authentication method in here
	public void authenticate(String username, String password) throws Exception {
		JsonObject o = Json.createObjectBuilder()
							.add("username", username)
							.add("password", password)
							.build();
		
		RequestEntity<String> req = RequestEntity.post(apiUrl)
												.contentType(MediaType.APPLICATION_JSON)
												.accept(MediaType.APPLICATION_JSON)
												.body(o.toString(), String.class);
		
		RestTemplate template = new RestTemplate();
		ResponseEntity<String> resp = template.exchange(req, String.class);
		String payload = resp.getBody();
		if(payload.contains("Authenticated")){
			return;
		} else {

		}
	


	}

	

	// TODO: Task 3
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to disable a user account for 30 mins
	public void disableUser(String username) {
		//using username, write to redis
		aRepo.saveDisabledUser(username);

	}

	// TODO: Task 5
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to check if a given user's login has been disabled
	public boolean isLocked(String username) {
		return aRepo.retrieveDisabledUser(username);
	}

	public String generateCaptcha(){

		Random rand = new Random();
		Integer int1 = rand.nextInt(50)+1;
		Integer int2 = rand.nextInt(50)+1;
		Integer randomOperator = rand.nextInt(4);
		String operator = null;
		
		switch(randomOperator){
			case 0:
				operator = " + ";
				break;
			case 1:
				operator = " - ";
				break;
			case 2:
				operator = " * ";
				break;
			case 3:
				operator = " / ";
				break;
			default:
		}

		return int1 + operator + int2;
	}

	public List<ObjectError> validateCaptcha(String captchaString, Integer answer){
		List<ObjectError> errors = new LinkedList<>();
		FieldError error;
		String[] captchaArr = captchaString.split(" ");
		Integer int1= Integer.parseInt(captchaArr[0]);
		Integer int2= Integer.parseInt(captchaArr[2]);
		String randomOperator = captchaArr[1];
		Integer total = 0;
		switch(randomOperator){
			case "+":
				total = int1 + int2;
				break;
			case "-":
				total = int1 - int2;
				break;
			case "*":
				total = int1 * int2;
				break;
			case "/":
				total = int1 / int2;
				break;
			default:
		}

		if(!total.equals(answer)){
			error = new FieldError("user", "captcha", "Wrong captcha");
			errors.add(error);
		}

		return errors;
	}
	
}
