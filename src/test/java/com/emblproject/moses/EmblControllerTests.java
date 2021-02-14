package com.emblproject.moses;

import com.emblproject.moses.Exception.ResourceNotFoundException;
import com.emblproject.moses.dto.request.CreatePersonRequest;
import com.emblproject.moses.dto.request.UpdatePersonRequest;
import com.emblproject.moses.entity.Person;
import com.emblproject.moses.service.IPersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.Charset;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
public class EmblControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPersonService iPersonService;

    private final ObjectMapper mapper= new ObjectMapper();

    @Test
    void it_should_return_created_person() throws Exception {

        CreatePersonRequest createPersonRequest = new CreatePersonRequest();
        createPersonRequest.setAge("29");
        createPersonRequest.setFirst_name("moses");
        createPersonRequest.setLast_name("mike");
        createPersonRequest.setFavourite_colour("yellow");

        Person person = new Person(1L, "moses", "mike","29", "yellow");
        doReturn(person).when(iPersonService).createPerson(any());

        mockMvc.perform(post("/persons")
                .with(user("moses").password("mosespass"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createPersonRequest)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.first_name", is("moses")))
                .andExpect(jsonPath("$.last_name", is("mike")));

    }

    @Test
    void it_should_return_400_created_person() throws Exception {

        CreatePersonRequest createPersonRequest = new CreatePersonRequest();
        createPersonRequest.setAge("29");
        createPersonRequest.setFavourite_colour("yellow");

        Person person = iPersonService.createPerson(createPersonRequest);
        when(iPersonService.createPerson(any(CreatePersonRequest.class))).thenReturn(person);
        mockMvc.perform(post("/persons")
                .with(user("moses").password("mosespass"))
                .content(mapper.writeValueAsString(createPersonRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser("MOSES")
    @Test
    @DisplayName("Test findAll()")
    public void findAllUsers_InputsAreValid_ReturnUserList() throws Exception {
        //given
        Person person1 = new Person(1L, "mike", "moses","23", "blue");
        Person person2 = new Person(1L, "mike", "moses","23", "blue");

        List<Person> persons = new ArrayList<>();
        persons.add(person1);
        persons.add(person2);
        Map<String, Object> response = new HashMap<>();

        response.put("data", persons);
        response.put("currentPage", 0);
        response.put("totalItems", 3);
        response.put("totalPages", 2);
        when(iPersonService.getPersons(0,3))
                           .thenReturn(response);

        mockMvc.perform(get("/persons")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


    @Test
    @DisplayName("Test findById() with invalid userId")
    public void findUserById_ReturnPersonAsResponse() throws Exception {
        Person person = new Person(1L, "mike", "moses","23", "blue");
        given(iPersonService.getPersonById(person.getId())).willReturn(person);

        mockMvc.perform(get("/persons/{id}",1L)
                .with(user("moses").password("mosespass"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("age", is(person.getAge())));
        verify(iPersonService,times(1)).getPersonById(Mockito.anyLong());
    }

    @Test
    @DisplayName("Test findById() with invalid userId")
    public void findUserById_WhenIdIsInValid_ReturnUserAsResponse_And_Internal_Error() throws Exception {
        when(iPersonService.getPersonById(Mockito.anyLong())).thenReturn(new Person());
        mockMvc.perform(get("/persons/{id}","aa")
                .with(user("moses").password("mosespass"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test findById() with invalid userId")
    public void findUserById_ReturnPerson_NotFound() throws Exception {
        when(iPersonService.getPersonById(Mockito.anyLong())).thenThrow(new ResourceNotFoundException(""));
        mockMvc.perform(get("/persons/{id}",1L)
                .with(user("moses").password("mosespass"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(iPersonService, times(1)).getPersonById(1L);
        verifyNoMoreInteractions(iPersonService);
    }

    @Test
    public void testPutExample() throws Exception {
        UpdatePersonRequest updatePersonRequest = new UpdatePersonRequest();
        updatePersonRequest.setId(1L);
        updatePersonRequest.setAge("23");
        updatePersonRequest.setFirst_name("moses");
        updatePersonRequest.setLast_name("mike");
        updatePersonRequest.setFavourite_colour("yellow");

        Person personToReturnFindById = new Person(1L, "moses", "mike","23", "yellow");
        Person personToSave = new Person(1L, "moses", "mike","23", "yellow");
        doReturn(personToReturnFindById).when(iPersonService).getPersonById(anyLong());
        doReturn(personToSave).when(iPersonService).updatePerson(anyLong(),any());
        String json = mapper.writeValueAsString(updatePersonRequest);

        mockMvc.perform(put("/persons/{id}",1L)
                .with(user("moses").password("mosespass"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.age", is("23")))
                .andExpect(jsonPath("$.id", is(1)));
    }


    @Test
    public void testUpdatePersonNotFound() throws Exception {
        UpdatePersonRequest updatePersonRequest = new UpdatePersonRequest();
        updatePersonRequest.setId(1L);
        updatePersonRequest.setAge("23");
        updatePersonRequest.setFirst_name("moses");
        updatePersonRequest.setLast_name("mike");
        updatePersonRequest.setFavourite_colour("yellow");

        when(iPersonService.updatePerson(anyLong(), any())).thenThrow(new ResourceNotFoundException(""));
        String json = mapper.writeValueAsString(updatePersonRequest);

        mockMvc.perform(put("/persons/{id}", 1L)
                .with(user("moses").password("mosespass"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdatePersonBadRequest() throws Exception {
        UpdatePersonRequest updatePersonRequest = new UpdatePersonRequest();
        updatePersonRequest.setId(1L);

        String json = mapper.writeValueAsString(updatePersonRequest);
        mockMvc.perform(put("/persons/{id}", 1L)
                .with(user("moses").password("mosespass"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(iPersonService);
    }

    @Test
    void deleteById_PersonEntryFound_ShouldDeleteTodoEntryAndReturnIt() throws Exception {
        when(iPersonService.deletePersonById(1L)).thenReturn(Boolean.TRUE);
        mockMvc.perform(delete("/persons/{id}", 1L)
                .with(user("moses").password("mosespass")))
                .andExpect(status().isNoContent());
        verify(iPersonService, times(1)).deletePersonById(1L);
        verifyNoMoreInteractions(iPersonService);
    }

    @Test
    public void deleteById_TodoIsNotFound_ShouldReturnHttpStatusCode404() throws Exception {
        when(iPersonService.deletePersonById(1L)).thenThrow(new ResourceNotFoundException(""));

        mockMvc.perform(delete("/persons/{id}", 1L)
                .with(user("moses").password("mosespass")))
                .andExpect(status().isNotFound());

        verify(iPersonService, times(1)).deletePersonById(1L);
        verifyNoMoreInteractions(iPersonService);
    }



}
