package com.mac.rltut.game.screen.menu;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.engine.util.StringUtil;
import com.mac.rltut.game.Game;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 10:44 AM.
 */
public class LooseScreen extends EndScreen{
    
    public LooseScreen(Game game) {
        super(game);
    }

    @Override
    public void render(Renderer renderer) {
        renderBorderFill(renderer);
        xp = Engine.instance().widthInTiles() / 2;
        yp = (int) (Engine.instance().heightInTiles() * 0.12f);

        renderer.renderSprite(Sprite.get("corpse"), xp, yp - 2);
        
        renderer.writeCenter("You died!", xp, yp++, Colors.RED);
        yp++;

        renderer.writeCenter("RIP " + player.name() + ", who was killed on level " + (player.z + 1) + ".", xp, yp++, Colors.RED);
        yp++;

        renderer.writeCenter(StringUtil.capitalizeFirst(player.causeOfDeath()) + ".", xp, yp++, Colors.darken(Colors.RED, 0.75f));
        yp += 3;
        
        super.render(renderer);
    }
}
