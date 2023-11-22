//package me.teho.SecurityJwtOauth2.util;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import me.teho.SecurityJwtOauth2.AppConfig;
//
//import java.time.LocalDateTime;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//@RequiredArgsConstructor
//public class Util {
//    private static ObjectMapper getObjectMapper() {
//        return (ObjectMapper) AppConfig.getContext().getBean("objectMapper");
//    }
//
//    public static <K, V> Map<K, V> mapOf(Object... args) {
//        Map<K, V> map = new LinkedHashMap<>();
//
//        int size = args.length / 2;
//
//        for (int i = 0; i < size; i++) {
//            int keyIndex = i * 2;
//            int valueIndex = keyIndex + 1;
//
//            K key = (K) args[keyIndex];
//            V value = (V) args[valueIndex];
//
//            map.put(key, value);
//        }
//
//        return map;
//    }
//
//    public static class date {
//
//        public static LocalDateTime bitsToLocalDateTime(List<Integer> bits) {
//            return LocalDateTime.of(bits.get(0), bits.get(1), bits.get(2), bits.get(3), bits.get(4), bits.get(5), bits.get(6));
//        }
//    }
//
//    public static class json {
//
//        public static Object toStr(Map<String, Object> map) {
//            try {
//                return getObjectMapper().writeValueAsString(map);
//            } catch (JsonProcessingException e) {
//                return null;
//            }
//        }
//
//        public static Map<String, Object> toMap(String jsonStr) {
//            try {
//                return getObjectMapper().readValue(jsonStr, LinkedHashMap.class);
//            } catch (JsonProcessingException e) {
//                return null;
//            }
//        }
//
//
//    }
//}
