package com.quangnn.brain;

import com.quangnn.shape.Board;

public class BadBrain extends DefaultBrain{
	public double rateBoard(Board board) {
		double score = super.rateBoard(board);
		return(10000 - score);
	}
}
