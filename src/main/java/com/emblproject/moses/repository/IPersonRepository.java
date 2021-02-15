package com.emblproject.moses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emblproject.moses.entity.Person;

@Repository
public interface IPersonRepository extends JpaRepository<Person, Long> {
}
