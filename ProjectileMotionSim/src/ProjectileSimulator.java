import processing.core.PApplet;
import processing.core.PImage;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.LineEvent.Type;

public class ProjectileSimulator extends PApplet {
	
	int width = 1250;
	int height = 750;
	
	double gravity;
	
	int screen = 0; // 0 is menu, 1 is main
	
	int highlighted = -1; // 0-4 for menu items, glows when selected
	int hoverHighlighted = -1; // 0-4 for menu items, glows when mouse is over
	
	String angleU = "°";
	String heightU = "m";
	String velocityU = "m/s";
	String gravityU = "m/s^2";
	
	String angleVal = "45";
	String heightVal = "0";
	String velocityVal = "20";
	String gravityVal = "9.8";
	
	Ball b;
	
	int numLimit = 11;
	
	int textBoxX = 485;
	int textBoxY = 220;
	int textBoxW = width - 700;
	int textBoxH = 75;
	int textBoxInterval = textBoxH + 15;
	
	double bWidth = 275;
	double bHeight = bWidth / 3.0;
	double bX = width/2 - bWidth/2;
	double bY = height/2 - bHeight/2 + 275;
	
	double ilSize = 100; // info logo size - 100 = 100%
	double ilDdefault = 75; // info logo default diameter
	double ilRdefault = ilDdefault/2;
	double ilBdefault = 25; // info logo border radius (distance from edges of window)
	double logoCenterX = (width - ilBdefault - ilDdefault / 2.0);
	double logoCenterY = (height - ilBdefault - ilDdefault / 2.0);
	
	boolean infoPopupShown = false;
	double infoPopupW = width * .80;
	double infoPopupH = height * .70;
	double infoPopupX = width - infoPopupW - ((width - infoPopupW)/2);
	double infoPopupY = height - infoPopupH - 40;
	
	double infoPopupExitX = infoPopupX + 40;
	double infoPopupExitY = infoPopupY + 35;
	double infoPopupExitR = 35;
	
	boolean simRunning = false;
	
	int simRunCounter = 0;
	
	int simHighlight = -1;
	
	long lastTime;
	double simSpeed = 100;
	double simTime = 0;
	double displayTime = 0;
	int buttonSpeed = 1;
	boolean paused = false;
	
	int borderSize = 100;
	int displayY = borderSize;
	int displayH = height - 2 * borderSize;
	int displayW = displayH;
	int displayX = (width - displayW) / 2;
	int borderSize2 = displayX;
	
	int tickSize = 25;
	double tickXInterval = displayW / 10.0;
	double tickYInterval = displayY / 10.0;
	
	int power;
	
	double bigNum;
	
	float sSize = 75;
	float sX = displayX;
	float sY = (displayY - sSize) / 2;
	float sInterval = (float) ((displayW - sSize * 6) / 6 + sSize + 3.4);
	
	PImage pauseButton;
	PImage playButton;
	PImage rewindButton;
	PImage fastForwardButton;
	// PImage blueGridBackground;
	PImage logoBackground;
	PImage infoLogo;
	PImage infoPopup;
	PImage infoPopupExit;
	PImage whiteFilter;
	
	/*
	File menuMoveCursorSound = new File("src/Sounds/Move.wav");
	File menuSelectSound = new File("src/Sounds/Select.wav");
	File menuInfoGrowSound = new File("src/Sounds/");
	File menuInfoShrinkSound = new File("src/Sounds/");
	File menuXSound = new File("src/Sounds/");
	File menuTypeSound = new File("src/Sounds/");
	File menuDeleteeSound = new File("src/Sounds/");
	File simPauseSound = new File("src/Sounds/");
	File simPlaySound = new File("src/Sounds/");
	File simRewindSound = new File("src/Sounds/");
	File simFastForwardSound = new File("src/Sounds/");
	File simBallSound = new File("src/Sounds/");
	File simBallLaunchSound = new File("src/Sounds/");
	File simBallLandSound = new File("src/Sounds/");
	File simReplaySound = new File("src/Sounds/");
	File simBackSound = new File("src/Sounds/");
	
	private static void playClip(File clipFile) throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
		class AudioListener implements LineListener {
			private boolean done = false;
			@Override public synchronized void update(LineEvent event) {
				Type eventType = event.getType();
				if (eventType == Type.STOP || eventType == Type.CLOSE) {
					done = true;
					notifyAll();
				}
			}
			public synchronized void waitUntilDone() throws InterruptedException {
				while (!done) { wait(); }
			}
		}
		AudioListener listener = new AudioListener();
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(clipFile);
		try {
		    Clip clip = AudioSystem.getClip();
		    clip.addLineListener(listener);
		    clip.open(audioInputStream);
		    try {
		      clip.start();
		      listener.waitUntilDone();
		    } finally {
		      clip.close();
		    }
	  	} finally {
	  		audioInputStream.close();
	  	}
	}
	
	public static void playSound(File soundFile) {
		try {
			playClip(soundFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	*/

