package com.hmt.Healix.Mapper;

import com.hmt.Healix.Dtos.PatientRegisterDto;
import com.hmt.Healix.Entities.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public Patient toPatient(PatientRegisterDto patientRegisterDto){
        Patient patient=new Patient();
        patient.setDob(patientRegisterDto.getDob());
        patient.setAddress(patientRegisterDto.getAddress());
        patient.setGender(patientRegisterDto.getGender());
        patient.setPhonenumber(patientRegisterDto.getPhonenumber());
        patient.setFullname(patientRegisterDto.getFullname());

        return patient;
    }
}
