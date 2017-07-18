package com.mac.rltut.engine.util;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 18/07/2017 at 09:27 AM.
 */
public class StringUtil {
    
    public static String makeSecondPerson(String text){
        if(text == null || text.length() == 0) return "null";
        String[] words = text.split(" ");
        words[0] = words[0] + "s";
        
        StringBuilder builder = new StringBuilder();
        for(String word : words){
            builder.append(" ");
            builder.append(word);
        }
        return builder.toString().trim();
    }
}