	public void settings() {
		size(width, height);
		pauseButton = loadImage("https://i.imgur.com/ftB4YvH.png", "png");
		playButton = loadImage("https://i.imgur.com/F0wxmMe.png", "png");
		rewindButton = loadImage("https://i.imgur.com/DjnVRCJ.png", "png");
		fastForwardButton = loadImage("https://i.imgur.com/kZdmqWP.png", "png");
		// blueGridBackground = loadImage("https://i.imgur.com/Ztsc4Bo.jpg", "jpg");
		logoBackground = loadImage("https://i.imgur.com/9bIhxNC.png", "png");
		infoLogo = loadImage("https://i.imgur.com/wdx8j6g.png", "png");
		infoPopup = loadImage("https://i.imgur.com/0f1C02W.png", "png");
		infoPopupExit = loadImage("https://i.imgur.com/Dy9c2Pi.png", "png");
		whiteFilter = loadImage("https://i.imgur.com/qqruspy.png", "png");
	}
	
	/*
	public void drawTitle() { // removed because background has custom logo
		textSize(75);
		fill(0, 0, 0);
		text("ProjectiIe Motion SimuIator", 130, 123);
	}
	*/
	
	public void drawLabels() {
		fill(255, 255, 255);
		stroke(0, 0, 0);
		rect(textBoxX - 300, textBoxY + textBoxInterval * 0, 300 - 15, textBoxH);
		rect(textBoxX - 300, textBoxY + textBoxInterval * 1, 300 - 15, textBoxH);
		rect(textBoxX - 300, textBoxY + textBoxInterval * 2, 300 - 15, textBoxH);
		rect(textBoxX - 300, textBoxY + textBoxInterval * 3, 300 - 15, textBoxH);
		
		textSize(45);
		fill(0, 0, 0);
		text("Angle", textBoxX - 218, textBoxY + textBoxInterval * 0 + textBoxH - 20);
		text("Height", textBoxX - 230, textBoxY + textBoxInterval * 1 + textBoxH - 20);
		text("Velocity", textBoxX - 243, textBoxY + textBoxInterval * 2 + textBoxH - 20);
		text("Gravity", textBoxX - 232, textBoxY + textBoxInterval * 3 + textBoxH - 20);
	}
	
	public void drawTextBoxes() {
		fill(255, 255, 255);
		strokeWeight(5);
		
		if (highlighted == 0) {
			stroke(100, 255, 100);
		} else if (hoverHighlighted == 0) {
			stroke(120, 200, 225);
		} else {
			stroke(0, 0, 0);
		}
		rect(textBoxX, textBoxY + textBoxInterval * 0, textBoxW, textBoxH);
		
		if (highlighted == 1) {
			stroke(100, 255, 100);
		} else if (hoverHighlighted == 1) {
			stroke(120, 200, 225);
		} else {
			stroke(0, 0, 0);
		}
		rect(textBoxX, textBoxY + textBoxInterval * 1, textBoxW, textBoxH);
		
		if (highlighted == 2) {
			stroke(100, 255, 100);
		} else if (hoverHighlighted == 2) {
			stroke(120, 200, 225);
		} else {
			stroke(0, 0, 0);
		}
		rect(textBoxX, textBoxY + textBoxInterval * 2, textBoxW, textBoxH);
		
		if (highlighted == 3) {
			stroke(100, 255, 100);
		} else if (hoverHighlighted == 3) {
			stroke(120, 200, 225);
		} else {
			stroke(0, 0, 0);
		}
		rect(textBoxX, textBoxY + textBoxInterval * 3, textBoxW, textBoxH);
	}
	
	public void drawText() {
		textSize(50);
		
		if (highlighted == 0) {
			fill(50, 50, 50);
		} else {
			fill(0, 0, 0);
		}
		text(angleVal + angleU, textBoxX + 5, textBoxY + textBoxH + textBoxInterval * 0 - 10);
		
		if (highlighted == 1) {
			fill(50, 50, 50);
		} else {
			fill(0, 0, 0);
		}
		text(heightVal + " " + heightU, textBoxX + 5, textBoxY + textBoxH + textBoxInterval * 1 - 10);
		
		if (highlighted == 2) {
			fill(50, 50, 50);
		} else {
			fill(0, 0, 0);
		}
		text(velocityVal + " " + velocityU, textBoxX + 5, textBoxY + textBoxH + textBoxInterval * 2 - 10);
		
		if (highlighted == 3) {
			fill(50, 50, 50);
		} else {
			fill(0, 0, 0);
		}
		text(gravityVal + " " + gravityU, textBoxX + 5, textBoxY + textBoxH + textBoxInterval * 3 - 10);
	}
	
