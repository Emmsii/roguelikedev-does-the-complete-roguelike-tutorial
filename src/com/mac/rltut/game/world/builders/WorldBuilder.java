package com.mac.rltut.game.world.builders;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.Pool;
import com.mac.rltut.engine.util.maths.MathUtil;
import com.mac.rltut.engine.util.maths.Point;
import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.entity.creature.Boss;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.NPC;
import com.mac.rltut.game.entity.creature.Wizard;
import com.mac.rltut.game.entity.creature.ai.*;
import com.mac.rltut.game.entity.item.*;
import com.mac.rltut.game.entity.item.util.Inventory;
import com.mac.rltut.game.entity.item.util.JewelryGenerator;
import com.mac.rltut.game.entity.item.util.PotionBuilder;
import com.mac.rltut.game.entity.item.util.SpellbookGenerator;
import com.mac.rltut.game.entity.util.BossSpawnProperty;
import com.mac.rltut.game.entity.util.CreatureSpawnProperty;
import com.mac.rltut.game.entity.util.ItemSpawnProperty;
import com.mac.rltut.game.world.World;
import com.mac.rltut.game.world.levels.*;
import com.mac.rltut.game.world.objects.Chest;

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

    private final String creatureSpawnBaseCount = "12-20"; 
    private final String maxCreatureTypesPerLevel = "5-6";
    
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
        levels.add(new DefaultLevel(width, height, 0, depth + 1, 70, -0.5f, 1.5f, 0, random));
        levels.add(new DenseLevel(width, height, 1, depth + 1, 55, 0.95f, 1.5f, -4, random));
        levels.add(new SparseLevel(width, height, 1, 8, 70, 0.9f, 1.5f, 3, random));
        levels.add(new LakeLevel(width, height, 3, 10, 50, 1.325f, 1.5f, 2, random));
        levels.add(new SwampLevel(width, height, 6, depth + 1, 40, 1.5f, 1.5f, -2, random));
        levels.add(new DarkLevel(width, height, 10, depth + 1, 20, 1.85f, 1.7f, -5, random));
        levels.add(new RuinedLevel(width, height, 3, depth + 1, 8, 1.25f, 1.7f, 0, random));

        File file = new File("images/");
        File[] files = file.listFiles();
        if(files != null) for(File f : files) f.delete();
        
        for (int z = 0; z < depth; z++) {
            Pool<LevelBuilder> pool = new Pool<LevelBuilder>(random);
            for (LevelBuilder l : levels) {
                int zChance = (int) ((z * l.zMultiplier()) * 3);
                int chance = l.chance() + zChance;
                if (z >= l.minLevel() && z <= l.maxLevel()) pool.add(l, chance);
            }

            LevelBuilder level = pool.get();

            level.generate(z);
//            level.buildShop();
            
            
            creatureSpawnMultiplier[z] = level.creatureSpawnModifier();
            world.setLevel(z, level.build());
            level.saveImage(z);
        }

        Log.debug("Generated world in " + ((System.nanoTime() - start) / 1000000) + "ms");
        return this;
    }

    //Temp for debugging
    List<HashMap<String, Integer>> spawnCounts = new ArrayList<HashMap<String, Integer>>();
    List<HashMap<String, Integer>> itemSpawnCounts = new ArrayList<HashMap<String, Integer>>();
    List<HashMap<String, Integer>> chestItemSpawnCounts = new ArrayList<HashMap<String, Integer>>();
    
    public WorldBuilder populate(){
        double start = System.nanoTime();
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
                Pool<BossSpawnProperty> pool = new Pool<BossSpawnProperty>(random);
                for (CreatureSpawnProperty c : canSpawn){
                    if (c instanceof BossSpawnProperty){
                        if(uniquesSpawned.contains(c.creature().name()
                        )) continue;
                        pool.add((BossSpawnProperty) c, c.chance());
                    }
                } 
                
                if(pool.isEmpty()){
                     Log.debug("Cannot spawn boss on level [" + z + "]");
                }else {
                    BossSpawnProperty boss = pool.get();
                    if (boss.isUnique()) uniquesSpawned.add(boss.creature().name());

                    Point spawn = getSpawnPoint(boss, z);
                    Boss newBoss  = (Boss) newCreature(spawn, (Creature) boss.creature().newInstance(), boss);
                    BossAI bossAI = new BossAI(newBoss);
                    modifyStats(newBoss, z);
                    spawnedThisLevel++;

                    int minionCount = MathUtil.randomIntFromString(boss.minionCount(), random);

                    if (minionCount > 0) {
                        List<CreatureSpawnProperty> minions = new ArrayList<CreatureSpawnProperty>();
                        for (String s : boss.minions()) minions.add(Codex.creatures.get(s.toLowerCase()));

                        PackAI pack = new PackAI(10);
                        for (int i = 0; i < minionCount; i++) {
                            Pool<CreatureSpawnProperty> minionPool = new Pool<CreatureSpawnProperty>(random);
                            for (CreatureSpawnProperty c : minions) minionPool.add(c, c.chance()); //TODO: Find why this makes null pointer

                            CreatureSpawnProperty minion = minionPool.get();
                            Point minionSpawn = world.randomEmptyPointInRadius(spawn, 6);
                            Creature packMember = newCreature(minionSpawn, (Creature) minion.creature().newInstance(), minion);
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
                Pool<CreatureSpawnProperty> pool = new Pool<CreatureSpawnProperty>(random);
                for (CreatureSpawnProperty c : canSpawn){
                    if(c instanceof BossSpawnProperty) continue;
                    if(!typesSpawned.contains(c.creature().name()) && typesSpawned.size() >= typesThisLevel) continue;
                    pool.add(c, c.chanceAtDepth(z));
                }
                
                CreatureSpawnProperty toSpawn = pool.get();
                Point spawn = getSpawnPoint(toSpawn, z);
                int packSize = toSpawn.packSize(random);
                typesSpawned.add(toSpawn.creature().name());
                
                if(packSize == 0) {
                    Creature newCreature = newCreature(spawn, (Creature) toSpawn.creature().newInstance(), toSpawn);
                    getAI(toSpawn.creature().aiType(), newCreature);
                    modifyStats(newCreature, z);
                    spawnedThisLevel++;
                }else{
                    PackAI pack = new PackAI(10);
                    for(int i = 0; i < packSize; i++){
                        Point newSpawn = world.randomEmptyPointInRadius(spawn, 4);
                        Creature packMember = newCreature(newSpawn, (Creature) toSpawn.creature().newInstance(), toSpawn);
                        pack.addPackMember(new PackMemberAI(packMember, pack));
                        modifyStats(packMember, z);
                        spawnedThisLevel++;
                    }
                }
            }
            creaturesSpawned += spawnedThisLevel;
            Log.debug("Level [" + z + "] [" + world.level(z).type() +"] " + spawnCounts.get(z) + " Total [" + spawnedThisLevel + "]");
        }
        
        placeNPCs();        
        
        Log.debug("Spawned " + creaturesSpawned + " creatures total.");
        Log.debug("Spawned creatures in " + ((System.nanoTime() - start) / 1000000) + "ms");
        return this;
    }
    
    private WorldBuilder placeNPCs(){
        for(int z = 0; z < depth -1 ; z++){
            Point spawn = new Point();
            spawn.z = z;
            do{
                spawn.x = random.nextInt(width);
                spawn.y = random.nextInt(height);
            }while(world.tile(spawn.x, spawn.y, spawn.z).solid() || MathUtil.distance(spawn.x, spawn.y, world.startPointAt(z).x, world.startPointAt(z).y) < 30);
          
            //Place wizard next to player for convenience
//            Point spawn = world.randomEmptyPointInRadius(world.startPointAt(z), 5);
            
            NPC npc = new Wizard("Wizard McGuffin", "An old man", Sprite.get("wizard"), "npc");
            npc.setStats(100, 100, 0, 0, 1, 1, 1, 1, 16, null);
            new NpcAI(npc);
            world.add(spawn.x, spawn.y, spawn.z, npc);    
        }
        
        return this;
    }
    
    public WorldBuilder spawnItems(){
        double start = System.nanoTime();

        int weaponCount = 2;
        int armorCount = 2;
        
        for(int z = 0; z < depth; z++){
            int spawnedThisLevel = 0;
            int chestSpawnedThisLevel = 0;
            itemSpawnCounts.add(new HashMap<>());
            chestItemSpawnCounts.add(new HashMap<>());
            
            for(int i = 0; i < weaponCount; i++){
                spawnItem(getEquipmentFromSlot(z, true, EquipmentSlot.WEAPON), z);
                spawnedThisLevel++;
            }
            
            for(int i = 0; i < armorCount; i++){
                spawnItem(getEquipmentFromSlot(z, true, EquipmentSlot.ARMOR), z);
                spawnedThisLevel++;
            }
            
            if(random.nextFloat() < 0.1){
                spawnItem(SpellbookGenerator.generate(z, random), z);
                spawnedThisLevel++;
            }
            
            for(int i = 0; i < 3; i++){
                if(random.nextFloat() >= 0.3) break;
                spawnItem(JewelryGenerator.generate((Equippable) getEquipmentFromSlot(z, false, EquipmentSlot.JEWELRY), random), z);
                spawnedThisLevel++;
            }
            
            //TODO: Too many items in chests
//            Log.debug("Chests on level " + z + ": " + world.level(z).chests().size());
            for(Chest chest : world.level(z).chests()){
                Inventory<Item> inventory = chest.inventory();

                if(random.nextFloat() <= 0.5f) {
                    for (int i = 0; i < 2; i++) {
                        Item item = JewelryGenerator.generate((Equippable) getEquipmentFromSlot(z, false, EquipmentSlot.JEWELRY), random);
                        inventory.add(item);

                        if (!chestItemSpawnCounts.get(z).containsKey(item.name())) chestItemSpawnCounts.get(z).put(item.name(), 0);
                        chestItemSpawnCounts.get(z).put(item.name(), chestItemSpawnCounts.get(z).get(item.name()) + 1);
                        chestSpawnedThisLevel++;
                        if (random.nextFloat() >= 0.2f + ((float) z / (float) world.depth()) * 0.1f) break;
                    }
                }
                
                if(random.nextFloat() <= 0.385f) {
                    for (int i = 0; i < 3; i++) {
                        Item item = SpellbookGenerator.generate(z, random);
                        inventory.add(item);
                        if (!chestItemSpawnCounts.get(z).containsKey(item.name())) chestItemSpawnCounts.get(z).put(item.name(), 0);
                        chestItemSpawnCounts.get(z).put(item.name(), chestItemSpawnCounts.get(z).get(item.name()) + 1);
                        chestSpawnedThisLevel++;
                        if (random.nextFloat() >= 0.1f + ((float) z / (float) world.depth()) * 0.1f) break;
                    }
                }
                
                if(random.nextFloat() <= 0.6f) {
                    for (int i = 0; i < 3; i++) {
                        Consumable potion = PotionBuilder.randomPotion(z, random);
                        inventory.add(potion);
                        if (!chestItemSpawnCounts.get(z).containsKey(potion.name())) chestItemSpawnCounts.get(z).put(potion.name(), 0);
                        chestItemSpawnCounts.get(z).put(potion.name(), chestItemSpawnCounts.get(z).get(potion.name()) + 1);
                        chestSpawnedThisLevel++;
                        if (random.nextFloat() >= 0.35f + ((float) z / (float) world.depth()) * 0.1f) break;
                    }
                }
                
                if(random.nextFloat() < 0.85f){
                    inventory.add(new ItemStack("gold", "a stack of gold", Sprite.get("gold"), "WHAT???", (int) (MathUtil.randomIntFromString("20-30", random) * ((z + 1) * 0.4f))));
                    if (!chestItemSpawnCounts.get(z).containsKey("gold")) chestItemSpawnCounts.get(z).put("gold", 0);
                    chestItemSpawnCounts.get(z).put("gold", chestItemSpawnCounts.get(z).get("gold") + 1);
                }
                
                Log.debug("Chest contains " + inventory.count() + " items");
            }
//            Log.debug("Level [" + z + "] [" + world.level(z).type() +"] Floor:  " + itemSpawnCounts.get(z) + " Total [" + spawnedThisLevel + "]");
            if(world.level(z).chests().size() > 0) Log.debug("Level [" + z + "] [" + world.level(z).type() +"] Chests: " + chestItemSpawnCounts.get(z) + " Total [" + chestSpawnedThisLevel + "]");
        }
        
        Log.debug("Placed items in " + ((System.nanoTime() - start) / 1000000) + "ms");
        return this;
    }
    
    private void spawnItem(Item newItem, int z){
        if(newItem == null) return; 
        if(newItem instanceof ItemStack){
            ItemStack stack = (ItemStack) newItem;
            stack.setAmount(MathUtil.randomIntFromString(stack.spawnAmount(), random));
        }
        Point spawn = world.randomEmptyPoint(z);
        world.add(spawn.x, spawn.y, spawn.z, newItem);

        if(!itemSpawnCounts.get(spawn.z).containsKey(newItem.name())) itemSpawnCounts.get(spawn.z).put(newItem.name(), 0);
        itemSpawnCounts.get(spawn.z).put(newItem.name(), itemSpawnCounts.get(spawn.z).get(newItem.name()) + 1);
    }

    private Item getEquipmentFromSlot(int z, boolean useSpawnChance, EquipmentSlot ... slots){
        Pool<ItemSpawnProperty> pool = new Pool<ItemSpawnProperty>(random);
        for (ItemSpawnProperty spawnProperty : Codex.items.values()) {
            if(spawnProperty.chance() <= 0 || spawnProperty.chanceAtDepth(z) <= 0 || !spawnProperty.canSpawnAtDepth(z)) continue;
            if (spawnProperty.entity() instanceof Equippable) {
                Equippable e = (Equippable) spawnProperty.entity();
                for (EquipmentSlot s : slots) {
                    if (e.slot() == s) {
                        pool.add(spawnProperty, useSpawnChance ? spawnProperty.chanceAtDepth(z) : 100);
                        break;
                    }
                }
            }
        }

        if (pool.isEmpty()) {
            Log.warn("No equipment spawned on level " + z);
            return null;
        }
        
        return (Item) pool.get().entity().newInstance();
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
            
            if(world.level(z).clearance(spawn.x, spawn.y) < toSpawn.creature().size()) continue;
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

    private Creature newCreature(Point spawn, Creature creature, CreatureSpawnProperty spawnProperty){
        world.add(spawn.x, spawn.y, spawn.z, creature);
        //modifyStats(creature, spawn.z);
        
        if(!spawnCounts.get(spawn.z).containsKey(creature.name())) spawnCounts.get(spawn.z).put(creature.name(), 0);
        spawnCounts.get(spawn.z).put(creature.name(), spawnCounts.get(spawn.z).get(creature.name()) + 1);
        
        giveEquippables(creature, spawnProperty);
        
        return creature;
    }
    
    private void giveEquippables(Creature creature, CreatureSpawnProperty spawnProperty){
        if(!spawnProperty.hasEquipment()) return;
        List<Equippable> items = spawnProperty.getEquipment(random);
        for(Equippable item : items) {
            Equippable equippable = (Equippable) item.newInstance();
            creature.inventory().add(equippable);
            creature.equip(equippable);
        }
    }
    
    private void modifyStats(Creature creature, int z){
//        creature.modifyMaxHp((z / 4) * 5);
//        creature.modifyMaxMana((z / 4) * 10);
//        creature.modifyStrength(z / 4);
//        creature.modifyDefense(z / 4);
//        creature.modifyAccuracy(z / 4);
//        creature.modifyIntelligence(z / 4);
//        creature.modifyHp(creature.maxHp(), "");
//        creature.modifyMana(creature.maxMana()); 
        int xp = (int) (Math.pow(1.295, z + 3) * 10);
        creature.modifyXp(xp);
        
        int totalLevel = creature.totalLevel() - 4;

//        xp += amount;
//
//        notify(new ColoredString("You %s %d xp.", amount < 0 ? Color.red.getRGB() : Color.green.getRGB()), amount < 0 ? "lose" : "gain", amount);
//
//        while(xp > (int) (Math.pow(level, 1.75) * 25)){
//            level++;
//            doAction(new ColoredString("advance to level %d", Color.GREEN.getRGB()), level);
//            ai.onGainLevel();
//        }
        
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
        else if(ai.equalsIgnoreCase("neutral")) return new NeutralAI(creature);
        else{
            Log.warn("Unknown creature ai [" + ai + "]");
            return new CreatureAI(creature);
        }
    }
    
    public World build(){
        world.init();
        return world;
    }
}
