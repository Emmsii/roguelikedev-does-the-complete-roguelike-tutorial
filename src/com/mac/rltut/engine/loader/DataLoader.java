package com.mac.rltut.engine.loader;

import com.mac.rltut.engine.parser.DataObject;
import com.mac.rltut.engine.parser.DataParser;

import java.io.*;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 07/07/2017 at 09:25 AM.
 */
public abstract class DataLoader {
    
    private final InputStream in;
    private final String fileName;
    
    protected List<DataObject> data;
    protected DataObject defaults;
    
    public DataLoader(String fileName) throws IOException {
        this.in = DataLoader.class.getClassLoader().getResourceAsStream(fileName);
        this.fileName = fileName;
        parse();
    }
    
    public abstract void load();
    
    private void parse() throws IOException {
        DataParser parser = new DataParser(fileName).parse(new BufferedReader(new InputStreamReader(in)));
        data = parser.dataObjects();
    }
    
    protected void loadDefaults(){
        for(DataObject obj : data){
            if(obj.type().equalsIgnoreCase("defaults")){
                defaults = obj;
                return;
            }
        }
    }
}
