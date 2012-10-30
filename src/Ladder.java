import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.*;
import java.util.*;
import java.io.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.game.*;

public class Ladder extends Sprite
{
    private int height;
    
    private int startX;
    
    private int startY;
    
    private boolean climbable;
    
    
    public Ladder( Image image, int frameWidth, int frameHeight,
                   int height, int startX, int startY, boolean climbable )
    {
        super(image, frameWidth, frameHeight);
        
        this.height    = height;
        this.startX    = startX;
        this.startY    = startY;
        this.climbable = climbable;

        this.setPosition( this.startX, this.startY );
    }
    
    
    public void setPositionByHeight( TiledLayer tiledlayer )
    {
        for ( int i = this.startY ; i < this.height ; i++ )
        {
            tiledlayer.setCell( this.startY, this.startX, 1 );
        }
    }

} // End of class Ladder