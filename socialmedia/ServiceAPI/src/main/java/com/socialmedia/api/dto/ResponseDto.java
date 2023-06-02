package com.socialmedia.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {
    private Boolean success;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String message;

    public ResponseDto(Boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public ResponseDto(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}
