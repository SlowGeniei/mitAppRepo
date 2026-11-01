package ChessGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BoardPanel extends JPanel implements BoardListener, MouseListener{
	Board board;
	Game game;
	Tile tiles[][] = new Tile[8][8];
	Color viewPoint;
	public BoardPanel(Board board, Game game) {
		this.board = board;
		this.game = game;
		viewPoint = Color.WHITE;
		initialise();
	}
	public void boardModified (Move move) {
		// this.paintImmediately(this.getBounds());
		this.repaint();
		Thread.yield();
		//try {
		//	Thread.sleep(10);
		//}
		//catch (InterruptedException e) {}
	}
	
	private void initialise() {
		setLayout(new GridLayout(8,8));
		if (viewPoint == Color.WHITE) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					Tile tile = new Tile(board.getSquare(i, j));
					tile.addMouseListener(this);
					add(tile);
					tiles[i][j] = tile;
				}
			}
		}
	}
	public void mouseClicked(MouseEvent e) {
		Tile tile = (Tile)e.getSource();
		Square square = tile.getSquare();
 		if (game.getCurrentPlayer().mouseClicked(square, board, game)) {
			this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
					((ImageIcon)square.getPiece().getIcon()).getImage(),
					new Point(0,0),
					square.getPiece().getName()));
		} else {
			this.setCursor(Cursor.getDefaultCursor());
		}
	}
	
	public void flip () {
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 4; j++) {
				
				Square temp = tiles[i][j].getSquare();
				tiles[i][j].setSquare(tiles[7-i][7-j].getSquare());
				tiles[7-i][7-j].setSquare(temp);
				
			}
		}
		
		repaint ();
	}
	
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}

