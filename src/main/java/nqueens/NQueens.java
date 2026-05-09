package nqueens;

// COSC 3P71 - Assignment 1
// Author: Matthew Grahlman	[5875695]
// Date: October 2017
// Solves the N Queens Problem using both a blind search (depth-first) and a heuristic search (A*). Prints to "output.txt" in the src folder.

import java.io.*;
import java.util.*;

public class NQueens {

	PrintWriter write;		// Used for Outputting to Text File
	int numAttempts = 0;	// # Board Configurations before a Valid Board was found


	// Constructor - N and Seed are Hardcoded (& Output File is Appended to each run) - Does both a Blind and Heuristic Search for Each N value
	public NQueens( ){
		int n = 8; 				// size of board
		int seed = 21;  			// seed used for the heuristic search
		int[] board = new int[n];   // 1D array representing board
		int numAttempts = 0;

		try{ 					    // Printing Search Info to Output
			write = new PrintWriter(new FileWriter(new File("output.txt"), true));
			write.println("Starting Blind Search");
			write.println("N Size: " + n);
		}
		catch(IOException e){ write.println(e); }

		doBlindSearch(n, board);    // Starting Blind Search
		printBoard(board, n); 		// Print the Board

		Random rand = new Random(seed);		// Generating a random board using the seed
		for (int a = 0; a < n; a++){ board[a] = rand.nextInt(n) + 1; }
		write.println("\nStarting Heuristic (A*) Search");
		write.println("N Size: " + n + " Seed: " + 21);

		Board newBoard = new Board(board); 			// Creates Board Object
        PriorityQueue<Board> pQ;					// Stores NextBoards of the Board
        pQ = newBoard.genNextBoards();				// Generates NextBoards
		doHeuristicSearch(newBoard, n);				// Starts the Heuristic Search (A*)

		write.close();	// Closes the PrintWriter (used for Output)

	} // end of Constructor



	// doHeuristicSearch - Performs an A* Search
	public void doHeuristicSearch(Board board, int n) {
		LinkedList<Board> needChecked = new LinkedList();	// Boards that Need Checked
        LinkedList<Board> checked = new LinkedList();		// Boards that Have Been Checked
        checked.add(board);									// Adds Initial Board to List
        Board bestNewBoard = null;
        numAttempts = 0;

        while (!checked.isEmpty( )) {	// Initial Board's Heuristic Value will Always Be Less Than or Equal to Every other Board
            int bestHValue = Integer.MAX_VALUE;
            int bestColumn = -10;
            numAttempts++;
            for (int i = 0; i < checked.size(); i++) {
                if (checked.get(i).getHeuristicValue() < bestHValue) {	// Checking if a New Best Heuristic Value was Found
                    bestHValue = checked.get(i).getHeuristicValue();
                    bestColumn = i;
                }
            }
            bestNewBoard = checked.remove(bestColumn);

            if (bestNewBoard.getHeuristicValue() == 0) {	// If Heuristic Value = 0 -> A Valid Board Was Found -> Print Board
                printBoard(bestNewBoard.getBoardState(), n);
                break;
            }

            needChecked.add(bestNewBoard);	// This Board has Now been Checked
            PriorityQueue<Board> newNextBoards = bestNewBoard.genNextBoards();
            for (Board newBoard : newNextBoards) {
                if (needChecked.contains(newBoard)) { continue; }
                if (!needChecked.contains(newBoard)) { checked.add(newBoard); }
            }
        }

	} // end of doHeuristicSearch



