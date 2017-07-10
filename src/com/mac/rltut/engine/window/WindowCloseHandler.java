package com.mac.rltut.engine.window;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.Config;
import com.mac.rltut.engine.Engine;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

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
