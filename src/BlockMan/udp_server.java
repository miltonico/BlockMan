package BlockMan;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class udp_server implements Runnable 
{
    public void run()
    {
        DatagramSocket sock = null;
        Personaje personaje=new Personaje();
        while(true){
        try
        {
            //1. creating a server socket, parameter is local port number
            sock = new DatagramSocket(7777);
             
            //buffer to receive incoming data
            byte[] buffer = new byte[65536];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
             
            //2. Wait for an incoming data
            echo("Server socket created. Waiting for incoming data...");
             
            //communication loop
            while(true)
            {
                
                sock.receive(incoming);
                byte[] data = incoming.getData();
                String s = new String(data, 0, incoming.getLength());
                Juego.s=s;
                
                //echo the details of incoming data - client ip : client port - client message
                //echo(incoming.getAddress().getHostAddress() + " : " + incoming.getPort() + " - " + s);
                 
                s = Juego.udpData;
                DatagramPacket dp = new DatagramPacket(s.getBytes() , s.getBytes().length , incoming.getAddress() , incoming.getPort());
                sock.send(dp);
                Thread.sleep(20);
            }
        }
         
        catch(IOException e)
        {
            System.err.println("IOException " + e);
        }   catch (InterruptedException ex) {
                Logger.getLogger(udp_server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
     
    //simple function to echo data to terminal
    public static void echo(String msg)
    {
        System.out.println(msg);
    }
}