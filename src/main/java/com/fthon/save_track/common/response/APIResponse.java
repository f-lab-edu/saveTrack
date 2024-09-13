package com.fthon.save_track.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class APIResponse<B> extends ResponseEntity<B> {

    public APIResponse(final HttpStatus status) {
        super(status);
    }

    public APIResponse(final B body, final HttpStatus status) {
        super(body, status);
    }

    public APIResponse(final B body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }
}
