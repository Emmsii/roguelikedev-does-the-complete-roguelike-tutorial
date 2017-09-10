package com.mac.rltut.game.screen.menu;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.file.FileHandler;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.engine.web.HttpRequest;
import com.mac.rltut.game.screen.Screen;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 31/08/2017 at 10:51 AM.
 */
public class HighscoresScreen extends Screen{

    private List<Entry> entries;
    private int page;
    private int maxPages;
    private int scoresPerPage;
    
    private List<String> localScores;
    private String error;
    
    public HighscoresScreen(){
        this.entries = new ArrayList<Entry>();
        this.localScores = FileHandler.loadScores();
        
        try {
            String scores = new HttpRequest().getScores();
            if(scores == null || scores.length() == 0 || !scores.startsWith("[") && !scores.endsWith("]")){
                Log.error("Could not load highscores.");
                error = "Could not load highscores.";
                return;
            }
            JSONArray array = (JSONArray) new JSONParser().parse(scores);

            for(int i = 0; i < array.size(); i++){
                JSONObject obj = (JSONObject) array.get(i);
                String id = (String) obj.get("id");
                String name = (String) obj.get("name");
                int score = Integer.parseInt((String) obj.get("score"));
                int deathLevel = Integer.parseInt((String) obj.get("death_level"));
                long timeTaken = Long.parseLong((String) obj.get("time_taken"));
                String endTime = (String) obj.get("end_time");
                String version = (String) obj.get("version");
                entries.add(new Entry(id, name, score, deathLevel, timeTaken, endTime, version));
            }

        } catch (IOException e) {
            Log.error("Could not get scores.");
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        entries.sort(new Comparator<Entry>() {
            @Override
            public int compare(Entry o1, Entry o2) {
                if(o1.score < o2.score) return 1;
                else if(o1.score > o2.score) return -1;
                return 0;
            }
        });
        
        scoresPerPage = height - 13;
        maxPages = entries.size() / scoresPerPage;
    }

    @Override
    public Screen input(KeyEvent key) {
        if(key.getKeyCode() == KeyEvent.VK_LEFT){
            page--;
            if(page < 0) page = 0;
        }else if(key.getKeyCode() == KeyEvent.VK_RIGHT){
            page++;
            if(page >= maxPages) page = maxPages;
        }else if(key.getKeyCode() == KeyEvent.VK_ESCAPE) return new StartScreen();
        
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorderFill(renderer);
        
        renderer.writeCenter("Highscores", Engine.instance().widthInTiles() / 2, 2);
        if(error != null) renderer.writeCenter(error, Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 2, Colors.RED);
        
        renderer.write("Page " + (page + 1) + "/" + (maxPages + 1), 2, 2, Colors.GRAY);
        
        int xp = 3;
        int yp = 5;

        String header = String.format("%-4s %s %14s %12s %12s %8s %10s", "RANK", "NAME", "SCORE", "DIED ON", "TIME TAKEN", "POSTED", "VERSION");
        renderer.write(header, xp, yp++);
        for(int i = page * scoresPerPage; i < Math.min(page * scoresPerPage + scoresPerPage, entries.size()); i++){
            Entry e = entries.get(i);
            String deathText = e.deathLevel == -1 ? "Won!" : "Level " + Integer.toString(e.deathLevel + 1);
            String text = String.format("%-4d %-13s %-10s %-9s %-12s %-9s %s", i + 1, e.name, NumberFormat.getIntegerInstance().format(e.score), deathText, prettyTime(e.timeTaken), e.endTime, e.version);
            if(localScores.contains(e.id)) renderer.write("*", xp - 1, yp);
            renderer.write(text, xp, yp++, i % 2 == 0 ? Colors.darken(Colors.WHITE, 0.8f) : Colors.WHITE);
        }

        renderer.writeCenter("Press [LEFT/RIGHT] to change page.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 5, Colors.GRAY);
        renderer.writeCenter("Press [ESCAPE] to return.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 4, Colors.GRAY);
    }

    public String prettyTime(long duration){
        int seconds = (int) ((duration / 1000) % 60);
        int minutes = (int) ((duration / (1000 * 60)) % 60);
        int hours = (int) ((duration / (1000 * 60 * 60)) % 24);
        String result = "";
        if(hours > 0) result += hours + "h";
        if(minutes > 0) result += " " + (minutes < 10 ? "0" : "") +minutes + "m";
        if(seconds > 0) result += " " + (seconds < 10 ? "0" : "") + seconds + "s";
        return result.trim();
    }
    
    class Entry{
        public String id;
        public String name;
        public int score;
        public int deathLevel;
        public long timeTaken;
        public String endTime;
        public String version;
        
        public Entry(String id, String name, int score, int deathLevel, long timeTaken, String endTime, String version){
            this.id = id;
            this.name = name;
            this.score = score;
            this.deathLevel = deathLevel;
            this.timeTaken = timeTaken;
            this.endTime = endTime;
            this.version = version;
        }
    }
}
