package com.mac.rltut.game.entity.creature;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.engine.util.maths.Point;
import com.mac.rltut.game.entity.creature.stats.PlayerStats;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 10:37 AM.
 */
public class Player extends Creature{
    
    private PlayerStats stats;

    protected Player() {}
    
    public Player(String name, Sprite sprite) {
        super(name, "its you", sprite, "player");
        this.stats = new PlayerStats(this);
    }
    
    public void tryTalk(){
        for(Point p : new Point(x, y, z).neighboursAll()){
            Creature other = world.creature(p.x, p.y, p.z);
            if(other instanceof NPC){
                NPC npc = (NPC) other;
                npc.onTalk(this);
                return;
            }
        }
        
        notify(new ColoredString("There is no one there", Colors.RED));
    }
    
    public PlayerStats stats(){
        return stats;
    }
    
}
