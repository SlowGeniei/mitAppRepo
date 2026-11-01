package ChessGame;

import java.awt.*;
import javax.swing.*;

public class BoardContainer extends JPanel {
	
	JPanel letterPanel;
	JPanel letterPanelFlipped;
	JPanel numberPanel;
	JPanel numberPanelFlipped;
	
	JPanel letterPanelContainer;
	CardLayout letterPanelManager = new CardLayout();
	JPanel numberPanelContainer;
	CardLayout numberPanelManager = new CardLayout();
	
	Boolean isFlipped = false;
	
public BoardContainer (BoardPanel bp) {
		this.setLayout(new BorderLayout());
		this.add(bp, BorderLayout.CENTER);
		
		letterPanel = new JPanel();
		letterPanelFlipped = new JPanel();
		numberPanel = new JPanel();
		numberPanelFlipped = new JPanel();
		
		letterPanel.setLayout(new GridBagLayout());
		letterPanelFlipped.setLayout(new GridBagLayout());

		Label l = new Label("  ");
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridheight = l.getPreferredSize().height;
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		
		letterPanel.add(l, gbc);
		letterPanelFlipped.add(new JLabel("  "), gbc);
		
		gbc.weightx = 0.125;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		gbc.gridx = 1;
		letterPanel.add(new JLabel("A", SwingConstants.CENTER), gbc);
		letterPanelFlipped.add(new JLabel("H", SwingConstants.CENTER), gbc);
		gbc.gridx = 2;
		letterPanel.add(new JLabel("B", SwingConstants.CENTER), gbc);
		letterPanelFlipped.add(new JLabel("G", SwingConstants.CENTER), gbc);
		gbc.gridx = 3;
		letterPanel.add(new JLabel("C", SwingConstants.CENTER), gbc);
		letterPanelFlipped.add(new JLabel("F", SwingConstants.CENTER), gbc);
		gbc.gridx = 4;
		letterPanel.add(new JLabel("D", SwingConstants.CENTER), gbc);
		letterPanelFlipped.add(new JLabel("E", SwingConstants.CENTER), gbc);
		gbc.gridx = 5;
		letterPanel.add(new JLabel("E", SwingConstants.CENTER), gbc);
		letterPanelFlipped.add(new JLabel("D", SwingConstants.CENTER), gbc);
		gbc.gridx = 6;
		letterPanel.add(new JLabel("F", SwingConstants.CENTER), gbc);
		letterPanelFlipped.add(new JLabel("C", SwingConstants.CENTER), gbc);
		gbc.gridx = 7;
		letterPanel.add(new JLabel("G", SwingConstants.CENTER), gbc);
		letterPanelFlipped.add(new JLabel("B", SwingConstants.CENTER), gbc);
		gbc.gridx = 8;
		letterPanel.add(new JLabel("H", SwingConstants.CENTER), gbc);
		letterPanelFlipped.add(new JLabel("A", SwingConstants.CENTER), gbc);
		
		numberPanel.setLayout(new GridLayout(8, 1));
		numberPanel.add(new JLabel("8"));
		numberPanel.add(new JLabel("7"));
		numberPanel.add(new JLabel("6"));
		numberPanel.add(new JLabel("5"));
		numberPanel.add(new JLabel("4"));
		numberPanel.add(new JLabel("3"));
		numberPanel.add(new JLabel("2"));
		numberPanel.add(new JLabel("1"));
		
		numberPanelFlipped.setLayout(new GridLayout(8, 1));
		numberPanelFlipped.add(new JLabel("1"));
		numberPanelFlipped.add(new JLabel("2"));
		numberPanelFlipped.add(new JLabel("3"));
		numberPanelFlipped.add(new JLabel("4"));
		numberPanelFlipped.add(new JLabel("5"));
		numberPanelFlipped.add(new JLabel("6"));
		numberPanelFlipped.add(new JLabel("7"));
		numberPanelFlipped.add(new JLabel("8"));
	
		letterPanelContainer = new JPanel();
		numberPanelContainer = new JPanel();
		
		letterPanelContainer.setLayout(letterPanelManager);
		numberPanelContainer.setLayout(numberPanelManager);
		
		letterPanelContainer.add(letterPanel, "DEFAULT");
		letterPanelContainer.add(letterPanelFlipped, "FLIPPED");
		
		numberPanelContainer.add(numberPanel, "DEFAULT");
		numberPanelContainer.add(numberPanelFlipped, "FLIPPED");
		
		this.add(numberPanelContainer, BorderLayout.WEST);
		this.add(letterPanelContainer, BorderLayout.SOUTH);
	}
	
	public void flip () {
		
		if (isFlipped) {
			letterPanelManager.show(letterPanelContainer, "DEFAULT");
			numberPanelManager.show(numberPanelContainer, "DEFAULT");
			isFlipped = false;
			
			
		} else {
			letterPanelManager.show(letterPanelContainer, "FLIPPED");
			numberPanelManager.show(numberPanelContainer, "FLIPPED");
			isFlipped = true;
		}
		repaint();
	}
}
