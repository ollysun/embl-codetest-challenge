package com.emblproject.moses.service;

import java.util.*;

import com.emblproject.moses.Exception.PersonException;
import com.emblproject.moses.Exception.ResourceNotFoundException;
import com.emblproject.moses.dto.request.CreatePersonRequest;
import com.emblproject.moses.dto.request.UpdatePersonRequest;
import com.emblproject.moses.repository.IPersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emblproject.moses.entity.Person;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PersonServiceImpl implements IPersonService {

	private final IPersonRepository ipersonRepository;

	@Autowired
	public PersonServiceImpl(IPersonRepository ipersonRepository) {
		this.ipersonRepository = ipersonRepository;
	}

	@Transactional
	@Override
	public Person createPerson(CreatePersonRequest createPersonRequest)  {
		Person personResponse;
			personResponse = ipersonRepository.save(new Person(null,createPersonRequest.getFirst_name(),
					createPersonRequest.getLast_name(), createPersonRequest.getAge(),
					createPersonRequest.getFavourite_colour()));
		return personResponse;
	}

	@Transactional(rollbackFor = {ResourceNotFoundException.class})
	@Override
	public Person updatePerson(long id, UpdatePersonRequest updatePersonRequest) {
		Person personData = ipersonRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Person with id = " + id));
		personData.setAge(updatePersonRequest.getAge());
		personData.setFirst_name(updatePersonRequest.getFirst_name());
		personData.setLast_name(updatePersonRequest.getLast_name());
		personData.setFavourite_colour(updatePersonRequest.getFavourite_colour());
		ipersonRepository.save(personData);
		return personData;
	}

	@Transactional(readOnly = true)
	@Override
	public Map<String, Object> getPersons(int pageNumber, int pageSize) {
		List<Person> persons;
		Pageable paging = PageRequest.of(pageNumber, pageSize);
		Page<Person> pagePerson;
		Map<String, Object> response = new HashMap<>();

		try{
			pagePerson = ipersonRepository.findAll(paging);
			persons = pagePerson.getContent();

			response.put("data", persons);
			response.put("currentPage", pagePerson.getNumber());
			response.put("totalItems", pagePerson.getTotalElements());
			response.put("totalPages", pagePerson.getTotalPages());

		}catch(Exception e){
			log.debug(e.getMessage());
		}
		return response;
	}

	@Transactional(readOnly = true, rollbackFor = {ResourceNotFoundException.class})
	@Override
	public Person getPersonById(long id) {

		Optional<Person> personData = ipersonRepository.findById(id);
		if (personData.isPresent()){
			return personData.get();
		}else{
			throw new ResourceNotFoundException("No entry found with id: " + id);
		}
	}

	@Transactional(rollbackFor = {ResourceNotFoundException.class})
	@Override
	public Boolean  deletePersonById(long id) {
		Person person = getPersonById(id);
		if (person == null) {
			return false;
		}
		ipersonRepository.deleteById(id);
		return true;
	}

	@Override
	public void deleteAllPersons() {
		ipersonRepository.deleteAll();
	}

}
