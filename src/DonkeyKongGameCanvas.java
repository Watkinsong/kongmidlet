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
import javax.microedition.media.*;
import javax.microedition.lcdui.Font;


/**
 * The game canvas class that is responsible for all the game action and graphic and sound
 * handling. 
 * The class extends class GameCanvas and uses it's methods and variables when creating
 * the visual game.
 * This class also implements interfaces Runnable and Finals. Runnable-interface is necessary
 * to create the thread where the game runs. Interface Finals contains final variables used in
 * this application in various ways.
 * 
 * @author Arttu Salonen
 *
 */
public class DonkeyKongGameCanvas extends GameCanvas implements Runnable, Finals
{
	/**
	 * Refernce to the MIDlet that shows this canvas.
	 */
    private DonkeyKongGame game;
    /**
     * Image of girder. Used to create the path where barrels and Mario move.
     */
    private Image          girderImage;
    /**
     * Image of ladder. Used to create the ladders to the game area.
     */
    private Image          ladderImage;
    /**
     * Image of Mario, the character the player controls.
     */
    private Image          marioImage;
    /**
     * Image of barrel. Used to create the barrels.
     */
    private Image          barrelImage;
    /**
     * A black image that is used in class Mario for collision detection purposes.
     */
    private Image          blackImage;
    /**
     * Image of Donkey Kong.
     */
    private Image          kongImage;
    /**
     * Image of princess.
     */
    private Image          princessImage;
    /**
     * Image of oilbarrel, where all the rolling barrels end.
     */
    private Image          oilbarrelImage;
    /**
     * Image of heart.
     */
    private Image          heartImage;
    /**
     * Image of orangutan. This image is shown when starting the level.
     */
    private Image          orangeImage;
    /**
     * A TiledLayer used to create the path with the image of girder.
     */
    private TiledLayer     backgroundLayer;
    /**
     * A TiledLayer used to create the ladders.
     */
    private TiledLayer     ladderLayer;
    /**
     * The game's controllable character.
     */
    private Mario          mario;
    /**
     * A Sprite used to check for Mario's movement.
     */
    private Sprite         checker;
    /**
     * A barrel.
     */
    private Barrel         barrel1;
    /**
     * A barrel.
     */
    private Barrel         barrel2;
    /**
     * A barrel.
     */
    private Barrel         barrel3;
    /**
     * A barrel.
     */
    private Barrel         barrel4;
    /**
     * A barrel.
     */
    private Barrel         barrel5;
    /**
     * A barrel.
     */
    private Barrel         barrel6;
    /**
     * An array where all the barrels are kept.
     */
    private Barrel[]       barrels;
    /**
     * Donkey Kong.
     */
    private Kong           donkey;
    /**
     * The Sprite object used to create the princess.
     */
    private Sprite         princess;
    /**
     * Sprite used to get barrels out of the bottom of the game area.
     */
    private Sprite         oilbarrel;
    /**
     * Sprite used when changing a level.
     */
    private Sprite         heart;
    /**
     * Sprite used to show the progress of the player. This particular Sprite could had been left
     * out by simply using the image of the orangutan, but because there was major problems in 
     * the game on the mobile phone this game was tested on it was added.
     */
    private Sprite         orange;
    /**
     * The player that is used to play the background music in a level.
     */
    private Player         backgroundMusic;
    /**
     * The Player that is used to play the melody when a new level begins.
     */
    private Player         levelStartingMusic;
    /**
     * The Player that is used to play the sound when Mario is hit byt a barrel or by Donkey Kong.
     */
    private Player         marioDiesMusic;
    /**
     * The Player that is used to play the ending music at the end of the game.
     */
    private Player         endingMusic;
    /**
     * A Random used to create random values.
     */
    private Random         random;
    /**
     * The level where the game is currently taking place. This value affects on how many barrels
     * there are simultaneusly on the screen and how often the fall from stairs.
     */
    private int            level;
    /**
     * Variable used to count the time of barrels to start moving and the princess-Sprite to change
     * frame.
     */
    private int            time;
    /**
     * A variable used to examine if the game is on.
     */
    private boolean        gameIsOn;
    /**
     * A variable used to examine if the player has reached the end of the current level.
     */
    private boolean        levelFinished;
    /**
     * The LayerManager that is resbonsible for holding and drawing the graphics.
     */
    private LayerManager layermanager;
    /**
     * The thread where the game goes on.
     */
    private Thread gameThread;
    
    /**
     * Setting the values of lesser variables.
     */
    public DonkeyKongGameCanvas( DonkeyKongGame game )
    {
        super(true);
        
        this.random = new Random();
        this.level  = 1;
        this.game   = game;
    }
    
