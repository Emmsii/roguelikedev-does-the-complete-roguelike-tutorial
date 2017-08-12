package com.mac.rltut.game.world.builders;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.util.Pool;
import com.mac.rltut.engine.util.maths.MathUtil;
import com.mac.rltut.engine.util.maths.Point;
import com.mac.rltut.game.world.Level;
import com.mac.rltut.game.world.objects.Chest;
import com.mac.rltut.game.world.objects.MapObject;
import com.mac.rltut.game.world.tile.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 30/06/2017 at 09:42 AM.
 */
public abstract class LevelBuilder {

    protected Random random;
    private final String type;
    protected final int width, height;
    private final int minLevel, maxLevel;
    private final int chance;
    private final float zMultiplier;
    private final float creatureSpawnMultiplier;
    private final int visibilityModifier;
    
    private Properties properties;
    private List<DecalTile> decalTiles;

    private HashMap<String, List<Tile>> tileTypes;
    private HashMap<String, HashMap<Tile, Integer>> tileTypeChances;
    
    private byte[][] tiles;
    private MapObject[][] mapObjects;
    protected List<Chest> chests;
    
    private byte[][] clearanceMap;
    
    private Level level;   
    
    private Point start;
    
    public LevelBuilder(String type, int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, float creatureSpawnMultiplier, int visibilityModifier, Random random) {
        this.type = type;
        this.width = width;
        this.height = height;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.chance = chance;
        this.zMultiplier = zMultiplier;
        this.creatureSpawnMultiplier = creatureSpawnMultiplier;
        this.visibilityModifier = visibilityModifier;
        this.random = random;
        
        this.properties = new Properties();
        this.decalTiles = new ArrayList<>();
        this.tileTypes = new HashMap<>();
        this.tileTypeChances = new HashMap<>();
        
        setTileTypes();
        setProperties();
    }
    
    public void init(int z){
        Log.trace("Init " + type + " level at " + z + "...");
        this.tiles = new byte[width][height];
        this.mapObjects = new MapObject[width][height];
        this.chests = new ArrayList<Chest>();
        this.clearanceMap = new byte[width][height];
        this.level = new Level(type, width, height, z, visibilityModifier);
        for(int y = 0; y < height; y++) for(int x = 0; x < width; x++) setTile(x, y, Tile.getTile("empty"));
        this.random.setSeed(random.nextLong());
    }
    
    protected abstract void setTileTypes();
    protected abstract void setProperties();
    public abstract LevelBuilder generate(int z);

