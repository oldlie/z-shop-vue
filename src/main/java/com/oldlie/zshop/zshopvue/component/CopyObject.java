package com.oldlie.zshop.zshopvue.component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CopyObject<T>{

    public T copy(T input, T output) throws Exception {
        Class cls = input.getClass();
        Field[] fields = cls.getFields();
        Map<String, Object> values = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(input);
            if (value != null) {
                field.set(output, value);
            }
        }
        return output;
    }
}
