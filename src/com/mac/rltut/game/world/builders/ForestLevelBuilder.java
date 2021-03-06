package com.mac.rltut.game.world.builders;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.util.maths.MathUtil;
import com.mac.rltut.engine.util.maths.Point;
import com.mac.rltut.game.world.objects.Chest;
import com.mac.rltut.game.world.objects.Shrine;
import com.mac.rltut.game.world.tile.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 02/07/2017 at 05:24 PM.
 */
public class ForestLevelBuilder extends LevelBuilder{
    
    private int[][] regions;
    private boolean[][] liquid;
    private List<List<Point>> liquidRegions;
    private List<Rectangle> ruins;
    private Rectangle shrine;
    private int nextRegion;
    
    public ForestLevelBuilder(String type, int minLevel, int maxLevel, int chance, float zMultiplier, float creatureSpawnMultiplier, int visibilityModifier) {
        super(type, minLevel, maxLevel, chance, zMultiplier, creatureSpawnMultiplier, visibilityModifier);
    }

    @Override
    public void init(int width, int height, int z) {
        super.init(width, height, z);
        this.regions = new int[width][height];
        this.liquid = new boolean[width][height];
        this.liquidRegions = new ArrayList<List<Point>>();
        this.ruins = new ArrayList<Rectangle>();
        this.nextRegion = 1;
    }
    
    @Override
    public LevelBuilder generate(int width, int height, int z, Random random) {
        this.random = random;
        Log.trace("Generating " + type() + " level at " + z + "...");
        int borderThickness = getPropertyInt("border_thickness");
        float treeRandomFrequency = getPropertyFloat("tree_random");
        int treeSmooth = getPropertyInt("tree_smooth");
        float liquidRandomFrequency = getPropertyFloat("liquid_random");
        int liquidSmooth = getPropertyInt("liquid_smooth");
        int minRegionSize = getPropertyInt("min_region_size");
        int roomCount = getPropertyInt("room_count");
        int roomSizeMin = getPropertyInt("room_size_min");
        int roomSizeMax = getPropertyInt("room_size_max");
        float chestFrequency = getPropertyFloat("chest_frequency");
                
        do{
            init(width, height, z);
            addBorder("tree", borderThickness);
            
            placeShrine(z);
            
            randomizeTrees(treeRandomFrequency);
            smoothTrees(treeSmooth);

            randomizeLiquid(borderThickness, liquidRandomFrequency);
            smoothLiquid(liquidSmooth, getRandomTile("water"));

            findLakes();
            setLiquids();

            buildShrine();

            addDecalTiles();

            addRuins(roomCount, roomSizeMin, roomSizeMax);

            cleanupRegions(minRegionSize);

            addChests(chestFrequency);
            
            addGrass();
            
        }while(!isValid((int) ((width * height) * 0.3)));
        
        addStart(z);
        Log.trace("Level generated!");
        return this;
    }
    
    private void placeShrine(int z){
        int size = 15;
        int x = 0, y = 0;
        boolean valid = false;
        int tries = 0;

        while(!valid){
            if(tries++ >= 1000) break;
            x = random.nextInt(width - 10) + 10;
            y = random.nextInt(height - 10) + 10;

            boolean solid = false;
            for(int ya = y; ya < y + size; ya++){
                for(int xa = x; xa < x + size; xa++){
                    if(!inBounds(xa, ya)){
                        solid = true;
                        break;
                    }
                    if(tile(xa, ya).solid()){
                        solid = true;
                        break;
                    }
                }
            }

            if(!solid) valid = true;
        }

        if(!valid){
            Log.debug("No Shrine on level " + z  + " " + type());
            return;
        }

        this.shrine = new Rectangle(x, y, size, size);
    }
    
