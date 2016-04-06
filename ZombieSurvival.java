
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * ZombieSurvival.java
 * 
 * The ZombieSurvival is the field of play for the game. It passes messages
 * between the Human and the Zombies. It also picks up the commands from the
 * mouse and does the appropriate action. This is the class that will have the
 * main method to start the game.
 * 
 * @authors
 * @compid
 * @lab
 */
public class ZombieSurvival {

	// The SurvivalField needs a canvas to draw on
	private SimpleCanvas canvas;

	// The InfoFrame that you use for output instead of System.out
	private InfoFrame output;

	// Default board size
	private final int BOARDHEIGHT = 700;
	private final int BOARDWIDTH = 700;

	// --------------------------------------------------------
	// Fields
	// You should setup fields to keep up with:
	// - a whole bunch of Zombies
	// - a single Human
	// - a whole bunch of obstacles, represented as Rectangles
	// - some way to know if the game is over
	// - a way to keep track of the score
	// - how many zombies you should start with
	// --------------------------------------------------------

	ArrayList<Zombie> zombieList = new ArrayList<Zombie>();
	ArrayList<Rectangle> obstacleList = new ArrayList<Rectangle>();

	private Human littleGirl;
	private Zombie zombieSpawn;
	private Rectangle obstacle;
	private int obstacle1;
	private int obstacle2;
	private int cycleCount = 0;
	private float bombRadius = 100;
	private int score;
	private boolean bomb = false;
	private boolean superBomb = false;
	private int bombTimer;
	private int superBombTimer;
	private boolean alive = true;
	private boolean gameEnd = false;
	private int zombieCount;

	// -----------------------------------------
	// Methods

	// new methods

	/**
	 * The Constructor - This method should instantiate a new canvas, create a
	 * new player character, and create the first four zombies in random
	 * locations around the board.
	 */
	public ZombieSurvival() throws Exception {
		// Create canvas object with 500x500 spatial dimensions.
		canvas = new SimpleCanvas(BOARDWIDTH, BOARDHEIGHT, this);

		// Initialize your output frame
		output = new InfoFrame(this);

		// TODO: Here is where you should create your initial zombies and your
		// Human
		// 20 is a good speed for the human - 10 for the zombie, but experiment!
		// You should also load your course file here to get the obstacles
		// on screen.
		littleGirl = new Human();

		Random generator = new Random();

		// little girl safezone

		zombieList.add(new Zombie(generator.nextInt(650), generator
				.nextInt(650)));
		zombieList.add(new Zombie(generator.nextInt(650), generator
				.nextInt(650)));
		zombieList.add(new Zombie(generator.nextInt(650), generator
				.nextInt(650)));
		zombieList.add(new Zombie(generator.nextInt(650), generator
				.nextInt(650)));

		zombieCount = zombieList.size();

		File course = new File("course.csv");
		Scanner fileReader = new Scanner(course);

		while (fileReader.hasNextLine()) {
			String line = fileReader.nextLine();
			String[] lineSplit = line.split(",");

			Rectangle temp = new Rectangle(Integer.parseInt(lineSplit[0]),
					Integer.parseInt(lineSplit[1]),
					Integer.parseInt(lineSplit[2]),
					Integer.parseInt(lineSplit[3]));
			obstacleList.add(temp);
		}

		obstacle1 = generator.nextInt(obstacleList.size());
		obstacle2 = generator.nextInt(obstacleList.size());
		while (obstacle1 == obstacle2) {
			obstacle2 = generator.nextInt(obstacleList.size());
		}
	}

	/**
	 * This method takes a file name that contains information about obstacles
	 * in the game. It should be formatted: x,y,width,height
	 * 
	 * @param filename
	 *            Name of the file
	 * @throws Exception
	 */
	public void loadObstacles(String filename) throws Exception {
		// TODO: fill in this method to read the csv file and
		// populate a list of obstacle Rectangles
	}

