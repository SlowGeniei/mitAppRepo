#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <errno.h>

//This defines the colors used for different kinds of prints
#define CDEBUG "\x1b[0m"
#define CERROR "\x1b[31m"
#define CINDEXING "\x1b[34m"
#define CCONSOLE "\x1b[32m"


//Change this value to the address of your matrix file from the perspective of where this file is
#define FILE_NAME "testMatrix.txt"

//This boolean (1 == TRUE, 0 == FALSE) defines whether the program prints the matrix into the console after each step is completed
//Not recomended for large matrixes
#define DEBUG 0

//This value defines how small the step size is during the initial 0-intercept scan
//A bigger number == more accuracy
#define LAMBDA_GRAPH_ACCURACY 25

void tag(int id) {
	printf("Tag%d\n", id);
}

/*printMatrix
 *Inputs:
 *  double **matrix     (the array to print)
 *  int data_entries    (M, rows)
 *  int data_attributes (N, columns)
 *Output
 *  (NaN)
 *Notes:
 *  This is just a simple function to print out an array to the console
 */
void printMatrix(double** matrix, int data_entries, int data_attributes) {
	for (int i = 0; i < data_entries; i++) {
		printf("%d:", i);
		for (int j = 0; j < data_attributes; j++) {
			printf("\t%f ", matrix[i][j]);
		}
		printf("\n");
	}
}

void printMatrixWithTabs(double** matrix, int data_entries, int data_attributes, int tabNum) {
	for (int i = 0; i < data_entries; i++) {
		for (int j = 0; j < tabNum; j++) {printf("\t");}
		printf("%d:", i);
		for (int j = 0; j < data_attributes; j++) {
			printf("\t%f ", matrix[i][j]);
		}
		printf("\n");
	}
}

void freeDynamicMatrix (double **matrix, int row) {
	int i;
	for (i = 0; i < row; i++) {
		free (matrix[i]);
	}
	free (matrix);
}

/*translateInputMatrix
 *Inputs:
 *  FILE *file          (the file from which to pull out the array)
 *  int data_entries    (M, rows)
 *  int data_attributes (N, columns)
 *Output:
 *  double **matrix     (the array retrieved from the file)
 *Notes:
 *  This function reads a file line by line and parses it into individual doubles
 *  It constitutes a ',' as the end of a double
 */
double** translateInputMatrix (FILE *file, int data_entries, int data_attributes) {
	char line[data_attributes * 20 + data_attributes];
	int row = 0;
	int col = 0;
	char *strNumber = malloc(sizeof(double) * sizeof(char));
	int strNumberCounter= 0;

	double **matrix = malloc(data_entries * sizeof(double *));
	for(int i = 0; i < data_entries; i++) {
		matrix[i] = malloc(data_attributes * sizeof(double));
	}

	while(fgets(line, sizeof(line), file)) {
		col = 0;
		for (int i = 0; i < strlen(line); i++) {
			char step = line[i];
			if (step != '\n' && step != '\0' && step != ' ') {
				if (step != ',') {
					strNumber[strNumberCounter] = step;
					strNumberCounter++;
				} else {
					if (row >= data_entries) {
						exit(0);
					} else if (col >= data_attributes) {
						exit(0);
					}
					strNumber[strNumberCounter + 1] = '\0';
					matrix[row][col] = atof(strNumber);
					for (int i = 0; i < strNumberCounter; i++) strNumber[i] = "";
					strNumberCounter = 0;
					//printMatrix(matrix, data_entries, data_attributes);
					col++;
				}
			}   
		}
		if (col < data_attributes) {
			exit(0);
		}
		row++;
	}
	if (row < data_entries) {
		exit(0);
	}
	free (strNumber);
	return matrix;
}

/*printMatrixColumnMeans
 *Inputs:
 *  double *matrixColumnMeans   (an array with each column corresponding to the mean of the column from the matrix it was calculated from)
 *  int data_attributes         (N, columns)
 *Output:
 *  (NaN)
 *Notes:
 *  A simple function to print a 1d array
 */	int matrix1Rows = 0;
