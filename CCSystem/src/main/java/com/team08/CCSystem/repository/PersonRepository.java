/**
 * 
 */
package com.team08.CCSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team08.CCSystem.model.Person;

/**
 * @author Veljko
 *
 */

public interface PersonRepository extends JpaRepository<Person, Long> {
	
}