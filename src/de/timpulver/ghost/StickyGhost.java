package de.timpulver.ghost;

import processing.core.PApplet;

public class StickyGhost extends Ghost {
	
	/**
	 * Default constructor. Creates a new transparent window, which sticks to a screen side.
	 * @param p pass 'this' from within your Processing sketch
	 * @param side which side of the screen the window should stick to
	 * @param w_h width / height of the window
	 */
	public StickyGhost(PApplet p, String side, int w_h){
		this(p, side, w_h, DEFAULT_RENDERER);
	}

	/**
	 * Default constructor. Creates a new transparent window, which sticks to a screen side.
	 * @param p pass 'this' from within your Processing sketch
	 * @param side which side of the screen the window should stick to
	 * @param w_h width / height of the window
	 * @param renderer the renderer to use (P2D, P3D or OPENGL)
	 */
	public StickyGhost(PApplet p, String side, int w_h, String renderer){
		String lowerCaseSide = side.toLowerCase();
		if(lowerCaseSide.equals("top")){
			init(p, 0, 0, p.displayWidth, w_h, renderer);
		}
		else if(lowerCaseSide.equals("right")){
			init(p, p.displayWidth-1-w_h, 0, w_h, p.displayHeight, renderer);
		}
		else if(lowerCaseSide.equals("bottom")){
			init(p, 0, p.displayHeight-1-w_h, p.displayWidth, w_h, renderer);
		}
		else if(lowerCaseSide.equals("left")){
			init(p, 0, 0, w_h, p.displayHeight, renderer);
		}
		else{
			System.err.println("Wrong argument! Side should be 'top', 'right', 'bottom' or 'left'. You passed '" + side + "'.");
		}
	}
}
