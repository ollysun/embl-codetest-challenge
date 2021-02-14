package com.emblproject.moses;

import com.emblproject.moses.entity.Person;
import com.emblproject.moses.repository.IPersonRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
class EmblProjectApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IPersonRepository mockRepository;

	@Test
	public void contextLoads() {

	}


	@WithMockUser("MOSES")
	@Test
	public void find_login_ok() throws Exception {
		Person person = new Person(1L, "Bodhisattva","female",
				"12", "Blue");
		when(mockRepository.findById(1L)).thenReturn(Optional.of(person));
		mockMvc.perform(get("/persons/1"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)));
	}

	@Test
	public void find_nologin_get_401() throws Exception {
		Person person = new Person(1L, "Bodhisattva","female",
				"12", "Blue");
		when(mockRepository.findById(1L)).thenReturn(Optional.of(person));
		mockMvc.perform(get("/persons/1"))
				.andDo(print())
				.andExpect(status().isUnauthorized());
	}




}
