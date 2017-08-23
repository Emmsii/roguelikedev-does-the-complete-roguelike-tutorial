package com.mac.rltut.game.screen.menu;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.screen.Screen;
import sun.security.krb5.internal.crypto.Des;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 23/08/2017 at 11:02 AM.
 */
public class AboutScreen extends Screen{
    
    private final String SPRITES_URL = "https://forums.tigsource.com/index.php?topic=14166.0";
    
    @Override
    public Screen input(KeyEvent key) {
        if(key.getKeyCode() == KeyEvent.VK_A){
            try {
                openWebpage(new URI(SPRITES_URL));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return this;
        }
        
        return new StartScreen();
    }

    @Override
    public void render(Renderer renderer) {
        int xp = Engine.instance().widthInTiles() / 2;
        int yp = 5;
        
        renderer.writeCenter("A game by Matt Clegg.", xp, yp++);
        yp++;
        
        renderer.writeCenter("Made in 10 weeks during the /r/roguelikedev Dev-Along 2017.", xp, yp++);
        yp+=4;
        
        renderer.writeCenter("Sprites by user Arachne on the TIGForums.", xp, yp++, Colors.GRAY);
        renderer.writeCenter("[a] forums.tigsource.com/index.php?topic=14166.0", xp, yp++, Colors.GRAY);
        renderer.writeCenter("Sprites licensed under CC BY-SA 4.0.", xp, yp++, Colors.GRAY);
        yp++;

        renderer.writeCenter("Libraries", xp, yp++, Colors.GRAY);
        renderer.writeCenter("Kryo, minlog & reflectasm by Esoteric Software. Licenced under BSD 3-clause.", xp, yp++, Colors.GRAY);
        renderer.writeCenter("objenesis by easymock. Licenced under Apache 2.0.", xp, yp++, Colors.GRAY);
    }
    
    private void openWebpage(URI uri){
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        
        if(desktop != null && desktop.isSupported(Desktop.Action.BROWSE)){
            try {
                desktop.browse(uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