    //TODO: 
    private void buildShrine(){
        if(shrine == null) return;
        int b = (int) (shrine.width * 0.3636f);
        
        for(int ya = shrine.y; ya < shrine.y + shrine.height; ya++) {
            for (int xa = shrine.x; xa < shrine.x + shrine.width; xa++) {
                
                int d = MathUtil.distance(shrine.x + (shrine.width / 2), shrine.y + (shrine.height / 2), xa, ya);
                
                float a = (float) d / (float) b;
                if(random.nextFloat() >= a && d <= b){
                    int r = random.nextInt(4);
                    switch (r){
                        case 0: setTile(xa, ya, Tile.getTile("cobblestone1")); break;
                        case 1: setTile(xa, ya, Tile.getTile("cobblestone2")); break;
                        case 2: setTile(xa, ya, Tile.getTile("cobblestone3")); break;
                        case 3: setTile(xa, ya, Tile.getTile("cobblestone4")); break;
                    }
                }
                
                if(xa == shrine.x + (shrine.width / 2) && ya == shrine.y + (shrine.height / 2)){
                    Shrine shrine = new Shrine("Shrine", "A magical slab of rock", Tile.getTile("shrine_charged"), Tile.getTile("shrine_empty"), random);
                    addMapObject(xa, ya, shrine);
                }
                
                if(d == b - random.nextInt(2)){
//                if(xa == x || ya == y || xa == x + size - 1 || ya == y + size - 1){
                    if(random.nextFloat() < 0.4){
//                        Tile t = random.nextBoolean() ? random.nextBoolean() ? Tile.getTile("pillar_short") : Tile.getTile("pillar_medium") : Tile.getTile("pillar_tall");
//                        setTile(xa, ya, t);
                        int r = random.nextInt(3);
                        switch (r){
                            case 0: setTile(xa, ya, Tile.getTile("pillar_short")); break;
                            case 1: setTile(xa, ya, Tile.getTile("pillar_medium")); break;
                            case 2: setTile(xa, ya, Tile.getTile("pillar_tall")); break;
                        }
                    }
                }
            }
        }
        
        
    }
    
