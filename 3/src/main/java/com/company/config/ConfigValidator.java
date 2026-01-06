package com.company.config;

import java.lang.reflect.Field;

public class ConfigValidator {

    public static void validate(SystemConfig config) {

        for (Field field : SystemConfig.class.getDeclaredFields()) {

            if (field.isAnnotationPresent(RangeCheck.class)) {

                RangeCheck range = field.getAnnotation(RangeCheck.class);
                field.setAccessible(true);

                try {
                    int value = field.getInt(config);

                    if (value < range.min() || value > range.max()) {
                        throw new ConfigValidationException(
                                field.getName() + " out of range"
                        );
                    }

                    SystemConfig.logSuccess(field.getName() + " valid");

                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
