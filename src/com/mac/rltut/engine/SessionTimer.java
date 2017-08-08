package com.mac.rltut.engine;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 07/08/2017 at 02:18 PM.
 */
public class SessionTimer {
    
    private long start;
    private long end;
    private long offset;

    protected SessionTimer(){}
    
    public SessionTimer(long start){
        this(start, 0);
    }

    public SessionTimer(long start, long offset){
        this.start = start;
        this.end = start;
        this.offset = offset;
    }
    
    public void update(){
        end = System.currentTimeMillis();
    }
    
    public long durationMilliseconds(){
        return (end - start) + offset;
    }
    
    public String prettyString(){
        int seconds = (int) ((durationMilliseconds() / 1000) % 60);
        int minutes = (int) ((durationMilliseconds() / (1000 * 60)) % 60);
        int hours = (int) ((durationMilliseconds() / (1000 * 60 * 60)) % 24);
        return String.format("%d hours, %d min, %d sec", hours, minutes, seconds);
    }
}