	public void drawStartButton() {
		fill(255, 255, 255);
		if (highlighted == 4) {
			stroke(100, 255, 100);
		} else if (hoverHighlighted == 4) {
			stroke(120, 200, 225);
		} else {
			stroke(0, 0, 0);
		}
		rect((float)(bX), (float)(bY), (float)(bWidth), (float)(bHeight));
		if (highlighted == 4) {
			fill(50, 50, 50);
		} else {
			fill(0, 0, 0);
		}
		textSize(75);
		text("Start", (float)(width/2 - 85), (float)(height/2 + 27 + 275));
		
		mouseHoverBoxes();
	}
	
	public void mouseHoverBoxes() {
		if (mouseX >= textBoxX && mouseX <= textBoxX + textBoxW && mouseY >= textBoxY + textBoxInterval * 0 && mouseY <= textBoxY + textBoxInterval * 0 + textBoxH) {
			hoverHighlighted = 0;
		} else if (mouseX >= textBoxX && mouseX <= textBoxX + textBoxW && mouseY >= textBoxY + textBoxInterval * 1 && mouseY <= textBoxY + textBoxInterval * 1 + textBoxH) {
			hoverHighlighted = 1;
		} else if (mouseX >= textBoxX && mouseX <= textBoxX + textBoxW && mouseY >= textBoxY + textBoxInterval * 2 && mouseY <= textBoxY + textBoxInterval * 2 + textBoxH) {
			hoverHighlighted = 2;
		} else if (mouseX >= textBoxX && mouseX <= textBoxX + textBoxW && mouseY >= textBoxY + textBoxInterval * 3 && mouseY <= textBoxY + textBoxInterval * 3 + textBoxH) {
			hoverHighlighted = 3;
		} else if (mouseX >= bX && mouseX <= bX + bWidth && mouseY >= bY && mouseY <= bY + bHeight) {
			hoverHighlighted = 4;
		} else {
			hoverHighlighted = -1;
		}
	}
	
	public void drawInfoLogo() {
		int ilDcurrent = (int)(ilDdefault * (ilSize/100));
		int ilRcurrent = (int)(ilRdefault * (ilSize/100));
		int ilXcurrent = (int)(logoCenterX - ilRcurrent);
		int ilYcurrent = (int)(logoCenterY - ilRcurrent);
		
		image(infoLogo, ilXcurrent, ilYcurrent, ilDcurrent, ilDcurrent);
		mouseHoverInfo();
	}
	
	public void mouseHoverInfo() {
		if (((Math.pow(mouseX - logoCenterX, 2) + Math.pow(mouseY - logoCenterY, 2)) <= Math.pow(ilDdefault/2, 2)) && !infoPopupShown) { //mouse is over default logo
			ilSize = 125;
		} else {
			ilSize = 100;
		}
	}
	
	public void drawInfoPopup() {
		image(whiteFilter, 0, 0, width, height);
		image(infoPopup, (float)infoPopupX, (float)infoPopupY, (float)infoPopupW, (float)infoPopupH);
		image(infoPopupExit, (float)(infoPopupExitX - infoPopupExitR), (float)(infoPopupExitY - infoPopupExitR), (float)infoPopupExitR*2, (float)infoPopupExitR*2);
	}
	
	public void startSim() {
		if (angleVal.length() <= 0) {
			angleVal = "45";
		}
		if (heightVal.length() <= 0) {
			heightVal = "0";
		}
		if (velocityVal.length() <= 0) {
			velocityVal = "20";
		}
		if (gravityVal.length() <= 0) {
			gravityVal = "9.6";
		}
		b = new Ball(Double.parseDouble(angleVal), Double.parseDouble(heightVal), Double.parseDouble(velocityVal), Double.parseDouble(gravityVal));
		screen = 1;
	}
	
	public void updateTime() {
		if (!paused) {
			simTime += buttonSpeed * (System.currentTimeMillis() - lastTime);
		}
		lastTime = System.currentTimeMillis();
		displayTime = simTime / 1000.0;
		if (displayTime >= b.getHangTime()) {
			displayTime = b.getHangTime();
			simTime = b.getHangTime() * 1000.0;
			buttonSpeed = 0;
		}
		if (displayTime <= 0) {
			displayTime = 0;
			simTime = 0;
			buttonSpeed = 0;
		}
	}
	
	public void drawBorder() {
		fill(60, 90, 210);
		strokeWeight(0);
		stroke(60, 90, 210);
		rect(0, 0, width, borderSize);
		rect(0, displayY + displayH, width, borderSize);
		rect(0, displayY, borderSize2, displayH);
		rect(displayX + displayW, displayY, borderSize2, displayH);
	}
	
