package com.mac.rltut.engine.parser;

import com.esotericsoftware.minlog.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 06/07/2017 at 11:50 AM.
 */
public class DataParser {
    
    private String name;
    private List<DataObject> dataObjects;
    
    public DataParser(String name){
        this.name = name;
        this.dataObjects = new ArrayList<DataObject>();
    }
    
    public DataParser parse(BufferedReader reader) throws IOException {
        String line = null;
        DataObject currentObject = null;
        int lineNumber = 0;
        
        while((line = reader.readLine()) != null) {
            lineNumber++;
            line = line.trim();
            
            if(line.length() == 0) continue;
            if(line.startsWith("#")) continue;
            else if(line.startsWith("[") && line.endsWith("]")){
                String objectName = line.substring(1, line.length() - 1);
                currentObject = new DataObject(objectName);
                dataObjects.add(currentObject);
            }else{
                if (currentObject == null) continue;
                String[] split = line.split("=");
                if (split.length == 2) {
                    currentObject.add(split[0], split[1]);
                } else {
                    Log.error("Cannot parse line [" + lineNumber + "] of file [" + name + "]");
                }
            }
        }
        
        reader.close();
        return this;
    }
    
    public List<DataObject> dataObjects(){
        return dataObjects;
    }
}