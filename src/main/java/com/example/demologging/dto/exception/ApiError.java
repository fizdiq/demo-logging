package com.example.demologging.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private Long status;
    private String message;
    private List<String> datas;

    public ApiError(Long status, String message, String data) {
        super();
        this.status = status;
        this.message = message;
        datas = Arrays.asList(data);
    }
}