    protected void addDecalTiles(){
        Log.trace("Adding objects tiles...");
        Pool<Tile> pool = new Pool<Tile>(random);
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
    
    protected void addStart(int z){
        Log.trace("Adding start...");
        int x, y;
        do{
            x = random.nextInt(width);
            y = random.nextInt(height);
        }while(solid(x, y));
        level.setStart(new Point(x, y, z));
    }
    
    public void generateClearanceMap(){
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++){

                if(tile(x, y).solid()){
                    clearanceMap[x][y] = -1;
                    continue;
                }

                byte value = 0;
                int size = 1;
                boolean hitSolid = false;
                while(!hitSolid) {
                    for (int ya = 0; ya < size; ya++){
                        int yp = y + ya;
                        for(int xa = 0; xa < size; xa++){
                            int xp = x + xa;
                            if(tile(xp, yp).solid()){
                                hitSolid = true;
                                break;
                            }
                        }
                    }

                    if(hitSolid) break;
                    size++;
                    value++;
                }

                clearanceMap[x][y] = value;
            }
        }
    }    
    
    public void buildShop(){
        int xp, yp;
        int size = 3;
        
        boolean valid = false;
        do{
            xp = random.nextInt(width);
            yp = random.nextInt(height);
            
            if(solid(xp, yp)) continue;
            
            boolean blocked = false;
            for(int ya = yp; ya < yp + size + 2; ya++){
                for(int xa = xp; xa < xp + size + 2; xa++){
                    if(solid(xa, ya)){
                        blocked = true;
                        break;
                    }
                }
            }
           if(!blocked) break;
        }while(!valid);

        Tile tile = getRandomTile("wall");
        if(tile != null) placeRoom(new Rectangle(xp, yp, size + 2, size + 2), 1f);

    }

    protected void placeRoom(Rectangle room, float wallFrequency){
        Tile wall = getRandomTile("wall");

        int xp = room.x;
        int yp = room.y;
        int w = room.width;
        int h = room.height;

        for(int y = 0; y < h; y++){
            int ya = y + yp;
            for(int x = 0; x < w; x++){
                int xa = x + xp;

                if(x == 0 || y == 0 || x == w - 1 || y == h - 1){
                    if(random.nextFloat() <= wallFrequency) setTile(xa, ya, wall);
                }
            }
        }
    }
    
    protected void addTileType(Tile tile, int chance){
        String type = tile.type();
        if(!tileTypes.containsKey(type)) tileTypes.put(type, new ArrayList<Tile>());
        tileTypes.get(type).add(tile);
        if(!tileTypeChances.containsKey(type)) tileTypeChances.put(type, new HashMap<Tile, Integer>());
        tileTypeChances.get(type).put(tile, chance);
    }
        
    protected void addDecalTile(Tile decal, int chance, Tile ... canPlaceOn){
        if(canPlaceOn == null || canPlaceOn.length < 1){
            Log.error("MapObject tile must have a tile if can place on.");
            return;
        }
        decalTiles.add(new DecalTile(decal, chance, canPlaceOn));
    }
    
    protected void setProperty(String key, String value){
        properties.setProperty(key, value);
    }
        
    protected int getPropertyInt(String key){
        if(!properties.containsKey(key)) Log.warn("Properties does not contain key [" + key + "] for level type " + type + ".");
        String value = properties.getProperty(key.trim());
        if(value.split("-").length == 0) return Integer.parseInt(value);
        else return MathUtil.randomIntFromString(value, random);
    }

    protected float getPropertyFloat(String key){
        if(!properties.containsKey(key)) Log.warn("Properties does not contain key [" + key + "] for level type " + type + ".");
        String value = properties.getProperty(key.trim());
        if(value.split("-").length == 0) return Float.parseFloat(value);
        else return MathUtil.randomFloatFromString(value, random);
    }
    
    protected boolean getPropertyBoolean(String key){
        return Boolean.parseBoolean(properties.getProperty(key.trim()));
    }
    
    protected boolean isValid(int minimumEmpty){
        Log.trace("Checking is valid...");
        int empty = 0;
        for(int y = 0; y < height; y++) for (int x = 0; x < width; x++) if(!solid(x, y)) empty++;
        return empty > minimumEmpty;
    }
    
    protected Tile getRandomTile(String type){
        if(!tileTypes.containsKey(type)){
            Log.warn("Unknown tile type [" + type + "] in level type " + this.type + ".");
            return null;
        }
        List<Tile> tiles = tileTypes.get(type);
        HashMap<Tile, Integer> chances = tileTypeChances.get(type);
        Pool<Tile> pool = new Pool<Tile>(random);
        for(Tile t : tiles) pool.add(t, chances.get(t));
        return pool.get();
    }

    protected Tile tile(int x, int y){
        if(!level.inBounds(x, y)) return Tile.getTile("empty");
        return Tile.getTile(tiles[x][y]);
    }
    
    protected boolean solid(int x, int y){
        if(!level.inBounds(x, y)) return true;
        return tile(x, y).solid();
    }
    
    public byte[][] tiles(){
        return tiles;
    }
    
    public Point start(){
        return start;
    }
    
    protected void setTiles(byte[][] tiles){
        this.tiles = tiles;
    }
    
    protected void setTile(int x, int y, Tile tile){
        if(!level.inBounds(x, y)) return;
        tiles[x][y] = tile.id;
    }
    
    protected void addMapObject(int x, int y, MapObject obj){
        if(!level.inBounds(x, y)) return;
        mapObjects[x][y] = obj;
        if(obj instanceof Chest) chests.add((Chest) obj);
    }
    
    public Level build(){
        generateClearanceMap();
        
        level.setTiles(tiles);
        level.setMapObjects(mapObjects);
        level.setChests(chests);
        level.setClearanceMapMap(clearanceMap);
        return level;
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
    
    public float creatureSpawnModifier(){
        return creatureSpawnMultiplier;
    }
    
    protected boolean inBounds(int x, int y){
        return level.inBounds(x, y);
    }
    
    public void saveImage(int z){
        File file = new File("images/");
        if(!file.exists()) file.mkdirs();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int id = tile(x, y).id;
                if(id == Tile.getTile("empty").id) image.setRGB(x, y, Color.BLACK.getRGB());
                else if(id == Tile.getTile("wallTopRed").id) image.setRGB(x, y, 0xBD5757);
                else if(id == Tile.getTile("wallTopBlue").id) image.setRGB(x, y, 0xBD5757);
                else if(id == Tile.getTile("treeConifer").id) image.setRGB(x, y, 0x3C7A50);
                else if(id == Tile.getTile("treeDeciduous").id) image.setRGB(x, y, 0x5DAD37);
//                else if(id == Tile.getTile("grassGreen").id) image.setRGB(x, y, 0xABE617);
                else if(id == Tile.getTile("waterBlue").id) image.setRGB(x, y, 0x56A6E8);
                else if(id == Tile.getTile("waterDirty").id) image.setRGB(x, y, 0xAB8652);
                else if(id == Tile.getTile("waterBonesDirty1").id) image.setRGB(x, y, 0xD0D1A5);
                else if(id == Tile.getTile("waterBonesDirty2").id) image.setRGB(x, y, 0xD0D1A5);
                else if(id == Tile.getTile("waterBonesFoul1").id) image.setRGB(x, y, 0xD0D1A5);
                else if(id == Tile.getTile("waterBonesFoul2").id) image.setRGB(x, y, 0xD0D1A5);
                else if(id == Tile.getTile("waterFoul").id) image.setRGB(x, y, 0x228A6C);
                else if(id == Tile.getTile("waterLilypad").id) image.setRGB(x, y, 0x36BAB1);
                else if(id == Tile.getTile("mushroom").id) image.setRGB(x, y, 0xBF4545);
//                else if(id == Tile.getTile("chestGold").id) image.setRGB(x, y, 0xFF8000);
//                else if(id == Tile.getTile("chestSilver").id) image.setRGB(x, y, 0xFF8000);
                
                if(mapObjects[x][y] instanceof Chest) image.setRGB(x, y, 0xFF8000);
            }
        }

        try {
            ImageIO.write(image, "png", new File("images/level_" + z + "_" + type + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
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
}


