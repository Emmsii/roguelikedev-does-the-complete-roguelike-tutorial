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
    private final String name;
    
    protected List<DataObject> data;
    protected DataObject defaults;
    
    public DataLoader(InputStream in, String name) throws IOException {
        this.in = in;
        this.name = name;
        parse();
    }
    
    public abstract void load();
    
    private void parse() throws IOException {
        DataParser parser = new DataParser(name).parse(new BufferedReader(new InputStreamReader(in)));
//        DataParser parser = new DataParser(file.getName()).parse(new BufferedReader(new FileReader(file)));
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