void printMatrixColumnMeans (double *matrixColumnMeans, int data_attributes) {
	for (int i = 0; i < data_attributes; i++) {
		printf("\t%f,", matrixColumnMeans[i]);
	}
	printf("\n");
}

/*findMatrixColumnMeans
 *Inputs:
 *  double **matrix             (a 2d array of doubles)
 *  int data_entries            (M, rows)
 *  int data_attributes         (N, columns)
 *Output:
 *  double *matrixColumnMeans   (an array with each column corresponding to the mean of the column from the matrix it was calculated from)
 *Notes:
 *  This function averages each row of a 2d array and compresses it into a 1d array
 */
double *findMatrixColumnMean (double **matrix, int data_entries, int data_attributes) {
	double *matrixColumnMeans = malloc(sizeof(double) * data_attributes);
	for (int j = 0; j < data_attributes; j++) {       //I switch I and J here because I'm doing the nested for loop backwards
		double matrixColumnTotal = 0;
		for (int i = 0; i < data_entries; i++) {
			matrixColumnTotal += matrix[i][j];
		}
		matrixColumnMeans[j] = matrixColumnTotal / data_entries;
	}
	return matrixColumnMeans;
}

/*calculateZeroMeanedMatrix
 *Inputs:
 *  double **matrix             (a 2d array of doubles)
 *  double *matrixColumnMeans   (an array with each column corresponding to the mean of the column from the matrix it was calculated from)
 *  int data_entries            (M, rows)
 *  int data_attributes         (N, columns)
 *Output:
 *  double **matrix             (a 2d array of doubles)
 *Notes:
 *  This function subtracts from each item in each column of 'matrix' the value in the corresponding column of 'matrixColumnMeans'
 */
double **calculateZeroMeanedMatrix (double **matrix, double *matrixColumnMeans, int data_entries, int data_attributes) {
	for (int j = 0; j < data_attributes; j++) {
		for (int i = 0; i < data_entries; i++) {
			matrix[i][j] -= matrixColumnMeans[j];
		}
	}
	return matrix;
}

/*calculateCovarianceMatrix
 *Inputs:
 *  double **matrix     (a 2d array of doubles
 *  int data_entries    (M, rows)
 *  int data_attributes (N, columns)
 *Ouput:
 *  double **covarianceMatrix   (the output of some fancy maths)
 */
double **calculateCovarianceMatrix (double **matrix, int data_entries, int data_attributes) {

	int covarianceMatrixRows = -1;
	int covarianceMatrixColumns = -1;

	double **covarianceMatrix = malloc(data_attributes * sizeof(double *));
	for (int i = 0; i < data_attributes; i++) covarianceMatrix[i] = malloc(data_attributes * sizeof(double));

	for (int i = 0; i < data_attributes; i++) {
		covarianceMatrixRows++;
		covarianceMatrixColumns = -1;
		for (int j = 0; j < data_attributes; j++) {
			covarianceMatrixColumns++;
			double rowAdditions = 0;
			for (int k = 0; k < data_entries; k++) {
				rowAdditions += matrix[k][i] * matrix[k][j];
			}
			covarianceMatrix[covarianceMatrixRows][covarianceMatrixColumns] = rowAdditions;
		}
	}
	return covarianceMatrix;
}

/*calculateDeterminant
 * Inputs
 *   double **matrix    (a 2d array of doubles)
 *   int size           (the width and height of said matrix)
 * Output
 *   double determinant (the determinant of the passed matrix)
 */
