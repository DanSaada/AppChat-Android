package com.appchat.api;

import com.appchat.Adapters.Json2EntityAdapter;
import com.appchat.AppStateManager;
import com.appchat.entities.Contact;
import com.appchat.entities.Message;
import com.appchat.entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactApiTest {

    private WebServiceApi webServiceApi;
    private boolean isSuccessful;

    @Before
    public void setUp() throws Exception {

        String serverUrl = "http://10.0.2.2:";
        String serverPort = "5000";


        isSuccessful = false;
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverUrl + serverPort + "/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webServiceApi = retrofit.create(WebServiceApi.class);
    }

    @Test
    public void getAllContacts() {
        try {
            // register two users
            User user = new User("maui12", "hello", "maui12", "maui12Pic");
            User user2 = new User("zazi34", "hello", "zazi34", "zazi34Pic");

            Response<JsonObject> responseMaui12 = webServiceApi.postUser(user).execute();
            Response<JsonObject> responseZazi34 = webServiceApi.postUser(user2).execute();

            // test that the response is successful
            Assert.assertEquals(200, responseMaui12.code());
            Assert.assertEquals(200, responseZazi34.code());
            Assert.assertNotNull(responseMaui12.body());
            Assert.assertNotNull(responseZazi34.body());

            // simulating login
            Response<JsonPrimitive> call = webServiceApi.createToken(user).execute();
            Assert.assertEquals(200, call.code());
            Assert.assertNotNull(call.body());
            String token = call.body().toString();
            String loggerUserTokenTest = "Bearer " + token.substring(1, token.length() - 1);
            AppStateManager.loggedUser = user.getUsername();
            AppStateManager.contactId = user2.getUsername();

            // simulating adding a contact-chat:
            // sender - maui12, receiver - zazi34
            JSONObject requestBody = new JSONObject();
            requestBody.put("username", "zazi34");

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestBody.toString());
            Response<JsonObject> addContactResponse = webServiceApi.postContact(body, loggerUserTokenTest).execute();
            Assert.assertEquals(200, addContactResponse.code());
            Assert.assertNotNull(addContactResponse.body());
            // Create a new contact object with the id and name of the new contact
            JsonObject responseBody = addContactResponse.body();
            String id = responseBody.getAsJsonPrimitive("id").getAsString();
            JsonObject userObject = responseBody.getAsJsonObject("user");
            String username = userObject.getAsJsonPrimitive("username").getAsString();
            String displayName = userObject.getAsJsonPrimitive("displayName").getAsString();
            String profilePic = userObject.getAsJsonPrimitive("profilePic").getAsString();
            Contact newContact = new Contact(id, displayName, null,
                    null, username, 0, profilePic);

            System.out.println("newContact: " + newContact);

            // now get the new contact for real
            Response<List<JsonObject>> response = webServiceApi.getAllContacts(loggerUserTokenTest).execute();
            Assert.assertEquals(200, response.code());
            Assert.assertNotNull(response.body());
            List<Contact> contacts = Json2EntityAdapter.Json2ContactList(response.body());
            System.out.println("contacts: " + contacts);


            // continue test for send message to this contact
            for (int i = 0; i < contacts.size(); i++) {
                if (contacts.get(i).getUsername().equals(newContact.getUsername())) {
                    // send 5 messages to this contact
                    for (int j = 0; j < 5; j++) {
                        testSendMessage(contacts.get(i).getId(), "message test #" + j, loggerUserTokenTest);
                    }
                }
            }

            // now test hte getMessages
            testGetMessages(newContact.getId(), loggerUserTokenTest);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void testSendMessage(String chatID, String message, String token) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("msg", message);
            RequestBody body = RequestBody.create(MediaType.parse("application/json"),
                    requestBody.toString());
            Response<JsonObject> call = webServiceApi.postMessage(chatID, body,
                    token).execute();
            Assert.assertEquals(200, call.code());
            Assert.assertNotNull(call.body());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void testGetMessages(String chatID, String token) {
        try {
            Response<List<JsonObject>> call = webServiceApi.getMessages(chatID, token).execute();
            Assert.assertEquals(200, call.code());
            Assert.assertNotNull(call.body());
            List<Message> messages = Json2EntityAdapter.Json2MessageList(call.body());
            System.out.println("messages: " + messages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}