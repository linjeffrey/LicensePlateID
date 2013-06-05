import java.util.ArrayList;
import java.util.Random;
import processing.core.PApplet;
import processing.core.PImage;

public class Tester extends PApplet {

	public static final String LOCATION = "D:\\ExampleLicensePlates\\10.JPG";
	public static final Random r = new Random();
	public final int pink = color(255, 105, 180);
	public final int green = color(60, 255, 100);
	private boolean didClickOnce = false;
	private boolean edgesRan= false;
	private boolean colorsRan= false;
	private PImage original;
	private PImage edgedPic;
	private PImage coloredPic;
	private Region densest;
	private Region colors;
	private Region best;
	private int testCount = 0;
	private int testAmount=200;
	private int x = 0;
	private int y = 0;
	private int x2 = 0;
	private int y2 = 0;
	private ArrayList<EdgeRegion> greatestEdges = new ArrayList<EdgeRegion>();
	private ArrayList<ColorRegion> bestColors = new ArrayList<ColorRegion>();

	public void setup() {
		densest = new EdgeRegion(null, 3, 3, 3, 3);
		colors = new ColorRegion(null, 3, 3, 3, 3);
		original = loadImage(LOCATION);
		edgedPic=edgeDetection(original);
		coloredPic=colorSnap(original);
		int width = original.width;
		int height = original.height;
		size(width *2, height*2);
		image(edgedPic, 0, 0);
		image(coloredPic, width, 0);
		image(original, 0, height);
		image(original, width, height);	
	}
	
	//run through tests and display results
	public void draw() {	
//		test();
		if (colorsRan==false) {
			if (edgesRan==false) 
				edges();
			else
				colors();
		} else {
			System.out.println("done with colors");
			for (Region best : bestColors) {
				drawRectangle(best.getX(), best.getY()+(height/2),
						best.getWidth(), best.getHeight(), pink, 3);
				
			}
			for (Region greatest : greatestEdges) {
				drawRectangle(greatest.getX(), greatest.getY()+(height/2),
						greatest.getWidth(), greatest.getHeight(), pink, 3);
			}
			
			
			noLoop();
			best=determineBest();
			drawRectangle(best.getX()+(width/2), best.getY()+(height/2), best.getWidth(), best.getHeight(), pink, 5);
		}
	}
	
	public Region determineBest() {
		best=new Region(null, 0, 0, 0, 0);
		best.setScore(Integer.MAX_VALUE);
		double bestScoreSoFar=best.getScore();
		double regionScore=0;
		for (Region bestest: bestColors) {
			EdgeRegion temp=new EdgeRegion(edgedPic, bestest.getX(), bestest.getY(), bestest.getWidth(), bestest.getHeight());
			regionScore=.45*temp.getScore()+.55*bestest.getScore();
			if (regionScore<bestScoreSoFar) {
				best=(Region) bestest;
				best.setScore(regionScore);
				bestScoreSoFar=best.getScore();
			}
		}
		for (Region greatest: greatestEdges) {
			ColorRegion temp=new ColorRegion(coloredPic, greatest.getX(), greatest.getY(), greatest.getWidth(), greatest.getHeight());
			regionScore=.45*temp.getScore()+.55*best.getScore();
			if (regionScore<bestScoreSoFar) {
				best=(Region) greatest;
				best.setScore(regionScore);
				bestScoreSoFar=best.getScore();
			}
		}
		return best;
	}
	
	//manually draw testing regions to find ratios for various areas of picture
	public void test() {
		PImage img2 = coloredPic;
		img2.loadPixels();
		 if (didClickOnce == false && testCount<1) {
		 Region reg = new ColorRegion(img2, x, y, Math.abs(x2 - x),
		 Math.abs(y2 - y));
		 System.out.println(reg.getRatio());
		 drawRectangle(reg.getX(), reg.getY(), reg.getWidth(),
		 reg.getHeight(), pink, 5);
		 System.out.println(
		 reg.getRatio());
		 testCount=1;
		 }
	}
	
	//used for manual data mining
	public void mouseClicked() {
		if (didClickOnce == false) {
			x = mouseX;
			y = mouseY;
			System.out.println("Source =" + mouseX + ", " + mouseY);
			didClickOnce = true;
		} else {
			x2 = mouseX;
			y2 = mouseY;
			System.out.println("Target =" + mouseX + ", " + mouseY);
			didClickOnce = false;
			testCount = 0;
		}
	}

	//draws rectangle where (x, y) is position of top left corner of rectangle
	public void drawRectangle(int x, int y, int w, int h, int color, int weight) {
		strokeWeight(weight);
		stroke(color);
		line(x, y, x + w, y);
		strokeWeight(weight);
		stroke(color);
		line(x, y, x, y + h);
		strokeWeight(weight);
		stroke(color);
		line(x + w, y + h, x + w, y);
		strokeWeight(weight);
		stroke(color);
		line(x + w, y + h, x, y + h);
	}

