package com.mac.rltut.game.screen.menu;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.file.Config;
import com.mac.rltut.engine.file.FileHandler;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.engine.util.StringUtil;
import com.mac.rltut.engine.web.HttpRequest;
import com.mac.rltut.game.Game;
import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.creature.stats.Stats;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 18/08/2017 at 07:44 PM.
 */
public abstract class EndScreen extends Screen{

    protected Game game;
    protected Player player;
    protected int xp, yp;
    
    private final int finalScore;
    
    private boolean screenshotSaved;
    private String scoreResult;
    private boolean scoreSubmitted;

    public EndScreen(Game game){
        this.game = game;
        this.player = game.player();
        this.screenshotSaved = false;
        this.scoreSubmitted = false;
        this.finalScore = calculateScore();
        FileHandler.deleteGameSave();
        
        if(Config.autoPostScore) submitScore();
    }

    @Override
    public Screen input(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) return new StartScreen();
        else if(e.getKeyCode() == KeyEvent.VK_K) return new KillsScreen(player, this);
        else if(e.getKeyCode() == KeyEvent.VK_S && !scoreSubmitted) submitScore();
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        Stats stats = player.stats();
        
        int days = game.world().dayNightController().day();
        renderer.writeCenter("You survived for " + days + " day" + (days > 1 ? "s" : "") + " (" + game.world().dayNightController().tick + " turns).", xp, yp++);
        yp++;

        renderer.writeCenter("Final Score: " + finalScore, xp, yp++);
        yp++;
        
        Date startDate = new Date(game.gameStartTime());
        Date endDate = new Date(game.sessionTimer().end());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd MMM yyyy");
        List<String> lines = StringUtil.lineWrap("You started on " + dateFormat.format(startDate) + " and ended on " + dateFormat.format(endDate) + ", lasting for " + game.sessionTimer().prettyString() + ".", this.width - 4, false);
        for(String s : lines) renderer.writeCenter(s, xp, yp++);
        yp++;

        renderer.writeCenter("You dealt " + stats.getValue("damage_dealt") + " damage while receiving " + stats.getValue("damage_received") + ".", xp, yp++);
        renderer.writeCenter("During combat you had " + stats.getValue("hits") + " hits, " + stats.getValue("misses") + " misses and made " + stats.getValue("blocks") + " blocks", xp, yp++);
        yp++;

        renderer.writeCenter("Press [k] to see your kills.", xp, yp++, Colors.GRAY);
        yp++;

        renderer.writeCenter("You traveled " + stats.getValue("tiles_traveled") + " tiles exploring " + String.format("%.2f", game.world().totalExploredPercent()) + "% of the forest.", xp, yp++);
        yp++;
        
        renderer.writeCenter("Final Stats", xp, yp++);
        yp++;
        renderer.writeCenter("Level " + player.level() + " (" + player.xp() + "xp)", xp, yp++);
        renderer.writeCenter(String.format("HP: %-4d MANA: %-4d", player.maxHp(), player.maxMana()), xp, yp++);
        renderer.writeCenter(String.format("STR: %-4d DEF: %-4d", player.strength(), player.defense()), xp, yp++);
        renderer.writeCenter(String.format("ACC: %-4d INT: %-4d", player.accuracy(), player.intelligence()), xp, yp++);

        if(!scoreSubmitted) renderer.writeCenter("Press [s] to submit your score.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 5, Colors.GRAY);
        else{
            if(scoreResult != null && scoreResult.equalsIgnoreCase("worked")) renderer.writeCenter("Score submitted.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 5, Colors.GRAY);
            else renderer.writeCenter("Error saving score.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 5, Colors.RED);
        }
        renderer.writeCenter("Press [ESCAPE] to continue.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 4, Colors.GRAY);

        if(!screenshotSaved){
            dateFormat = new SimpleDateFormat("HHmmss_ddMMyyyy");
            File file = new File("morgue/");
            if(!file.exists()) file.mkdirs();
            Engine.instance().saveScreenshot("morgue/" + player.name() + "_" + dateFormat.format(System.currentTimeMillis()));
            screenshotSaved = true;
        }
    }
    
    private void submitScore(){
        UUID id = UUID.randomUUID();
        try {
            HttpRequest request = new HttpRequest();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            int deathLevel = this instanceof WinScreen ? -1 : player.z;
            scoreResult = request.sendScore(id.toString(), player.name(), finalScore, deathLevel, game.sessionTimer().durationMilliseconds(), dateFormat.format(game.sessionTimer().end()), Engine.instance().version());
        } catch (IOException e) {
            Log.warn("Unable to submit highscores.");
        }
        FileHandler.saveScore(id);
        scoreSubmitted = true;
    }
    
    private int calculateScore(){
        int score = 0;
        
        score += player.gold() * 2.5;
        score += Math.pow(player.stats().getValue("tiles_traveled"), 0.9) / 2;
        
        for(String name : player.stats().kills().keySet()){
            if(!Codex.creatures.containsKey(name)) continue;
            Creature kill = Codex.creatures.get(name).creature();
            int total = kill.strength() + kill.defense() + kill.accuracy() + (kill.maxHp() / 2) * 5;
            score += total; 
        }
        
        return score;
    }
}