	public void drawTicks(double x, double y) {
		bigNum = x;
		if (y > bigNum) {
			bigNum = y;
		}
		String n = "" + bigNum;
		if (bigNum > 1) {
			power = ("" + (int)bigNum).length() - 1;
		}
		else {
			power = 2;
			for (int i = 0; i < n.length(); i++) {
				if (n.substring(i, i+1) == ".") {
					power--;
				}
				else if (n.substring(i, i+1) == "0") {
					power--;
				}
				else {
					power--;
					break;
				}
			}
		}
		strokeWeight(2);
		stroke(200, 200, 200);
		fill(255, 255, 255);
		textSize(20);
		for (int i = 0; i < 11; i++) {
			if (power >= 5) {
				text(i + " x 10^" + power, (float) (displayX - 125 - (power/10 * 5)), (float) (displayY + displayH - tickXInterval * i + 10)); // y
				if (i % 2 == 0) {
					text(i + " x 10^" + power, (float) (displayX + tickXInterval * i - 45 - (power/10 * 5)), (float) (displayY + displayH + 50)); // x
				}
				else {
					text(i + " x 10^" + power, (float) (displayX + tickXInterval * i - 45 - (power/10 * 5)), (float) (displayY + displayH + 75)); // x
				}
			}
			else {
				text((int)(i * Math.pow(10, power)) + "", (float) (displayX - 100), (float) (displayY + displayH - tickXInterval * i + 10)); // y
				if (i % 2 == 0) {
					text((int)(i * Math.pow(10, power)) + "", (float) (displayX + tickXInterval * i - power * 6), (float) (displayY + displayH + 50)); // x
				}
				else {
					text((int)(i * Math.pow(10, power)) + "", (float) (displayX + tickXInterval * i - power * 6), (float) (displayY + displayH + 75)); // x
				}
			}
			line((float) (displayX + displayW), (float) (displayY + displayH - tickXInterval * i), (float) (displayX - tickSize), (float) (displayY + displayH - tickXInterval * i)); // y
			if (i % 2 == 0) {
				line((float) (displayX + tickXInterval * i), (float) (displayY), (float) (displayX + tickXInterval * i), (float) (displayY + displayH + tickSize)); // x
			}
			else {
				line((float) (displayX + tickXInterval * i), (float) (displayY), (float) (displayX + tickXInterval * i), (float) (displayY + displayH + tickSize * 2)); // x
			}
		}
	}
	
	public void drawArc() {
		fill(0, 0, 0);
		strokeWeight(0);
		stroke(0, 0, 0);
		for (double d = 0; d < displayTime; d += b.getHangTime() / 200.0) {
			double bX = b.distanceAt(d); // scale of 0-100/1000/10000etc - total range of width
			double bY = b.heightAt(d); // scale of 0-100/1000/10000etc - total range of height
			double newX = bX * ((double)displayW / (Math.pow(10, power + 1))) + displayX; // scale of displayX-(displayX+displayW) - total range of displayW
			double newY = bY * ((double)displayH / (Math.pow(10, power + 1))) + displayY; // scale of displayY-(displayY+displayH) - total range of displayH
			circle((float) (newX), (float) (height - newY), 5);
		}
	}
	
	public void drawHeight() {
		if (displayTime > b.timeAtY(b.getMaxHeight())) {
			double bX = b.distanceAt(b.timeAtY(b.getMaxHeight())); // scale of 0-100/1000/10000etc - total range of width
			double bY = b.getMaxHeight(); // scale of 0-100/1000/10000etc - total range of height
			float newX = (float) (bX * ((double)displayW / (Math.pow(10, power + 1))) + displayX); // scale of displayX-(displayX+displayW) - total range of displayW
			float newY = (float) (bY * ((double)displayH / (Math.pow(10, power + 1))) + displayY); // scale of displayY-(displayY+displayH) - total range of displayH
			stroke(0, 0, 0);
			strokeWeight(2);
			line(newX, height - newY, newX, displayY + displayH);
			fill(0, 0, 0);
			circle(newX, height - newY, 7);
			textSize(15);
			text("(" + (Math.round(b.distanceAt(b.timeAtY(b.getMaxHeight())) * 1000) / 1000.0) + ", " + (Math.round(b.getMaxHeight() * 1000) / 1000.0) + ")", newX - 60, height - newY - 10);
		}
	}
	
	public void drawDistance() {
		if (displayTime >= b.getHangTime()) {
			double bX = b.distanceAt(b.timeAtX(b.getDistance())); // scale of 0-100/1000/10000etc - total range of width
			double bY = 0; // scale of 0-100/1000/10000etc - total range of height
			float newX = (float) (bX * ((double)displayW / (Math.pow(10, power + 1))) + displayX); // scale of displayX-(displayX+displayW) - total range of displayW
			float newY = (float) (bY * ((double)displayH / (Math.pow(10, power + 1))) + displayY); // scale of displayY-(displayY+displayH) - total range of displayH
			fill(0, 0, 0);
			textSize(15);
			text("(" + (Math.round(bX * 1000) / 1000.0) + ", 0)", newX + 5, height - newY - 10);
		}
	}
	
