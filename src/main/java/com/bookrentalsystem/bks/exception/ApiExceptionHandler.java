//package com.bookrentalsystem.bks.exception;
//
//import com.bookrentalsystem.bks.exception.globalException.BookNotFoundException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ControllerAdvice
//public class ApiExceptionHandler {
//
//    @ExceptionHandler(value = {AuthorNotfoundException.class})
//    public ResponseEntity<Object> handelException(AuthorNotfoundException authorNotfoundException){
//        ApiException apiException = new ApiException(authorNotfoundException.getMessage(),
//                HttpStatus.NOT_FOUND);
//
//        return new ResponseEntity<>(apiException,HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(value = {CategoryNotFoundException.class})
//    public ResponseEntity<Object> handelException(CategoryNotFoundException categoryNotFoundException){
//        ApiException apiException = new ApiException(categoryNotFoundException.getMessage(),
//                HttpStatus.NOT_FOUND);
//
//        return new ResponseEntity<>(apiException,HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(value = {MemberNotFoundException.class})
//    public ResponseEntity<Object> handelException(MemberNotFoundException memberNotFoundException){
//        ApiException apiException = new ApiException(memberNotFoundException.getMessage(),
//                HttpStatus.NOT_FOUND);
//
//        return new ResponseEntity<>(apiException,HttpStatus.NOT_FOUND);
//    }
////    @ExceptionHandler(value = {BookNotFoundException.class})
////    public ResponseEntity<Object> handelException(BookNotFoundException bookNotFoundException){
////        ApiException apiException = new ApiException(bookNotFoundException.getMessage(),
////                HttpStatus.NOT_FOUND);
////
////        return new ResponseEntity<>(apiException,HttpStatus.NOT_FOUND);
////    }
//}
