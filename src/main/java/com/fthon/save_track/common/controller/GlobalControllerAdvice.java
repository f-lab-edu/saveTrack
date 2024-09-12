package com.fthon.save_track.common.controller;


import com.fthon.save_track.common.dto.ErrorResponse;
import com.fthon.save_track.common.exceptions.BadRequestException;

import com.fthon.save_track.common.exceptions.UnAuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {


    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException e){
        return new ErrorResponse(400, e.getMessage(), ErrorResponse.ErrorInfo.of(e.getStackTrace()));
    }

    @ExceptionHandler({UnAuthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnAuthorizedException(UnAuthorizedException e){
        return new ErrorResponse(401, e.getMessage(), ErrorResponse.ErrorInfo.of(e.getStackTrace()));
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpectedException(Exception e){
        return new ErrorResponse(500, e.getMessage(), ErrorResponse.ErrorInfo.of(e.getStackTrace()));
    }

}