package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.KafkaProducer.PatientKafkaProducer;
import com.example.demo.dto.PatientRequestDTO;
import com.example.demo.dto.PatientResponseDTO;
import com.example.demo.entities.Patient;
import com.example.demo.event.PatientCreatedEvent;
import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.exception.PatientNotFoundException;
// import com.example.demo.grpc.BillingServiceGrpcClient;
import com.example.demo.mapper.PatientMapper;
import com.example.demo.repositories.PatientRepository;

@Service
public class PatientService {
    private PatientRepository patientRepository;
    // private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final PatientKafkaProducer kafkaProducer;

    @Autowired
    private EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(PatientService.class);

    public PatientService(PatientRepository patientRepository,
            PatientKafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        // this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public String generateUHID() {

        int year = Year.now().getValue();
        String randomPart = UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();

        return "UH-" + year + "-" + randomPart;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        logger.info("All patients list returned");
        List<PatientResponseDTO> patientResponseDTOs = patients.stream()
                .map(patient -> PatientMapper.toDTO(patient))
                .toList();

        return patientResponseDTOs;
    }

    public PatientResponseDTO getSinglePatient(UUID patientId) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        logger.info("Single patients info returned");

        return PatientMapper.toDTO(patient);
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "A Patient with this email" + "already exists" + patientRequestDTO.getEmail());
        }

        Patient newPatient = PatientMapper.toModel(patientRequestDTO);

        newPatient.setUHID(generateUHID());

        Patient savedPatient = patientRepository.save(newPatient);

        logger.info("patients created success");
        try {
            PatientCreatedEvent event = new PatientCreatedEvent();
            event.setPatientId(savedPatient.getId().toString());
            event.setName(savedPatient.getName());
            event.setEmail(savedPatient.getEmail());
            kafkaProducer.sendPatientCreatedEvent(event);
            logger.info("billing account created success");

        } catch (Exception e) {
            logger.error("Kafka failed but patient saved", e);
        }

        try {
            emailService.sendEmail(
                    "vasugoel4308@gmail.com",
                    "Registration completed successfully",
                    "Hi you are registered");
        } catch (Exception e) {
            System.out.println(e);
            logger.error("Email failed", e);
        }
        // billingservice
        // billingServiceGrpcClient.createBillingAccount(newpatient.getId().toString(),
        // newpatient.getName(), newpatient.getEmail());

        return PatientMapper.toDTO(savedPatient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not founc with id" + id));
        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            logger.error("Patient exist with this email");
            throw new EmailAlreadyExistsException(
                    "A patient exists with this email" + patientRequestDTO.getEmail());
        }
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(patientRequestDTO.getDateOfBirth());
        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);

    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }

    public List<PatientResponseDTO> getPatientByName(String name) {
        List<Patient> patients = patientRepository.findByName(name);
        List<PatientResponseDTO> patientResponseDTOs = patients.stream()
                .map(patient -> PatientMapper.toDTO(patient))
                .toList();

        return patientResponseDTOs;

    }

    public PatientResponseDTO getPatientByUHID(String uhid) {
        Patient patient = patientRepository.findByUhid(uhid);
        return PatientMapper.toDTO(patient);
    }

}