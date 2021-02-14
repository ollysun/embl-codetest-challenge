package com.emblproject.moses.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.emblproject.moses.entity.Person;

@Repository
public interface IPersonRepository extends JpaRepository<Person, Long> {
//    Page<Person> findAll(Pageable pageable);
}
