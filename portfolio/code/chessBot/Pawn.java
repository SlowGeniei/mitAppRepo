package ChessGame;

import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Pawn extends Piece {
	static int[][] whiteTable = {
			{ 0,  0,  0,  0,  0,  0,  0,  0},
			{50, 50, 50, 50, 50, 50, 50, 50},
			{10, 10, 20, 30, 30, 20, 10, 10},
			{ 5,  5, 10, 25, 25, 10,  5,  5},
			{ 0,  0,  0, 20, 20,  0,  0,  0},
			{ 5, -5,-10,  0,  0,-10, -5,  5},
			{ 5, 10, 10,-20,-20, 10, 10,  5},
			{ 0,  0,  0,  0,  0,  0,  0,  0},
	};
	static int[][] blackTable = {
			{ 0,  0,  0,  0,  0,  0,  0,  0},
			{ 5, 10, 10,-20,-20, 10, 10,  5},
			{ 5, -5,-10,  0,  0,-10, -5,  5},
			{ 0,  0,  0, 20, 20,  0,  0,  0},
			{ 5,  5, 10, 25, 25, 10,  5,  5},
			{10, 10, 20, 30, 30, 20, 10, 10},
			{50, 50, 50, 50, 50, 50, 50, 50},
			{ 0,  0,  0,  0,  0,  0,  0,  0},
	};
	int value = 100;
	public Pawn (Color color) {
		super (color, "P");
		if (color == Color.BLACK) {
			setPositionTable(blackTable);
			setIcon (new ImageIcon ("images/BP.png"));
		} else {
			setPositionTable(whiteTable);
			setIcon (new ImageIcon ("images/WP.png"));
		}
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public char getPieceType() {
		return 'P';
	}
	public boolean isLegal(RegularMove move, Board b) {
		return true;
	}
	public ArrayList<Move> getLegalMoves(Board b) {
		//System.out.println("Checking moves for " + (this.color == Color.WHITE ? "white" : "black") + "pawn;");

		ArrayList<Move> legalMoves = new ArrayList<Move>();
		int row = this.getRow();
		int col = this.getColumn();
		int rinc = (this.color == Color.WHITE) ? -1 : 1;
		int trow = (this.color == Color.WHITE) ? 6 : 1;
		if (row + rinc >= 0 && row + rinc <= 7 && col - 1 >= 0 && col - 1 <= 7) {
			Piece piece = b.getSquare(row + rinc, col - 1).getPiece();
			if (piece != null && piece.getColor() != this.getColor()) {
				if ((row + rinc) != 0 && (row + rinc) != 7) {
					Move legalMove = new RegularMove(b.getSquare(row,col), b.getSquare(row + rinc, col - 1));
					legalMoves.add(legalMove);
				} else {
					this.generatePromotionMoves (legalMoves, b.getSquare(row, col), b.getSquare(row + rinc, col - 1));
				}
			}
		}
		if (row + rinc >= 0 && row + rinc <= 7 && col + 1 >= 0 && col + 1 <= 7) {
			Piece piece = b.getSquare(row + rinc, col + 1).getPiece();
			if (piece != null && piece.getColor() != this.getColor()) {
				if ((row + rinc) != 0 && (row + rinc) != 7) {
					Move legalMove = new RegularMove(b.getSquare(row,col), b.getSquare(row + rinc, col + 1));
					legalMoves.add(legalMove);
				} else {
					this.generatePromotionMoves (legalMoves, b.getSquare(row, col), b.getSquare(row + rinc, col + 1));
				}
			}
		}
		if (row + rinc >= 0 && row + rinc <= 7 && col >= 0 && col <= 7) {
			Piece piece = b.getSquare(row + rinc, col).getPiece();
			if (piece == null) {
				if ((row + rinc) != 0 && (row + rinc) != 7) {
					Move legalMove = new RegularMove(b.getSquare(row,col), b.getSquare(row + rinc, col));
					legalMoves.add(legalMove);
				} else {
					this.generatePromotionMoves (legalMoves, b.getSquare(row, col), b.getSquare(row + rinc, col));
				}
			}
		}
		if (row + 2*rinc >= 0 && row + 2*rinc <= 7 && col >= 0 && col <= 7) {
			if (row == trow && b.getSquare(row + rinc, col).getPiece() == null) {
				Piece piece = b.getSquare(row + 2*rinc, col).getPiece();
				if (piece == null) {
					if ((row + rinc) != 0 && (row + rinc) != 7) {
						Move legalMove = new RegularMove(b.getSquare(row,col), b.getSquare(row + 2*rinc, col));
						legalMoves.add(legalMove);
					} else {
						this.generatePromotionMoves (legalMoves, b.getSquare(row, col), b.getSquare(row + 2*rinc, col));
					}
				}
			}
		}
		return legalMoves;
	}
	
	private void generatePromotionMoves (ArrayList<Move> legalMoves, Square fromSquare, Square toSquare) {
		
		legalMoves.add(new PromotionMove (fromSquare, toSquare, new Rook(this.getColor())));
		legalMoves.add(new PromotionMove (fromSquare, toSquare, new Knight(this.getColor())));
		legalMoves.add(new PromotionMove (fromSquare, toSquare, new Bishop(this.getColor())));
		legalMoves.add(new PromotionMove (fromSquare, toSquare, new Queen(this.getColor())));
	}
}