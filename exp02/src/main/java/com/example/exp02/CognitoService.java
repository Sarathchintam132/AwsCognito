package com.example.exp02;

import java.util.Map;

public interface CognitoService {
	
	
	Map<String, Object> getCognitoJWT(String authString) throws Exception;
}
