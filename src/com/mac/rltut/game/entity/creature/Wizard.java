package com.mac.rltut.game.entity.creature;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.world.World;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 08/08/2017 at 01:34 PM.
 */
public class Wizard extends NPC{
    
    private int playerSightTime;
    
    private boolean readyToTeleport;
    private boolean readyToEnd;
    
    protected Wizard(){}
    
    public Wizard(String name, String description, Sprite sprite, String aiType) {
        super(name, description, sprite, aiType);
        playerSightTime = 0;
        readyToTeleport = false;
        readyToEnd = false;
    }

    @Override
    public void update() {
        super.update();
        
        if(playerSightTime == 0 && canSee(world.player())){
            playerSightTime = 20;
            say(new ColoredString(getAttentionGrabber()));
        }
        
        if(playerSightTime > 0) playerSightTime--;
    }

    @Override
    public void onTalk(Creature other) {
        
        if(z == world.depth()){
            if(!readyToEnd){
                say(new ColoredString("Congratulations! You made it!"));
                say(new ColoredString("I should probably ask you to kill the boss on this level, but I can't tell if you have or not!"));
                readyToEnd = true;
            }else{
                other.setHasWon(true);
            }
        }else {
            //TODO: This will be hard coded for now, eventually I'll come up with a better system for dialogue.
            if (!readyToTeleport) {
                say(new ColoredString("Good, you made it."));
                if (other.z == 0) {
                    say(new ColoredString("I can transport you to the next part of the forest. Though I can only take you one way, forwards not back."));
                    say(new ColoredString("Talk to me again when you are ready to move on."));
                } else say(new ColoredString(getReadyQuestion()));

                other.setHasPerformedAction(true);
                readyToTeleport = true;
            } else {
                if (Math.random() < 0.1) {
                    String hint = getLevelHint(other.world(), other.z + 1);
                    if (hint != null) say(new ColoredString(hint));
                }
                say(new ColoredString(getTransportSaying()));
                world.moveDown(other);
                other.notify(new ColoredString("You feel tingly as you are magically transported to another part of the forest.", Colors.BLUE));
            }
        }
    }
    
    private String getLevelHint(World world, int level){
        if(level > world.depth()) return null;
        switch (world.level(level).type().toLowerCase()){
            case "default": return null;
            case "dense":
            case "dark": return "It can get awful dark in these parts.";
            case "lake":
            case "swamp": return "Don't fall in the water!";
            case "ruined": return "Did you know that people used to live here?";
            case "sparse": return "I wonder where all the trees have gone.";
            default: return null;
        }
    }

    private String getReadyQuestion(){
        String[] options = { "Are you ready to go?", "Ready to proceed?" };
        return options[(int) (Math.random() * options.length)];
    }

    private String getTransportSaying(){
        String[] options = { "And here we go!", "3, 2, 1... Magic!", "Poof!", "Engage!" };
        return options[(int) (Math.random() * options.length)];
    }
    
    private String getAttentionGrabber(){
        String[] options = { "Over here!", "Quick, come here!", "Ahh there you are." };
        return options[(int) (Math.random() * options.length)];
    }
}