    private void addBorder(String type, int thickness){
        Log.trace("Adding border...");
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if(x < thickness || y < thickness || x > width - (1 + thickness) || y > height - (1 + thickness)){
                    setTile(x, y, getRandomTile(type));
                }
            }
        }
    }
    
    private void randomizeTrees(float frequency){
        Log.trace("Randomizing trees...");
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if(solid(x, y)) continue;
                if(shrine != null && shrine.contains(x, y)) setTile(x, y, Tile.getTile("empty"));
                if(random.nextFloat() <= frequency) setTile(x, y, getRandomTile("tree"));
                else setTile(x, y, Tile.getTile("empty"));
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
                            if(tile(x + ox, y + oy).id == 0) empty++;
                            else solid++;
                        }
                    }

                    tiles2[x][y] = empty >= solid ? 0 : getRandomTile("tree").id;
                }
            }
            setTiles(tiles2);
        }
    }
    
    private void randomizeLiquid(int borderThickness, float frequency){
        Log.trace("Randomizing liquid...");
        liquid = new boolean[width][height];
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(x < borderThickness || y < borderThickness || x >= width - borderThickness || y >= height - borderThickness) liquid[x][y] = false;
                else if(shrine != null && shrine.contains(x, y)) liquid[x][y] = false;
                else liquid[x][y] = random.nextFloat() <= frequency;
            }
        }
    }
    
    private void smoothLiquid(int amount, Tile tile){
        Log.trace("Smoothing liquid...");
        boolean[][] liquid2 = new boolean[width][height];

        for(int i = 0; i < amount; i++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int empty = 0;
                    int lake = 0;

                    for(int ox = -1; ox <= 1; ox++) {
                        for (int oy = -1; oy <= 1; oy++) {
                            if(x + ox < 0 || x + ox >= width || y + oy < 0 || y + oy >= height) continue;
                            if(liquid[x + ox][y + oy]) empty++;
                            else lake++;
                        }
                    }
                    liquid2[x][y] = empty >= lake;
                }
            }
            liquid = liquid2;
        }
    }
    
    private void findLakes(){
        Log.trace("Finding lakes...");
        regions = new int[width][height];
        nextRegion = 1;
        
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(liquid[x][y] && regions[x][y] == 0){
                    fillLakeRegion(nextRegion++, x, y);
                }
            }
        }
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
                if(regions[n.x][n.y] > 0 || !liquid[n.x][n.y]) continue;
                size++;
                regions[n.x][n.y] = id;
                newLakeRegion.add(n);
                open.add(n);
            }
        }
        liquidRegions.add(newLakeRegion);
        return size;
    }
    
    private void setLiquids(){
        Log.trace("Setting liquids...");
        for(List<Point> lake : liquidRegions){
            Tile liquid = getRandomTile("water");
            for(Point p : lake) setTile(p.x, p.y, liquid);
        }
    }
    
    private void addRuins(int count, int ruinSizeMin, int ruinSizeMax){
        Log.trace("Adding ruins...");
        while(ruins.size() < count){
            int xp = MathUtil.range(ruinSizeMax + 3, width - ruinSizeMin - 3, random);
            int yp = MathUtil.range(ruinSizeMax + 3, height - ruinSizeMin - 3, random);
            int w = MathUtil.range(ruinSizeMin, ruinSizeMax, random);
            int h = MathUtil.range(ruinSizeMin, ruinSizeMax, random);
            
            if(!validRuinPosition(xp, yp, w, h)) continue;
            
            Rectangle newRuin = new Rectangle(xp, yp, w, h);
            
            if(ruinOverlaps(newRuin)) continue;

            ruins.add(newRuin);
            placeRoom(newRuin, 0.4f);
        }
        
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if(!tile(x, y).isType("wall") || tile(x, y).id == Tile.getTile("wallSide").id) continue;
                if(!tile(x, y + 1).isType("wall")) setTile(x, y + 1, Tile.getTile("wallSide"));
            }
        }
    }
    
    private boolean validRuinPosition(int xp, int yp, int w, int h){
        int solid = 0, total = 0;
        for(int y = 0; y < h; y++) {
            int ya = y + yp;
            for(int x = 0; x < w; x++) {
                int xa = x + xp;
                total++;
                if(solid(xa, ya)) solid++;
            }
        }
        float percentSolid = (float) solid / (float) total;
        return percentSolid <= 0.3f;
    }
    
    private boolean ruinOverlaps(Rectangle newRuin){
        if(ruins.contains(newRuin)) return true;
        for(Rectangle r : ruins) if(r.intersects(newRuin)) return true;
        if(shrine != null && shrine.intersects(newRuin)) return true;
        return false;
    }
        
    private void addChests(float chestFrequency){
        Log.trace("Adding chests...");
        for(Rectangle r : ruins){
            if(random.nextFloat() <= chestFrequency){
                Tile chestTile = (Tile) getRandomTile("chest");
                int x, y;
                int tries = 0;
                do {
                    x = MathUtil.range(r.x + 1, r.x + r.width - 2, random);
                    y = MathUtil.range(r.y + 1, r.y + r.height - 2, random);
                }while(solid(x, y) && tries++ < (r.width * r.height));
                if(!solid(x, y)) addChest(x, y, chestTile);
            }
        }
        
        List<Chest> toRemove = new ArrayList<Chest>();
        for(Chest chest : chests){
            boolean blocked = true;
            for(Point p : new Point(chest.x, chest.y, chest.z).neighboursAll()){
                if(tile(p.x, p.y).solid()){
                    blocked = true;
                    break;
                }
            }
            
            if(blocked){
                setTile(chest.x, chest.y, getRandomTile("tree"));
                toRemove.remove(chest);
            }
        }
        chests.removeAll(toRemove);
    }
    
    private void addChest(int x, int y, Tile tile){
        addMapObject(x, y, new Chest(tile));
    }
    
    private void addGrass(){
        int emptyId = Tile.getTile("empty").id;
        int floorId = Tile.getTile("floor").id;
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int id = tile(x, y).id;
                if(id == emptyId || id == floorId){
                    Tile t = getRandomTile("grass");
                    setTile(x, y, t);
                }
            }
        }
    }

    private void cleanupRegions(int minSize){
        Log.trace("Cleaning up regions...");
        regions = new int[width][height];
        nextRegion = 1;

        int largestRegion = 0;
        int largestRegionId = 0;

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if(!solid(x, y) && regions[x][y] == 0){
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
                    setTile(x, y, getRandomTile("tree"));
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
                if(regions[n.x][n.y] > 0 || solid(n.x, n.y)) continue;
                size++;
                regions[n.x][n.y] = id;
                open.add(n);
            }
        }

        return size;
    }
    
    private void removeRegion(int id){
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(regions[x][y] == id){
                    regions[x][y] = 0;
                    setTile(x, y, getRandomTile("tree"));
                }
            }
        }
    }
 
}
