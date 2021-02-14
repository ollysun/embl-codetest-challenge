package com.emblproject.moses.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.Data;

import java.io.Serializable;

@Entity
@Table(indexes = {@Index(name="SEC_INDEX",columnList = "ID,FIRSTNAME,LASTNAME")})
public class Person implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Please enter the first name")
	@Column(name = "firstname", unique = true, nullable = false)
	private String first_name;

	@NotBlank(message = "Please enter the last name")
	@Column(name = "lastname", nullable = false)
	private String last_name;

	@NotBlank(message = "Age field cannot be empty")
	@Positive(message = "Please enter the age number")
	@Column(nullable = false)
	private String age;

	@NotBlank(message = "Please enter the favourite colour")
	@Column(name = "favouritecolour")
	private String favourite_colour;

	public Person() { }

	public Person(Long id,String first_name, String last_name,
				  String age, String favourite_colour) {
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.age = age;
		this.favourite_colour = favourite_colour;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getFavourite_colour() {
		return favourite_colour;
	}

	public void setFavourite_colour(String favourite_colour) {
		this.favourite_colour = favourite_colour;
	}

	@Override
	public String toString() {
		return "Person{" +
				"id=" + id +
				", first_name='" + first_name + '\'' +
				", last_name='" + last_name + '\'' +
				", age='" + age + '\'' +
				", favourite_colour='" + favourite_colour + '\'' +
				'}';
	}
}
