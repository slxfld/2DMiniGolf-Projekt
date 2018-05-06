package Main_Spiel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

public class GameButton {

	private int x;
	private int y;
	
	private int inp_x;
	private int inp_y;
	
	private int width;
	private int height;
	
	public boolean enabled;
	public boolean pressed;
	public boolean was_pressed = false;
	
	private String text;
	private final Font font = new Font("Verdana", Font.PLAIN, 18);
	private ActionListener listener;
	
	public GameButton(ActionListener listener, String text, int x,int y,int width,int height,int inp_x,int inp_y){
		
		this.listener = listener;
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.inp_x = inp_x;
		this.inp_y = inp_y;
		enabled = true;
	}
	
	public void render(Graphics g){
		
		if(pressed){
			g.setColor(Color.green);
			was_pressed = true;
			
		} else {
			g.setColor(Color.gray.darker().darker());
		}
	
	
	if(enabled){
		g.setColor(Color.BLACK);
		g.fillRect(x,y-height/2,width,height);
		g.setColor(Color.gray.darker());
		if(pressed){ //Draw UnElevated
			g.fillRect(x,y-height/2,width,height);
		}else{ 		//Draw Elevated
			g.fillRect(x - 3,y-height/2 -3,width,height);
		}
	    Graphics2D g5 = (Graphics2D) g;
	    AffineTransform originalTransform = g5.getTransform();
	    g5.setStroke(new BasicStroke(2));
		//g.setColor(Color.BLACK);
		//g.fillRect(x,y-height/2,width,height);
	    
	    g5.setTransform(originalTransform);
	    
		g.setFont(font);
		g.setColor(Color.WHITE);
		int stringWidth = g.getFontMetrics().stringWidth(text);
		if(pressed){
			g.drawString(text,x+width/2-stringWidth/2,y + 10);
		}else{
			g.drawString(text,x+width/2-stringWidth/2-5,y + 10-5);
		}

		
	   }
	}
	
	private boolean isPressed(int x, int y){
		return x > inp_x && x < inp_x + width && y > inp_y -27&& y < inp_y + height-9;
	}

	public void mousePressed(MouseEvent e){
		if(isPressed(e.getX(), e.getY())){
			pressed = true;
			System.out.println("Pressed!");
		}
	}

	public void mouseReleased(MouseEvent e){
		if(pressed && enabled){
			listener.actionPerformed(new ActionEvent(this, height, text));
			pressed = false;
		}
	}
	
}




