package com.mac.rltut.game.entity.item;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 02:54 PM.
 */
public class Inventory<T> {
    
    public static final int DEFAULT_SIZE = 20;
    
    private List<T> items;
    private int maxSize;
    
    public Inventory(){
        this(DEFAULT_SIZE);
    }
    
    public Inventory(int maxSize){
        this.items = new ArrayList<T>();
        this.maxSize = maxSize;
    }
    
    public List<T> items(){
        return items;
    }
    
    public T get(int i){
        if(i < 0 || i >= items.size()) return null;
        return items.get(i);
    }
    
    public boolean add(T item){
        if(item == null || isFull()) return false;
        return items.add(item);
    }
    
    public boolean remove(T item){
        if(item == null || isEmpty()) return false;
        return items.remove(item);
    }
    
    public boolean isEmpty(){
        return items.isEmpty();
    }
    
    public boolean isFull(){
        return items.size() == maxSize;
    }
    
    public int count(){
        return items.size();
    }
}
