package com.emblproject.moses;

import com.emblproject.moses.dto.request.CreatePersonRequest;
import com.emblproject.moses.dto.request.UpdatePersonRequest;
import com.emblproject.moses.entity.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = EmblProjectApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmblControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    HttpHeaders headers = new HttpHeaders();

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    @Test
    public void contextLoads() {

    }

    @Test
    public void testGetAllPersons() {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("moses", "mosespass")
                .exchange(getRootUrl() + "/persons",
                HttpMethod.GET, entity, String.class);
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetPersonById() {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                getRootUrl() + "/persons/1", HttpMethod.GET, entity, String.class);
        assertNotNull(response.getBody());
    }

    @Test
    public void testCreatePerson() {
        CreatePersonRequest createPersonRequest = new CreatePersonRequest();
        createPersonRequest.setAge("29");
        createPersonRequest.setFirst_name("moses");
        createPersonRequest.setLast_name("mike");
        createPersonRequest.setFavourite_colour("yellow");
        ResponseEntity<Person> postResponse =
                restTemplate.withBasicAuth("moses", "mosespass")
                            .postForEntity(getRootUrl() + "/persons", createPersonRequest, Person.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
    }

    @Test
    public void testUpdatePerson() {
        Person updatePersonRequest = new Person();
        int id = 1;
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<Person> response =
                restTemplate.withBasicAuth("moses", "mosespass")
                        .exchange(getRootUrl() + "/persons/1", HttpMethod.GET, entity, Person.class);
        updatePersonRequest =  response.getBody();
        assert updatePersonRequest != null;

        updatePersonRequest.setFirst_name("admin1");
        updatePersonRequest.setLast_name("admin2");
        updatePersonRequest.setAge("23");
        updatePersonRequest.setFavourite_colour("pink");

        restTemplate.put(getRootUrl() + "/persons/" + id, updatePersonRequest);
        ResponseEntity<Person> updatedPersonResponse =
                restTemplate.withBasicAuth("moses", "mosespass")
                        .exchange(getRootUrl() + "/persons/" + id, HttpMethod.GET, entity, Person.class);
        assertNotNull(updatedPersonResponse.getBody());
    }

    @Test
    public void testDeletePerson() {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        Person updatePersonRequest;

        int id = 2;
        ResponseEntity<Person> response =
                restTemplate.withBasicAuth("moses", "mosespass")
                        .exchange(getRootUrl() + "/persons/" + id, HttpMethod.GET, entity, Person.class);
        updatePersonRequest =  response.getBody();
        assertNotNull(updatePersonRequest);
        restTemplate.delete(getRootUrl() + "/persons/" + id);
        try {
            ResponseEntity<Person> responseDeleteTest =
                    restTemplate.withBasicAuth("moses", "mosespass")
                            .exchange(getRootUrl() + "/persons/" + id, HttpMethod.GET, entity, Person.class);
        } catch (final HttpClientErrorException e) {
            assertEquals(e.getStatusCode(), Objects.requireNonNull(HttpStatus.NOT_FOUND));
        }
    }


}
