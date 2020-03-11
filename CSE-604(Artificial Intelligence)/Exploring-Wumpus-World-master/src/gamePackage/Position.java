package gamePackage;

public class Position {
	public int row;
	public int col;
	
	public Position parent;
	public Position child;
	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}
}
