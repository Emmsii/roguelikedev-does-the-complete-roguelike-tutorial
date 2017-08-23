package com.mac.rltut.game.screen.menu;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.file.FileHandler;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.engine.util.StringUtil;
import com.mac.rltut.game.Game;
import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.creature.stats.Stats;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 18/08/2017 at 07:44 PM.
 */
public abstract class EndScreen extends Screen{

    protected Game game;
    protected Player player;
    protected int xp, yp;
    
    private boolean screenshotSaved;
    

    public EndScreen(Game game){
        this.game = game;
        this.player = game.player();
        this.screenshotSaved = false;
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
        Stats stats = player.stats();
//
//        int xp = Engine.instance().widthInTiles() / 2;
//        int yp = (int) (Engine.instance().heightInTiles() * 0.12f);

//        renderer.writeCenter("You died!", xp, yp++, Colors.RED);
//        yp++;
//
//        renderer.writeCenter("RIP " + player.name() + ", who was killed on level " + (player.z + 1) + ".", xp, yp++, Colors.RED);
//        yp++;
//
//        renderer.writeCenter(StringUtil.capitalizeFirst(player.causeOfDeath()), xp, yp++, Colors.darken(Colors.RED, 0.75f));
//        yp += 3;

        int days = game.world().dayNightController().day();
        renderer.writeCenter("You survived for " + days + " day" + (days > 1 ? "s" : "") + " (" + game.world().dayNightController().tick + " turns).", xp, yp++);
        yp++;

        renderer.writeCenter("Final Score: " + calculateScore(), xp, yp++);
        yp++;
        
        Date startDate = new Date(game.sessionTimer().start());
        Date endDate = new Date(game.sessionTimer().end());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd MMM yyyy");
        List<String> lines = StringUtil.lineWrap("You started on " + dateFormat.format(startDate) + " and ended on " + dateFormat.format(endDate) + ", lasting for " + game.sessionTimer().prettyString() + ".", this.width - 4, false);
        for(String s : lines) renderer.writeCenter(s, xp, yp++);
        yp++;

        renderer.writeCenter("You dealt " + stats.getValue("damage_dealt") + " damage while taking " + stats.getValue("damage_received") + ".", xp, yp++);
        renderer.writeCenter("During combat you had " + stats.getValue("hits") + " hits, made " + stats.getValue("blocks") + " blocks and " + stats.getValue("misses") + " misses.", xp, yp++);
        yp++;

        renderer.writeCenter("Press [k] to see your kills.", xp, yp++, Colors.GRAY);
        yp++;

        renderer.writeCenter("You traveled " + stats.getValue("tiles_traveled") + " tiles exploring " + String.format("%.2f", game.world().totalExploredPercent()) + "% of the forest.", xp, yp++);

        renderer.writeCenter("Press [ESCAPE] to continue.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 4, Colors.GRAY);

        if(!screenshotSaved){
            dateFormat = new SimpleDateFormat("HHmmss_ddMMyyyy");
            File file = new File("morgue/");
            if(!file.exists()) file.mkdirs();
            Engine.instance().saveScreenshot("morgue/" + player.name() + "_" + dateFormat.format(System.currentTimeMillis()));
            screenshotSaved = true;
        }
    }
    
    private int calculateScore(){
        int score = 0;
        
        score += player.gold() * 2.5;
        score += Math.pow(player.stats().getValue("tiles_traveled"), 0.9) / 2;
        
        for(String name : player.stats().kills().keySet()){
            Creature kill = Codex.creatures.get(name).creature();
            int total = kill.strength() + kill.defense() + kill.accuracy() + (kill.maxHp() / 2) * 5;
            score += total; 
        }
        
        return score;
    }
}
