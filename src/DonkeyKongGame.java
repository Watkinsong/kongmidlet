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

/**
 * Class DonkeyKongGame
 *
 * Responsible of starting, running and suspending the game. Also in here are the images and
 * audios loaded, so they will be ready when the game starts.
 * Class implements the required methods inherited from class MIDlet to maintain and monitor
 * top-level actions.
 */
public class DonkeyKongGame extends MIDlet implements Finals
{
    /**
     *  The display in use.
     */
    private Display display;
    
    /**
     * The link to the gamecanvas-class
     */
    private DonkeyKongGameCanvas gameCanvas;
    /**
     * The menu used to navigate to the game and away
     */
    private MenuScreen menuscreen;

    
    /**
     * Constructor.
     *
     * The game canvas object and the menu canvas objects are created.
     */
    public DonkeyKongGame()
    {
        this.menuscreen = new MenuScreen(this);
        this.gameCanvas = new DonkeyKongGameCanvas(this);
    }
    
    /**
     * Changing the current state of the menu.
     * Determined by the user input given in the menu (or in the game)
     * a new game is started, the application is closed or the menu screen is
     * brought to sight.
     * 
     * @param state
     */
    public void changeState( int state )
    {
        if( state == NEWGAME_STATE )
        {
            gameCanvas.startGame();
            display.setCurrent(gameCanvas);
        }
        else if ( state == EXIT_STATE )
        {
            notifyDestroyed();
        }
        else if ( state == MENUSCREEN_STATE )
        {
            display.setCurrent(menuscreen);
        }
    }
    
    /**
     * Where the application starts.
     * Fetching all the external resources is done by using a method initialize on
     * class DonkeyKongGameCanvas.
     * The mobile phones display is also taken into use.
     * 
     * @see initialize
     */
    public void startApp()
    {
        gameCanvas.initialize();
        // Reference to the screen
        display = Display.getDisplay(this);
        display.setCurrent( menuscreen );
        //display.setCurrent( this.gameCanvas );
    }
    
    /**
     * In this method one should implement the manoveurs needed when the application
     * closes - in this game there aren't any.
     *
     * @param unconditional - If true when this method is called, the MIDlet must 
     *  cleanup and release all resources. If false the MIDlet may throw MIDletStateChangeException 
     *  to indicate it does not want to be destroyed at this time. 
     */
    public void destroyApp(boolean unconditional)
    {
        // Do nothing
    }
    
    /**
     * Method where one should implement functionality when the game
     * is paused due to a call etc.
     */
    public void pauseApp()
    {
        // Do nothing
    }

} // End of class DonkeyKongGame