package com.mac.rltut.game.screen.menu;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.FileHandler;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.engine.util.StringUtil;
import com.mac.rltut.game.Game;
import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.creature.stats.PlayerStats;
import com.mac.rltut.game.entity.util.CreatureSpawnProperty;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 10:44 AM.
 */
public class LooseScreen extends Screen{
    
    private Game game;
    private Player player;
    
    public LooseScreen(Game game){
        this.game = game;
        this.player = game.player();
        FileHandler.deleteGameSave();
    }
    
    @Override
    public Screen input(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) return new StartScreen();
        else if(e.getKeyCode() == KeyEvent.VK_K) return new KillsScreen(player, this);
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorderFill(renderer);
        PlayerStats stats = player.stats();

        int xp = Engine.instance().widthInTiles() / 2;
        int yp = (int) (Engine.instance().heightInTiles() * 0.12f);
                
        renderer.writeCenter("You died!", xp, yp++, Colors.RED);
        renderer.writeCenter("RIP " + player.name() + " who was killed on level " + (player.z + 1) + ".", xp, yp++, Colors.RED);
        yp++;
        
        renderer.writeCenter(StringUtil.capitalizeFirst(player.causeOfDeath()), xp, yp++, Colors.darken(Colors.RED, 0.75f));
        yp += 3;
        
        int days = game.world().dayNightController().day();
        renderer.writeCenter("You survived for " + days + " day" + (days > 1 ? "s" : "") + " (" + game.world().dayNightController().tick + " turns).", xp, yp++);
        yp++;
        
        Date startDate = new Date(game.sessionTimer().start());
        Date endDate = new Date(game.sessionTimer().end());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd MMM yyyy");
        List<String> lines = StringUtil.lineWrap("You started on " + dateFormat.format(startDate) + " and ended on " + dateFormat.format(endDate) + ", lasting for " + game.sessionTimer().prettyString() + ".", this.width - 4, false);
        for(String s : lines) renderer.writeCenter(s, xp, yp++);
        yp++;
        
        renderer.writeCenter("You dealt " + stats.damageDealt() + " damage while taking " + stats.damageReceived() + ".", xp, yp++);
        renderer.writeCenter("During combat you had " + stats.hits() + " hits, " + stats.blocks() + " blocks and " + stats.misses() + " misses", xp, yp++);
        yp++;
        
        renderer.writeCenter("Press [k] to see your kills.", xp, yp++, Colors.GRAY);
        yp++;
        
        renderer.writeCenter("You traveled " + stats.tilesTraveled() + " tiles exploring " + String.format("%.2f", game.world().totalExploredPercent()) + "% of the forest.", xp, yp++);
        
        
        renderer.writeCenter("Press [ESCAPE] to continue.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 4, Colors.GRAY);
    }
}
