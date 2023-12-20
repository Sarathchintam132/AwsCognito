package com.example.exp02;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {
	
	private static final Logger logger =
			LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private final CognitoService cognitoService;
    
    @Autowired
    private CognitoSignup cogSignup;

    public AuthenticationController(CognitoService cognitoService,CognitoSignup cogSignup) {
        this.cognitoService = cognitoService;
        this.cogSignup = cogSignup;
    }

    @GetMapping("/authenticate")
    public ResponseEntity<Map<String,Object>> authenticate(@RequestHeader("Authorization") String authorization) {
        try {
             Map<String,Object> jwt = cognitoService.getCognitoJWT(authorization);
             logger.info("Result {}", jwt);
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
        	Map<String,Object> errorResponse = new HashMap<>();
        	errorResponse.put("errorResponse",e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }  
    
    @PostMapping("/createNewUser")
    public ResponseEntity<String>  createUser(@RequestBody SignUpRequest signup){
    	 try {
             String response = cogSignup.signupUser(signup);
             if (response.isEmpty()){
            	 return ResponseEntity.ok("creation is not succes");
             }else {
           
            	 return ResponseEntity.ok(response+"user creation has succesful");
             }} catch (Exception e) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
         }
    	
    }
}