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
import com.mac.rltut.game.screen.menu.LevelUpScreen;
import com.mac.rltut.game.screen.menu.LooseScreen;
import com.mac.rltut.game.world.World;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.world.objects.Chest;

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
    private Screen equipmentScreen;
    
    private Screen subscreen;
        
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
        player.setStats(100, 100, 3, 3, 3, 3, 16, null);
        new PlayerAI(player, log);

        logScreen = new LogScreen(Engine.instance().widthInTiles(), log, 9, "Log");
        infoScreen = new InfoScreen(Engine.instance().widthInTiles() - 29, 0, 29, logScreen.height(), "Info", player);
        equipmentScreen = new EquipmentScreen(Engine.instance().widthInTiles() - 29, infoScreen.height(), 29, Engine.instance().heightInTiles() - logScreen.height() - infoScreen.height(), "[Q] Equipment [T] Stats", player);
        levelScreen = new LevelScreen(0, 0, Engine.instance().widthInTiles() - infoScreen.width(), Engine.instance().heightInTiles() - logScreen.height(), "", world, player);
          
        Point spawn = world.startPointAt(0);
        world.add(spawn.x, spawn.y, spawn.z, player);
    }

    @Override
    public Screen input(KeyEvent key) {
        int level = player.level();
        boolean shouldUpdate = false;

        if(subscreen != null) subscreen = subscreen.input(key);
        else {
            
            switch (key.getKeyCode()) {
                case KeyEvent.VK_COMMA: shouldUpdate = true; break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W: shouldUpdate = player.moveBy(0, -1, 0); break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S: shouldUpdate = player.moveBy(0, 1, 0); break;
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A: shouldUpdate = player.moveBy(-1, 0, 0); break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D: shouldUpdate = player.moveBy(1, 0, 0); break;
                case KeyEvent.VK_SPACE: 
                    Chest chest = player.tryOpen(); 
                    if(chest != null){
                        if(chest.inventory().isEmpty()) player.notify(new ColoredString("The chest is empty", Color.ORANGE.getRGB()));
                        else subscreen = new ChestScreen(levelScreen.width() / 2 - 20, Engine.instance().heightInTiles() / 2  - 20, 40, 30, "Chest", chest, player);
                    }
                    else player.notify(new ColoredString("There is nothing there.", Color.ORANGE.getRGB()));
                    break; 
                
                case KeyEvent.VK_PAGE_DOWN: world.moveDown(player); break;
                case KeyEvent.VK_PAGE_UP: world.moveUp(player); break;

                case KeyEvent.VK_P: player.pickup(); break;

                case KeyEvent.VK_Q: equipmentScreen = new EquipmentScreen(Engine.instance().widthInTiles() - 29, infoScreen.height(), 29, Engine.instance().heightInTiles() - logScreen.height() - infoScreen.height(), "[Q] Equipment [T] Stats", player); break;
                case KeyEvent.VK_T: equipmentScreen = new EquipmentStatsScreen(Engine.instance().widthInTiles() - 29, infoScreen.height(), 29, Engine.instance().heightInTiles() - logScreen.height() - infoScreen.height(), "[Q] Equipment [T] Stats", player); break;

                case KeyEvent.VK_X: subscreen = new ExamineScreen(levelScreen.width() / 2 - (39 / 2), Engine.instance().heightInTiles() / 2  - 20, 39, 30, null, player.inventory(), player); break;
                case KeyEvent.VK_E: subscreen = new EquipScreen(levelScreen.width() / 2 - (39 / 2), Engine.instance().heightInTiles() / 2  - 20, 39, 30, null, player.inventory(), player); break;
                case KeyEvent.VK_L: subscreen = new LookScreen(0, 0, levelScreen.width(), levelScreen.height(), player, player.x - levelScreen.getScrollX() + 1, player.y - levelScreen.getScrollY() + 1); break;

                case KeyEvent.VK_F1: LevelScreen.showFov = !LevelScreen.showFov; break;
            }
        }
        
        if(subscreen == null){
            if(player.level() > level){
                subscreen = new LevelUpScreen(Engine.instance().widthInTiles() / 2 - 20, Engine.instance().heightInTiles() / 2 - 6, 40, 11, player, player.level() - level);
                return this;
            }
            if(shouldUpdate) world.update(player.z);
        }
        
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
        
        if(subscreen != null) subscreen.render(renderer);
    }
}
