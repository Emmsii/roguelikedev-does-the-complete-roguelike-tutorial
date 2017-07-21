package com.mac.rltut.game.screen.game;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.pathfinding.astar.AStar;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Point;
import com.mac.rltut.game.MessageLog;
import com.mac.rltut.game.entity.creature.ai.PlayerAI;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.screen.menu.LooseScreen;
import com.mac.rltut.game.world.World;
import com.mac.rltut.game.screen.Screen;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:28 AM.
 */
public class GameScreen extends Screen{
    
    private World world;
    private MessageLog log;
    
    private LevelScreen levelScreen;
    private LogScreen logScreen;
    private InfoScreen infoScreen;
    private EquipmentScreen equipmentScreen;
        
    private Player player;
    
    public GameScreen(World world){
        this.world = world;
        AStar.instance().init(world);
        init();
    }
    
    private void init(){
        log = new MessageLog();
        log.add(new ColoredString("hello world", Color.cyan.getRGB()));
        
        player = new Player("player", Sprite.get("player"));//temp
        player.setStats(10, 100, 10, 10, 10, 10, 16);
        new PlayerAI(player, log);

        logScreen = new LogScreen(Engine.instance().widthInTiles(), log, 9, "Log");
        infoScreen = new InfoScreen(Engine.instance().widthInTiles() - 30, 0, 30, logScreen.height(), "Info", player);
        equipmentScreen = new EquipmentScreen(Engine.instance().widthInTiles() - 30, infoScreen.height(), 30, Engine.instance().heightInTiles() - logScreen.height() - infoScreen.height(), "Equipment", player);
        levelScreen = new LevelScreen(0, 0, Engine.instance().widthInTiles() - infoScreen.width(), Engine.instance().heightInTiles() - logScreen.height(), "", world, player);
          
        Point spawn = world.startPointAt(0);
        world.add(spawn.x, spawn.y, spawn.z, player);
    }

    @Override
    public Screen input(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W: player.moveBy(0, -1, 0); break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S: player.moveBy(0, 1, 0); break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A: player.moveBy(-1, 0, 0); break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D: player.moveBy(1, 0, 0); break;

            case KeyEvent.VK_PAGE_DOWN: world.moveDown(player); break;
            case KeyEvent.VK_PAGE_UP: world.moveUp(player); break;

            case KeyEvent.VK_F1: LevelScreen.showFov = !LevelScreen.showFov; break;
        }

        world.update(player.z);
        if(player.hp() < 1) return new LooseScreen(player);
                
        return this;
    }

    @Override
    public void render(Renderer renderer) {
//        levelScreen.setTitle("Level " + (player.z + 1) + " Explored: " + world.exploredPercent(player.z) + "% Time: " + world.dayNightController().currentTime() + " (" + world.dayNightController().light() + ")");
        
        levelScreen.render(renderer);
        logScreen.render(renderer);
        infoScreen.render(renderer);
        equipmentScreen.render(renderer);
    }
}
