package com.emblproject.moses.service;

import java.util.Map;

import com.emblproject.moses.dto.request.UpdatePersonRequest;
import com.emblproject.moses.entity.Person;

public interface IPersonService {

	Person createPerson(Person createPersonRequest);

	Person updatePerson(long id, UpdatePersonRequest updatePersonRequest);

	Map<String, Object> getPersons(int pageNumber, int pageSize);

	Person getPersonById(long id) ;

	Boolean deletePersonById(long id);

	void deleteAllPersons();

}