double calculateDeterminant (double **matrix, int size) {
	//Variable declarations
	int topCol = 0;
	int tempMatrixRow = 0;
	int tempMatrixCol = 0;

	double determinant = 0.0;
	int sign = 1;

	////printf("\n\n\n\nEntering matrix size: %d\n", size);
	int i, j, k;
	if (size != 2) { //If size = 2, return ad - cb
		//printf("\tSize > 2\n");
		for (i = 0; i < size; i++) {
			//if (i > 0) printf("\n\n\n\nReturning to size: %d\n", size);
			//Grabbing the current topRow step
			double topStep = matrix[0][i];

			//printf("\tTop Step col:%d, val:%f\n", i, topStep);

			tempMatrixRow = 0; //Resetting tempMatrixRow for new parsing run

			//Temp Matrix declaration
			double **tempMatrix = malloc (sizeof(double *) * size);
			for (j = 0; j < size; j++) tempMatrix[j] = malloc (sizeof(double) * size);

			//TempMatrix parsing
			for (j = 1; j < size; j++) { //j starts at 1 to skip top row
				//printf("\t\tStarted row %d\n", j);
				for (k = 0; k < size; k++) {
					//printf("\t\t\tChecking val at row:%d,col:%d\n", j, k);
					if (k != i) { //Skips the col that the topStep is in
						//printf("\t\t\t\tValue does not share a col with i\n");

						double currentStep = matrix[j][k];
						tempMatrix[tempMatrixRow][tempMatrixCol] = currentStep;

						//printf("\t\t\t\tAdded value:%f from matrix[%d][%d] to tempMatrix[%d][%d]\n", currentStep, j, k, tempMatrixRow, tempMatrixCol);

						tempMatrixCol++; //Steps forward through columns in tempMatrix
						//printf("\t\t\t\tIncremented tempMatrixCol now %d\n", tempMatrixCol);
					}
				}
				tempMatrixCol = 0; //Resets the col marker for next row in tempMatrix
				tempMatrixRow++; //Jumps down a row in tempMatrix
				//printf("\t\tIncremented tempMatrixRow now %d\n", tempMatrixRow);
			}

			//Step down a layer into the matrix while updating total
			//printf("\tPassing tempMatrixLower");
			determinant += sign * topStep * calculateDeterminant (tempMatrix, size - 1);
			freeDynamicMatrix (tempMatrix, size-1);
			sign *= -1.0;
		}

		return determinant;
	} else {
		//printf("\tFound 2x2 matrix\n");
		return (matrix[0][0] * matrix[1][1]) - (matrix[1][0] * matrix[0][1]);
	}
}

double calculateDeterminantWithLambdaVal (double **matrix, int size, double lamda) {

	int i;
	double *diagonals = malloc (sizeof (double) * size);

	for (i = 0; i < size; i++) {
		diagonals[i] = matrix[i][i];
		matrix[i][i] -= lamda;
	}

	double determinant = calculateDeterminant (matrix, size);

	for (i = 0; i < size; i++) matrix[i][i] = diagonals[i];
	free(diagonals);

	return determinant;
}


double **generateLambdaGraph (double **matrix, int size) {

	int i, j;
	const int increments = size * LAMBDA_GRAPH_ACCURACY;
	double diagonals = 0;

	double *allDiagonals = malloc (sizeof(double) * size);
	double **graph = malloc (sizeof(double *) * increments);
	for (i = 0; i < increments; i++) graph[i] = malloc (sizeof(double) * 2);

	for (i = 0; i < size; i++) {
		diagonals += matrix[i][i];
		allDiagonals[i] = matrix[i][i];
		//printf("Added %f to diagonals (now %f) and to allDiagonals[%d]\n", matrix[i][i], diagonals, i);
	}

	double range = 2 * diagonals;
	double stepSize = fabs(range / increments);

	//printf("The sum of the diagonals is %f with calculated step size of %f\n", diagonals, stepSize);    

	double determinant = 0;
	double currentLambda = -diagonals;
	for (int i = 0; i < increments; i++) {

		for (int j = 0; j < size; j++) {
			matrix[j][j] = allDiagonals[j] - currentLambda;
		}

		//printf("\tCalculating determinant of:\n");
		//printMatrix (matrix, size, size);
		determinant = calculateDeterminant (matrix, size);
		//printf("Attempting to save det:%f at lambda val:%f\n", determinant, currentLambda);
		graph[i][0] = currentLambda;
		graph[i][1] = determinant;
		//printf("%f\t%f\n", currentLambda, determinant);

		currentLambda += stepSize;
	}

	//printf("Finished calculation! Starting data print:\n");

	//Reset matrix
	for (i = 0; i < size; i++) matrix[i][i] = allDiagonals[i];

	free (allDiagonals);
	return graph;
}

