package Main_Spiel;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import javax.swing.ImageIcon;

import javax.swing.JFrame;
import javax.swing.Timer;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.samples.SimulationBody;

public class Main implements ActionListener, MouseListener, KeyListener, MouseMotionListener {

    public static final String Version = "1.24";
    /**
     * The scale 45 pixels per meter
     */
    public static final double SCALE = 45.0;

    /**
     * The conversion factor from nano to base
     */
    public static final double NANO_TO_BASE = 1.0e9;

    public static Main main1;

    private String player_type = "none";

    private String Game_State = "MAIN_MENU";

    public GameButton join_button;
    public GameButton host_button;
    public GameButton enterlobby_button;
    public GameButton startgame_button;

    private final String join_Ip = "";
    private final String Server_Ip = "";

    private float Ball_X;
    private float Ball_Y;

    private boolean In_Game = false;
    private int LevelIndex = 1;
    private int Tick = 0;

    /**
     * The canvas to draw to
     */
    protected Canvas canvas;

    /**
     * The dynamics engine
     */
    public World world;

    /**
     * Wether the example is stopped or not
     */
    protected boolean stopped;

    /**
     * The time stamp for the last iteration
     */
    protected long last;

    /**
     * Default constructor for the window
     */
    private Point point;

    private Client_CN ccn;
    boolean pressed_join = false;
    private String connect_ip = "";
    private String playername = "";
    private String connect_port = "2050";
    public boolean isplayer = false;
    boolean nameset = false;
    boolean ipset = false;
    boolean Server_Running = false;
    private ConnectionThread Thread1;

    public Image flagge;

    private LevelBuilder builder;
    private final Font liste_font;

    //Gamerelated variables
    private final boolean has_turn = false;
    private Point aim_point = new Point(0, 0);
    private final Vector2 world_aim_point = new Vector2(0, 0);
    private final boolean pushed = false;
    private final int push_force;

    private float receive_posx;
    private float receive_posy;

    private Point mousepoint;

    private JFrame jframe;

    private double xa;
    private double ya;

    private final int testing_counter = 100;

    private final float rotation_v = 0;
    private final String currentPlayer = "Name";
    private boolean Game_Over = false;
//networking Classes, Edit_Copy of Server Java

    public void start_Server() {
        try {
            Thread1 = new ConnectionThread("Server_Thread", Integer.parseInt(connect_port));
            if (!Server_Running) {
                Server_Running = true;
                System.out.println("Reached, Starting Server");
                Thread1.start();
            }

        } catch (Exception e) {
            System.out.println("Error D: .." + e);
        }
    }

    public void connect_to_server() {
        try {
            ccn = new Client_CN(connect_ip, Integer.parseInt(connect_port), playername);
            ccn.start();

        } catch (Exception e) {
        }
    }

    public final class CustomMouseAdapter extends MouseAdapter {

