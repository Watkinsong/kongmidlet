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
 * Class Kong
 * 
 * A class to create Donkey Kong and to make the object move and throw barrels.
 * The barrels are given to this class in an array at the constructor, so the barrels
 * can be created elsewhere.
 * 
 * @author Arttu Salonen
 *
 */
public class Kong extends Sprite implements Finals
{
	/**
	 * The index number of the barrel-array where the barrel that is just being thrown
	 * lies. This index is used when the barrel is a falling-type barrel that falls
	 * directly down and therefore demands a different frame than the rest of the barrels.
	 */
    private int     barrelIndex;
    
    /**
     * A variable that is used to examine if a barrel is being thrown.
     */
    private boolean throwsBarrel;

    /**
     * The reference to the DonkeyKongGameCanvas that holds the Donkey Kong.
     */
    private         DonkeyKongGameCanvas canvas;
    /**
     * The barrel-array.
     */
    private         Barrel[] barrels;
    
    /**
     * The contructor.
     * Here the classes private variables are initated to proper values.
     * 
     * @param image       The image of the Sprite
     * @param frameWidth  The width of a frame.
     * @param frameHeight The height of a frame.
     * @param canvas      The DonkeyKongGameCanva object.
     * @param barrels     The barrel-array.
     */
    public Kong( Image image, int frameWidth, int frameHeight,
                  DonkeyKongGameCanvas canvas, Barrel[] barrels)
    {
        super(image, frameWidth, frameHeight);

        this.defineReferencePixel(23, 18);
        this.setPosition( 10, 35 );
        this.canvas       = canvas;
        this.barrels      = barrels;
        this.barrelIndex  = 0;
        this.throwsBarrel = false;
    }
    
    /**
     * Sets Donkey Kong to it's normal position and frame. This
     * method is used after an animation where the sprite's frame
     * and position change.
     */
    public void setToStartingPosition()
    {
        this.setPosition(10, 35);
        setFrame(0);
    }
    
    /**
     * Sets a new value for the barrel-index.
     * 
     * @param index New value for the index.
     */
    public void setBarrelIndex( int index )
    {
        this.barrelIndex = index;
    }
    
    /**
     * Gets the current barrel-index.
     * 
     * @return The current barrel-index.
     */
    public int getBarrelIndex()
    {
        return this.barrelIndex;
    }
    
    /**
     * Sets a new value for the variable depicting that a barrel is being thrown.
     * 
     * @param value New value.
     */
    public void setThrowsBarrel( boolean value )
    {
        this.throwsBarrel = value;
    }
    
    /**
     * Gets the state for the variable depicting that a barrel is being thrown.
     * 
     * @return true, if a barrel is being thrown, else false.
     */
    public boolean getThrowsBarrel()
    {
        return this.throwsBarrel;
    }
    
    /**
     * A method that removes all barrels from the game screen and sets their direction
     * to the right. This method is used after the player completes a level and moves
     * into next one.
     */
    public void drawBarrelsBack()
    {
        for ( int i = 0 ; i < AMOUNT_OF_BARRELS ; i++ )
        {
            barrels[i].setVisible(false);
            barrels[i].setDirection(RIGHT);
        }
    }
    
    /**
     * Gets the maximum barrel-index. The barrel-index depends on the level where
     * the game goes, on higher levels there can be more barrels on screen.
     * 
     * @return The maximum index of barrels in use.
     */
    public int getMaximumBarrelIndex()
    {
        int ret = -1;
    
        switch( this.canvas.getLevel() )
        {
            case 1:
                ret = 3;
                break;
            case 2:
                ret = 4;
                break;
            case 3:
                ret = 5;
                break;
            case 4:
                ret = 5;
                break;
            default:
                ret = 5;
        }
        
        return ret;
    }
    
    /**
     * Selects the proper frame for Donkey Kong. If there is a barrel to be thrown
     * the frame is picked speifically, otherwise the frame is the next frame until
     * the last frame in sequence.
     */
    public void selectFrame()
    {
        int index = this.barrelIndex;
        
        if( !this.getThrowsBarrel() )
        {
            if( this.getFrame() == 0 )
            {
                this.setFrame(2);
            }
            else if ( this.getFrame() == 2 )
            {
                this.setFrame(3);
            }
            else
            {
                this.setFrame(0);
            }
        }
        else
        {
            
            if( this.barrels[this.barrelIndex].getFalling() )
            {
                this.setFrame(1);
            }
            else
            {
                this.setFrame(4);
            }
        }
        
    }
    
    /**
     * A method to make barrels appear on screen and make them move.
     * If no barrel can be thrown this method simply calls for the 
     * selectFrame method and selects a new frame for Donkey Kong.
     */
    public void throwBarrel()
    {
        boolean barrelsVisible = false;
        
        if ( this.canvas.getTime() == 0 )
        {
        	/*
        	 * Because the barrels can reach their ending destination in a
        	 * different order where they were thrown the whole barrel-array
        	 * has to be looked through, instead of checking them in the same
        	 * order.
        	 */
            for ( int i = 0 ; i <= this.getMaximumBarrelIndex() ; i++ )
            {
                if( !this.barrels[i].isVisible() )
                {
                    this.barrels[i].setToStartingPosition();
                    this.setBarrelIndex(i);
                    this.setThrowsBarrel(true);
                    barrelsVisible = true;
                    break;
                }
            }
        
            if ( !barrelsVisible )
            {
                this.setThrowsBarrel(false);
            }
            
            this.selectFrame();
        }
        
        // Making the barrels move.
        for ( int i = 0 ; i <= this.getMaximumBarrelIndex() ; i++ )
        {
            this.barrels[i].moveBarrel();
            
            if ( this.barrels[i].getY() >= 230 || barrels[i].collidesWith( canvas.getOilBarrel(), true ) )
            {
                this.barrels[i].setVisible(false);
            }
        }
    }

} // End of class Kong