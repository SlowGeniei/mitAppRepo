package ChessGame;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

public class HumanPlayer extends Player {
	
	Square sourceSquare;
	public HumanPlayer (Color side) {
		super(side);
	}
	
	@Override
	public RegularMove makeMove() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean mouseClicked (Square square, Board board, Game game) {
		if (sourceSquare == null) {
			if (square.getPiece() != null && square.getPiece().getColor() == side) {
				sourceSquare = square;
				return true;
			}
		} else {
			Move move;
			if (Castle.isCastleAttempt(sourceSquare, square)) {
				move = new Castle(board, square.getColumn() > sourceSquare.getColumn() ? true : false, side == Color.WHITE ? 1 : -1);
			} else if (PromotionMove.isPromotionAttempt(sourceSquare, square)){
				move = new PromotionMove(sourceSquare, square, null);
			} else {
				move = new RegularMove(sourceSquare, square);
			}
			sourceSquare = null;
			if (game.processMove(move)) {
				/*if (move.getFromSquare().getPiece().getColor() == Color.WHITE) {
					Chess.moveStackWhite.push(move);
				} else {
					Chess.moveStackBlack.push(move);
				}*/
			}
		}
		return false;
	}

	@Override
	public void startMove(Board board, Game game) {
	}
}