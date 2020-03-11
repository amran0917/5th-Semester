package gamePackage;

import java.util.ArrayList;
import java.util.Random;

public class Game {
	private int boardRow;
	private int boardCol;

	private int sourceI;
	private int sourceJ;

	private int goldI;
	private int goldJ;

	 int fx[] = { 1, 0, 0, -1 };
	 int fy[] = { 0, 1, -1, 0 };
//
//	int fx[] = { 0, 0, -1, 1 };
//	int fy[] = { -1, 1, 0, 0 };

	private String[][] board;

	private boolean[][] pits;
	private boolean[][] wumpus;
	private boolean[][] breeze;
	private String[][] safePits;
	private String[][] safeWumpus;
	private boolean[][] visited;

	// private int arrowNo = GameSettings.arrowNo;
	
	ArrayList<Position> backPath = new ArrayList<Position>(); 

	int parentI = sourceI;
	int parentJ = sourceJ;

	public Game() {
		init();
	}

	private void init() {

		boardRow = GameSettings.BOARD_ROW;
		boardCol = GameSettings.BOARD_COL;

		this.sourceI = GameSettings.sourceI;
		this.sourceJ = GameSettings.sourceJ;

		this.goldI = GameSettings.goldI;
		this.goldJ = GameSettings.goldJ;

		board = new String[boardRow][boardCol];

		pits = new boolean[boardRow][boardCol];
		wumpus = new boolean[boardRow][boardCol];

		breeze = new boolean[boardRow][boardCol];
		visited = new boolean[boardRow][boardCol];

		safePits = new String[boardRow][boardCol];
		safeWumpus = new String[boardRow][boardCol];

		safePits[sourceI][sourceJ] = "safe";
		safeWumpus[sourceI][sourceJ] = "safe";

	}

	public void printBoard(String board[][]) {
		for (int i = 0; i < boardRow; i++)
			System.out.print("|-----");
		System.out.println("|");

		for (int i = 0; i < boardRow; i++) {
			System.out.print("|  ");
			for (int j = 0; j < boardCol; j++) {
				System.out.print(board[i][j]);
				if (j != boardCol - 1)
					System.out.print("  |  ");
			}
			System.out.println("  |  ");

			for (int k = 0; k < boardRow; k++)
				System.out.print("|-----");
			System.out.println("|");
		}

	}

	public String[][] initialiseBoard2() {

		Position tempPos;
		board = new String[boardRow][boardCol];

		for (int i = 0; i < boardRow; i++) {
			for (int j = 0; j < boardCol; j++) {
				board[i][j] = "E";
			}
		}

		board[sourceI][sourceJ] = "S";
		board[goldI][goldI] = "G";

		for (int pitN = 0; pitN < 2; pitN++) {
			tempPos = getElementsPosition(board);
			board[tempPos.row][tempPos.col] = "P";
		}
		for (int wumN = 0; wumN < 1; wumN++) {
			tempPos = getElementsPosition(board);
			board[tempPos.row][tempPos.col] = "W";
		}

		int rn = new Random().nextInt(2);

		if (rn == 0) {
			board[boardRow - 1 - 2][0] = "P";
			board[boardRow - 1][2] = "W";
		} else {
			board[boardRow - 1 - 2][0] = "W";
			board[boardRow - 1][2] = "P";
		}
		printBoard(board);

		board = setBGS(board);

		printBoard(board);

		return board;
	}

	public String[][] initialiseBoard() {

		for (int i = 0; i < boardRow; i++) {
			for (int j = 0; j < boardCol; j++) {
				board[i][j] = "E";

				pits[i][j] = true;
				wumpus[i][j] = true;

				breeze[i][j] = true;
				visited[i][j] = false;

				safePits[i][j] = "dk"; // do not know.
				safeWumpus[i][j] = "dk"; // do not know.
			}
		}
		Position tempPos;

		board[goldI][goldJ] = "G";

		board[0][2] = "P";
		board[3][9] = "P";

		for (int pitN = 0; pitN < 3; pitN++) {
			tempPos = getElementsPosition(board);
			board[tempPos.row][tempPos.col] = "P";
		}

		int rn = new Random().nextInt(2);

		if (rn == 0) {
			board[boardRow - 1 - 2][0] = "P";
			board[boardRow - 1][2] = "W";
		} else {
			board[boardRow - 1 - 2][0] = "W";
			board[boardRow - 1][2] = "P";
		}

		visited[sourceI][sourceJ] = true;

		board = setBGS(board);

		return board;
	}

