import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

import java.awt.*;
import java.awt.event.MouseEvent;

public class BreakoutGame extends GraphicsProgram {

    // ၃ ကြိမ်ပြီးတိုင်း အနိင် အရှုံးကို  Turns and Number of bricks နဲ့တွက်ထားပါသည်။
    // ၁ ကြိမ်တည်းနဲ့ အပြီးဆော့နိုင်ရင်လည်း Number of bricks နဲ့ condition ထိန်းထားပါသည်။

    //    Width and Height of application window
    public static final int APPLICATION_WIDTH = 400;
    public static final int APPLICATION_HEIGHT = 600;

    // Dimension of game board ( the same as application window )
    private static final int WIDTH = APPLICATION_WIDTH;
    private static final int HEIGHT = APPLICATION_HEIGHT;

    // Dimension of paddles ( width , height , offset )
    final int PDL_WIDTH = 60;       // paddle width
    final int PDL_HEIGHT = 10;      // paddle height
    final int PDL_Y_OFFSET = 30;    // distance between bottom wall and bottom of the paddle

    // Bricks
    final int BRICKS_PER_ROW = 10;  // number of bricks per row
    final int BRICK_ROWS = 10;      // number of total bricks row
    final int BRICK_GAP = 4;        // horizontal and vertical gap between bricks rows and brick columns

    // Width of a brick
    final int BRICK_WIDTH = (WIDTH - (BRICKS_PER_ROW - 1) * BRICK_GAP) / BRICKS_PER_ROW;

    // Height of a brick
    final int BRICK_HEIGHT = 8;
    final int BRICK_Y_OFFSET = 70;  // distance from y = 0 (above wall) to top of the first row of bricks

    private final int BALL_RADIUS = 10;     // size of ball will be (2 * BALL_RADIUS), you can change this value according to your taste

    public int TURNS = 3;            // number of turns for one game

    public GLabel bricksCounterLabel; // bricks counter label

    public GLabel livesCounterLabel;   // live score brick counter label

    private GRect paddle; // paddle object

    private double vx = 0;          // horizontal velocity
    private double vy = 0;          // vertical velocity

    public void run() {
        addMouseListeners();
        tenRowsOfBricks();
        bricksCounter();
        livesCounter();
        createPaddleAndMove();
        createBallMakeItMove();
    }

    //The ceiling which consists of 10 rows. Each row has its own color and consists of 10 bricks.
    private void tenRowsOfBricks() {
        // Y coordinate for first row
        double yCoordinate = BRICK_Y_OFFSET;

        // create each row
        for (int i = 0; i < BRICK_ROWS; i++) {
            double xCoordinate = (getWidth() - BRICK_WIDTH * 10 - BRICK_GAP * 9) / 2;

            // fill the row with bricks
            for (int j = 0; j < BRICKS_PER_ROW; j++) {
                // add brick method with passing parameters
                createBrick(xCoordinate, yCoordinate, i);

                // change x-axis to next brick (right side)
                xCoordinate = xCoordinate + BRICK_WIDTH + BRICK_GAP;
            }

            // change y-axis to next row
            yCoordinate = yCoordinate + BRICK_HEIGHT + BRICK_GAP;
        }
    }

    // Users can see how many bricks left to finish the game
    private void bricksCounter() {
        int numberBricksToWinTheGame = BRICKS_PER_ROW * BRICK_ROWS;
        bricksCounterLabel = new GLabel("BRICKS TO WIN THIS GAME: " + numberBricksToWinTheGame);
        double y = getHeight() - bricksCounterLabel.getHeight() + 7;
        add(bricksCounterLabel, 1, y);
    }

    // Display how many bricks lefts to win the game
    private void displayBricksLeft(int quantityOfBricksLeft) {
        bricksCounterLabel.setLabel("BRICKS TO WIN THIS GAME: " + quantityOfBricksLeft);
    }

