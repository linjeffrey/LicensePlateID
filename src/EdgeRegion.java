
import java.util.ArrayList;

import processing.core.PImage;

public class EdgeRegion extends Region{

	public EdgeRegion(PImage original, int x, int y, int width, int height) {
		super(original, x, y, width, height);
		if (original == null) {
			ratio = 100000;
			this.score=Integer.MAX_VALUE;
			return;
		} else {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.pic = original.get(x, y, width, height);
			pic.loadPixels();
			int black = 0;
			int notBlack = 0;
	//		pic.parent.stroke(pic.parent.color(255, 0, 0));
	//		pic.parent.rect(x, y, pic.width, pic.height);
			for(int i = 0; i < pic.pixels.length; i++) {
				if(original.parent.brightness(pic.pixels[i]) < 20) 
					black++;
				else
					notBlack++;
				//System.out.println(original.parent.brightness(pic.pixels[i]));
			}
			System.out.println("Black: "+black+" notBlack: "+notBlack);
			if (black<2000|| notBlack< 2000) ratio=10000000;
			else this.ratio = ((double)black)/(black+notBlack);
//			System.out.println("Ratio: " + ratio);
		}
		score=Math.abs(ratio-0.23);
		if (score<10) {
		System.out.println(score);
		}
	}
	
	public EdgeRegion randomShifter() {
		ArrayList<EdgeRegion> regions = new ArrayList<EdgeRegion>();
		for(int i = -20; i <= 20; i+=10) {
			for(int j = -20; j <= 20; j+=10) {
				regions.add(new EdgeRegion(pic, x + i, y + j, width, height));
			}
		}
		EdgeRegion m = regions.get(0);
		for(EdgeRegion reg : regions) 
			if(reg.getScore() < m.getScore()) m = reg;
		return m;
	}
}