    /**
     * Starting the game.
     * The sprites are set back to their starting positions and other needed values
     * are reseted. Also the game thread is created and started.
     *
     */
    public void startGame()
    {
        donkey.setToStartingPosition();
        donkey.drawBarrelsBack();
        mario.setToStartingPoint();
        level = 1;
        gameThread = new Thread(this); // Creating the game thread and firing it up.
		gameThread.start();
        setGameIsOn(true);
    }
    
    /**
     * Gets the game thread.
     * 
     * @return The game thread.
     */
    public Thread getGameThread()
    {
        return this.gameThread;
    }
    
    /**
     * Gets the layer that creates the paths.
     * 
     * @return The background layer.
     */
    public TiledLayer getBackgroundLayer()
    {
        return this.backgroundLayer;
    }
    
    /**
     * Gets the ladder layer.
     * 
     * @return The ladder layer.
     */
    public TiledLayer getLadderLayer()
    {
        return this.ladderLayer;
    }
    
    /**
     * Gets the checker sprite.
     * 
     * @return The Sprite that is used for collision detection examination.
     */
    public Sprite getCheckerSprite()
    {
        return this.checker;
    }
    
    /**
     * Gets the randomgenerator.
     * 
     * @return The randomgenerator.
     */
    public Random getRandom()
    {
        return this.random;
    }
    
    /**
     * Increasing the level the player is on. This method is used when the player
     * has reached the end of the level.
     *
     */
    public void increaseLevel()
    {
        this.level++;
    }
    
    /**
     * Gets the level of the game played.
     * 
     * @return The level of the game.
     */
    public int getLevel()
    {
        return this.level;
    }
    
    /**
     * Sets a new value for the timer.
     * 
     * @param time New value for time.
     */
    public void setTime( int time )
    {
        this.time = time;
    }
    
    /**
     * Gets the time-variable.
     * 
     * @return The time-variable.
     */
    public int getTime()
    {
        return this.time;
    }
    
    /**
     * Sets a new value for the variable used to check if the game is on.
     * 
     * @param value New value.
     */
    public void setGameIsOn( boolean value )
    {
        this.gameIsOn = value;
    }

    /**
     * Gets the state of the game: is it on or off. The state 'off' doesn't always
     * mean the game has ended. This variable has the value false when showing
     * animations.
     * 
     * @return true, if the game is on, else false.
     */
    public boolean getGameIsOn()
    {
        return this.gameIsOn;
    }

    /**
     * Sets a new value for the variable that is used to examine if the player has
     * finsihed a level.
     * 
     * @param value New value.
     */
    public void setLevelFinished( boolean value )
    {
        this.levelFinished = value;
    }
    
    /**
     * Gets the information about the player's progress considering the completion
     * of a level.
     * 
     * @return true, if the player has finished a level, else false.
     */
    public boolean getLevelFinished()
    {
        return this.levelFinished;
    }
    
    /**
     * Gets the oilbarrel-sprite, used by collision detection to remove barrels from screen.
     * 
     * @return The oilbarrel Sprite.
     */
    public Sprite getOilBarrel()
    {
        return this.oilbarrel;
    }
    
    /**
     * A method that is used after Mario and the barrel(s) have moved to figure out, if the game should
     * be terminated because Mario has been hit by a barrel or by Donkey Kong or if he has fallen from the
     * moving path, or if Mario has reached the end of the level by contacting with the princess.
     * Hitting and contacting is checked by collision detection. Before the checks Mario's collision
     * rectangle is expanded, as well as the barrels' in order to make a more precise detection of
     * collisions with pixel-based check. This is done because many mobile phones perform the check
     * otherwise poorly.
     *
     */
    public void checkForEvents()
    {
        if ( mario.collidesWith( princess, false ) )
        {
            this.setLevelFinished( true );
        }
        else if ( mario.collidesWith( donkey, true ) )
        {
            this.setGameIsOn( false );
        }
        else if ( mario.getY() >= 230 )
        {
            this.setGameIsOn(false);
        }
        else
        {
            for ( int i = 0 ; i < AMOUNT_OF_BARRELS ; i++ )
            {
                barrels[i].defineCollisionRectangle(0, 0, barrels[i].getWidth(), barrels[i].getHeight() );
                mario.defineCollisionRectangle(0, 0, mario.getWidth(), mario.getHeight() );
            
                if( mario.collidesWith(barrels[i], true ) )
                {
                    this.setGameIsOn( false );
                    barrels[i].defineCollisionRectangle(9, 9, 1, 1);
                    break;
                }
                
                barrels[i].defineCollisionRectangle(9, 9, 1, 1);
            }
        }

        mario.defineCollisionRectangle(11, 14, 187, 3); // Setting the collision rectangle back where it was
    }
    
