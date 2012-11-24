package de.timpulver.ghost;

import processing.core.PApplet;

public class FullscreenGhost extends Ghost{
	
	public FullscreenGhost(PApplet p){
		init(p, 0, 0, p.displayWidth, p.displayHeight, DEFAULT_RENDERER);
	}
	
	public FullscreenGhost(PApplet p, String renderer){
		init(p, 0, 0, p.displayWidth, p.displayHeight, renderer);
	}
}
