package com.pollra.engine.entity;

import com.pollra.configuration.Description;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * @since       2020.09.17
 * @author      pollra
 * @description Base
 **********************************************************************************************************************/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Base {

    @Description("등록자")
    @CreatedBy
    private String createdBy;

    @Description("수정자")
    @LastModifiedBy
    private String updatedBy;

    @Description("등록일시")
    @LastModifiedDate
    private LocalDateTime createdAt;

    @Description("수정일시")
    private LocalDateTime updatedAt;
}
