package com.mac.rltut.engine.pathfinding.dijkstra;

import com.mac.rltut.engine.util.Point;
import com.mac.rltut.game.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 09:18 AM.
 */
public class Dijkstra {

    public static final int FILL = 10000;

    public static Point highestPoint(int[][] input){
        Point point = new Point();
        int highest = Integer.MIN_VALUE;

        for(int y = 0; y < input[0].length; y++){
            for(int x = 0; x < input.length; x++){
                if(input[x][y] == FILL) continue;
                if(input[x][y] > highest){
                    highest = input[x][y];
                    point.x = x;
                    point.y = y;
                }
            }
        }

        return point;
    }

    public static int[][] calculate(Point goal, int offset, float coefficient, int[][] input, World world){
        List<Point> open = new ArrayList<Point>();
        Set<Point> closed = new HashSet<Point>();

        int[][] result = new int[world.width()][world.height()];

        //Initialize a new result array if input array is null
        if(input == null){
            for(int y = 0; y < world.height(); y++) {
                for (int x = 0; x < world.width(); x++) {
                    result[x][y] = FILL;
                }
            }
        }else System.arraycopy(input, 0, result, 0, input.length);


        //Set goal tile
        open.add(goal);
        result[goal.x][goal.y] = 0 - offset;

        while(!open.isEmpty()){
            //Get a point from the open list to evaluate
            Point current = open.remove(0);

            int lowestNeighbourTile = FILL;

            //Get a list of neighbours from the current point (Cardinal + Diagonal)
            List<Point> neighbours = current.neighboursAll();

            for(Point p : neighbours){
                //If neighbour tile is solid, ignore
                if(world.tile(p.x, p.y, p.z).solid()) continue;
                //If out of bounds, ignore
                if(!world.inBounds(p.x, p.y, p.z)) continue;
                //If neighbour tile value is < lowestNeighbourTile
                if(result[p.x][p.y] < lowestNeighbourTile) lowestNeighbourTile = result[p.x][p.y];
                //If neighbour is NOT in open list and NOT in closed, add to open list
                if(!open.contains(p) && !closed.contains(p)) open.add(p);
            }

            //If lowest neighbour is < current tile, set current tile to +1 of lowest neighbour
            if(lowestNeighbourTile != FILL && lowestNeighbourTile < result[current.x][current.y]){
                result[current.x][current.y] = lowestNeighbourTile + 1;
            }
            closed.add(current);
        }

        return result;
    }

    public static int[][] calculate(Point goal, float coefficient, int[][] array, World world){
        List<Point> open = new ArrayList<Point>();
        Set<Point> closed = new HashSet<Point>();

        //Set goal tile
        array[goal.x][goal.y] = 0;
        open.add(goal);

        if(coefficient != 1) for(int y = 0; y < world.height(); y++) for(int x = 0; x < world.width(); x++) if(array[x][y] != FILL) array[x][y] *= coefficient;

        while(!open.isEmpty()){
            //Get a point from the open list to evaluate
            Point current = open.remove(0);

            //Value FILL = 10000
            int lowestNeighbourTile = FILL;

            //Get a list of neighbours from the current point (Cardinal + Diagonal)
            List<Point> neighbours = current.neighboursCardinal();

            //
            for(Point p : neighbours){
                //If neighbour tile is solid, ignore
                if(world.tile(p.x, p.y, p.z).solid()) continue;
                //If out of bounds, ignore
                if(!world.inBounds(p.x, p.y, p.z)) continue;
                //If neighbour tile value is < lowestNeighbourTile
                if(array[p.x][p.y] < lowestNeighbourTile) lowestNeighbourTile = array[p.x][p.y];
                //If neighbour is NOT in open list and NOT in closed, add to open list
                if(!open.contains(p) && !closed.contains(p)) open.add(p);
            }

            //If lowest neighbour is < current tile, set current tile to +1 of lowest neighbour
            if(lowestNeighbourTile != FILL && lowestNeighbourTile < array[current.x][current.y]){
                array[current.x][current.y] = lowestNeighbourTile + 1;
            }
            closed.add(current);
        }
        return array;
    }

    public static Point rollDown(Point start, int[][] input, int offset, int stopAt){

        //if the current input tile plus the offset value = the stopAt value, return 
        if(input[start.x][start.y] + offset == stopAt) return start;
        else if(input[start.x][start.y] + offset < stopAt) return rollUp(start, input, offset, stopAt);

        Point pointCardinal = start;
        int lowestCardinal = Integer.MAX_VALUE;
        for(Point n : start.neighboursCardinal()){
            int in = input[n.x][n.y] + offset;
            if(in == FILL || in > input.length * input[0].length) continue;
            if(in < lowestCardinal && in >= stopAt){
                lowestCardinal = in;
                pointCardinal = n;
            }
        }

        Point pointDiagonal = start;
        int lowestDiagonal = Integer.MAX_VALUE;
        for(Point n : start.neighboursDiagonal()){
            int in = input[n.x][n.y] + offset;
            if(in == FILL || in > input.length * input[0].length) continue;
            if(in < lowestDiagonal && in >= stopAt){
                lowestDiagonal = in;
                pointDiagonal = n;
            }
        }

        if(lowestCardinal <= lowestDiagonal) return pointCardinal;
        else return pointDiagonal;
    }

    public static Point rollUp(Point start, int[][] input, int offset, int stopAt){
        if(input[start.x][start.y] + offset == stopAt) return start;
        else if(input[start.x][start.y] + offset > stopAt) return rollDown(start, input, offset, stopAt);

        Point pointCardinal = start;
        int highestCardinal = Integer.MIN_VALUE;
        for(Point n : start.neighboursCardinal()){
            int in = input[n.x][n.y] + offset;
            if(in == FILL || in > input.length * input[0].length) continue;
            if(in > highestCardinal){
                highestCardinal = in;
                pointCardinal = n;
            }
        }

        Point pointDiagonal = start;
        int highestDiagonal = Integer.MIN_VALUE;
        for(Point n : start.neighboursDiagonal()){
            int in = input[n.x][n.y] + offset;
            if(in == FILL || in > input.length * input[0].length) continue;
            if(in > highestDiagonal){
                highestDiagonal = in;
                pointDiagonal = n;
            }
        }

        if(highestCardinal >= highestDiagonal) return pointCardinal;
        else return pointDiagonal;
    }
}
