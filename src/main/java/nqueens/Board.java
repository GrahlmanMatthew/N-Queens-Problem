package nqueens;

// COSC 3P71 - Assignment 1
// Author: Matthew Grahlman	[5875695]
// Date: October 2017

import java.util.*;

public class Board implements Comparable <Board> {

    int n; 								// Size of Board
    int hValue;							// Heuristic Value (Heuristic is # of Queen's Currently Conflicting on the Board);
    int[] curBoard; 					// Current Board
    PriorityQueue<Board> nextBoards;	// Priority Queue of Next Board States -> Sorted by hValue (lowest to highest)

	// Board Constructor -> Receives the 1D Board Array -> Calculates it's Heuristic -> Finds all of the Next Possible Boards States
    public Board(int[] board) {
    	n = board.length;
        curBoard = new int[n];  			// 1D array representing current board
		for (int a = 0; a < n; a++){		// Copying the values of the board into the current board
			curBoard[a] = board[a];
		}
        hValue = calculateHeuristic(); 			// Calcultes the heuristic of this board
        nextBoards = new PriorityQueue(); 	// Initiliaze the Next Possible Board States

    } // end of Board Constructor



	// Accessor Method: getHeuristicValue -> Returns hValue (the heuristic value)
    public int getHeuristicValue( ) {
        return hValue;

    } // end of getHeuristicValue



    // Function: calculateHeuristic -> Calculates the hValue of this Board Object
    private int calculateHeuristic() {
    	int numConflicts = 0;
    	int nCol = 0;
    	int nRow = 0;
        for (int a = 0; a < n; a++) {
  			numConflicts += checkState(a);	// Sums Number of Conflicts for all Queens
         }
        return numConflicts;

    } // end of calculateHeuristic



    // Function - checkState - Returns the Number of Conflicts for a Given Queen (Modified Version of checkState in the main)
	public int checkState(int a) {
		int conflicts = 0;
		int nCol, nRow;
	 	for (int i = 0; i < n; i++) {
	 		if (i != a) {
				if (curBoard[i] == curBoard[a] && curBoard[a] != 0) {	// If there is a Row Conflict
				 conflicts++;
				}
	 		}

	 		// Check Upper Left Diagonal
			nCol = a - 1;
			nRow = curBoard[a] - 1;
			while (nCol >= 0 && nRow <= n){		// BUG: condition should be nRow > 0
				if (curBoard[nCol] == nRow){
					conflicts++;		// If there is a Conflict
			}
				nCol--;
				nRow--;
			}

	 		// Check Upper Right Diagonal
			nCol = a + 1;
			nRow = curBoard[a] - 1;
			while (nCol < n && nRow > 0){
				if (curBoard[nCol] == nRow){
					conflicts++;		// If there is a Conflict
				}
				nCol++;
				nRow--;
			}

			// Check Down Left Diagonal
			nCol = a - 1;
			nRow = curBoard[a] + 1;
			while (nCol >= 0 && nRow <= n){
				if (curBoard[nCol] == nRow){
					conflicts++;		// If there is a Conflict
				}
				nCol--;
				nRow++;
			}

			// Check Down Right Diagonal
			nCol = a + 1;
			nRow = curBoard[a] + 1;
			while (nCol < n && nRow <= n){
				if (curBoard[nCol] == nRow){
					conflicts++;			// If there is a Conflict
				}
				nCol++;
				nRow++;
			}
	 	}
		return conflicts;

	} // end of checkState



	// Accessor Method getBoardState - Returns this Board Object
    public int[] getBoardState( ) {
        return this.curBoard;

    } // end of getBoardState



    // Function genNextBoards - Generates all of the next Possible States and puts them into a PriorityQueue
    public PriorityQueue<Board> genNextBoards() {
        int[] cBoard = new int[n];
        for (int a = 0; a < n; a++){	// Copies Board
        	cBoard[a] = curBoard[a];
        }

        for (int b = 0; b < n; b++) {
            for (int c = 1; c <= n; c++) {
                if (cBoard[b] != c) {	// If a Queen doesn't move -> Don't Make a New Board
                    cBoard[b] = c; 		// Moving Queen
                    nextBoards.add(new Board(cBoard));
                    for (int d = 0; d < n; d++){
                    	cBoard[d] = curBoard[d];	// Resets the Copy of Board
                    }

                }
            }
        }
        return nextBoards;

    } // end of genNextBoards



	@Override  // Overriding compareTo method -> Boards are Sorted by Ascending hValue
    public int compareTo(Board board) {
        if (this.getHeuristicValue() == board.getHeuristicValue()) {
            return 0;		// If the Board is Valid
        }
        return (this.getHeuristicValue() < board.getHeuristicValue() ? -1:1);
    }

} // end of Board class
