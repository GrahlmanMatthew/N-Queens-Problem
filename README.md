# N-Queens Problem 👑

Place N queens on an N×N chessboard so that no two queens attack each other — no shared row, column, or diagonal. The classic 8-queens puzzle has 92 distinct solutions.

This project explores the problem through two search strategies: a brute-force depth-first search and an A\* heuristic search that uses the number of conflicting queens as a guide. The difference in efficiency is striking — for N=8, blind search requires 764 attempts while A\* finds a solution in 47.

---

## Origin

This started as **Assignment 1 for COSC 3P71 — Introduction to Artificial Intelligence** at Brock University, submitted in October 2017. The original implementation is preserved here as a historical baseline before the project was expanded with a visual interface. The algorithms and core logic are unchanged from the 2017 submission.

---

## Algorithms

**Blind Search (Depth-First)**  
Places queens column by column, trying each row in order. When a conflict is detected it backtracks to the previous column and tries the next row. No domain knowledge is used — it explores the space exhaustively until a valid placement is found.

**Heuristic Search (A\*)**  
Starts from a randomly seeded board with all queens placed. Each iteration selects the board state with the lowest heuristic value (number of conflicting queen pairs) and expands it into all possible single-queen moves. Continues until a conflict-free board is found.

---

## Results (N=8, Seed=21)

| Algorithm | Attempts to Solution |
|-----------|---------------------|
| Blind Search (DFS) | 764 |
| Heuristic Search (A\*) | 47 |

---

## Requirements

- Java 21+
- Maven 3.6+

---

## Build & Run

**Compile:**
```bash
mvn compile
```

**Run:**
```bash
mvn exec:java
```

Output is written to `output.txt` in the working directory. Each run appends results so you can compare across configurations.

**Run directly (alternative):**
```bash
mvn package
java -cp target/n-queens-problem-1.0-SNAPSHOT.jar nqueens.NQueens
```

---

## Project Structure

```
src/main/java/nqueens/
├── NQueens.java   — main class; blind search and A* search implementations
└── Board.java     — board state model; heuristic calculation and next-state generation
```

Board state is represented as a 1D array where `board[column] = row` (1-indexed rows, 0-indexed columns).
