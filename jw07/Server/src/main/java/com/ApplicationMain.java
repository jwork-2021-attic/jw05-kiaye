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
import com.world.World;
import com.screen.PlayScreen;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import javax.swing.*;
import java.awt.event.*;

/**
 *
 * @author Aeranythe Echosong
 */
public class ApplicationMain extends JFrame{

    private AsciiPanel terminal;
    private Screen screen;
    private Selector selector;
    private InetSocketAddress listenAddress;
    private final static int PORT = 9093;
    
    private int count = 0;

    public ApplicationMain() {
        super();
        terminal = new AsciiPanel(80, 32, AsciiFont.TALRYTH_15_15);
        add(terminal);
        pack();
        screen = new PlayScreen();
        // addKeyListener(this);
        // repaint();

        listenAddress = new InetSocketAddress("localhost", PORT);
        try{
            this.selector = Selector.open();
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);

            // bind server socket channel to port
            serverChannel.socket().bind(listenAddress);
            serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);

            System.out.println("Server started on port >> " + PORT);
            }catch(IOException e) {
                e.printStackTrace();
            }
        new Thread(new Freshmain(this)).start();
    }

    public void startServer() throws IOException{
        while (true){
            synchronized(selector){
                // wait for events
                int readyCount = selector.select();
                if (readyCount == 0) {
                    continue;
                }

                // process selected keys...
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                Iterator iterator = readyKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = (SelectionKey) iterator.next();

                    // Remove key from set so we don't process it twice
                    iterator.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isAcceptable() && count < 3) { // Accept client connections
                        count++;
                        this.accept(key, count);
                        System.out.println(count);
                    } else if (key.isReadable()) { // Read from client
                        this.read(key);
                    } 
                }
            }
        }
    }

    private void accept(SelectionKey key, int count) throws IOException {
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		SocketChannel channel = serverChannel.accept();
		channel.configureBlocking(false);
		Socket socket = channel.socket();
		SocketAddress remoteAddr = socket.getRemoteSocketAddress();
		System.out.println("Connected to: " + remoteAddr);

		/*
		 * Register channel with selector for further IO (record it for read/write
		 * operations, here we have used read operation)
		 */
		channel.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, count);
        screen.addplayer(count);
	}

    private void read(SelectionKey key) throws IOException{
        int index = (int)key.attachment();
        SocketChannel channel = (SocketChannel)key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
		int numRead = -1;
		numRead = channel.read(buffer);

		if (numRead == -1) {
			Socket socket = channel.socket();
			SocketAddress remoteAddr = socket.getRemoteSocketAddress();
			System.out.println("Connection closed by client: " + remoteAddr);
			channel.close();
			key.cancel();
			return;
		}
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(buffer.array());
            ObjectInputStream ois = new ObjectInputStream(bis);
            int dir = (int)ois.readObject();
            screen.respondToUserInput(dir, index-1);
            bis.close();
            ois.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void write() throws Exception {
        synchronized(selector){
            int readyCount = selector.select();
            if (readyCount == 0){
                return;
            }
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();
                iterator.remove();
                if (!key.isValid()) {
                    continue;
                }
                if(key.isWritable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
                    ByteArrayOutputStream bos = null;
                    ObjectOutputStream oos = null;
                    try {
                        bos = new ByteArrayOutputStream();
                        oos = new ObjectOutputStream(bos);
                        synchronized(screen) {
                        oos.writeObject(screen);
                        }
                        byte[] bytes = bos.toByteArray();
                        buffer.put(bytes);
                        buffer.flip();
                        channel.write(buffer);
                        buffer.clear();
                        //System.out.println("out");
                    }
                    catch(IOException e) {
                        e.printStackTrace();
                    }
                    //channel.close();
                    bos.close();
                    oos.close();
                }
            }
        }
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


    public static void main(String[] args) {
        ApplicationMain app = new ApplicationMain();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
        try{
            app.startServer();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}