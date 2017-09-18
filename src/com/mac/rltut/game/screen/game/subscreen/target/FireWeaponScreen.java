package com.mac.rltut.game.screen.game.subscreen.target;

import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.engine.util.maths.Line;
import com.mac.rltut.engine.util.maths.Point;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.item.EquipmentSlot;
import com.mac.rltut.game.entity.util.CombatManager;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/07/2017 at 07:46 PM.
 */
public class FireWeaponScreen extends TargetBasedScreen{
       
    public FireWeaponScreen(int x, int y, int w, int h, Player player, int sx, int sy) {
        super(x, y, w, h, player, sx, sy, false);
    }

    @Override
    public void render(Renderer renderer) {
        super.render(renderer);
    }

    @Override
    public boolean isAcceptable(int xa, int ya) {
        if(!player.world().inFov(xa, ya, player.z)) return false;
        for(Point p : new Line(player.x, player.y, xa, ya)) if(!player.world().tile(p.x, p.y, player.z).canSee()) return false;
        return true;
    }

    @Override
    public void selectWorldCoordinate(int xa, int ya, int screenX, int screenY) {
        
        if(player.ammo() == null || player.ammo().amount() == 0){
            player.notify(new ColoredString("You don't enough ammo.", Colors.ORANGE));
            return;
        }

        if(!player.ammo().name().equalsIgnoreCase(player.getEquippedAt(EquipmentSlot.WEAPON).ammoType())){
            player.notify(new ColoredString("You don't have the right ammo equipped for this weapon.", Colors.ORANGE));
            return;
        }
        
        Creature other = player.world().creature(xa, ya, player.z);
        if(other == null) player.notify(new ColoredString("There is no one to fire at.", Colors.ORANGE));
        else{
            new CombatManager(player, other).rangedAttack();
            player.setHasPerformedAction(true);
        }
    }
}
