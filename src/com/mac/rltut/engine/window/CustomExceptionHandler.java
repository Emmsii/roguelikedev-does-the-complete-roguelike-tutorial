package com.mac.rltut.engine.window;

import com.mac.rltut.engine.Engine;

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
    }
    
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss");

        File file = new File(logFolder);
        if(!file.exists()) file.mkdirs();

        String fileName = "logs/" + sdf.format(cal.getTime()) + ".txt";

        PrintStream writer = null;

        long megabytes = 1024 * 1024;
        
        try {
            writer = new PrintStream(fileName, "UTF-8");
            writer.println("Java Vendor: " + System.getProperty("java.vendor"));
            writer.println("Java Vendor URL: " + System.getProperty("java.vendor.url"));
            writer.println("Java Version: " + System.getProperty("java.version"));
            writer.println("OS Name: " + System.getProperty("os.name"));
            writer.println("OS Architecture: " + System.getProperty("os.arch"));
            writer.println("OS Version: " + System.getProperty("os.version"));
            
            writer.println("\nFree Memory (mb): " + Runtime.getRuntime().freeMemory() / (float) megabytes);
            long maxMemory = Runtime.getRuntime().maxMemory();
            writer.println("Max Memory (mb): " + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory / (float) megabytes));
            writer.println("Total Memory (mb): " + Runtime.getRuntime().totalMemory() / (float) megabytes);
            writer.println("Used Memory (mb): " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (float) megabytes));
            
            writer.println("\nGame Version: " + Engine.instance().version());
            writer.println("\n-------------------------------------------------------");
            writer.println("\nException in thread \"" + t.getName() + "\" " + e.getClass() + ": " + e.getMessage());
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
