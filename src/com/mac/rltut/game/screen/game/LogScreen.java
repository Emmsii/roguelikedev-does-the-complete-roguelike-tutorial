package com.mac.rltut.game.screen.game;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.engine.util.StringUtil;
import com.mac.rltut.game.MessageLog;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

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
        
        List<ColoredString> linesToRender = new ArrayList<ColoredString>();
        int multiLineEntries = 0;
        
        for(int i = 0; i < messageCount; i++){
            int index = log.getEntries().size() - i - 1;
            if(index < 0) continue;
            ColoredString entry = log.getEntries().get(index);
            List<String> lines = StringUtil.lineWrap(entry.text, width - 2, true);
            for(int j = lines.size() - 1; j >= 0; j--) linesToRender.add(new ColoredString(lines.get(j), entry.color));
            if(lines.size() > 1 && i < log.newEntries()) multiLineEntries += lines.size() - 1;
            if(linesToRender.size() >= messageCount) break; 
        }
                    
        for(int i = 0; i < linesToRender.size(); i++){
            ColoredString entry = linesToRender.get(i);
            int color = i >= log.newEntries() + multiLineEntries && log.newEntries() != 0 ? Colors.darken(entry.color) : entry.color;
            renderer.write(entry.text, x + 1, y + height - 2 - i, color);
        }
        
//        log.resetNewEntryCount();
    }
}
