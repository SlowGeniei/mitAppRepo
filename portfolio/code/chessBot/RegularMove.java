package ChessGame;

import java.util.ArrayList;

public class RegularMove extends Move {
	Square from;
	Square to;
	
	String moveToString = null;
	Piece capturedPiece;
	Piece movedPiece;
	
	public RegularMove(Square from, Square to) {
		this.from = from;
		this.to = to;
	}
	public String toString() {
		
		if (moveToString != null) {
			return moveToString;
		} else {
			Square fromSquare = from;
			Square toSquare = to;
			if (from.getPiece() == null) {
				fromSquare = to;
				toSquare = from;
			}
			
			moveToString = fromSquare.getPiece().getName();
			moveToString += "(" + fromSquare.toString() + ")";
			if (toSquare.getPiece() != null) {
				moveToString += "x"; 
			} else {
				moveToString += "-";
			}
			moveToString += toSquare.toString();
			
			KingStatus ks = checkKingStatus(fromSquare);
			switch (ks) {
				case CHECK:
					moveToString += "+";
					break;
				case MATE:
					moveToString += "#";
					break;
				default:
					break;
			}
			return moveToString;
		}
	}
	
	public KingStatus checkKingStatus(Square fromSquare) {
		KingStatus status = KingStatus.SAFE;
		Color fromPieceColor = fromSquare.getPiece().getColor();
		Color kingColor = fromPieceColor == Color.WHITE ? Color.BLACK : Color.WHITE;
		Square kingSquare = Game.getGame().getPlayerByColor(kingColor).getKingPos();
		Board board = Game.getGame().board;
		
		Boolean isKingThreatened = kingSquare.isThreatened(kingColor, board, false, this); 
		
		System.out.println((isKingThreatened ? "King is threatened" : "King is safe") + " from piece moving to:" + to.getRow() + ", col:" + to.getColumn());
		
		if (isKingThreatened) {
			status = KingStatus.CHECK;
			ArrayList<Move> kingMoves = Game.getGame().board.getLegalMoves(kingColor == Color.WHITE ? 1 : -1);
			if (kingMoves.size() == 0) {
				status = KingStatus.MATE;
			}
		}
		System.out.println("Checked king status: " + status.toString());
		return status;
	}
	
	public void setCapturedPiece(Piece piece) {
		capturedPiece = piece;
	}
	public Piece getCapturedPiece() {
		return capturedPiece;
	}
	
	public void apply(Board b, boolean isFinalMove) {
		
		if (isFinalMove) this.toString();
		
		//System.out.println ("applyMove: " + move.toString());
		Square fromSquare = this.from;
		Square toSquare = this.to;
		
		movedPiece = fromSquare.getPiece();
		
		fromSquare.getPiece().changeNumMoves(1, isFinalMove);
		if (isFinalMove) System.out.println("APPLY: Piece " + fromSquare.getPiece().getName() + " has moved " + fromSquare.getPiece().getNumMoves() + " times!");
		
		if (fromSquare.getPiece() instanceof King) {
			Game.getGame().getPlayerByColor(fromSquare.getPiece().getColor()).setKingPos(toSquare);
		}
		this.setCapturedPiece(toSquare.getPiece());
		if (toSquare.getPiece() != null) {
			toSquare.getPiece().death();
		}
		toSquare.setPiece(fromSquare.getPiece(), isFinalMove);
		fromSquare.setPiece(null, isFinalMove);
		if(isFinalMove)b.notifyListeners(this);
	}
	public void undo(Board b, boolean finalMove) {
		Square toSquare = this.from;
		Square fromSquare = this.to;
		
		movedPiece.changeNumMoves(-1, finalMove);
		if (finalMove) System.out.println("UNDO: Piece " + movedPiece.getName() + " has moved " + movedPiece.getNumMoves() + " times!");
		
		if (movedPiece instanceof King) {
			Game.getGame().getPlayerByColor(movedPiece.getColor()).setKingPos(toSquare);
		}
		toSquare.setPiece(movedPiece, finalMove);
		fromSquare.setPiece(this.capturedPiece, finalMove);
		if (this.capturedPiece != null) {
			fromSquare.getPiece().revive();
		}
		//System.out.println ("undoMove: " + move.toString());
		if(finalMove) b.notifyListeners(this);
	}
	public boolean isLegal(Board board) {
		if(from.getPiece().isLegal(this, board)) {
			Square kingSquare = Game.getGame().getCurrentPlayer().getKingPos();
			if (from.getPiece() instanceof King) {
				return true;
			} else {
				if (!kingSquare.isThreatened(kingSquare.getPiece().getColor(), board, false, this)) {
					return true;
				}
				this.undo(board, false);
			}
		}
		return false;
	}
	public Square getFromSquare() {
		return from;
	}
	public Square getToSquare() {
		return to;
	}
}