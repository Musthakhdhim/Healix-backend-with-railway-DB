package com.hmt.Healix.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class PatientRegisterDto {

    @NotEmpty(message = "name can't be empty")
    @NotBlank(message = "name can't be blank")
    private String fullname;

    @NotEmpty(message = "address can't be empty")
    @NotBlank(message = "address can't be blank")
    private String address;

    private LocalDate dob;
    @NotEmpty(message = "gender can't be empty")
    @NotBlank(message = "gender can't be blank")
    private String gender;
    @NotEmpty(message = "phone number can't be empty")
    @NotBlank(message = "phone number can't be blank")
    private String phonenumber;
}
