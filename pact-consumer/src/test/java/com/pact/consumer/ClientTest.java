package com.pact.consumer;

import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import static org.hamcrest.Matchers.hasSize;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ClientTest {

    // This sets up a mock server that pretends to be our provider
    @Rule
    public PactProviderRule provider = new PactProviderRule("user-provider", "localhost", 1234, this);

    // This defines the expected interaction for out test
    @Pact(provider = "user-provider", consumer = "user-consumer")
    public RequestResponsePact pactUserExists(PactDslWithProvider builder) {
        return builder
                .given("user 1 exists")
                .uponReceiving("request to users/1")
                .path("/provider/users/1")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(
                        new PactDslJsonBody()
                                .stringType("name", "vineet")
                                .stringType("role", "admin")
                )
                .toPact();
    }

    @Test
    @PactVerification(value = "user-provider", fragment = "pactUserExists")
    public void pactWithOurProvider() throws UnirestException {
        // Set up our HTTP client class
        Client client = new Client(provider.getUrl());

        // Invoke out client
        List<User> result = client.fetchUserData(1);

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getName(), is("vineet"));
        assertThat(result.get(0).getRole(), is("admin"));
    }
}
