package chatapp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer 
{
    public static void main(String[] args) throws Exception
    {
        int id=1;
        int count=0;
        ServerSocket welcomeSocket = new ServerSocket(5678);
        while(true)
        {
            Socket connectionSocket = welcomeSocket.accept();
            WorkerThread wt = new WorkerThread(connectionSocket,id);
            Thread t = new Thread(wt);
            t.start();
            count++;
            System.out.println("Client : "+id+"got connected. Total threads : "+count);
            id++;
        }
        // TODO code application logic here
    }
}

class WorkerThread implements Runnable
{
    private int id;
    private Socket connectionSocket;
    public WorkerThread(Socket connectionSocket, int id) 
    {
        this.id=id;
        this.connectionSocket=connectionSocket;
    }
    
    public void run()
    {
        String s1;
        String s2;
        try
        {
            BufferedReader inFromClient = new BufferedReader(
                new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream toClient = new DataOutputStream(connectionSocket.getOutputStream());
        
            while(true)
            {
                s1=inFromClient.readLine();
                s2=s1.toUpperCase();
                toClient.writeBytes(s2+'\n');
            }
            
        }
        catch(Exception e)
        {
            
        }
        
        
    }
}
