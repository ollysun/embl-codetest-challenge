package com.emblproject.moses.controller;

import com.emblproject.moses.dto.request.CreatePersonRequest;
import com.emblproject.moses.dto.request.UpdatePersonRequest;
import com.emblproject.moses.entity.Person;
import com.emblproject.moses.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Map;

@RestController
@RequestMapping("/persons")
@Validated
public class PersonController {

    @Autowired
    private IPersonService ipersonService;

        @GetMapping
        public ResponseEntity<Map<String, Object>> getAllPersons(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "3") int size) {
            Map<String, Object> response = ipersonService.getPersons(page, size);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Person> getPersonById(@Positive @PathVariable("id") long id) {
            Person person = ipersonService.getPersonById(id);
            return ResponseEntity.ok().body(person);
        }

        @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE},
                consumes={MediaType.APPLICATION_JSON_VALUE})
        public ResponseEntity<Person> createPerson(@Valid @RequestBody CreatePersonRequest createPersonRequest) {

            Person person = ipersonService.createPerson(createPersonRequest);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            return new ResponseEntity<>(person, headers,HttpStatus.CREATED);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Person> updatePerson(@Positive @PathVariable("id") long id,
                                                   @Valid @RequestBody UpdatePersonRequest updatePersonRequest){
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            return new ResponseEntity<>(ipersonService.updatePerson(id,updatePersonRequest),headers, HttpStatus.OK);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<HttpStatus> deletePersonById(@PathVariable("id") @Positive long id) {
            if (Boolean.TRUE.equals(ipersonService.deletePersonById(id))){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }

        @DeleteMapping
        public ResponseEntity<HttpStatus> deleteAllPersons() {
            ipersonService.deleteAllPersons();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }
