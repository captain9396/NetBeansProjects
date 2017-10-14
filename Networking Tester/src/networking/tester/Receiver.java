/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networking.tester;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
class ReceiverThread implements Runnable{
    
    
    private Thread receiverThread;

    public ReceiverThread() {
        receiverThread = new Thread(this);
        receiverThread.start();
    }
    
    
    
    
    
    
    
    
    @Override
    public void run(){
        
        try {
            ServerSocket receiverSocket = new ServerSocket(66);
            Socket receiveDataSocket = receiverSocket.accept();
            DataInputStream rdin = new DataInputStream(receiveDataSocket.getInputStream());
            DataOutputStream rdout = new DataOutputStream(receiveDataSocket.getOutputStream());
            
            
            System.out.println(rdin.readUTF());
            
            
            
            
            Thread.sleep(5);
            
            
            Socket serverWantsToSend = new Socket("localhost", 123);
            DataInputStream din = new DataInputStream(serverWantsToSend.getInputStream());
            DataOutputStream dout = new DataOutputStream(serverWantsToSend.getOutputStream());

            dout.writeUTF("hello server");
            dout.flush();
            
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ReceiverThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}



public class Receiver{
    public static void main(String[] args){
        new ReceiverThread();
    }
}
