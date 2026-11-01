package ChessGame;

import javax.swing.*;

public class StackTextArea extends JTextArea implements StackListener {
	MoveStack s;
	
	public StackTextArea(MoveStack s) {
		super(40,10);
		this.s = s;
		s.addListener(this);
	}

	@Override
	public void StackModified() {
		this.setText("");
		for (int i = 0; i <= s.getStackTop(); i++) {
			this.append(s.peek(i).toString() + "\n");
		}
	}
}