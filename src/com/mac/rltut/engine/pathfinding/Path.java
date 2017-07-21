package com.mac.rltut.engine.pathfinding;

import com.mac.rltut.engine.pathfinding.astar.AStar;
import com.mac.rltut.engine.util.Point;
import com.mac.rltut.game.entity.creature.Creature;

import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 08:24 AM.
 */
public class Path {
    
    private List<Point> points;
    
    public Path(Creature creature, int x, int y){
        this.points = AStar.instance().findPath(new Point(creature.x, creature.y, creature.z), new Point(x, y, creature.z));
    }
    
    public boolean hasNext(){
        return points!= null && points.size() > 0;
    }
    
    public Point getNext(){
        if(points == null) return null;
        Point p = points.get(0);
        points.remove(0);
        return p;
    }
    
    public int length(){
        if(points == null) return 0;
        return points.size();
    }
    
    public List<Point> points(){
        return points;
    }
}
