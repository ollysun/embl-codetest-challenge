package com.emblproject.moses.controller;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.emblproject.moses.dto.request.CreatePersonRequest;
import com.emblproject.moses.dto.request.UpdatePersonRequest;
import com.emblproject.moses.entity.Person;
import com.emblproject.moses.service.IPersonService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {PersonController.class})
@ExtendWith(SpringExtension.class)
public class PersonControllerTest {
    @MockBean
    private IPersonService iPersonService;

    @Autowired
    private PersonController personController;

    @Test
    public void testGetPersonById() throws Exception {
        Person person = new Person();
        person.setFavourite_colour("Favourite colour");
        person.setId(123L);
        person.setAge("Age");
        person.setLast_name("Doe");
        person.setFirst_name("Jane");
        when(this.iPersonService.getPersonById(anyLong())).thenReturn(person);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/persons/{id}", 12345678987654321L);
        MockMvcBuilders.standaloneSetup(this.personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString(
                                "{\"id\":123,\"first_name\":\"Jane\",\"last_name\":\"Doe\",\"age\":\"Age\",\"favourite_colour\":\"Favourite colour\"}")));
    }

    @Test
    public void testCreatePerson() throws Exception {
        when(this.iPersonService.getPersons(anyInt(), anyInt())).thenReturn(new HashMap<String, Object>(1));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.get("/persons")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new CreatePersonRequest()));
        MockMvcBuilders.standaloneSetup(this.personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("{}")));
    }

    @Test
    public void testDeletePersonById() throws Exception {
        when(this.iPersonService.deletePersonById(anyLong())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/persons/{id}", 12345678987654321L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.personController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testDeletePersonById2() throws Exception {
        when(this.iPersonService.deletePersonById(anyLong())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/persons/{id}", 12345678987654321L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.personController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testDeletePersonById3() throws Exception {
        doNothing().when(this.iPersonService).deleteAllPersons();
        when(this.iPersonService.deletePersonById(anyLong())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/persons/{id}", "", "Uri Vars");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.personController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testDeleteAllPersons() throws Exception {
        when(this.iPersonService.getPersons(anyInt(), anyInt())).thenReturn(new HashMap<String, Object>(1));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/persons");
        MockMvcBuilders.standaloneSetup(this.personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("{}")));
    }

    @Test
    public void testGetAllPersons() throws Exception {
        when(this.iPersonService.getPersons(anyInt(), anyInt())).thenReturn(new HashMap<String, Object>(1));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/persons");
        MockHttpServletRequestBuilder paramResult = getResult.param("page", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(this.personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("{}")));
    }

    @Test
    public void testUpdatePerson() throws Exception {
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.put("/persons/{id}", 12345678987654321L)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new UpdatePersonRequest()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.personController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}

