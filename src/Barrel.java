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
 * Class Barrel
 * 
 * A class that is used to create barrels seen on the game and that holds
 * methods to examine and change the state of a barrel.
 * Class inherits class Sprite and uses its methods alike.
 * 
 * @author Arttu Salonen
 *
 */
public class Barrel extends Sprite implements Finals
{
	/**
	 * To check if the barrel at hand is falling down stairs
	 */
    private boolean falling;
    /**
     * A counter used to examine whether the barrel passes a ladder leading
     * downstairs on it's path and should it fall down from there. This variable
     * is also used to check should the barrel fall down from the starting place
     * or normally start moving vertically.
     */
    private int ladderDown;
    /**
     * The level which barrel is on.
     */
    private int level;
    /**
     * The direction where the barrel is moving.
     */
    private int direction;
    /**
     * The direction where the barrel was moving before the direction it is moving
     * now.
     */
    private int lastDirection;
    /**
     * A counter that is used to when a barrel is falling down in stairs and it shouldn't
     * react to the normal vertical pathway.
     */
    private int fallingLadders;
    /**
     * The link to the DonkeyKongGameCanvas this barrel is drawn to.
     */
    private DonkeyKongGameCanvas canvas;
    
    /**
     * The constructor. Here all the classes variables are initiated to proper values.
     * 
     * @param image       The image of the Sprite.
     * @param frameWidth  The width of a frame.
     * @param frameHeight The height of a frame.
     * @param canvas      The DonkeyKongGameCanvas in use.
     */
    public Barrel( Image image, int frameWidth, int frameHeight, DonkeyKongGameCanvas canvas)
    {
        super(image, frameWidth, frameHeight);
        
        this.falling        = false;
        this.canvas         = canvas;
        this.level          = 6;
        this.falling        = falling;
        this.direction      = RIGHT;   // Barrels always start rolling to right
        this.fallingLadders = -1;
        this.defineCollisionRectangle(9, 9, 1, 1); // Setting the collision rectangle to the bottom
        this.setToStartingPosition();
        this.setVisible(false); // Barrel is not shown before it is set to move
    }

    /**
     * Placing the barrel to it's starting position and setting the values that have impact on it's
     * movement to appropiate values.
     */
    public void setToStartingPosition()
    {
        this.setPosition(23, 61);
        this.setVisible(true);
        this.setDirection(RIGHT);
        this.ladderDown = this.canvas.getRandom().nextInt(15 - canvas.getLevel() );

        if ( ladderDown  == 12 ) // Approximately one of fifteen barrels will start falling down directly
        {
            this.falling = true;
        }
    }
    
    /**
     * Changing the level of the barrel.
     * Since barrels try constantly to the bottom of the area the level is simply reduced.
     */
    public void changeLevel()
    {
        switch (this.level)
        {
            case LEVEL6:
                this.level = LEVEL5;
                this.setDirection(LEFT);
                break;
            case LEVEL5:
                this.level = LEVEL4;
                this.setDirection(RIGHT);
                break;
            case LEVEL4:
                this.level = LEVEL3;
                this.setDirection(LEFT);
                break;
            case LEVEL3:
                this.level = LEVEL2;
                this.setDirection(RIGHT);
                break;
            case LEVEL2:
                this.level = LEVEL1;
                this.setDirection(LEFT);
                break;
            case LEVEL1:
                this.level = LEVEL6;
                this.setDirection(RIGHT);
                break;
            default:
                this.level = LEVEL6;
                this.setDirection(RIGHT);
                break;
        }
    }
    
    /**
     * Gets the level of the barrel.
     * 
     * @return The level of the barrel.
     */
    public int getLevel()
    {
        return this.level;
    }
    
    /**
     * Sets a new value for the variable used to check if the barrel is falling down
     * instead of moving to left or right.
     * 
     * @param falling new value for the variable
     */
    public void setFalling( boolean falling )
    {
        this.falling = falling;
    }
    
    /**
     * Gets the boolean value of the barrel's variable that is used to mark that the barrel
     * is falling down.
     * 
     * @return true if the barrel is falling down, else false.
     */
    public boolean getFalling()
    {
        return this.falling;
    }
    
    /**
     * Gets the approximately value of how many ladders , or any, the barrel will pass before
     * falling down one of them. The returned value is not specific, because the value of this
     * variable is reduced by more than one ever time the barrel contacts with a ladder leading
     * down.
     * 
     * @return The current value of ladders to be passed.
     */
    public int getLadderDown()
    {
        return this.ladderDown;
    }
    
    /**

     * 
     * Gets the value of how many moves a barrel has moved down in a ladder.
     * 

     * @return Times the barrel has moved down in a ladder.
     */
    public int getFallingLadders()
    {
        return this.fallingLadders;
    }
    