	public void drawBall() {
		fill(220, 35, 75);
		stroke(0, 0, 0);
		strokeWeight(2);
		double bX = b.distanceAt(displayTime); // scale of 0-100/1000/10000etc - total range of width
		double bY = b.heightAt(displayTime); // scale of 0-100/1000/10000etc - total range of height
		double newX = bX * ((double)displayW / (Math.pow(10, power + 1))) + displayX; // scale of displayX-(displayX+displayW) - total range of displayW
		double newY = bY * ((double)displayH / (Math.pow(10, power + 1))) + displayY; // scale of displayY-(displayY+displayH) - total range of displayH
		circle((float) (newX), (float) (height - newY), 20);
		// stroke(255, 255, 255);
		// fill(255, 255, 255);
		// circle((float) (newX), (float) (height - newY), 2);
	}
	
	public void redrawBorder() {
		drawBorder();
		strokeWeight(2);
		stroke(200, 200, 200);
		fill(255, 255, 255);
		textSize(20);
		for (int i = 0; i < 11; i++) {
			if (power >= 5) {
				text(i + " x 10^" + power, (float) (displayX - 125 - (power/10 * 5)), (float) (displayY + displayH - tickXInterval * i + 10)); // y
				if (i % 2 == 0) {
					text(i + " x 10^" + power, (float) (displayX + tickXInterval * i - 45 - (power/10 * 5)), (float) (displayY + displayH + 50)); // x
				}
				else {
					text(i + " x 10^" + power, (float) (displayX + tickXInterval * i - 45 - (power/10 * 5)), (float) (displayY + displayH + 75)); // x
				}
			}
			else {
				text((int)(i * Math.pow(10, power)) + "", (float) (displayX - 100), (float) (displayY + displayH - tickXInterval * i + 10)); // y
				if (i % 2 == 0) {
					text((int)(i * Math.pow(10, power)) + "", (float) (displayX + tickXInterval * i - power * 6), (float) (displayY + displayH + 50)); // x
				}
				else {
					text((int)(i * Math.pow(10, power)) + "", (float) (displayX + tickXInterval * i - power * 6), (float) (displayY + displayH + 75)); // x
				}
			}
			line((float) (displayX), (float) (displayY + displayH - tickXInterval * i), (float) (displayX - tickSize), (float) (displayY + displayH - tickXInterval * i)); // y
			if (i % 2 == 0) {
				line((float) (displayX + tickXInterval * i), (float) (displayY + displayH), (float) (displayX + tickXInterval * i), (float) (displayY + displayH + tickSize)); // x
			}
			else {
				line((float) (displayX + tickXInterval * i), (float) (displayY + displayH), (float) (displayX + tickXInterval * i), (float) (displayY + displayH + tickSize * 2)); // x
			}
		}
	}
	
	public void drawTime() {
		stroke(0, 0, 0);
		strokeWeight(5);
		fill(255, 255, 255);
		rect(sX + sInterval * 2, sY + 2, sInterval + sSize, sSize - 5);
		textSize(25);
		fill(0, 0, 0);
		text(((Math.round(displayTime * 100.0) / 100.0) + " s"), sX + sInterval * 2 + 25, sY + 60);
		String speedStr;
		if (paused) {
			speedStr = "0";
		} else {
			speedStr = "" + buttonSpeed;
		}
		text("Speed: " + speedStr, sX + sInterval*2 + 25, sY + 30);
	}
	
	public void drawSpeedControls() {
		stroke(225, 120, 155);
		strokeWeight(5);
		noFill();
		image(rewindButton, sX + sInterval * 0, sY, sSize, sSize);
		if (simHighlight == 0) {
			rect(sX + sInterval * 0 + 2, sY + 2, sSize - 5, sSize - 5);
		}
		image(fastForwardButton, sX + sInterval * 1, sY, sSize, sSize);
		if (simHighlight == 1) {
			rect(sX + sInterval * 1 + 2, sY + 2, sSize - 5, sSize - 5);
		}
		image(playButton, sX + sInterval * 4, sY, sSize, sSize);
		if (simHighlight == 2) {
			rect(sX + sInterval * 4 + 2, sY + 2, sSize - 5, sSize - 5);
		}
		image(pauseButton, sX + sInterval * 5, sY, sSize, sSize);
		if (simHighlight == 3) {
			rect(sX + sInterval * 5 + 2, sY + 2, sSize - 5, sSize - 5);
		}
	}
	
