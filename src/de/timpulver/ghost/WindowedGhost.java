package de.timpulver.ghost;

import processing.core.PApplet;

public class WindowedGhost extends Ghost {
	
	public WindowedGhost(PApplet p, int x, int y, int w, int h){
		init(p, x, y, w, h, DEFAULT_RENDERER);
	}
	
	public WindowedGhost(PApplet p, int x, int y, int w, int h, String renderer){
		init(p, x, y, w, h, renderer);
	}
}
