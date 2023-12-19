package com.example.exp02;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
public class CognitoConfig {

    @Value("${aws.cognito.clientId}")
    private String clientId;

    // The user pool ID is used to initiate admin auth requests
    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;
    
    
    @Value("${aws.region}")
    private String region;
    

//    public String getAccessKeyId() {
//		return accessKeyId;
//	}
//
//	public void setAccessKeyId(String accessKeyId) {
//		this.accessKeyId = accessKeyId;
//	}
//
//	public String getSecretKey() {
//		return secretKey;
//	}
//
//	public void setSecretKey(String secretKey) {
//		this.secretKey = secretKey;
//	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	// Getters and setters
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserPoolId() {
        return userPoolId;
    }

    public void setUserPoolId(String userPoolId) {
        this.userPoolId = userPoolId;
    }
    
    @Bean
    public CognitoIdentityProviderClient cognitoIdentityProviderClient() {
        return CognitoIdentityProviderClient.builder()
                //.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretKey)))
                .region(Region.of(region))
                .build();
    }
}