	/**
	 * This method should control all of your mouse actions. The mouse activity
	 * is picked up by the SimpleCanvas and then it should call this method,
	 * passing either the button that was pressed or some other flag.
	 */
	public void mouseAction(float x, float y, int button) {
		// TODO: Change this method to help the player move!
		if (button == -1) {
			// output.println("Mouse: " + x + "," + y);
			Human.setxDestCoord((int) x);
			Human.setyDestCoord((int) y);
		}

		if (button == 1 && alive == true) {
			// output.println("You clicked the left mouse button!");

			if (littleGirl.getBombCount() > 0) {

				bomb = true;

				littleGirl.setBombCount(littleGirl.getBombCount() - 1);
				output.println("You dropped a bomb!");
				output.println("Bomb count: " + littleGirl.getBombCount());

				bombTimer = cycleCount;
			} else {
				output.println("You don't have any bombs!");
			}

		}

		if (button == 3 && alive == true) {
			// output.println("You clicked the right mouse button!");

			if (littleGirl.getBombCount() >= 3) {

				superBomb = true;

				littleGirl.setBombCount(littleGirl.getBombCount() - 3);
				output.println("YOU DROPPED THE SUPERBOMB!!!!!!");
				output.println("Bomb count: " + littleGirl.getBombCount());

				superBombTimer = cycleCount;
			} else {
				output.println("You don't have enough bombs! (You need 3, noob.)");
				output.println("Bomb count: " + littleGirl.getBombCount());
			}
		}

	}

	/**
	 * This method controls the bomb action. It should check to see if the
	 * player has any bombs. If so, that count should be decremented by one.
	 * Then every zombie within a 50 pixel radius of the player should be
	 * eliminated.
	 */
	public void detonateBomb() {
		// TODO: fill in this method to kill zombies near the human!

		for (int i = 0; i < zombieList.size(); i++) {
			double distanceBetween = Math
					.sqrt((zombieList.get(i).getxCoord() - littleGirl
							.getxCoord())
							* (zombieList.get(i).getxCoord() - littleGirl
									.getxCoord())
							+ (zombieList.get(i).getyCoord() - littleGirl
									.getyCoord())
							* (zombieList.get(i).getyCoord() - littleGirl
									.getyCoord()));
			if (distanceBetween <= bombRadius) {
				zombieList.remove(i);
			}
		}
	}

	public void detonateSuperBomb() {
		// TODO: fill in this method to kill zombies near the human!

		for (int i = 0; i < zombieList.size(); i++) {
			zombieList.remove(i);
		}
	}

