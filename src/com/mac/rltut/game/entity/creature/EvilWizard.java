package com.mac.rltut.game.entity.creature;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 08/08/2017 at 01:34 PM.
 */
public class EvilWizard extends NPC{

    private boolean readyToFight;
    private int talkIndex;

    private String[] talkOptions = {
            "There you are. I'm impressed you made it this far.",
            "I'm afraid I have to kill you now! For it was I who kidnapped the king!",
            "He is no ruler! Look how easily I turned him into a duck!",
            "Prepare to die!"
    };

    protected EvilWizard(){}

    public EvilWizard(String name, String description, Sprite sprite, String aiType) {
        super(name, description, sprite, aiType);
        readyToFight = false;
        talkIndex = 0;
    }

    @Override
    public void onTalk(Creature other) {
        Log.debug("Talking! " + talkIndex);
        if(talkIndex >= talkOptions.length){
            readyToFight = true;
            return;
        }
        say(new ColoredString(talkOptions[talkIndex]));
        talkIndex++;
    }

    public boolean readyToFight(){
        return readyToFight;
    }
}
