package ChessGame;

public class PromotionMove extends Move {

	Square fromSquare;
	Square toSquare;
	Piece pieceType;
	RegularMove pawnMove;
	
	Boolean isPieceTypeDefined;
	String toString;
	
	public PromotionMove (Square fromSquare, Square toSquare, Piece pieceType) {
		
		this.fromSquare = fromSquare;
		this.toSquare = toSquare;
		this.pieceType = pieceType == null ? new Queen(Color.WHITE) : pieceType;
		
		isPieceTypeDefined = pieceType == null ? false : true;
		pawnMove = new RegularMove (fromSquare, toSquare);
	}
	
	@Override
	public void apply(Board board, boolean finalMove) {
		
		if (finalMove) this.toString();
		
		pawnMove.apply(board, false);
		pawnMove.getFromSquare().notifyListeners();
		pawnMove.getToSquare().setPiece(pieceType, finalMove);
		if (finalMove) board.notifyListeners(this);
	}
	
	@Override
	public void undo(Board board, boolean finalMove) {
		
		pawnMove.undo(board, false);
		pawnMove.getToSquare().notifyListeners();
		if (finalMove) board.notifyListeners(this);
	}

	public static Boolean isPromotionAttempt (Square fromSquare, Square toSquare) {
		if (fromSquare.getPiece() instanceof Pawn && (toSquare.getRow() == 0 || toSquare.getRow() == 7)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isLegal(Board b) {
		Boolean isLegal = pawnMove.isLegal(b);
		return isLegal;
	}

	@Override
	public String toString() {
		if (toString == null) {
			toString = pawnMove.toString() + "(" + pieceType.getPieceType() + ")";
		}
		
		return toString;
	}

	@Override
	public Square getFromSquare() {

		return pawnMove.getFromSquare();
	}

	@Override
	public Square getToSquare() {

		return pawnMove.getToSquare();
	}

}