	public void speedMouseHover(int i) {
		if (i != -1) {
			if (mouseX >= sX + sInterval * 0 && mouseX <= sX + sInterval * 0 + sSize && mouseY >= sY && mouseY <= sY + sSize) { // rewind
				simHighlight = 0;
			} else if (mouseX >= sX + sInterval * 1 && mouseX <= sX + sInterval * 1 + sSize && mouseY >= sY && mouseY <= sY + sSize) { // fast forward
				simHighlight = 1;
			} else if (mouseX >= sX + sInterval * 4 && mouseX <= sX + sInterval * 4 + sSize && mouseY >= sY && mouseY <= sY + sSize) { // play
				simHighlight = 2;
			} else if (mouseX >= sX + sInterval * 5 && mouseX <= sX + sInterval * 5 + sSize && mouseY >= sY && mouseY <= sY + sSize) { // pause
				simHighlight = 3;
			} else if (mouseX >= 20 && mouseX <= 20 + sInterval + sSize && mouseY >= height - sSize - 20 && mouseY <= height - 20) { // back
				simHighlight = 4;
			} else if (mouseX >= width - sInterval - sSize - 20 && mouseX <= width - 20 && mouseY >= height - sSize - 20 && mouseY <= height - 20) { // replay
				simHighlight = 5;
			} else {
				simHighlight = -1;
			}
		} else {
			if (mouseX >= 20 && mouseX <= 20 + sInterval + sSize && mouseY >= height - sSize - 20 && mouseY <= height - 20) { // back
				simHighlight = 4;
			} else if (mouseX >= width - sInterval - sSize - 20 && mouseX <= width - 20 && mouseY >= height - sSize - 20 && mouseY <= height - 20) { // replay
				simHighlight = 5;
			} else {
				simHighlight = -1;
			}
		}
	}
	
	public void drawBottomButtons() {
		stroke(220, 35, 75);
		strokeWeight(5);
		fill(255, 255, 255);
		if (simHighlight == 4) {
			stroke(225, 120, 155);
		} else {
			stroke(220, 35, 75);
		}
		rect(0 + 20, height - sSize - 20, sInterval + sSize, sSize);
		if (simHighlight == 5) {
			stroke(225, 120, 155);
		} else {
			stroke(220, 35, 75);
		}
		rect(width - sInterval - sSize - 20, height - sSize - 20, sInterval + sSize, sSize);
		textSize(40);
		fill(0, 0, 0);
		text("Back", 57, height - 42);
		text("Replay", width - sInterval - sSize + 1, height - 42);
	}
	
	public void drawInputOutput() {
		stroke(0, 0, 0);
		strokeWeight(5);
		fill(255, 255, 255);
		rect(20, displayY + (displayH / 10), sInterval + sSize, (int) (displayH * .8));
		rect(width - sInterval - sSize - 20, displayY + (displayH / 10), sInterval + sSize, (int) (displayH * .8));
		fill(0, 0, 0);
		int spacer = displayY + (displayH / 10) + 15;
		textSize(35);
		text("Angle", 55, spacer + 40);
		text("Height", 48, spacer + 140);
		text("Velocity", 37, spacer + 240);
		text("Gravity", 44, spacer + 340);
		textSize(15);
		text(angleVal + angleU, 30, spacer + 80);
		text(heightVal + heightU, 30, spacer + 180);
		text(velocityVal + velocityU, 30, spacer + 280);
		text(gravityVal + gravityU, 30, spacer + 380);
		textSize(25);
		text("Hang Time", width - 170, spacer + 40);
		text("Max Height", width - 175, spacer + 140);
		text("Distance", width - 160, spacer + 240);
		textSize(18);
		text("Terminal Velocity", width - 182, spacer + 340);
		textSize(15);
		double hTVal = Math.round(b.getHangTime() * 100000000) / 100000000.0;
		double mHVal = Math.round(b.getMaxHeight() * 100000000) / 100000000.0;
		double dVal = Math.round(b.getDistance() * 100000000) / 100000000.0;
		double tVVal = Math.round(b.getMaxVelocity() * 100000000) / 100000000.0;
		text(hTVal + "s", -10 + width - sInterval - sSize, spacer + 80);
		text(mHVal + heightU, -10 + width - sInterval - sSize, spacer + 180);
		text(dVal + heightU, -10 + width - sInterval - sSize, spacer + 280);
		text(tVVal + velocityU, -10 + width - sInterval - sSize, spacer + 380);
	}
	
	public void errorTooMuch() {
		stroke(220, 35, 75);
		strokeWeight(1);
		fill(220, 35, 75);
		rect(displayX, displayY, displayW, displayH);
		fill(0, 0, 0);
		textSize(50);
		text("ERROR", displayX + 185, displayY + 200);
		textSize(25);
		text("Numbers are too large", displayX + 135, displayY + 300);
	}
	
