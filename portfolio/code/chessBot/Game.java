package ChessGame;

import java.awt.event.*;
import java.util.concurrent.ArrayBlockingQueue;
/*
 * Give move board Panel TODO:
 */
public class Game {
	int turn;
	Board board;
	Thread thread;
	Player whitePlayer;
	Player blackPlayer;
	Player currentPlayer;
	BoardPanel boardPanel;
	private static Game theGame;
    private ArrayBlockingQueue<BlockingQueuePackage> queue;
    
	public Game (Board board, Player whitePlayer, Player blackPlayer, BoardPanel boardPanel) {
		turn = 1;
		this.board = board;
		this.whitePlayer = whitePlayer;
		this.blackPlayer = blackPlayer;
		this.boardPanel = boardPanel;
		currentPlayer = this.whitePlayer;
		queue = new ArrayBlockingQueue<BlockingQueuePackage>(1);
		theGame = this;
	}
	
	public static Game getGame() {
		return theGame;
	}
	
	public boolean addToQueue(BlockingQueuePackage bqp) {
		return queue.offer(bqp);
	}
	
	public void play(Square whitePlayerKingSquare, Square blackPlayerKingSquare) {
		//board.setUpGame();	Already called in Board.initialise which is called in Board's constructor
		whitePlayer.setKingPos(whitePlayerKingSquare);
		blackPlayer.setKingPos(blackPlayerKingSquare);
		currentPlayer = whitePlayer;
		boardPanel.setGame(this);
		thread = new Thread(new Runnable() {
			public void run() {
				try {
					while(true) {
						BlockingQueuePackage bqp = queue.take();
						bqp.run();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		whitePlayer.startMove(board, this);
	}
	
	public Player getPlayerByColor(Color color) {
		return color == Color.WHITE ? whitePlayer : blackPlayer;
	}
	
	public Boolean processMove(Move move) {
		if (move.isLegal(board)) {
			move.apply(board, true);
			currentPlayer = currentPlayer == whitePlayer ? blackPlayer : whitePlayer;
			currentPlayer.startMove(board, this);
			return true;
		}
		return false;
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public void moveWhite() {
		whitePlayer.makeMove();
		turn = -turn;
	}
	
	public void moveBlack () {
		blackPlayer.makeMove();
		turn = -turn;
	}
}
