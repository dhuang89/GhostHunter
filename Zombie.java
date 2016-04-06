import java.awt.Rectangle;
import java.util.Random;

/**
 * Zombie.java
 * 
 * The class representing the Zombie object in the game. This class needs to
 * have some representation of current location, a reference to its target (aka
 * the Human), a speed, and a Rectangle representing its hitbox. It might also
 * help to have variables representing the size of both the Zombie and its
 * hitbox. You should create methods for the following: 1. checking to see if
 * this Zombie's hitbox Rectangle is colliding with any other hitbox Rectangle
 * (either the Human or an obstacle Rectangle) 2. movement (normal movement and
 * what to do if there is a collision) 3. constructors 4. getters/setters 5.
 * anything else you may need.
 * 
 * @authors
 * @compids
 * @lab
 */
public class Zombie {

	private float xCoord;
	private float yCoord;
	private float xTargetCoord;
	private float yTargetCoord;
	private float speed = 20;
	private Rectangle hitbox;
	private Human target;
	private int direction; // up - 1; down - 2; left - 3; right - 4
	private boolean collisionDetect;

	// constructor
	public Zombie(float xCoord, float yCoord) {
		super();
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.xTargetCoord = Human.getxCoord();
		this.yTargetCoord = Human.getyCoord();
		this.hitbox = new Rectangle((int) this.xCoord+18, (int) this.yCoord+18, 35, 65);
	}

	// hitbox movement

	public Rectangle hitboxMovements() {

		hitbox.setLocation(((int) this.xCoord)+18, ((int) this.yCoord)+18);

		return hitbox;
	}

	// zombie movement

	public void move(float elapsedTime) {
		float x1 = this.xCoord;
		float y1 = this.yCoord;
		float x2 = Human.getxCoord();
		float y2 = Human.getyCoord();
		float dx = x1 - x2;
		float dy = y1 - y2;
		double absAngle = (Math.atan(Math.abs(dy) / Math.abs(dx)))
				* (180 / Math.PI);

		if (dx == 0 && dy == 0){
			direction = 2;
		}
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

	// collision checker

	public void collisionChecker(Zombie compareTo) {
		Rectangle currentHitbox = this.getHitbox();

		if (currentHitbox.intersects(compareTo.hitbox) == true) {
			collisionDetect = true;
		}
	}

	// obstacle collision effect

	public void obstacleCollisionEffect(Rectangle compareTo) {

		Rectangle currentHitbox = this.getHitbox();

		if (currentHitbox.intersects(compareTo) == true) {

			Rectangle temp = currentHitbox.intersection(compareTo);
			double tempWidth = temp.getWidth();
			double tempHeight = temp.getHeight();
			
			// corner collision
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
			
			// side collision
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

	
	// zombie/zombie collision effect
	
	public void zombieCollisionEffect(Zombie compareTo){
		
		Rectangle currentHitbox = this.getHitbox();
		Rectangle compareToBox = compareTo.getHitbox();
		
		if (currentHitbox.intersects(compareToBox) == true) {

			Rectangle temp = currentHitbox.intersection(compareToBox);
			double tempWidth = temp.getWidth();
			double tempHeight = temp.getHeight();
			
			// corner collision
			if (currentHitbox.getWidth() != tempWidth && currentHitbox.getHeight() != tempHeight){
				if(tempWidth < tempHeight){
					if (currentHitbox.getX() > compareToBox.getX()) {
						this.xCoord += temp.getWidth();
					} else {
						this.xCoord -= temp.getWidth();
					}
				}
				else if (tempWidth > tempHeight){
					if (currentHitbox.getY() > compareToBox.getY()) {
						this.yCoord += temp.getHeight();
					} else {
						this.yCoord -= temp.getHeight();
					}
				}
			}
			
			// side collision
			if (currentHitbox.getWidth() == temp.getWidth()) {
				if (currentHitbox.getY() > compareToBox.getY()) {
					this.yCoord += temp.getHeight();
				} else {
					this.yCoord -= temp.getHeight();
				}
			}
			if (currentHitbox.getHeight() == temp.getHeight()) {
				if (currentHitbox.getX() > compareToBox.getX()) {
					this.xCoord += temp.getWidth();
				} else {
					this.xCoord -= temp.getWidth();
				}

			}

		}
	}
	
	
	
	// getters and setters
	public float getxCoord() {
		return xCoord;
	}

	public void setxCoord(float xCoord) {
		this.xCoord = xCoord;
	}

	public float getyCoord() {
		return yCoord;
	}

	public void setyCoord(float yCoord) {
		this.yCoord = yCoord;
	}

	public float getxTargetCoord() {
		return xTargetCoord;
	}

	public void setxTargetCoord(float xTargetCoord) {
		this.xTargetCoord = xTargetCoord;
	}

	public float getyTargetCoord() {
		return yTargetCoord;
	}

	public void setyTargetCoord(float yTargetCoord) {
		this.yTargetCoord = yTargetCoord;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
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