        @Override
        public void mouseDragged(MouseEvent e) {
            // store the mouse click postion for use later
            mousepoint = new Point(e.getX(), e.getY());
            aim_point = new Point(e.getX(), e.getY());
            xa = (aim_point.getX() - canvas.getWidth() * 0.5) / SCALE;
            ya = -(aim_point.getY() - canvas.getHeight() * 0.5) / SCALE;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // store the mouse click postion for use later
            if (Game_State == "MAIN_MENU") {
                join_button.mousePressed(e);
                host_button.mousePressed(e);
            }
            if (Game_State == "INPUT_STATE") {
                enterlobby_button.mousePressed(e);
            }
            if (Game_State == "LOBBY") {
                startgame_button.mousePressed(e);
            }

            point = new Point(e.getX(), e.getY());
            System.out.println(point);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (Game_State == "MAIN_MENU") {
                join_button.mouseReleased(e);
                host_button.mouseReleased(e);

                if (join_button.was_pressed == true) {
                    Game_State = "INPUT_STATE";
                }

                if (host_button.was_pressed == true) {
                    Game_State = "LOBBY";
                    start_Server();
                }
            }
            if (Game_State == "INPUT_STATE") {
                enterlobby_button.mouseReleased(e);
                if (enterlobby_button.was_pressed == true) {

                    isplayer = true;
                    connect_to_server();
                }
            }
            if (Game_State == "LOBBY") {
                startgame_button.mouseReleased(e);
                if (startgame_button.was_pressed == true) {
                    Thread1.accepting_clients = false;
                    initialize_world();
                    Game_State = "PLAYING";
                    In_Game = true;
                    //Set Server to not in Lobby

                    if (!isplayer) {

                        Thread1.client_handlers.get(0).inlobby = false;
                        System.out.println(Thread1.client_handlers.get(0).Client_Name + ":" + Thread1.client_handlers.get(0).inlobby);

                        for (int i = 0; i < Thread1.client_handlers.size(); i++) {
                            try {

                                ClientHandler cl_local = Thread1.client_handlers.get(i);
                                cl_local.out.writeObject("START"); //Senden An Client
                                cl_local.inlobby = false; //Setzen von ServerSide InLobby false
                            } catch (Exception s) {
                                System.out.println("Error: " + s);
                            }
                        }
                    }
                }
            }
            if (Game_State == "PLAYING") {

                if (isplayer) {
                    try {

                        world_aim_point.set(xa, ya);
                        //Senden Der ZielPosition durch CCN(Client Connection Thread)
                        if (ccn.has_turn) {
                            ccn.out.writeObject("PLAY:" + world_aim_point.x + ":" + world_aim_point.y);
                        }
                    } catch (Exception k) {
                        System.out.println("Error: " + k);
                    }
                }
                if (!isplayer) {
                    try {

                        if (Thread1.client_handlers.get(0).has_turn == true && Thread1.client_handlers.get(0).can_play == true) {
                            //world_aim_point.set(xa*600, ya*600);
                            Thread1.client_handlers.get(0).sv_ballx = ((float) xa);
                            Thread1.client_handlers.get(0).sv_bally = ((float) ya);

                            Thread1.client_handlers.get(0).can_play = false;
                            Thread1.client_handlers.get(0).sv_pushed = true;
                            Thread1.client_handlers.get(0).sv_clk = 50;
                        }
                    } catch (Exception k) {
                        System.out.println("Error: " + k);
                    }

                }
            }
        }
    }

    public int input_case = 1;

    public class CustomKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            if (Game_State == "INPUT_STATE") {
                char character = e.getKeyChar();
                boolean addchar = true;
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    addchar = false;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    addchar = false;
                }

                if (input_case == 1 && addchar == true) {
                    connect_ip += character;
                    if (e.getKeyCode() == com.sun.glass.events.KeyEvent.VK_BACKSPACE) {
                        connect_ip = "";
                    }
                }

