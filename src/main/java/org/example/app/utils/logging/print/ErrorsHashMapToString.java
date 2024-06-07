package org.example.app.utils.logging.print;

import java.util.HashMap;
import java.util.Map;

public class ErrorsHashMapToString {
    public static String errorsHashMapToString(HashMap<String, String> errors) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : errors.entrySet()) {
            sb.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
