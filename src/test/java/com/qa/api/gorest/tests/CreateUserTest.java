package com.qa.api.gorest.tests;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.qa.api.base.BaseTest;
import com.qa.api.constants.AppConstants;
import com.qa.api.constants.AuthType;
import com.qa.api.manager.ConfigManager;
import com.qa.api.pojo.User;
import com.qa.api.utils.ExcelUtil;
import com.qa.api.utils.StringUtils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CreateUserTest extends BaseTest{
	
	private String tokenId;
	
	@BeforeClass
	public void setUpToken() {
		tokenId = "e4b8e1f593dc4a731a153c5ec8cc9b8bbb583ae964ce650a741113091b4e2ac6";
		ConfigManager.set("bearertoken", tokenId);
	}
	
	
	@DataProvider
	public Object[][] getUserData() {
		return new Object[][] {
			{"Priyanka", "female", "active"},
			{"Ranjit", "male", "inactive"},
			{"Elmar", "male", "active"}
		};
	}
	
	@DataProvider
	public Object[][] getUserExcelData() {
		return ExcelUtil.readData(AppConstants.CREATE_USER_SHEET_NAME);
	}
	
	
	@Test(dataProvider = "getUserExcelData")
	public void createAUserTest(String name, String gender, String status) {
		User user = new User(null, name, StringUtils.getRandomEmailId(), gender, status);			
		
		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.jsonPath().getString("name"), name);
		Assert.assertEquals(response.jsonPath().getString("gender"), gender);
		Assert.assertEquals(response.jsonPath().getString("status"), status);
		Assert.assertNotNull(response.jsonPath().getString("id"));
		
		ChainTestListener.log("user id :::: "+ response.jsonPath().getString("id"));
	}
	
	
	@Test(enabled = false)
	public void createAUserTestWithJsonString() {

		String userJson = "{\n"
				+ "\"name\": \"naveen\",\n"
				+ "\"email\": \"naveen8990@gmail.com\",\n"
				+ "\"gender\": \"male\",\n"
				+ "\"status\": \"active\"\n"
				+ "}";
		
		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, userJson, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.jsonPath().getString("name"), "naveen");
		Assert.assertNotNull(response.jsonPath().getString("id"));	
	}
	
	
	@Test(enabled = false)
	public void createAUserTestWithJsonfile() {

		File userFile = new File("./src/test/resources/jsons/user.json");
		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, userFile, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.jsonPath().getString("name"), "Tom");
		Assert.assertNotNull(response.jsonPath().getString("id"));	
	}
	
	
	
	

}
