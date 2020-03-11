package gamePackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import asset.ImageLoader;

public class GameGraphics implements Runnable, KeyListener {

	private Display display;

	private int width;
	private int height;
	private String title;

	private String[][] board;

	private Thread thread;
	private BufferStrategy buffer;
	private Graphics g;

	private int BOX_width = 80;
	private int BOX_height = 80;

	private int paddingX = 80;
	private int paddingY = 80;

	private int boardRow;
	private int boardCol;

	private boolean isGameEnd = false;
	private boolean isGameStarted = false;

	private int sourceI;
	private int sourceJ;

	private int goldI;
	private int goldJ;

	private Position parent;
	private Position nextPosition;

	private Game game;

	private boolean proceed = true;

	private BufferedImage wumpus;
	private BufferedImage pit;
	private BufferedImage stench;
	private BufferedImage breeze;
	private BufferedImage wumPit;
	private BufferedImage gold;
	private BufferedImage agent;
	private BufferedImage agentWidArrow;
	
	private String statement = "";

	public static boolean noWay = false;

	private ArrayList<Position> path;

	private Image wumIcon;

	public GameGraphics() {
		this.width = GameSettings.width;
		this.height = GameSettings.height;
		this.title = GameSettings.title;

		sourceI = GameSettings.sourceI;
		sourceJ = GameSettings.sourceJ;

		this.goldI = GameSettings.goldI;
		this.goldJ = GameSettings.goldJ;

		boardRow = GameSettings.BOARD_ROW;
		boardCol = GameSettings.BOARD_COL;

		display = new Display(width, height, title);
		parent = new Position(sourceI, sourceJ);
		nextPosition = parent;

		path = new ArrayList<>();
		path.add(parent);

		game = new Game();

	}

	private void init() {

		wumpus = ImageLoader.loadImage("/Images/wumpus.png.jpg", BOX_width, BOX_height);
		pit = ImageLoader.loadImage("/Images/Pit.jpg", BOX_width, BOX_height);
		stench = ImageLoader.loadImage("/Images/stench.jpg", BOX_width, BOX_height);
		breeze = ImageLoader.loadImage("/Images/breeze.png", BOX_width, BOX_height);
		gold = ImageLoader.loadImage("/Images/gold.png", BOX_width, BOX_height);
		wumPit = ImageLoader.loadImage("/Images/wumpit.png", BOX_width, BOX_height);
		agent = ImageLoader.loadImage("/Images/agent.jpg", BOX_width, BOX_height);
		agentWidArrow = ImageLoader.loadImage("/Images/agentWdArraw.jpg.png", BOX_width, BOX_height);

		display.canvas.addKeyListener(this);

	}

	private void drawBackground(Graphics g) {
		g.setColor(new Color(53, 53, 53));
		g.fillRect(0, 0, width, height);

	}

	private void drawBoard(Graphics g) {

		drawBackground(g);
		Color color = Color.white;
		int lineWeight = 2;
		g.setColor(color);
		for (int i = paddingX; i <= (boardRow * BOX_width) + paddingX; i += BOX_width) {
			for (int k = i - lineWeight; k < i + lineWeight; k++)
				g.drawLine(k, paddingY, k, BOX_height * boardRow + BOX_height);

		}

		for (int i = paddingY; i <= (boardCol * BOX_height) + paddingY; i += BOX_height) {
			for (int k = i - lineWeight; k < i + lineWeight; k++)
				g.drawLine(paddingX, k, BOX_width * boardCol + BOX_width, k);
		}

	}

	private void drawElement(Graphics g) {
		for (int i = 0; i < boardRow; i++) {
			for (int j = 0; j < boardCol; j++) {
				if (!board[i][j].equals("E")) {
					String stringToDraw = "";
					boolean isBGS = true;

					if (i == sourceI && j == sourceJ)
						stringToDraw = "Start";

					g.setColor(Color.black);
					int x = (j + 1) * BOX_width;
					int y = (i + 1) * BOX_height;

					if (board[i][j].equals("W")) {
						g.drawImage(wumpus, x, y, null);
					} else if (board[i][j].equals("P")) {
						g.drawImage(pit, x, y, null);
					} else if (board[i][j].equals("S")) {
						g.drawImage(stench, x, y, null);
					} else if (board[i][j].equals("B")) {
						g.drawImage(breeze, x, y, null);
					} else if (board[i][j].equals("BS")) {
						g.drawImage(wumPit, x, y, null);
					} else if (board[i][j].equals("G")) {
						g.drawImage(gold, x, y, null);
					}

				}
			}
		}
	}

	private void drawPlayer(Graphics g, int i, int j) {

		if (i == -1 && j == -1)
			return;

		int x = (j + 1) * BOX_width;
		int y = (i + 1) * BOX_height;

		if (GameSettings.arrowNo == 1) {
			g.drawImage(agentWidArrow, x, y, null);
		} else {
			g.drawImage(agent, x, y, null);
		}
	}
	private void drawStatement(Graphics g, String statement){
		g.setColor(Color.yellow);
		g.setFont(new Font("Century Gothic", Font.CENTER_BASELINE, 30));

		g.drawString(statement, width - 540, 360);
	}

	private void play(Graphics g) {

		parent = nextPosition;

		if (nextPosition.row == goldI && nextPosition.col == goldJ) {
			isGameEnd = true;
			statement = "Yee, Found the gold!!";
			
		}

		if (!isGameEnd) {
			nextPosition = game.getNextPosition(parent);
			path.add(nextPosition);
		}

		if (nextPosition.row == -1 && nextPosition.col == -1) {
			isGameEnd = true;
			return;
		}

		if (parent.row == nextPosition.row && parent.col == nextPosition.col) {
			isGameEnd = true;
			return;
		}

		proceed = false;

	}

	private void render() {
		buffer = display.canvas.getBufferStrategy();
		if (buffer == null) {
			display.canvas.createBufferStrategy(3);
			return;
		}

		g = buffer.getDrawGraphics();

		g.clearRect(0, 0, width, height);

		drawBoard(g);
		drawElement(g);
		drawPlayer(g, nextPosition.row, nextPosition.col);
		drawStatement(g, statement);
		
		if (!isGameEnd) {
			play(g);
		}

		buffer.show();
		g.dispose();

	}

	@Override
	public void run() {
		init();

		board = game.initialiseBoard();

		while (true) {
			render();

			Thread.currentThread();
			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		switch (e.getKeyCode()) {

		case KeyEvent.VK_SPACE:
			proceed = true;
			break;

		}

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
