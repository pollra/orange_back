package com.pollra.engine.auditor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuditorEvent<T> {
    private T source;
}
