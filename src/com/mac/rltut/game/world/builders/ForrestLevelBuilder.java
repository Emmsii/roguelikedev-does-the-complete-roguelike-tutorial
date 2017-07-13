package com.mac.rltut.game.world.builders;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.util.MathUtil;
import com.mac.rltut.engine.util.Point;
import com.mac.rltut.engine.util.Pool;
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
public class ForrestLevelBuilder extends LevelBuilder{
    
    private int[][] regions;
    private boolean[][] liquid;
    private List<List<Point>> liquidRegions;
    private List<Rectangle> ruins;
    private int nextRegion;
    
    public ForrestLevelBuilder(String type, int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, float creatureSpawnMultiplier, Random random) {
        super(type, width, height, minLevel, maxLevel, chance, zMultiplier, creatureSpawnMultiplier, random);
    }

    @Override
    public void init(int z) {
        super.init(z);
        this.regions = new int[width][height];
        this.liquid = new boolean[width][height];
        this.liquidRegions = new ArrayList<List<Point>>();
        this.ruins = new ArrayList<Rectangle>();
        this.nextRegion = 1;
    }

    @Override
    protected void setTileTypes() {
        addTileType(Tile.getTile("treeConifer"), 50);
        addTileType(Tile.getTile("treeDeciduous"), 50);
        addTileType(Tile.getTile("waterBlue"), 100);
        addTileType(Tile.getTile("wallTopRed"), 100);        
        addTileType(Tile.getTile("chestSilver"), 100);

        addDecalTile(Tile.getTile("waterLilypad"), 4, Tile.getTile("waterBlue"));
        addDecalTile(Tile.getTile("grassGreen"), 50, Tile.getTile("empty"), Tile.getTile("floor"));
        addDecalTile(Tile.getTile("treeConifer"), 22, Tile.getTile("empty"));
        addDecalTile(Tile.getTile("treeDeciduous"), 22, Tile.getTile("empty"));
    }

    @Override
    protected void setProperties() {
        setProperty("tree_random_frequency", "0.438-0.46");
        setProperty("tree_smooth", "5-6");
        setProperty("liquid_random_frequency", "0.35-0.425");
        setProperty("liquid_smooth", "5-8");
        setProperty("border_thickness", "3");
        setProperty("min_region_size", "80");
        setProperty("room_count", "3-4");
        setProperty("room_size_min", "5-6");
        setProperty("room_size_max", "8-9");
        setProperty("chest_frequency", "0.4");
    }
    
    @Override
    public LevelBuilder generate(int z) {
        Log.trace("Generating " + type() + " level at " + z + "...");
        int borderThickness = getPropertyInt("border_thickness");
        float treeRandomFrequency = getPropertyFloat("tree_random_frequency");
        int treeSmooth = getPropertyInt("tree_smooth");
        float liquidRandomFrequency = getPropertyFloat("liquid_random_frequency");
        int liquidSmooth = getPropertyInt("liquid_smooth");
        int minRegionSize = getPropertyInt("min_region_size");
        int roomCount = getPropertyInt("room_count");
        int roomSizeMin = getPropertyInt("room_size_min");
        int roomSizeMax = getPropertyInt("room_size_max");
        float chestFrequency = getPropertyFloat("chest_frequency");
                
        do{
            init(z);
            addBorder("tree", borderThickness);

            randomizeTrees(treeRandomFrequency);
            smoothTrees(treeSmooth);
            
            randomizeLiquid(borderThickness, liquidRandomFrequency);
            smoothLiquid(liquidSmooth, getRandomTile("water"));

            findLakes();
            setLiquids();

            addDecalTiles();

            addRuins(roomCount, roomSizeMin, roomSizeMax);

            cleanupRegions(minRegionSize);

            addChests(chestFrequency);
            
        }while(!isValid((int) ((width * height) * 0.3)));
        
        addStart(z);
        Log.trace("Level generated!");
        return this;
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
                if(tile(x, y).solid()) continue;
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
            placeRuin(newRuin);
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
                if(tile(xa, ya).solid()) solid++;
            }
        }
        float percentSolid = (float) solid / (float) total;
        return percentSolid <= 0.3f;
    }
    
    private boolean ruinOverlaps(Rectangle newRuin){
        if(ruins.contains(newRuin)) return true;
        for(Rectangle r : ruins) if(r.intersects(newRuin)) return true;
        return false;
    }
    
    private void placeRuin(Rectangle ruin){
        Tile wall = getRandomTile("wall");

        int xp = ruin.x;
        int yp = ruin.y;
        int w = ruin.width;
        int h = ruin.height;
        
        for(int y = 0; y < h; y++){
            int ya = y + yp;
            for(int x = 0; x < w; x++){
                int xa = x + xp;
                
                if(x == 0 || y == 0 || x == w - 1 || y == h - 1){
                    if(random.nextFloat() < 0.4f) setTile(xa, ya, wall);
                }
            }
        }
    }
    
    private void addChests(float chestFrequency){
        Log.trace("Adding chests...");
        for(Rectangle r : ruins){
            if(random.nextFloat() <= chestFrequency){
                Tile chestTile = getRandomTile("chest");
                int x, y;
                int tries = 0;
                do {
                    x = MathUtil.range(r.x + 1, r.x + r.width, random);
                    y = MathUtil.range(r.y + 1, r.y + r.height, random);
                }while(tile(x, y).solid() && tries++ < (r.width * r.height));
                setTile(x, y, chestTile);
                //TODO: separate list of chests
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
                if(regions[n.x][n.y] > 0 || tile(n.x, n.y).solid()) continue;
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
