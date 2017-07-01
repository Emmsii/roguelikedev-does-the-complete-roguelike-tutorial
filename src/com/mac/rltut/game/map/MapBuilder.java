package com.mac.rltut.game.map;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.util.Pool;
import com.mac.rltut.game.map.levels.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 30/06/2017 at 11:05 AM.
 */
public class MapBuilder {

    private final int width, height, depth;
    private final long seed;
    private final Random random;

    private Map map;

    private boolean[][] lakes;
    private int[][] regions;
    private int nextRegion;

    public MapBuilder(int width, int height, int depth, long seed){
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.seed = seed;
        this.random = new Random(seed);
        this.map = new Map(width, height, depth, seed);
    }

    public MapBuilder generate(){
        Log.debug("Generating " + width + "x" + height + "x" + depth + " map [" + seed + "]");
        double start = System.nanoTime();

        List<LevelBuilder> levels = new ArrayList<LevelBuilder>();
        levels.add(new DefaultLevel(width, height, -1, 3, 100, -2.6f, random));
        levels.add(new DenseLevel(width, height, -1, -1, 60, 1f, random));
        levels.add(new SparseLevel(width, height, -1, 10, 60, 1f, random));
        levels.add(new LakesLevel(width, height, 3, 9, 50, 1f, random));
        levels.add(new SwampLevel(width, height, 6, -1, 50, 1f, random));
        levels.add(new DarkLevel(width, height, 10, -1, 30, 1.25f, random));
        
        
        for(int z = 0; z < depth; z++){
            
            Pool<LevelBuilder> pool = new Pool<LevelBuilder>();
            for(LevelBuilder l : levels){
                if(l.minLevel() == -1 || l.minLevel() >= z || l.maxLevel() == -1 || l.maxLevel() <= z){ //TODO: Fix me
                    int zChance = (int) ((10 + z) * l.zMultiplier());
                    pool.add(l, l.chance() + zChance);
                }
            }
            
            LevelBuilder level = pool.get();
            
            level.generate(z);
            level.saveImage(z);
            map.setTiles(z, level.tiles());
            Log.debug("Level " + z + " " + level.type());
        }

        Log.debug("Generated map in " + ((System.nanoTime() - start) / 1000000) + "ms");
        return this;
    }

    public Map build(){
        return map;
    }
}
