package ChessGame;

import java.util.ArrayList;
import java.util.List;

public class Square {
	int row;
	int column;
	Piece piece;
	
	List<SquareListener> listeners = new ArrayList<SquareListener>();
	
	public void removeListener (SquareListener s) {
		if (listeners.contains(s)) {listeners.remove(s);}
	}
	
	public void addListener(SquareListener s) {
		listeners.add(s);
	}
	
	public void notifyListeners() {
		for (SquareListener smthn :  listeners) {
			smthn.squareModified();
		}
	}

	public Square (int i, int j) {
		row = i;
		column = j;
	}
	public String toString() {
		String result = (char)(getColumn() + 0x61) + String.valueOf(8 - getRow());
		return result;
	}
	public int getValue() {
		return piece == null ? 0 : piece.squareValue();
	}
	public Piece getPiece() {
		return piece;
	}
	public void setPiece(Piece newPiece, boolean finalMove) {
		if (piece != null && newPiece == null) {
			piece.death();
		}
		if (newPiece != null) {
			newPiece.setSquare(this);
		}
		piece = newPiece;
		if (finalMove) notifyListeners();
	}
	public int getRow() {
		return row;
	}
	public int getColumn() {
		return column;
	}
	public final boolean isThreatened(Color threatenedColor, Board board, Boolean debug, Move move) {
		boolean moved = false;
		if (move != null) {
			move.apply(board, false);
			moved = true;
		}
		int[] rowDirections = {-1, -1, -1, 0, 0, 1, 1, 1};
		int[] colDirections = {-1,  0,  1,-1, 1,-1, 0, 1};
		int[][] knightDirections = {
				{-2, -1},
				{-2,  1},
				{-1,  2},
				{1,   2},
				{2,   1},
				{2,  -1},
				{1,  -2},
				{-1, -2}
		};
		
		boolean bishopThreats[] = {true, false, true, false, false, true, false, true};
		boolean rookThreats[] = {false, true, false, true, true, false, true, false};
		boolean queenThreats[] = {true, true, true, true, true, true, true, true, true};
		boolean kingThreats[] = {true, true, true, true, true, true, true, true, true};
		
		boolean kill = threatenedColor.equals(Color.WHITE) ? true: false;
		boolean pawnThreats[] = {kill, false, kill, false, false, !kill, false, !kill};
		
		boolean threatDetected = false;
		int threatenedRow = this.getRow();
		int threatenedCol = this.getColumn();
		
		for (int direction = 0; direction < 8 && !threatDetected; direction++) {
			int row = threatenedRow;
			int col = threatenedCol;
			int krow = threatenedRow;
			int kcol = threatenedCol;
			int rowIncrement = rowDirections[direction];
			int colIncrement = colDirections[direction];
			
			 if (debug) { 
				switch (direction) {
				case 0:
					System.out.println("\tChecking NW");
					break;
				case 1:
					System.out.println("\tChecking N");
					break;
				case 2:
					System.out.println("\tChecking NE");
					break;
				case 3:
					System.out.println("\tChecking W");
					break;
				case 4:
					System.out.println("\tChecking E");
					break;
				case 5:
					System.out.println("\tChecking SW");
					break;
				case 6:
					System.out.println("\tChecking S");
					break;
				case 7:
					System.out.println("\tChecking SE");
					break;
				default:
					System.out.println("\tBROKE!");
					break;
				}
			}
			krow += knightDirections[direction][0];
			kcol += knightDirections[direction][1];
			if (krow >= 0 && krow <= 7 && kcol >= 0 && kcol <= 7) {
				if (debug) System.out.println("\tChecking for knight at row:" + krow + " col:" + kcol);
				Piece piece = board.getSquare(krow, kcol).getPiece();
				if (piece != null && piece instanceof Knight && !piece.getColor().equals(threatenedColor)) {
					threatDetected = true;
				}
			}
			
			for (int step = 0; step < 8; step++) {
				row += rowIncrement;
				col += colIncrement;
				
				if (row < 0 || row > 7 || col < 0 || col > 7) {
					if (debug) System.out.println("\t\tOutside of board");
					break;
				} else {
					if (debug) System.out.println("\t\tOn board and checking for pice at row:" + row + " col:" + col);
					Square square = board.getSquare(row, col);
					Piece piece = square.getPiece();
					
					if (piece != null) {
						if(!piece.getColor().equals(threatenedColor)) {
							if (debug) System.out.println("\t\tEnemy piece found at row:" + row + " col:" + col);
							if (piece instanceof Bishop && bishopThreats[direction]) {
								if (debug) System.out.println("\t\t\tSquare row:" + threatenedRow + " col:" + threatenedCol + " is being attacked by bishop at row:" + row + " col:" + col);
								threatDetected = true;
							} else if (piece instanceof Rook && rookThreats[direction]) {
								if (debug) System.out.println("\t\t\tSquare row:" + threatenedRow + " col:" + threatenedCol + " is being attacked by rook at row:" + row + " col:" + col);
								threatDetected = true;
							} else if (piece instanceof Queen && queenThreats[direction]) {
								if (debug) System.out.println("\t\t\tSquare row:" + threatenedRow + " col:" + threatenedCol + " is being attacked by queen at row:" + row + " col:" + col);
								threatDetected = true;
							} else if (step == 0) {
								if (piece instanceof Pawn && pawnThreats[direction]) {
									if (debug) System.out.println("\t\t\tSquare row:" + threatenedRow + " col:" + threatenedCol + " is being attacked by pawn at row:" + row + " col:" + col);
									threatDetected = true;
								}
								if (piece instanceof King && kingThreats[direction]) {
									if (debug) System.out.println("\t\t\tSquare row:" + threatenedRow + " col:" + threatenedCol + " is being attacked by king at row:" + row + " col:" + col);
									threatDetected = true;
								}
							} else {
								break;
							}
						} else {
							break;
						}
					}
				}
			}
		}
		if (moved) move.undo(board, false);
		return threatDetected;
	}
}
