
import java.util.ArrayList;

import processing.core.PImage;

public class ColorRegion extends Region{
	
	public ColorRegion(PImage original, int x, int y, int width, int height) {
		super(original, x, y, width, height);
		if (original == null) {
			ratio = 100000;
			score=Integer.MAX_VALUE;
			return;
		} else {
	//		pic.parent.stroke(pic.parent.color(255, 0, 0));
	//		pic.parent.rect(x, y, pic.width, pic.height);
			int blue = 0;
			int white = 0;
			int other=0;
			for(int i = 0; i < pic.pixels.length; i++) {
				if(pic.pixels[i] == original.parent.color(0, 0, 255) ) 
					blue++;
				else if (pic.pixels[i] == original.parent.color(255, 255, 255))
					white++;
				else other++;
				
			}
			if (blue<1400||white<3000) ratio=1000000;
			else this.ratio = (((double)(blue+white))/other);
			System.out.println("Blue: "+blue+" White: "+ white);
			score=Math.abs(ratio-2.088);
			if (score<10) {
			System.out.println(score);
			}
		}
	}

	public ColorRegion randomShifter() {
		ArrayList<ColorRegion> regions = new ArrayList<ColorRegion>();
		for(int i = -20; i <= 20; i+=10) {
			for(int j = -20; j <= 20; j+=10) {
				regions.add(new ColorRegion(pic, x + i, y + j, width, height));
			}
		}
		ColorRegion m = regions.get(0);
		for(ColorRegion reg : regions) 
			if(reg.getRatio() > m.getRatio()) m = reg;
		return m;
	}

}