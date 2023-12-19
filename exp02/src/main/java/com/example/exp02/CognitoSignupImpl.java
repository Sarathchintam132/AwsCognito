package com.example.exp02;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;

@Service
public class CognitoSignupImpl implements CognitoSignup {
	
	private final CognitoIdentityProviderClient cognitoClient;
	private final CognitoConfig cognitoConfig;
	
	@Autowired
	public CognitoSignupImpl(CognitoIdentityProviderClient cognitoClient,CognitoConfig cognitoConfig) {
		this.cognitoClient = cognitoClient;
		this.cognitoConfig = cognitoConfig;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String signupUser(SignUpRequest signupRequest) {
		
//		CognitoIdentityProviderClient.builder()
//        //.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretKey)))
//        .region(Region.of(region))
//        .build();
		 //CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.create()
	            AttributeType emailAttribute = AttributeType.builder()
	                    .name("email")
	                    .value(signupRequest.getEmail())
	                    .build();

	            AttributeType customAttribute = AttributeType.builder()
	                    .name("custom:Role")
	                    .value(signupRequest.getCustomAttributeValue())
	                    .build();

	            AdminCreateUserRequest userRequest = AdminCreateUserRequest.builder()
	                    .userPoolId(cognitoConfig.getUserPoolId())
	                    .username(signupRequest.getUsername())
	                    .temporaryPassword(signupRequest.getPassword()) // Consider handling password securely
	                    .userAttributes(emailAttribute, customAttribute)
	                    .build();

	            AdminCreateUserResponse adminResponse = cognitoClient.adminCreateUser(userRequest);
	            // Handle the response as needed
	            System.out.println(adminResponse.toString());
	            return adminResponse.toString();
	            
	        }
	    }
		
		
	

