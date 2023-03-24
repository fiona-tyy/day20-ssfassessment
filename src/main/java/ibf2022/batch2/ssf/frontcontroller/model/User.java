package ibf2022.batch2.ssf.frontcontroller.model;

import java.io.Serializable;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class User implements Serializable{

    @NotBlank(message="Please provide your username")
    @Size(min=2, message="Username must at least be 2 characters in length")
    private String username;

    @NotBlank(message="Please provide your password")
    @Size(min=2, message="Password must at least be 2 characters in length")
    private String password;

    private Integer captchaAnswer;

    private Integer loginAttempts = 0 ;
    private boolean isAuthenticated = false;

    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Integer getCaptchaAnswer() {
        return captchaAnswer;
    }
    public void setCaptchaAnswer(Integer captchaAnswer) {
        this.captchaAnswer = captchaAnswer;
    }
    public Integer getLoginAttempts() {
        return loginAttempts;
    }
    public void setLoginAttempts(Integer loginAttempts) {
        this.loginAttempts = loginAttempts;
    }
    
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
    public void setAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }
    
    
    @Override
    public String toString() {
        return "User [username=" + username + ", password=" + password + ", captchaAnswer=" + captchaAnswer
                + ", loginAttempts=" + loginAttempts + ", isAuthenticated=" + isAuthenticated + "]";
    }

    public JsonObject toJSON(){
        return Json.createObjectBuilder()
                .add("username", this.getUsername())
                .add("password", this.getPassword())
                .build();
    }
   

}
