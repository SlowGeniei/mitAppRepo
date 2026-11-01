package ChessGame;

import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Rook extends Piece {
	static int[][] whiteTable = {
			{  0,  0,  0,  0,  0,  0,  0,  0},
			{  5, 10, 10, 10, 10, 10, 10,  5},
			{ -5,  0,  0,  0,  0,  0,  0, -5},
			{ -5,  0,  0,  0,  0,  0,  0, -5},
			{ -5,  0,  0,  0,  0,  0,  0, -5},
			{ -5,  0,  0,  0,  0,  0,  0, -5},
			{ -5,  0,  0,  0,  0,  0,  0, -5},
			{  0,  0,  0,  5,  5,  0,  0,  0},
	};
	static int[][] blackTable = {
			{  0,  0,  0,  5,  5,  0,  0,  0},
			{ -5,  0,  0,  0,  0,  0,  0, -5},
			{ -5,  0,  0,  0,  0,  0,  0, -5},
			{ -5,  0,  0,  0,  0,  0,  0, -5},
			{ -5,  0,  0,  0,  0,  0,  0, -5},
			{ -5,  0,  0,  0,  0,  0,  0, -5},
			{  5, 10, 10, 10, 10, 10, 10,  5},
			{  0,  0,  0,  0,  0,  0,  0,  0},
	};
	int value = 500;
	Boolean moved = false;
	
	public Rook(Color color) {
		super (color, "R");
		if (color == Color.BLACK) {
			setPositionTable(blackTable);
			setIcon (new ImageIcon ("images/BR.png"));
		} else {
			setPositionTable(whiteTable);
			setIcon (new ImageIcon ("images/WR.png"));
		}
	}
	
	@Override
	public int getValue() {
		return value;
	}

	@Override
	public char getPieceType() {
		return 'R';
	}
	public boolean isLegal(RegularMove move, Board b) {
		return true;
	}
	public ArrayList<Move> getLegalMoves(Board b) {
		//System.out.println("Checking moves for " + (this.color == Color.WHITE ? "white" : "black") + "rook;");

		ArrayList<Move> legalMoves = new ArrayList<Move>();
		int row = this.getRow();
		int col = this.getColumn();
		int trow = this.getRow();
		int tcol = this.getColumn();
		int[][] I = {
				{-1,0},
				{0,1},
				{1,0},
				{0,-1}
		};
		for (int i = 0; i < 4; i++) {
			trow = row;
			tcol = col;
			for (;;) {
				trow += I[i][0];
				tcol += I[i][1];
				if (trow >= 0 && trow <= 7 && tcol >= 0 && tcol <= 7) {
					// System.out.println("Checking move: " + row + "," + col + "-" + trow + "," + tcol);
					Piece piece = b.getSquare(trow, tcol).getPiece();
					if (piece != null && piece.getColor() == this.getColor()) {
						break;
					}
					// System.out.println ("adding " + (this.color == Color.WHITE ? "W" : "B")+ "R from " + row + "," + col + " to " + trow + "," + tcol );
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
