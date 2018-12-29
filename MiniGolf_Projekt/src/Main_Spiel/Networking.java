
package Main_Spiel;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Networking extends Main {
   
    
    public class ClientHandler implements Runnable{
	    public Thread t1;
	    public Socket socket;
	    public ObjectOutputStream out;
	    public ObjectInputStream in;
	    public boolean disconnected;
	    public String RT_name;
	    public int RT_port;

	    public ArrayList<ClientHandler> clis = new ArrayList();
	    public String Client_Name ="";
	    private boolean disconnect;
	    public int Serv_id;
	    public int CoolDown = 0;
            
            //Game
            public boolean your_turn=false;
	    
	    public ClientHandler(Socket socket,String RT_name,int RT_port,boolean password,String pass,int Serv_id){
	        this.socket=socket; this.RT_name=RT_name; this.RT_port=RT_port;
	        this.Serv_id=Serv_id;
	    }
	    public void run() {
	            try{
	            if(Client_Name == "Server"){
	            System.out.println("[created new Server Handler]");
	            }   
	            else
	            {
	            System.out.println("[created new Client Handler]");
	            Client_Name = ""+socket.getRemoteSocketAddress(); //platform independent 
	            }
	            
	            out = new ObjectOutputStream(socket.getOutputStream());
	            in = new ObjectInputStream(socket.getInputStream());
	                
	            //Send Message to all clients, that this one has connected
	                        for(int i = 0; i < clis.size();i++){
	                        ClientHandler client_h = clis.get(i);     
	                            client_h.out.writeObject("\n<"+socket.getPort()+">:"+"New Connection");
	                        }
	            
	                if(socket.isConnected()){
	                //out.writeObject("connected to port:["+socket.getRemoteSocketAddress()+"]"); 
	                System.out.println("client<"+socket.getPort()+">ID["+Serv_id+"]connected from["+socket.getLocalAddress()+"] in ["+RT_name+"]:"+RT_port);}
	                
	                
	                //  out.writeObject("Verbunden von, "+ Client_Name); 
	                
	                while(true){
	                    if(disconnect == true){break;}
	                if(in!=null){
	                    int iscommand = 0;
	                    Thread.sleep(120); //CoolDown
	                        String received_str = "";
	                        received_str =(String) in.readObject();
	                        
	                        //Receive Client Angle,Power and use it for the game

	                        
	                        //Send Sent Message to all clients
	                        for(int i = 0; i < clis.size();i++){
	                        	ClientHandler client_h = clis.get(i);    
	
	                        }   
	                        
	                    }
	                }
	            }catch(Exception e){
	                exit_this();
	            }
	    }
	    
	    private void exit_this(){
	                    try{
	                    System.out.println(socket.getPort()+": Disconnected>");
	                        //Send Message to All clients that Cleint has disconnected
	                        for(int i = 0; i < clis.size();i++){
	                        ClientHandler client_h = clis.get(i);     
	                            client_h.out.writeObject("<"+Client_Name+">:"+"Disconnected");
	                            client_h.clis.remove(this);
	                        }
	                        t1.stop();
	            }catch(Exception ef){disconnected = true; System.out.print("second exception client handler, disconnect set"+ef); }
	    }
	    
	        public void start(){
	      if (t1 == null) {t1 = new Thread (this, "adder"); t1.start (); }
	   }
	}


	
}
