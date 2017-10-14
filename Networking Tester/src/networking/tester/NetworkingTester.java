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

/**
 *
 * @author User
 */
public class NetworkingTester {

    
    public static void main(String[] args) throws IOException {
        Socket serverWantsToSend = new Socket("localhost", 66);
        DataInputStream din = new DataInputStream(serverWantsToSend.getInputStream());
        DataOutputStream dout = new DataOutputStream(serverWantsToSend.getOutputStream());
        
        dout.writeUTF("hello receiver");
        dout.flush();
        
        
        ServerSocket receiverSocket = new ServerSocket(123);
        Socket receiveDataSocket = receiverSocket.accept();
        DataInputStream rdin = new DataInputStream(receiveDataSocket.getInputStream());
        DataOutputStream rdout = new DataOutputStream(receiveDataSocket.getOutputStream());


        System.out.println(rdin.readUTF());
        
    }
    
}
