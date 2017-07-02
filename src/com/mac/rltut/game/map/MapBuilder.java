package com.mac.rltut.game.map;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.util.Pool;
import com.mac.rltut.game.map.levels.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
        levels.add(new DefaultLevel(width, height, 0, depth, 70, -1.75f, random));
        levels.add(new DenseLevel(width, height, 1, depth, 55, 0.95f, random));
        levels.add(new SparseLevel(width, height, 1, 5, 65, 0.8f, random));
        levels.add(new LakesLevel(width, height, 3, 10, 50, 1.325f, random));
        levels.add(new SwampLevel(width, height, 6, depth, 40, 1.5f, random));
        levels.add(new DarkLevel(width, height, 10, depth, 20, 1.85f, random));
        levels.add(new RuinedLevel(width, height, 3, depth, 8, 0, random));

        //Temp code
        //Deletes all images in image folder for level preview
        File file = new File("images/");
        File[] files = file.listFiles();
        if(files != null) for(File f : files) f.delete();
        
        for (int z = 0; z < depth; z++) {
            Pool<LevelBuilder> pool = new Pool<LevelBuilder>();
            for (LevelBuilder l : levels) {
                int zChance = (int) ((z * l.zMultiplier()) * 3);
                int chance = l.chance() + zChance;
                if (z >= l.minLevel() && z <= l.maxLevel()) pool.add(l, chance);
            }

            LevelBuilder level = pool.get();
                
            level.generate(z);
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
