package com.pact.consumer;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Client {

  private final String url;
  private final Gson gson ;
  public Client(String url) {
    this.url = url;
    this.gson = new Gson();
  }

  private Optional<JsonNode> loadUserJson(int id) throws UnirestException {
    HttpRequest getRequest = Unirest.get(url + "/provider/users/"+id);


    HttpResponse<JsonNode> jsonNodeHttpResponse = getRequest.asJson();
    if (jsonNodeHttpResponse.getStatus() == 200) {
      return Optional.of(jsonNodeHttpResponse.getBody());
    } else {
      return Optional.empty();
    }
  }

  public List<User> fetchUserData(int id) throws UnirestException {
    Optional<JsonNode> data = loadUserJson(id);
    System.out.println("data=" + data);

    if (data != null && data.isPresent()) {
      JSONObject jsonObject = data.get().getObject();
      String name = jsonObject.getString("name");
      String role = jsonObject.getString("role");

      System.out.println("name = " + name);
      System.out.println("role = " + role);

      return Arrays.asList(gson.fromJson(jsonObject.toString(), User.class));
    } else {
      return Arrays.asList(new User());
    }
  }
}
