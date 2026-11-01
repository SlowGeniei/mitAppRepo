package ChessGame;

public abstract class Player {
	Color side;
	Square kingPos;
	
	protected Player (Color side) {
		this.side = side;
	}
	
	public Square getKingPos() {
		return kingPos;
	}
	public void setKingPos(Square kingPos) {
		this.kingPos = kingPos;
	}
	public abstract void startMove(Board board, Game game);
	public abstract boolean mouseClicked(Square square, Board board, Game game);
	public abstract RegularMove makeMove();
}