package com.mac.rltut.game.map;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.util.MathUtil;
import com.mac.rltut.engine.util.Point;
import com.mac.rltut.game.map.tile.Tile;

import java.util.ArrayList;
import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 12:31 PM.
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
      
        for(int z = 0; z < depth; z++){
            
            Tile[] trees = { Tile.treeDeciduous, Tile.treeConifer };
            Tile[] grass = { Tile.grassGreen };
            Tile[] water = { Tile.waterBlue };
            
            randomizeTiles(z, MathUtil.range(0.4f, 0.47f, random), trees);
            addBorder(z, 3, trees);
            smooth(z, 3, trees);
            addWater(z, 10, MathUtil.range(0.3f, 0.43f, random), water);
            
            float grassFrequency = MathUtil.range(0.25f, 0.35f, random);
            float treeFrequency= MathUtil.range(0.075f, 0.125f, random);
            float waterDecalFrequency= MathUtil.range(0.1f, 0.3f, random);
            
            populate(z, grassFrequency, treeFrequency, waterDecalFrequency, trees, grass);
            
            cleanupRegions(z, 60, trees);
        }
        
        Log.debug("Generated map in " + ((System.nanoTime() - start) / 1000000) + "ms");
        return this;
    }
    
    private void randomizeTiles(int z, float frequency, Tile[] tiles){
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map.setTile(x, y, z, random.nextFloat() < frequency ? getRandomTile(tiles) : Tile.empty);
            }
        }
    }
    
    private void addBorder(int z, int thickness, Tile[] tiles){
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(x < thickness || y < thickness || x > width - (1 + thickness) || y > height - (1 + thickness)) map.setTile(x, y, z, getRandomTile(tiles));
            }
        }
    }
    
    private void smooth(int z, int smoothAmount, Tile[] tiles){
        byte[][] tiles2 = new byte[width][height];
        for(int i = 0; i < smoothAmount; i++){
            for(int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int floors = 0;
                    int trees = 0;

                    for(int ox = -1; ox < 2; ox++){
                        for(int oy = -1; oy < 2; oy++){
                            if(map.tile(x + ox, y + oy, z).id == Tile.empty.id) floors++;
                            else trees++;
                        }
                    }
                    tiles2[x][y] = floors >= trees ? Tile.empty.id : getRandomTile(tiles).id;
                }
            }
            map.setTiles(z, tiles2);
        }
    }
    
    private void addWater(int z, int smoothAmount, float frequency, Tile[] tiles){
        lakes = new boolean[width][height];
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(x < 2 || y < 2 || x >= width - 2 || y >= height - 2) lakes[x][y] = false;
                else lakes[x][y] = random.nextFloat() < frequency;
            }
        }

        boolean[][] lakes2 = new boolean[width][height];
        for(int i = 0; i < smoothAmount; i++){
            for(int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int empty = 0;
                    int water = 0;

                    for(int ox = -1; ox < 2; ox++){
                        for(int oy = -1; oy < 2; oy++){
                            if(x + ox < 0 || x + ox >= width || y + oy < 0 || y + oy >= height) continue;
                            if(lakes[x + ox][y + oy]) empty++;
                            else water++;
                        }
                    }
                    lakes2[x][y] = empty >= water;
                }
            }
            lakes = lakes2;
        }

        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(lakes[x][y]){
                    map.setTile(x, y, z, getRandomTile(tiles));
                    for(Point p : new Point(x, y, z).neighboursAll()){
                        int id = map.tile(p.x, p.y, p.z).id;
                        if(id == Tile.treeDeciduous.id || id == Tile.treeConifer.id) map.setTile(p.x, p.y, p.z, Tile.empty);
                    }
                }
            }
        }
    }
    
    private void populate(int z, float grassFrequency, float treeFrequency, float waterDecalFrequency, Tile[] trees, Tile[] grass){
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(map.tile(x, y, z).solid()) continue;
                if(random.nextFloat() <= grassFrequency) map.setTile(x, y, z, getRandomTile(grass));
                if(random.nextFloat() <= treeFrequency) map.setTile(x, y, z, getRandomTile(trees));
                if(random.nextFloat() <= waterDecalFrequency && isWaterTile(map.tile(x, y, z))){
                    setWaterDecal(x, y, z, map.tile(x, y, z));
                }
            }
        }
    }
    
    private boolean isWaterTile(Tile tile){
        if(tile.id == Tile.waterBlue.id) return true;
        if(tile.id == Tile.waterBrown.id) return true;
        if(tile.id == Tile.waterPurple.id) return true;
        return false;
    }
    
    private void setWaterDecal(int x, int y, int z, Tile tile){
        Log.debug("Setting");
        if(tile.id == Tile.waterBlue.id) map.setTile(x, y, z, Tile.waterLilypad);
        else if(tile.id == Tile.waterBrown.id) map.setTile(x, y, z, random.nextBoolean() ? Tile.waterBonesBrown1 : Tile.waterBonesBrown2);
        else if(tile.id == Tile.waterPurple.id) map.setTile(x, y, z, random.nextBoolean() ? Tile.waterBonesPurple1 : Tile.waterBonesPurple2);
    }
    
    private void cleanupRegions(int z, int minRegionSize, Tile[] trees){
        nextRegion = 1;
        regions = new int[width][height];

        regions = new int[width][height];

        int largestRegion = 0;
        int largestRegionId = 0;

        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(!map.tile(x, y, z).solid() && regions[x][y] == 0){
                    int size = fillRegion(nextRegion++, x, y, z);
                    if(size > largestRegion){
                        largestRegion = size;
                        largestRegionId = nextRegion - 1;
                    }
                    if(size < minRegionSize) removeRegion(nextRegion - 1, z, trees);
                }
            }
        }
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(regions[x][y] != largestRegionId && regions[x][y] > 0){
                    regions[x][y] = 0;
                    map.setTile(x, y, z, getRandomTile(trees));
                }
            }
        }
    }
    
    private int fillRegion(int id, int x, int y, int z){
        int size = 1;
        ArrayList<Point> open = new ArrayList<Point>();
        open.add(new Point(x, y, z));
        regions[x][y] = id;

        while(!open.isEmpty()){
            Point p = open.remove(0);

            for(Point n : p.neighboursCardinal()){
                if(!map.inBounds(n.x, n.y, n.z)) continue;
                if(regions[n.x][n.y] > 0 || map.tile(n.x, n.y, n.z).solid()) continue;
                size++;
                regions[n.x][n.y] = id;
                open.add(n);
            }
        }
        return size;
    }
    
    private void removeRegion(int id, int z, Tile[] trees){
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(regions[x][y] == id){
                    regions[x][y] = 0;
                    map.setTile(x, y, z, getRandomTile(trees));
                }
            }
        }
    }
    
    public Map build(){
        return map;
    }
    
    private Tile getRandomTile(Tile[] tiles){
        if(tiles == null || tiles.length == 0) throw new IllegalArgumentException("Cannot get random tile from empty array");
        return tiles[random.nextInt(tiles.length)];
    }
}
