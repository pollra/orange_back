package com.pollra.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@Getter
@Setter
public class ApiDataDetail {
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private Date timeStamp;
    private Object data;

    public ApiDataDetail() {}

    public ApiDataDetail(String message) {
        this.message = message;
        this.timeStamp = new Date();
    }
    public ApiDataDetail(String message, Object data) {
        this.message = message;
        this.data = data;
        this.timeStamp = new Date();
    }
}
