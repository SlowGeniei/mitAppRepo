package ChessGame;

import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Knight extends Piece {
	static int[][] whiteTable = {
			{-50,-40,-30,-30,-30,-30,-40,-50},
			{-40,-20,  0,  0,  0,  0,-20,-40},
			{-30,  0, 10, 15, 15, 10,  0,-30},
			{-30,  5, 15, 20, 20, 15,  5,-30},
			{-30,  0, 15, 20, 20, 15,  0,-30},
			{-30,  5, 10, 15, 15, 10,  5,-30},
			{-40,-20,  0,  5,  5,  0,-20,-40},
			{-50,-40,-30,-30,-30,-30,-40,-50},
	};
	static int[][] blackTable = {
			{-50,-40,-30,-30,-30,-30,-40,-50},
			{-40,-20,  0,  5,  5,  0,-20,-40},
			{-30,  5, 10, 15, 15, 10,  5,-30},
			{-30,  0, 15, 20, 20, 15,  0,-30},
			{-30,  5, 15, 20, 20, 15,  5,-30},
			{-30,  0, 10, 15, 15, 10,  0,-30},
			{-40,-20,  0,  0,  0,  0,-20,-40},
			{-50,-40,-30,-30,-30,-30,-40,-50},
	};
	int value = 320;
	public Knight(Color color) {
		super (color, "N");
		if (color == Color.BLACK) {
			setPositionTable(blackTable);
			setIcon (new ImageIcon ("images/BN.png"));
		} else {
			setPositionTable(whiteTable);
			setIcon (new ImageIcon ("images/WN.png"));
		}
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public char getPieceType() {
		return 'N';
	}
	public boolean isLegal(RegularMove move, Board b) {
		return true;
	}
	public ArrayList<Move> getLegalMoves(Board b) {
		//System.out.println("Checking moves for " + (this.color == Color.WHITE ? "white" : "black") + "knight;");

		ArrayList<Move> legalMoves = new ArrayList<Move>();
		int row = this.getRow();
		int col = this.getColumn();
		int trow = this.getRow();
		int tcol = this.getColumn();
		int[][] I = {
				{-1,-2},
				{-2,-1},
				{-2,1},
				{-1,2},
				{1,2},
				{2,1},
				{2,-1},
				{1,-2}
		};
		for (int i = 0; i < 8; i++) {
			trow = row;
			tcol = col;
			trow += I[i][0];
			tcol += I[i][1];
			if (trow >= 0 && trow <= 7 && tcol >= 0 && tcol <= 7) {
				Piece piece = b.getSquare(trow, tcol).getPiece();
				if (piece != null && piece.getColor() == this.getColor()) {
					continue;
				}
				Move legalMove = new RegularMove(b.getSquare(row,col), b.getSquare(trow, tcol));
				legalMoves.add(legalMove);
			}
		}
		return legalMoves;
	}
}
