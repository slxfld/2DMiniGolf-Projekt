
package Main_Spiel;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javafx.scene.input.RotateEvent;

class Client_CN implements Runnable{
    
    private Thread t;
    private String threadName = "C_C_T";
    
    public int port_number;
    public String ip_address;
    public String playername;
    public ObjectOutputStream out;
    public ObjectInputStream in; 
    public Socket socket;
    public String received_string;
    public boolean connected = false;
    
    public String playernames[] = new String[12];
    public int playercount = 0;
    public boolean inlobby = true;
    
    //Gamerelated
    public boolean has_turn = false;
    public boolean can_play = false;
    
    
    public float ballx = 0;
    public float bally = 0;
    
    public float ballpos_x;
    public float ballpos_y;
    
    public int Level = 1;
    public boolean change_level_now = false;
    public String currentplayer = "Server";
    public int schlag;
    
    public boolean showpoints = false;
    public int points[];
    public float rotation;
    
    public Client_CN(String in_ip,int in_port,String in_name){ip_address = in_ip; port_number = in_port; playername = in_name;
    playernames[0] = "Server";
    }

    public void run() { //CLIENT EMPFANG
          try{
            socket = new Socket(ip_address, port_number);
            connected = true;
            System.out.println("Connected:" + socket.getRemoteSocketAddress());
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            
            out.writeObject(playername); //SENDE NAME
            playercount++;
            
            while(true){
                int is_command = 0;
                received_string = (String) in.readObject();
               // System.out.println("test");
                //System.out.println(received_string);
                if(inlobby){
                    String splt[] = new String[100];
                    splt = received_string.split(":");
                    if(splt[0].equals("PY")){
                      playernames[Integer.parseInt(splt[1])] = splt[2];
                      playercount = Integer.parseInt(splt[3]);
                    }
                    if(splt[0].equals("START")){
                      inlobby = false;
                      
                    }
                }
                
                if(!inlobby){
                    
                    String splt[] = new String[100];
                    splt = received_string.split(":");
                    
                    if(splt[0].equals("TCK")){
                      //  System.out.println("TCKK:");
                      ballpos_x = Float.parseFloat(splt[1]);
                      ballpos_y = Float.parseFloat(splt[2]);
                      can_play = Boolean.parseBoolean(splt[3]);
                      schlag = Integer.parseInt(splt[4]);
                    }
                     if(splt[0].equals("HASTURN")){
                         System.out.println("RECEIVED HAS TURN");
                      has_turn = true;
                      
                    }
                     
                    if(splt[0].equals("STOPTURN")){
                         System.out.println("RECEIVED STOP TURN");
                      has_turn = false;
                    }
                     
                    if(splt[0].equals("LEVEL")){
                        System.out.println("LEVEL command received");
                        Level += 1;
                        change_level_now = true;
                    }
                    
                    if(splt[0].equals("PLAYER")){
                        System.out.println("PLAYER command received");
                        currentplayer = splt[1];
                    }
                    
                    if(splt[0].equals("POINTS")){
                        System.out.println("POINTS command received");
                        
                        points = new int[splt.length];
                        for(int i = 1; i < splt.length;i++){
                            points[i-1] = Integer.parseInt(splt[i]);
                        }
                        showpoints = true;
                    }
                    
                    
                    
                }
                
                try{
                
                }catch(Exception ef){System.out.println("Erro2:"+ef);}
            }
            
        }
        catch(Exception e)
        {
            connected=false;
            System.out.println("Error:"+e);
        }
    }
    public void start () {
      System.out.println("Starting " +  threadName );
      if (t == null) {
         t = new Thread (this, threadName);
         t.start ();
      }
   }
        public void exit_thread(){
        try{
        System.out.println("exiting..");
        out.close(); in.close(); socket.close(); connected=false; 
        }catch(Exception e){System.out.println(e); t.stop(); connected=false;}
    }
}

