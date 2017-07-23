package com.mac.rltut.engine.parser;

import com.esotericsoftware.minlog.Log;
import jdk.nashorn.internal.runtime.ParserException;

import java.util.HashMap;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 06/07/2017 at 12:07 PM.
 */
public class DataObject {

    private String type;
    private HashMap<String, Object> values;

    public DataObject(String type){
        this.type = type;
        this.values = new HashMap<String, Object>();
    }

    public boolean isType(String input){
        return type.equalsIgnoreCase(input.trim().toLowerCase());
    }
    
    public void add(String token, Object value){
        values.put(token, value);
    }
    
    public String getString(String token){
        return (String) get(token);
    }
    
    public Boolean getBoolean(String token){
        String value = (String) get(token);
        if(value == null) return null;
        return Boolean.parseBoolean(value);
    }
    
    public Integer getInt(String token){
        String value = (String) get(token);
        if(value == null) return null;
        return Integer.parseInt(value);
    }
    
    public Float getFloat(String token){
        String value = (String) get(token);
        if(value == null) return null;
        return Float.parseFloat(value);
    }
    
    private Object get(String token){
        token = token.toUpperCase().trim();
        if(!values.containsKey(token)){
            throw new DataObjectException("Data Object [" + type + "] does not contain the token [" + token + "]");
        }
        return values.get(token);
    }
    
    public boolean hasToken(String token){
        token = token.toUpperCase().trim();
        return values.containsKey(token);
    }
    
    public String type(){
        return type;
    }
}
