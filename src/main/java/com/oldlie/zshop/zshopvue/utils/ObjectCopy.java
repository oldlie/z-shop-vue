package com.oldlie.zshop.zshopvue.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ObjectCopy<T> {

    public T copy(T source, T target){
        return this.copy(source, target, null);
    }

    public T copy(T source, T target, List<String> ignoreFieldList) {
        Map<String, Field> sourceMap = getAllFields(source, ignoreFieldList);
        Map<String, Field> targetMap = getAllFields(target);
        for (String key : sourceMap.keySet()) {
            Field sourceField = sourceMap.get(key);
            if (targetMap.containsKey(key)) {
                Field targetField = targetMap.get(key);
                sourceField.setAccessible(true);
                targetField.setAccessible(true);
                Object value = null;
                try {
                    value = sourceField.get(source);
                    if (value != null) {
                        targetField.set(target, value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                    target = null;
                }
            }
        }
        return target;
    }

    public T copyValue2Entity(T source, T target){
        List<String> list = new ArrayList<>();
        list.add("id");
        list.add("createTime");
        list.add("updateTime");
        return copy(source, target, list);
    }

    public T copyWithoutId(T source, T target) {
        List<String> list = new ArrayList<>();
        list.add("id");
        return copy(source, target, list);
    }

    private Map<String, Field> getAllFields(T source) {
        return getAllFields(source, null);
    }

    private Map<String, Field> getAllFields(T source, List<String> ignore) {
        Map<String, Field> map = new HashMap<>();
        Class clazz = source.getClass();
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null && fields.length > 0) {
                for(Field field : fields) {
                    if (ignore != null && ignore.size() > 0 && ignore.contains(field.getName())) {
                        continue;
                    }
                    map.put(field.getName(), field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return map;
    }
}