    /**
     * Displays an animation that is used when Mario is hit by a barrel or by Donkey Kong (or when he
     * falls out of the image, though then this animation cannot be seen). In the animation Mario
     * falls to his back, waves his arms and then is left to rest with a halo.
     *
     */
    public void displayMarioDiesAnimation()
    {
        Graphics g = getGraphics();
        g.setColor(0,0,0);
        g.fillRect(0,0,getWidth(), getHeight());
        layermanager.paint(g,0,0);
        
        try
        {
            marioDiesMusic.start();
        }
        catch ( Exception e )
        {
        
        }
        
        flushGraphics();
        mario.setTransform(Sprite.TRANS_NONE);
        
        for ( int i = 0 ; i < 10 ; i++ )
        {
            try
            {
                getGameThread().sleep(150);
            }
            catch ( Exception e )
            {
            
            }
            g.fillRect(0,0,getWidth(), getHeight());

            if ( i % 2 == 0 )
            {
                mario.setFrame(1);
            }
            else
            {
                mario.setFrame(2);
            }
            
            layermanager.paint(g,0,0);
            mario.setTransform(Sprite.TRANS_ROT90);
            
            flushGraphics();
        }
        
        mario.setTransform(Sprite.TRANS_NONE);
        mario.setFrame(8);
        layermanager.paint(g,0,0);
        flushGraphics();
        
        try
        {
            getGameThread().sleep(500);
        }
        catch ( Exception e )
        {
            
        }
    }

    /**
     * Displays a screen that is shown before a level begins. On the screen are
     * pictures of orangutan, times the level about to be entered.
     *
     */
    public void displayLevelStartScreen()
    {
        Graphics g = getGraphics();
        g.setColor(0,0,0);
        g.fillRect(0,0,getWidth(), getHeight());
        Font f = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
        g.setFont(f);
        g.setColor(255,255,255);
        g.drawString("HOW HIGH CAN YOU GET?", getWidth()/2, 25, Graphics.TOP|Graphics.HCENTER);
        g.drawString(getLevel()*25+"M", getWidth()/2, 190, Graphics.TOP|Graphics.HCENTER);
        
        for ( int i = 0 ; i < getLevel() ; i++ )
        {
            orange.setVisible(true);
            orange.setPosition(97, 150-(orange.getHeight() * i ) );
            orange.paint(g);
        }
        
        g.setColor(0,0,0);
        flushGraphics();
        
        try
        {
            levelStartingMusic.start(); // Playing the level starting music.
        }
        catch( Exception e )
        {
            
        }
        
        try
        {
            getGameThread().sleep(3000);
        }
        catch ( Exception e )
        {
            
        }
        
        orange.setVisible(false);
    }

    /**
     * Displays an animation, that is used when moving to next level.
     */
    public void displayMovingToNextLevelAnimation()
    {
        Graphics g = getGraphics();
        g.setColor(0,0,0);
        mario.setPosition(118, 35);
        mario.setFrame(0);
        donkey.setFrame(0);
        layermanager.paint(g,0,0);
        flushGraphics();
        princess.setFrame(0);
        heart.setVisible(true);
        heart.setPosition(105, 30);
        heart.setFrame(0);
        g.fillRect(0,0,getWidth(), getHeight());
        layermanager.paint(g,0,0);
        flushGraphics();
        
        try
        {
            getGameThread().sleep(1500);
        }
        catch ( Exception e )
        {
            
        }

        heart.nextFrame();
        princess.setVisible(false);
        donkey.move(40, -5);
        
        for ( int i = 0 ; i < 15 ; i++ )
        {
            g.fillRect(0,0,getWidth(), getHeight());
            layermanager.paint(g,0,0);
            donkey.move(0, -3);
            
            if( donkey.getFrame() == 6 )
            {
                donkey.setFrame(5);
            }
            else if ( donkey.getFrame() == 5 )
            {
                donkey.nextFrame();
            }
            else
            {
                donkey.setFrame(5);
            }
            
            try
            {
                getGameThread().sleep(100);
            }
            catch ( Exception e )
            {
                
            }
            
            layermanager.paint(g,0,0);
            flushGraphics();
        }
        heart.setVisible(false);
        layermanager.paint(g,0,0);
        flushGraphics();
        princess.setVisible(true);
        donkey.setToStartingPosition();
    }
    
