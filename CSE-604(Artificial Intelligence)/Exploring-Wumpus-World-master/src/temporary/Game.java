package temporary;

import java.util.ArrayList;

public class Game {
	private String[][] board;

	private boolean[][] pits;
	private boolean[][] breeze;
	private String[][] safePits;
	private boolean[][] visited;

	int fx[] = { 0, 1, 0, -1 };
	int fy[] = { 1, 0, -1, 0 };

	private int boardSize = 5;

	private int goldI = 4;
	private int goldJ = 4;

	public Game() {
		init();
		makeBoard();
		printBoard(board);
		start();
//		temp();
	}

	private void init() {

		board = new String[boardSize][boardSize];
		pits = new boolean[boardSize][boardSize];
		breeze = new boolean[boardSize][boardSize];
		visited = new boolean[boardSize][boardSize];
		safePits = new String[boardSize][boardSize];

	}

	private void makeBoard() {

		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				board[i][j] = "N";
				pits[i][j] = true;
				breeze[i][j] = true;
				visited[i][j] = false;

				safePits[i][j] = "dk"; // do not know.
			}
		}
		board[0][2] = "P";
		board[3][2] = "P";
		board[2][4] = "P";

		board[goldI][goldJ] = "G";

		board = setBGS(board);
	}

	private String[][] setBGS(String board[][]) {
		for (int ii = 0; ii < boardSize; ii++) {
			for (int jj = 0; jj < boardSize; jj++) {
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

						if (board[tx][ty].equals("N"))
							board[tx][ty] = "";
						if (!board[tx][ty].contains(cellCH))
							board[tx][ty] += cellCH;
					}
				}
			}
		}
		return board;
	}

	public void printBoard(String board[][]) {
		for (int i = 0; i < boardSize; i++)
			System.out.print("|-----");
		System.out.println("|");

		for (int i = 0; i < boardSize; i++) {
			System.out.print("|  ");
			for (int j = 0; j < boardSize; j++) {
				System.out.print(board[i][j]);
				if (j != boardSize - 1)
					System.out.print("  |  ");
			}
			System.out.println("  |  ");

			for (int k = 0; k < boardSize; k++)
				System.out.print("|-----");
			System.out.println("|");
		}

	}

	private void start() {
		int startI = 0;
		int startJ = 0;

		int I = startI;
		int J = startJ;

		int parentI = I;
		int parentJ = J;

		int it = 0;

		boolean hasRisk = false;
		
		boolean game = true;
		
		safePits[I][J] = "safe";

		while (true) {

			boolean isAnyWay = true;
			boolean negOfLhs;
			System.out.println(I + ", " + J);

			board = checkPitsPossibility(board);
			//
			if (board[I][J].equals("G"))
				break;

			// if (it == 29)
			// break;

			if (board[I][J].equals("N")) {
				negOfLhs = true;
			} else
				negOfLhs = false;

			for (int ii = 0; ii < 4; ii++) {

				int tx = I + fx[ii];
				int ty = J + fy[ii];
				boolean isValidCell = adjacentCellValid(tx, ty);
				if (isValidCell) {

					boolean entailPit = entail(I, J, true, negOfLhs, pits[tx][ty]);
					if (entailPit)
						safePits[tx][ty] = "safe";
					else {
						if (!safePits[tx][ty].equals("safe"))
							safePits[tx][ty] = "dk";
					}
					System.out.println("pit: "+tx + ", "+ ty +": "+safePits[tx][ty]);

				}
			}

			for (int ii = 0; ii < 4; ii++) {
				int tx = I + fx[ii];
				int ty = J + fy[ii];
				boolean isValidCell = adjacentCellValid(tx, ty);
				if (isValidCell && !visited[tx][ty] && safePits[tx][ty].equals("safe")) {

					parentI = I;
					parentJ = J;

					I = tx;
					J = ty;

					visited[I][J] = true;
					isAnyWay = true;
					break;
				} else if (isValidCell && !safePits[tx][ty].equals("safe")) {
					isAnyWay = false;
				}
			}
			if (I == parentI && J == parentJ) {
				break;
			}

			if (!isAnyWay) {
				I = parentI;
				J = parentJ;
			}

			it++;
		}
	}

	private String[][] checkPitsPossibility(String board[][]) {

		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (board[i][j].equals("B")) {
					int validAdjCell = 0;
					int safeAdjCell = 0;
					for (int ii = 0; ii < 4; ii++) {
						int tx = i + fx[ii];
						int ty = j + fy[ii];

						if (adjacentCellValid(tx, ty)) {
							validAdjCell++;
							if (safePits[tx][ty].equals("safe")) {
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
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				System.out.println(i + ", " + j + ": " + safePits[i][j]);
			}
		}
	}

	private boolean adjacentCellValid(int tx, int ty) {
		if (tx >= boardSize || tx < 0)
			return false;
		if (ty >= boardSize || ty < 0)
			return false;
		return true;
	}

	private boolean entail(int i, int j, boolean lhs, boolean negOfLhs, boolean alpha) {
		ArrayList<Boolean> sentences = new ArrayList<Boolean>();

		boolean x = !lhs;

		for (int ii = 0; ii < 4; ii++) {

			int tx = i + fx[ii];
			int ty = j + fy[ii];
			boolean isValidCell = adjacentCellValid(tx, ty);
			if (isValidCell) {

				x |= pits[tx][ty];
				sentences.add((!pits[tx][ty]) | lhs);
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
