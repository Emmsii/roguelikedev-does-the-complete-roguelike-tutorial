package com.mac.rltut.game.map.levels;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.util.MathUtil;
import com.mac.rltut.engine.util.Point;
import com.mac.rltut.engine.util.Pool;
import com.mac.rltut.game.map.tile.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 30/06/2017 at 09:42 AM.
 */
public abstract class LevelBuilder {
    
    private final Random random;
    private final String type;
    private final int width, height;
    private final int minLevel, maxLevel;
    private final int chance;
    private final float zMultiplier;
    
    private HashMap<String, Object> properties;
    private List<DecalTile> decalTiles;
    
    private List<Tile> trees;
    private List<Tile> liquids;
    private HashMap<Tile, Integer> treeChances;
    private HashMap<Tile, Integer> liquidChances;
    
    private byte[][] tiles;

    private List<List<Point>> lakeRegions;
    private boolean[][] lakes;
    private int[][] regions;
    private int nextRegion;

    private final int BORDER_THICKNESS_MIN = 2;
    private final int BORDER_THICKNESS_MAX = 5;
    private final float TREE_FREQUENCY_MIN = 0.3f;
    private final float TREE_FREQUENCY_MAX = 0.5f;
    private final int TREE_SMOOTH_MIN = 1;
    private final int TREE_SMOOTH_MAX = 12;
    private final float LIQUID_FREQUENCY_MIN = 0.15f;
    private final float LIQUID_FREQUENCY_MAX = 0.475f;
    private final int LIQUID_SMOOTH_MIN = 1;
    private final int LIQUID_SMOOTH_MAX = 12;
    private final int REGION_SIZE_MIN = 40;
    private final int REGION_SIZE_MAX = 80;
    
    public LevelBuilder(String type, int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, Random random){
        this.type = type;
        this.width = width;
        this.height = height;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.chance = chance;
        this.zMultiplier = zMultiplier;
        this.random = random;
        this.properties = new HashMap<String, Object>();
        this.decalTiles = new ArrayList<DecalTile>();
        this.trees = new ArrayList<Tile>();
        this.liquids = new ArrayList<Tile>();
        this.treeChances = new HashMap<Tile, Integer>();
        this.liquidChances = new HashMap<Tile, Integer>();
        
        setDefaults();
    }
    
    private void init(){
        this.tiles = new byte[width][height];
        this.regions = new int[width][height];
        this.lakes = new boolean[width][height];
        this.lakeRegions = new ArrayList<List<Point>>();
        this.nextRegion = 1;
    } 
    
    private void setDefaults(){
        addTreeType(Tile.treeConifer, 50);
        addTreeType(Tile.treeDeciduous, 50);
        addLiquidType(Tile.waterBlue, 100);

        addDecalTile(Tile.waterLilypad, 4, Tile.waterBlue);
        addDecalTile(Tile.grassGreen, 50, Tile.empty);
        addDecalTile(Tile.treeConifer, 22, Tile.empty);
        addDecalTile(Tile.treeDeciduous, 22, Tile.empty);

        setProperty("tree_random_frequency", "0.445-0.475");
        setProperty("tree_smooth", "6-7");
        setProperty("liquid_random_frequency", "0.35-0.425");
        setProperty("liquid_smooth", "5-8");
        setProperty("border_thickness", 3);
        setProperty("min_region_size", 80);
    }
        