    /**
     * Displays the screen that is shown when the game is completed. Also the 
     * ending music is played.
     *
     */
    public void displayEndingScreen()
    {
        try
        {
            endingMusic.start();
        }
        catch ( Exception e )
        {
            
        }
    
        Graphics g = getGraphics();
        g.setColor(0,0,0);
        g.fillRect(0,0,getWidth(), getHeight());
        flushGraphics();
        mario.setPosition(130, ( getHeight()/2 ) + 7 );
        mario.setFrame(0);
        mario.paint(g);
        princess.setPosition(80, getHeight()/2);
        princess.setFrame(0);
        princess.paint(g);
        heart.setPosition(110, ( getHeight()/2) - 20 );
        heart.setVisible(true);
        heart.setFrame(0);
        heart.paint(g);
        Font f = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
        g.setFont(f);
        g.setColor(255,255,255);
        g.drawString("PRESS JUMP", getWidth()/2, 200, Graphics.TOP|Graphics.HCENTER);
        flushGraphics();
    }
    
    /**
     * An overrided GameCanvas-methodm that is used to examine the inputs of the player.
     * 
     * @param keyCode The corresponding integer value of the button pressed.
     */
    protected void keyRepeated( int keyCode )
    {
        if ( keyCode == GameCanvas.LEFT_PRESSED )
        {
            mario.setDirection( Finals.LEFT );
        }
        else if ( keyCode == GameCanvas.RIGHT_PRESSED )
        {
            mario.setDirection( Finals.RIGHT );
        }
        else if ( keyCode == GameCanvas.UP_PRESSED )
        {
            mario.setDirection( Finals.UP );
        }
        else if ( keyCode == GameCanvas.DOWN_PRESSED )
        {
            mario.setDirection( Finals.DOWN );
        }
        else
        {
            mario.setDirection(-1);
        }
    }
    
    /**
     * The run-method, where all the game mechanics are performed.
     */
    public void run()
    {
    	// Getting the graphics
        Graphics g = getGraphics();
        // Displaying the level starting screen
        displayLevelStartScreen();
        
        while( getGameIsOn() && !getLevelFinished() )
        {

            try
            {
                this.backgroundMusic.start();
            }
            catch ( Exception e )
            {
                
            }
            // Drawing the black background
            g.setColor(0,0,0);
            g.fillRect(0,0,getWidth(), getHeight());
            
            try
            {
                getGameThread().sleep(50);
            }
            catch ( Exception e )
            {
                
            }
            // Reducing the time.
            this.setTime( this.getTime() - 1 );
            
            // If Mario isn't jumping he can have another move
            if ( !mario.getJumping() )
            {
                int ks = getKeyStates();
            
                if ((ks & FIRE_PRESSED) != 0)
                {
                    mario.setDirection(Finals.JUMP);
                }
                else if ( ks != 0 )
                {
                    this.keyRepeated( ks );
                }
                else
                {
                    mario.setDirection(-1);
                }
                mario.moveMario();
            }
            else
            {
                mario.setDirection(Finals.JUMP);
                mario.moveMario();
            }
            
            // Making the barrels move
            donkey.throwBarrel();

            if (this.getTime() <= 0 )
            {
                this.setTime(5);
                princess.nextFrame();
            }
            
            // Checking for collisions
            checkForEvents();
            
            if ( this.getLevelFinished() )
            {
                if ( getLevel() < 4 )
                {
                    displayMovingToNextLevelAnimation();
                    mario.setToStartingPoint();
                    donkey.drawBarrelsBack();
                    increaseLevel();
                    displayLevelStartScreen();
                    setGameIsOn( true );
                    setLevelFinished( false );
                }
                else
                {
                    displayEndingScreen();
                    
                    int ks = getKeyStates();
                    while( (ks & FIRE_PRESSED) == 0)
                    {
                        ks = getKeyStates();
                        
                        try
                        {
                            getGameThread().sleep(100);
                        }
                        catch ( Exception e )
                        {
                            
                        }
                    }
                    
                    try
                    {
                        endingMusic.stop();
                        this.getGameThread().interrupt();
                    }
                    catch ( Exception e )
                    {
                        
                    }
                    game.changeState(EXIT_STATE);
                }
            }
            else if ( !this.getGameIsOn() && !this.getLevelFinished() )
            {
                this.displayMarioDiesAnimation();
                
                try
                {
                    getGameThread().sleep(1000);
                }
                catch ( Exception e )
                {
                    
                }
                game.changeState(MENUSCREEN_STATE);
            }
            
        	layermanager.paint(g,0,0);

        	flushGraphics();
            
        }

    }
    
