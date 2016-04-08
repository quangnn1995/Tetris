package com.quangnn.shape;

public class Board {
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	private int width;
	private int height;
	private int MaxHeight;

	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;
	private boolean[][] bacKupGrid;

	private int[] widths;
	private int[] heights;

	/**
	 * 
	 * [Method name] //Method name [Descript] //Tạo một bảng rỗng với kích thước
	 * cho sẵn tính theo khối [Author] quang [Date] Apr 2, 2016
	 */
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[this.width][this.height];
		MaxHeight = 0;
		widths = new int[this.height];
		heights = new int[width];
		this.bacKupGrid = new boolean[this.width][this.height];
		committed = true;
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				this.grid[i][j] = false;
				this.bacKupGrid[i][j] = false;
				this.heights[i] = 0;
				this.widths[j] = 0;
			}
		}
	}

	
	public boolean isCommitted() {
		return committed;
	}


	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * 
	 * [Method name] //Method name [Descript] //Trả về chiều cao của cột cao
	 * nhất trong bảng [Author] quang
	 */
	public int getMaxHeight() {
		return this.MaxHeight;
	}

	/**
	 * 
	 * [Method name] //Method name [Descript] //Kiểm tra dữ liệu của bảng xem có
	 * nhất quán không -- dùng cho việc soát lỗi. [Author] quang [Date] Apr 2,
	 * 2016
	 */
	public void sanityCheck() {
		if (DEBUG) {
			int maxHeight = 0;
			for (int i = 0; i < this.width; i++) {
				int Height = 0;

				for (int j = this.height - 1; j >= 0; j--) {
					if (this.grid[i][j] == false) {
						Height++;
					} else {
						break;
					}
				}
				maxHeight = Math.max(maxHeight, this.height - Height);
				if (this.heights[i] != this.height - Height) {
					throw new RuntimeException("exception");
				}
			}
			for (int i = 0; i < this.height; i++) {
				int Row = 0;
				for (int j = 0; j < this.width; j++) {
					if (this.grid[j][i]) {
						Row++;
					}
				}
				if (this.widths[i] != Row) {
					throw new RuntimeException("exception");
				}
			}
			if (maxHeight != this.MaxHeight) {
				throw new RuntimeException("exception");
			}
		}
	}

	/**
	 * 
	 * [Method name] //Method name [Descript] // Cho một mảnh và giá trị x, trả
	 * về giá trị y mà mảnh sẽ chạm đáy tại đó nếu nó được thả thẳng xuống tại
	 * cột x [Author] quang [Date] Apr 2, 2016
	 */
	public int dropHeight(Piece piece, int x) {
		int maxHeight = 0;
		int nextKirt[] = piece.getSkirt();
		for (int i = 0; i < nextKirt.length; i++) {
			maxHeight = Math.max(maxHeight, heights[x + i] - nextKirt[i]);
		}
		return maxHeight;
	}

	private int[] buildHeight() {
		for (int i = 0; i < this.width; i++) {
			int count = 0;
			for (int j = this.height - 1; j >= 0; j--) {
				if (this.grid[i][j] == false) {
					count++;
				} else {
					break;
				}
			}
			this.heights[i] = this.height - count;
		}
		this.MaxHeight = 0;

		for (int j = 0; j < width; j++) {
			this.MaxHeight = Math.max(this.MaxHeight, this.heights[j]);
		}
		return heights;
	}

	private int[] buildWidth() {

		for (int i = 0; i < this.height; i++) {
			int count = 0;
			for (int j = 0; j < this.width; j++) {
				if (this.grid[j][i]) {
					count++;
				}
			}
			this.widths[i] = count;
		}
		return widths;
	}

	/**
	 * 
	 * [Method name] //Method name [Descript] //Trả về chiều cao của cột x --
	 * nghĩa là giá trị y của khối cao nhất + 1. Chiều cao bằng 0 nếu cột không
	 * chứa khối nào [Author] quang [Date] Apr 2, 2016
	 */
	public int getColumnHeight(int x) {
		return heights[x];
	}

	/**
	 * 
	 * [Method name] //Method name [Descript] //trả về số khối đã lấp trong hàng
	 * y [Author] quang
	 */
	public int getRowWidth(int y) {
		return widths[y];
	}

	/**
	 * 
	 * [Method name] //Method name [Descript] //Trả về true nếu khối (x,y) trong
	 * bảng đã được lấp. Các khối nằm ngoài phạm vi hợp lệ của bảng luôn trả về
	 * true. [Author] quang
	 */
	public boolean getGrid(int x, int y) {
		return x >= 0 && y >= 0 && x < width && y < height && grid[x][y];
	}


	public int place(Piece piece, int x, int y) {
		if (!committed) {
			throw new RuntimeException("place commit problem");
		}
		for (int i = 0; i < this.width; i++) {
			System.arraycopy(grid[i], 0, this.bacKupGrid[i], 0, this.height);
		}

		if (x < 0 || y < 0 || x + piece.getWidth() > this.width
				|| y + piece.getHeight() > this.height) {
			return PLACE_OUT_BOUNDS;
		}

		for (TPoint body : piece.getBody()) {
			if (grid[x + body.x][y + body.y]) {
				return PLACE_BAD;
			}
		}

		int result = PLACE_OK;

		for (TPoint body : piece.getBody()) {
			int tmpx = x + body.x;
			int tmpy = y + body.y;
			grid[tmpx][tmpy] = true;
			heights = this.buildHeight();
			widths = this.buildWidth();
		}

		for (int i = y; i < y + piece.getHeight(); i++) {
			if (this.widths[i] == this.width) {
				result = PLACE_ROW_FILLED;
			}
		}
		sanityCheck();

		committed = false;
		return result;
	}

	/**
	 * 
	 * [Method name] //Method name [Descript] //Xóa các dòng đã được lấp đầy,
	 * dịch các khối bên trên xuống. Trả về số dòng đã xóa. [Author] quang
	 * [Date] Apr 2, 2016
	 */
	public int clearRows() {
		int rowsCleared = 0;
		int k = this.getMaxHeight();
		for (int i = 0; i < k; i++) {
			if (this.widths[i] == this.width) {
				rowsCleared++;
				for (int j = i; j < k; j++) {
					for (int t = 0; t < this.width; t++) {
						this.grid[t][j] = this.grid[t][j + 1];
						this.widths[j] = this.widths[j + 1];
					}
				}
				k--;
				i--;
			}
		}
		this.heights = this.buildHeight();
		this.widths = this.buildWidth();
		sanityCheck();
		return rowsCleared;
	}

	public void undo() {
		if (committed == true) {
			return;
		}

		boolean[][] tempGrid = grid;
		grid = this.bacKupGrid;
		this.bacKupGrid = tempGrid;

		this.heights = this.buildHeight();
		this.widths = this.buildWidth();

		committed = true;
		for (int i = 0; i < this.width; i++) {
			System.arraycopy(grid[i], 0, this.bacKupGrid[i], 0, this.height);
		}
	}

	public void commit() {
		committed = true;
	}

	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height - 1; y >= 0; y--) {
			buff.append('|');
			for (int x = 0; x < width; x++) {
				if (getGrid(x, y))
					buff.append('+');
				else
					buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x = 0; x < width + 2; x++)
			buff.append('-');
		return (buff.toString());
	}
}
