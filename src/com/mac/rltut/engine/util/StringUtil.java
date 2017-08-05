package com.mac.rltut.engine.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 18/07/2017 at 09:27 AM.
 */
public class StringUtil {
    
    public static String getPercent(float amount){
        return Math.round(amount * 100) + "%";
    }
    
    public static String clean(String text){
        if(text == null || text.length() == 0) return text;
        return text.toLowerCase().trim().replace("_", " ");
    }
    
    public static String capitalizeFirst(String text){
        if(text == null || text.length() == 0) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1, text.length());
    }
    
    public static String capitalizeEachWord(String text){
        if(text == null || text.length() == 0) return text;
        String[] split = text.split(" ");
        StringBuilder result = new StringBuilder();
        for(String s : split){
            if(s.equalsIgnoreCase("the") || s.equalsIgnoreCase("of")) result.append(s).append(" ");
            else result.append(s.substring(0, 1).toUpperCase()).append(s.substring(1, s.length())).append(" ");
        }
        return result.toString().trim();
    }
    
    public static String makeSecondPerson(String text){
        if(text == null || text.length() == 0) return "null";
        int space = text.indexOf(" ");
        if(space == -1) return text + "s";
        else return text.substring(0, space) + "s" + text.substring(space);
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
