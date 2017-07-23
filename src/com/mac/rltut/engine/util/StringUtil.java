package com.mac.rltut.engine.util;

import java.util.ArrayList;
import java.util.List;

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
    
    public static List<String> lineWrap(String input, int width){
        String[] words = input.trim().split(" ");
        StringBuilder currentLine = new StringBuilder();
        List<String> newLines = new ArrayList<String>();

        int currentLength = 0;
        for(int i = 0; i < words.length; i++){
            currentLine.append(words[i] + " ");
            currentLength = currentLine.length();

            int nextWordLength = 0;
            if(i + 1 < words.length) nextWordLength = words[i + 1].length();
            if(currentLength + nextWordLength >= width - 2 || i + 1 >= words.length){
                newLines.add(currentLine.toString());
                currentLine = new StringBuilder();
            }
        }

        return newLines;
    }
}
