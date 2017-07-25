package com.mac.rltut.game.world.builders;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.game.entity.creature.Boss;
import com.mac.rltut.game.entity.creature.util.BossSpawnProperty;
import com.mac.rltut.game.entity.creature.util.CreatureSpawnProperty;
import com.mac.rltut.engine.util.MathUtil;
import com.mac.rltut.engine.util.Point;
import com.mac.rltut.engine.util.Pool;
import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.ai.*;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.ItemStack;
import com.mac.rltut.game.world.World;
import com.mac.rltut.game.world.levels.*;

import java.io.File;
import java.util.*;

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

    private String creatureSpawnBaseCount = "12-20"; 
    private String maxCreatureTypesPerLevel = "5-6";
    private String itemsPerLevel = "4-8";
    
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
        levels.add(new DefaultLevel(width, height, 0, depth + 1, 70, -0.5f, 1.5f, random));
        levels.add(new DenseLevel(width, height, 1, depth + 1, 55, 0.95f, 1.5f, random));
        levels.add(new SparseLevel(width, height, 1, 8, 70, 0.9f, 1.5f, random));
        levels.add(new LakeLevel(width, height, 3, 10, 50, 1.325f, 1.5f, random));
        levels.add(new SwampLevel(width, height, 6, depth + 1, 40, 1.5f, 1.5f, random));
        levels.add(new DarkLevel(width, height, 10, depth + 1, 20, 1.85f, 1.7f, random));
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

    //Temp for debugging
    List<HashMap<String, Integer>> spawnCounts = new ArrayList<HashMap<String, Integer>>();
    
    public WorldBuilder populate(){
        Collection<CreatureSpawnProperty> creatures = Codex.creatures.values();
        
        int creaturesSpawned = 0;
        int spawnedThisLevel = 0;
        HashSet<String> uniquesSpawned = new HashSet<String>();
        
        for(int z = 0; z < depth; z++){
            spawnedThisLevel = 0;
            spawnCounts.add(new HashMap<>());
            boolean canSpawnBoss = false;
            List<CreatureSpawnProperty> canSpawn = new ArrayList<CreatureSpawnProperty>();
            for(CreatureSpawnProperty c : creatures){
                if(c.canSpawnOnType(world.level(z).type()) && c.canSpawnAtDepth(z + 1)){
                    if(c.creature() instanceof Boss) canSpawnBoss = true;
                    canSpawn.add(c);
                }
            }
            
            if(canSpawn.isEmpty()){
                Log.warn("No creatures spawned on level " + (z + 1) + "!");
                continue;
            }

            int count = (int) (creatureSpawnMultiplier[z] * MathUtil.randomIntFromString(creatureSpawnBaseCount, random) + (z * 1.2));

            if(canSpawnBoss) {
                Pool<BossSpawnProperty> pool = new Pool<BossSpawnProperty>();
                for (CreatureSpawnProperty c : canSpawn){
                    if (c instanceof BossSpawnProperty){
                        if(uniquesSpawned.contains(c.creature().name())) continue;
                        pool.add((BossSpawnProperty) c, c.spawnWeight());
                    }
                } 
                
                if(pool.isEmpty()){
                     Log.debug("Cannot spawn boss on level [" + z + "]");
                }else {
                    BossSpawnProperty boss = pool.get();
                    if (boss.isUnique()) uniquesSpawned.add(boss.creature().name());

                    Point spawn = getSpawnPoint(boss, z);
                    Boss newBoss  = (Boss) newCreature(spawn, (Creature) boss.creature().newInstance());
                    BossAI bossAI = new BossAI(newBoss);
                    modifyStats(newBoss, z);
                    spawnedThisLevel++;

                    int minionCount = MathUtil.randomIntFromString(boss.minionCount(), random);

                    if (minionCount > 0) {
                        List<CreatureSpawnProperty> minions = new ArrayList<CreatureSpawnProperty>();
                        for (String s : boss.minions()) minions.add(Codex.creatures.get(s.toLowerCase()));

                        PackAI pack = new PackAI();
                        for (int i = 0; i < minionCount; i++) {
                            Pool<CreatureSpawnProperty> minionPool = new Pool<CreatureSpawnProperty>();
                            for (CreatureSpawnProperty c : minions) minionPool.add(c, c.spawnWeight()); //TODO: Find why this makes null pointer

                            CreatureSpawnProperty minion = minionPool.get();
                            Point minionSpawn = world.randomEmptyPointInRadius(spawn, 6);
                            Creature packMember = newCreature(minionSpawn, (Creature) minion.creature().newInstance());
                            pack.addPackMember(new PackMemberAI(packMember, pack));
                            modifyStats(packMember, z);
                            spawnedThisLevel++;
                        }
                        bossAI.setPack(pack);
                    }
                }
            }
            
            HashSet<String> typesSpawned = new HashSet<>();
            int typesThisLevel = MathUtil.randomIntFromString(maxCreatureTypesPerLevel, random);
            
            while(spawnedThisLevel < count) {
                Pool<CreatureSpawnProperty> pool = new Pool<CreatureSpawnProperty>();
                for (CreatureSpawnProperty c : canSpawn){
                    if(c instanceof BossSpawnProperty) continue;
                    if(!typesSpawned.contains(c.creature().name()) && typesSpawned.size() >= typesThisLevel) continue;
                    pool.add(c, c.spawnWeight());
                }
                
                CreatureSpawnProperty toSpawn = pool.get();
                Point spawn = getSpawnPoint(toSpawn, z);
                int packSize = toSpawn.packSize(random);
                typesSpawned.add(toSpawn.creature().name());
                
                if(packSize == 0) {
                    Creature newCreature = newCreature(spawn, (Creature) toSpawn.creature().newInstance());
                    getAI(toSpawn.creature().aiType(), newCreature);
                    modifyStats(newCreature, z);
                    spawnedThisLevel++;
                }else{
                    PackAI pack = new PackAI();
                    for(int i = 0; i < packSize; i++){
                        Point newSpawn = world.randomEmptyPointInRadius(spawn, 4);
                        Creature packMember = newCreature(newSpawn, (Creature) toSpawn.creature().newInstance());
                        pack.addPackMember(new PackMemberAI(packMember, pack));
                        modifyStats(packMember, z);
                        spawnedThisLevel++;
                    }
                }
            }
            creaturesSpawned += spawnedThisLevel;
//            Log.debug("Level [" + z + "] [" + world.level(z).type() +"] " + spawnCounts.get(z) + " Total [" + spawnedThisLevel + "]");
        }
//        Log.debug("Spawned " + creaturesSpawned + " creatures total.");

        return this;
    }
    
    public WorldBuilder spawnItems(){
        
        Point spawn = world.randomEmptyPointInRadius(world.startPointAt(0), 12);
        world.add(spawn.x, spawn.y, spawn.z, (Item) Codex.items.get("dagger").newInstance());
        
        for(int z = 0; z < depth; z++){
            
            int items = MathUtil.randomIntFromString(itemsPerLevel, random);
            
            for(int i = 0; i < items; i++){
                Pool<Item> pool = new Pool<Item>();
                for(Item item : Codex.items.values()){
                    if(item.spawnChance() == -1) continue;
                    pool.add(item, item.spawnChance());
                }
                
                if(pool.isEmpty()){
                    Log.warn("Cannot get item from empty pool.");
                    continue;
                }
                
                Item newItem = (Item) pool.get().newInstance();
                if(newItem != null){
                    if(newItem instanceof ItemStack){
                        ItemStack stack = (ItemStack) newItem;
                        stack.setAmount(MathUtil.randomIntFromString(stack.spawnAmount(), random));
                    }
                    spawn = world.randomEmptyPoint(z);
                    world.add(spawn.x, spawn.y, spawn.z, newItem);
                }
            }
        }
        
        return this;
    }
    
    private Point getSpawnPoint(CreatureSpawnProperty toSpawn, int z){
        Point spawn = null;
        boolean blocked = true;
        while(blocked) {
            if(toSpawn.spawnNear().isEmpty()) spawn = world.randomEmptyPoint(z);
            else{
                String spawnNear = toSpawn.spawnNear().get(random.nextInt(toSpawn.spawnNear().size()));
                spawn = world.randomEmptyPointNearType(z, spawnNear);
            }
            
            if(MathUtil.distance(spawn.x, spawn.y, world.startPointAt(z).x, world.startPointAt(z).y) < 10) continue;
            
            blocked = false;
            for(int y = spawn.y; y <= spawn.y + toSpawn.creature().size(); y++){
                for(int x = spawn.x; x <= spawn.x + toSpawn.creature().size(); x++){
                    if(world.solid(x, y, z) || world.creature(x, y, z) != null){
                        blocked = true;
                        break;
                    }
                }
            }
        }
        return spawn;
    }

    private Creature newCreature(Point spawn, Creature creature){
        world.add(spawn.x, spawn.y, spawn.z, creature);
        //modifyStats(creature, spawn.z);
        
        if(!spawnCounts.get(spawn.z).containsKey(creature.name())) spawnCounts.get(spawn.z).put(creature.name(), 0);
        spawnCounts.get(spawn.z).put(creature.name(), spawnCounts.get(spawn.z).get(creature.name()) + 1);
        return creature;
    }
    
    private void modifyStats(Creature creature, int z){
//        creature.modifyMaxHp(z / 4);
//        creature.modifyMaxMana(z / 4);
//        creature.modifyStrength(z / 4);
//        creature.modifyDefense(z / 4);
//        creature.modifyAccuracy(z / 4);
//        creature.modifyIntelligence(z / 4);
//        creature.modifyHp(creature.maxHp(), "");
//        creature.modifyMana(creature.maxMana());
        int xp = (int) (Math.pow(1.295, z + 3) * 10);
        creature.modifyXp(xp);
        
        
//        Log.debug("Stats for " + creature.name() + " on level " + z);
//        Log.debug("HP: " + creature.maxHp());
//        Log.debug("MAN: " + creature.maxMana());
//        Log.debug("STR: " + creature.strength());
//        Log.debug("DEF: " + creature.defense());
//        Log.debug("ACC: " + creature.accuracy());
//        Log.debug("INT: " + creature.intelligence());
        
    }
    
    private CreatureAI getAI(String ai, Creature creature){
        if(ai.equalsIgnoreCase("aggressive")) return new AggressiveAI(creature);
        else if(ai.equalsIgnoreCase("passive")) return new PassiveAI(creature);
        else return new CreatureAI(creature);
    }
    
    public World build(){
        world.init();
        return world;
    }
}