	private String[][] setBGS(String board[][]) {
		for (int ii = 0; ii < boardRow; ii++) {
			for (int jj = 0; jj < boardRow; jj++) {
				String cellCH = "N";
				if (board[ii][jj].equals("P")) {
					cellCH = "B";
				} else if (board[ii][jj].equals("W")) {
					cellCH = "S";
				}
				for (int k = 0; k < 4; k++) {
					int tx = ii + fx[k];
					int ty = jj + fy[k];

					if (adjacentCellValid(tx, ty) && !cellCH.equals("N")
							&& !(board[tx][ty].equals("G") || board[tx][ty].equals("W") || board[tx][ty].equals("P"))) {

						if (board[tx][ty].equals("E"))
							board[tx][ty] = "";
						if (!board[tx][ty].contains(cellCH))
							board[tx][ty] += cellCH;
					}
				}
			}
		}
		return board;
	}

	private Position getElementsPosition(String board[][]) {
		while (true) {
			int i = new Random().nextInt(boardRow);
			int j = new Random().nextInt(boardCol);

			if (!board[i][j].equals("E"))
				continue;
			if (i == sourceI && j == sourceJ)
				continue;
			if (i == sourceI - 1 && j == sourceJ)
				continue;
			if (i == sourceI && j == sourceJ + 1)
				continue;
			if (i == boardRow - 1 && j == boardRow - 1 - 2)
				continue;
			if (i == boardRow - 1 - 2 && j == 0)
				continue;
			if (i == boardRow - 2 && j == 1)
				continue;

			return new Position(i, j);
		}
	}

	private boolean adjacentCellValid(int tx, int ty) {
		if (tx >= boardRow || tx < 0)
			return false;
		if (ty >= boardCol || ty < 0)
			return false;
		return true;
	}

	private void setSafeCellSituation(int tx, int ty, boolean entailedRes, boolean isPitType) {
		if (isPitType) {
			if (entailedRes)
				safePits[tx][ty] = "safe";
			else {
				if (!safePits[tx][ty].equals("safe"))
					safePits[tx][ty] = "dk";
			}
		} else {
			if (entailedRes)
				safeWumpus[tx][ty] = "safe";
			else {
				if (!safeWumpus[tx][ty].equals("safe"))
					safeWumpus[tx][ty] = "dk";
			}
		}
	}

	private void setAllAdjacentCellSituation(int I, int J) {

		boolean negOfPit = false;
		boolean negOfWumpus = false;

		if (board[I][J].equals("E")) {
			negOfPit = true;
			negOfWumpus = true;
		} else if (board[I][J].equals("B")) {
			negOfPit = false;
			negOfWumpus = true;
		} else if (board[I][J].equals("S")) {
			negOfPit = true;
			negOfWumpus = false;
		} else if (board[I][J].equals("BS")) {
			negOfPit = false;
			negOfWumpus = false;
		}

		for (int ii = 0; ii < 4; ii++) {

			int tx = I + fx[ii];
			int ty = J + fy[ii];
			boolean isValidCell = adjacentCellValid(tx, ty);

			if (isValidCell) {

				boolean entailPit = entail(I, J, true, negOfPit, pits[tx][ty], true);
				boolean entailWumpus = entail(I, J, true, negOfWumpus, wumpus[tx][ty], false);

				setSafeCellSituation(tx, ty, entailPit, true);
				setSafeCellSituation(tx, ty, entailWumpus, false);

			}
		}
	}

	private void throwArrowIfThereISWumpus(int i, int j) {
		for (int ii = 0; ii < 4; ii++) {
			int tx = i + fx[ii];
			int ty = j + fy[ii];

			boolean isValidCell = adjacentCellValid(tx, ty);

			if (isValidCell && safeWumpus[tx][ty].equals("wum") && GameSettings.arrowNo == 1) {
				board[tx][ty] = "E";
				for (int jj = 0; jj < 4; jj++) {
					int tx2 = tx + fx[jj];
					int ty2 = ty + fy[jj];
					isValidCell = adjacentCellValid(tx2, ty2);
					if (isValidCell) {

						if (board[tx2][ty2].equals("P")) {
							board[tx2][ty2] = "B";
						}
						if (board[tx2][ty2].equals("BS"))
							board[tx2][ty2] = "B";
						else if (board[tx2][ty2].equals("S")) {
							board[tx2][ty2] = "E";
						}
						visited[tx2][ty2] = false;
					}
				}
				GameSettings.arrowNo = 0;
			}
		}
	}

