package com.cts.project.Travelling_package.Exception;

//public class ResourceNotFoundException extends RuntimeException{
//    public ResourceNotFoundException(String reviewNotFound) {
//
//    }
//}
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
