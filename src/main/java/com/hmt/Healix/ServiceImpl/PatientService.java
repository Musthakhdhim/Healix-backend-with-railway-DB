package com.hmt.Healix.ServiceImpl;

import com.hmt.Healix.Dtos.ChangePasswordDto;
import com.hmt.Healix.Dtos.PatientRegisterDto;
import com.hmt.Healix.Dtos.UpdatePatientDto;
import com.hmt.Healix.Entities.Patient;
import com.hmt.Healix.Entities.Users;
import com.hmt.Healix.Exception.PasswordNotMatchingException;
import com.hmt.Healix.Exception.PatientAlreadyRegisteredException;
import com.hmt.Healix.Exception.PatientNotFoundException;
import com.hmt.Healix.Exception.UserNotFoundException;
import com.hmt.Healix.Mapper.PatientMapper;
import com.hmt.Healix.Repository.PatientRepository;
import com.hmt.Healix.Repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PatientMapper patientMapper;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> addPatientDetails(Long userId, Patient patientPayload) {
        Optional<Users> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        Users user = userOpt.get();

        Optional<Patient> existing = patientRepository.findByUserId(userId);
        Patient patient;
        if (existing.isPresent()) {
            patient = existing.get();
            patient.setFullname(patientPayload.getFullname());
            patient.setAddress(patientPayload.getAddress());
            patient.setDob(patientPayload.getDob());
            patient.setGender(patientPayload.getGender());
            patient.setPhonenumber(patientPayload.getPhonenumber());
        } else {
            patient = new Patient();
            patient.setUser(user);
            patient.setFullname(patientPayload.getFullname());
            patient.setAddress(patientPayload.getAddress());
            patient.setDob(patientPayload.getDob());
            patient.setGender(patientPayload.getGender());
            patient.setPhonenumber(patientPayload.getPhonenumber());
        }
        patientRepository.save(patient);
        return ResponseEntity.ok(patient);
    }

    public ResponseEntity<?> getPatientProfileDetails(long userId){
        Patient patient=patientRepository.findByUserId(userId).orElseThrow(()->{
            throw new PatientNotFoundException("patient with the id: "+userId+" is not found");
        });
        return ResponseEntity.ok(patient);
    }

    public ResponseEntity<?> updatePatientDetails(long userId, UpdatePatientDto updatePatientDto){
        Patient patient=patientRepository.findByUserId(userId).orElseThrow(()->{
            throw new PatientNotFoundException("patient with the id: "+userId+" is not found");
        });
        patient.setFullname(updatePatientDto.getFullname());
        patient.setDob(updatePatientDto.getDob());
        patient.setGender(updatePatientDto.getGender());
        patient.setPhonenumber(updatePatientDto.getPhonenumber());
        patient.setAddress(updatePatientDto.getAddress());

        patientRepository.save(patient);
        return ResponseEntity.ok(patient);
    }

    public ResponseEntity<?> changePassword(long userId, ChangePasswordDto changePasswordDto){
        Users user=userRepository.findById(userId).orElseThrow(()->{
            throw new UserNotFoundException("user is not found");
        });

        if(!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())){
            throw new PasswordNotMatchingException("old password is not matching");
        }

        if(!(changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword()))){
            throw new PasswordNotMatchingException("your new password is not matching with confirmation password");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password changed successfully");
    }



//    public ResponseEntity<?> logout(HttpServletResponse response) {
//        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
//                .httpOnly(true)
//                .secure(false)
//                .path("/")
//                .maxAge(0)
//                .sameSite("Lax")
//                .build();
//
//        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
//
//        return ResponseEntity.ok().build();
//    }


}
























//    public ResponseEntity<?> addPatientDetails(long userId, PatientRegisterDto patientRegisterDto){
//        Users user=userRepository.findById(userId).orElseThrow(()->{
//            throw new UsernameNotFoundException("User not found with the id: "+userId);
//        });
//
//        Optional<Patient> existing=patientRepository.findByUserId(userId);
//        if(existing.isPresent()){
//            throw new PatientAlreadyRegisteredException("patient is already registered");
//        }
//        Patient patient=patientMapper.toPatient(patientRegisterDto);
//        patient.setUser(user);
//        return ResponseEntity.ok(patientRepository.save(patient));
//    }

//        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
//                .httpOnly(true)
//                .secure(false) // Match the original
//                .path("/")
//                .maxAge(0) // Deletes the cookie
//                .sameSite("Lax")
//                .build();
//
//        response.setHeader("Set-Cookie", deleteCookie.toString());
//
//        return ResponseEntity.ok("Logged out");