

import java.util.ArrayList;

import processing.core.PImage;

public class Region {

	public double ratio;
	public int x;
	public int y;
	public int width;
	public int height;
	public PImage pic;
	public double score;
	
	public Region(PImage original, int x, int y, int width, int height) {
		if(original != null) {
			this.pic = original.get(x, y, width, height);
			pic.loadPixels();
		}
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.ratio = 0.0;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public PImage getPic() {
		return pic;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setPic(PImage pic) {
		this.pic = pic;
	}

	public double getRatio() {
		return ratio;
	}
	
	public void setScore(double s) {
		score=s;
	}
	public double getScore() {
		return score;
	}
}