    public void generate(int z){
        Log.trace("Generating new level " + z + "...");
        double start = System.nanoTime();
        
        int borderThickness = (int) getProperty("border_thickness");
        float treeRandomFrequency = MathUtil.randomFloatFromString((String) getProperty("tree_random_frequency"), random);
        int treeSmooth = MathUtil.randomIntFromString((String) getProperty("tree_smooth"), random);
        float liquidRandomFrequency = MathUtil.randomFloatFromString((String) getProperty("liquid_random_frequency"), random);
        int liquidSmooth = MathUtil.randomIntFromString((String) getProperty("liquid_smooth"), random);
        int minRegionSize = (int) getProperty("min_region_size");
        
        if(borderThickness < BORDER_THICKNESS_MIN) borderThickness = BORDER_THICKNESS_MIN;
        if(borderThickness > BORDER_THICKNESS_MAX) borderThickness = BORDER_THICKNESS_MAX;
        if(treeRandomFrequency < TREE_FREQUENCY_MIN) treeRandomFrequency = TREE_FREQUENCY_MIN;
        if(treeRandomFrequency > TREE_FREQUENCY_MAX) treeRandomFrequency = TREE_FREQUENCY_MAX;
        if(treeSmooth < TREE_SMOOTH_MIN) treeSmooth = TREE_SMOOTH_MIN;
        if(treeSmooth > TREE_SMOOTH_MAX) treeSmooth = TREE_SMOOTH_MAX;
        
        if(liquidRandomFrequency < LIQUID_FREQUENCY_MIN) liquidRandomFrequency = LIQUID_FREQUENCY_MIN;
        if(liquidRandomFrequency > LIQUID_FREQUENCY_MAX) liquidRandomFrequency = LIQUID_FREQUENCY_MAX;
        if(liquidSmooth < LIQUID_SMOOTH_MIN) liquidSmooth = LIQUID_SMOOTH_MIN;
        if(liquidSmooth > LIQUID_SMOOTH_MAX) liquidSmooth = LIQUID_SMOOTH_MAX;
        
        if(minRegionSize < REGION_SIZE_MIN) treeSmooth = REGION_SIZE_MIN;
        if(minRegionSize > REGION_SIZE_MAX) minRegionSize = REGION_SIZE_MAX;

        Log.trace("Border Thickness: " + borderThickness);
        Log.trace("Tree Frequency: " + treeRandomFrequency);
        Log.trace("Tree Smooth: " + treeSmooth);
        Log.trace("Liquid Frequency: " + liquidRandomFrequency);
        Log.trace("Liquid Smooth: " + liquidSmooth);
        Log.trace("Min Region Size: " + minRegionSize);
        
        do {
            init();
            addBorder(borderThickness);
            randomizeTrees(treeRandomFrequency);
            smoothTrees(treeSmooth);
            randomizeLiquid(borderThickness, liquidRandomFrequency);
            smoothLiquid(liquidSmooth, getRandomTile(liquids, liquidChances));

            findLakes();
            setLiquids();

            addDecalTiles();

            createRegions(minRegionSize);
        }while(!isValid((int) ((width * height) * 0.3)));


        
        Log.trace("Generated in " + ((System.nanoTime() - start) / 1000000) + "ms");
        //TODO: Check if valid
    }
    
