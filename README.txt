MIT Application Project File Repo
Hello reviewers! This repository contains the source files for my MIT portfolio projects.

Directories

All the relevant files lie inside the portfolio folder.
The source for the Principle Component Analysis project lies at "portfolio/code/PCA1_0.c"
The source for the NegaMax Chess Bot project lies at "portfolio/code/chessBot/*"
The source files for the Weather Station project lie at "portfolio/SolidWorks/anemometer_cup.STL" & "portfolio/SolidWorks/windVane_base_ours.STL"
The source file for the the Involute Gear Generator lies at "portfolio/SolidWorks/VariableizedInvoluteGear.SLDPRT"

ADD JAVA AWT STUFF

The rest of the ReadMe file will contain information on how to use and edit the project files.


Principle Component Analysis

This project comes in the form of a large (>700line) C file. This file is the latest version of my PCA algorithm. In my experience, the best way to navigate the file is to start at the bottom in the "main" function. As the "main" function is the director of the workflow, it is possible to intuit the locations of certain logical functions based on their position in "main." In case this is not enough, the below list contains the major functions listed in top-to-bottom order (as they exist in the file).
  findMatrixColumnMeans(..) - calculates the mean value of each column
  calculateZeroMeanedMatrix(..) - zeros the matrix around its origin
  calculateCovarianceMatrix(..) - calculates the covariance matrix of the provided dataset
  calculateDeterminant(..)  - calculates the determinant of a matrix
  generateLambdaGraph(..) - generates a graph in order to find zero-intercepts (eigenvalues)
  findZeroInterceptAreas(..) - finds the rough area, in the graph, where a zero-intercept occurs
  calculateLinearInterpolationIntercept(..) - linearly interpolates between two points, and then fits the resulting value to the graph
  calculatePolynomialCoefficientsFromPointTriplet(..) - fits a polynomial function to the current set of three points
  calculatePolynomialxIntercept(..) - finds the x coordinate of the x-intercept in the given polynomial
  calculateZeroIntercepts(..) - the "brain" function for the entire zero-intercept finding process
  solveGaussianEliminatedMatrix(..) - solves a matrix that has been gaussian eliminated (only 1 variable type)
  calculateEigenVectors(..) - calculates the eigenvectors based on the eigenvalues
Many other utility functions exist, but they are not central to the algorithm.
I was unsure of the best way to show how the algorithm is actually doing its work. The main explanation is in the PDF attached to the portfolio.


NegaMax Chess Bot

These source files mark the current state of my NegaMax-powered chess bot. Because of my focus on the bot itself, the UI is not in an advanced enough state to have a playable example. To supplement this, a large portion of the portfolio video is dedicated to showcasing gameplay. Rest assured that the core algorithm is complete. The following is a list of relevant classes and their definitions. "Relevance" based on how necessary the class is for the bot to function. UI classes are not included.
  Piece - An abstract parent class, Piece contains utility functions that define the movement, capturing, and scoring logic for any chess piece.
  Bishop - A child of Piece, Bishop contains the movement, capturing, and scoring logic of a bishop chess piece.
  King - A child of Piece, King contains the movement, capturing, and scoring logic of a king chess piece.
  Knight - A child of Piece, Knight contains the movement, capturing, and scoring logic of a king chess piece.
  Pawn - A child of Piece, Pawn contains the movement, capturing, scoring, and promotion logic of a player's pawn pieces.
  Queen - A child of Piece, Queen contains the movement, capturing, and scoring of a player's Queen pieces.
  Rook - A child of Piece, Rook defines the capturing, movement, and scoring of a player's rooks.
  Player - An abstract parent class, Player contains utility functions used to define movement logic for any player type.
  ComputerPlayer - A child of Player, ComputerPlayer defines the movement logic for a bot player.
  HumanPlayer - A child of Player, HumanPlayer defines the movement logic for a human player.
  Move - An abstract parent class, Move contains utility functions that define the movement logic of a chess move.
  PromotionMove - A child of Move, PromotionMove defines the movement logic of a pawn promotion move.
  RegularMove - A child of Move, RegularMove contains the movement logic of a standard chess move.
  Castle - A child of Move, Castle is a class that defines the move and un-move logic for a "castle" move.
  Board - Contains the actual chess board data, in the form of a grid of Squares. All positional logic has to pass through Board.
  Chess - The class that contains the "main" method, Chess initializes the entire project.
  Game - Contains high-level game logic such as start/stop code and legal move generation. Game also features the management code for the NegaMax algorithm
  KingStatus - A class tasked solely with keeping track of the current status of a player's king piece.
  Square - A class that defines a chessboard square. It contains many utility functions as well as its resident piece.
I hope this list will guide your review.