    // Users can see how many attempts left.
    private void livesCounter() {
        if (TURNS > 1) {
            livesCounterLabel = new GLabel(TURNS + " turns left!");
        } else {
            livesCounterLabel = new GLabel(" Final turn!");
        }

        // label x-axis and y-axis | positioning the result label
        double x = getWidth() - livesCounterLabel.getWidth() - 8;
        double y = getHeight() - livesCounterLabel.getHeight() + 7;

        add(livesCounterLabel, x, y);
    }

    // Display how many turns that user left | counter decreased
    private void displayTurnsThatUserLeft(int noOfAttempts) {
        if (noOfAttempts > 1) {
            livesCounterLabel.setLabel(noOfAttempts + " turns left!");
        }
        if (noOfAttempts == 1) {
            livesCounterLabel.setLabel("Final turn!");
        }
    }

    // create a brick using coordinates and set it color according to the task
    private void createBrick(double xCoordinate, double yCoordinate, int i) {
        // create a brick
        GRect rect = new GRect(xCoordinate, yCoordinate, BRICK_WIDTH, BRICK_HEIGHT);
        rect.setFilled(true);

        // set different colors for each row of bricks
        if (i == 0 || i == 1) {
            rect.setColor(Color.red);
        }
        if (i == 2 || i == 3) {
            rect.setColor(Color.orange);
        }
        if (i == 4 || i == 5) {
            rect.setColor(Color.yellow);
        }
        if (i == 6 || i == 7) {
            rect.setColor(Color.green);
        }
        if (i == 8 || i == 9) {
            rect.setColor(Color.cyan);
        }
        add(rect);
    }

    // create a paddle to play the game
    private void createPaddleAndMove() {
        double x = getWidth() / 2 - PDL_WIDTH / 2;
        double y = getHeight() - PDL_Y_OFFSET - PDL_HEIGHT;
        paddle = new GRect(x, y, PDL_WIDTH, PDL_HEIGHT);
        paddle.setFilled(true);
        add(paddle);
    }

    // The paddle can follow users` mouse.
    public void mouseMoved(MouseEvent paddleMove) {

        // X-axis mouse movement
        double mouseX = paddleMove.getX();

        // center (x-axis) of the paddle
        double centerOfPaddle = PDL_WIDTH / 2;

        // min x-axis coordinate for paddle
        double xLimits = getWidth() - centerOfPaddle;

        // current x coordinate of paddle
        double paddleX = paddle.getX() + centerOfPaddle;

        // fixed value that represents the y-coordinate of the bottom of the paddle.
        double paddleY = getHeight() - PDL_Y_OFFSET;


        // set limitations for paddle to prevent it from going outside the app window
        if (mouseX >= centerOfPaddle && mouseX <= xLimits) {
            paddle.move(mouseX - paddleX, paddleY - paddle.getY());
        }
    }


    // Create the ball and make it move
    private void createBallMakeItMove() {
        // create a black ball
        GOval ball = createBall();
        // click on mouse to start the game
        waitForClick();
        // make it move
        moveBall(ball);
    }

    // The ball
    private GOval createBall() {
        GOval ball;
        int SIZE = BALL_RADIUS * 2;
        int x = (getWidth() - SIZE) / 2;
        int y = (getHeight() - SIZE) / 2;
        ball = new GOval(x, y, SIZE, SIZE);
        ball.setFilled(true);
        add(ball);
        return ball;
    }

    // Initial random velocity
    void initVelocity() {
        RandomGenerator randomGen = RandomGenerator.getInstance();
        vy = 3;
        vx = randomGen.nextDouble(1.0, 3.0);
        if (randomGen.nextBoolean(0.5)) vx = -vx;
    }

