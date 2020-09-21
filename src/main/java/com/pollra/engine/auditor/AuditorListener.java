package com.pollra.engine.auditor;

import com.pollra.config.security.helper.PrincipalHelper;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AuditorListener {

    @EventListener
    public void onEvent(AuditorEvent event) {
        recursive(event.getSource());
    }

    public void recursive(Object source) {
        if(Objects.nonNull(source)) {
            List<Field> fieldsOfClass = Arrays.asList(Optional.ofNullable(source.getClass().getDeclaredFields()).orElse(new Field[]{}));
            List<Field> fieldsOfSuperClass = Arrays.asList(Optional.ofNullable(source.getClass().getSuperclass().getDeclaredFields()).orElse(new Field[]{}));

            for (Field field : Stream.of(fieldsOfClass, fieldsOfSuperClass)
                        .flatMap(f -> f.stream())
                        .filter (f -> BooleanUtils.isFalse(f.getType().isEnum()))
                        .filter (f -> BooleanUtils.isFalse(f.getType().isPrimitive()))
                        .collect(Collectors.toList())) {

                ReflectionUtils.makeAccessible(field);
                setValueIfCreateByOrLastModifiedBy(field, source);

            }

        }
    }

    private void setValueIfCreateByOrLastModifiedBy(Field field, Object source) {
        if(ObjectUtils.anyNotNull(field.getAnnotation(CreatedBy.class), field.getAnnotation(LastModifiedBy.class))) {
            ReflectionUtils.setField(field, source, PrincipalHelper.getAccount());
        }
    }
}
