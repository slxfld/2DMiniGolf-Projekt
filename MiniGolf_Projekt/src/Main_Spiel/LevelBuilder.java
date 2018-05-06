/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main_Spiel;

import static Main_Spiel.Main.SCALE;
import java.awt.Canvas;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.samples.SimulationBody;

/**
 *
 * @author SLZX
 */
public class LevelBuilder {
    
    //Construction World
    public World c_world;
    public Ziel goal;
    public Canvas canvas1;
    public double start_px;
    public double start_py;
    public int LevelNumber = 0;
    LevelBuilder(Canvas canvas){
        canvas1 = canvas;
    }
    
    // Wand Funktion Welt , Breite , Höhe , Position X , Position Y, Rotation
        public void add_wall(World world1,float size_x,float size_y,float pos_x,float pos_y,float rotate){
                // create the Object
		SimulationBody wallr = new SimulationBody();
		wallr.addFixture(Geometry.createRectangle(size_x, size_y));
		wallr.translate(pos_x, pos_y);
		wallr.setMass(MassType.INFINITE);
                wallr.rotateAboutCenter(rotate);
		world1.addBody(wallr);
    }
    
    public void reset_world(World world1){
       // c_world.removeAllBodies();
        world1.setGravity(new Vector2(0,0));
        world1.removeAllBodies();
    }
    
    private void ball_hinzu(World worldx,double startbx,double startby){
           SimulationBody ball1 = new SimulationBody();
		ball1.addFixture(Geometry.createCircle(0.2175),
				217.97925, 
				0.08,
				0.9);
		ball1.translate(startbx,startby);
		
		ball1.setLinearVelocity(0, 0);	
                ball1.setLinearDamping(1); 
		ball1.setMass(MassType.NORMAL);
		worldx.addBody(ball1);
       }
    
        public void Level1(World world1){
        reset_world(world1);
        world1.setGravity(new Vector2(0,0));
        start_px = -7.0;
        start_py = -5.0;
                ball_hinzu(world1,start_px,start_py);
            //WÄnde
                add_wall(world1,1f,15,-9,0,0);
                add_wall(world1,1f,10,-3,-3,0);
                add_wall(world1,15f,1f,-1f,7,0);
                add_wall(world1,10f,1f,1.5f,2,0);
                add_wall(world1,1f,6f,7f,4.5f,0);
                add_wall(world1,7f,1f,-6f,-7.5f,0);
                
                goal = new Ziel(200*SCALE,-200*SCALE,world1,canvas1);
        
    }
        
        
        public void Level2(World world1){
        reset_world(world1);
        start_px = -0;
        start_py = -7.0;
                ball_hinzu(world1,start_px,start_py);
            //Wände
                add_wall(world1,1f,17,-4,0,0);
                add_wall(world1,1f,17,4,0,0); //Erklärung (Size_,Size_Pos_x,Pos_y)
                add_wall(world1,10f,1,0,-9,0);
                add_wall(world1,10f,1,0,9,0);
            //Vertikale Balken
                add_wall(world1,5f,1,1,-3,0);
                add_wall(world1,5f,1,-1,3,0);
                
                goal = new Ziel(-100*(SCALE - 5),-200*(SCALE - 5),world1,canvas1);
        
    }
        
       public void Level3(World world1){
        reset_world(world1);
        start_px = -8.0;
        start_py = 7.0;
                ball_hinzu(world1,start_px,start_py);
                //Wand Lang 1
                add_wall(world1,1f,20f,-3.2f,0.5f,1.1f);
                //Wand Lang 2
                add_wall(world1,1f,24f,2f,6f,1.1f);
                //Wand Links Oben 
                add_wall(world1,1f,11f,-9.5f,9.2f,-0.5f);
                //Wand Rechts Ende
                add_wall(world1,1f,12f,12f,-4.4f,0f);
                //Wand Unten
                add_wall(world1,30f,1f,0f,-9.6f,0f);
                //Wand Oberhalb Unten
                add_wall(world1,20f,1f,-4.5f,-4f,0f);
                //End Wand
                add_wall(world1,1f,7f,-12f,-7f,0f);
                
                goal = new Ziel(-420*SCALE,240*SCALE,world1,canvas1);
        
    }
        
       public void Level4(World world1){
           LevelNumber = 4;
        reset_world(world1);
        start_px = 10.0;
        start_py = -6.0;
                ball_hinzu(world1,start_px,start_py);
                //Wand
                add_wall(world1,30f,1f,-1f,3.5f,0f);
                add_wall(world1,30f,1f,-1f,-8.5f,0f);
                
                add_wall(world1,20f,1f,3f,-2.5f,0f);
                
                add_wall(world1,1f,12f,-12f,-2.5f,0f);
                add_wall(world1,1f,12f,12f,-2.5f,0f);
                
                //Links Oben Rotiert
                add_wall(world1,1f,6f,-10f,1.9f,2.1f);
                
                //Links Unten Rotiert
                add_wall(world1,1f,6f,-10f,-6.9f,-2.1f);
                
                
                goal = new Ziel(400*SCALE,-40*SCALE,world1,canvas1);
        
    } 
       
       
    
}

