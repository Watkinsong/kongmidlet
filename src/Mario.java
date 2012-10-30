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
 * class Mario
 * 
 * This class holds the variables and methods to create and move Mario.
 * Mario-class inherits class Sprite and uses it's methods alike.
 * 
 * @author Arttu Salonen
 *
 */
public class Mario extends Sprite implements Finals
{
	/**
	 * The direction where Mario is being moved.
	 */
    private int direction;
    /**
     * The last command that Mario perfomed. This variable is used to exclude some
     * actions, like checking for free space below when climbing a ladder.
     */
    private int lastCommand;
    /**
     * A variable depicting whether Mario is currently performing a jump.
     */
    private boolean jumping;
    /**
     * A helping variable used to store Mario's x-location during jump.
     */
    private int jumpX;
    /**
     * A helping variable used to store Mario's y-location during jump.
     */
    private int jumpY;
    /**
     * Reference to the DonkeyKongGameCanvas that holds this object.
     */
    private DonkeyKongGameCanvas canvas;
    
    /**
     * The constructor of class Mario.
     * 
     * In the constructor the local attributes and needed values are initialized
     * properly.
     * 
     * @param image       The image of the Sprite.
     * @param frameWidth  The width of the frame.
     * @param frameHeight The height of the frame.
     * @param canvas      The DonkeyKongCanvas in use.
     */
    public Mario( Image image, int frameWidth, int frameHeight,
                  DonkeyKongGameCanvas canvas)
    {
        super(image, frameWidth, frameHeight);

        this.direction   = LEFT; // Mario is facing left when the game begins.
        this.lastCommand = -1;
        this.jumping     = false;
        this.jumpX       = 0;
        this.jumpY       = 0;
        this.canvas      = canvas;
        this.setPosition( 50, 202 );
        this.defineCollisionRectangle(11, 14, 187, 3);
        this.defineReferencePixel(8, 6);
    }
    
    /**
     * Sets a new direction.
     * 
     * @param direction New direction.
     */
    public void setDirection( int direction )
    {
        this.direction = direction;
    }

    /**
     * Gets the current direction.
     * 
     * @return The current direction.
     */
    public int getDirection()
    {
        return this.direction;
    }
    
    /**
     * Sets a new last command.
     * 
     * @param command The last command.
     */
    public void setLastCommand( int command )
    {
        this.lastCommand = command;
    }
    
    /**
     * Gets the last command.
     * 
     * @return The last command.
     */
    public int getLastCommand()
    {
        return this.lastCommand;
    }
    
    /**
     * Sets a new value for the value used to tell that Mario is performing a jump.
     * 
     * @param value New value.
     */
    public void setJumping( boolean value )
    {
        this.jumping = value;
    }
    
    /**
     * Gets the value of Mario's status in the context of jumping.
     * 
     * @return true, if a jump is going on, else false.
     */
    public boolean getJumping()
    {
        return this.jumping;
    }
    
    /**
     * Sets a new value for the helping variable.
     * 
     * @param x New value.
     */
    public void setJumpX( int x )
    {
        this.jumpX = x;
    }
    
    /**
     * Gets the value of the helping variable.
     * 
     * @return The value of the variable JumpX.
     */
    public int getJumpX()
    {
        return this.jumpX;
    }
    
    /**
     * Sets a new value for the helping variable.
     * 
     * @param y New value.
     */
    public void setJumpY( int y )
    {
        this.jumpY = y;
    }
    
    /**
     * Gets the value of the helping variable.
     * 
     * @return The value of the variable JumpY.
     */
    public int getJumpY()
    {
        return this.jumpY;
    }
    
    /**
     * Takes Mario back to the starting point, where he must be when the
     * level starts.
     *
     */
    public void setToStartingPoint()
    {
        setJumping(false);
        setFrame(0);
        setPosition(50, 202);
        setLastCommand(-1);
    }
    
