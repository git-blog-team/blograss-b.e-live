package com.blograss.blograsslive.commons.utils;

import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class EtcUtils {

    @Autowired
    private RedisUtil redisUtil;

    public String generateUUID() {
        UUID uuid = UUID.randomUUID();
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();
        
        String shortUUID = String.format("%010d", Math.abs(mostSigBits + leastSigBits) % 10000000000L);
        
        return shortUUID;
    }
    
    public String extractTextFromHtml(String html) {
        Document doc = Jsoup.parse(html);

        String text = doc.text();

        if(text.length() > 100) {
            text = text.substring(0, 100);
        }

        return text;
    }

    public String getUserIdByAccessToken(HttpServletRequest req) {

        String token = req.getHeader("Authorization");

        String accessToken = token.split(" ")[1];
        
        String userId = (String) redisUtil.get(accessToken);

        return userId;
    }
    
}
