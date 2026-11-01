package ChessGame;
import javax.swing.JPanel;
import java.awt.Color;

import java.awt.*;

import javax.swing.JLabel;


public class Tile extends JPanel implements SquareListener {
	Square square;
	JLabel label;
	public Tile(Square square) {
		
		setSize(20,20);
		label = new JLabel("");
		this.add(label, BorderLayout.CENTER);
		
		setSquare (square);
	} 
	
	public void setSquare (Square s) {		
		if (square != null) {square.removeListener(this);}
		
		this.square = s;
		
		if((square.getRow() + square.getColumn()) % 2 == 0) {
			setBackground(new Color(0xda, 0xbd, 0x8d));
		} else {
			setBackground(new Color(0x7b, 0x4b, 0x21));

		}
		
		squareModified();
		
		square.addListener(this);
	}
	
	public Square getSquare() { return square; }
	
	public void squareModified() {
		// System.out.println ("Square at " + square.getRow() + "," + square.getColumn() + " modified");
		if (square.getPiece() != null) {
			label.setIcon(square.getPiece().getIcon());
			//System.out.println("Set Icon #2");
		}
		else {
			label.setIcon(null);
		}
		repaint();
	}
}
