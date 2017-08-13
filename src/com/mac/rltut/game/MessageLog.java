package com.mac.rltut.game;

import com.mac.rltut.engine.util.ColoredString;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 18/07/2017 at 11:28 AM.
 */
public class MessageLog {
    
    private List<ColoredString> entries;
    private int newEntries;

    public MessageLog(){
        this.entries = new ArrayList<ColoredString>();
        this.newEntries = 0;
    }
    
    public void add(String text){
        add(new ColoredString(text));
    }
    
    public void add(ColoredString string){
        if(!checkForRepeat(string.text, string.color)){
            entries.add(string);
            newEntries++;
        }
    }
    
    private boolean checkForRepeat(String text, int color){
        if(entries == null || entries.isEmpty()) return false;
        
        String previous = entries.get(entries.size() - 1).text;
        String previousClean = previous;

        String[] previousSplit = previous.split(" ");
        if(previousSplit[previousSplit.length - 1].startsWith("x")){
            previousClean = "";
            for(int i = 0; i < previousSplit.length - 1; i++) previousClean += previousSplit[i] + " ";
        }

        if(previousClean.trim().equals(text.trim())){
            String[] split = previous.split(" ");
            int count = 1;
            if(split[split.length - 1].startsWith("x")){
                String last = split[split.length - 1];
                count = Integer.parseInt(last.substring(1));
            }
            count += 1;
            entries.set(entries.size() - 1, new ColoredString(text += " x" + count, color));
            return true;
        }
        return false;
    }
    
    public int newEntries(){
        return newEntries;
    }
    
    public void resetNewEntryCount(){
        newEntries = 0;
    }
    
    public List<ColoredString> getEntries(){
        return entries;
    }

}
