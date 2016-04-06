import java.awt.Rectangle;

/**
 * Human.java
 * 
 * The player's character. This class should have fields that can represent the
 * players' current location, the location to which it is trying to go, a
 * relative speed, and a number of bombs, and a Rectangle representing the
 * player's hitbox. Other fields for the size of the player and the hitbox may
 * be useful, along with frame information if you do animation. You should
 * create methods for the following: 1. checking to see if the player's hitbox
 * Rectangle is colliding with any other hitbox Rectangle (just obstacle
 * Rectangles - Zombies can handle Human collision) 2. movement (normal movement
 * and what to do if there is a collision) 3. constructors 4. getters/setters 5.
 * bomb counting 6. anything else you may need.
 * 
 * @authors
 * @compids
 * @lab
 * 
 */
public class Human {

	private static float xCoord;
	private static float yCoord;
	private static float xDestCoord;
	private static float yDestCoord;
	private float speed = 100;
	private int bombCount;
	private Rectangle hitbox;
	private int direction; // up - 1; down - 2; left - 3; right - 4
	private boolean collisionDetect;
	private int zombieCount;

	// constructor

	public Human() {
		super();
		this.xCoord = 350;
		this.yCoord = 350;
		this.xDestCoord = xDestCoord;
		this.yDestCoord = yDestCoord;
		this.bombCount = 3;
		this.hitbox = new Rectangle(350 + 10, 350 + 10, 28, 61);
	}

	// hitbox movement
	public Rectangle hitboxMovements() {

		hitbox.setLocation((int) this.xCoord + 10, (int) this.yCoord + 10);

		return hitbox;
	}

	// littleGirl movement

	public void move(float elapsedTime) {
		
		float x1 = this.xCoord;
		float y1 = this.yCoord;
		float x2 = this.xDestCoord - 24;
		float y2 = this.yDestCoord - 40;
		float dx = x1 - x2;
		float dy = y1 - y2;
		double absAngle = (Math.atan(Math.abs(dy) / Math.abs(dx)))
				* (180 / Math.PI);

		if (dx < 0) {
			if (dy < 0) { // upper left quadrant
				if (absAngle >= 45) {
					direction = 2;
				} else if (absAngle < 45) {
					direction = 4;
				}
			} else if (dy > 0) { // lower left quadrant
				if (absAngle >= 45) {
					direction = 1;
				} else if (absAngle < 45) {
					direction = 4;
				}
			}
		} else if (dx > 0) {
			if (dy < 0) { // upper right quadrant
				if (absAngle >= 45) {
					direction = 2;
				} else if (absAngle < 45) {
					direction = 3;
				}
			} else if (dy > 0) { // lower right quadrant
				if (absAngle >= 45) {
					direction = 1;
				} else if (absAngle < 45) {
					direction = 3;
				}
			}
		}

		if (dy > 0) {
			this.yCoord -= this.speed * (elapsedTime);
		} else if (dy < 0) {
			this.yCoord += this.speed * (elapsedTime);
		}

		if (dx > 0) {
			this.xCoord -= this.speed * (elapsedTime);
		} else if (dx < 0) {
			this.xCoord += this.speed * (elapsedTime);
		}

		hitboxMovements();

	}

	// obstacle collision effect

	public void obstacleCollisionEffect(Rectangle compareTo) {

		Rectangle currentHitbox = this.getHitbox();

		if (currentHitbox.intersects(compareTo) == true) {

			Rectangle temp = currentHitbox.intersection(compareTo);
			double tempWidth = temp.getWidth();
			double tempHeight = temp.getHeight();
			
			if (currentHitbox.getWidth() != tempWidth && currentHitbox.getHeight() != tempHeight){
				if(tempWidth < tempHeight){
					if (currentHitbox.getX() > compareTo.getX()) {
						this.xCoord += temp.getWidth();
					} else {
						this.xCoord -= temp.getWidth();
					}
				}
				else if (tempWidth > tempHeight){
					if (currentHitbox.getY() > compareTo.getY()) {
						this.yCoord += temp.getHeight();
					} else {
						this.yCoord -= temp.getHeight();
					}
				}
			}
			
			if (currentHitbox.getWidth() == temp.getWidth()) {
				if (currentHitbox.getY() > compareTo.getY()) {
					this.yCoord += temp.getHeight();
				} else {
					this.yCoord -= temp.getHeight();
				}
			}
			if (currentHitbox.getHeight() == temp.getHeight()) {
				if (currentHitbox.getX() > compareTo.getX()) {
					this.xCoord += temp.getWidth();
				} else {
					this.xCoord -= temp.getWidth();
				}

			}

		}
	}
	
	

	// human/zombie collision effect

	public void zombieCollisionEffect(Zombie compareTo) {

		Rectangle currentHitbox = this.getHitbox();

		if (currentHitbox.intersects(compareTo.getHitbox()) == true) {
			this.setSpeed(0);
			compareTo.setSpeed(0);
		}
	}

	// getters and setters
	public static float getxCoord() {
		return xCoord;
	}

	public static void setxCoord(float xTempCoord) {
		xCoord = xTempCoord;
	}

	public static float getyCoord() {
		return yCoord;
	}

	public static void setyCoord(float yTempCoord) {
		yCoord = yTempCoord;
	}

	public float getxDestCoord() {
		return xDestCoord;
	}

	public static void setxDestCoord(float tempxDestCoord) {
		xDestCoord = tempxDestCoord;
	}

	public float getyDestCoord() {
		return yDestCoord;
	}

	public static void setyDestCoord(float tempyDestCoord) {
		yDestCoord = tempyDestCoord;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getBombCount() {
		return bombCount;
	}

	public void setBombCount(int bombCount) {
		this.bombCount = bombCount;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public void setHitbox(Rectangle hitbox) {
		this.hitbox = hitbox;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public boolean isCollisionDetect() {
		return collisionDetect;
	}

	public void setCollisionDetect(boolean collisionDetect) {
		this.collisionDetect = collisionDetect;
	}

}