int *findZeroInterceptAreas (double **graph, int entries, int size) {
	int i, foundIntercepts = 0;

	int *lambdaZeroInterceptAreas = malloc (sizeof (int) * size);

	for (i = 1; i < entries; i++) {

		double lambdaValue1 = graph[i-1][0];
		double lambdaValue2 = graph[i][0];
		double determinant1 = graph[i-1][1];
		double determinant2 = graph[i][1];

		//printf("At Index:%d, checking value %f and %f\n", i, determinant1, graph[i][0]);
		if ((determinant1 > 0 && determinant2 < 0) || (determinant1 < 0 && determinant2 > 0)) {
			//printf("\tFound zero intercept between lambda values %f and %f (index %d)\n", determinant1, determinant2, i);
			lambdaZeroInterceptAreas[foundIntercepts] = (i - 1);
			foundIntercepts++;
		} else {
			//printf("\tFailed to find zero intercept between lambda values %f and %f (index %d)\n", determinant1, determinant2, i);
		}
	}

	if (foundIntercepts != size) {
		printMatrix (graph, entries, size);
		printf("\n\n%sInput matrix has led to a covariance matrix with an invalid graph spread.\nPlease reconsider your life choices.\n", CERROR);
		printf("Found %d intercepts\n");
		exit(0);
	}

	return lambdaZeroInterceptAreas;
}

double *calculateLinearInterpolationIntercept (double **matrix, int size, double p1X, double p1Y, double p2X, double p2Y) {

	//Simple line variable algebra
	double slope = (p1Y - p2Y) / (p1X - p2X);
	double yIntercept = p1Y - (slope * p1X); //b = y - mx
	double xIntercept = (0 - yIntercept) / slope; //x = (-b)/m

	//Find location along curve of xIntercept
	int i;
	double *allDiagonals = malloc (sizeof (double) * size);
	for (i = 0; i < size; i++) {
		//Saving the diagonal to restore the matrix after calculation
		allDiagonals[i] = matrix[i][i];

		//Subtracting lambda from the diagonal
		matrix[i][i] -= xIntercept;
	}
	double determinant = calculateDeterminant (matrix, size);
	for (i = 0; i < size; i++) matrix[i][i] = allDiagonals[i];
	free (allDiagonals);

	//Return in format x,y
	double *linearInterpolationIntercept = malloc (sizeof (double) * 2);
	linearInterpolationIntercept[0] = xIntercept;
	linearInterpolationIntercept[1] = determinant;

	return linearInterpolationIntercept;
}

