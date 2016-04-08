package com.quangnn.brain;

import com.quangnn.shape.Board;
import com.quangnn.shape.Piece;

public interface Brain {
	// Move là cấu trúc lưu một nước đơn.
	// "static" ở đây có nghĩa rằng nó không trỏ tới một đối tượng ngoài Brain
	// nó chỉ nằm trong không gian Brain.)
	public static class Move {
		public int x;
		public int y;
		public Piece piece;
		public double score;
	}

	/**
	 * 
	 * [Method name] //Method name [Descript] //Cho một mảnh và một bảng, trả về
	 * một đối tượng move đại diện cho nước đi tốt nhất cho mảnh đó, hoặc trả về
	 * null nếu không thể chơi mảnh đó. Bảng cần ở trạng thái committed khi gọi
	 * hàm này. [Author] quang [Date] Apr 9, 2016
	 */
	public Brain.Move bestMove(Board board, Piece piece, int limitHeight,
			Brain.Move move);
}
