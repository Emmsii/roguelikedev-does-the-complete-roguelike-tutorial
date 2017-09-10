package com.mac.rltut.game.entity.creature;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.engine.util.maths.Point;
import com.mac.rltut.game.entity.creature.stats.Stats;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 10:37 AM.
 */
public class Player extends Creature{
    
    private Stats stats;
    private boolean hasWon;

    protected Player() {}
    
    public Player(String name, Sprite sprite) {
        super(name, "its you", sprite, "player");
        this.stats = new Stats();
    }
    
    public void tryTalk(){
        for(Point p : new Point(x, y, z).neighboursAll()){
            Creature other = world.creature(p.x, p.y, p.z);
            if(other instanceof NPC){
                NPC npc = (NPC) other;
                if(npc.name().equalsIgnoreCase("duck")) continue;
                npc.onTalk(this);
                return;
            }
        }
        
        notify(new ColoredString("There is no one there", Colors.RED));
    }

    @Override
    public void addKill(String name) {
        stats.addKill(name);
    }

    @Override
    public void incrementStat(String key, int amount) {
        stats.incrementValue(key, amount);
    }

    public Stats stats(){
        return stats;
    }

    public void setHasWon(boolean hasWon){
        this.hasWon = hasWon;
    }

    public boolean hasWon(){
        return hasWon;
    }

}