double *calculatePolynomialCoefficientsFromPointTriplet (double **matrix, int size, double p1X, double p1Y, double p2X, double p2Y, double *latestIntercept, double deltaX) {

	int i, j;

	//List of coefficients to return
	double *coefficients = malloc (sizeof (double) * 3);

	//matrix to use in crammer's method coefficient solving
	double **polynomialMatrix = malloc (sizeof (double *) * 3);
	for (i = 0; i < 3; i++) polynomialMatrix[i] = malloc (sizeof (double) * 3);

	//list of y values to use in crammer's method for loop
	double yPointList[3] = {p1Y, latestIntercept[1], p2Y};

	//Adding variables into polynomial matrix in format:
	//  [x2, x1, 1] [a]   [y1]
	//  [x2, x1, 1] [b] = [y2]
	//  [x2, x1, 1] [c]   [y3]
	polynomialMatrix[0][0] = p1X * p1X;
	polynomialMatrix[1][0] = 0; //Value should actually be latestIntercept[0]^2, but, with graph centering, always equals 0
	polynomialMatrix[2][0] = p2X * p2X;
	polynomialMatrix[0][1] = p1X;
	polynomialMatrix[1][1] = 0; //also latestIntercept[0]
	polynomialMatrix[2][1] = p2X;
	for (i = 0; i < 3; i++) polynomialMatrix[i][2] = 1.0;

	//printf("Passed points: %f,%f %f,%f %f,%f\n", p1X, p1Y, 0.0/*latestIntercept[0]*/, latestIntercept[1], p2X, p2Y);    
	//printf("Original polynomialMatrix:\n");
	//printMatrix (polynomialMatrix, 3, 3);

	//Storing determinant of polynomialmatrix for use in main loop
	double polynomialMatrixDeterminant = calculateDeterminant (polynomialMatrix, 3);
	//printf("Determinant of polynomnialMatrix is %f\n", polynomialMatrixDeterminant);

	//Main loop
	for (i = 0; i < 3; i++) {
		//printf("\nInside crammer's loop index:%d\n", i);
		//Storing soon-to-be overwritten values for later restoration
		double replacedColumn[3] = {polynomialMatrix[0][i], polynomialMatrix[1][i], polynomialMatrix[2][i]};
		//printf("\tSaved column of index %d is %f,%f,%f\n", i, replacedColumn[0], replacedColumn[1], replacedColumn[2]);

		//Replace the current column of the loop with the Y column
		for (j = 0; j < 3; j++) polynomialMatrix[j][i] = yPointList[j];
		//printf("\tReplaced column index %d of polynomialMatrix with yPointList (column: %f,%f,%f, yPointList: %f,%f,%f)\n", i, polynomialMatrix[0][i], polynomialMatrix[1][i], polynomialMatrix[2][i], yPointList[0], yPointList[1], yPointList[2]);

		//Calculate the determinant of the edited polynomialMatrix to find a of i
		double determinant = calculateDeterminant (polynomialMatrix, 3);
		//printf("\tDeterminant of created matrix is %f\n", determinant);

		//Reset polynomialMatrix
		for (j = 0; j < 3; j++) polynomialMatrix[j][i] = replacedColumn[j];
		//printf("\tReset column index %d of polynomialMatrix with yPointList (column: %f,%f,%f, yPoinyMatrix: %f,%f,%f)\n", i, polynomialMatrix[0][i], polynomialMatrix[1][i], polynomialMatrix[2][i], yPointList[0], yPointList[1], yPointList[2]);

		//Save coefficient
		coefficients[i] = determinant / polynomialMatrixDeterminant;
		//printf("\tCalculated coefficient: %f; (%f / %f = %f)\n", coefficients[i], determinant, polynomialMatrixDeterminant, coefficients[i]);
		//printf("\tFound coefficient for index %d: %f\n", i, coefficients[i]);

	}

	return coefficients;
}

double calculatePolynomialxIntercept (double *coefficients) {

	//Grab coefficients for easier use
	double a = coefficients[0];
	double b = coefficients[1];
	double c = coefficients[2];

	if (a != 0) {
		double intercepts[2];

		//Solve for x using quadratic formula: x = (-b +||- sqrt(b^2 - 4ac)) / 2a
		intercepts[0] = ((0 - b) + sqrt((b * b) - (4 * a * c))) / (2 * a);
		intercepts[1] = ((0 - b) - sqrt((b * b) - (4 * a * c))) / (2 * a);

		//Math function 'sqrt' sets global error variable 'errno' to 'EDOM' when passed a negative argument
		if (errno == EDOM) {
			printf("Error in finding the square root of %f\n", (b * b) - (4 * a * c));
			exit (0);
		}

		//Check which point lies on lambdGraphCurve to eliminate incorrect intercept
		//This check assumes that the intercept closest to latestIntercept[0] is the correct one

		double check1 = abs(intercepts[0]);
		double check2 = abs(intercepts[1]);

		if (check1 < check2) {
			//printf("\tChose option 1 (%f), option two: %f\n", intercepts[0], intercepts[1]);
			return intercepts[0];
		} else {
			//printf("\tChose option 2 (%f), option one: %f\n", intercepts[1], intercepts[0]);
			return intercepts[1];
		}

		printf("\n\nSomething seriously wrong has happened. Consider re-thinking your life choices.\n");
		exit(0);
	} else {
		//If a == 0, then a simple linear line is used:
		//y = bx + c
		// is equivelant to:
		//x = (-c) / b
		// when y == 0
		double intercept = (0.0 - c) / b;
		//printf("Detected linear line, intercept is %f\n", intercept);
		return intercept;
	}
}

