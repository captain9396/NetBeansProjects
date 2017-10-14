package chatapp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
public class TCPClient 
{
    public static void main(String[] args) throws Exception
    {
        String s1;
        String s2;
        Socket connectionSocket = new Socket("localhost",5678);
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader inFromServer = new BufferedReader(
                new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream toServer = new DataOutputStream(connectionSocket.getOutputStream());
        
        while(true)
        {
            s1=inFromUser.readLine();
            toServer.writeBytes(s1+'\n');
            s2=inFromServer.readLine();
            System.out.println("Server Says : "+s2);
        }
        // TODO code application logic here
    }
}
