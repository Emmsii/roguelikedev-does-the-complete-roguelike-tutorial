package com.mac.rltut.game.map;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.util.MathUtil;
import com.mac.rltut.game.map.tile.Tile;

import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 30/06/2017 at 11:05 AM.
 */
public class MapBuilder2 {

    private final int width, height, depth;
    private final long seed;
    private final Random random;

    private Map map;

    private boolean[][] lakes;
    private int[][] regions;
    private int nextRegion;

    public MapBuilder2(int width, int height, int depth, long seed){
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.seed = seed;
        this.random = new Random(seed);
        this.map = new Map(width, height, depth, seed);
    }

    public MapBuilder2 generate(){
        Log.debug("Generating " + width + "x" + height + "x" + depth + " map [" + seed + "]");
        double start = System.nanoTime();

        for(int z = 0; z < depth; z++){

           DefaultLevel level = new DefaultLevel(width, height, z, -1, -1, 100);
           level.init(random);
           level.generate();
           level.saveImage(z);
           map.setTiles(z, level.tiles());
        }

        Log.debug("Generated map in " + ((System.nanoTime() - start) / 1000000) + "ms");
        return this;
    }

    public Map build(){
        return map;
    }
}
