package com.mac.rltut.game.world.builders;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.loader.CreatureSpawnProperty;
import com.mac.rltut.engine.util.MathUtil;
import com.mac.rltut.engine.util.Point;
import com.mac.rltut.engine.util.Pool;
import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.world.World;
import com.mac.rltut.game.world.levels.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 30/06/2017 at 11:05 AM.
 */
public class WorldBuilder {

    private final int width, height, depth;
    private final long seed;
    private final Random random;

    private World world;

    private String creatureSpawnBaseCount = "17-25"; 
    
    private float[] creatureSpawnMultiplier;

    public WorldBuilder(int width, int height, int depth, long seed){
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.seed = seed;
        this.random = new Random(seed);
        this.world = new World(width, height, depth, seed);
        this.creatureSpawnMultiplier = new float[depth];
    }
    
    public WorldBuilder generate(){
        Log.debug("Generating " + width + "x" + height + "x" + depth + " world [" + seed + "]");
        double start = System.nanoTime();

        List<LevelBuilder> levels = new ArrayList<LevelBuilder>();
        levels.add(new DefaultLevel(width, height, 0, depth + 1, 70, -0.5f, 1f, random));
        levels.add(new DenseLevel(width, height, 1, depth + 1, 55, 0.95f, 1.5f, random));
        levels.add(new SparseLevel(width, height, 1, 5, 65, 0.8f, 0.8f, random));
        levels.add(new LakesLevel(width, height, 3, 10, 50, 1.325f, 1f, random));
        levels.add(new SwampLevel(width, height, 6, depth + 1, 40, 1.5f, 1.35f, random));
        levels.add(new DarkLevel(width, height, 10, depth + 1, 20, 1.85f, 1.8f, random));
        levels.add(new RuinedLevel(width, height, 3, depth + 1, 8, 0f, 1.7f, random));

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
                if(chance < 2) chance = 2;
                if (z >= l.minLevel() && z <= l.maxLevel()) pool.add(l, chance);
            }

            LevelBuilder level = pool.get();// TODO: This might break

            level.generate(z);
            creatureSpawnMultiplier[z] = level.creatureSpawnModifier();
//            level.saveImage(z);
            world.setLevel(z, level.build());
        }

        Log.debug("Generated world in " + ((System.nanoTime() - start) / 1000000) + "ms");
        return this;
    }
    
    public WorldBuilder populate(){
        Collection<CreatureSpawnProperty> creatures = Codex.creatures.values();
        
        int creaturesSpawned = 0;
        
        for(int z = 0; z < depth; z++){
            
            List<CreatureSpawnProperty> canSpawn = new ArrayList<CreatureSpawnProperty>();
            for(CreatureSpawnProperty c : creatures){
                if(c.canSpawnOnType(world.level(z).type()) && c.canSpawnAtDepth(z)) canSpawn.add(c);
            }
            
            if(canSpawn.isEmpty()){
                Log.warn("No creatures spawned on level " + (z + 1) + "!");
                continue;
            }
            
            int count = (int) (creatureSpawnMultiplier[z] * MathUtil.randomIntFromString(creatureSpawnBaseCount, random) + (z * 1.2));
            int total = 0;

            while(total < count) {
                Pool<CreatureSpawnProperty> pool = new Pool<CreatureSpawnProperty>();
                for (CreatureSpawnProperty c : canSpawn) pool.add(c, c.spawnWeight());
                
                CreatureSpawnProperty toSpawn = pool.get();

                Point spawn = null;
                int packSize = toSpawn.packSize(random);
                
                if(toSpawn.spawnNear().isEmpty()) spawn = world.randomEmptyPoint(z);
                else{
                    String spawnNear = toSpawn.spawnNear().get(random.nextInt(toSpawn.spawnNear().size()));
                    spawn = world.randomEmptyPointNearType(z, spawnNear);
                }
                
                if(packSize == 0) {
                    world.add(spawn.x, spawn.y, spawn.z, (Creature) toSpawn.creature().newInstance());
                    total++;
                }else{
                    for(int i = 0; i < packSize; i++){
                        Point newSpawn = world.randomEmptyNearPoint(spawn);
                        world.add(newSpawn.x, newSpawn.y, newSpawn.z, (Creature) toSpawn.creature().newInstance());
                        total++;
                    }
                }
            }
            creaturesSpawned += total;
        }
        Log.debug("Spawned " + creaturesSpawned + " creatures total.");
        return this;
    }

    public World build(){
        return world;
    }
}
