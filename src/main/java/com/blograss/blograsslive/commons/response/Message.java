package com.blograss.blograsslive.commons.response;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Message {
    
    private String message;
    private Integer count;
    private List<?> result;

    public static Message write(final String message, final List<?> result) {
        
        return Message.builder()
                .message(message)
                .count(result.size())
                .result(result)
                .build();
    }

    public static Message write(final String message, final List<?> result, Integer allCount) {
        
        return Message.builder()
                .message(message)
                .count(allCount)
                .result(result)
                .build();
    }

    public static Message write(final String message, final Object result) {

        ArrayList<Object> obj = new ArrayList<>();
        obj.add(result);

        return Message.builder()
                .message(message)
                .count(1)
                .result(obj)
                .build();
    }

    public static Message write(final String message) {

        return Message.builder()
                .message(message)
                .count(0)
                .result(null)
                .build();
    }

}


