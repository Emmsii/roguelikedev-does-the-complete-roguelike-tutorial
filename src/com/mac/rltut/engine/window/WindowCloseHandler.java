package com.mac.rltut.engine.window;

import com.esotericsoftware.minlog.Log;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:54 AM.
 */
public class WindowCloseHandler extends WindowAdapter {

    @Override
    public void windowClosing(WindowEvent e) {
        Log.info("Goodbye!");
        System.exit(0);
    }
}