	public void errorNegAngle() {
		stroke(220, 35, 75);
		strokeWeight(1);
		fill(220, 35, 75);
		rect(displayX, displayY, displayW, displayH);
		fill(0, 0, 0);
		textSize(50);
		text("ERROR", displayX + 185, displayY + 200);
		textSize(25);
		text("The angle must be less than 180", displayX + 80, displayY + 300);
	}
	
	public void draw() {
		if (screen == 0) {
			background(60, 90, 210);
			image(logoBackground, 0, 0, width, height);
			// drawTitle();
			drawLabels();
			drawTextBoxes();
			drawText();
			drawStartButton();
			drawInfoLogo();
			if (infoPopupShown) {
				drawInfoPopup();
			}
		} else {
			background(255, 255, 255); // grid color
			if (simRunning) {
				if (!paused) {
					updateTime();
				}
			}
			else {
				simRunCounter++;
				if (simRunCounter >= 40) {
					simRunning = true;
					simRunCounter = 0;
					lastTime = System.currentTimeMillis();
				}
			}
			drawBorder();
			if (b.getAngle() % 360.0 >= 180) {
				errorNegAngle();
				speedMouseHover(-1);
			}
			else if (!(bigNum >= Math.pow(10, 10))) {
				drawTicks(b.getDistance(), b.getMaxHeight());
				drawArc();
				drawBall();
				redrawBorder();
				drawHeight();
				drawDistance();
				drawTime();
				speedMouseHover(0);
				drawSpeedControls();
			} else {
				errorTooMuch();
				speedMouseHover(-1);
			}
			drawBottomButtons();
			drawInputOutput();
			/*
			debug
			
			fill(255, 255, 255);
			textSize(10);
			text("Max Height: " + b.getMaxHeight(), 10, 20);
			text("Time at max height: " + b.timeAtY(b.getMaxHeight()), 10, 35);
			text("Max Height: " + b.heightAt(b.timeAtY(b.getMaxHeight())), 10, 50);
			text("Distance: " + b.getDistance(), 10, 65);
			text("Time at max distance: " + b.timeAtX(b.getDistance()), 10, 80);
			text("Distance: " + b.distanceAt(b.timeAtX(b.getDistance())), 10, 95);
			*/
		}
	}
	
	public void mousePressed() {
		if (screen == 0 && !infoPopupShown) {
			if (mouseX >= textBoxX && mouseX <= textBoxX + textBoxW) {
				if (mouseY >= textBoxY + textBoxInterval * 0 && mouseY <= textBoxY + textBoxInterval * 0 + textBoxH) {
					highlighted = 0;
				} else if (mouseY >= textBoxY + textBoxInterval * 1 && mouseY <= textBoxY + textBoxInterval * 1 + textBoxH) {
					highlighted = 1;
				} else if (mouseY >= textBoxY + textBoxInterval * 2 && mouseY <= textBoxY + textBoxInterval * 2 + textBoxH) {
					highlighted = 2;
				} else if (mouseY >= textBoxY + textBoxInterval * 3 && mouseY <= textBoxY + textBoxInterval * 3 + textBoxH) {
					highlighted = 3;
				}
			}
			if (mouseX >= bX && mouseX <= bX + bWidth && mouseY >= bY && mouseY <= bY + bHeight) {
				startSim();
			}
			if (((Math.pow(mouseX - logoCenterX, 2) + Math.pow(mouseY - logoCenterY, 2)) <= Math.pow(ilDdefault/2, 2)) && !infoPopupShown) { //mouse is over default info logo
				infoPopupShown = true;
			}
		}
		else if (screen == 0 && infoPopupShown) {
			if (((Math.pow(mouseX - infoPopupExitX, 2) + Math.pow(mouseY - infoPopupExitY, 2)) <= Math.pow(infoPopupExitR, 2)) && infoPopupShown) { //mouse is over red X
				infoPopupShown = false;
			}
		}
		else if (screen == 1) {
			if (simRunning) {
				if (mouseX >= sX + sInterval * 0 && mouseX <= sX + sInterval * 0 + sSize && mouseY >= sY && mouseY <= sY + sSize) { // rewind
					if (paused) {
						paused = false;
						lastTime = System.currentTimeMillis();
					}
					if (buttonSpeed >= 0) {
						buttonSpeed = -1;
					}
					else {
						buttonSpeed *= 2;
					}
				}
				if (mouseX >= sX + sInterval * 1 && mouseX <= sX + sInterval * 1 + sSize && mouseY >= sY && mouseY <= sY + sSize) { // fast forward
					if (paused) {
						paused = false;
						lastTime = System.currentTimeMillis();
					}
					if (buttonSpeed <= 0) {
						buttonSpeed = 1;
					}
					else {
						buttonSpeed *= 2;
					}
				}
				if (mouseX >= sX + sInterval * 4 && mouseX <= sX + sInterval * 4 + sSize && mouseY >= sY && mouseY <= sY + sSize) { // play
					if (paused) {
						paused = false;
						lastTime = System.currentTimeMillis();
					}
					else {
						buttonSpeed = 1;
					}
				}
				if (mouseX >= sX + sInterval * 5 && mouseX <= sX + sInterval * 5 + sSize && mouseY >= sY && mouseY <= sY + sSize) { // pause
					if (!paused) {
						paused = true;
					}
				}
				if (mouseX >= 20 && mouseX <= 20 + sInterval + sSize && mouseY >= height - sSize - 20 && mouseY <= height - 20) { // back
					screen = 0;
					highlighted = -1;
					paused = false;
					buttonSpeed = 1;
					simTime = 0;
					displayTime = 0;
					simRunCounter = 0;
					simRunning = false;
				}
				if (mouseX >= width - sInterval - sSize - 20 && mouseX <= width - 20 && mouseY >= height - sSize - 20 && mouseY <= height - 20) { // restart
					paused = false;
					buttonSpeed = 1;
					simTime = 0;
					displayTime = 0;
					simRunCounter = 0;
					simRunning = false;
				}
			}
		}
	}
	
