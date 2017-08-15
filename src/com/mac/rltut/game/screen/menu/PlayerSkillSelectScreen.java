package com.mac.rltut.game.screen.menu;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.world.builders.WorldBuilder;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 03:19 PM.
 */
public class PlayerSkillSelectScreen extends Screen{
    
    private int startingPoints;
    private int points;
    
    private int strength;
    private int defence;
    private int accuracy;
    private int intelligence;
    
    private int selection = 0;
    
    private Player player;
    
    public PlayerSkillSelectScreen(Player player){
        this.startingPoints = 0;
        this.points = startingPoints;
        this.strength = 2;
        this.defence = 2;
        this.accuracy = 2;
        this.intelligence = 2;
        this.player = player;
    }
    
    @Override
    public Screen input(KeyEvent e) {
        
        if(e.getKeyCode() == KeyEvent.VK_DOWN) if(selection < 3) selection++;
        if(e.getKeyCode() == KeyEvent.VK_UP) if(selection > 0) selection--;
         
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(selection == 0 && strength > 1){
                strength--;
                points++;
            }else if(selection == 1 && defence > 1){
                defence--;
                points++;
            }else if(selection == 2 && accuracy > 1){
                accuracy--;
                points++;
            }else if(selection == 3 && intelligence > 1){
                intelligence--;
                points++;
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_RIGHT && points > 0){
            if(selection == 0){
                strength++;
                points--;
            }else if(selection == 1){
                defence++;
                points--;
            }else if(selection == 2){
                accuracy++;
                points--;
            }else if(selection == 3){
                intelligence++;
                points--;
            }
        }
        
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            player.setStats(30, 100, 2, 20, 1, 1, 1, 1, 16, null);
//            player.setStats(50, 100, 2, 20, strength, defence, accuracy, intelligence, 16, null);
//            player.setStats(1000, 1000, 2, 20, 99, 99, 99, 99, 16, null);
            return new LoadingScreen(player, new WorldBuilder(92, 92, 20, System.currentTimeMillis()));
        }
        
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorderFill(renderer);
        
        renderer.writeCenter("Choose your skills", Engine.instance().widthInTiles() / 2, 8);
        renderer.write("Points Left: " + points, 10, 13);

        int xp = 12;
        int yp = 15;

        String str = String.format("Strength: %d", strength);
        String def = String.format("Defense: %d", defence);
        String acc = String.format("Accuracy: %d", accuracy);
        String intel = String.format("Intelligence: %d", intelligence);

        if(selection == 0) str = str + " <";
        if(selection == 1) def = def + " <";
        if(selection == 2) acc = acc + " <";
        if(selection == 3) intel = intel + " <";

        renderer.write(str, xp, yp+=2);
        renderer.write(def, xp, yp+=2);
        renderer.write(acc, xp, yp+=2);
        renderer.write(intel, xp, yp+=2);

        renderer.writeCenter("Press [ENTER] to continue.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 4, Colors.GRAY);
    }
}
