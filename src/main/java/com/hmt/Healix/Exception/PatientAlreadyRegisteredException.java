package com.hmt.Healix.Exception;

public class PatientAlreadyRegisteredException extends RuntimeException{
    public PatientAlreadyRegisteredException(String message){
        super(message);
    }
}
