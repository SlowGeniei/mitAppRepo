package ChessGame;

import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Queen extends Piece {
	static int[][] whiteTable = {
			{-20,-10,-10, -5, -5,-10,-10,-20},
			{-10,  0,  0,  0,  0,  0,  0,-10},
			{-10,  0,  5,  5,  5,  5,  0,-10},
			{ -5,  0,  5,  5,  5,  5,  0, -5},
			{ -5,  0,  5,  5,  5,  5,  0, -5},
			{-10,  5,  5,  5,  5,  5,  0,-10},
			{-10,  0,  5,  0,  0,  0,  0,-10},
			{-20,-10,-10, -5, -5,-10,-10,-20},
	};
	static int[][] blackTable = {
			{-20,-10,-10, -5, -5,-10,-10,-20},
			{-10,  0,  5,  0,  0,  0,  0,-10},
			{-10,  5,  5,  5,  5,  5,  0,-10},
			{ -5,  0,  5,  5,  5,  5,  0, -5},
			{ -5,  0,  5,  5,  5,  5,  0, -5},
			{-10,  0,  5,  5,  5,  5,  0,-10},
			{-10,  0,  0,  0,  0,  0,  0,-10},
			{-20,-10,-10, -5, -5,-10,-10,-20},
	};
	int value = 900;
	public Queen (Color color) {
		super (color, "Q");
		if (color == Color.BLACK) {
			setPositionTable(blackTable);
			setIcon (new ImageIcon ("images/BQ.png"));
		} else {
			setPositionTable(whiteTable);
			setIcon (new ImageIcon ("images/WQ.png"));
		}
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public char getPieceType() {
		return 'Q';
	}
	public boolean isLegal(RegularMove move, Board b) {
		return true;
	}
	public ArrayList<Move> getLegalMoves(Board b) {
		//System.out.println("Checking moves for " + (this.color == Color.WHITE ? "white" : "black") + "queen;");

		ArrayList<Move> legalMoves = new ArrayList<Move>();
		int row = this.getRow();
		int col = this.getColumn();
		int trow = this.getRow();
		int tcol = this.getColumn();
		int[][] I = {
				{-1,0},
				{-1,1},
				{0,1},
				{1,1},
				{1,0},
				{1,-1},
				{0,-1},
				{-1,-1}
		};
		for (int i = 0; i < 8; i++) {
			trow = row;
			tcol = col;
			for (;;) {
				trow += I[i][0];
				tcol += I[i][1];
				if (trow >= 0 && trow <= 7 && tcol >= 0 && tcol <= 7) {
					Piece piece = b.getSquare(trow, tcol).getPiece();
					if (piece != null && piece.getColor() == this.getColor()) {
						break;
					}
					Move legalMove = new RegularMove(b.getSquare(row,col), b.getSquare(trow, tcol));
					legalMoves.add(legalMove);
					if (piece != null) {
						break;
					}
				} else {
					break;
				}
			}
		}
		return legalMoves;
	}
}