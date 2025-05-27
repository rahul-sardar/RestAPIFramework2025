package com.qa.api.gorest.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.pojo.User;
import com.qa.api.utils.StringUtils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class DeleteUserTest extends BaseTest{
	
	
	@Test
	public void deleteUserTest() {
		//1. create a user - POST
		//User user = new User("Priyanka", StringUtils.getRandomEmailId(), "female", "active");
		
		User user = User.builder()
			.name("Rahul")
			.email(StringUtils.getRandomEmailId())
			.status("inactive")
			.gender("male")
			.build();
		
		Response responsePost = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(responsePost.jsonPath().getString("name"), "Rahul");
		Assert.assertNotNull(responsePost.jsonPath().getString("id"));
		
		//fetch the userId:
		String userId = responsePost.jsonPath().getString("id");
		System.out.println("user id ====>" +  userId);
		ChainTestListener.log("user id : "+ userId);
	
		
		//2. GET: fetch the user using the same user id:
		Response responseGet = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertTrue(responseGet.statusLine().contains("OK"));		
		Assert.assertEquals(responseGet.jsonPath().getString("id"), userId);
		
		//3. Delete the user using the same user id:
		Response responseDelete = restClient.delete(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertTrue(responseDelete.statusLine().contains("No Content"));	
		
		//4. GET: fetch the deleted user using the same user id:
		responseGet = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertTrue(responseGet.statusLine().contains("Not Found"));
		Assert.assertEquals(responseGet.statusCode(), 404);
		Assert.assertEquals(responseGet.jsonPath().getString("message"), "Resource not found");

		
	}
	
	
	
	
	

}
