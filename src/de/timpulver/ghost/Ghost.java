package de.timpulver.ghost;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
//import java.awt.DisplayMode;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

/**
 * Author: Tim Pulver
 * Date: 2012
 * tim_pulver AT gmx DOT de
 *
 * Tested with Processing 2.0b6
 *
 * Adapted code by jungalero (processing.org forum)
 */

public abstract class Ghost implements PConstants{
	PApplet p;
	PImage screenShot;
	private boolean clearBackground = true;
	protected static final String DEFAULT_RENDERER = JAVA2D; 

	private int x, y, w, h;

	
	public void init(PApplet p, int x, int y, int w, int h, String renderer){
		this.p = p;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		p.size(w, h, DEFAULT_RENDERER);  
		screenShot = getScreen(x, y, w, h);
		p.frame.removeNotify();
		p.frame.setUndecorated(true);
		p.registerMethod("pre", this); // new in Processing 2.0 -> test
		p.image(screenShot,0,0, w, h);
	}
	
	
	/**
	 * Equals a background(0) in the Processing draw() function. 
	 * When set to <i>true</i>, the background image will be drawn every frame, 
	 * so everything beneath it will not be visible any more. 
	 * @param b Whether or not the background should be cleared every frame
	 */
	public void clearBackground(boolean b){
		this.clearBackground = b;
	}
	
	/**
	 * Will be called before draw() in the main sketch. 
	 * We need to reposition the window every frame (tested on win) and 
	 * draw the screenshot as a background.
	 */
	public void pre(){
		  p.frame.setLocation(x, y);
		  if(clearBackground){
			  p.image(screenShot,0,0, w, h);
		  }
	}

	/**
	 * Returns an image of the screen (full)
	 */
	private PImage getFullscreenCapture(){
	  return getScreen(0, 0, p.displayWidth, p.displayHeight);
	}

	/**
	 * Returns an area of the screen (screenshot)
	 * @param x Top-Left corner of the area to copy (x)
	 * @param y Top Left corner of the area to copy (y)
	 * @param w Width of the rectangle
	 * @param h Height of the rectangle
	 */
	private PImage getScreen(int x, int y, int w, int h){
	  GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	  GraphicsDevice[] gs = ge.getScreenDevices();
	  //DisplayMode mode = gs[0].getDisplayMode();
	  Rectangle bounds = new Rectangle(x, y, w, h);
	  BufferedImage desktop = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

	  try {
	    desktop = new Robot(gs[0]).createScreenCapture(bounds);
	  }
	  catch(AWTException e) {
	    System.err.println("Screen capture failed.");
	  }
	  return (new PImage(desktop));
	}
}
