package kr.ac.devsign.HomePage.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "API 공통 응답 형식")
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
}

