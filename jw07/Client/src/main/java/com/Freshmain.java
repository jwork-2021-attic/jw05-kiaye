package com;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.screen.PlayScreen;

public class Freshmain implements Runnable {
    ApplicationMain main;

    public Freshmain(ApplicationMain main){
        this.main = main;
    }

    public void run() {
        while (true) {
            SocketChannel client = main.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
            int numRead = -1;
            try {
                numRead = client.read(buffer);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            if(numRead != -1) {
                try {
                    ByteArrayInputStream bis = new ByteArrayInputStream(buffer.array());
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    PlayScreen screen = (PlayScreen)ois.readObject();
                    main.setscreen(screen);
                    main.repaint();
                    bis.close();
                    ois.close();
                    //System.out.print("read");
                }
                catch(IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    
                    e.printStackTrace();
                }
            }
            
        }
    }
}