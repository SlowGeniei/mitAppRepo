package ChessGame;

public class ComputerPlayer extends Player {
	
	int depth;
	public ComputerPlayer (Color side, int depth) {
		super (side);
		this.depth = depth;
	}
	@Override
	public void startMove(Board board, Game game) {
		// TODO Auto-generated method stub
		BlockingQueuePackage bqp = new BlockingQueuePackage(board, game, this);
		game.addToQueue(bqp);
	}
	
	@Override
	public boolean mouseClicked(Square square, Board board, Game game) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public RegularMove makeMove() {
		// TODO Auto-generated method stub
		return null;
	}
}
