package Ex_Act;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo; 

public class Activity3 {
	RequestSpecification requestSpec;
	ResponseSpecification responseSpec;

	@BeforeClass
	public void setUp() {
		requestSpec = new RequestSpecBuilder()
				.setContentType(ContentType.JSON)
				.setBaseUri("https://petstore.swagger.io/v2/pet")
				.build();

		responseSpec = new ResponseSpecBuilder()
				.expectStatusCode(200)
				.expectContentType("application/json")
				.expectBody("status", equalTo("alive"))
				.build();
	}

	@DataProvider
	public Object[][] petIdProvider() {
		Object[][] testData = new Object[][] { 
			{77236}, {77237}};
		return testData;
	}

	@Test(priority=1)
	public void PostNewPet(){
		String reqBody1 = "{\"id\": 77236,\"name\": \"myActivity31\", \"status\": \"alive\"}"; 
		String reqBody2 = "{\"id\": 77237,\"name\": \"myActivity32\", \"status\": \"alive\"}"; 
		Response response1 =
				given().spec(requestSpec)
				.body(reqBody1).when().post();
		Response response2 =
				given().spec(requestSpec)
				.body(reqBody2).when().post();

		System.out.println("POST Response 1--> "+response1.getBody().asPrettyString());
		response1.then().spec(responseSpec);
		System.out.println("POST Response 2--> "+response2.getBody().asPrettyString());
		response2.then().spec(responseSpec);
	}

	
	@Test(priority=2, dataProvider = "petIdProvider")
	public void GetPostedPet(int id) {
		Response response = 
				given().spec(requestSpec)
				.when().pathParam("petId", id)
				.get("/{petId}");

		System.out.println("GET Response--> "+response.getBody().asPrettyString());
		response.then().spec(responseSpec);
	}

	
	@Test(priority=3, dataProvider = "petIdProvider")
	public void DeletePostedPet(int id) {
		Response response = 
				given().spec(requestSpec)
				.when().pathParam("petId", id)
				.when().delete("/{petId}");

		response.then().body("code", equalTo(200));
		System.out.println("DELETE Response--> "+response.getBody().asPrettyString());
	}
}
