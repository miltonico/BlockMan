package BlockMan;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class udp_client implements Runnable
{
    
    public void run()
    {
        int client_connection_failed=0;
        DatagramSocket sock = null;
        int port = 7777;
        String s;
         
        BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
        Personaje personaje=new Personaje();
        boolean tc=false;
        while(client_connection_failed<10 && !tc){
            try
            {

                sock = new DatagramSocket();
                sock.setSoTimeout(1000);
                InetAddress host = InetAddress.getLocalHost();//InetAddress.getByName("192.168.0.105");

                //Juego.termino_conexion=false;
                Juego.contadorCon=0;
                System.out.println("conectando...");
                while(!tc){//sigue hasta q termine el juego.
                    
                    //take input and send the packet
                    s = Juego.udpData;
                    byte[] b = s.getBytes();

                    DatagramPacket  dp = new DatagramPacket(b , b.length , host , port);
                    
                    sock.send(dp);

                    //now receive reply
                    //buffer to receive incoming data
                    byte[] buffer = new byte[65536];
                    DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                    sock.setSoTimeout(1000);
                    sock.receive(reply);
                    /*
                    if(!sock.isClosed()){
                        sock.close();
                    }
                    */
                    byte[] data = reply.getData();
                    s = new String(data, 0, reply.getLength());
                    Juego.s=s;
                    //echo the details of incoming data - client ip : client port - client message
                    //echo(reply.getAddress().getHostAddress() + " : " + reply.getPort() + " - " + s);
                    tc=Juego.termino_conexion;
                    Thread.sleep(20);
                    client_connection_failed=0;
                }
            }
            catch(IOException e)
            {
                System.out.println("IOException " + e);
            } catch (InterruptedException ex) {
                Logger.getLogger(udp_client.class.getName()).log(Level.SEVERE, null, ex);
            }
            client_connection_failed++;
        }
        Juego.client_connection_down=true;
    }
}