    /**
     * Sets a new value for the variable telling how many times the barrel
     * has fallen on a ladder
     * 
     * @param number New value for the variable
     */
    public void setFallingLadders( int number )
    {
        this.fallingLadders = number;
    }
    
    /**
     * Creates a new value for the variable that is used to determine whether the
     * barrel should fall down stairs. This method is used when the barrel has
     * fallen down a ladder so it  gets a new value and could fall again if possible.
     * The value is generated randomly.
     */
    public void changeLadderDown()
    {
        if ( this.ladderDown == 0 )
        {
            this.ladderDown = this.canvas.getRandom().nextInt(15);
        }
        else
        {
            this.ladderDown--;
        }
    }
    
    /**
     * Sets a new direction to the barrel.
     * 
     * @param direction The new direction.
     */
    public void setDirection( int direction )
    {
        if ( direction == LEFT || direction == RIGHT || direction == DOWN )
        {
            this.direction = direction;
        }
    }
    
    /**
     * Return the direction where the barrel is moving.
     * 
     * @return The direction of the barrel.
     */
    public int getDirection()
    {
        return this.direction;
    }
    
    /**
     * Selects a proper frame for the barrel-sprite. 
     *
     */
    public void selectFrame()
    {
    	// Barrel falling down has only one frame to use
        if ( this.getDirection() == DOWN || this.getFalling() )
        {
            this.setFrame(4);
        }
        else
        {
            if ( this.getFrame() == 3 )
            {
                this.setFrame(0);
            }
            else
            {
                this.nextFrame();
            }
        }
    }
    
    /**
     * Moves the barrel according to the direction, level and value of ladders to be
     * passed before falling down. 
     *
     */
    public void moveBarrel()
    {
        int x = 0;
        int y = 0;
    
        // A ladder that is simply falling down doesn't need much examining
        if ( !this.getFalling() )
        {
            if ( this.getDirection() == DOWN )
            {
                y = 3;
                
                if ( this.getFallingLadders() == 0 ) // Barrel has fallen down the top of ladder
                {
                    this.setFallingLadders(-1);
                }
                else if ( this.getFallingLadders() > 0 ) // Barrel is still on the top of the ladder, falling down
                {
                    this.setFallingLadders( this.getFallingLadders() - 1 );
                }
            }
            else if ( this.collidesWith( this.canvas.getLadderLayer(), true ) ) // A check that prevents the barrel from
            {                                                                   // moving inside the path, rather than above
                this.move(0, -4 );                                              // it.
                
                if ( !this.collidesWith( this.canvas.getLadderLayer(), true ) )
                {
                    if ( this.getLadderDown() == 0 )
                    {
                        x = 0;
                        y = 3;
                        this.setDirection( DOWN );
                        this.setFallingLadders( 3 );
                    }
                    else
                    {
                        if ( this.getDirection() == RIGHT )
                        {
                            x = 3;
                            y = 1;
                        }
                        else
                        {
                            x = -3;
                            y = 1;
                        }
                    }
                    this.changeLadderDown();
                }
                else
                {
                    if ( this.getDirection() == RIGHT )
                    {
                        x = 3;
                        y = 1;
                    }
                    else
                    {
                        x = -3;
                        y = 1;
                    }
                }
                
                this.move(0, 4);
            }
            else
            {
                if ( this.getDirection() == RIGHT )
                {
                    x = 3;
                    y = 1;
                }
                else if ( this.getDirection() == LEFT )
                {
                    x = -3;
                    y = 1;
                }
            }
            
            this.move(x, y);

            if ( this.getFallingLadders() < 0 )
            {
                if ( !this.collidesWith( this.canvas.getBackgroundLayer(), true ) )
                {
                    this.move(0, 1);
                    
                    if ( !this.collidesWith( this.canvas.getBackgroundLayer(), true ) )
                    {
                        this.setDirection( DOWN );
                    }
                    
                }
                else
                {
                    if ( this.getDirection() != DOWN )
                    {
                        this.move(0, -1);
                    }
                    else
                    {
                        if ( this.getDirection() == DOWN )
                        {
                            this.changeLevel();
                            this.setFallingLadders(-1);
                        }
                        this.move(0, -1);
                    }
                }
                
            }
            
        }
        else
        {
            this.move(0, 4);
            
            // Moving the barrel a bit upwards to create a bouncing animation
            if( this.collidesWith( this.canvas.getBackgroundLayer(), true ) )
            {
                this.move(0, -2);
            }
        }
        
        this.selectFrame();
    }
    

} // End of class Barrel