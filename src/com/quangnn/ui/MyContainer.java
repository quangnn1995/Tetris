package com.quangnn.ui;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JPanel;

import com.quangnn.ui.PlayGamePanel.OnPlayGameAgainListener;

public class MyContainer extends JPanel implements OnPlayGameAgainListener {
	/** */
	private static final long serialVersionUID = 1L;

	private MenuGamePanel menu;
	private PlayGamePanel play;

	public MyContainer() {
		initPanel();
		initComps();
		addComps();
	}

	private void initPanel() {
		this.setLayout(new CardLayout());
		this.setBackground(Color.WHITE);
	}

	private void initComps() {
		menu = new MenuGamePanel();
		play = new PlayGamePanel();
		play.setOnPlayGameAgainListener(this);
	}

	private void addComps() {
		this.add(menu);
		this.add(play);
		menu.setPlayPanel(play);
		play.setMenu(menu);
	}

	@Override
	public void restartGame() {
		menu.setVisible(true);
		play.setVisible(false);
	}

}
