MIT Application Project File Repo
Hello reviewers! This repository contains the source files for my MIT portfolio projects. I recommend that, before you look at this repository, you watch the portfolio video and read the portfolio PDF.

Directories

All the relevant files lie inside the portfolio folder.
Source files for Principle Component Analysis are in "portfolio/code/principleComopnentAnalysis/"
Source files for Negamax Chess Bot are in "portfolio/code/chessBot/"
Source files for Weather Station are in "portfolio/SolidWorks/weatherStation/"
Source files for AWT Graphics Handler are in "portfolio/code/AWTGH/"
Source files for Involute Gear Generator are in "portfolio/SolidWorks/involuteGearGenerator/"

The rest of the readme file contains technical information pertaining to the implementation of each project.


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
As a side note, I chose to sample the graph (instead of something like power iteration) because of its simplicity. Also, if I scale certain aspects of the graph generation limits as the data-set sizes increase, I have found that it performs quite well.

You can run this program by running the pca.exe file that is in the project directory. It must be run in the same folder as a file named "testMatrix.txt." Because of the libraries it requires, I would recommend compiling the C file yourself. A standard Cygwin installation is capable of running this tool. A sample testMatrix.txt file is provided to show the proper input syntax. The program is currently set up to print the eigenvalues and vectors.


NegaMax Chess Bot

These source files mark the current state of my NegaMax-powered chess bot. Because of my focus on the bot itself, the UI is not in an advanced enough state to have a playable example. To supplement this, a large portion of the portfolio video is dedicated to showcasing gameplay. Rest assured that the core algorithm is complete. The following is a list of relevant classes and their definitions. "Relevance" based on how necessary the class is for the bot to function. UI classes are not included.
  Piece - An abstract parent class, Piece contains utility functions that define the movement, capturing, and scoring logic for any chess piece.
  Bishop - A child of Piece, Bishop contains the movement, capturing, and scoring logic of a bishop chess piece.
  King - A child of Piece, King contains the movement, capturing, and scoring logic of a king chess piece.
  Knight - A child of Piece, Knight contains the movement, capturing, and scoring logic of a knight chess piece.
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

The project directory contains all of the source class files, as well as a text file containing a transcription of a chess game where I ran the bot against itself. It does not contain the entire game, as it quickly devolves into a stalemate from the point I ended with.


Weather Station

The weather station's design process has been comprised of a cycle of initial design, building, testing, and then revising the design. Through this process, I have successfully built two fully functional data-collection modules, with two more along the way. These are the current module designs that I have.
	The Brain Board
		The Brain Board is comprised of a large number of connectors, resistors, and level shifters, all of which are leading into an ESP-32 microcontroller. The board's main purpose is to provide power for, and translate incoming data from, the weather station's sensing modules. The ESP-32 is running an OS called ESPHome, which is an OS that allows the chip to interface cleanly with my Home Assistant (HAOS) virtual machine. The chip receives data from the sensors, and sends it to my server. The server will then display and archive the real-time weather data. The current weather-proofing plan for the board is to mount it inside a weather-proof container (Like this one: "https://www.amazon.com/gp/aw/d/B0B87V7QTH"), and use watertight cable-passthroughs (Like these: "https://www.amazon.com/Pack-Cable-Gland-Kit-Waterproof/dp/B0FJFVBLJD/") for the data cables. The container will likely mount directly onto a T-frame bar using a 3D printed bracket.
	Stevenson Shield
		The Stevenson Shield is comprised of stacked disk layers whose walls are flared at the base, thus creating a rain-proof container. In addition, the downward facing gaps between the layers allow for air to flow freely through the module. The Stevenson Shield is, therefore, perfectly suited for sensors that detect static atmospheric variables. My Stevenson Shield is currently equipped with a combined temperature and humidity sensor and a barometric pressure sensor. The resulting data is sent through a cable to the Brain Board. A threaded rod is used to mount the module onto a T-frame bar. The Stevenson Shield's mounting rod is long enough that the module is raised several inches above the bar. This is done because I have to vertically stagger the modules in order to fit them all onto a single T-frame.
	Anemometer
		The Anemometer is very simple sensor-wise, but mechanically interesting. The module is made up of a stationary base and a rotating upper/outer portion. The rotating section is made up of three hemispherical "cups" that present varying degrees of aerodynamics depending on which angle they are approached from, leading to rotation when the wind is blowing. The upper portion also contains a magnet, which allows the base section to count its rotations using a hall effect sensor. The data is passed to the Brain Board using the same type of cable as the Stevenson Shield. The Anemometer module is mounted onto a T-frame bar using, similar to the Stevenson Shield, a threaded rod. This module is, however, mounted lower than the shield, as the rotating motion will incur stresses that might decrease the lifespan of the rod.
	Weather Vane
		The Weather Vane presents itself as the most technically difficult module I have worked on thus far. Like the Anemometer, the Weather Vane is built with a fixed base that is covered by a rotating upper section. Unlike the Anemometer, the upper section is designed such that it points in the direction that the wind is blowing. My current method of detecting this rotation, as can be seen in the video, is a ring of eight hall effect sensors surrounding the rotational axis. These sensors will be able to detect up to sixteen different wind directions. The data wires from these sensors are sent through the base of the module, and there consolidated into two data cables. I have not yet designed an upper section for the Weather Vane, so its mounting method is not set in stone. It will likely be attached to a T-frame bar in a similar method as the other modules.
	Rain Gauge
		The Rain Gauge is currently in its preliminary stages. The following explanation is not necessarily the final design. The Rain Gauge will likely be implemented using an tipping "hopper" shape with a known volume. A collector with a large surface area will funnel rain water into this container. Whenever the hopper "tips", a sensor of some type will detect the movement. The brain board will contain logic to convert the data into rain-flow metrics.

