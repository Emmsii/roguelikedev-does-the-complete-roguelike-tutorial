package com.mac.rltut.engine.loader;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.parser.DataObject;
import com.mac.rltut.game.effects.Effect;
import com.mac.rltut.game.entity.creature.Creature;

import java.io.IOException;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/07/2017 at 10:31 AM.
 */
public class EffectsLoader extends DataLoader{
    
    public EffectsLoader(String fileName) throws IOException {
        super(fileName);
    }

    @Override
    public void load() {
        Log.debug("Loading effects...");
        for(DataObject obj : data){
            if(obj.isType("effect")){
                String name = obj.getString("name");
                int duration = obj.hasToken("duration") ? obj.getInt("duration") : 0;
                String causeOfDeath = obj.hasToken("death_cause") ? obj.getString("death_cause") : "unknown";
                
                Effect effect = new Effect(duration){

                    @Override
                    public void start(Creature creature) {
                        if(obj.hasToken("heal")) creature.modifyHp(obj.getInt("heal"), causeOfDeath);
                    }

                    @Override
                    public void stop(Creature creature) {
                        
                    }

                    @Override
                    public void update(Creature creature) {
                        
                    }
                };
                
                
                
            }else{
                Log.error("Unknown object type [" + obj.type() + "]");
                continue;
            }
        }
        
        Log.debug("Loaded x effects.");
    }
}
