package com.mac.rltut.game.screen.game;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.MessageLog;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 18/07/2017 at 11:27 AM.
 */
public class LogScreen extends Screen{
    
    private MessageLog log;
    private int messageCount;
    
    private String defaultTitle;
    
    public LogScreen(int width, MessageLog log, int messageCount, String title){
        super(0, Engine.instance().heightInTiles() - (messageCount + 2), width, messageCount + 2, title);
        this.log = log;
        this.messageCount = messageCount;
        this.defaultTitle = title;
    }
    
    @Override
    public Screen input(KeyEvent e) {
        return null;
    }

    @Override
    public void render(Renderer renderer) {
        setTitle(defaultTitle + " (" + log.newEntries() + ")");
        renderBorder(renderer);
        
        for(int i = 0; i < messageCount; i++){
            int index = log.getEntries().size() - i - 1;
            if(index < 0) continue;
            ColoredString entry = log.getEntries().get(index);
            int color = i >= log.newEntries() && log.newEntries() != 0 ? Colors.darken(entry.color) : entry.color;
            renderer.write(entry.text, x + 1, y + height - 2 - i, color);
        }
        
        log.resetNewEntryCount();
    }
}
