package com.mac.rltut.engine.util.maths;

import com.mac.rltut.engine.util.maths.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 05/07/2017 at 09:27 AM.
 */
public class Line implements Iterable<Point> {

    private List<Point> points;

    public Line(int x0, int y0, int x1, int y1){
        points = new ArrayList<Point>();

        int dx = Math.abs(x1-x0);
        int dy = Math.abs(y1-y0);

        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx-dy;

        while (true){
            points.add(new Point(x0, y0, 0));

            if (x0==x1 && y0==y1)
                break;

            int e2 = err * 2;
            if (e2 > -dx) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx){
                err += dx;
                y0 += sy;
            }
        }
    }

    public int length(){
        return points.size();
    }

    public List<Point> points(){
        return points;
    }

    @Override
    public Iterator<Point> iterator() {
        return points.iterator();
    }
}
