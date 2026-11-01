package ChessGame;

public class BlockingQueuePackage implements Runnable{

	Board board;
	int depth;
	Game game;
	Player player;
	public BlockingQueuePackage (Board board, Game game, Player player) {
		this.board = board;
		this.game = game;
		this.player = player;
		this.depth = ((ComputerPlayer)player).depth;
	}
	@Override
	public void run() {
		try {
			Move move = board.think(player.side == Color.WHITE ? 1 : -1, 2 * depth - 1);
			if (move != null) {
				game.processMove(move);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}