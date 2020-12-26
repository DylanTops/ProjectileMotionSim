public class Ball {
	private double height, initialVelocity, angle, gravity; // inputs
	private double xVelocity, yVelocity, hangTime1, hangTime2, hangTime, maxHeight,
		distanceTraveled, maxYVelocity, maxVelocity; // outputs/intermediaries
	
	public Ball(double angle, double height, double initialVelocity, double gravity) {
		this.angle = angle; // degrees
		this.height = height; // meters
		this.initialVelocity = initialVelocity; // meters/second
		this.gravity = gravity; // meters/second^2
		calculate();
	}
	
	public void calculate() { // calculates all variables
		yVelocity = Math.sin(Math.toRadians(angle)) * initialVelocity;
		xVelocity = Math.cos(Math.toRadians(angle)) * initialVelocity;
		hangTime1 = yVelocity / gravity;
		maxHeight = height + (yVelocity * hangTime1) - ((gravity/2) * hangTime1 * hangTime1);
		hangTime2 = Math.sqrt(maxHeight/(gravity/2));
		hangTime = hangTime1 + hangTime2;
		distanceTraveled = xVelocity * hangTime;
		maxYVelocity = yVelocity - gravity * hangTime;
		maxVelocity = Math.sqrt(xVelocity * xVelocity + maxYVelocity * maxYVelocity);
	}
	
	public double getAngle() {
		return angle;
	}
	
	public double getHeight() {
		return height;
	}
	
	public double getVelocity() {
		return initialVelocity;
	}
	
	public double getGravity() {
		return gravity;
	}
	
	public double getHangTime() {
		return hangTime;
	}
	
	public double getMaxHeight() {
		return maxHeight;
	}
	
	public double getDistance() {
		return distanceTraveled;
	}
	
	public double getMaxVelocity() {
		return maxVelocity;
	}
	
	public float heightAt(double seconds) {
		float m = (float) (-gravity/2.0 * seconds * seconds + yVelocity * seconds + height);
		if (seconds >= hangTime) {
			return 0;
		}
		else {
			return m;
		}
	}
	
	public float distanceAt(double seconds) {
		float m = (float) (seconds * xVelocity);
		if (seconds >= hangTime) {
			return (float) distanceTraveled;
		}
		else {
			return m;
		}
	}
	
	public float timeAtX(double x) {
		return (float) (x / xVelocity);
	}
	
	public float timeAtY(double y) {
		return (float) ((-yVelocity + Math.sqrt(yVelocity * yVelocity - 2 * gravity * (y - height))) / -gravity);
	}
}
