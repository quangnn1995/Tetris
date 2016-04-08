package com.quangnn.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuGamePanel extends JPanel {
	/** */
	private static final long serialVersionUID = 1L;

	private JButton btPlay;
	private JButton btExit;

	Font font = new Font("Tahoma", Font.BOLD, 14);
	private PlayGamePanel play;

	int btWidth = 200;
	int btHeight = 30;

	public MenuGamePanel() {
		initPanel();
		initComps();
		addComps();
	}

	private void initPanel() {
		this.setLayout(null);
		this.setBackground(Color.GREEN.darker());
	}

	private void initComps() {
		ImageIcon icon = new ImageIcon(getClass()
				.getResource("/IMAGE/play.png").getPath());
		btPlay = new JButton(icon);
		btPlay.setBounds(GUI.WIDTH / 2 - btWidth / 2, 100, btWidth, btHeight);
		btPlay.setForeground(Color.YELLOW);
		btPlay.addActionListener(action);

		ImageIcon icon1 = new ImageIcon(getClass().getResource(
				"/IMAGE/exit.png").getPath());
		btExit = new JButton(icon1);
		btExit.setBounds(GUI.WIDTH / 2 - btWidth / 2, 150, btWidth, btHeight);
		btExit.setForeground(Color.YELLOW);
		btExit.addActionListener(action);
	}

	private void addComps() {
		this.add(btPlay);
		this.add(btExit);
	}

	public void setPlayPanel(PlayGamePanel play) {
		this.play = play;
	}

	ActionListener action = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(btPlay)) {
				MenuGamePanel.this.setVisible(false);
				play.setVisible(true);
				play.startGame();
			} else if (e.getSource().equals(btExit)) {
				System.exit(0);
			}
		}
	};

	Color color1 = new Color(205, 0, 0);
	Color color2 = new Color(255, 255, 0);
	Color color = color1;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		ImageIcon icon = new ImageIcon(getClass().getResource(
				"/IMAGE/menu_background.jpg").getPath());
		Image img = icon.getImage();
		g2d.drawImage(img, 0, 0, GUI.WIDTH, GUI.HEIGHT, null);
		g2d.setFont(font);
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (color == color1) {
						color = color2;
					} else {
						color = color1;
					}
					try {
						g2d.setColor(color);
						g2d.drawString(
								"press 'up' to rotate, press 'left' to move left",
								40, 560);
						g2d.drawString(
								"press 'right' to move right, press 'down' to move down",
								5, 580);
						g2d.drawString("click 'Brain' to auto play", 120, 600);
						repaint();
					} catch (NullPointerException e) {
					}
				}
			}
		});
		thread.start();
		try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