double *calculateZeroIntercepts (double **matrix, int size, double **graph) {

	int i, j;

	//Step 1: Find rough intercept areas
	int *roughIntercepts = findZeroInterceptAreas (graph, size * LAMBDA_GRAPH_ACCURACY, size);
	double *zeroIntercepts = malloc (sizeof(double) * size);

	//Step 2: Loop through rough areas
	for (i = 0; i < size; i++) {

		
		//Seg fault below
		double p1X = graph[roughIntercepts[i]][0];
		double p1Y = graph[roughIntercepts[i]][1];
		double p2X = graph[roughIntercepts[i] + 1][0];
		double p2Y = graph[roughIntercepts[i] + 1][1];


		//Step 2.1: Find location along curve of linear interpolation x intercept
		double *latestIntercept = calculateLinearInterpolationIntercept (matrix, size, p1X, p1Y, p2X, p2Y);


		//Step 2.2: Zero coordinate system around latestIntercept to mitigate roundoff error
		double deltaX = 0 - latestIntercept[0]; //The value to add to graph X values to convert back into the regular graph form for use in determinant calculation
		double tp1X = p1X + deltaX;
		double tp2X = p2X + deltaX;
		

		//Step 2.3: Iterate upon polynomial intercept solving algorithm
		for (j = 0; j < 10; j++) {

			//Step 2.3.1: Solve for coefficients of degree 3 polynomial based on points
			double *coefficients = calculatePolynomialCoefficientsFromPointTriplet (matrix, size, tp1X, p1Y, tp2X, p2Y, latestIntercept, deltaX); //latest intercept is needed for the Y value. 0 is used for latestIntercept[0]

			//Step 2.3.2: Solve for x intercept using coefficients
			double xIntercept = calculatePolynomialxIntercept (coefficients);

			//Reset value
			xIntercept -= deltaX;

			//Step 2.3.3: set latestIntercept to xIntercept and its corresponding y value
			latestIntercept[0] = xIntercept;
			latestIntercept[1] = calculateDeterminantWithLambdaVal (matrix, size, xIntercept);

			//Step 2.3.4: Re-center points 1 and 2 around new latestIntercept[0]
			deltaX = 0 - latestIntercept[0];
			tp1X = p1X + deltaX;
			tp2X = p2X + deltaX;

		}

		printf("Found xIntercept #%d to be at coordinate point (%f,%f)\n", i, latestIntercept[0], latestIntercept[1]);
		zeroIntercepts[i] = latestIntercept[0];
	}

	return zeroIntercepts;
}

void addMatrixRows (double **covarianceMatrix, int size, int sourceRow, int destinationRow) {

	int i;
	for (i = 0; i < size; i++) {covarianceMatrix[destinationRow][i] += covarianceMatrix[sourceRow][i];}
}

double *solveGaussianEliminatedMatrix(double **matrix, int size) {

	int i, j;
	double *eigenVector = malloc (sizeof (double) * size);
	for (i = 0; i < size; i++) {eigenVector[i] = 0;}
	eigenVector[size - 1] = 1;

	//Main Logic Loop
	for (int rowIndex = (size - 1), i = 0; rowIndex > 0; rowIndex--, i++) {//It's helpful to have a 'numCycles' sort of index (i).

		int columnIndex = size - 2 - i;
		//printf("Solving for term #%d\n", rowIndex);

		//Works backward along the current row until it hits the current column
		double runningTotal = 0;
		for (j = (size - 1); j != columnIndex; j--) {runningTotal += eigenVector[j] * matrix[rowIndex][j];}

		//printf("\tCalculated a sum of: %f\n", runningTotal);

		double termValue = (0 - runningTotal) / matrix[rowIndex][columnIndex];
		eigenVector[columnIndex] = termValue;
		//printf("\tTerm is calculated to be: %f\n", termValue);

	}

	return eigenVector;
}

