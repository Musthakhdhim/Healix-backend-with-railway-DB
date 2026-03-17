package com.hmt.Healix.Dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdatePatientDto {
    private String fullname;
    private LocalDate dob;
    private String gender;
    private String phonenumber;
    private String address;
}
