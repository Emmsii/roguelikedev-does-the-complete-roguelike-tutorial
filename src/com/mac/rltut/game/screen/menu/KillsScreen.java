package com.mac.rltut.game.screen.menu;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.FileHandler;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.engine.util.StringUtil;
import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.util.CreatureSpawnProperty;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 10:44 AM.
 */
public class KillsScreen extends Screen{
    
    private Player player;
    private Screen lastScreen;
    
    public KillsScreen(Player player, Screen lastScreen){
        this.player = player;
        this.lastScreen = lastScreen;
    }
    
    @Override
    public Screen input(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) return lastScreen;
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorderFill(renderer);
        int xp = 2;
        int yp = 4;

        List<Creature> creaturesKilled = new ArrayList<Creature>();

        for(String kill : player.stats().kills().keySet()) creaturesKilled.add(Codex.creatures.get(kill.toLowerCase()).creature());

        Collections.sort(creaturesKilled, new Comparator<Creature>() {
            @Override
            public int compare(Creature o1, Creature o2) {
                if(o1.size() > o2.size()) return 1;
                else if(o1.size() < o2.size()) return -1;
                else return 0;
            }
        });
        
        for(Creature c : creaturesKilled){
            renderer.renderSprite(c.sprite(), xp + (c.size() == 1 ? 1 : 0), yp);
            renderer.write(StringUtil.capitalizeEachWord(c.name()), xp + 3, yp);
            renderer.write("x" + player.stats().kills().get(c.name()), xp + 3, yp + 1);
            
            if(c.size() == 1) yp += 3;
            else yp += 3;

            if(yp >= height - 3){
                yp = 4;
                xp += 20;
            }
        }
        
        renderer.writeCenter("Press [ESCAPE] to return.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 4, Colors.GRAY);
    }
}
