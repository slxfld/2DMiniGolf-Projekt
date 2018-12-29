
package Main_Spiel;

import static Main_Spiel.Main.SCALE;
import java.awt.Canvas;
import org.dyn4j.dynamics.World;

public class Ziel {
    
    double p_x;
    double p_y;
    public Ziel(double i_px, double i_py,World world1,Canvas canvas){
        p_x = i_px;
        p_y = i_py;
            p_x =  (i_px - canvas.getWidth() * 0.5) / (SCALE);
            p_y = -(i_py - canvas.getHeight() * 0.5) / (SCALE);
        System.out.println(p_x + "||" + p_y);
        
    }
    public boolean update(World world1){
        if(world1.getBody(0).getWorldCenter().x*(SCALE-5) > p_x &&
           world1.getBody(0).getWorldCenter().x*(SCALE-5) < p_x+40.5 &&
           world1.getBody(0).getWorldCenter().y*(SCALE-5) > p_y &&
           world1.getBody(0).getWorldCenter().y*(SCALE-5) < p_y+40.5
        ){System.out.println("Collision");  return true;   }
        else
        {
            return false;
        }
    }
    
    
    public void draw(){
        
    }
    
}