double **calculateEigenVectors (double **originalCovarianceMatrix, int size, double *eigenValues) {

	int i, j, k, l;

	double **eigenVectors = malloc (sizeof(double *) * size);
	for (i = 0; i < size; i++) {eigenVectors[i] = malloc (sizeof(double) * size);}

	double **covarianceMatrix = malloc (sizeof (double *) * size);
	for (i = 0; i < size; i++) {covarianceMatrix[i] = malloc (sizeof (double) * size);}

	for (i = 0; i < size; i++) {

		for (j = 0; j < size; j++) {
			for (k = 0; k < size; k++) {covarianceMatrix[j][k] = originalCovarianceMatrix[j][k];}
		}

		double currentEigenValue = eigenValues[i];
		//double covarianceMatrixDiagonals[size];
		//for (j = 0; j < size; j++) {covarianceMatrixDiagonals[j] = covarianceMatrix[j][j];}
		for (j = 0; j < size; j++) {covarianceMatrix[j][j] -= currentEigenValue;}

		//printf("\nFinished prep-work for eigenvalue:%f\n", currentEigenValue);
		//printMatrixWithTabs (covarianceMatrix, size, size, 1);
		//Logic
		for (j = 0; j < (size - 1); j++) { //Unfortunately, j == currentCol, not j == currentRow


			//printf("\tBeginning prep work for column:%d\n", j);
			double leadingCoefficientTopTerm = covarianceMatrix[j + 1][j];
			double topTerm[size];
			for (k = 0; k < size; k++) {topTerm[k] = covarianceMatrix[j + 1][k];}
			//printf("\tEnding prep work for column:%d\n", j);
			//printf("\tCanceling using row %d\n", j + 1);

			for (k = (size - 1); k > (1 + j); k--) { //Likewise, k == currentRow, not k == currentCol

				//printf("\t\tStarting prepwork for row-cancelation at row:%d\n", k);
				double leadingCoefficientLowerTerm = covarianceMatrix[k][j];
				double topToBottomSFwithSinFlip = (0 - leadingCoefficientLowerTerm) / leadingCoefficientTopTerm;
				//printf("\t\tScaleFactor is %f; topCoef: %f, bottomCoef: %f\n", topToBottomSFwithSinFlip, leadingCoefficientTopTerm, leadingCoefficientLowerTerm);
				for (l = 0; l < size; l++) {covarianceMatrix[j + 1][l] = topTerm[l] * topToBottomSFwithSinFlip;}
				//printf("\t\tEnding prepwork for row-cancelation at row:%d\n", k);

				addMatrixRows (covarianceMatrix, size, j + 1, k);
				//printf("\t\tAdded rows. Covariance matrix is now:\n");
				//printMatrixWithTabs (covarianceMatrix, size, size, 2);
				//printf("\n");

			}

			for (k = 0; k < size; k++) {covarianceMatrix[j + 1][k] = topTerm[k];}
			//printf("\n\tRestored covarianceMatrix\n");
			//printMatrixWithTabs(covarianceMatrix,size,size,1);
			//printf("\n");
		}

		//At this point, 'covarianceMatrix' contains a matrix whose unecessary coefficients have been eliminated

		double *eigenVector = solveGaussianEliminatedMatrix(covarianceMatrix, size);
		for (j = 0; j < size; j++) {eigenVectors[i][j] = eigenVector[j];}

		free (eigenVector);

		//for (j = 0; j < size; j++) {covarianceMatrix[i][i] = covarianceMatrixDiagonals[j];}
	}

	freeDynamicMatrix (covarianceMatrix, size);

	return eigenVectors;
}