    private boolean isValid(int minimumEmpty){
        int empty = 0;
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++){
                if(!tile(x, y).solid()) empty++;
            }
        }
        return empty >= minimumEmpty;
    }
    
    private void addBorder(int thickness){
        Log.trace("Adding border...");
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if(x < thickness || y < thickness || x > width - (1 + thickness) || y > height - (1 + thickness)) setTile(x, y, getRandomTile(trees, treeChances));
            }
        }
    }
    
    private void randomizeTrees(float frequency){
        Log.trace("Adding random trees...");
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if(tile(x, y).solid()) continue;
                if(random.nextFloat() <= frequency) setTile(x, y, getRandomTile(trees, treeChances));
                else setTile(x, y, Tile.empty);
            }
        }
    }
    
    private void smoothTrees(int amount){
        Log.trace("Smoothing trees...");
        byte[][] tiles2 = new byte[width][height];
        for(int i = 0; i < amount; i++){
            for(int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int empty = 0;
                    int solid = 0;

                    for(int ox = -1; ox <= 1; ox++) {
                        for (int oy = -1; oy <= 1; oy++) {
                            if(tile(x + ox, y + oy).id == Tile.empty.id) empty++;
                            else solid++;
                        }
                    }

                    tiles2[x][y] = empty >= solid ? Tile.empty.id : getRandomTile(trees, treeChances).id;
                    
                }
            }
            tiles = tiles2;
        }
    }
    
    private void randomizeLiquid(int borderThickness, float waterFrequency){
        Log.trace("Adding random liquid...");
        lakes = new boolean[width][height];
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(x < 3 || y < 3 || x >= width - 3 || y >= height - 3) lakes[x][y] = false;
                else lakes[x][y] = random.nextFloat() <= waterFrequency;
            }
        }
    }
    
    private void smoothLiquid(int amount, Tile water){
        Log.trace("Smoothing trees...");
        boolean[][] lakes2 = new boolean[width][height];

        for(int i = 0; i < amount; i++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int empty = 0;
                    int lake = 0;

                    for(int ox = -1; ox <= 1; ox++) {
                        for (int oy = -1; oy <= 1; oy++) {
                            if(x + ox < 0 || x + ox >= width || y + oy < 0 || y + oy >= height) continue;
                            if(lakes[x + ox][y + oy]) empty++;
                            else lake++;
                        }
                    }
                    lakes2[x][y] = empty >= lake;
                }
            }
            lakes = lakes2;
        }
    }
    
    private void findLakes(){
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(lakes[x][y] && regions[x][y] == 0){
                    fillLakeRegion(nextRegion++, x, y);
                }
            }
        }

        regions = new int[width][height];
        nextRegion = 1;
    }
    
    private void setLiquids(){
        for(List<Point> lake : lakeRegions){
           Tile liquid = getRandomTile(liquids, liquidChances);
           for(Point p : lake) setTile(p.x, p.y, liquid);
        }
    }
    
    private void createRegions(int minSize){
        Log.trace("Creating and filling regions...");
        regions = new int[width][height];

        int largestRegion = 0;
        int largestRegionId = 0;

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if(!tile(x, y).solid() && regions[x][y] == 0){
                    int size = fillRegion(nextRegion++, x, y);
                    if(size < minSize){
                        removeRegion(nextRegion - 1);  
                    } else if(size > largestRegion){
                        largestRegion = size;
                        largestRegionId = nextRegion - 1;
                    }
                }
            }
        }

        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(regions[x][y] != largestRegionId && regions[x][y] > 0){
                    regions[x][y] = 0;
                    setTile(x, y, getRandomTile(trees, treeChances));
                }
            }
        }
    }
    
    private int fillRegion(int id, int x, int y){
        int size = 1;
        List<Point> open = new ArrayList<Point>();
        open.add(new Point(x, y, 0));
        regions[x][y] = id;

        while(!open.isEmpty()){
            Point p = open.remove(0);

            for(Point n : p.neighboursCardinal()){
                if(!inBounds(n.x, n.y)) continue;
                if(regions[n.x][n.y] > 0 || tile(n.x, n.y).solid()) continue;
                size++;
                regions[n.x][n.y] = id;
                open.add(n);
            }
        }
        
        return size;
    }
        
    private int fillLakeRegion(int id, int x, int y){
        int size = 1;
        List<Point> open = new ArrayList<Point>();
        List<Point> newLakeRegion = new ArrayList<Point>();
        open.add(new Point(x, y, 0));
        regions[x][y] = id;
        
        while(!open.isEmpty()){
            Point p = open.remove(0);
            
            for(Point n : p.neighboursCardinal()){
                if(!inBounds(n.x, n.y)) continue;
                if(regions[n.x][n.y] > 0 || !lakes[n.x][n.y]) continue;
                size++;
                regions[n.x][n.y] = id;
                newLakeRegion.add(n);
                open.add(n);
            }
        }
        lakeRegions.add(newLakeRegion);
        return size;
    }
    
    private void removeRegion(int id){
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(regions[x][y] == id){
                    regions[x][y] = 0;
                    setTile(x, y, getRandomTile(trees, treeChances));
                }
            }
        }
    }
    
    private void addDecalTiles(){
        Log.trace("Adding decal tiles...");
        Pool<Tile> pool = new Pool<Tile>();
        Tile currentTile = null;
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                currentTile = tile(x, y);

                for(DecalTile t : decalTiles){
                    if(!t.canPlaceOn(currentTile)) continue;
                    pool.add(t.tile, t.chance);
                    for(Tile placeOn : t.tilesCanPlaceOn) if(!pool.contains(placeOn)) pool.add(placeOn, Pool.DEFAULT_WEIGHT);
                }
                
                if(pool.isEmpty()) continue;
                setTile(x, y, pool.get());
                pool.clear();
            }
        }
    }

    
    public void addTreeType(Tile tree, int chance){
        treeChances.put(tree, chance);
        if(!trees.contains(tree)) trees.add(tree);
    }
    
    public void addLiquidType(Tile liquid, int chance){
        liquidChances.put(liquid, chance);
        if(!liquids.contains(liquid)) liquids.add(liquid);
    }
    
    public void addDecalTile(Tile tile, int chance, Tile ... tilesCanPlaceOn){
        if(tilesCanPlaceOn == null || tilesCanPlaceOn.length < 1){
            Log.error("Tile to addDecalTiles must have tiles to place on.");
            return;
        }
        decalTiles.add(new DecalTile(tile, chance, tilesCanPlaceOn));
    }
    
    public void clearDecalTiles(){
        decalTiles.clear();
    }
    
    public void setProperty(String key, Object prop){
        properties.put(key, prop);
    }
    
    public Object getProperty(String key){
        if(!properties.containsKey(key)) Log.warn("Properties does not exist [" + key + "].");
        return properties.get(key);
    }
    
    private boolean tileIsIn(Tile tile, List<Tile> tiles){
        for(Tile t : tiles) if(tile.id == t.id) return true;
        return false;
    }
    
    public String type(){
        return type;
    }
    
    public int minLevel(){
        return minLevel;
    }
    
    public int maxLevel(){
        return maxLevel;
    }
    
    public int chance(){
        return chance;
    }
    
    public float zMultiplier(){
        return zMultiplier;
    }

    public byte[][] tiles(){
        return tiles;
    }

    //Util methods
    
    private Tile getRandomTile(List<Tile> tiles, HashMap<Tile, Integer> chances){
        Pool<Tile> pool = new Pool<Tile>();
        for(Tile t : tiles) pool.add(t, chances.get(t));
        return pool.get();
    }
    
    private Tile tile(int x, int y){
        if(!inBounds(x, y)) return Tile.empty;
        return Tile.getTile(tiles[x][y]);
    }
    
    private void setTile(int x, int y, Tile tile){
        if(!inBounds(x, y)) return;
        tiles[x][y] = tile.id;
    }    
    
    private boolean inBounds(int x, int y){
        return x >= 0 && y >= 0 && x < width && y < height;
    }
    
    public void saveImage(int z){
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int id = tile(x, y).id;
                if(id == Tile.empty.id) image.setRGB(x, y, Color.BLACK.getRGB());
                else if(id == Tile.treeConifer.id) image.setRGB(x, y, 0x3C7A50);
                else if(id == Tile.treeDeciduous.id) image.setRGB(x, y, 0x5DAD37);
                else if(id == Tile.grassGreen.id) image.setRGB(x, y, 0xABE617);
                else if(id == Tile.waterBlue.id) image.setRGB(x, y, 0x56A6E8);
                else if(id == Tile.waterDirty.id) image.setRGB(x, y, 0xAB8652);
                else if(id == Tile.waterBonesDirty1.id) image.setRGB(x, y, 0xD0D1A5);
                else if(id == Tile.waterBonesDirty2.id) image.setRGB(x, y, 0xD0D1A5);
                else if(id == Tile.waterBonesFoul1.id) image.setRGB(x, y, 0xD0D1A5);
                else if(id == Tile.waterBonesFoul2.id) image.setRGB(x, y, 0xD0D1A5);
                else if(id == Tile.waterFoul.id) image.setRGB(x, y, 0x228A6C);
                else if(id == Tile.waterLilypad.id) image.setRGB(x, y, 0x36BAB1);
                else if(id == Tile.mushroom.id) image.setRGB(x, y, 0xBF4545);
            }
        }

        try {
            ImageIO.write(image, "png", new File("images/level_" + z + "_" + type + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class DecalTile {

    public Tile tile;
    public int chance;
    public Tile[] tilesCanPlaceOn;
            
    public DecalTile(Tile tile, int chance, Tile[] tilesCanPlaceOn){
        this.tile = tile;
        this.chance = chance;
        this.tilesCanPlaceOn = tilesCanPlaceOn;
    }
    
    public boolean canPlaceOn(Tile tile){
        for(Tile t : tilesCanPlaceOn) if(t.id == tile.id) return true;
        return false;
    }
}
