/**
 * DonkeyKongGame - a mobile game
 *
 *
 * @author     Arttu Salonen
 * @copyright  2008 Arttu Salonen
 * @version    2008-10-07
 */

import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.*;
import java.util.*;
import java.io.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.game.*;

/**
 * class MenuScreen
 * 
 * This class is for creating the main menu of the game.
 * Class also contains methods to pass information to the DonkeyKongGame-class
 * to start the game or to exit the game.
 * 
 * @author Arttu Salonen
 *
 */
public class MenuScreen extends Canvas implements Finals
{
	/**
	 * The state that will be forwarded to class DonkeyKongGame
	 */
    private int state;

    /**
     * The logo image.
     */
    private Image logo;
    
    /**
     * Reference to the DonkeyKongGame-object hosting this class' object.
     */
    private DonkeyKongGame game;
    
    /**
     * The constructor.
     * 
     * In the constructor the variables are initiatied. Also here the logo
     * image is fetched.
     * 
     * @param game DonkeyKongGame-object in use
     */
    public MenuScreen( DonkeyKongGame game )
    {
    	this.state = NEWGAME_STATE;
    	
        try
        {
            logo = Image.createImage("/logo.png");
        }
        catch ( Exception e )
        {
        
        }
        
        this.game = game;
    }

    /**
     * Method to paint the canvas. The menu's options
     * are colored depending whether the are selected.
     * 
     * @param g Graphics-object.
     */
    public void paint( Graphics g)
    {
        g.setColor(0,0,0);
        g.fillRect(0,0,getWidth(), getHeight() );
        g.drawImage(logo, getWidth()/2, 50, Graphics.TOP|Graphics.HCENTER);
        Font f = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
        g.setFont(f);
        if( state == NEWGAME_STATE )
        {
            g.setColor(87,90,230);
        }
        else
        {
            g.setColor(255,255,255);
        }
        g.drawString("NEW GAME", getWidth()/2, 180, Graphics.TOP|Graphics.HCENTER);
        
        if ( state == EXIT_STATE )
        {
            g.setColor(87,90,230);
        }
        else
        {
            g.setColor(255,255,255);
        }
        g.drawString("EXIT", getWidth()/2, 200, Graphics.TOP|Graphics.HCENTER);
        
    }
    
    /**
     * Changing the state of the variable.
     *
     */
    public void changeState()
    {
        if ( state == NEWGAME_STATE )
        {
            state = EXIT_STATE;
        }
        else
        {
            state = NEWGAME_STATE;
        }
    }
    
    /**
     * A method to examine user inputs on mobile phone. 
     * 
     * @param keycode The corresponding value to the key that has been pressed.
     */
    public void keyPressed( int keycode )
    {
		if(getGameAction(keycode) == Canvas.DOWN) {
            changeState();
			repaint();
		}
		else if(getGameAction(keycode) == Canvas.UP) {
            changeState();
			repaint();
		}
		else if(getGameAction(keycode) == Canvas.FIRE) {
			game.changeState( state );
		}
    }
    

} // End of class MenuScreen