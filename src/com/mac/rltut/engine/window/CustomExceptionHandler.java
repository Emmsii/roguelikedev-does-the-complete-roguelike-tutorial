package com.mac.rltut.engine.window;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 10/07/2017 at 12:40 PM.
 * 
 * Found on stackoverflow: https://stackoverflow.com/questions/21089278/how-do-i-make-my-jar-application-generate-a-crash-log-file
 */
public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {
    
    private final String logFolder;
    
    public CustomExceptionHandler(String logFolder){
        this.logFolder = logFolder;
        File file = new File(logFolder);
        if(!file.exists()) file.mkdirs();
    }
    
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss");

        File file = new File("logs/");
        if(!file.exists()) file.mkdirs();

        String fileName = "logs/" + sdf.format(cal.getTime()) + ".txt";

        PrintStream writer = null;

        try {
            writer = new PrintStream(fileName, "UTF-8");
            writer.println("Exception in thread \"" + t.getName() + "\" " + e.getClass() + ": " + e.getMessage());
            for(int i = 0; i < e.getStackTrace().length; i++){
                writer.println(e.getStackTrace()[i].toString());
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }finally {
            writer.flush();
            writer.close();
        }
    }
}
