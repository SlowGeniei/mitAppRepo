package ChessGame;

import java.util.ArrayList;

import javax.swing.ImageIcon;

public class King extends Piece {
	static int[][] whiteTable = {
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-20,-30,-30,-40,-40,-30,-30,-20},
			{-10,-20,-20,-20,-20,-20,-20,-10},
			{ 20, 20,  0,  0,  0,  0, 20, 20},
			{ 20, 30, 10,  0,  0, 10, 30, 20},
	};
	static int[][] blackTable = {
			{ 20, 30, 10,  0,  0, 10, 30, 20},
			{ 20, 20,  0,  0,  0,  0, 20, 20},
			{-10,-20,-20,-20,-20,-20,-20,-10},
			{-20,-30,-30,-40,-40,-30,-30,-20},
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-30,-40,-40,-50,-50,-40,-40,-30},
	};
	int value = 20000;	
	public King (Color color) {
		super (color, "K");
		if (color == Color.BLACK) {
			setPositionTable(blackTable);
			setIcon (new ImageIcon ("images/BK.png"));
		} else {
			setPositionTable(whiteTable);
			setIcon (new ImageIcon ("images/WK.png"));
		}
	}

	@Override
	public int getValue() {
		return value;
	}
	@Override
	public char getPieceType() {
		return 'K';
	}
	public boolean isLegal(RegularMove move, Board b) {
		return true;
	}
	public ArrayList<Move> getLegalMoves(Board b) {
		//System.out.println("Checking moves for " + (this.color == Color.WHITE ? "white" : "black") + "king;");

		ArrayList<Move> legalMoves = new ArrayList<Move>();
		int row = this.getRow();
		int col = this.getColumn();
		int trow = this.getRow();
		int tcol = this.getColumn();
		int[][] I = {
				{-1, 0},
				{-1, 1},
				{0,  1},
				{1,  1},
				{1,  0},
				{1, -1},
				{0, -1},
				{-1,-1}
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
				if (!b.getSquare(trow, tcol).isThreatened(color, b, false, legalMove)) {
					legalMoves.add(legalMove);
				}
				if (piece != null) {
					continue;
				}
			}
		}
		if (this.getNumMoves() == 0) {
			Move castle1 = new Castle(b, true, this.getColor() == Color.WHITE ? 1 : -1);
			if (castle1.isLegal(b)) legalMoves.add(castle1);
			
			Move castle2 = new Castle(b, false, this.getColor() == Color.WHITE ? 1 : -1);
			if (castle2.isLegal(b)) legalMoves.add(castle2);
		}
		return legalMoves;
	}
}
//Galen is an amazing person who has never ever done anything wrong
//Grandpa will never find this
//This was made on 4/16/2024 at 6:39