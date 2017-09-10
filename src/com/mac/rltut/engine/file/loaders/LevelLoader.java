package com.mac.rltut.engine.file.loaders;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.parser.DataObject;
import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.world.builders.ForestLevelBuilder;
import com.mac.rltut.game.world.builders.LevelBuilder;
import com.mac.rltut.game.world.tile.Tile;

import java.io.IOException;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 03/09/2017 at 01:32 PM.
 */
public class LevelLoader extends DataLoader{
    
    public LevelLoader(String fileName) throws IOException {
        super(fileName);
    }

    @Override
    public void load() {
        Log.debug("Loading levels...");
        loadDefaults();
                
        for(DataObject obj : data){
            String name = obj.getString("name");
            int minLevel = obj.getInt("min_level");
            int maxLevel = parseLevel(obj.getString("max_level"));
            int chance = obj.getInt("chance");
            float zMultiplier = obj.getFloat("z_multiplier");
            float creatureSpawnMultiplier = obj.getFloat("creature_spawn_multiplier");
            int visibility = obj.getInt("visibility");
            
            LevelBuilder levelBuilder = null;
            if(obj.isType("forest") || obj.isType("defaults")){
                levelBuilder = new ForestLevelBuilder(name, minLevel, maxLevel, chance, zMultiplier, creatureSpawnMultiplier, visibility);
                
                parseTileTypes(obj.getString("tile_types"), levelBuilder);
                parseDecalTiles(obj.getString("decal_tiles"), levelBuilder);

                levelBuilder.setProperty("tree_random", obj.hasToken("tree_random") ? obj.getString("tree_random") : defaults.getString("tree_random"));
                levelBuilder.setProperty("tree_smooth", obj.hasToken("tree_smooth") ? obj.getString("tree_smooth") : defaults.getString("tree_smooth"));
                levelBuilder.setProperty("liquid_random", obj.hasToken("liquid_random") ? obj.getString("liquid_random") : defaults.getString("liquid_random"));
                levelBuilder.setProperty("liquid_smooth", obj.hasToken("liquid_smooth") ? obj.getString("liquid_smooth") : defaults.getString("liquid_smooth"));
                levelBuilder.setProperty("border_thickness", obj.hasToken("border_thickness") ? obj.getString("border_thickness") : defaults.getString("border_thickness"));
                levelBuilder.setProperty("min_region_size", obj.hasToken("min_region_size") ? obj.getString("min_region_size") : defaults.getString("min_region_size"));
                levelBuilder.setProperty("room_count", obj.hasToken("room_count") ? obj.getString("room_count") : defaults.getString("room_count"));
                levelBuilder.setProperty("room_size_min", obj.hasToken("room_size_min") ? obj.getString("room_size_min") : defaults.getString("room_size_min"));
                levelBuilder.setProperty("room_size_max", obj.hasToken("room_size_max") ? obj.getString("room_size_max") : defaults.getString("room_size_max"));
                levelBuilder.setProperty("chest_frequency", obj.hasToken("chest_frequency") ? obj.getString("chest_frequency") : defaults.getString("chest_frequency"));
            }else{
                Log.warn("Unknown level type [" + obj.type() + "]");
                continue;
            }
            
            Codex.levelBuilders.put(name, levelBuilder);
        }

        Log.debug("Loaded " + Codex.levelBuilders.size() + " levels.");
    }
    
    private void parseTileTypes(String input, LevelBuilder builder){
        String[] split = input.trim().split(",");
        
        for(String s : split){
            String[] parts = s.trim().split(":");
            Tile tile = Tile.getTile(parts[0].trim());
            int chance = parts.length > 1 ? Integer.parseInt(parts[1].trim()) : 100;
            builder.addTileType(tile, chance);
        }
    }

    private void parseDecalTiles(String input, LevelBuilder builder){
        String[] split = input.trim().split(",");
        
        for(String s : split){
            String[] parts = s.trim().split(":");
            Tile decal = Tile.getTile(parts[0].trim());
            int chance = Integer.parseInt(parts[1].trim());
            String[] placeOnNames = parts[2].trim().split(";");
            Tile[] canPlaceOn = new Tile[placeOnNames.length];
            
            for(int i = 0; i < placeOnNames.length; i++) canPlaceOn[i] = Tile.getTile(placeOnNames[i].trim());
            
//            if(parts.length == 3){
//                
//                canPlaceOn = new Tile[placeOnNames.length + 1];
//                for(int i = 0; i < placeOnNames.length; i++) canPlaceOn[i] = Tile.getTile(placeOnNames[i].trim());
//                canPlaceOn[placeOnNames.length] = Tile.getTile("empty");
//            }else canPlaceOn[0] = Tile.getTile("empty");
            builder.addDecalTile(decal, chance, canPlaceOn);
        }
    }
    
    private int parseLevel(String input){
        if(input.trim().equalsIgnoreCase("depth")) return -1;
        else return Integer.parseInt(input);
    }
}
