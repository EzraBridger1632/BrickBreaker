import javax.swing.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.Image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Panel extends JPanel implements ActionListener, KeyListener {
	//constants
	private int height = 600;
	private int width = 800;
	//ball
	private int ball_x = 234;
	private int ball_y = 500;
	private int ball_x_velocity = -10;
	private int ball_y_velocity = -10;
	private int ball_width = 12;
	private int ball_height = 12;
	//paddle
	private int paddle_x = 380;
	private int paddle_y = 530;
	private int paddle_x_velocity = 20;
	private int paddle_width = 60;
	private int paddle_height = 6;
	//bricks
	private int brick_x_start = 150;//starting point of bricks (x)
	private int brick_x_end = 650;//ending point of bricks (x)
	private int brick_y_start = 100;//start of bricks (y)
	private int brick_y_end = 200;//end of bricks (y)
	private int brick_width = 32;
	private int brick_height = 16;
	
	//current brick position
	private int brick_x;
	private int brick_y;
	
	//gameover cond
	private boolean gameover = false;
	
	//score
	private int score;
		
	//image resources
	private BufferedImage paddle;
	private BufferedImage ball;
	private BufferedImage brick;
	private BufferedImage empty;
	private BufferedImage [][] bricks = new BufferedImage[5][16];
	
	//timers
	Timer timer = new Timer(50, this);
	
	public Panel() {
		
		setPreferredSize(new Dimension(width, height));
		setBackground(Color.black);
		addKeyListener(this);
		timer.start();
		
		//images
		try {	
			paddle = ImageIO.read(new File("Sprites/paddle.png"));
			ball = ImageIO.read(new File("Sprites/ball.png"));
			brick = ImageIO.read(new File("Sprites/brick.png"));
			empty = ImageIO.read(new File(""));
		} catch (IOException e) {
			
		}
		
		initBricks();
	}
	
	/*Initialising Bricks*/
	public void initBricks() {
		for(int i=0; i<bricks.length; i++) {
			for(int j=0; j<bricks[0].length; j++) {
				bricks[i][j] = brick;
			}
		}
	}
	
	//getting the x & y positions of the ball
	public int getBallX() {
		return ball_x;
	}
	
	public int getBallY() {
		return ball_y;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(ball, ball_x, ball_y, ball_width, ball_height, null);//render ball
		g.drawImage(paddle, paddle_x, paddle_y, paddle_width, paddle_height, null);//render paddle
		
		//brick renderer
		brick_x = brick_x_start;
		brick_y = brick_y_start;
		for(int i=0; i<bricks.length; i++) {
			for(int j=0; j<bricks[0].length; j++) {
					
				g.drawImage(bricks[i][j], brick_x, brick_y, brick_width, brick_height, null);
				brick_x += brick_width;
				
			}
			brick_x = brick_x_start;
			brick_y += brick_height;
		}
		
		brick_x = brick_x_start;
		brick_y = brick_y_start;
		
		if(gameover) {
			ball.flush();
			timer.stop();
			g.setColor(Color.RED);
			g.setFont(new Font("Times Roman", Font.PLAIN, 50));
			g.drawString("GAME OVER", 250, 300);
			g.setColor(Color.RED);
			g.setFont(new Font("Times Roman", Font.PLAIN, 40));
			g.drawString("Start again", 300, 350);
			g.drawString("SCORE = " + score, 280, 400);
		}	
		
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(gameover==false){	
			//ball animation
			ball_x += ball_x_velocity;
			ball_y += ball_y_velocity;
			
			//bounds of the ball
			if(ball_x >= (width - ball_width) || ball_x <= 0) 
				ball_x_velocity *= -1;
			if(ball_y <= 0)
				ball_y_velocity *= -1;
			if(ball_y > (height - 5)) {
				gameover = true;
				repaint();
			}
			
			//paddle-ball collision
			if((ball_y + ball_height > paddle_y) && (ball_y + ball_height < paddle_y + paddle_height)){
				if((ball_x >= paddle_x - ball_width) && (ball_x + ball_width <= paddle_x + paddle_width + ball_width))
					ball_y_velocity *= -1;
			}
		
			//ball-brick collision per frame most confusing part
			A: for(int i=0 ; i<bricks.length ; i++){
				for(int j=0 ; j<bricks[0].length ; j++){
					//if(bricks[i][j] > 0){
						brick_x = j*brick_width + brick_x_start;
						brick_y = i*brick_width + brick_y_start;

						Rectangle brickRect = new Rectangle(brick_x, brick_y, brick_width, brick_height);
						Rectangle ballRect = new Rectangle(getBallX(), getBallY(), ball_width, ball_height);

						if(ballRect.intersects(brickRect)){
							bricks[i][j] = empty;
							score++;
							
							B: if((getBallX()+ball_width > brickRect.x) || (getBallX() < brickRect.x + brick_width)){
								if((getBallY() > brickRect.y) || (getBallY() < brickRect.y)){
									break B;
								}
								ball_x_velocity *= -1;
							}
							C: if((getBallY()+ball_height > brickRect.y) || (getBallY() < brickRect.y + brick_height)){
								if((getBallX() > brickRect.x) || (getBallX() < brickRect.x)){
									break C;
								}
								ball_y_velocity *= -1;
							}
							break A;
						}
					//}
				}
			}
			
			
			for(int i=0; i<bricks.length ; i++){
				for(int j=0; j<bricks[0].length ; j++){

				}
			}
			
			repaint();
		}
		
	}
	
	//keylistener methods
	public void keyTyped(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT && paddle_x > 0) {
			paddle_x -= paddle_x_velocity;
			repaint();
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT && paddle_x < width - paddle_width) {
			paddle_x += paddle_x_velocity;
			repaint();
		}	
	}
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT && paddle_x > 0) {
			paddle_x -= paddle_x_velocity;
			repaint();
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT && paddle_x < width - paddle_width) {
			paddle_x += paddle_x_velocity;
			repaint();
		}	
	}
	
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT && paddle_x > 0) {
			paddle_x -= paddle_x_velocity;
			repaint();
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT && paddle_x < width - paddle_width) {
			paddle_x += paddle_x_velocity;
			repaint();
		}
	}
}