                if (input_case == 2 && addchar == true) {
                    playername += character;
                    if (e.getKeyCode() == com.sun.glass.events.KeyEvent.VK_BACKSPACE) {
                        playername = "";
                    }
                }

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        input_case = 1;
                        break;
                    case KeyEvent.VK_DOWN:
                        input_case = 2;
                        break;
                }
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:

                    break;
                case KeyEvent.VK_RIGHT:

                    break;
            }
        }
    }

    public Main() {

        Timer timer = new Timer(100, this);
        timer.start();
        JFrame jframe = new JFrame();

        //Setup WorldBuilder
        push_force = 3500;
        //Menu Buttons
        join_button = new GameButton(this, "Beitreten", 0, 0, 100, 40, 500, 400);
        host_button = new GameButton(this, "Host", 0, 100, 100, 40, 500, 400 + 100);
        enterlobby_button = new GameButton(this, "Enter", 140, 10, 100, 40, 500 + 140, 400 + 10);
        startgame_button = new GameButton(this, "Start", 70, 150, 100, 40, 500 + 70, 400 + 150);

        // setup the JFrame
        jframe.setTitle("Minigolf Netzwerk Spiel");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setResizable(false);

        // add a window listener
        jframe.addWindowListener(new WindowAdapter() {
            /* (non-Javadoc)
             * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
             */
            @Override
            public void windowClosing(WindowEvent e) {
                // before we stop the JVM stop the example

                super.windowClosing(e);
            }
        });

        // create the size of the window
        Dimension size = new Dimension(1000, 800);

        // create a canvas to paint to 
        this.canvas = new Canvas();
        this.canvas.setPreferredSize(size);
        this.canvas.setMinimumSize(size);
        this.canvas.setMaximumSize(size);

        MouseAdapter ml = new CustomMouseAdapter();
        this.canvas.addMouseMotionListener(ml);
        this.canvas.addMouseWheelListener(ml);
        this.canvas.addMouseListener(ml);

        KeyListener listener = new CustomKeyListener();
        //this.canvas.addKeyListener(listener);
        this.canvas.addKeyListener(listener);

        liste_font = new Font("Verdana", Font.BOLD, 15);
        // add the canvas to the JFrame
        jframe.add(this.canvas);

        // make the JFrame not resizable
        jframe.setResizable(false);

        // size everything
        jframe.pack();

        jframe.setVisible(true);

        // make sure we are not stopped
        this.stopped = false;

        this.start();

        //Erstellen des Builders
        builder = new LevelBuilder(this.canvas);
        flagge = new ImageIcon(getClass().getResource("/Bilder/Flagge.gif")).ge‌​tImage();
    }

    protected void render(Graphics2D g, double elapsedTime) {
        //System.out.println("RenderTick:"+Tick);
        Tick++;
        // lets draw over everything with a white background
        g.setColor(Color.WHITE);
        g.fillRect(-500, -400, 1000, 800);

        if (!Game_State.equals("PlAYING")) {
            //Background Colour
            g.setColor(Color.GREEN.darker());
            g.fillRect(-500, -500, 1000, 1000);
        }

        if (Game_State.equals("INPUT_STATE")) {

            Font font = new Font("Verdana", Font.PLAIN, 18);
            AffineTransform yFlip = AffineTransform.getScaleInstance(1, -1);
            g.transform(yFlip);

            //Draw Menu Unterfläche
            g.setColor(Color.gray);
            g.fillRect(-50, -90, 300, 150);
            //Draw Menu Oberfläche
            g.setColor(Color.gray.brighter());
            g.fillRect(-50 - 5, -90 - 5, 300, 150);
            g.setFont(font);

            if (input_case == 1) {
                g.setColor(Color.GREEN.darker());
            } else {
                g.setColor(Color.BLACK);
            }
            g.drawString("IP:", -20, -50);
            g.setColor(Color.BLACK);
            g.drawString(connect_ip, 40, -50);

            if (input_case == 2) {
                g.setColor(Color.GREEN.darker());
            } else {
                g.setColor(Color.BLACK);
            }
            g.drawString("Name:", -20, -20);
            g.setColor(Color.BLACK);
            g.drawString(playername, 40, -20);
            try {
                enterlobby_button.render(g);
            } catch (Exception e) {
            }

            try {
                if (ccn.socket.isConnected()) {
                    Game_State = "LOBBY";
                }
            } catch (Exception k) {
            }

        }

        if (Game_State.equals("MAIN_MENU")) {

            AffineTransform yFlip = AffineTransform.getScaleInstance(1, -1);
            g.transform(yFlip);
            //Render the Menu

            g.setColor(Color.GRAY);
            g.setFont(new Font("Verdana", Font.BOLD, 50));
            g.drawString("Minigolf", -60 + 2, -130 + 2);

            g.setFont(new Font("Verdana", Font.BOLD, 50));
            g.setColor(Color.WHITE);
            g.drawString("Minigolf", -60, -130);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Verdana", Font.BOLD, 21));
            g.drawString(Version, 80, -103);

            g.setFont(new Font("Verdana", Font.TYPE1_FONT, 12));

            g.drawString("(C) 2017,2018 Simon Lixenfeld", -480, 380);

            //Draw Menu Unterfläche
            g.setColor(Color.gray);
            g.fillRect(-50, -90, 200, 300);

            //Draw Menu Oberfläche
            g.setColor(Color.gray.brighter());
            g.fillRect(-50 - 5, -90 - 5, 200, 300);

            try {
                join_button.render(g);
                host_button.render(g);
            } catch (Exception e) {
            }

        }

        if (Game_State.equals("LOBBY")) {
            AffineTransform yFlip = AffineTransform.getScaleInstance(1, -1);
            g.transform(yFlip);
            //Render the Menu

            //Draw Menu Unterfläche
            g.setColor(Color.gray);
            g.fillRect(-200, -200, 400, 400);

            //Draw Menu Oberfläche
            g.setColor(Color.gray.brighter());
            g.fillRect(-200 - 5, -200 - 5, 400, 400);

            if (!isplayer) {
                g.setFont(liste_font);
                //Draw PlayerList
                g.setColor(Color.WHITE);
                try {
                    for (int i = 0; i < Thread1.client_handlers.size(); i++) {
                        try {
                            ClientHandler cl_local = Thread1.client_handlers.get(i);
                            if (i == 0) {
                                g.drawString("Server 0", -180, -170 + i * 50);
                            } else {
                                g.drawString("Spieler " + i + ":" + cl_local.playername, -180, -170 + i * 50);
                            }
                        } catch (Exception e) {
                        }
                    }
                } catch (Exception b) {
                }
            } else if (isplayer) {

                g.setFont(liste_font);
                //Draw PlayerList
                g.setColor(Color.WHITE);
                g.drawString("Name: " + playername, 30, -180);
                for (int i = 0; i < 10; i++) {
                    try {
                        String l_name = ccn.playernames[i];

                        if (i == 0) {
                            g.drawString("Server", -180, -170 + i * 50);
                        } else {
                            if (l_name.equals("")) {
                                g.drawString("Spieler " + i + ":" + l_name, -180, -170 + i * 30);
                            } else {
                                g.drawString("Spieler " + i + ":" + l_name, -180, -170 + i * 30);
                            }

                        }

                    } catch (Exception e) {
                    }
                }

                if (ccn.inlobby == false) { //Wenn "START" Nachricht erhalten, Spiel ändert zu Spielen Zustand
                    //Problem here.
                    initialize_world();
                    Game_State = "PLAYING";
                    In_Game = true;
                }
            }

            try {
                if (!isplayer) {
                    startgame_button.render(g);
                }
            } catch (Exception e) {
            }

        }

        // lets move the view up some
        //g.translate(0.0, -1.0 * SCALE);
        if (Game_State.equals("PLAYING")) {

            //Update Network Logik
            if (!isplayer) {
                update_network();
            }

            // Ändern Hinzu Ziel  Darstellung    
            //Zeichnen von Ziel und Flagge
            g.setColor(Color.GRAY.brighter());
            g.fill(new Ellipse2D.Double(builder.goal.p_x - 0, builder.goal.p_y - 0, 0.8 * SCALE, 0.8 * SCALE));
            g.setColor(Color.GRAY.darker());
            g.fill(new Ellipse2D.Double(builder.goal.p_x - 1, builder.goal.p_y + 1, 0.8 * SCALE, 0.8 * SCALE));

            AffineTransform yFlip = AffineTransform.getScaleInstance(1, -1);

            Font font = new Font("Verdana", Font.PLAIN, 22);

            //Tick für Clients
            if (isplayer) {

                //Check for LevelChange
                if (ccn.change_level_now) {
                    ccn.change_level_now = false;
                    change_client_level();
                }

                this.world.getBody(0).translateToOrigin();
                this.world.getBody(0).translate(ccn.ballpos_x, ccn.ballpos_y);

                //Wenn Spieler Dran ist, Elemente Rendern
                if (ccn.has_turn) {
                    g.setColor(Color.BLACK);

                    yFlip = AffineTransform.getScaleInstance(1, -1);
                    g.transform(yFlip);
                    g.setFont(font);

                    g.setColor(Color.BLACK);
                    g.drawString("Du bist dran", 300, -370);
                    g.setColor(Color.WHITE);
                    g.drawString("Du bist dran", 300 + 2, -370);
                    g.transform(yFlip);

                    //Zeichnen von Linie von Client
                    try {
                        if (ccn.can_play) {
                            g.setColor(Color.RED);
                            g.draw(new Line2D.Double(
                                    ccn.ballpos_x * (SCALE - 5),
                                    ccn.ballpos_y * (SCALE - 5),
                                    // ccn.ballpos_x * (SCALE - 5) + xa * (SCALE - 5),
                                    // ccn.ballpos_y * (SCALE - 5) + ya * (SCALE - 5)));
                                    xa * (SCALE),
                                    ya * (SCALE)));
                        }
                    } catch (Exception b) {
                    }
                }
            }

            //Tick von Server
            //Zeichnen von Linie Wenn Server dran ist
            if (!isplayer) {

                if (Thread1.client_handlers.get(0).has_turn) {
                    g.setColor(Color.BLACK);
                    yFlip = AffineTransform.getScaleInstance(1, -1);
                    g.transform(yFlip);
                    g.setFont(font);

                    g.setColor(Color.BLACK);
                    g.drawString("Du bist dran", 300, -370);
                    g.setColor(Color.WHITE);
                    g.drawString("Du bist dran", 300 + 2, -370);

                    g.transform(yFlip);

                    //Zeichnen von Linie von Server
                    try {
                        if (Thread1.client_handlers.get(0).can_play) {
                            g.setColor(Color.RED);
                            g.draw(new Line2D.Double(
                                    this.world.getBody(0).getWorldCenter().x * (SCALE - 5),
                                    this.world.getBody(0).getWorldCenter().y * (SCALE - 5),
                                    // this.world.getBody(0).getWorldCenter().x *  (SCALE - 5) + xa *  (SCALE - 5),
                                    // this.world.getBody(0).getWorldCenter().y *  (SCALE - 5) + ya *  (SCALE - 5)));
                                    xa * (SCALE),
                                    ya * (SCALE)));
                        }
                    } catch (Exception b) {
                    }
                }
            }

            //World Draw Function of All Bodys
            // draw all the objects in the world
            for (int i = 0; i < this.world.getBodyCount(); i++) {
                // get the object

                //Ball
                if (i == 0) {
                    SimulationBody body = (SimulationBody) this.world.getBody(i);
                    double bx = body.getWorldCenter().x;
                    double by = body.getWorldCenter().y;
                    body.render(g, 40, Color.BLACK);
                    g.setColor(Color.WHITE);
                    g.fill(new Ellipse2D.Double(bx * (SCALE - 5) - 11, by * (SCALE - 5) - 9, 0.43 * SCALE, 0.43 * SCALE));
                } else {

                    SimulationBody body = (SimulationBody) this.world.getBody(i);
                    body.render(g, 40, Color.BLACK);

                }

            }

            //SpielerName Aktueller
            if (!isplayer) {
                g.setColor(Color.BLACK);
                yFlip = AffineTransform.getScaleInstance(1, -1);
                g.transform(yFlip);
                g.setFont(font);

                g.setColor(Color.BLACK);
                g.drawString("Aktueller Spieler: " + Thread1.client_handlers.get(Thread1.client_handlers.get(0).sv_player_index).playername, -450, -370);
                g.setColor(Color.WHITE);
                g.drawString("Aktueller Spieler: " + Thread1.client_handlers.get(Thread1.client_handlers.get(0).sv_player_index).playername, -450 + 2, -370);

                g.setColor(Color.BLACK);
                g.drawString("Schlag: " + Thread1.client_handlers.get(0).sv_schlag, -450, -340);
                g.setColor(Color.WHITE);
                g.drawString("Schlag: " + Thread1.client_handlers.get(0).sv_schlag, -450 + 2, -340);

                g.transform(yFlip);
            }
            if (isplayer) {
                g.setColor(Color.BLACK);

                yFlip = AffineTransform.getScaleInstance(1, -1);
                g.transform(yFlip);
                g.setFont(font);
                g.setColor(Color.BLACK);
                g.drawString("Aktueller Spieler: " + ccn.currentplayer, -450, -370);
                g.setColor(Color.WHITE);
                g.drawString("Aktueller Spieler: " + ccn.currentplayer, -450 + 2, -370);

                g.setColor(Color.BLACK);
                g.drawString("Schlag: " + ccn.schlag, -450, -340);
                g.setColor(Color.WHITE);
                g.drawString("Schlag: " + ccn.schlag, -450 + 2, -340);

                g.transform(yFlip);
            }

            Color backg_cl = new Color(245, 245, 245);

            //PunkteStand
            if (!isplayer) {
                if (Thread1.client_handlers.get(0).sv_showpoints) {

                    g.setColor(Color.BLACK.brighter());
                    g.fillRect(-150, -100, 300, 300);

                    g.setColor(backg_cl);
                    g.fillRect(-150, -100 + 2, 300 - 2, 300 - 2);
                    g.setColor(Color.BLACK);

                    yFlip = AffineTransform.getScaleInstance(1, -1);
                    g.transform(yFlip);

                    g.setFont(new Font("Arial", Font.BOLD, 23));
                    g.setColor(Color.BLACK);
                    g.drawString("Spiel Ende!", -70 + 1, -170 + 1);
                    g.setColor(Color.GREEN);
                    g.drawString("Spiel Ende!", -70, -170);

                    g.setColor(Color.BLACK);
                    g.drawString("Versuche:", -140, -140);
                    g.setFont(font);
                    for (int i = 0; i < Thread1.client_handlers.size(); i++) {
                        g.drawString("" + Thread1.client_handlers.get(i).playername + ":" + Thread1.client_handlers.get(i).schlage, -120, -110 + (i * 30));
                    }

                    g.transform(yFlip);
                }
            }
            if (isplayer) {
                g.setColor(Color.BLACK);
                if (ccn.showpoints) {
                    g.setColor(Color.BLACK.brighter());
                    g.fillRect(-150, -100, 300, 300);

                    g.setColor(backg_cl);
                    g.fillRect(-150, -100 + 2, 300 - 2, 300 - 2);

                    yFlip = AffineTransform.getScaleInstance(1, -1);
                    g.transform(yFlip);
                    g.setFont(font);

                    g.setFont(new Font("Arial", Font.BOLD, 23));
                    g.setColor(Color.BLACK);
                    g.drawString("Spiel Ende!", -70 + 1, -170 + 1);
                    g.setColor(Color.GREEN);
                    g.drawString("Spiel Ende!", -70, -170);
                    g.setColor(Color.BLACK);
                    g.drawString("Versuche:", -140, -140);
                    g.setFont(font);
                    for (int i = 0; i < ccn.playercount; i++) {

                        g.drawString("" + ccn.playernames[i] + ":" + ccn.points[i], -120, -110 + (i * 30));

                    }

                    g.transform(yFlip);
                }
            }

            g.transform(yFlip);

            g.drawImage(flagge, (int) builder.goal.p_x - 15, (int) -builder.goal.p_y - 80, 75, 75, null);
            g.transform(yFlip);

        } //End PlayingState

    }

    public void start() {
        // initialize the last update time
        this.last = System.nanoTime();
        // don't allow AWT to paint the canvas since we are
        this.canvas.setIgnoreRepaint(true);
        // enable double buffering (the JFrame has to be
        // visible before this can be done)
        this.canvas.createBufferStrategy(2);
        // run a separate thread to do active rendering
        // because we don't want to do it on the EDT
        Thread thread = new Thread() {
            public void run() {
                // perform an infinite loop stopped
                // render as fast as possible
                while (true) {
                    try {
                        Thread.sleep(8);
                    } catch (Exception e) {
                    }
                    gameLoop();
                    // you could add a Thread.yield(); or
                    // Thread.sleep(long) here to give the
                    // CPU some breathing room
                }
            }
        };
        // set the game loop thread to a daemon thread so that
        // it cannot stop the JVM from exiting
        thread.setDaemon(true);
        // start the game loop
        thread.start();
    }

    public void punkte_senden() {

        //Senden von Punkten and alle Clients
        String punkte_S = "POINTS:";
        for (int i = 0; i < Thread1.client_handlers.size(); i++) {
            ClientHandler cl_local = Thread1.client_handlers.get(i);
            punkte_S += Thread1.client_handlers.get(i).schlage + ":";
        }
        System.out.println(punkte_S);
        for (int i = 1; i < Thread1.client_handlers.size(); i++) {
            ClientHandler cl_local = Thread1.client_handlers.get(i);
            try {
                cl_local.out.writeObject(punkte_S);
            } catch (Exception e) {
            }
        }
    }

    public void spielstand() {
        Game_Over = true;
        Thread1.client_handlers.get(0).sv_showpoints = true;
        punkte_senden();
    }

    public void change_client_level() {
        switch (ccn.Level) {
            case 1:
                System.out.println("Level1");
                break; // Erstes Level der Welt Init
            case 2:
                System.out.println("Level2");
                builder.Level2(world);
                break;
            case 3:
                System.out.println("Level3");
                builder.Level3(world);
                break;
            case 4:
                System.out.println("Level4");
                builder.Level4(world);
                break;
            case 5:
                System.out.println("Level5");
                break;
        }
    }

    public void change_level() {
        //Builder
        Thread1.client_handlers.get(0).sv_level_index += 1; //Level Index erhöhen
        //Senden von ChangeLevel and alle Clients
        for (int i = 1; i < Thread1.client_handlers.size(); i++) {
            ClientHandler cl_local = Thread1.client_handlers.get(i);
            try {
                cl_local.out.writeObject("LEVEL:" + Thread1.client_handlers.get(0).sv_level_index);
            } catch (Exception e) {
            }
        }
        switch (Thread1.client_handlers.get(0).sv_level_index) {
            case 1:
                System.out.println("Level1");
                break; // Erstes Level der Welt Init
            case 2:
                System.out.println("Level2");
                builder.Level2(world);
                break;
            case 3:
                System.out.println("Level3");
                builder.Level3(world);
                break;
            case 4:
                System.out.println("Level4");
                builder.Level4(world);
                break;
            case 5:
                System.out.println("Level5");
                spielstand();
                break;
        }
    }

    public void reset_ball() {

        //Setzt ball andere Position und F goal falsch
        world.getBody(0).translateToOrigin();
        world.getBody(0).translate(builder.start_px, builder.start_py);
        Thread1.client_handlers.get(0).sv_fgoal = false;

        //Dieser Spieler
        System.out.println("Player: " + Thread1.client_handlers.get(0).sv_player_index); // Client now
        //Wenn Spieler = Server, HasTurn von Server = 0
        if (Thread1.client_handlers.get(0).sv_player_index == 0) {
            Thread1.client_handlers.get(0).has_turn = false;
        } else {   //Wenn nicht Server
            try { //Send STOPTURN to current client

                Thread1.client_handlers.get(Thread1.client_handlers.get(0).sv_player_index).out.writeObject("STOPTURN");
            } catch (Exception e) {
            }
        }

        // Nächster Spieler
        Thread1.client_handlers.get(0).sv_player_index += 1;

        // Erster Schlag
        Thread1.client_handlers.get(0).sv_schlag = 0;

        //Check IF Limit überschritten und setz zurück, Change Level
        if (Thread1.client_handlers.get(0).sv_player_index > Thread1.ID_Counter) {
            Thread1.client_handlers.get(0).sv_player_index = 0;
            change_level();
        }

        //Wenn Spieler = Server, HasTurn von Server = 1
        if (Thread1.client_handlers.get(0).sv_player_index == 0) {
            Thread1.client_handlers.get(0).has_turn = true;
        }

        try { //Send HASTURN to next client
            Thread1.client_handlers.get(Thread1.client_handlers.get(0).sv_player_index).out.writeObject("HASTURN");
        } catch (Exception e) {
        }

        //Sende SpielerNamen an alle
        for (int i = 1; i < Thread1.client_handlers.size(); i++) {
            ClientHandler cl_local = Thread1.client_handlers.get(i);
            try {
                cl_local.out.writeObject("PLAYER:" + Thread1.client_handlers.get(Thread1.client_handlers.get(0).sv_player_index).playername);
            } catch (Exception e) {
            }
        }

    }

    //Update Network Function
    public void update_network() {
        //Check ob Gewonnen
        if (builder.goal.update(this.world)
                && (world.getBody(0).getChangeInPosition().x + world.getBody(0).getChangeInPosition().y) < 0.005
                && (world.getBody(0).getChangeInPosition().x + world.getBody(0).getChangeInPosition().y) > -0.005) {
            //Ziel erreicht
            if (!Thread1.client_handlers.get(0).sv_fgoal) {
                Thread1.client_handlers.get(0).sv_fgoal = true;

                //Set CurrentClienthandler Versuche zu try count of server 
                Thread1.client_handlers.get(Thread1.client_handlers.get(0).sv_player_index).Punkte[Thread1.client_handlers.get(0).sv_player_index]
                        = Thread1.client_handlers.get(0).try_count;
                reset_ball();
                System.out.println("ZIEL ERREICHT");
            }
        }

        //Ist Ball Speed 0 dann can Spielen
        if ((world.getBody(0).getChangeInPosition().x + world.getBody(0).getChangeInPosition().y) < 0.003
                && (world.getBody(0).getChangeInPosition().x + world.getBody(0).getChangeInPosition().y) > -0.003) {

            Thread1.client_handlers.get(0).can_play = true;
            //System.out.println((world.getBody(0).getChangeInPosition().x+world.getBody(0).getChangeInPosition().y));
        } else { //Sonst kann nicht
            Thread1.client_handlers.get(0).can_play = false;
        }

        //Tick Pos X Y  und  CPL , schlag
        for (int i = 1; i < Thread1.client_handlers.size(); i++) {
            ClientHandler cl_local = Thread1.client_handlers.get(i);
            //cl_local.can_play = true;

            //Senden von Position an alle Clients
            try {
                cl_local.out.writeObject("TCK:" + this.world.getBody(0).getWorldCenter().x
                        + ":" + +this.world.getBody(0).getWorldCenter().y + ":" + Thread1.client_handlers.get(0).can_play + ":" + Thread1.client_handlers.get(0).sv_schlag);
            } catch (Exception e) {
            }

        }

        //
        //Ist Ein Clk wieder 0, kann wieder spielen
        //Server Ball Push Befehl
        if (Thread1.client_handlers.get(0).sv_pushed == true) {
            Thread1.client_handlers.get(0).sv_pushed = false;
            //Ball suchen
            if (Thread1.client_handlers.get(0).can_play && Game_Over == false) {
                SimulationBody ball = (SimulationBody) this.world.getBody(0);
                world_aim_point.x = -(Thread1.client_handlers.get(0).sv_ballx - (float) world.getBody(0).getWorldCenter().x * (float) 0.9) * push_force; //-1700 wegen ball position
                world_aim_point.y = -(Thread1.client_handlers.get(0).sv_bally - (float) world.getBody(0).getWorldCenter().y * (float) 0.9) * push_force;
                Thread1.client_handlers.get(0).sv_schlag += 1;

                ball.applyForce(world_aim_point);
                //Schlag hinzufügen Zu Punkte des Spielers
                Thread1.client_handlers.get(Thread1.client_handlers.get(0).sv_player_index).schlage += 1;

                //System.out.println("pushed:" +  world_aim_point.x + ":" + world_aim_point.y);
            }
        }

    } //End Network Function

    public void initialize_world() {
        // create the world
        world = new World();

        // Load First Level
        builder.Level1(this.world);

    }

    public void gameLoop() {
        // get the graphics object to render to
        Graphics2D g = (Graphics2D) this.canvas.getBufferStrategy().getDrawGraphics();

        // before we render everything im going to flip the y axis and move the
        // origin to the center (instead of it being in the top left corner)
        AffineTransform yFlip = AffineTransform.getScaleInstance(1, -1);
        AffineTransform move = AffineTransform.getTranslateInstance(500, -400);
        g.transform(yFlip);
        g.transform(move);

        // now (0, 0) is in the center of the screen with the positive x axis
        // pointing right and the positive y axis pointing up
        // render anything about the Example (will render the World objects)
        // get the current time
        long time = System.nanoTime();
        // get the elapsed time from the last iteration
        long diff = time - this.last;
        // set the last time
        this.last = time;
        // convert from nanoseconds to seconds
        double elapsedTime = diff / NANO_TO_BASE;
        // update the world with the elapsed time
        this.render(g, elapsedTime);

        // dispose of the graphics object
        g.dispose();

        // blit/flip the buffer
        BufferStrategy strategy = this.canvas.getBufferStrategy();
        if (!strategy.contentsLost()) {
            strategy.show();
        }

        // Sync the display on some systems.
        // (on Linux, this fixes event queue problems)
        Toolkit.getDefaultToolkit().sync();

        // update the World
        if (In_Game == true) {
            this.world.update(elapsedTime);
        }

    }

    public static void main(String[] args) {
        main1 = new Main();
    }

//Unused Implements..
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}
