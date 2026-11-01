package ChessGame;
import java.util.ArrayList;
import java.util.List;

public class Board {
	
	Square[][] board;
	boolean alreadyThinking;
	RegularMove bestMove;
	int bestScore;
	Square sourceSquare = null;
	List<BoardListener> listener = new ArrayList<BoardListener>();

	public Square getSourceSquare() {
		return sourceSquare;
	}
	public void setSourceSquare(Square sourceSquare) {
		this.sourceSquare = sourceSquare;
	}
	public int getScore() {
		return 0;
	}
	public void addListener(BoardListener s) {
		listener.add(s);
	}
	public void notifyListeners(Move move) {
		for (BoardListener smthn :  listener) {
			smthn.boardModified(move);
		}
	}
	public ArrayList<Move> getLegalMoves(int color) {
		//System.out.println("Started board.getLegalMoves");
		ArrayList<Move> legalMoves = new ArrayList<Move>();
		Color pieceColor = Color.WHITE;
		if (color == -1) {
			pieceColor = Color.BLACK;
		}
		for (int j = 0; j < 8; j++) {
			for (int i = 0; i < 8; i++) {
				Square square = board[j][i];
				Piece piece = square.getPiece();
				if (piece != null && piece.getColor() == pieceColor) {
					//System.out.println ("getting legal moves for " + piece.getName() + " at " + piece.getRow() + "," + piece.getColumn());
					ArrayList<Move> moves = piece.getLegalMoves(this);
					//System.out.println(moves.size() + " moves found");
					legalMoves.addAll(moves);
				}
			}
		}
		
		return legalMoves;
	}
	public int getValue() {
		int val = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				val += board[i][j].getValue();
			}
		}
		return val;
	}
	
	public int negaMax(int color, int depth) {
		if (depth == 0 || isTerminal()) {
			return this.getValue() * color;
		}
		int bestScore = -Integer.MIN_VALUE;
		ArrayList<Move> moves = this.getLegalMoves(color);
		for (Move move : moves) {
			move.apply(this, false);
			bestScore = Math.max(bestScore, -negaMax(-color, depth-1));
			move.undo(this, false);
		}
		return bestScore;
	}
	public Move think (int color, int depth) {
		float movePercentIncrease;
		float percentCalculated = 0;;
		System.out.println ("think: color=" + color + ", depth=" + depth);
		if (alreadyThinking)
		{
			System.out.println ("already thinking!");
			//System.out.println ("current best move=" + (bestMove == null ? "(null)" : bestMove.toString()) + "; score=" + bestScore);
			return null;
		}
		alreadyThinking = true;
		Move bestMove = null;
		int bestScore = Integer.MIN_VALUE;
		
		ArrayList<Move> moves = this.getLegalMoves(color);
		movePercentIncrease = (float)(100.0 / moves.size());
		for (Move move : moves) {
			move.apply(this, false);
			int score = -negaMax(-color, depth);
			move.undo(this, false);
			percentCalculated += movePercentIncrease;
			System.out.println("\t" + (int)percentCalculated + "% finished");
			if (score > bestScore) {
				bestMove = move;
				bestScore = score;
			}
		}
		//System.out.println ("Best move: " + bestMove.toString() + "; score: " + bestScore);
		alreadyThinking = false;
		return bestMove;
	}
	public boolean isTerminal() {
		return false;
	}
	public Board(int[] whitePlayerKingPosition, int[] blackPlayerKingPosition) {
		board = new Square[8][8];
		initialise(whitePlayerKingPosition, blackPlayerKingPosition);
	}
	public Square getSquare(int row, int col) {
		return board[row][col];
	}
	public void initialise(int[] whitePlayerKingPosition, int[] blackPlayerKingPosition) {
		Square whitePlayerKingSquare = null;
		Square blackPlayerKingSquare = null;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Square tempSquare = new Square(i, j);
				if (i == whitePlayerKingPosition[0] && j == whitePlayerKingPosition[1]) {
					whitePlayerKingSquare = tempSquare;
				}
				if (i == blackPlayerKingPosition[0] && j == blackPlayerKingPosition[1]) {
					blackPlayerKingSquare = tempSquare;
				}
				board[i][j] = tempSquare;
			}
		}
		setUpGame(whitePlayerKingSquare, blackPlayerKingSquare);
		alreadyThinking = false;
		bestMove = null;
		bestScore = Integer.MIN_VALUE;
		
		//printBoard();
	}
	public void printBoard() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				System.out.println (board[i][j].toString());
			}
		}
	}
	public int getColorValue(Color color) {
		//System.out.println("Called");
		int i, j;
		int colorValue = 0;
		for (i = 0; i < 8; i++) {
			for (j = 0; j < 8; j++) {
				
				if (board[i][j].getPiece() != null) {
					//System.out.println("Not Null");
					if (board[i][j].getPiece().getColor() == color) {
						//System.out.println("Right Color");
						colorValue += board[i][j].getPiece().squareValue();
					}
				}
			}
		}
		return colorValue;
	}
	

	
	public void setUpGame(Square whitePlayerKingSquare, Square blackPlayerKingSquare) {
		
		
		board[7][0].setPiece(new Rook (Color.WHITE), true);
		board[7][1].setPiece(new Knight (Color.WHITE), true);
		board[7][2].setPiece(new Bishop (Color.WHITE), true);
		board[7][3].setPiece(new Queen (Color.WHITE), true);
		whitePlayerKingSquare.setPiece(new King (Color.WHITE), true);
		board[7][5].setPiece(new Bishop (Color.WHITE), true);
		board[7][6].setPiece(new Knight (Color.WHITE), true);
		board[7][7].setPiece(new Rook (Color.WHITE), true);
		int i;
		for (i = 0; i <= 7; i++) {
			board[6][i].setPiece(new Pawn (Color.WHITE), true);
		}
		board[0][0].setPiece(new Rook (Color.BLACK), true);
		board[0][1].setPiece(new Knight (Color.BLACK), true);
		board[0][2].setPiece(new Bishop (Color.BLACK), true);
		board[0][3].setPiece(new Queen (Color.BLACK), true);
		blackPlayerKingSquare.setPiece(new King (Color.BLACK), true);
		board[1][2].setPiece(new Queen (Color.BLACK), true);
		board[0][5].setPiece(new Bishop (Color.BLACK), true);
		board[0][6].setPiece(new Knight (Color.BLACK), true);
		board[0][7].setPiece(new Rook (Color.BLACK), true);
		for (i = 0; i <= 7; i++) {
			board[1][i].setPiece(new Pawn (Color.BLACK), true);
		}
		
		//board[1][0].setPiece(new Pawn (Color.WHITE), true);
	}
}