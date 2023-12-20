package com.example.exp02;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.HashMap;
import java.util.Map;

@Service
public class CognitoServiceImpl implements CognitoService {

    private final CognitoIdentityProviderClient cognitoClient;
    private final CognitoConfig cognitoConfig;

    @Autowired
    public CognitoServiceImpl(CognitoIdentityProviderClient cognitoClient, CognitoConfig cognitoConfig) {
        this.cognitoClient = cognitoClient;
        this.cognitoConfig = cognitoConfig;
    }

    @Override
    public Map<String, Object> getCognitoJWT(String authString) throws Exception {
        Map<String, String> credentials = parseAuthString(authString);
        return authenticateWithCognito(credentials.get("username"), credentials.get("password"));
    }

    private Map<String, Object> authenticateWithCognito(String username, String password) {
        AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
                .clientId(cognitoConfig.getClientId())
                .userPoolId(cognitoConfig.getUserPoolId())
                .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                .authParameters(Map.of("USERNAME", username, "PASSWORD", password))
                .build();

        AdminInitiateAuthResponse authResponse = cognitoClient.adminInitiateAuth(authRequest);
        
        String jwtToken = authResponse.authenticationResult().idToken();
//        Jws<Claims> jwt = Jwts.parser()
//                .parseClaimsJws(jwtToken);
//        Claims claims = jwt.getBody();
//        System.out.println(claims);
        try {
        
        JWT jwt = JWTParser.parse(jwtToken);
        JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
        System.out.println("jwt : "+claimsSet);
        
        //ReadOnlyJWTClaimsSet claimsSet = (ReadOnlyJWTClaimsSet) jwt.getJWTClaimsSet();
        }catch (Exception e) {
        	System.out.println(e.getMessage());
        }
    
        Map<String,Object> response = new HashMap<>();
        response.put("idToken",authResponse.authenticationResult().idToken());
        response.put("refreshToken",authResponse.authenticationResult().refreshToken());
        response.put("AccessToken",authResponse.authenticationResult().accessToken());
        
        return response;
    }

    private Map<String, String> parseAuthString(String authString) throws Exception {
        if (authString == null || !authString.toLowerCase().startsWith("basic")) {
            throw new Exception("The authorization header is either empty or isn't Basic.");
        }

        String base64Credentials = authString.substring("Basic".length()).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded);

        final String[] values = credentials.split(":", 2);
        if (values.length == 0) {
            throw new Exception("Failed to extract credentials from auth string");
        }

        Map<String, String> result = new HashMap<>();
        result.put("username", values[0]);
        result.put("password", values[1]);
        return result;
    }
}