    /**
     * Selecting the proper frame to be shown.
     * The frame is transformed when Mario is facing the opposite direction than
     * the images in the Sprite's image.
     *
     */
    public void selectFrame()
    {
        this.setTransform(Sprite.TRANS_NONE);

            if ( this.getDirection() == LEFT )
            {
                if ( this.getFrame() == 0 || this.getFrame() == 1 )
                {
                    this.nextFrame();
                }
                else if ( this.getFrame() == 2 )
                {
                    this.setFrame(1);                
                }
                else
                {
                    this.setFrame(1);
                }
            }
            else if ( this.getDirection() == RIGHT )
            {
                if ( this.getFrame() == 0 || this.getFrame() == 1 )
                {
                    this.setTransform(Sprite.TRANS_MIRROR);
                    this.nextFrame();
                }
                else if ( this.getFrame() == 2 )
                {
                    this.setTransform(Sprite.TRANS_MIRROR);
                    this.setFrame(1);                  
                }
                else
                {
                    this.setFrame(1);
                }
            }
            else if ( this.getDirection() == UP )
            {
                    if ( this.getFrame() == 3 )
                    {
                        this.setFrame(4);
                    }
                    else
                    {
                        this.setFrame(3);
                    }
            }
            else if ( this.getDirection() == DOWN )
            {
                    if ( this.getFrame() == 3 )
                    {
                        this.setFrame(4);
                    }
                    else
                    {
                        this.setFrame(3);
                    }
            }
            else if ( this.getDirection() == JUMP )
            {
                if ( this.getLastCommand() == RIGHT )
                {
                    this.setTransform(Sprite.TRANS_MIRROR);
                }
                
                this.setFrame(2);
            }
    }
    
    /**
     * A method to move vertically across the playing area.
     * Because the majority of the level is slope, every move only to the left
     * or to the right could cause Mario to appear inside the path, rather than on
     * the surface of it. That is why the outcome of every requested movement must be
     * properly examined and reacted to when needed. This method performs the following
     * actions:
     * <ul>
     * <li>Setting the checker Sprite to the same position where Mario currently is.</li>
     * <li>Performing the desired move, defined by the parameter.</li>
     * <li>Examining whether Mario is inside the walking path. If so, Mario is trying to
     *     move uphill and it's clear that Mario will appear too deep and so the move to
     *     be performed is forward and up, out of the walking path.</li>
     * <li>In the case where Mario is not inside the walking path, the direction must be
     *     downhill, and so the checker-Sprite is used to count how much free space there
     *     is below Mario. Then the move will be forward and down, so that Mario will be
     *     standing on the walking path.</li>
     * <li>Finally, after the move another check is being performed. This is because mobile
     *     phones have somewhat great alteration on the collision detection, and by double
     *     checking Mario's position there can be more confidence that Mario will not
     *     appear inside the walking path</li>
     *</ul>
     *In addition to moving vertically this method is also responsible for performing the jump.
     *The jumping is done by first selecting the direction where will be jumped and then
     *increasing the x and y coordinates until a top-spot of the jump is reached and then 
     *decreasing the same values until colliding with the ground.
     * 
     * @param command The command what will be performed.
     */
    public void moveVertically( int command )
    {
        int x          = 0;
        int y          = 0;
        int freepixels = 0;
        
        Sprite checker = this.canvas.getCheckerSprite();
        checker.setVisible(true);
        checker.setPosition( this.getX(), this.getY() );
        
        switch( command )
        {
            case LEFT:
            
                checker.move(-4, 0);
            
                if( checker.collidesWith( this.canvas.getBackgroundLayer(), true ) )
                {
                    x = -4;
                    y = -1;
                }
                else
                {
                    while( !checker.collidesWith( this.canvas.getBackgroundLayer(), true ) )
                    {
                        checker.move(0, 1);
                        freepixels++;
                        
                        if( checker.collidesWith( this.canvas.getBackgroundLayer(), true ) )
                        {
                            break;
                        }
                    }
                    
                    x = -4;
                    y = freepixels-1;

                }
                break;
            
            case RIGHT:
            
                checker.move(4, 0);
            
                if( checker.collidesWith( this.canvas.getBackgroundLayer(), true ) )
                {
                    x = 4;
                    y = -1;
                }
                else
                {
                    while( !checker.collidesWith( this.canvas.getBackgroundLayer(), true ) )
                    {
                        checker.move(0, 1);
                        freepixels++;
                        
                        if( checker.collidesWith( this.canvas.getBackgroundLayer(), true ) )
                        {
                            break;
                        }
                    }
                    
                    x = 4;
                    y = freepixels-1;
                }
                break;
                
            case JUMP:
            
                this.setJumping(true);

                if ( this.getLastCommand() == RIGHT )
                {
                    this.setJumpX(3);
                    x = 3;
                }
                else if ( this.getLastCommand() == LEFT )
                {
                    this.setJumpX(-3);
                    x = -3;
                }
                else
                {
                    x = this.getJumpX();
                }
                
                if ( this.getJumpY() == 0 )
                {
                    this.setJumpY( this.getJumpY() + 1 );
                    y = -5;
                }
                else
                {
                    if ( this.getJumpY() >= 20 )
                    {
                        y = this.getJumpY() + 5;
                    }
                    else
                    {
                        this.setJumpY( this.getJumpY() + 1 );
                        y = this.getJumpY() - 5;
                    }
                }
                
                checker.move(x,  y);
                if( checker.collidesWith( this.canvas.getBackgroundLayer(), true ) )
                {
                    while ( checker.collidesWith( this.canvas.getBackgroundLayer(), true ) )
                    {
                        checker.move(0, -1);
                        freepixels++;
                        
                        if( checker.collidesWith( this.canvas.getLadderLayer(), true ) )
                        {
                            break;
                        }
                    }
                        
                    x = 0;
                    y = freepixels-1;
                    this.setJumpX(0);
                    this.setJumpY(0);
                    this.setJumping(false);
                    this.setLastCommand(JUMP);
                }
                
                break;
                
            default:
            
                checker.move(0, 1);
                
                if( checker.collidesWith( this.canvas.getBackgroundLayer(), true )
                    ||
                    checker.collidesWith( this.canvas.getLadderLayer(), true ) )
                {
                    x = 0;
                    y = 0;
                }
                else
                {
                    while( !checker.collidesWith( this.canvas.getLadderLayer(), true )
                           &&
                           !checker.collidesWith( this.canvas.getBackgroundLayer(), true ) )
                    {
                        checker.move(0, 1);
                        freepixels--;
                        
                        if( checker.collidesWith( this.canvas.getLadderLayer(), true ) )
                        {
                            break;
                        }
                    }
                    
                    x = 0;
                    y = freepixels-1;
                }
            
        }
        
        checker.defineCollisionRectangle(11, 15, 22, 2);
        checker.defineReferencePixel(8, 6);
        checker.move(x, y);
        
        freepixels = 0;
        
        if ( checker.collidesWith( this.canvas.getBackgroundLayer(), true ) )
        {
            while( checker.collidesWith( this.canvas.getBackgroundLayer(), true ) )
            {
                checker.move(0, -1);
                freepixels++;
                
                if( checker.collidesWith( this.canvas.getBackgroundLayer(), true ) )
                {
                    break;
                }
            }
            
            y = y - freepixels + 1;
        }
        
        checker.setVisible(false);
        this.move(x, y);
        
    }
    
