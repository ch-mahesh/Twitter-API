import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;

import static io.restassured.RestAssured.given;

public class TwitterAPITestcases {

    String sBearer="";

    // This get executed before any test get started
    @BeforeClass
    public void intialize(){

        RestAssured.baseURI="http://api.twitter.com";
        sBearer = "AAAAAAAAAAAAAAAAAAAAAAV8HgEAAAAAVw393xYYk%2FrE%3DO5vZiREySbGeBzjXSBz7uO0f3SXoD8m5UOrRPjK7DnKFGG1W5r";

    }

    // Example for Post method

    @Test
    public void PostTwitterFeed(){

        // set Base path
        RestAssured.basePath="/1.1/statuses/";

        given()
                .queryParam("status", "hello testing rest assured")
                .header(new Header("Authorization", "Bearer "+sBearer))
        .when()
                .post("update.json")
        .then()
                .statusCode(200);

    }

    // Example for Get method

    @Test
    public void getTwitterFollowers(){

        RestAssured.basePath="/1.1/followers/";

        // set Base uri
        Response response = null;
        System.out.println("Mahesh" + sBearer);
        try{
            response = RestAssured.given()
                    .queryParam("cursor", "-1")     //Setting parameters
                    .queryParam("screen_name","twitterdev")
                    .queryParam("skip_status", "true")
                    .queryParam("include_user_entities", "false")
                    .header(new Header("Authorization", "Bearer "+sBearer)) // setting the header
                    .when()
                    .get("list.json");

        }catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals("HTTP/1.1 200 OK", response.getStatusLine());

    }

    @Test
    public void getTwitterFollowersCountCheck(){

        RestAssured.basePath="/1.1/followers/";

        // set Base uri
        Response response = null;
        System.out.println("Mahesh" + sBearer);
        try{
            response = RestAssured.given()
                    .queryParam("cursor", "-1")     //Setting parameters
                    .queryParam("screen_name","twitterdev")
                    .queryParam("skip_status", "true")
                    .queryParam("include_user_entities", "false")
                    .header(new Header("Authorization", "Bearer "+sBearer)) // setting the header
                    .when()
                    .get("list.json");

        }catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(response.getBody().asString());

            // A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
            JSONObject jsonObject = (JSONObject) obj;

            // A JSON array. JSONObject supports java.util.List interface.
            JSONArray UsersList = (JSONArray) jsonObject.get("users");


            System.out.println(jsonObject.get("next_cursor")); // If we would like iterate for more followers, we can use this string for next itertion
            Assert.assertEquals(20, UsersList.size()); // Checking the size of usersList

            // Below code is just incase if we need to verify Users list

            Iterator<JSONObject> iterator = UsersList.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Data Driven Rest API testing

    @Test(dataProvider = "TwitterFeedData")
    public void PostBulkTwitterFeed(String sFeed){

        // set Base path
        RestAssured.basePath="/1.1/statuses/";

        given()
                .queryParam("status", sFeed)
                .header(new Header("Authorization", "Bearer "+sBearer))
                .when()
                .post("update.json")
                .then()
                .statusCode(200);

    }

    // Data provide to feed Data Driven - we can make this fucntion reading from csv or excel (poi) etc
    @DataProvider(name="TwitterFeedData")
    Object[][] getFeedData(){

        String sFeedData[][]={{"1", "Data1"},{"2", "Data2"}, {"3", "Data3"}};
        return (sFeedData);
    }


    // we can write more testcases to verify 1. Header text 2. wrong data, etc
    // we can add tags to these testcases - testng annotations
    // I did not included tesng frame work to include suite XML file which gives flexibility to run mutliple classes and gneerate extend report etc


}
