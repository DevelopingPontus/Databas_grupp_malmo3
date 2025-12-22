package com.example.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestDataLoader {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> List<T> loadTestData(String filePath, String dataKey, Class<T> valueType) throws IOException {
        File file = ResourceUtils.getFile("classpath:" + filePath);
        Map<String, List<T>> dataMap = objectMapper.readValue(file,
                new TypeReference<Map<String, List<T>>>() {
                });
        return dataMap.get(dataKey);
    }

    public static <T> T loadSingleTestData(String filePath, String dataKey, Class<T> valueType) throws IOException {
        File file = ResourceUtils.getFile("classpath:" + filePath);
        Map<String, T> dataMap = objectMapper.readValue(file,
                new TypeReference<Map<String, T>>() {
                });
        return dataMap.get(dataKey);
    }
}