    /**
     * A method to examine the desired movement's rightness and to call for 
     * methods to set a new last command and to select a frame.
     * Moving across stairs is performed also here, and the collision detection in
     * those movements are performed by expanding Mario's collision rectangel temporarily.
     * Again this manoveur is forced by mobile phones' different interpretations of
     * colliding.
     */
    public void moveMario()
    {
        if ( this.getDirection() == JUMP )
        {
            if ( this.getLastCommand() == LEFT || this.getLastCommand() == RIGHT 
                 || this.getLastCommand() == -1 )
            {
                this.moveVertically( JUMP );
                
                if( this.getLastCommand() != JUMP )
                {
                    this.selectFrame();
                }
            }
        }
        else if ( this.getDirection() == LEFT 
             &&
             this.getX() > 4 ) // Mario can't walk over the edges of the path.
        {
            this.moveVertically( LEFT );
            this.selectFrame();
            this.setLastCommand(LEFT);
        }
        else if ( this.getDirection() == RIGHT 
                  &&
                  this.getX() < 217 )
        {
            this.moveVertically( RIGHT );
            this.selectFrame();
            this.setLastCommand(RIGHT);
        }
        else if ( this.getDirection() == UP )
        {
                this.defineCollisionRectangle(0, 0, this.getWidth(), this.getHeight() );
                
                if ( this.collidesWith ( this.canvas.getLadderLayer(), true ) )
                {
                    this.move(0, -3);
                    this.selectFrame();
                    this.setLastCommand( UP );
                }
                
                this.defineCollisionRectangle(11, 14, 187, 3);
        }
        else if ( this.getDirection() == DOWN )
        {
                this.defineCollisionRectangle(0, 0, this.getWidth(), this.getHeight() );
        
                if ( this.collidesWith ( this.canvas.getLadderLayer(), true ) )
                {
                    this.move(0, 3);
                    this.selectFrame();
                    this.setLastCommand( DOWN );
                }
                
                this.defineCollisionRectangle(11, 14, 187, 3);
        }
        else
        {
            if ( this.getLastCommand() == LEFT || this.getLastCommand() == RIGHT )
            {
                this.setFrame(0);
            }
        }

    }

} // End of class Mario