	/**
	 * This is the main drawing function that is automatically called whenever
	 * the canvas is ready to be redrawn. The 'elapsedTime' argument is the
	 * time, in seconds, since the last time this function was called.
	 * 
	 * Other things this method should do: 1. draw the zombies, obstacles, and
	 * the human on the screen 2. tell the zombies and human to move 3. check to
	 * see if the game is over after they move 4. halt the game if the game is
	 * over 5. update the score for every cycle that the user is alive 6. add a
	 * new zombie every 5000 or so cycles 7. add a new bomb every 50000 or so
	 * cycles
	 * 
	 * 
	 */
	public void draw(Graphics2D g, float elapsedTime) {
		// TODO: Nearly ALL your game logic will go here! This is called on
		// every refresh of the screen and is the "main game loop".

		cycleCount++;

		// This is how you draw the Human, replacing the null with the human
		// object

		littleGirl.move(elapsedTime);

		// zombie collision

		if (cycleCount == 2000){
			output.println("Your invisibility cloak is down, run for your life!");
		}
		
		if (cycleCount > 2000) {  // little girl invincible for first 2000 cycles
			for (int i = 0; i < zombieList.size(); i++) {
				littleGirl.zombieCollisionEffect(zombieList.get(i));

				if (littleGirl.getSpeed() == 0) {
					alive = false;
					for (int z = 0; z < zombieList.size(); z++) {
						zombieList.get(z).setSpeed(0);
					}
				}

				if (alive == false && gameEnd == false) {
					output.println("You got caught by a zombie! You lost!");
					output.println("Final score: " + score);
					gameEnd = true;
				}

			}
		}
		// obstacle collision
		littleGirl.obstacleCollisionEffect(obstacleList.get(obstacle1));
		littleGirl.obstacleCollisionEffect(obstacleList.get(obstacle2));
		littleGirl.hitboxMovements();

		int temp = cycleCount % 800;

		if (temp >= 0 && temp <= 99) {
			canvas.drawHuman0(g, littleGirl);
		} else if (temp >= 100 && temp <= 199) {
			canvas.drawHuman1(g, littleGirl);
		} else if (temp >= 200 && temp <= 299) {
			canvas.drawHuman2(g, littleGirl);
		} else if (temp >= 300 && temp <= 399) {
			canvas.drawHuman3(g, littleGirl);
		} else if (temp >= 400 && temp <= 499) {
			canvas.drawHuman4(g, littleGirl);
		} else if (temp >= 500 && temp <= 599) {
			canvas.drawHuman5(g, littleGirl);
		} else if (temp >= 600 && temp <= 699) {
			canvas.drawHuman6(g, littleGirl);
		} else if (temp >= 700 && temp <= 799) {
			canvas.drawHuman7(g, littleGirl);
		}

		// This is how you draw the Zombies, replacing the null with a zombie
		// object

		for (int i = 0; i < zombieList.size(); i++) {
			zombieList.get(i).move(elapsedTime);

			// obstacle collision
			zombieList.get(i).obstacleCollisionEffect(
					obstacleList.get(obstacle1));
			zombieList.get(i).obstacleCollisionEffect(
					obstacleList.get(obstacle2));
			zombieList.get(i).hitboxMovements();

			for (int k = 0; k < zombieList.size(); k++) {
				if (k != i) {
					zombieList.get(i).zombieCollisionEffect(zombieList.get(k));
					zombieList.get(i).hitboxMovements();
				}
			}

			int temp2 = cycleCount % 999;

			if (temp2 >= 0 && temp2 <= 332) {
				canvas.drawZombie0(g, zombieList.get(i));
			} else if (temp2 >= 333 && temp2 <= 665) {
				canvas.drawZombie1(g, zombieList.get(i));
			} else if (temp2 >= 666 && temp2 <= 998) {
				canvas.drawZombie2(g, zombieList.get(i));
			}
		}

		// This is how your draw an obstacle, replacing the new Rectangle with
		// one from your list of obstacles

		canvas.drawObstacle(g, obstacleList.get(obstacle1));
		canvas.drawObstacle(g, obstacleList.get(obstacle2));

		// hitbox testing
//		canvas.drawObstacle(g, littleGirl.getHitbox());
//
//		for (int r = 0; r < zombieList.size(); r++) {
//			canvas.drawObstacle(g, zombieList.get(r).getHitbox());
//		}

		// bomb effect

		if (bomb == true) {
			while (cycleCount - bombTimer < 300) {
				detonateBomb();
				canvas.drawBoom(g, littleGirl);
				break;
			}
		}

		if (cycleCount - bombTimer >= 300) {
			bomb = false;
		}

		// super bomb effect

		if (superBomb == true) {
			while (cycleCount - superBombTimer < 1000) {
				detonateSuperBomb();
				canvas.drawSuperBomb(g, littleGirl);
				break;
			}
		}

		if (cycleCount - superBombTimer >= 1000) {
			superBomb = false;
		}


		// adding new zombies, bombs, etc.

		if (cycleCount % 100 == 0) {
			score++;
		}

		if (cycleCount % 5000 == 0) {
			Random generator = new Random();

			if (alive == true) {

				// little girl safezone for subsequent zombies

				Zombie tempZ = new Zombie(generator.nextInt(650),
						generator.nextInt(650));
				do {
					tempZ.setxCoord(generator.nextInt(650));
				} while (Math.abs(tempZ.getxCoord() - littleGirl.getxCoord()) < 70);

				do {
					tempZ.setyCoord(generator.nextInt(650));
				} while (Math.abs(tempZ.getyCoord() - littleGirl.getyCoord()) < 120);

				zombieList.add(tempZ);
				output.println("A new zombie just appeared!");
				zombieCount = zombieList.size();
			}

		}

		if (alive == true) {
			if (cycleCount % 50000 == 0) {
				littleGirl.setBombCount(littleGirl.getBombCount() + 1);
				output.println("You got a new bomb! You now have "
						+ littleGirl.getBombCount() + " bombs");
			}
		}
		
		// opening text: welcome, explanations
		
		if(cycleCount == 1){
			output.println("------ Zombie Survival Game ------");
			output.println("Guide:");
			output.println("You can drop a bomb and kill nearby zombies by clicking the left mouse button.");
			output.println("You can drop a SUPERBOMB by clicking the right mouse botton. Beware, this costs 3 bombs.");
			output.println("You are invisible to zombies for a short period of time at the beginning of the game.");
			output.println("Get high score by surviving as long as you can. Have fun and good luck!");
			output.println("-------------------------------------------------------------------------------------------");
		}
	}
		

	/**
	 * Your standard main method
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ZombieSurvival simulator = new ZombieSurvival();
		simulator.play();
	}

	/**
	 * This method starts the game.
	 */
	public void play() {
		canvas.setupAndDisplay();
	}
	
}
