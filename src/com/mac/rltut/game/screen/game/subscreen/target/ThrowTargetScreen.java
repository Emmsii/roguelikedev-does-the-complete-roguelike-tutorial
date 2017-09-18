package com.mac.rltut.game.screen.game.subscreen.target;

import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.item.Item;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 13/08/2017 at 08:33 AM.
 */
public class ThrowTargetScreen extends TargetBasedScreen {
    
    private Item item;
    
    public ThrowTargetScreen(int x, int y, int w, int h, Player player, int sx, int sy, Item item) {
        super(x, y, w, h, player, sx, sy, true);
        this.item = item;
    }

    @Override
    public boolean isAcceptable(int xa, int ya) {
        if(!player.world().inFov(xa, ya, player.z) || player.world().solid(xa, ya, player.z)) return false;
//        for(Point p : new Line(player.x, player.y, xa, ya)) if(!player.world().tile(p.x, p.y, player.z).canSee()) return false;
        return true;
    }

    @Override
    public void selectWorldCoordinate(int xa, int ya, int screenX, int screenY) {
        player.throwItem(item, xa, ya, player.z);
    }
}