    // Make the ball moves, it bounces from each wall
    private void moveBall(GOval ball) {

        // set random speed-ball moves on x-axis
        initVelocity();

        // delay
        final int delay = 8;

        // the number of lives a user has to finish the game
        int numberAttemptsUserHas = TURNS;

        // origin quantity of bricks user has to break to win the game
        int noOfBricksToWinTheGame = BRICKS_PER_ROW * BRICK_ROWS;

        while (noOfBricksToWinTheGame != 0 && numberAttemptsUserHas != 0) {
            // if the ball touch something
            GObject collider = getCollidingObj(ball);
            if (collider == paddle) {
                vy = vy * (-1);
            }

            // collider != null and collider != paddle
            if (collider != null && collider != paddle && collider != bricksCounterLabel && collider != livesCounterLabel) {
                vy = vy * (-1);
                remove(collider);
                noOfBricksToWinTheGame--;
                displayBricksLeft(noOfBricksToWinTheGame);
            }

            // bounce left border
            if (ball.getX() < 0) {
                vx = vx * (-1);
            }

            // bounce right border
            if (ball.getX() > getWidth() - BALL_RADIUS * 2) {
                vx = vx * (-1);
            }

            // bounce top border
            if (ball.getY() < 0) {
                vy = vy * (-1);
            }

            // if user missed to touch the ball | bottom border
            if (ball.getY() > getHeight() - BALL_RADIUS * 2) {
                numberAttemptsUserHas--;
                TURNS--;

                displayTurnsThatUserLeft(numberAttemptsUserHas);

                // set starting position of the ball
                ball.setLocation(getWidth() / 2 - BALL_RADIUS, getHeight() / 2 - BALL_RADIUS);
                if (TURNS == 2) {
                    nextTurn(TURNS);
                }
                if (TURNS == 1) {
                    nextTurn(TURNS);
                }
                if (TURNS == 0) {
                    this.TURNS = 3;
                }
                continue;
            }
            ball.move(vx, vy);
            pause(delay);
        }
        finalResult(numberAttemptsUserHas, noOfBricksToWinTheGame);
    }

    // Function returns object if the ball met it
    private GObject getCollidingObj(GOval ball) {

        double x = ball.getX();
        double y = ball.getY();
        double blx = x;
        double bly = y + 2 * BALL_RADIUS;

        double brx = x + 2 * BALL_RADIUS;
        double bry = y + 2 * BALL_RADIUS;

        GObject obj = getElementAt(blx, bly);
        if (obj != null) return obj;

        obj = getElementAt(brx, bry);
        if (obj != null) return obj;

        return null;
    }

    // Display message until reach final turn
    private void nextTurn(int attempt) {
        removeAll();
        GLabel label = new GLabel("");
        label.setFont("JetBrains Mono-16");

        // user won
        if (attempt == 2) {
            label.setColor(Color.RED);
            label.setLabel("You Lose!! Click to play next turn");
        }

        if (attempt == 1) {
            label.setColor(Color.RED);
            label.setLabel("You Lose!! Click to play final turn");
        }

        double x = (getWidth() - label.getWidth()) / 2;
        double y = (getHeight() - label.getHeight()) / 2;

        add(label, x, y);

        waitForClick();

        playNextTurn(label);
    }

    // Play Next Turn
    private void playNextTurn(GLabel label) {
        remove(label);
        run();
    }

    // Final results
    private void finalResult(int numberAttemptsUserHas, int numberBricksToWinTheGame) {
        removeAll();
        GLabel label = new GLabel("");
        label.setFont("JetBrains Mono-16");

        // user won
        if (numberBricksToWinTheGame == 0 && numberAttemptsUserHas != 0) {
            label.setColor(Color.RED);
            label.setLabel("YOU WON!! Click to play again");
            this.TURNS = 3;
        }

        // user lost
        if (numberAttemptsUserHas == 0) {
            label.setColor(Color.RED);
            label.setLabel("Game Over! Click to restart");
        }

        // get x-axis, y-axis to center final message
        double x = (getWidth() - label.getWidth()) / 2;
        double y = (getHeight() - label.getHeight()) / 2;

        add(label, x, y);

        waitForClick();

        restartGame(label);
    }

    private void restartGame(GLabel label) {
        remove(label);
        run();
    }
}
