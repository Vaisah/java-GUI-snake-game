package com.fun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final int UNIT_SIZE = 25;
    private final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private final int DELAY = 100;
    private final ArrayList<Integer> snakeX = new ArrayList<>();
    private final ArrayList<Integer> snakeY = new ArrayList<>();
    private int snakeLength = 1;
    private int appleX;
    private int appleY;
    private int score = 0;
    private final Timer timer;
    private boolean isMovingRight = true;
    private boolean isMovingLeft = false;
    private boolean isMovingUp = false;
    private boolean isMovingDown = false;
    private boolean isGameRunning = true;
    JButton playAgain;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BorderLayout());
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        startGame();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void startGame() {
        spawnApple();
        snakeX.add(WIDTH / 2);
        snakeY.add(HEIGHT / 2);
       
    }

    public void spawnApple() {
        Random random = new Random();
        appleX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
        int newX = snakeX.get(0);
        int newY = snakeY.get(0);

        if (isMovingRight) {
            newX += UNIT_SIZE;
        } else if (isMovingLeft) {
            newX -= UNIT_SIZE;
        } else if (isMovingUp) {
            newY -= UNIT_SIZE;
        } else if (isMovingDown) {
            newY += UNIT_SIZE;
        }

        if (snakeX.size() > snakeLength) {
            snakeX.remove(snakeX.size() - 1);
            snakeY.remove(snakeY.size() - 1);
        }

        snakeX.add(0, newX);
        snakeY.add(0, newY);

        checkAppleCollision();
        checkCollisions();
    }

    public void checkAppleCollision() {
        if (snakeX.get(0) == appleX && snakeY.get(0) == appleY) {
            snakeLength++;
            score++;
            spawnApple();
        }
    }

    public void checkCollisions() {
        for (int i = 1; i < snakeX.size(); i++) {
            if (snakeX.get(i) == snakeX.get(0) && snakeY.get(i) == snakeY.get(0)) {
                isGameRunning = false;
                timer.stop();
              //  break;
            }
        }

        if (snakeX.get(0) < 0 || snakeX.get(0) >= WIDTH || snakeY.get(0) < 0 || snakeY.get(0) >= HEIGHT) {
            isGameRunning = false;
            timer.stop();
        }

        if (!isGameRunning) {
            // add play again button when game over
        	playAgain = new JButton("PLAY AGAIN");
        	add(playAgain, BorderLayout.AFTER_LAST_LINE);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (isGameRunning) {
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < snakeX.size(); i++) {
                g.setColor(Color.green);
                g.fillRect(snakeX.get(i), snakeY.get(i), UNIT_SIZE, UNIT_SIZE);
            }
        } else {
            // Draw game over screen
        	String message = "Game Over - Score: " + score;
            Font font = new Font("SansSerif", Font.BOLD, 30);
            g.setColor(Color.white);
            g.setFont(font);
            g.drawString(message, (WIDTH - g.getFontMetrics().stringWidth(message)) / 2, HEIGHT / 2);
            
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isGameRunning) {
            move();
            repaint();
        }
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT && !isMovingRight) {
                isMovingLeft = true;
                isMovingUp = false;
                isMovingDown = false;
            }

            if (key == KeyEvent.VK_RIGHT && !isMovingLeft) {
                isMovingRight = true;
                isMovingUp = false;
                isMovingDown = false;
            }

            if (key == KeyEvent.VK_UP && !isMovingDown) {
                isMovingUp = true;
                isMovingRight = false;
                isMovingLeft = false;
            }

            if (key == KeyEvent.VK_DOWN && !isMovingUp) {
                isMovingDown = true;
                isMovingRight = false;
                isMovingLeft = false;
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame snakeGame = new SnakeGame();
        frame.add(snakeGame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
