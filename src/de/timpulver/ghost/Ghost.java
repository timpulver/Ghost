//
// Ghost.java
// Ghost (v.##library.prettyVersion##) is released under the MIT License.
//
// Copyright (c) 2012, Tim Pulver http://timpulver.de
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
//


/* 
 * Tested with Processing 2.0b6 (win8 64bit)
 * Adapted code from jungalero (processing.org forum)
 * 
 * TODO:
 * - reposition screenshot image on window move / resize
 * 		- take screenshot of whole desktop
 * 		- implement move(int x, int y)
 * 			- grab pixles to fit new area from old screenshot
 * - two different modes? AWTUtil (win only) + Robot  
 */

package de.timpulver.ghost;

import java.awt.AWTException;
import com.sun.awt.AWTUtilities;

import java.awt.Frame;
import java.awt.Robot;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.lang.reflect.Method;
//import java.awt.DisplayMode;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;


public abstract class Ghost implements PConstants{
	private PApplet p;
	private PImage screenShotArea, screenShotFull;
	private boolean clearBackground = true;
	boolean redrawScreenShot = false;
	protected static final String DEFAULT_RENDERER = JAVA2D; 

	private int x, y, w, h;

	
	public void init(PApplet p, int x, int y, int w, int h, String renderer){
		this.p = p;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		p.size(w, h, DEFAULT_RENDERER);  
		screenShotArea = getScreenCapture(x, y, w, h);
		screenShotFull = getFullscreenCapture();
		p.frame.removeNotify();
		p.frame.setUndecorated(true);
		p.registerMethod("pre", this); // new in Processing 2.0
		p.image(screenShotArea,0,0, w, h);
		// if we are on a mac, remove the drop shadow
		if(isMac()){
			boolean dropShadowRemoved = removeDropShadow(p);
			if(!dropShadowRemoved){
				System.err.println("WARNING: Could not remove the drop shadow from Processing frame! " +
						"You may need to install the latest Java version.");
			}
		}
		//AWTUtilities.setWindowOpaque(p.frame, false); // removes shadow on osx
	}
	
	private boolean isMac(){
		return System.getProperty("os.name").toLowerCase().indexOf("max") > 0;
	}
	
	private boolean removeDropShadow(PApplet p){
		try {
            Window win = p.frame;
            //invoke AWTUtilities.setWindowOpacity(win, 0.0f);
            Class awtutil = Class.forName("com.sun.awt.AWTUtilities");
            Method setWindowOpaque = awtutil.getMethod("setWindowOpacity", Window.class, boolean.class);
            setWindowOpaque.invoke(win, false);
        } catch (Exception ex) {
            //ex.printStackTrace();
        	return false;
        }
		return true;
	}
	
	//TODO, what happens when there is already something drawed?
	/**
	 * Sets the screen to a new position, 
	 * actual reposition will be done on next draw() / pre() call. 
	 * @param x new x position of the window
	 * @param y new y position of the window
	 */
	public void setPosition(int x, int y){
		if(!clearBackground){
			System.err.println("WARNING: setPosition() clears the screen");
		}
		this.x = x;
		this.y = y;
		screenShotArea.copy(screenShotFull, x, y, w, h, 0, 0, w, h);
		redrawScreenShot = true;
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
	 * We need to reposition the window every frame and 
	 * draw the screenshot as a background. Do not call this from your sketch!
	 */
	public void pre(){
		  p.frame.setLocation(x, y);
		  if(clearBackground || redrawScreenShot){
			  p.image(screenShotArea,0,0, w, h);
			  redrawScreenShot = false;
		  }
	}

	/**
	 * Returns an image of the screen (full)
	 */
	private PImage getFullscreenCapture(){
	  return getScreenCapture(0, 0, p.displayWidth, p.displayHeight);
	}

	/**
	 * Returns an area of the screen (screenshot)
	 * @param x Top-Left corner of the area to copy (x)
	 * @param y Top Left corner of the area to copy (y)
	 * @param w Width of the rectangle
	 * @param h Height of the rectangle
	 * @return Screenshot of the current desktop,  
	 * when you call this in Processing before size() the sketch will 
	 * not be included in the Screenshot. 
	 */
	public static PImage getScreenCapture(int x, int y, int w, int h){
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