    /**
     * In this method external image and sound resources are fetched, and the layout of the game's
     * playing area is made. Also every Sprite is created by using the pictures. This method is called
     * from the class DonkeyKongGame in order the make sure the game is ready to play when starting.
     */
    public void initialize()
    {
		try
        {
            girderImage        = Image.createImage("/girder.png");
            ladderImage        = Image.createImage("/ladder.png");
            marioImage         = Image.createImage("/mario.png");
            blackImage         = Image.createImage("/black.png");
            barrelImage        = Image.createImage("/barrel.png");
            kongImage          = Image.createImage("/kong.png");
            princessImage      = Image.createImage("/princess.png");
            oilbarrelImage     = Image.createImage("/oilbarrel.png");
            heartImage         = Image.createImage("/heart.png");
            orangeImage        = Image.createImage("/orange.png");
            
            InputStream in     = getClass().getResourceAsStream("/theme.mid");
            backgroundMusic    = Manager.createPlayer(in, "audio/midi");
            in                 = getClass().getResourceAsStream("/level-start.mid");
            levelStartingMusic = Manager.createPlayer(in, "audio/midi");
            levelStartingMusic.prefetch();
            in                 = getClass().getResourceAsStream("/mario-dies.mid");
            marioDiesMusic     = Manager.createPlayer(in, "audio/midi");
            marioDiesMusic.prefetch();
            in                 = getClass().getResourceAsStream("/end.mid");
            endingMusic        = Manager.createPlayer(in, "audio/midi");
            endingMusic.prefetch();
		}	
		catch(Exception e)
        {
			
		}


        
        // Creating the background moving paths
        backgroundLayer = new TiledLayer(14, 24, girderImage, 16, 8) ;
        backgroundLayer.setPosition(8, 35);
        backgroundLayer.setCell(0, 23, 1);
        backgroundLayer.setCell(1, 23, 1);
        backgroundLayer.setCell(2, 23, 1);
        backgroundLayer.setCell(3, 23, 1);
        backgroundLayer.setCell(4, 23, 1);
        backgroundLayer.setCell(5, 23, 1);
        backgroundLayer.setCell(6, 23, 1);
        backgroundLayer.setCell(7, 23, 2);
        backgroundLayer.setCell(8, 23, 3);
        backgroundLayer.setCell(9, 23, 4);
        backgroundLayer.setCell(10, 23, 5);
        backgroundLayer.setCell(11, 23, 6);
        backgroundLayer.setCell(12, 23, 7);
        backgroundLayer.setCell(13, 23, 8);
        backgroundLayer.setCell(7, 22, 9);
        backgroundLayer.setCell(8, 22, 10);
        backgroundLayer.setCell(9, 22, 11);
        backgroundLayer.setCell(10, 22, 12);
        backgroundLayer.setCell(11, 22, 13);
        backgroundLayer.setCell(12, 22, 14);
        backgroundLayer.setCell(13, 22, 15);
        backgroundLayer.setCell(12, 20, 1);
        backgroundLayer.setCell(11, 20, 2);
        backgroundLayer.setCell(10, 20, 3);
        backgroundLayer.setCell(9, 20, 4);
        backgroundLayer.setCell(8, 20, 5);
        backgroundLayer.setCell(7, 20, 6);
        backgroundLayer.setCell(6, 20, 7);
        backgroundLayer.setCell(5, 20, 8);
        backgroundLayer.setCell(0, 19, 5);
        backgroundLayer.setCell(1, 19, 4);
        backgroundLayer.setCell(2, 19, 3);
        backgroundLayer.setCell(3, 19, 2);
        backgroundLayer.setCell(4, 19, 1);
        backgroundLayer.setCell(5, 19, 15);
        backgroundLayer.setCell(6, 19, 14);
        backgroundLayer.setCell(7, 19, 13);
        backgroundLayer.setCell(8, 19, 12);
        backgroundLayer.setCell(9, 19, 11);
        backgroundLayer.setCell(10, 19, 10);
        backgroundLayer.setCell(11, 19, 9);
        backgroundLayer.setCell(3, 18, 9);
        backgroundLayer.setCell(2, 18, 10);
        backgroundLayer.setCell(1, 18, 11);
        backgroundLayer.setCell(0, 18, 12);
        backgroundLayer.setCell(1, 17, 5);
        backgroundLayer.setCell(2, 17, 6);
        backgroundLayer.setCell(3, 17, 7);
        backgroundLayer.setCell(4, 17, 8);
        backgroundLayer.setCell(1, 16, 12);
        backgroundLayer.setCell(2, 16, 13);
        backgroundLayer.setCell(3, 16, 14);
        backgroundLayer.setCell(4, 16, 15);
        backgroundLayer.setCell(5, 16, 1);
        backgroundLayer.setCell(6, 16, 2);
        backgroundLayer.setCell(7, 16, 3);
        backgroundLayer.setCell(8, 16, 4);
        backgroundLayer.setCell(9, 16, 5);
        backgroundLayer.setCell(10, 16, 6);
        backgroundLayer.setCell(11, 16, 7);
        backgroundLayer.setCell(12, 16, 8);
        backgroundLayer.setCell(6, 15, 9);
        backgroundLayer.setCell(7, 15, 10);
        backgroundLayer.setCell(8, 15, 11);
        backgroundLayer.setCell(9, 15, 12);
        backgroundLayer.setCell(10, 15, 13);
        backgroundLayer.setCell(11, 15, 14);
        backgroundLayer.setCell(12, 15, 15);
        backgroundLayer.setCell(13, 15, 1);
        backgroundLayer.setCell(12, 13, 1);
        backgroundLayer.setCell(11, 13, 2);
        backgroundLayer.setCell(10, 13, 3);
        backgroundLayer.setCell(5, 13, 8);
        backgroundLayer.setCell(6, 13, 7);
        backgroundLayer.setCell(7, 13, 6);
        backgroundLayer.setCell(8, 13, 5);
        backgroundLayer.setCell(9, 13, 4);
        backgroundLayer.setCell(0, 12, 5);
        backgroundLayer.setCell(1, 12, 4);
        backgroundLayer.setCell(2, 12, 3);
        backgroundLayer.setCell(3, 12, 2);
        backgroundLayer.setCell(4, 12, 1);
        backgroundLayer.setCell(5, 12, 15);
        backgroundLayer.setCell(6, 12, 14);
        backgroundLayer.setCell(7, 12, 13);
        backgroundLayer.setCell(8, 12, 12);
        backgroundLayer.setCell(9, 12, 11);
        backgroundLayer.setCell(10, 12, 10);
        backgroundLayer.setCell(11, 12, 9);
        backgroundLayer.setCell(0, 11, 12);
        backgroundLayer.setCell(1, 11, 11);
        backgroundLayer.setCell(2, 11, 10);
        backgroundLayer.setCell(3, 11, 9);
        backgroundLayer.setCell(1, 9, 1);
        backgroundLayer.setCell(2, 9, 2);
        backgroundLayer.setCell(3, 9, 3);
        backgroundLayer.setCell(4, 9, 4);
        backgroundLayer.setCell(5, 9, 5);
        backgroundLayer.setCell(6, 9, 6);
        backgroundLayer.setCell(7, 9, 7);
        backgroundLayer.setCell(8, 9, 8);
        backgroundLayer.setCell(2, 8, 9);
        backgroundLayer.setCell(3, 8, 10);
        backgroundLayer.setCell(4, 8, 11);
        backgroundLayer.setCell(5, 8, 12);
        backgroundLayer.setCell(6, 8, 13);
        backgroundLayer.setCell(7, 8, 14);
        backgroundLayer.setCell(8, 8, 15);
        backgroundLayer.setCell(9, 8, 1);
        backgroundLayer.setCell(10, 8, 2);
        backgroundLayer.setCell(11, 8, 3);
        backgroundLayer.setCell(12, 8, 4);
        backgroundLayer.setCell(13, 8, 5);
        backgroundLayer.setCell(10, 7, 9);
        backgroundLayer.setCell(11, 7, 10);
        backgroundLayer.setCell(12, 7, 11);
        backgroundLayer.setCell(13, 7, 12);
        backgroundLayer.setCell(10, 5, 3);
        backgroundLayer.setCell(11, 5, 2);
        backgroundLayer.setCell(12, 5, 1);
        backgroundLayer.setCell(0, 5, 5);
        backgroundLayer.setCell(1, 5, 5);
        backgroundLayer.setCell(2, 5, 5);
        backgroundLayer.setCell(3, 5, 5);
        backgroundLayer.setCell(4, 5, 5);
        backgroundLayer.setCell(5, 5, 5);
        backgroundLayer.setCell(6, 5, 5);
        backgroundLayer.setCell(7, 5, 5);
        backgroundLayer.setCell(8, 5, 5);
        backgroundLayer.setCell(9, 5, 4);
        backgroundLayer.setCell(0, 4, 12);
        backgroundLayer.setCell(1, 4, 12);
        backgroundLayer.setCell(2, 4, 12);
        backgroundLayer.setCell(3, 4, 12);
        backgroundLayer.setCell(4, 4, 12);
        backgroundLayer.setCell(5, 4, 12);
        backgroundLayer.setCell(6, 4, 12);
        backgroundLayer.setCell(7, 4, 12);
        backgroundLayer.setCell(8, 4, 12);
        backgroundLayer.setCell(9, 4, 11);
        backgroundLayer.setCell(10, 4, 10);
        backgroundLayer.setCell(11, 4, 9);
        backgroundLayer.setCell(7, 2, 1);
        backgroundLayer.setCell(6, 2, 1);
        backgroundLayer.setCell(5, 2, 1);
        
        // Creating the ladders
        ladderLayer = new TiledLayer(50, 80, ladderImage, 8, 3);
        // First level
        ladderLayer.setPosition(-5, 3);
        ladderLayer.setCell(10, 71, 1);
        ladderLayer.setCell(10, 70, 1);
        
        ladderLayer.setCell(14, 61, 1);
        ladderLayer.setCell(14, 60, 1);
        ladderLayer.setCell(14, 59, 1);
        ladderLayer.setCell(14, 58, 1);
        ladderLayer.setCell(14, 57, 1);
        ladderLayer.setCell(14, 56, 1);
        ladderLayer.setCell(14, 55, 1);
        ladderLayer.setCell(14, 54, 1);
        ladderLayer.setCell(14, 53, 1);
        ladderLayer.setCell(14, 52, 1);
        
        ladderLayer.setCell(10, 64, 1);
        
        ladderLayer.setCell(20, 70, 1);
        ladderLayer.setCell(20, 69, 1);
        ladderLayer.setCell(20, 68, 1);
        ladderLayer.setCell(20, 67, 1);
        ladderLayer.setCell(20, 66, 1);
        ladderLayer.setCell(20, 65, 1);
        ladderLayer.setCell(20, 64, 1);
        ladderLayer.setCell(20, 63, 1);
        ladderLayer.setCell(20, 62, 1);
        // Second level
        ladderLayer.setCell(4, 59, 1);
        ladderLayer.setCell(4, 58, 1);
        ladderLayer.setCell(4, 57, 1);
        ladderLayer.setCell(4, 56, 1);
        ladderLayer.setCell(4, 55, 1);
        ladderLayer.setCell(4, 54, 1);
        
        ladderLayer.setCell(10, 53, 1);
        ladderLayer.setCell(10, 52, 1);
        ladderLayer.setCell(10, 51, 1);

        ladderLayer.setCell(16, 52, 1);
        ladderLayer.setCell(16, 51, 1);
        ladderLayer.setCell(16, 50, 1);
        ladderLayer.setCell(16, 49, 1);
        ladderLayer.setCell(16, 48, 1);
        ladderLayer.setCell(16, 47, 1);
        ladderLayer.setCell(16, 46, 1);
        ladderLayer.setCell(16, 45, 1);
        ladderLayer.setCell(16, 44, 1);
        ladderLayer.setCell(16, 43, 1);
        // Third level
        ladderLayer.setCell(10, 46, 1);
        ladderLayer.setCell(10, 45, 1);
        
        ladderLayer.setCell(24, 50, 1);
        ladderLayer.setCell(24, 49, 1);
        ladderLayer.setCell(24, 48, 1);
        ladderLayer.setCell(24, 47, 1);
        ladderLayer.setCell(24, 46, 1);
        ladderLayer.setCell(24, 45, 1);
        ladderLayer.setCell(24, 44, 1);
        
        ladderLayer.setCell(20, 43, 1);
        ladderLayer.setCell(20, 42, 1);
        ladderLayer.setCell(20, 41, 1);
        ladderLayer.setCell(20, 40, 1);
        ladderLayer.setCell(20, 39, 1);
        ladderLayer.setCell(20, 38, 1);
        ladderLayer.setCell(20, 37, 1);
        ladderLayer.setCell(20, 36, 1);
        ladderLayer.setCell(20, 35, 1);
        ladderLayer.setCell(20, 34, 1);
        ladderLayer.setCell(20, 33, 1);
        ladderLayer.setCell(20, 32, 1);
        ladderLayer.setCell(20, 31, 1);
        
        ladderLayer.setCell(12, 42, 1);
        ladderLayer.setCell(12, 41, 1);
        ladderLayer.setCell(12, 40, 1);
        ladderLayer.setCell(12, 39, 1);
        ladderLayer.setCell(12, 38, 1);
        ladderLayer.setCell(12, 37, 1);
        ladderLayer.setCell(12, 36, 1);
        ladderLayer.setCell(12, 35, 1);
        ladderLayer.setCell(12, 34, 1);
        ladderLayer.setCell(12, 33, 1);
        // fourth level
        ladderLayer.setCell(6, 41, 1);
        ladderLayer.setCell(6, 40, 1);
        ladderLayer.setCell(6, 39, 1);
        ladderLayer.setCell(6, 38, 1);
        ladderLayer.setCell(6, 37, 1);
        ladderLayer.setCell(6, 36, 1);
        ladderLayer.setCell(6, 35, 1);
        ladderLayer.setCell(6, 34, 1);
        ladderLayer.setCell(6, 33, 1);
        // fifth level
        ladderLayer.setCell(14, 32, 1);
        ladderLayer.setCell(14, 31, 1);
        ladderLayer.setCell(14, 30, 1);
        
        ladderLayer.setCell(24, 30, 1);
        ladderLayer.setCell(24, 29, 1);
        ladderLayer.setCell(24, 28, 1);
        ladderLayer.setCell(24, 27, 1);
        ladderLayer.setCell(24, 26, 1);
        ladderLayer.setCell(24, 25, 1);
        ladderLayer.setCell(24, 24, 1);
        ladderLayer.setCell(24, 23, 1);
        
        ladderLayer.setCell(16, 22, 1);
        ladderLayer.setCell(16, 21, 1);
        ladderLayer.setCell(16, 20, 1);
        ladderLayer.setCell(16, 19, 1);
        ladderLayer.setCell(16, 18, 1);
        ladderLayer.setCell(16, 17, 1);
        ladderLayer.setCell(16, 16, 1);
        // sixth level
        ladderLayer.setCell(14, 26, 1);
        ladderLayer.setCell(14, 25, 1);
        
        ladderLayer.setCell(10, 22, 1);
        ladderLayer.setCell(10, 21, 1);
        ladderLayer.setCell(10, 20, 1);
        ladderLayer.setCell(10, 19, 1);
        ladderLayer.setCell(10, 18, 1);
        ladderLayer.setCell(10, 17, 1);
        ladderLayer.setCell(10, 16, 1);
        ladderLayer.setCell(10, 15, 1);
        ladderLayer.setCell(10, 14, 1);
        ladderLayer.setCell(10, 13, 1);
        ladderLayer.setCell(10, 12, 1);
        ladderLayer.setCell(10, 11, 1);
        ladderLayer.setCell(10, 10, 1);
        
        ladderLayer.setCell(8, 22, 1);
        ladderLayer.setCell(8, 21, 1);
        ladderLayer.setCell(8, 20, 1);
        ladderLayer.setCell(8, 19, 1);
        ladderLayer.setCell(8, 18, 1);
        ladderLayer.setCell(8, 17, 1);
        ladderLayer.setCell(8, 16, 1);
        ladderLayer.setCell(8, 15, 1);
        ladderLayer.setCell(8, 14, 1);
        ladderLayer.setCell(8, 13, 1);
        ladderLayer.setCell(8, 12, 1);
        ladderLayer.setCell(8, 11, 1);
        ladderLayer.setCell(8, 10, 1);
        
        
        mario   = new Mario( marioImage, 22, 17, this);
        checker = new Sprite( blackImage );
        checker.defineCollisionRectangle(11, 15, 22, 2);
        checker.defineReferencePixel(8, 6);
        checker.setVisible(false);
        
        barrel1 = new Barrel(barrelImage, 18, 10, this);
        barrel2 = new Barrel(barrelImage, 18, 10, this);
        barrel3 = new Barrel(barrelImage, 18, 10, this);
        barrel4 = new Barrel(barrelImage, 18, 10, this);
        barrel5 = new Barrel(barrelImage, 18, 10, this);
        barrel6 = new Barrel(barrelImage, 18, 10, this);
        
        barrels = new Barrel[6];
        barrels[0] = barrel1;
        barrels[1] = barrel2;
        barrels[2] = barrel3;
        barrels[3] = barrel4;
        barrels[4] = barrel5;
        barrels[5] = barrel6;
        
        donkey    = new Kong(kongImage, 46, 36, this, barrels);
        princess  = new Sprite(princessImage, 41, 24);
        princess.setPosition(86, 29);
        oilbarrel = new Sprite(oilbarrelImage);
        oilbarrel.setPosition(20, 190);
        heart     = new Sprite(heartImage, 16, 13);
        heart.setVisible(false);
        orange    = new Sprite(orangeImage);
        orange.setVisible(false);
        
        //Creating the layer manager and appending the graphics to it
        layermanager = new LayerManager();
        layermanager.append(mario);
        layermanager.append(barrel1);
        layermanager.append(barrel2);
        layermanager.append(barrel3);
        layermanager.append(barrel4);
        layermanager.append(barrel5);
        layermanager.append(barrel6);
        layermanager.append(donkey);
        layermanager.append(ladderLayer);
        layermanager.append(backgroundLayer);
        layermanager.append(checker);
        layermanager.append(princess);
        layermanager.append(oilbarrel);
        layermanager.append(heart);
        
        this.time          = 5;
        this.gameIsOn      = true;
        this.levelFinished = false;
    }

} // End of class DonkeyKongGameCanvas