I can see that the "collaboration" section of the PDF could raise some eyebrows. As you know from the PDF, a man named Giovanni inspired the weather station with his "instructibles" article (linked in the PDF). Because of this, in the portfolio document, I give Giovanni a 15% "stake" in my project. Since it may seem like my mentor and I were simply following a tutorial, I will now give more collaboration details.
	There are two main parts of the weather station. The 3D-printed structure and the electrical/sensory system. In my weather station, the electrical system's design and implementation are 100% my own. Some 3D models were originally sourced from Giovanni, but every single one had to be re-designed in order to iron out some strange modeling artifacts. In some cases, there were necessary 3D models that were simply missing, which I then designed myself. Because of these two facts, I did not give Giovanni a huge amount of credit for the models. I believe it is fair to say that Giovanni contributed 15% of the project.

The project directory contains two example 3D files: the base for the weather vane, and the hollowed-hemisphere arm for the anemometer. I believe that these files showcase the complexity and breadth of coverage for the modeling that I have done for the project.


AWTGH

The internals of my AWT graphics "handler" are actually quite simple. When I designed AWTGH, I had three main goals: to move rendering work off of the event thread, to accurately and efficiently handle frame scheduling, and to package the tool in a user-friendly way. I believe that my current implementation satisfies all of these requirements.
	The tool is built around one main premise: doing rendering calculations on a dedicated thread. I implement this using two features: the Thread class, and BufferedImages. If a class extends the Thread class, it inherits a "run()" method. Any code inside this method will run on a dedicated thread if/when "start()" is called on that class. A BufferedImage is a misleadingly named class that acts as a canvas. By using custom methods or a Graphics2D object, a developer can generate any graphics they might want onto a BufferedImage. The benefit of using BufferedImages over, say, the Graphics object that gets passed in "paintComponent()" is that a BufferedImage can be generated at anytime and by anyone. Using these two classes is the job of GraphicsProcessor. GraphicsProcessor will, in its "run()" method, paint the current graphics logic onto a BufferedImage. This image is then the next frame. GraphicsProcessor also handles the timing of frame generation and display. It tracks the amount of time a frame takes to generate, and uses this information to correctly schedule the creation of the next frame.
	PaintLogic is a simple interface class that defines the "painting" logic for a given frame. The user defines a PaintLogic using in-line lambda syntax, and then GraphicsProcessor will do the actual painting.
	RenderPanel is a output and "wrapper" class. It is the only class that a user would ever interact with. RenderPanel handles hot-swapping variables such as width, height, targetFPS, and the current PaintLogic. The class is also tasked with managing the lifecycle of the GraphicsProcessor thread, seamlessly handling startup and shutdown logic. In addition, RenderPanel is what actually displays the frames that GraphicsProcessor generates. Due to the fact that it extends a JPanel, RenderPanel draws all of the processed BufferedImages onto itself. This means that a user only has to define a RenderPanel, call ".start()" on the panel, and then add the panel to their JFrame.
	As you can see, all three goals are met.

As I mentioned in the PDF, I used AI to generate Javadoc that would be genuinely useful for the project. All code is 100% written by me.

The project directory contains the complete source files, in the "source" folder, and the example files (in the "examples" folder). As it is a utility program, there is no easy way to "run" the program. The examples, I believe, well showcase the capability of the tool. Unfortunately, I limited myself to a single file for each example, leading to computation code being lumped into the PaintLogic method. This is obviously not the intended use of the interface class, but it is functional.

AWTGH is featured much better in its own repository (also under this account). If you are interested in this project in particular, I recommend that you check it out.


Involute Gear Generator

Before I could make a SolidWorks feature tree that would build a parametrized gear, I first had to set and/or derive the input variables. My generator takes in the module, the pressure angle, the tooth count, and the thickness, and calculates all the other necessary variables using these four. The formulas I used are as follows:

module (m) = given
pressure angle (pa) = given
gear width (gw) = given
tooth count (tc) = given
clearance (c) = 0.167 * m
circular pitch (cp) = m * PI
reference pitch diameter (rpd) = m * tc
base circle diameter (bcd) = rpd * cos ( pa )
addendum circle diameter (acd) = rpd + 2 * m
dedendum circle diameter (dcd) = rpd - 2 * m - 2 * c

I could then reference theses variables when setting dimensions on SolidWorks sketch components or defining distances and/or instances in SolidWorks features. A single hurdle remained: actually generating an involute curve. I managed to do this in a fairly simple way.
	Because an involute is defined as "the path a string takes as it unwinds from a circle", I could calculate the lengths of arcs from the starting point to different points along the circle. I would then create lines that were tangent to the circle at these points, and set their lengths to the previously calculated arc-length. For example, if point A is 180 degrees from the starting point, then line A would extend half of the circles circumference. Once I had enough lines, I could use their end points to define a curve. This curve is now an involute. In my experiments with overlaying a genuine involute curve over my approximation, I have determined that I only need five guide points to accurately create an involute.
	After I have this curve, it is simply a matter of extruding, cutting, pasting, and patterning to arrive at the finished product.

The project directory contains the parametrized SolidWorks part. In case the reviewers are unable to open/edit a SolidWorks part, I included several 3D model files that were generated using the part. You can refer to the portfolio video for footage of the generator in action.
