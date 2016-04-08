package com.quangnn.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.quangnn.brain.Brain;
import com.quangnn.brain.DefaultBrain;
import com.quangnn.shape.Board;
import com.quangnn.shape.Piece;

public class PlayGamePanel extends JPanel {
	/** */
	private static final long serialVersionUID = 1L;

	private MenuGamePanel menu;

	public static final int WIDTH = 10;
	public static final int HEIGHT = 15;

	public static final int ROTATE = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int DROP = 3;
	public static final int DOWN = 4;

	public static final int PIXELS = 20;

	public static final int TOP_SPACE = 1;

	protected boolean testMode = false;
	public final int TEST_LIMIT = 100;

	protected boolean DRAW_OPTIMIZE = false;

	protected Board board;
	protected Piece[] pieces;

	protected Piece currentPiece;
	protected int currentX;
	protected int currentY;
	protected boolean moved;

	protected Piece newPiece;
	protected int newX;
	protected int newY;

	protected boolean gameOn;
	protected int count;
	protected long startTime;
	protected Random random;

	protected JLabel countLabel;
	protected JLabel lbScore;
	protected int score;
	protected javax.swing.Timer timer;
	protected JCheckBox cbBrainMode;
	Font font = new Font("Tahoma", Font.BOLD, 15);

	boolean stop = false;
	boolean isSeLec = false;

	public final int DELAY = 600;
	private OnPlayGameAgainListener listener;

	public void setOnPlayGameAgainListener(OnPlayGameAgainListener listener) {
		this.listener = listener;
	}

	public PlayGamePanel() {
		initPanel();
		initComps();
		addComps();
		check = 0;
		brain = new DefaultBrain();
	}

	@Override
	public void setVisible(boolean aFlag) {
		setRequestFocusEnabled(true);
		requestFocusInWindow();
	}

