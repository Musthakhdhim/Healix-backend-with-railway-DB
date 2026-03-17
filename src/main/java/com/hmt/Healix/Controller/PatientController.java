package com.hmt.Healix.Controller;


import com.hmt.Healix.Dtos.ChangePasswordDto;
import com.hmt.Healix.Dtos.UpdatePatientDto;
import com.hmt.Healix.Entities.Patient;
import com.hmt.Healix.ServiceImpl.JwtService;
import com.hmt.Healix.ServiceImpl.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final JwtService jwtService;

    @GetMapping("/register/hello")
    public String hello(){
        return "Hello world";
    }

    @PostMapping("/register")
    public ResponseEntity<?> addPatient(HttpServletRequest request, @Valid @RequestBody Patient patient){
        String token= jwtService.getTokenFromAuthorization(request);
        Long userId= jwtService.getIdFromToken(token);
        return patientService.addPatientDetails(userId,patient);
    }

    @GetMapping
    public ResponseEntity<?> getPatientDetails(HttpServletRequest request){
        String token= jwtService.getTokenFromAuthorization(request);
        Long userId= jwtService.getIdFromToken(token);
        return patientService.getPatientProfileDetails(userId);
    }

    @PutMapping
    public ResponseEntity<?> updatePatientDeails(HttpServletRequest request, @RequestBody UpdatePatientDto updatePatientDto){
        String token= jwtService.getTokenFromAuthorization(request);
        Long userId= jwtService.getIdFromToken(token);
        return patientService.updatePatientDetails(userId,updatePatientDto);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(HttpServletRequest request,@Valid @RequestBody ChangePasswordDto changePasswordDto){
        String token= jwtService.getTokenFromAuthorization(request);
        Long userId= jwtService.getIdFromToken(token);
        return patientService.changePassword(userId,changePasswordDto);
    }

//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletResponse response){
//        return patientService.logout(response);
//    }
}
