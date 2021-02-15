package com.emblproject.moses;

import com.emblproject.moses.entity.Person;
import com.emblproject.moses.repository.IPersonRepository;
import com.emblproject.moses.service.PersonServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EmblPersonServiceTests {

    @Mock
    private IPersonRepository ipersonRepository;


    @Test
    public void testGetPersonById(){
        Person person = new Person(1L, "mike", "moses","23", "blue");
        when(ipersonRepository.findById(1L)).thenReturn(java.util.Optional.of(person));
        PersonServiceImpl personService = new PersonServiceImpl(ipersonRepository);
        Person result = personService.getPersonById(1);
        assertEquals(1, result.getId());
        assertEquals("23", result.getAge());
        assertEquals("mike", result.getFirst_name());
    }


}
