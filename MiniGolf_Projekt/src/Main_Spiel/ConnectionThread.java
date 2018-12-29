
package Main_Spiel;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

 public class ConnectionThread implements Runnable{

	    public Thread t;
	    private String threadName;
	    public int port_number;
	    private ObjectOutputStream out;
	    private ObjectInputStream in;
	    public Socket clientSocket;
	    private String received_str;
	    public ServerSocket listener;
	    
	    public boolean password;
	    public String pass;
	    
	    
	    public ArrayList<ClientHandler> client_handlers = new ArrayList();
	    
	    int ID_Counter;
            public boolean accepting_clients = true;
            int has_turn_now = 0;
	    
	    public ConnectionThread(String threadName,int port_number){
	        Random rand = new Random();
	        int zz = rand.nextInt(500);
	        
	        this.threadName = threadName;
	        this.port_number = port_number;
	        this.clientSocket = clientSocket;
	        this.password=password;
	        this.pass=pass;
	    }


	    public void run() {
	        try{
	            // Socket for server to listen at.
	            listener = new ServerSocket(port_number);
	            // Server Thread 

	            System.out.println("C_Thread:["+threadName+"] is now running at port: " + port_number);
	            // Accept a client connection once Server recieves one.
	            // Creating inout and output streams. Must creat out put stream first.
	            
	            Socket serverconnection = new Socket("127.0.0.1",port_number);
	            clientSocket = listener.accept();
	            ClientHandler cli = new ClientHandler(clientSocket,threadName,port_number,ID_Counter);
	            cli.Client_Name = "Server";
	            cli.start();
	            client_handlers.add(cli);
	            client_handlers.get(0).sv_playercount+=1;
	            while(true){
                      if(accepting_clients){
                        System.out.println("Waiting for Connection " + ID_Counter);
                        clientSocket = listener.accept();
                        cli = new ClientHandler(clientSocket,threadName,port_number,ID_Counter);
                        cli.start();
                        client_handlers.add(cli);
                        client_handlers.get(0).sv_playercount+=1;
	                ID_Counter++;
	            
                            for(int i = 0; i < client_handlers.size(); i ++){
                                ClientHandler client_h = client_handlers.get(i);
                                client_h.clis = client_handlers;
                            
                            }
                      }
	            }
	        }catch(Exception e){System.out.println("ERROR:"+e); exit_thread();}
	    }
	    
	    public void exit_thread(){
	        try{
	        System.out.println("exiting..");
	        }catch(Exception e){System.out.println(e);}
	    }
	    
	    public void start () {
	      System.out.println("Starting " +  threadName );
	      if (t == null) {
	         t = new Thread (this, threadName);
	         t.start ();
	      }
	   }
	}
	
