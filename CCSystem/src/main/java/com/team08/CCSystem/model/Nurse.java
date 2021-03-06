/**
 * 
 */
package com.team08.CCSystem.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.Data;

/**
 * @author Veljko
 *
 */
@Entity
@SQLDelete(sql = "UPDATE nurse SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted <> true")
@Data
@Table(name = "Nurse")
public class Nurse extends User {

	
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private Clinic clinic;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Absence> absences = new HashSet<Absence>();
	
	@Column(name = "isPasswordChanged", nullable = false)
	private boolean isPasswordChanged;
	
	public Nurse() {}

	/**
	 * @param id
	 * @param email
	 * @param name
	 * @param surname
	 * @param address
	 * @param phone
	 * @param password
	 * @param clinic
	 * @param absences
	 */
	public Nurse(Long id, String email, String name, String surname, Address address, String phone, String password,
			Clinic clinic, Set<Absence> absences) {
		super(id, email, name, surname, address, phone, password);
		this.clinic = clinic;
		this.absences = absences;
		this.isPasswordChanged = false;
	}

		

	/**
	 * @param id
	 * @param email
	 * @param name
	 * @param surname
	 * @param address
	 * @param phone
	 * @param password
	 */
	public Nurse(Long id, String email, String name, String surname, Address address, String phone,
			String password) {
		super(id, email, name, surname, address, phone, password);
		this.isPasswordChanged = false;
	}

	public boolean isPasswordChanged() {
		return isPasswordChanged;
	}

	public void setPasswordChanged(boolean isPasswordChanged) {
		this.isPasswordChanged = isPasswordChanged;
	}

	public Clinic getClinic() {
		return clinic;
	}

	public void setClinic(Clinic clinic) {
		this.clinic = clinic;
	}

	public Set<Absence> getAbsences() {
		return absences;
	}

	public void setAbsences(Set<Absence> absences) {
		this.absences = absences;
	}
	
	@Override
	public String toString() {
		return "Request for nurse:" +
				"\nName: " + this.getName() +
				"\nSurname: " + this.getSurname() +
				"\nEmail: " + this.getEmail() +
				"\nStreet: " + this.getAddress().getStreet() +
				"\nCity: " + this.getAddress().getCity() +
				"\nCountry: " + this.getAddress().getCountry() +
				"\nPhone number: " + this.getPhone();
	}

}
