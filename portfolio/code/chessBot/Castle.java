package ChessGame;

import java.math.*;

public class Castle extends Move {
	
	Square kingFromSquare;
	Square kingToSquare;
	Square rookFromSquare;
	Square rookToSquare;
	
	Boolean kingSide;
	int color;
	Board board;
	int[]  kingPos;
	Piece king;
	Piece rook;
	public Castle (Board board, Boolean kingSide, int color) {
		this.kingSide = kingSide;
		this.color = color;
		this.board = board;
		//if (color == -1) {
			
			kingFromSquare  = board.getSquare(color == -1 ? 0 : 7, 4);
			kingToSquare = (kingSide ? board.getSquare(color == -1 ? 0 : 7, 6) : board.getSquare(color == -1 ? 0 : 7,  2));
			
			rookFromSquare = board.getSquare(color == -1 ? 0 : 7, kingSide ? 7 : 0);
			rookToSquare = board.getSquare(color == -1 ? 0 : 7, kingSide ? 5 : 3);

			
/*			kingFromSquare  = board.getSquare(0, 4);
			kingToSquare = (kingSide ? board.getSquare(0, 6) : board.getSquare(0,  2));
			
			rookFromSquare = board.getSquare(0, kingSide ? 7 : 0);
			rookToSquare = board.getSquare(0, kingSide ? 5 : 3);
			
			//kingMove = new RegularMove (board.getSquare(0, 4), (kingSide ? board.getSquare(0, 6) : board.getSquare(0,  2)));
			//rookMove = new RegularMove (board.getSquare(0, kingSide ? 7 : 0), board.getSquare(0, kingSide ? 5 : 3));
		} else {
			
			kingFromSquare = board.getSquare(7, 4);
			kingToSquare = (kingSide ? board.getSquare(7, 6) : board.getSquare(7,  2));
			
			rookFromSquare = board.getSquare(7, kingSide ? 7 : 0);
			rookToSquare = board.getSquare(7, kingSide ? 5 : 3);
			
			//kingMove = new RegularMove (board.getSquare(7, 4), (kingSide ? board.getSquare(7, 6) : board.getSquare(7,  2)));
			//rookMove = new RegularMove (board.getSquare(7, kingSide ? 7 : 0), board.getSquare(7, kingSide ? 5 : 3));
		}*/
	}

	static boolean isCastleAttempt (Square fromSquare, Square toSquare) {
		Piece king = fromSquare.getPiece();
		if (king instanceof King) {
			if (Math.abs(fromSquare.getColumn() - toSquare.getColumn()) == 2) {
				return true;
			}
		}
		return false;
	}
	
	public void apply(Board board, boolean updateBoard) {
		
		this.applySingleMove(board, kingFromSquare, kingToSquare, updateBoard);
		this.applySingleMove(board, rookFromSquare, rookToSquare, updateBoard);
		if(updateBoard) board.notifyListeners(this);

/*		kingMove.getFromSquare().getPiece().changeNumMoves(1, updateBoard);
		rookMove.getFromSquare().getPiece().changeNumMoves(1, updateBoard);
		kingMove.apply(board, updateBoard);
		rookMove.apply(board, updateBoard);*/
		
	}
	public void undo(Board board, boolean updateBoard) {
		
		this.undoSingleMove(board, kingFromSquare, kingToSquare, updateBoard);
		this.undoSingleMove(board, rookFromSquare, rookToSquare, updateBoard);
		if(updateBoard) board.notifyListeners(this);
		
/*		kingMove.undo(board, updateBoard);
		rookMove.undo(board, updateBoard);
		kingMove.getFromSquare().getPiece().changeNumMoves(-1, updateBoard);
		rookMove.getFromSquare().getPiece().changeNumMoves(-1, updateBoard);*/
	}
	public String toString() {
		return kingSide ? "O-O" : "O-O-O";
	}
	public boolean isLegal(Board b) {
		king = kingFromSquare.getPiece();
		int kingRow = kingFromSquare.getRow();
		int kingCol = kingFromSquare.getColumn();
		
		if (king != null && king.numMoves == 0 && king instanceof King) {
			if (kingSide) {
				Square checkSquare1 = board.getSquare(kingRow, kingCol + 1);
				Square checkSquare2 = board.getSquare(kingRow, kingCol + 2);
				if (checkSquare1.getPiece() == null && checkSquare2.getPiece() == null) {
					if (!checkSquare1.isThreatened(king.getColor(), board, false, null) && !checkSquare2.isThreatened(king.getColor(), board, false, null)) {
						rook = board.getSquare(kingRow, kingCol + 3).getPiece();
						if (rook != null && rook instanceof Rook && rook.numMoves == 0) {
							return true;
						}
					}
				}
			} else {
				Square checkSquare1 = board.getSquare(kingRow, kingCol - 1);
				Square checkSquare2 = board.getSquare(kingRow, kingCol - 2);
				Square checkSquare3 = board.getSquare(kingRow, kingCol - 3);
				if (checkSquare1.getPiece() == null && checkSquare2.getPiece() == null && checkSquare3.getPiece() == null) {
					if (!checkSquare1.isThreatened(king.getColor(), board, false, null) && !checkSquare2.isThreatened(king.getColor(), board, false, null) && !checkSquare3.isThreatened(king.getColor(), board, false, null)) {
						rook = board.getSquare(kingRow, kingCol - 4).getPiece();
						if (rook != null && rook instanceof Rook && rook.numMoves == 0) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public void applySingleMove(Board b, Square fromSquare, Square toSquare, boolean isFinalMove) {
		
		if (isFinalMove) this.toString();
		
		fromSquare.getPiece().changeNumMoves(1, isFinalMove);
		
		if (isFinalMove) System.out.println("APPLY: Piece " + fromSquare.getPiece().getName() + " has moved " + fromSquare.getPiece().getNumMoves() + " times!");
		
		if (fromSquare.getPiece() instanceof King) {
			Game.getGame().getPlayerByColor(fromSquare.getPiece().getColor()).setKingPos(toSquare);
		}
		
		toSquare.setPiece(fromSquare.getPiece(), isFinalMove);
		fromSquare.setPiece(null, isFinalMove);
	}

	public void undoSingleMove(Board b, Square fSquare, Square tSquare, boolean finalMove) {
		
		Square toSquare = fSquare;
		Square fromSquare = tSquare;
		
		fromSquare.getPiece().changeNumMoves(-1, finalMove);
		if (finalMove) System.out.println("UNDO: Piece " + fromSquare.getPiece().getName() + " has moved " + fromSquare.getPiece().getNumMoves() + " times!");
		
		if (fromSquare.getPiece() instanceof King) {
			Game.getGame().getPlayerByColor(fromSquare.getPiece().getColor()).setKingPos(toSquare);
		}
		
		toSquare.setPiece (fromSquare.getPiece(), finalMove);
		fromSquare.setPiece (null, finalMove);
	}
	
	public Square getKingFromSquare() {
		return kingFromSquare;
	}

	public Square getKingToSquare() {
		return kingToSquare;
	}

	public Square getRookFromSquare() {
		return rookFromSquare;
	}

	public Square getRookToSquare() {
		return rookToSquare;
	}

	@Override
	public Square getFromSquare() {
		// TODO Auto-generated method stub
		return kingFromSquare;
	}

	@Override
	public Square getToSquare() {
		// TODO Auto-generated method stub
		return kingToSquare;
	}
	
}