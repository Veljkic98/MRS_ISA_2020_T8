package com.team08.CCSystem.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.team08.CCSystem.dto.ExaminationForPatientDTO;
import com.team08.CCSystem.dto.MedicalRecordsDTO;
import com.team08.CCSystem.dto.RegistrationRequestDTO;
import com.team08.CCSystem.model.Address;
import com.team08.CCSystem.model.ClinicMark;
import com.team08.CCSystem.model.ClinicalCenter;
import com.team08.CCSystem.model.Disease;
import com.team08.CCSystem.model.Doctor;
import com.team08.CCSystem.model.DoctorMark;
import com.team08.CCSystem.model.Examination;
import com.team08.CCSystem.model.ExaminationType;
import com.team08.CCSystem.model.Medication;
import com.team08.CCSystem.model.Patient;
import com.team08.CCSystem.model.Prescription;
import com.team08.CCSystem.model.VerificationToken;
import com.team08.CCSystem.model.enums.BloodType;
import com.team08.CCSystem.repository.ClinicalCenterRepository;
import com.team08.CCSystem.repository.PatientRepository;
import com.team08.CCSystem.repository.VerificationTokenRepository;

/**
 * @author Veljko
 *
 */
@Service
public class PatientService {
	@Autowired
	private PatientRepository patientRepository;
	
	@Autowired
	private ClinicalCenterRepository clinicalCenterRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailServiceImpl emailService;
	
	@Autowired
    private VerificationTokenRepository tokenRepository;
	
	public Patient findOne(Long id) {
		return patientRepository.findById(id).orElseGet(null);
	}
	
	public List<Patient> findAll() {
		return patientRepository.findAll();
	}
	
	public Patient save(Patient patient) {
		return patientRepository.save(patient);
	}
	
	public void remove(Long id) {
		patientRepository.deleteById(id);
	}
	
	public MedicalRecordsDTO convertToMedicalRecords(Patient p) {
		MedicalRecordsDTO records = new MedicalRecordsDTO();
		
		records.setName(p.getName());
		records.setSurname(p.getSurname());
		records.setPolicyholder(p.getPolicyholder());
		records.setHeight(p.getHeight());
		records.setWeight(p.getWeight());
		records.setBloodType(p.getBloodType().toString());
		records.setAllergies(p.getAllergy());
		records.setDiopter(p.getDiopter());
		Set<ExaminationForPatientDTO> examinations = new HashSet<ExaminationForPatientDTO>();
		convertToExaminations(p.getExaminations(), examinations);
		records.setExaminations(examinations);
		
		return records;
	}
	
	public void convertToExaminations(Set<Examination> patientExamin, Set<ExaminationForPatientDTO> newSetExamin) {
		
		for(Examination e : patientExamin) {
			ExaminationForPatientDTO newExamin = new ExaminationForPatientDTO();
			
			newExamin.setDateOfExamination(e.getDate());
			newExamin.setWasOnExamination(e.getWasOnExamination());
			
			Doctor doctor = e.getDoctor();
			newExamin.setDoctorsName(doctor.getName() + " " + doctor.getSurname());
			
			ExaminationType type = e.getPrice().getExaminationType();
			newExamin.setExaminationType(type.getSpecialisation().toString() + " " + type.getInterventionType().toString());
			
			newExamin.setClinicName(doctor.getClinic().getName());
			newExamin.setPrice(Double.toString(e.getStaticPrice()));
			newExamin.setDescription(e.getDescription());
			
			Set<String> diseases = new HashSet<String>();
			for(Disease d : e.getDiseases()) {
				diseases.add(d.getName());
			}
			newExamin.setDiseases(diseases);
			
			Set<String> medications = new HashSet<String>();
			for(Prescription p : e.getPrescriptions()) {
				medications.add(p.getMedication().getName() + ": " + p.getDescription());
			}
			newExamin.setMedications(medications);
			
			newSetExamin.add(newExamin);
		}
		
	}
	
	public Patient addNewPatient(RegistrationRequestDTO request) {
		Patient p = new Patient();
		
		Address address = new Address(null, request.getStreet(), request.getCity(), request.getCountry());
		p.setAddress(address);
		p.setAllergy("");
		p.setBloodType(BloodType.UNKNOWN);
		ClinicalCenter cc = clinicalCenterRepository.findByName("Clinical Center of Vojvodina");
		p.setClinicalCenter(cc);
		p.setClinicsMarks(new HashSet<ClinicMark>());
		p.setDiopter("");
		p.setDoctorsMarks(new HashSet<DoctorMark>());
		p.setEmail(request.getEmail());
		p.setExaminations(new HashSet<Examination>());
		p.setHeight(0);
		p.setName(request.getName());
		p.setPassword(passwordEncoder.encode(request.getPassword()));
		p.setPhone(request.getPhone());
		p.setPolicyholder(request.getPolicyholder());
		p.setSurname(request.getSurname());
		p.setWeight(0);
		
		p.setEnabled(false); //patient is not able to login until use activation link
		
		return patientRepository.save(p);
	}
	
	
	public void registrationApproval(boolean approved, String userEmail, String explanation) {
		
		Patient patient = patientRepository.findByEmail(userEmail);
		// if administrator not approve user registration request
		if(!approved) {
			//delete patient from database and send explanation by email
			remove(patient.getId());
			emailService.sendMail(userEmail, "Registration response", explanation);
			return;
		}
		
		// if admin approve registration request		
		// generate verification token for patient and send mail with activation link
        String token = UUID.randomUUID().toString();
        createVerificationToken(patient, token);
        
        String confirmationUrl = "http://localhost:8080/auth/regitration/" + token;
        String message = "Hello " + patient.getEmail() + ",\n"
        		+ "Our Clinical Center approve your registration request.\n" +
        		"Here is your activation link:\n" + confirmationUrl;
        
        emailService.sendMail(userEmail, "Registration Confirmation", message);
	}
	
	public Patient getUser(String verificationToken) {
        Patient patient = tokenRepository.findByToken(verificationToken).getPatient();
        return patient;
    }
     
    public VerificationToken getVerificationToken(String verificationToken) {
        return tokenRepository.findByToken(verificationToken);
    }
     
    public void createVerificationToken(Patient patient, String token) {
        VerificationToken myToken = new VerificationToken(patient, token);
        tokenRepository.save(myToken);
    }

}
