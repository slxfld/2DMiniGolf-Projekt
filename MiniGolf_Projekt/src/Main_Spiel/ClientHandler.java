package Main_Spiel;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


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
            
            public int Punkte[];
            public int schlage;
            public int Platz;
            public String playername = "noname";
            public boolean inlobby = true;
            
            public int try_count = 0;
	    public boolean has_turn = false;
            public boolean can_play = false;
            
            public float sv_ballx;
            public float sv_bally;
            public boolean sv_pushed = false;
            public int sv_clk = 50;
            
            public boolean sv_goal = false;
            public boolean sv_fgoal = false;
            public int sv_player_index = 0;
            public int sv_level_index = 1;
            public boolean sv_showpoints = false;
            public int sv_schlag = 0;
            public int sv_playercount = 0;
            
            
	    public ClientHandler(Socket socket,String RT_name,int RT_port,int Serv_id){
	        this.socket=socket; this.RT_name=RT_name; this.RT_port=RT_port;
	        this.Serv_id=Serv_id;
                Punkte = new int[10];
                for(int i=0;i<10-1;i++){
                    Punkte[i] = 0;
                }
	    }
	    public void run() {
	            try{
	            if(Client_Name == "Server"){
	            System.out.println("[created new Server Handler]");
                      playername = "Server";
                      has_turn = true;
                      can_play = true;
                      sv_player_index = 0; //Set Client Index to Server
	            }   
	            else
	            {
	            System.out.println("[created new Client Handler]");
	            Client_Name = ""+socket.getRemoteSocketAddress(); //platform independent 
                      has_turn = false;
                      can_play = false;
	            }
	            
	            out = new ObjectOutputStream(socket.getOutputStream());
	            in = new ObjectInputStream(socket.getInputStream());
	            
	            //Send Message to all clients, that this one has connected
	                        for(int i = 0; i < clis.size();i++){
	                        ClientHandler client_h = clis.get(i);     
	                            //client_h.out.writeObject("\n<"+socket.getPort()+">:"+"New Connection");
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
                                
                                
	                        if(playername.equals("noname")){
                                    playername = received_str;
                                    System.out.println(playername);
                                    for(int i = 0; i < clis.size();i++){
                                        for(int j = 0; j < clis.size();j++){
                                            ClientHandler client_h = clis.get(i);
                                            ClientHandler client_h2 = clis.get(j);
                                            client_h.out.writeObject("PY:"+j+":"+client_h2.playername+":"+clis.get(0).sv_playercount); //Sende Namen zu Arrays der Clients
                                        }  
                                        
                                    }
                                }
                                
                                
                                String splt[] = new String[100];
                                splt = received_str.split(":");
                                    
                                if(inlobby){
                                    if(splt[0].equals("START")){
                                      //  System.out.println("Changed Lobby Var");
                                      // inlobby = false;
                                    }
                                }
	                        //Receive Client Angle,Power and use it for the game
                                System.out.println(received_str);
                                if(!inlobby){
                                    
                                    if(splt[0].equals("CPL")){
                                        System.out.println("Set CanPlay to True");
                                        can_play =true;
                                    }
                                    
                                    
                                    
                                    
                                    //Wenn DIESER Spieler dran ist und spielen kann
                                    //if(has_turn==true && can_play==true){
                                    if(splt[0].equals("PLAY")){
                                        System.out.println("reached play function");
                                        try_count++;
                                        //Zwischen Daten zu Server ClientHandler Senden
                                     //   for(int i=0;i<clis.size();i++){
                                       //     ClientHandler client_h = clis.get(i);
                                       //     if(client_h.Client_Name.equalsIgnoreCase("Server")){
                                                
                                                clis.get(0).sv_pushed = true;
                                                sv_ballx = Float.parseFloat(splt[1]);
                                                sv_bally = Float.parseFloat(splt[2]);
                                                clis.get(0).sv_ballx = sv_ballx;
                                                clis.get(0).sv_bally = sv_bally;
                                                
                                                
                                           //     client_h.out.writeObject("SRV_PUSH:"+splt[1]+":"+splt[2]);
                                        //    }
                                       // }
                                     //   }
                                    }
                                }
                        
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


	
