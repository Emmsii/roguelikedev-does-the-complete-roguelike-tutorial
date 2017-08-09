package com.mac.rltut.game.entity.creature;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;

import java.awt.event.WindowAdapter;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 08/08/2017 at 01:34 PM.
 */
public class Wizard extends NPC{
    
    private int playerSightTime;
    
    private boolean readyToTeleport;
    
    protected Wizard(){}
    
    public Wizard(String name, String description, Sprite sprite, String aiType) {
        super(name, description, sprite, aiType);
        playerSightTime = 0;
        readyToTeleport = false;
    }

    @Override
    public void update() {
        super.update();
        
        if(playerSightTime == 0 && canSee(world.player())){
            playerSightTime = 15;
            say(new ColoredString(getAttentionGrabber()));
        }
        
        if(playerSightTime > 0) playerSightTime--;
    }

    @Override
    public void onTalk(Creature other) {
        if(!readyToTeleport) {
            say(new ColoredString("Good, you made it."));
            if(other.z == 0) say(new ColoredString("I can transport you to the next part of the forest."));
            say(new ColoredString("Talk to me again when you are ready to move on."));
            readyToTeleport = true;
        }else{
            say(new ColoredString("Here we go!"));
            world.moveDown(other);
            other.notify(new ColoredString("You feel tingly as you are magically transported to another part of the forest."));
            say(new ColoredString("Good luck!"));
        }
        
    }
    
    private String getAttentionGrabber(){
        String[] options = { "Over here!", "Quick, come here!", "Ahh there you are." };
        return options[(int) (Math.random() * options.length)];
    }
}
