package ChessGame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;

public class Chess extends JFrame implements ActionListener, BoardListener {
	
	static Board board;
	static Game game;
	static MoveStack moveStackWhite = new MoveStack(3000);
	static MoveStack moveStackBlack = new MoveStack(3000);
	int turn = 1;
	private JPanel contentPane;
	private JTextArea moveFieldWhite;
	private JTextArea moveFieldBlack;
	private JPanel movePanel;
	private JScrollPane scrollPane;
	JButton startBtn;
	JButton temp;
	JButton cancel;
	JButton whiteMove;
	JButton blackMove;
	JButton flipBoard;
	
	BoardPanel boardPanel;
	BoardContainer boardContainer;
	
	static int[] whitePlayerKingPosition = {7, 4};
	static int[] blackPlayerKingPosition = {0, 4};
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					board = new Board(whitePlayerKingPosition, blackPlayerKingPosition);
					game = null;
					Chess frame = new Chess();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Chess() {
//START		
	//Setting up 1/2
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 600);
		
	//Setting up 2/2
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
	//Creates and adds boardPanel to the contentPane
		boardPanel = new BoardPanel(board, game);
		boardContainer = new BoardContainer (boardPanel);
		contentPane.add(boardContainer, BorderLayout.CENTER);
		
	/*Creates the buttons:
	Start button, at the moment, calls think with alternating colors
	temp button will eventually call isThreatenedSquare on all of board
	
	Creates the buttonPanel
	adds both buttons to the panel
	*/
		startBtn = new JButton ("Start");
		temp = new JButton ("Calc Threats");
		cancel = new JButton ("Cancel Move");
		whiteMove = new JButton ("Move White");
		whiteMove.setEnabled(false);
		blackMove = new JButton ("Move Black");
		blackMove.setEnabled(false);
		flipBoard = new JButton ("Flip Board");
		
		JPanel buttonPanel = new JPanel();
		JPanel buttonPanelNorth = new JPanel();
		JPanel buttonPanelSouth = new JPanel();
		
		buttonPanel.setLayout(new BorderLayout(0,0));
		buttonPanelNorth.setLayout(new BorderLayout(0,0));
		buttonPanelSouth.setLayout(new BorderLayout(0,0));
		
		buttonPanel.add(buttonPanelNorth, BorderLayout.NORTH);
		buttonPanel.add(buttonPanelSouth, BorderLayout.SOUTH);
		
		buttonPanelNorth.add(whiteMove, BorderLayout.WEST);
		buttonPanelNorth.add(blackMove, BorderLayout.EAST);
		buttonPanelNorth.add(flipBoard, BorderLayout.CENTER);
		
		buttonPanelSouth.add(startBtn, BorderLayout.CENTER);
		buttonPanelSouth.add(temp, BorderLayout.SOUTH);
		buttonPanelSouth.add(cancel, BorderLayout.NORTH);
		
	//Creates side panel and adds button panel
		JPanel sidePanel = new JPanel();
		sidePanel.setLayout(new BorderLayout(0,0));
		sidePanel.add(buttonPanel, BorderLayout.SOUTH);
		
	//A JLabel that will eventually accept the depth for the bot
		//TODO: Make this work
		JTextField depthField = new JTextField(2);
		JLabel depthLabel = new JLabel("Depth: ");
		JPanel depthPanel = new JPanel();
		depthPanel.setLayout (new BorderLayout(0,0));
		depthPanel.add(depthLabel, BorderLayout.WEST);
		depthPanel.add(depthField, BorderLayout.EAST);
		sidePanel.add(depthPanel,BorderLayout.NORTH);
		
	//The move displayer + scrollBar
		movePanel = new JPanel();
		moveFieldWhite = new StackTextArea(moveStackWhite);
		moveFieldBlack = new StackTextArea(moveStackBlack);
		movePanel.add(moveFieldWhite, BorderLayout.WEST);
		movePanel.add(moveFieldBlack, BorderLayout.EAST);
		scrollPane = new JScrollPane(movePanel);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sidePanel.add(scrollPane, BorderLayout.CENTER);
		
	/*
	The cleaning up:
	adds sidePanel to the content pane
	adds a listener to boardPanel
	adds an action listener to the start button (think button)
	*/
		contentPane.add(sidePanel, BorderLayout.EAST);
		board.addListener(boardPanel);
		board.addListener(this);
		startBtn.addActionListener (this);
		flipBoard.addActionListener(this);
		whiteMove.addActionListener(this);
		blackMove.addActionListener(this);
		cancel.addActionListener(this);
		
		//this is the temporary calc threats button
		/*
		temp.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		    	System.out.println("Pushed button \"temp\" and started calculating");
		    	for (int row = 0; row < 8; row++) {
					for (int col = 0; col < 8; col++) {
						if (board.getSquare(row, col).getPiece() == null) {
							System.out.println("No Piece on square row:" + row + " col:" + col);
						} else {
							System.out.println("Looking for threats at row:" + row + " col:" + col);
							boolean threatened = board.getSquare(row, col).isThreatened(Color.WHITE, board, true);
							if (threatened) {
								System.out.println("Threat on square row:" + row + " col:" + col);
							} else {
								System.out.println("No threat on square row:" + row + " col:" + col);
							}
						}
					}
				}
		    	System.out.println("Finished with button \"temp\"");
		    }
		});
		*/
//END
	}
	public void actionPerformed(ActionEvent e) {
		
		System.out.println("In actionPerformed");
		if (e.getSource() == startBtn) {
			Player whitePlayer = new HumanPlayer (Color.WHITE);
			Player blackPlayer = new ComputerPlayer (Color.BLACK, 2);
			game = new Game (board, whitePlayer, blackPlayer, boardPanel);
			boardPanel.setGame(game);
			whiteMove.setEnabled(true);
			blackMove.setEnabled(false);
			game.play(
					board.getSquare(
							whitePlayerKingPosition[0],
							whitePlayerKingPosition[1]),
					board.getSquare(
							blackPlayerKingPosition[0],
							blackPlayerKingPosition[1]
									)
					);
		} else if (e.getSource() == flipBoard) {
			System.out.println("Flipping");
			boardContainer.flip();
			boardPanel.flip();
		}
	}

	@Override
	public void boardModified(Move move) {
		if (move.getToSquare().getPiece().getColor() == Color.WHITE) {
			moveStackWhite.push(move);
		} else {
			moveStackBlack.push(move);
		}
		repaint();
	}
}