	public Position getNextPosition(Position position) {
		int I = position.row;
		int J = position.col;

		checkPitsPossibility(board);
		checkWumpusPossibility(board);

		boolean isAnyWay = true;

		System.out.println(I + ", " + J);

		setAllAdjacentCellSituation(I, J);

		for (int ii = 0; ii < 4; ii++) {
			int tx = I + fx[ii];
			int ty = J + fy[ii];
			boolean isValidCell = adjacentCellValid(tx, ty);

			if (isValidCell && !visited[tx][ty] && safePits[tx][ty].equals("safe")
					&& safeWumpus[tx][ty].equals("safe")) {

				parentI = I;
				parentJ = J;

				I = tx;
				J = ty;

				visited[I][J] = true;
				isAnyWay = true;

				break;
			} else if (isValidCell && (!safePits[tx][ty].equals("safe") || !safeWumpus[tx][ty].equals("safe"))) {
				isAnyWay = false;
			}
		}
		if (I == parentI && J == parentJ) {
			return new Position(-1, -1);
		}

		if (!isAnyWay) {
			I = parentI;
			J = parentJ;
		}

		throwArrowIfThereISWumpus(I, J);

		return new Position(I, J);

	}

	public Position exploreUnexploredCell(ArrayList<Position> path) {
		while (true) {

			int lastIndex = path.size() - 1;
			Position pos = path.get(lastIndex);
			path.remove(lastIndex);

			for (int ii = 0; ii < 4; ii++) {
				int tx = pos.row + fx[ii];
				int ty = pos.col + fy[ii];

				if (adjacentCellValid(tx, ty) && safePits[tx][ty].equals("safe") && safeWumpus[tx][ty].equals("safe")) {
					return new Position(tx, ty);
				}
			}
		}

	}

	private String[][] checkPitsPossibility(String board[][]) {

		for (int i = 0; i < boardRow; i++) {
			for (int j = 0; j < boardCol; j++) {
				if (board[i][j].equals("B") && visited[i][j]) {
					int validAdjCell = 0;
					int safeAdjCell = 0;
					for (int ii = 0; ii < 4; ii++) {
						int tx = i + fx[ii];
						int ty = j + fy[ii];

						if (adjacentCellValid(tx, ty)) {
							validAdjCell++;
							if (safePits[tx][ty].equals("safe") && visited[tx][ty]) {
								safeAdjCell++;
							}
						}
					}

					if (safeAdjCell == validAdjCell - 1) {
						for (int ii = 0; ii < 4; ii++) {
							int tx = i + fx[ii];
							int ty = j + fy[ii];

							if (adjacentCellValid(tx, ty)) {
								validAdjCell++;
								if (safePits[tx][ty].equals("dk")) {
									safePits[tx][ty] = "pit";
									System.out.println("There must be a pit on cell: " + tx + " " + ty);
								}
							}
						}
					}
				}
			}
		}
		return board;
	}

	private String[][] checkWumpusPossibility(String board[][]) {

		for (int i = 0; i < boardRow; i++) {
			for (int j = 0; j < boardCol; j++) {
				if (board[i][j].equals("S") && visited[i][j]) {
					int validAdjCell = 0;
					int safeAdjCell = 0;
					for (int ii = 0; ii < 4; ii++) {
						int tx = i + fx[ii];
						int ty = j + fy[ii];

						if (adjacentCellValid(tx, ty)) {
							validAdjCell++;
							if (safeWumpus[tx][ty].equals("safe")) {
								safeAdjCell++;
							}
						}
					}

					if (safeAdjCell == validAdjCell - 1) {
						for (int ii = 0; ii < 4; ii++) {
							int tx = i + fx[ii];
							int ty = j + fy[ii];

							if (adjacentCellValid(tx, ty)) {
								validAdjCell++;
								if (safeWumpus[tx][ty].equals("dk")) {
									safeWumpus[tx][ty] = "wum";
									System.out.println("There must be a wumpus on cell: " + tx + " " + ty);
								}
							}
						}
					}
				}
			}
		}
		return board;
	}

	private void temp() {
		for (int i = 0; i < boardRow; i++) {
			for (int j = 0; j < boardCol; j++) {
				System.out.println(i + ", " + j + ": " + safePits[i][j]);
			}
		}
	}

	private boolean entail(int i, int j, boolean lhs, boolean negOfLhs, boolean alpha, boolean isPitTypeEntail) {
		ArrayList<Boolean> sentences = new ArrayList<Boolean>();

		boolean x = !lhs;

		for (int ii = 0; ii < 4; ii++) {

			int tx = i + fx[ii];
			int ty = j + fy[ii];
			boolean isValidCell = adjacentCellValid(tx, ty);
			if (isValidCell) {
				if (isPitTypeEntail) {
					x |= pits[tx][ty];
					sentences.add((!pits[tx][ty]) | lhs);
				} else {
					x |= wumpus[tx][ty];
					sentences.add((!wumpus[tx][ty]) | lhs);
				}
			}
		}
		sentences.add(x);
		boolean res = true;
		for (int ii = 0; ii < sentences.size(); ii++) {
			res &= sentences.get(ii);
		}
		if (negOfLhs)
			res &= !lhs;
		res &= alpha;
		return !res;
	}
}
