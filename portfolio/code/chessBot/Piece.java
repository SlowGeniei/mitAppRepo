package ChessGame;
import java.util.ArrayList;

import javax.swing.Icon;

public abstract class Piece {
	Color color;
	String name;
	Square square;
	int[][] table;
	Icon image;
	int numMoves;
	public Piece (Color color, String name) {
		this.color = color;
		this.name = name;
		square = null;
		image = null;
		numMoves = 0;
	}
	
	public int getNumMoves() {
		return numMoves;
	}
	public void setNumMoves(int numMoves) {
		this.numMoves = numMoves;
	}
	public void changeNumMoves(int change, boolean finalMove) {
		numMoves += change;
		if (finalMove) System.out.println("Added " + change + " to numMoves which is now " + numMoves + " at piece " + name);
	}
	public void setIcon (Icon image) {
		this.image = image;
	}
	public Icon getIcon () {
		return image;
	}
	public void setSquare (Square square) {
		this.square = square;
		row = square.getRow();
		column = square.getColumn();
	}
	public final Square getSquare () {
		return square;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public int getRow() {
		return row;
	}
	public int getColumn() {
		return column;
	}
	public void death() {
		alive = false;
	}
	public void revive() {
		alive = true;
	}

	public String getName() {
		return name;
	}
	public void setPositionTable(int[][] table) {
		this.table = table;
		//System.out.println("\tSet Table" + table);
	}
	public int squareValue() {
		int value;
		value = getValue() + table[getSquare().row][getSquare().column];
		//System.out.println(getPieceType() + "=RealValue: " + getValue() + ", PosValue: " + value + "; Table: " + table);
		return (color == Color.WHITE) ? value : -value;
	}
	public boolean isLegal(Move move, Board b) {
		ArrayList<Move> moves = move.getFromSquare().getPiece().getLegalMoves(b);
		for (Move candidateMove : moves) {
			if (candidateMove.getToSquare().equals(move.getToSquare())) {
				return true;
			}
		}
		return false;
	}

	
	public abstract ArrayList<Move> getLegalMoves(Board b);
	public abstract int getValue();
	public abstract char getPieceType();
	int row;
	int column;
	boolean alive;
	static int[][] kingPositionValues = {
			{-50,-40,-30,-20,-20,-30,-40,-50},
			{-30,-20,-10,  0,  0,-10,-20,-30},
			{-30,-10, 20, 30, 30, 20,-10,-30},
			{-30,-10, 30, 40, 40, 30,-10,-30},
			{-30,-10, 30, 40, 40, 30,-10,-30},
			{-30,-10, 20, 30, 30, 20,-10,-30},
			{-30,-30,  0,  0,  0,  0,-30,-30},
			{-50,-30,-30,-30,-30,-30,-30,-50},
	};
	static int[][] bishopPositionValues = {
			{-20,-10,-10,-10,-10,-10,-10,-20},
			{-10,  0,  0,  0,  0,  0,  0,-10},
			{-10,  0,  5, 10, 10,  5,  0,-10},
			{-10,  5,  5, 10, 10,  5,  5,-10},
			{-10,  0, 10, 10, 10, 10,  0,-10},
			{-10, 10, 10, 10, 10, 10, 10,-10},
			{-10,  5,  0,  0,  0,  0,  5,-10},
			{-20,-10,-10,-10,-10,-10,-10,-20},

	};
	static int[][] knightPositionValues = {
			{-50,-40,-30,-30,-30,-30,-40,-50},
			{-40,-20,  0,  0,  0,  0,-20,-40},
			{-30,  0, 10, 15, 15, 10,  0,-30},
			{-30,  5, 15, 20, 20, 15,  5,-30},
			{-30,  0, 15, 20, 20, 15,  0,-30},
			{-30,  5, 10, 15, 15, 10,  5,-30},
			{-40,-20,  0,  5,  5,  0,-20,-40},
			{-50,-40,-30,-30,-30,-30,-40,-50},
	};
	static int[][] pawnPositionValues = {
			{ 0,  0,  0,  0,  0,  0,  0,  0},
			{50, 50, 50, 50, 50, 50, 50, 50},
			{10, 10, 20, 30, 30, 20, 10, 10},
			{ 5,  5, 10, 25, 25, 10,  5,  5},
			{ 0,  0,  0, 20, 20,  0,  0,  0},
			{ 5, -5,-10,  0,  0,-10, -5,  5},
			{ 5, 10, 10,-20,-20, 10, 10,  5},
			{ 0,  0,  0,  0,  0,  0,  0,  0},
	};
	static int[][] queenPositionValues = {
			{-20,-10,-10, -5, -5,-10,-10,-20},
			{-10,  0,  0,  0,  0,  0,  0,-10},
			{-10,  0,  5,  5,  5,  5,  0,-10},
			{ -5,  0,  5,  5,  5,  5,  0, -5},
			{  0,  0,  5,  5,  5,  5,  0, -5},
			{-10,  5,  5,  5,  5,  5,  0,-10},
			{-10,  0,  5,  0,  0,  0,  0,-10},
			{-20,-10,-10, -5, -5,-10,-10,-20},
	};
	static int[][] rookPositionValues = {
			{  0,  0,  0,  0,  0,  0,  0,  0},
			{  5, 10, 10, 10, 10, 10, 10,  5},
			{ -5,  0,  0,  0,  0,  0,  0, -5},
			{ -5,  0,  0,  0,  0,  0,  0, -5},
			{ -5,  0,  0,  0,  0,  0,  0, -5},
			{ -5,  0,  0,  0,  0,  0,  0, -5},
			{ -5,  0,  0,  0,  0,  0,  0, -5},
			{  0,  0,  0,  5,  5,  0,  0,  0},
	};
}