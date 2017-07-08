package com.mac.rltut.engine.loader;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.parser.DataObject;
import com.mac.rltut.game.world.tile.ChestTile;
import com.mac.rltut.game.world.tile.Tile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 08/07/2017 at 10:14 AM.
 */
public class TileLoader extends DataLoader{
    
    public TileLoader(InputStream in, String name) throws IOException {
        super(in, name);
    }

    @Override
    public void load() {
        Log.debug("Loading tiles...");
        HashMap<String, TileType> tileTypes = new HashMap<>();
        
        for(DataObject obj : data){
            
            if(obj.type().equalsIgnoreCase("type")) {
                String name = obj.getString("name");
                boolean solid = obj.getBoolean("solid");
                boolean canSee = obj.getBoolean("can_see");
                boolean canFly = obj.getBoolean("can_fly");
                tileTypes.put(name, new TileType(name, solid, canSee, canFly));
            }else if(obj.type().equalsIgnoreCase("tile")){
                int id = obj.getInt("id");
                String name = obj.getString("name");
                Sprite sprite = Sprite.get(obj.getString("sprite"));
                TileType type = tileTypes.get(obj.getString("type"));
                String typeName = obj.hasToken("type_name") ? obj.getString("type_name") : type.name;
                
                Tile newTile = new Tile((byte) id, name, sprite, typeName, type.solid, type.canSee, type.canFly);
                Tile.addTile(newTile);
            }else if(obj.type().equalsIgnoreCase("chest")){
                int id = obj.getInt("id");
                String name = obj.getString("name");
                Sprite closedSprite = Sprite.get(obj.getString("sprite_closed"));
                Sprite openSprite = Sprite.get(obj.getString("sprite_open"));
                TileType type = tileTypes.get(obj.getString("type"));
                String typeName = obj.hasToken("type_name") ? obj.getString("type_name") : type.name;
                        
                ChestTile chestTile = new ChestTile((byte) id, name, closedSprite, openSprite, typeName, type.solid, type.canSee, type.canFly);
                Tile.addTile(chestTile);
            }
        }
        
        Log.debug("Loaded " + Tile.tileNames.size() + " tiles.");
    }
}

class TileType{
    public String name;
    public boolean solid;
    public boolean canSee;
    public boolean canFly;
    
    public TileType(String name, boolean solid, boolean canSee, boolean canFly){
        this.name = name;
        this.solid = solid;
        this.canSee = canSee;
        this.canFly = canFly;
    }
}
