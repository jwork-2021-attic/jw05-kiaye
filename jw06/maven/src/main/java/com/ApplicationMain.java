package com;
/*
 * Copyright (C) 2015 Aeranythe Echosong
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.asciiPanel.AsciiFont;
import com.asciiPanel.AsciiPanel;
import com.screen.Screen;
import com.screen.StartScreen;
import com.world.World;
import com.screen.PlayScreen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.swing.*;
import java.awt.event.*;

/**
 *
 * @author Aeranythe Echosong
 */
public class ApplicationMain extends JFrame implements KeyListener {

    private AsciiPanel terminal;
    private Screen screen;
    private JButton newgame;
    private JButton oldgame;
    private JPanel panel;

    public ApplicationMain() {
        super();
        // terminal = new AsciiPanel(80, 32, AsciiFont.TALRYTH_15_15);
        // add(terminal);
        // pack();
        // screen = new StartScreen();
        // addKeyListener(this);
        // repaint();

        // new Thread(new Freshmain(this)).start();
        panel = new JPanel();
        this.setSize(200, 200);
        newgame = new JButton("Start new game!");
        newgame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                remove(panel);
                newstart();
            }
        });
        oldgame = new JButton("Play old game!");
        oldgame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                remove(panel);
                oldstart();
            }
        });
        panel.add(newgame);
        panel.add(oldgame);
        add(panel);

    }

    public void newstart() {
        File file = new File("log.txt");
        if (file.exists()) {
            file.delete();
        }
        terminal = new AsciiPanel(80, 32, AsciiFont.TALRYTH_15_15);
        add(terminal);
        pack();
        this.setFocusable(true);
        this.requestFocus();
        this.requestFocusInWindow();
        screen = new PlayScreen();
        addKeyListener(this);
        repaint();

        new Thread(new Freshmain(this)).start();
    }

    public void oldstart() {
        terminal = new AsciiPanel(80, 32, AsciiFont.TALRYTH_15_15);
        add(terminal);
        pack();
        this.setFocusable(true);
        this.requestFocus();
        File file = new File("log.txt");
        if (!file.exists()) {
            return;
        }

        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            fileInputStream = new FileInputStream("log.txt");
            objectInputStream = new ObjectInputStream(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            screen = new PlayScreen((World)objectInputStream.readObject());
            addKeyListener(this);
            repaint();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        new Thread(new Freshmain(this)).start();
    }

    Screen getscreen(){
        return screen;
    }

    void setscreen(Screen screen){
        this.screen = screen;
    }

    @Override
    public void repaint() {
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    /**
     *
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        screen = screen.respondToUserInput(e);
        repaint();
    }

    /**
     *
     * @param e
     */
    public void keyReleased(KeyEvent e) {
    }

    /**
     *
     * @param e
     */
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        ApplicationMain app = new ApplicationMain();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }

}