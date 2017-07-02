package com.mac.rltut.engine.util;

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
        int runningWeight = 0;
        int roll = random.nextInt(totalWeight - 1) + 1;

        for(PoolItem<T> poolItem : poolItems){
            runningWeight += poolItem.weight;
            if(roll <= runningWeight){
                remove(poolItem);
                return poolItem.item;
            }
        }

        throw new RuntimeException("Cannot get item from pool.");
    }

    public void add(T item, int weight){
        poolItems.add(new PoolItem<T>(weight, item));
        if(weight < 1) weight = 1;
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
