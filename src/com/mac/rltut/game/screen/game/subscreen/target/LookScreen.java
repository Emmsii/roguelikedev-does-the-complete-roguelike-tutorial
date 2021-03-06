package com.mac.rltut.game.screen.game.subscreen.target;

import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.StringUtil;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.NPC;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.world.objects.MapObject;

import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 22/07/2017 at 11:10 AM.
 */
public class LookScreen extends TargetBasedScreen{
    
    private StringBuilder caption;
        
    public LookScreen(int x, int y, int w, int h, Player player, int sx, int sy) {
        super(x, y, w, h, player, sx, sy, false);
        this.caption = new StringBuilder();
        enterWorldCoordinate(player.x, player.y, sx, sy);
    }

    @Override
    public void render(Renderer renderer) {
        super.render(renderer);
        if(caption.length() > width - 6){
            List<String> lines = StringUtil.lineWrap(caption.toString(), width - 6, false);
            renderBox(2, height - 4 - lines.size(), width - 4, 2 + lines.size(), true, renderer);
            for(int i = 0; i < lines.size(); i++) renderer.writeCenter(lines.get(i), this.x + (width / 2), this.y + (height - 3 - lines.size() + i));
        }else {
            renderBox(2, height - 5, width - 4, 3, true, renderer);
            renderer.writeCenter(caption.toString(), this.x + (width / 2), this.y + (height - 4));
        }
    }

    @Override
    public void enterWorldCoordinate(int xa, int ya, int screenX, int screenY) {
        caption = new StringBuilder();
        
        Creature creature = player.world().creature(xa, ya, player.z);
        MapObject mapObject = player.world().mapObject(xa, ya, player.z);
        Item item = player.world().item(xa, ya, player.z);
        boolean blood = player.world().level(player.z).blood(xa, ya);
        
        if(creature != null){
            if(creature.isPlayer()) caption.append("You are standing on ");
            else if(creature instanceof NPC) caption.append("The " + creature.name() + " standing on ");
            else caption.append(StringUtil.articleName(creature) + " standing on ");
        }

        if(mapObject != null) caption.append(StringUtil.articleName(mapObject) + " on ");
        if(item != null) caption.append(StringUtil.articleName(item) + " lying on ");
        if(blood) caption.append("a pool of blood, splattered on ");
                
        caption.append(player.world().tile(xa, ya, player.z).description().toLowerCase());
        
        caption.trimToSize();
        caption.append(".");
        caption = new StringBuilder(caption.substring(0, 1).toUpperCase().concat(caption.substring(1, caption.length())));
    }

    @Override
    public boolean isAcceptable(int xa, int ya) {
        return player.world().inFov(xa, ya, player.z);
    }
    
}
