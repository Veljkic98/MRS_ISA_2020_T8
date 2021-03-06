/**
 * 
 */
package com.team08.CCSystem.model;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.web.bind.annotation.DeleteMapping;

import lombok.Data;

/**
 * @author Veljko
 *
 */
@Entity
@SQLDelete(sql = "UPDATE clinic SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted <> true")
//@Data
@Table(name = "Clinic")
public class Clinic {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", nullable = false, unique = true)
	private String name;
	
	@Column(name = "description", nullable = true, unique = false)
	private String description;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Address address;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private ClinicalCenter clinicalCenter;
	
	@Column(name = "startTime", nullable = false)
	private int startTime;
	
	@Column(name = "endTime", nullable = false)
	private int endTime;
	
	@OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<MedicalRoom> rooms = new HashSet<>();
	
	@OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<ClinicMark> marks = new HashSet<>();
	
	@OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY)//, cascade = CascadeType.ALL)
	private Set<Doctor> doctors = new HashSet<>();
	
	@OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Nurse> nurses = new HashSet<>();
	
	@OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<ClinicAdmin> admins = new HashSet<>();
	
	@OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Price> prices = new HashSet<>();
	
	@Column(name = "averageMark", nullable = false, unique = false)
	private float averageMark;
	
	@Column(nullable = false)
	 private Boolean deleted;

	/**
	 * @param id
	 * @param name
	 * @param address
	 * @param clinicalCenter
	 * @param rooms
	 * @param marks
	 * @param doctors
	 * @param nurses
	 * @param admins
	 * @param averageMark
	 */
	public Clinic(Long id, String name, Address address, ClinicalCenter clinicalCenter, int startTime, int endTime, Set<MedicalRoom> rooms,
			Set<ClinicMark> marks, Set<Doctor> doctors, Set<Nurse> nurses, Set<ClinicAdmin> admins, float averageMark) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.clinicalCenter = clinicalCenter;
		this.startTime = startTime;
		this.endTime = endTime;
		this.rooms = rooms;
		this.marks = marks;
		this.doctors = doctors;
		this.nurses = nurses;
		this.admins = admins;
		this.averageMark = averageMark;
		this.deleted = false;
	}

	/**
	 * 
	 */
	public Clinic() {
		super();
		this.deleted = true;
	}
	
	public Date getTodayStartDateTime() {
		Calendar cal = Calendar.getInstance();
		
    	cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, this.startTime);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		return cal.getTime();
	}
	
	public Date getTodayEndDateTime() {
		Calendar cal = Calendar.getInstance();
		
    	cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, this.endTime);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		return cal.getTime();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public ClinicalCenter getClinicalCenter() {
		return clinicalCenter;
	}

	public void setClinicalCenter(ClinicalCenter clinicalCenter) {
		this.clinicalCenter = clinicalCenter;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public Set<MedicalRoom> getRooms() {
		return rooms;
	}

	public void setRooms(Set<MedicalRoom> rooms) {
		this.rooms = rooms;
	}

	public Set<ClinicMark> getMarks() {
		return marks;
	}

	public void setMarks(Set<ClinicMark> marks) {
		this.marks = marks;
	}

	public Set<Doctor> getDoctors() {
		return doctors;
	}

	public void setDoctors(Set<Doctor> doctors) {
		this.doctors = doctors;
	}

	public Set<Nurse> getNurses() {
		return nurses;
	}

	public void setNurses(Set<Nurse> nurses) {
		this.nurses = nurses;
	}

	public Set<ClinicAdmin> getAdmins() {
		return admins;
	}

	public void setAdmins(Set<ClinicAdmin> admins) {
		this.admins = admins;
	}

	public float getAverageMark() {
		return averageMark;
	}

	public void setAverageMark(float averageMark) {
		this.averageMark = averageMark;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Price> getPrices() {
		return prices;
	}

	public void setPrices(Set<Price> prices) {
		this.prices = prices;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "Clinic [id=" + id + ", name=" + name + ", address=" + address
				+ ", rooms=" + rooms + ", marks=" + marks + ", averageMark=" + averageMark + "]";
	}
	
}
