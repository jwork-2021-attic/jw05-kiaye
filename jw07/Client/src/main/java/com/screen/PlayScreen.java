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
package com.screen;

import com.world.*;
import com.asciiPanel.AsciiPanel;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
/**
 *
 * @author Aeranythe Echosong
 */
public class PlayScreen implements Screen, Serializable {

    private static final long serialVersionUID = 1L;
    private World world;
    private List<Creature> players;
    private int screenWidth;
    private int screenHeight;
    private List<String> messages;
    private List<String> oldMessages;

    public PlayScreen() {
        this.screenWidth = 30;
        this.screenHeight = 30;
        createWorld();
        this.messages = new ArrayList<String>();
        this.oldMessages = new ArrayList<String>();
        players = new ArrayList<>();
        //CreatureFactory creatureFactory = new CreatureFactory(this.world);
        
        //createCreatures(creatureFactory);
    }
    public PlayScreen(World world){
        this.screenWidth = 30;
        this.screenHeight = 30;
        this.world = world;
        this.messages = new ArrayList<String>();
        this.oldMessages = new ArrayList<String>();
        Creature player = world.getPaths().get(0).get(0);
        players = new ArrayList<>();
        players.add(player);
        player.move(player.getmx(), player.getmy());
    }
    // private void createCreatures(CreatureFactory creatureFactory) {
    //     this.player = creatureFactory.newPlayer(this.messages);
    //     creatureFactory.newFungus();
    // }

    private void createWorld() {
        world = new WorldBuilder(30, 30).makeCaves().build();
    }

    private void displayTiles(AsciiPanel terminal, int left, int top) {
        // Show terrain
        for (int x = 0; x < screenWidth; x++) {
            for (int y = 0; y < screenHeight; y++) {
                int wx = x + left;
                int wy = y + top;
                terminal.write(world.glyph(wx, wy), x, y, world.color(wx, wy));
            }
        }
        // Show creatures
        for (Creature creature : world.getCreatures()) {
            if (creature.x() >= left && creature.x() < left + screenWidth && creature.y() >= top
                    && creature.y() < top + screenHeight) {
                    terminal.write(creature.glyph(), creature.x() - left, creature.y() - top, creature.color());
            }
        }
        // Creatures can choose their next action now
        //world.update();
    }

    private void displayMessages(AsciiPanel terminal, List<String> messages) {
        int top = this.screenHeight - messages.size();
        for (int i = 0; i < messages.size(); i++) {
            terminal.write(messages.get(i), 1, top + i + 1);
        }
        this.oldMessages.addAll(messages);
        messages.clear();
    }

    private void save() throws IOException {
        File file = new File("log.txt");
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fileOutputStream = new FileOutputStream("log.txt", true);
        ObjectOutputStream objectOutputStream;
        if(file.exists()) {
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
        }
        else {
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
        }
        objectOutputStream.writeObject(this.world);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        // Terrain and creatures
        displayTiles(terminal, getScrollX(), getScrollY());
        // Player
        //terminal.write(player.glyph(), player.x() - getScrollX(), player.y() - getScrollY(), player.color());
        // Stats
        String stats = String.format("Score:%3d!\n Press 's' to save!", players.get(0).getscore());
        terminal.write(stats, 1, 31);
        // Messages
        displayMessages(terminal, this.messages);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        int res = 0;
        switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                //res = player.moveBy(-1, 0);
                players.get(0).setmx(-1);
                players.get(0).setmy(0);
                break;
            case KeyEvent.VK_RIGHT:
                //res = player.moveBy(1, 0);
                players.get(0).setmx(1);
                players.get(0).setmy(0);
                break;
            case KeyEvent.VK_UP:
                //res = player.moveBy(0, -1);
                players.get(0).setmx(0);
                players.get(0).setmy(-1);
                break;
            case KeyEvent.VK_DOWN:
                //res = player.moveBy(0, 1);
                players.get(0).setmx(0);
                players.get(0).setmy(1);
                break;
            case KeyEvent.VK_S:
                try {
                    save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        //new Thread(new creaturethread()).start();
        // if (players.get(index).getstate() == -1) return new LoseScreen();
        // else if (players.get(index).getstate() == 1) return new WinScreen();
        // else
        //     return this;
        return this;
    }

    @Override
    public void addplayer(int index){
        CreatureFactory creatureFactory = new CreatureFactory(this.world);
        Creature player = new Creature(this.world, (char)2, AsciiPanel.red, 100, 20, 5, 9);
        world.addAtEmptyLocation(player);
        List<Creature> listplayer = new ArrayList<>();
        listplayer.add(player);
        players.add(player);
        world.addpath(listplayer);
        new PlayerAI(player, messages);
        creatureFactory.newFungus();

    }
    public int getScrollX() {
        return Math.max(0, Math.min(players.get(0).x() - screenWidth / 2, world.width() - screenWidth));
    }

    public int getScrollY() {
        return Math.max(0, Math.min(players.get(0).y() - screenHeight / 2, world.height() - screenHeight));
    }

}
