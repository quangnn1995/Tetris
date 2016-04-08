package com.quangnn.shape;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Piece {
	// Các hằng xâu kí tự cho 7 mảnh tetris chuẩn
	public static final String STICK_STR = "0 0	0 1	 0 2  0 3";
	public static final String L1_STR = "0 0	0 1	 0 2  1 0";
	public static final String L2_STR = "0 0	1 0 1 1	 1 2";
	public static final String S1_STR = "0 0	1 0	 1 1  2 1";
	public static final String S2_STR = "0 1	1 1  1 0  2 0";
	public static final String SQUARE_STR = "0 0  0 1  1 0  1 1";
	public static final String PYRAMID_STR = "0 0  1 0  1 1  2 0";

	// Chỉ số của 7 mảnh chuẩn trong mảng pieces
	public static final int STICK = 0;
	public static final int L1 = 1;
	public static final int L2 = 2;
	public static final int S1 = 3;
	public static final int S2 = 4;
	public static final int SQUARE = 5;
	public static final int PYRAMID = 6;

	private TPoint[] body;
	private int[] skirt;

	private int width;
	private int height;

	private Piece next;

	static private Piece[] pieces;

	/**
	 * 
	 * [Method name] //Piece() [Descript] //Khởi tạo một mảnh mới từ một mảng
	 * TPoint[] chứa thân mảnh. Chép mảng points vào mảng của riêng đối tượng
	 * hiện hành. [Author] quang [Date] Apr 1, 2016
	 */
	public Piece(TPoint[] points) {
		body = new TPoint[points.length];
		for (int i = 0; i < points.length; i++) {
			body[i] = new TPoint(points[i]);
		}

		for (TPoint body1 : body) {
			height = Math.max(height, body1.y);
			width = Math.max(width, body1.x);
		}
		height++;
		width++;

		skirt = new int[width];
		for (int i = 0; i < width; i++) {
			skirt[i] = height;
		}

		for (TPoint body1 : body) {
			if (body1.y <= skirt[body1.x]) {
				skirt[body1.x] = body1.y;
			}
		}
	}

	/**
	 * 
	 * [Method name] //Method name [Descript] //Hàm tạo thay thế, lấy đối số là
	 * một String với tọa độ của các khối thân mảnh [Author] quang [Date] Apr 1,
	 * 2016
	 */
	public Piece(String points) {
		this(parsePoints(points));
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public TPoint[] getBody() {
		return body;
	}

	/**
	 * 
	 * [Method name] //Method name [Descript] //Trả về một con trỏ tới skirt của
	 * mảnh. Với mỗi giá trị x dọc theo chiều ngang của mảnh, skirt lưu giá trị
	 * y thấp nhất của thân mảnh. Dữ liệu này hữu ích cho việc tính xem mảnh sẽ
	 * chạm đáy ở đâu. [Author] quang [Date] Apr 1, 2016
	 */
	public int[] getSkirt() {
		return skirt;
	}

	/**
	 * 
	 * [Method name] //Method name [Descript] //Trả về một mảnh mới là kết quả
	 * của việc xoay đối tượng 90o ngược chiều kim đồng hồ [Author] quang [Date]
	 * Apr 1, 2016
	 */
	public Piece computeNextRotation() {
		TPoint[] nextBody = new TPoint[body.length];

		for (int i = 0; i < nextBody.length; i++) {
			nextBody[i] = new TPoint(height - body[i].y - 1, body[i].x);
		}
		return new Piece(nextBody);
	}

	/**
	 * 
	 * [Method name] //Method name [Descript] //Trả về mảnh đã được tính từ
	 * trước, là kết quả của việc xoay đối tượng hiện hành 90o ngược chiều kim
	 * đồng hồ. [Author] quang [Date] Apr 1, 2016
	 */
	public Piece fastRotation() {
		return next;
	}

	/**
	 * Trả về true nếu hai mảnh giống nhau
	 */
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof Piece))
			return false;
		Piece other = (Piece) obj;

		int count = 0;
		if (body.length != other.body.length) {
			return false;
		} else {

			for (TPoint body1 : body) {
				for (int j = 0; j < body.length; j++) {
					if (body1.equals(other.body[j])) {
						count++;
					}
				}
			}
		}
		return count == body.length;
	}

	/**
	 * 
	 * [Method name] //Method name [Descript] //Trả về một mảng chứa trạng thái
	 * xoay đầu tiên của 7 mảnh chuẩn theo thứ tự STICK, L1, L2, S1, S2, SQUARE,
	 * PYRAMID. [Author] quang [Date] Apr 1, 2016
	 */
	public static Piece[] getPieces() {
		if (Piece.pieces == null) {
			Piece.pieces = new Piece[] {
					makeFastRotations(new Piece(STICK_STR)),
					makeFastRotations(new Piece(L1_STR)),
					makeFastRotations(new Piece(L2_STR)),
					makeFastRotations(new Piece(S1_STR)),
					makeFastRotations(new Piece(S2_STR)),
					makeFastRotations(new Piece(SQUARE_STR)),
					makeFastRotations(new Piece(PYRAMID_STR)), };
		}

		return Piece.pieces;
	}

	/**
	 * 
	 * [Method name] //Method name [Descript] // * Cho trước trạng thái gốc (đầu
	 * tiên) của một mảnh, tính tất cả các trạng thái xoay khác và liên kết
	 * chúng với nhau trong một danh sách dạng vòng tròn quay ngược lại điểm
	 * gốc. [Author] quang [Date] Apr 1, 2016
	 */
	private static Piece makeFastRotations(Piece root) {
		Piece other = root;
		Piece nextPiece = other.computeNextRotation();
		while (!nextPiece.equals(root)) {
			other.next = nextPiece;
			other = nextPiece;
			nextPiece = other.computeNextRotation();
		}

		other.next = root;
		return root;
	}

	/**
	 * 
	 * [Method name] //Method name [Descript] // Cho trước một xâu kí tự gồm các
	 * cặp x,y, ví dụ ("0 0	0 1 0 2 1 0"), dịch thành một mảng TPoint[].
	 * [Author] quang [Date] Apr 1, 2016
	 */
	private static TPoint[] parsePoints(String string) {
		List<TPoint> points = new ArrayList<TPoint>();
		StringTokenizer tok = new StringTokenizer(string);
		try {
			while (tok.hasMoreTokens()) {
				int x = Integer.parseInt(tok.nextToken());
				int y = Integer.parseInt(tok.nextToken());

				points.add(new TPoint(x, y));
			}
		} catch (NumberFormatException e) {
			throw new RuntimeException("Could not parse x,y string:" + string);
		}

		TPoint[] array = points.toArray(new TPoint[0]);
		return array;
	}
}