	// doBlindSearch - Iterative - Performs a Depth First Search on an NxN board
	public void doBlindSearch(int n, int[] board) {
		// Declaring and Initializing Variables
		int col, row, rTemp, mainRow;
	    col = 0;
		row = 1;
		rTemp = 0;
		mainRow = 1;
		numAttempts = 0;

		board[col] = mainRow;			// Placing first Queen in Top Left of the Board
		col++;
		numAttempts++;
		while (col < n){
			while (row <= n){
				board[col] = row;		// Places next Queen at value of Row
				if (checkState(board, n, col, row)){ 	// If the Queen DOESN'T conflict with other Queen's (Board is Valid) -> Move to Next Column
					col++;
					row = 1;
					break;
				}
				else{					// If the Queen DOES conflict with other Queen's (Board in Invalid) -> Attempts Next Row
					board[col] = 0;
					row++;
					numAttempts++;
				}

				if (row > n){			// If All Row's Have Been Attempted -> Moves Back A Column and Moves Down a Row
					col -= 1;
					while (col != 0){	// While we aren't moving the Queen in the 1rst Column
						rTemp = board[col] + 1;	// Attempts to move Row
						while (rTemp <= n){
							board[col] = rTemp;
							if (checkState(board,n,col,rTemp)){  // If the Queen DOESN'T conflict with other Queen's (Board is Valid) -> Move to Next Column
								col++;
								row = 0;
								rTemp = 1;
								break;
							}
							else{	// If the Queen DOES conflict with other Queen's (Board is Invalid) -> Move to next Row
								board[col] = 0;
								rTemp++;
								row = rTemp;
								numAttempts++;
							}
						}
						if (row == 0){	// If a Valid Move was found
							row++;
							break;
						}
						else{			// If All Row Values have been attempted -> Move back another column
							board[col] = 0;
							col--;
						}
					}
				}
              }
              if (row > n){	// If a Valid Move has NOT yet been found -> Move Queen in First Column Down
				mainRow++;
				if (mainRow > n){   	// Break when mainRow becomes larger than N (occurs when no solution can be found - e.g. N=2 or N=3)
					break;
				}
				board[col] = mainRow;	// Move's the Queen in the First Column -> Resets Col and Row values
				row = 1;
				col += 1;
				numAttempts++;
              }
		}

	}  // end of doBlindSearch



	// Check State - Returns a Boolean that represents whether or not the current board configuration is valid or not.
	public boolean checkState(int[] board, int n, int col, int row){
		int nCol = col;
		int nRow = row;

		// Checks Row's for Other Queens
		for (int a = 0; a < n; a++){
			for (int b = 0; b < n; b++){
				if (a != b && board[a] == board[b] && board[a] != 0 && board[b] != 0 ){
					return false;
				}
			}
		}

		// Check upLeft
		nCol = col - 1;
		nRow = row - 1;
		while (nCol >= 0 && nRow > 0){
			if (board[nCol] == nRow){
				return false;
			}
			nCol--;
			nRow--;
		}

		// Check upRight
		nCol = col + 1;
		nRow = row - 1;
		while (nCol < n && nRow > 0){
			if (board[nCol] == nRow){
				return false;
			}
			nCol++;
			nRow--;
		}

		// Check downLeft
		nCol = col - 1;
		nRow = row + 1;
		while (nCol >= 0 && nRow <= n){
			if (board[nCol] == nRow){
				return false;
			}
			nCol--;
			nRow++;
		}

		// Check downRight
		nCol = col + 1;
		nRow = row + 1;
		while (nCol < n && nRow <= n){
			if (board[nCol] == nRow){
				return false;
			}
			nCol++;
			nRow++;
		}

		return true;  // If Board is VALID State

	} // end checkState



	// printBoard method - Print's the board in ASCII characters and Print's Number of Attempts
	public void printBoard(int board[], int n){
		int row;
		boolean print = true;

		for (int i = 0; i < n; i++){// Validating Board State -> If Any Conflicts are Found It will Break and Print No Valid Configurations Found
			row = board[i];
			if (checkState(board, n, i, row) && board[i] != 0 && numAttempts != 0){
				print = true;
			}
			else{
				print = false;
				break;
			}
		}

		if (print == false){	// No Valid Configuration Found
			try{
				write.println("No Valid Configurations Found!");
				write.println("Number of Configurations Attempted: " + numAttempts);
			}
			catch(Exception e){ }
		}
		else{					// Valid Configuration Found
			try{
				write.println("Valid Configuration Found!");
				write.println("Number of Configurations Attempted: " + numAttempts);
			}
			catch(Exception e){ }

			for (int j = 1; j <= n; j++){			// Printing the ASCII Board
				for (int i = 1; i <= n; i++){
					if (j == board[i-1]) {
						try{ write.print("Q "); }
						catch(Exception e){ }
					}
					else{
						try{write.print("x "); }
						catch(Exception e){ }
					}
				}

				try{ write.println("\n"); }
				catch(Exception e){ }
			}
			try{ write.println("\n"); }
			catch(Exception e){ }
		}

	} // end of printBoard



    public static void main(String[] args) { NQueens q = new NQueens(); }	// Main Method

} // end of NQueens class
