package com.mac.rltut.engine.util;

import com.esotericsoftware.minlog.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 01:11 PM.
 */
public class Pool<T> {
    
    public static final int DEFAULT_WEIGHT = 100;

    private List<PoolItem<T>> poolItems;
    private final Random random;
    private int totalWeight;

    public Pool() {
        this(new Random());
    }

    public Pool(Random random){
        this.poolItems = new ArrayList<PoolItem<T>>();
        this.random = random;
    }

    public T get(){
        if(isEmpty()){
            Log.error("Cannot get item from an empty pool");
            return null;
        }

        int rand = random.nextInt(totalWeight);
        
        for(PoolItem<T> poolItem : poolItems){
            rand -= poolItem.weight;
            if(rand <= 0){
                remove(poolItem);
                return poolItem.item;
            }
        }
        
        throw new RuntimeException("Cannot get item from pool. (size: " + poolItems.size() + ")");
    }

    public void add(T item, int weight){
        if(weight < 1){
            Log.warn("Cannot add item [" + item + "] with weight [" + weight + "] less than 1.");
            return;
        }
        
        poolItems.add(new PoolItem<T>(weight, item));
        totalWeight += weight;
    }

    public void remove(PoolItem<T> poolItem){
        poolItems.remove(poolItem);
        totalWeight -= poolItem.weight;
    }
    
    public boolean contains(T item){
        return poolItems.contains(item);
    }

    public int size(){
        return poolItems.size();
    }

    public boolean isEmpty(){
        return poolItems.isEmpty();
    }
    
    public void clear(){
        poolItems.clear();
        totalWeight = 0;
    }

    @SuppressWarnings("hiding")
    class PoolItem<T>{
        public int weight;
        public T item;

        public PoolItem(int weight, T item){
            this.weight = weight;
            this.item = item;
        }
        
        public int hashCode(){
            return item.hashCode();
        }
        
        public boolean equals(Object o){
            return item.equals(o);
        }
    }
}
