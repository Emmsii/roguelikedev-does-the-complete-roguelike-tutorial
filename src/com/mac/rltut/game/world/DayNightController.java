package com.mac.rltut.game.world;

import com.esotericsoftware.minlog.Log;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 17/07/2017 at 12:05 PM.
 */
public class DayNightController {
    
    private int dayLength;
    private int day;
    private int currentTime;

    private int morningTime;
    private int eveningTime;
        
    private int minLight, maxLight;
    private int light;

    private int lightChangeEvery;
    
    public int tick;
    
    public DayNightController(int dayLength, int minLight, int maxLight, int lightChangeEvery){
        this.day = 1;
        this.dayLength = dayLength;
        this.minLight = minLight;
        this.maxLight = maxLight;
        this.morningTime = (dayLength / 2) - (dayLength / 4);
        this.eveningTime = (dayLength / 2) + (dayLength / 4);
        this.currentTime = morningTime;
        this.light = maxLight;
        this.lightChangeEvery = lightChangeEvery;
    }
    
    public void update(){
        tick++;
        currentTime++;
        if(currentTime > dayLength){
            currentTime = 0;
            day++;
        }
                
        if(currentTime % lightChangeEvery == 0) {
            if (isNight() && light > minLight) modifyLight(-1);
            if (isDay() && light < maxLight) modifyLight(1);
        }
    }
    
    private void modifyLight(int amount){
        light += amount;
        if(light > maxLight) light = maxLight;
        if(light < minLight) light = minLight;
    }
    
    public float lightPercent(){
        
        if(light == minLight) return 0f;
        else if(light == minLight) return 1f;
        
        int lightFloor = light - minLight;
        
        float result = (float) lightFloor / (float) (maxLight - minLight);
        
        return result;
    }
    
    public int light(){
        return light;
    }
        
    public int day(){
        return day;
    }
    
    public int currentTime(){
        return currentTime;
    }
    
    public boolean isDay(){
        return currentTime >= morningTime && currentTime < eveningTime;
    }
    
    public boolean isNight(){
        return currentTime >= eveningTime || currentTime() < morningTime;
    }
}
