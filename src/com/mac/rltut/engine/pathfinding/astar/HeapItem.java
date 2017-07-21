package com.mac.rltut.engine.pathfinding.astar;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 08:25 AM.
 */
public abstract class HeapItem<T> implements Comparable<T>{
    
    public int heapIndex;

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof HeapItem<?>)) return false;
        HeapItem<T> other = (HeapItem<T>) obj;
        if(other.heapIndex != heapIndex) return false;
        return true;
    }
}
