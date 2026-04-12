package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

    public PatientService(PatientRepository patientRepository, 
            PatientKafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        // this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        List<PatientResponseDTO> patientResponseDTOs = patients.stream()
                .map(patient -> PatientMapper.toDTO(patient))
                .toList();

        return patientResponseDTOs;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "A Patient with this email" + "already exists" + patientRequestDTO.getEmail());
        }
        Patient newpatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));

        PatientCreatedEvent event = new PatientCreatedEvent();
        event.setPatientId(newpatient.getId().toString());
        event.setName(newpatient.getName());
        event.setEmail(newpatient.getEmail());
        kafkaProducer.sendPatientCreatedEvent(event);
        // billingservice
        // billingServiceGrpcClient.createBillingAccount(newpatient.getId().toString(),
        // newpatient.getName(), newpatient.getEmail());
        return PatientMapper.toDTO(newpatient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not founc with id" + id));
        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
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

}