//Verified
void **multiplyMatrices (double **matrix1, int matrix1Rows, int matrix1Cols, double **matrix2, int matrix2Rows, int matrix2Cols) {

	if (matrix1Cols != matrix2Rows || matrix1Rows != matrix2Cols) {
		printf("Invalid passed matrices. Cannot multipy.\n");
		exit(0);
	}

	int i, j, k;
	int resultSize = matrix1Rows;
	double **result = malloc (sizeof (double *) * resultSize);
	for (i = 0; i < resultSize; i++) {result[i] = malloc (sizeof (double) * resultSize);}

	for (i = 0; i < matrix1Rows; i++) {
		for (j = 0; j < matrix2Cols; j++) {
			double total = 0;
			for (k = 0; k < matrix1Cols; k++) {
				total += matrix1[i][k] * matrix2[k][j];
			}
			result[i][j] = total;
		}
	}

	//printMatrix (result, resultSize, resultSize);
	
	return result;
}

/*main
 *Notes:
 *  This is what guides everything else
 */
int main (int argc,char *argv[]) {
	
	if (DEBUG) printf("Step 0 begin:\n");

	FILE *file = fopen(FILE_NAME, "r");

	int i,j;
	char line[41];
	int data_entries;
	int data_attributes;
	if (fgets(line, sizeof(line), file)) {
		if (sscanf(line,"%d,%d", &data_entries, &data_attributes) == 2) {
		} else {
			exit(0);
		}
	} else {
		exit(0);
	}
	
	if (DEBUG) printf("Step 0 end:\n");
	if (DEBUG) printf("Step 1 begin:\n");
	//Translating the file into a double** array
	double **matrix = translateInputMatrix(file, data_entries, data_attributes);

	//TODO: A-transpose-A

	if (DEBUG) printf("Step 1 end:\n");
	if (DEBUG) printf("Step 2 begin:\n");
	//Translating the file into a double** array
	//Finding the means of each of the columns
	double *matrixColumnMeans = findMatrixColumnMean(matrix, data_entries, data_attributes);

	if (DEBUG) printf("Step 2 end:\n");
	if (DEBUG) printf("Step 3 begin:\n");
	//Translating the file into a double** array
	//Subtracting said means from each item in the mean's respective column
	matrix = calculateZeroMeanedMatrix(matrix, matrixColumnMeans, data_entries, data_attributes);
	free (matrixColumnMeans);

	if (DEBUG) printf("Step 3 end:\n");
	if (DEBUG) printf("Step 4 begin:\n");
	//Translating the file into a double** array
	//Calculating the covariance matrix from this zero meaned matrix
	double **covarianceMatrix = calculateCovarianceMatrix(matrix, data_entries, data_attributes);
	freeDynamicMatrix (matrix, data_entries);

	if (DEBUG) printf("Step 4 end:\n");
	if (DEBUG) printf("Step 5 begin:\n");
	//Translating the file into a double** array
	//Generate sample spread for lambda determinant
	double **lambdaGraph = generateLambdaGraph (covarianceMatrix, data_attributes);

	if (DEBUG) printf("Step 5 end:\n");
	if (DEBUG) printf("Step 6 begin:\n");
	//Translating the file into a double** array
	//Calculate zero intercepts
	double *zeroIntercepts = calculateZeroIntercepts (covarianceMatrix, data_attributes, lambdaGraph);

	if (DEBUG) printf("Step 6 end:\n");
	if (DEBUG) printf("Step 7 begin:\n");
	//Translating the file into a double** array
	//Solve for eigenvectors based on eigenvalues
	double **eigenVectors = calculateEigenVectors(covarianceMatrix, data_attributes, zeroIntercepts); //Unsorted

	if (DEBUG) printf("Step 7 end:\n");

	printMatrixWithTabs(eigenVectors, data_attributes, data_attributes, 1);

	return 1;
}