	//runs detection of edges to find license plate
	public void edges() {
		System.out.println("edges() started");
		PImage img2 = edgedPic;
		int h = r.nextInt(60) + 50;
		int w = h * 2;
		if (x + w > width/2 ) {
			y = y + (int) h - 20 ;
			x = 0;
		}
		if (y + h > height/2) {
			y =0 ;
			x = 0;
		}
		drawRectangle(x, y, w, h, green, 2);
		testCount++;
		System.out.println(testCount);
		EdgeRegion temp = new EdgeRegion(img2, x, y, w, h);
		x = x + (int)w - 20;
		System.out.println(temp.getScore()+" "+ densest.getScore());
		if (temp.getScore() < densest.getScore()) {
			System.out.println("added");
			densest = temp;
			greatestEdges.add(temp);
		}
		System.out.println("edges() ended");
		if (testCount > testAmount) {
			image(img2, 0, 0);
			System.out.println("Test Count > "+testAmount);
			if (greatestEdges.size()>2) greatestEdges.remove(0);
			for (EdgeRegion greatest : greatestEdges) {
				//greatest = greatest.randomShifter();
				drawRectangle(greatest.getX(), greatest.getY(),
						greatest.getWidth(), greatest.getHeight(), pink, 3);
				System.out.println(greatest.getX() + " " + greatest.getY()
						+ " " + greatest.getWidth() + " "
						+ greatest.getHeight() + " " + greatest.getScore());
			}
			edgesRan=true;
			x=width/2;
			testCount=0;
			return;
		}
	}
	
	public void colors() {
		System.out.println("colors() started");

		PImage img2 = coloredPic;
		ColorRegion temp=generate4Colors(img2);
		if (temp.getScore() < colors.getScore()) {
			System.out.println("added");
			colors = temp;
			bestColors.add(temp);
		}
		System.out.println("colors() ended");
		if (testCount > testAmount) {
			image(img2, width, 0);
			System.out.println("test Count > "+ testAmount);
			for (ColorRegion best : bestColors) {
				//best = best.randomShifter();
				drawRectangle(best.getX()+(width/2), best.getY(),
						best.getWidth(), best.getHeight(), pink, 3);
				System.out.println((best.getX()+(width/2)) + " " + best.getY()
						+ " " + best.getWidth() + " "
						+ best.getHeight() + " " + best.getScore());
			}
			colorsRan=true;
			return;
		}
	}
	
	public ColorRegion generate4Colors(PImage img2) {
		int h = r.nextInt(60) + 50;
		int w = h * 2;
		if (x + w > width ) {
			y = y + (int) h - 20;
			x = width/2;
		}
		if (y + h > height/2) {
			y = 0;
			x = width/2;
		}
		drawRectangle(x, y, w, h, green, 2);
	
		testCount++;
		System.out.println(testCount);
		int temp=x;
		x = x + (int) w - 20;
		return new ColorRegion(img2, temp-(width/2), y, w, h);
	}
	
	

	public static int truncate(int a) {
		if (a < 0)
			return 0;
		else if (a > 255)
			return 255;
		else
			return a;
	}

	public PImage edgeDetection(PImage pic0) {
		// int[][] filter1 = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };   original filter
		int[][] filter2 = { { 1, 2, 1 }, { 0, 0, 0 }, { -1, -2, -1 } };
		int[][] filter1 = { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } }; 		//only detect vertical edges
		pic0.loadPixels();
		int width = pic0.width;
		int height = pic0.height;

		PImage pic1 = createImage(width, height, RGB);
		pic1.loadPixels();

		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {

				// get 3-by-3 array of colors in neighborhood
				int[][] gray = new int[3][3];
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						gray[i][j] = (int) brightness(pic0.get(x - 1 + i, y - 1
								+ j));
					}
				}

				// apply filter
				int gray1 = 0, gray2 = 0;
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						gray1 += gray[i][j] * filter1[i][j];
						gray2 += gray[i][j] * filter2[i][j];
					}
				}
				int magnitude = 255 - truncate((int) Math.sqrt(gray1 * gray1
						+ gray2 * gray2));
				int grayscale = color(magnitude, magnitude, magnitude);
				pic1.set(x, y, grayscale);

			}
		}
		pic1.updatePixels();
		return pic1;
	}

	public PImage colorSnap(PImage original) {
		original.loadPixels();
		PImage pic = createImage(original.width, original.height, RGB);
		pic.loadPixels();
		PApplet a = original.parent;
		int red = color(255, 0, 0);
		int green = color(0, 255, 0);
		int blue = color(0, 0, 255);
		int black = color(0, 0, 0);
		int white = color(255, 255, 255);
		for (int i = 0; i < original.width; i++) {
			for (int j = 0; j < original.height; j++) {
				int pixel = original.get(i, j);
				float r = a.red(pixel);
				float g = a.green(pixel);
				float b = a.blue(pixel);

				if (a.brightness(pixel) > 200)
					pic.set(i, j, white);
				else if (a.brightness(pixel) < 50)
					pic.set(i, j, black);
				else if (r > b + 10 && r > g + 10)
					pic.set(i, j, red);
				else if (g > r + 10 && g > b + 10)
					pic.set(i, j, green);
				else if (b > r + 10 && b > g + 10)
					pic.set(i, j, blue);
				else
					pic.set(i, j, black);
			}
		}
		pic.updatePixels();
		return pic;
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "Main" });
	}
}