	public void keyPressed() {
		if (screen == 0 && !infoPopupShown) {
			if ((keyCode >= 48 && keyCode <= 57) || keyCode == 46 || keyCode == BACKSPACE) {
				if (highlighted == 0) {
					if (keyCode == BACKSPACE) {
						if (angleVal.length() > 0) {
							angleVal = angleVal.substring(0, angleVal.length() - 1);
						}
					}
					else if (angleVal.length() < numLimit + 5) {
						if (keyCode == 46) {
							if (!angleVal.contains(".")) {
								angleVal += ".";
							}
						}
						else {
							angleVal += keyCode - 48;
						}
					}
				}
				else if (highlighted == 1) {
					if (keyCode == BACKSPACE) {
						if (heightVal.length() > 0) {
							heightVal = heightVal.substring(0, heightVal.length() - 1);
						}
					}
					else if (heightVal.length() < numLimit + 4) {
						if (keyCode == 46) {
							if (!heightVal.contains(".")) {
								heightVal += ".";
							}
						}
						else {
							heightVal += keyCode - 48;
						}
					}
				}
				else if (highlighted == 2) {
					if (keyCode == BACKSPACE) {
						if (velocityVal.length() > 0) {
							velocityVal = velocityVal.substring(0, velocityVal.length() - 1);
						}
					}
					else if (velocityVal.length() < numLimit + 2) {
						if (keyCode == 46) {
							if (!velocityVal.contains(".")) {
								velocityVal += ".";
							}
						}
						else {
							velocityVal += keyCode - 48;
						}
					}
				}
				else if (highlighted == 3) {
					if (keyCode == BACKSPACE) {
						if (gravityVal.length() > 0) {
							gravityVal = gravityVal.substring(0, gravityVal.length() - 1);
						}
					}
					else if (gravityVal.length() < numLimit) {
						if (keyCode == 46) {
							if (!gravityVal.contains(".")) {
								gravityVal += ".";
							}
						}
						else {
							gravityVal += keyCode - 48;
						}
					}
				}
			}
			else if (keyCode == UP) {
			     highlighted--;
			     // playSound(menuMoveCursorSound);
			     if (highlighted == -1 || highlighted == -2) {
			    	 highlighted = 4;
			     }
			}
			else if (keyCode == DOWN || keyCode == 32) {
				highlighted++;
				if (highlighted == 5) {
					highlighted = 0;
				}
			}
			else if (keyCode == ENTER) {
				if (highlighted == 4) {
					startSim();
				}
				else {
					highlighted++;
				}
			}
			else if (keyCode == ESC) { // ESC closes application
				highlighted = -1;
			}
		}
		else if (screen == 0 && infoPopupShown) {
			if (keyCode == ESC || keyCode == 32 || keyCode == ENTER) { // ESC closes application
				infoPopupShown = false;
			} 
		}
		else if (screen == 1) {
			if (keyCode == 32) {
				if (paused) {
					paused = false;
					lastTime = System.currentTimeMillis();
				} else {
					paused = true;
				}
			}
			else if (keyCode == LEFT) {
				if (paused) {
					paused = false;
					lastTime = System.currentTimeMillis();
				}
				if (buttonSpeed >= 0) {
					buttonSpeed = -1;
				}
				else {
					buttonSpeed *= 2;
				}
			}
			else if (keyCode == RIGHT) {
				if (paused) {
					paused = false;
					lastTime = System.currentTimeMillis();
				}
				if (buttonSpeed <= 0) {
					buttonSpeed = 1;
				}
				else {
					buttonSpeed *= 2;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		String[] processingArgs = {"Projectile Simulator"};
		ProjectileSimulator ballSimSketch = new ProjectileSimulator();
		PApplet.runSketch(processingArgs, ballSimSketch);
	}

}
