package com.mac.rltut.game.screen.menu;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.Game;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 10:44 AM.
 */
public class WinScreen extends EndScreen{

    public WinScreen(Game game) {
        super(game);
    }

    @Override
    public void render(Renderer renderer) {
        renderBorderFill(renderer);
        xp = Engine.instance().widthInTiles() / 2;
        yp = (int) (Engine.instance().heightInTiles() * 0.12f);

        renderer.renderSprite(Sprite.get("duck"), xp, yp - 2);
        
        renderer.writeCenter("You won!", xp, yp++);
        yp++;

        renderer.writeCenter("You saved the King!", xp, yp++);
        yp+=3;
        
        super.render(renderer);
    }
}
