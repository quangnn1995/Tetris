package com.quangnn.ui;

import java.awt.CardLayout;

import javax.swing.JFrame;

public class GUI extends JFrame {
	/** */
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 400;
	public static final int HEIGHT = 700;

	private MyContainer contaier;

	public GUI() {
		initGUI();
		initComps();
		addComps();
	}

	private void initGUI() {
		this.setTitle("Tetris");
		this.setLayout(new CardLayout());
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initComps() {
		contaier = new MyContainer();
	}

	private void addComps() {
		this.add(contaier);
	}
}
