package gamePackage;

public class GameSettings {
	// game properties
	public static String title = "The Wumpus World";
	public static int height = 1200;
	public static int width = 1500;

	// location on monitor
	public static int loacationX = 100;
	public static int locationY = 50;

	public static int BOARD_ROW = 10;
	public static int BOARD_COL = 10;

	public static int sourceI = BOARD_ROW - 1;
	public static int sourceJ = 0;

	public static int goldI = BOARD_ROW / 2 - 1;
	public static int goldJ = BOARD_COL / 2 + 1;

	public static int arrowNo = 1;
}
