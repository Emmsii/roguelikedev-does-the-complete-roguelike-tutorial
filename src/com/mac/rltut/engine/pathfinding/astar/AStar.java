package com.mac.rltut.engine.pathfinding.astar;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.util.maths.Point;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 08:32 AM.
 */
public class AStar {
    
    private static AStar instance = null;
    
    private World world;
    private boolean init = false;
    
    
    protected AStar(){
        
    }
    
    public void init(World world){
        this.world = world;
        this.init = true;
    }
   
    public List<Point> findPath(Point startPos, Point endPos, Creature creaturePathing){
        if(!init){
            Log.error("Pathfinder not initialized.");
            System.exit(-1);
        }
        
        if(world.solid(startPos.x, startPos.y, startPos.z) || world.tile(endPos.x, endPos.y, endPos.z).solid()){
            return null;
        }
        
        Node startNode = new Node(true, startPos);
        Node endNode = new Node(true, endPos);
        
        Heap<Node> openSet = new Heap<Node>(world.width() * world.height());
        HashSet<Node> closedSet = new HashSet<Node>();
        openSet.add(startNode);
        
        int tries = 0;
        int maxTries = world.width() * world.height();
        
        while(openSet.size() > 0 && tries++ < maxTries){
            Node node = openSet.remove();
            closedSet.add(node);
            
            if(node.pos.equals(endNode.pos)) return getPath(startNode, node);
            
            for(Node neighbour : getNeighbours(node)){
                if(!neighbour.walkable || closedSet.contains(neighbour)) continue;
                if(!neighbour.pos.equals(endNode.pos) && world.level(neighbour.pos.z).clearance(neighbour.pos.x, neighbour.pos.y) < creaturePathing.size()) continue;
                
                Creature creature = world.creature(neighbour.pos.x, neighbour.pos.y, neighbour.pos.z);
                if(creature != null && creature.id != creaturePathing.id){
                    //If neighbour tile has creature, and the creature is NOT the same as the creature pathing and the tile is NOT EQUAL to the end, ignore
                    if(!neighbour.pos.equals(endNode.pos)) continue;
                }
                
                int newCostToNeighbour = node.gCost + distance(node, neighbour);

                Creature c = world.creature(neighbour.pos.x, neighbour.pos.y, neighbour.pos.z);
                if(c != null && c.id != creature.id) if(!c.isPlayer()) newCostToNeighbour += (c.timeStationary() * 2) + 1;
                
                if(newCostToNeighbour < neighbour.gCost || !openSet.contains(neighbour)){
                    neighbour.gCost = newCostToNeighbour;
                    neighbour.hCost = distance(neighbour, endNode);
                    neighbour.parent = node;
                    if(!openSet.contains(neighbour)) openSet.add(neighbour);
                }
            }
        }
        
        
        List<Point> result = new ArrayList<Point>();
        result.add(startPos);
        return result;
    }
    
    private int distance(Node a, Node b){
        int dx = Math.abs(a.pos.x - b.pos.x);
        int dy = Math.abs(a.pos.y - b.pos.y);
        if(dx > dy) return 14 * dy + 10 * (dx - dy);
        return 14 * dx + 10 * (dy - dx);
    }
    
    private List<Node> getNeighbours(Node from){
        List<Node> result = new ArrayList<Node>();
        int z = from.pos.z;
        
        for(int y = -1; y <= 1; y++){
            for(int x = -1; x <= 1; x++){
                if(x == 0 && y == 0) continue;
                int checkX = from.pos.x + x;
                int checkY = from.pos.y + y;
                if(world.inBounds(checkX, checkY, z) && !world.solid(checkX, checkY, z)){
                    result.add(new Node(!world.solid(checkX, checkY, z), new Point(checkX, checkY, z)));
                }
            }
        }
        return result;
    }
   
    private List<Point> getPath(Node startNode, Node endNode){
        List<Node> path = new ArrayList<Node>();
        List<Point> result = new ArrayList<Point>();
        
        Node currentNode = endNode;
        
        while(!currentNode.pos.equals(startNode.pos)){
            path.add(currentNode);
            result.add(currentNode.pos);
            currentNode = currentNode.parent;
        }

        Collections.reverse(result);
        return result;
    }
    
    public static AStar instance(){
        if(instance == null) instance = new AStar();
        return instance;
    }
}
