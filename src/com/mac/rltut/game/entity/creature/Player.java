package com.mac.rltut.game.entity.creature;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.stats.PlayerStats;
import com.mac.rltut.game.entity.item.ItemBuilder;
import com.mac.rltut.game.screen.Screen;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 10:37 AM.
 */
public class Player extends Creature{
    
    private PlayerStats stats;
    private Screen decalScreen;
    
    public Player(String name, Sprite sprite) {
        super(name, sprite, "player");
        this.stats = new PlayerStats(this);
        
        for(int i = 0; i < 30; i++) inventory().add(ItemBuilder.newDagger(0));
    }
    
    public PlayerStats stats(){
        return stats;
    }
    
    public void setDecalScreen(Screen decalScreen){
        this.decalScreen = decalScreen;
    }
    
    public Screen decalScreen(){
        return decalScreen;
    }
}
