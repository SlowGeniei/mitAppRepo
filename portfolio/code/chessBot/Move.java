package ChessGame;

public abstract class Move {
	public abstract void apply(Board board, boolean finalMove);
	public abstract void undo (Board board, boolean finalMove);
	public abstract boolean isLegal(Board b);
	public abstract String toString();
	public abstract Square getFromSquare();
	public abstract Square getToSquare();
}