	private void initPanel() {
		setLayout(null);
		setBounds(0, TOP_SPACE * PIXELS, (WIDTH * PIXELS) + 2,
				(HEIGHT + TOP_SPACE) * PIXELS + 2);
		setBackground(Color.GRAY.darker());
		gameOn = false;

		pieces = Piece.getPieces();
		board = new Board(WIDTH, HEIGHT + TOP_SPACE);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					tickBrain(LEFT);
					break;
				case KeyEvent.VK_RIGHT:
					tickBrain(RIGHT);
					break;
				case KeyEvent.VK_UP:
					tickBrain(ROTATE);
					break;
				case KeyEvent.VK_DOWN:
					tickBrain(DROP);
					break;

				}
			}
		});
		timer = new javax.swing.Timer(DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tickBrain(DOWN);
			}
		});

	}

	private void initComps() {

		lbScore = new JLabel("Score: 0");
		lbScore.setBounds(0, 0, 100, 30);
		lbScore.setForeground(Color.RED);
		lbScore.setFont(font);

		cbBrainMode = new JCheckBox("Brain");
		cbBrainMode.setBounds(200, 0, 100, 30);
		cbBrainMode.setFont(font);
		cbBrainMode.setForeground(Color.RED);
		cbBrainMode.setBackground(Color.BLUE.darker());

	}

	private void addComps() {
		this.add(lbScore);
		this.add(cbBrainMode);
	}

	public void startGame() {
		board = new Board(WIDTH, HEIGHT + TOP_SPACE);

		repaint();

		count = 0;
		score = 0;
		updateCounters();
		gameOn = true;

		if (testMode) {
			random = new Random(0);
		} else {
			random = new Random();
		}
		addNewPiece();
		timer.start();
		startTime = System.currentTimeMillis();
	}

	public void stopGame() {
		gameOn = false;
		timer.stop();
		int result = JOptionPane.showConfirmDialog(this, "Start Game Again.",
				"Game Over", JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			listener.restartGame();
		} else {
			if (result == JOptionPane.NO_OPTION) {
				System.exit(0);
			}
		}
		stop = true;
	}

	public int setCurrent(Piece piece, int x, int y) {
		int result = board.place(piece, x, y);

		if (result <= Board.PLACE_ROW_FILLED) {
			if (currentPiece != null) {
				repaintPiece(currentPiece, currentX, currentY);
			}
			currentPiece = piece;
			currentX = x;
			currentY = y;
			repaintPiece(currentPiece, currentX, currentY);
		} else {
			board.undo();
		}

		return (result);
	}

	public Piece pickNextPiece() {
		int pieceNum;

		pieceNum = (int) (pieces.length * random.nextDouble());

		Piece piece = pieces[pieceNum];

		return (piece);
	}

	public void addNewPiece() {
		count++;
		if (testMode && count == TEST_LIMIT + 1) {
			stopGame();
			return;
		}

		board.commit();
		currentPiece = null;

		Piece piece = pickNextPiece();

		int px = (board.getWidth() - piece.getWidth()) / 2;
		int py = board.getHeight() - piece.getHeight();

		int result = setCurrent(piece, px, py);

		if (result > Board.PLACE_ROW_FILLED) {
			stopGame();
		}
	}

	private void updateCounters() {
		lbScore.setText("Score " + score);
	}

	public void computeNewPosition(int verb) {
		newPiece = currentPiece;
		newX = currentX;
		newY = currentY;

		switch (verb) {
		case LEFT:
			newX--;
			break;

		case RIGHT:
			newX++;
			break;

		case ROTATE:
			newPiece = newPiece.fastRotation();
			newX = newX + (currentPiece.getWidth() - newPiece.getWidth()) / 2;
			newY = newY + (currentPiece.getHeight() - newPiece.getHeight()) / 2;
			break;

		case DOWN:
			newY--;
			break;

		case DROP:
			newY = board.dropHeight(newPiece, newX);
			if (newY > currentY) {
				newY = currentY;
			}
			break;

		default:
			throw new RuntimeException("Bad verb");
		}

	}

	public void tick(int verb) {
		if (!gameOn) {
			return;
		}

		if (currentPiece != null) {
			board.undo();
		}

		computeNewPosition(verb);

		int result = setCurrent(newPiece, newX, newY);
		if (result == Board.PLACE_ROW_FILLED) {
			repaint();
		}

		boolean failed = (result >= Board.PLACE_OUT_BOUNDS);
		if (failed) {
			if (currentPiece != null) {
				board.place(currentPiece, currentX, currentY);
			}
			repaintPiece(currentPiece, currentX, currentY);
		}

		if (failed && verb == DOWN && !moved) {

			int cleared = board.clearRows();
			if (cleared > 0) {

				switch (cleared) {
				case 1:
					score += 5;
					break;
				case 2:
					score += 10;
					break;
				case 3:
					score += 20;
					break;
				case 4:
					score += 40;
					break;
				default:
					score += 50;
				}
				updateCounters();
				repaint();
			}

			if (board.getMaxHeight() > board.getHeight() - TOP_SPACE) {
				stopGame();
			} else {
				addNewPiece();
			}
		}

		moved = (!failed && verb != DOWN);
	}

	int check;
	private Brain.Move brainMove;
	private DefaultBrain brain;

	public void tickBrain(int verb) {
		if (cbBrainMode.isSelected()) {
			if (check != this.count) {
				board.undo();
				brainMove = brain.bestMove(board, currentPiece,
						board.getHeight(), brainMove);
				check = count;
			}
			if (!currentPiece.equals(brainMove.piece)) {
				tick(ROTATE);
			}
			if (currentPiece.equals(brainMove.piece) == true
					&& currentX > brainMove.x) {
				tick(LEFT);
			}
			if (currentPiece.equals(brainMove.piece) == true
					&& currentX < brainMove.x) {
				tick(RIGHT);
			}
		}
		this.tick(verb);
	}

	public void repaintPiece(Piece piece, int x, int y) {
		if (DRAW_OPTIMIZE) {
			int px = xPixel(x);
			int py = yPixel(y + piece.getHeight() - 1);
			int pwidth = xPixel(x + piece.getWidth()) - px;
			int pheight = yPixel(y - 1) - py;

			repaint(px, py, pwidth, pheight);
		} else {
			repaint();
		}
	}

	private final float dX() {
		return (((float) (getWidth() - 2)) / board.getWidth());
	}

	private final float dY() {
		return (((float) (getHeight() - 2)) / board.getHeight());
	}

	private final int xPixel(int x) {
		return (Math.round(1 + (x * dX())));
	}

	private final int yPixel(int y) {
		return (Math.round(getHeight() - 1 - (y + 1) * dY()));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		ImageIcon icon = new ImageIcon(getClass().getResource(
				"/IMAGE/background.jpg").getPath());
		Image img = icon.getImage();
		g2d.drawImage(img, 0, 0, GUI.WIDTH, GUI.HEIGHT, null);
		g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

		g2d.setColor(Color.BLUE.darker());
		g2d.fillRect(0, 0, 400, 40);
		Rectangle clip = null;
		if (DRAW_OPTIMIZE) {
			clip = g.getClipBounds();
		}

		final int dx = Math.round(dX() - 2);
		final int dy = Math.round(dY() - 2);
		final int bWidth = board.getWidth();

		int x, y;
		for (x = 0; x < bWidth; x++) {
			int left = xPixel(x); // the left pixel

			int right = xPixel(x + 1) - 1;

			if (DRAW_OPTIMIZE && clip != null) {
				if ((right < clip.x) || (left >= (clip.x + clip.width))) {
					continue;
				}
			}

			final int yHeight = board.getColumnHeight(x);
			for (y = 0; y < yHeight; y++) {
				if (board.getGrid(x, y)) {
					boolean filled = (board.getRowWidth(y) == bWidth);
					if (filled) {
						g2d.setColor(Color.GREEN);
					}

					g2d.fillRect(left + 1, yPixel(y) + 1, dx, dy);

					if (filled) {
						g2d.setColor(Color.BLUE.darker());
					}
				}
			}
		}
	}

	public MenuGamePanel getMenu() {
		return menu;
	}

	public void setMenu(MenuGamePanel menu) {
		this.menu = menu;
	}

	public interface OnPlayGameAgainListener {
		void restartGame();
	}

}
