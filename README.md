# N-Queens Problem 👑

Animated side-by-side visualisation of two algorithms racing to solve the N-Queens problem — watch blind depth-first search and A* heuristic search compete in real time.

![CI](https://github.com/GrahlmanMatthew/N-Queens-Problem/actions/workflows/ci.yml/badge.svg)
![GitHub release](https://img.shields.io/github/v/release/GrahlmanMatthew/N-Queens-Problem)

---

## Demo

> 📸 *Demo GIF coming soon — to be recorded after first working build.*

---

## How it works

Place N queens on an N×N chessboard so that no two share a row, column, or diagonal. The classic 8-queens puzzle has 92 distinct solutions.

Two fundamentally different strategies attack the same problem simultaneously:

**Blind Search (Depth-First)** places queens column by column, trying each row in order. When a conflict is detected it backtracks to the previous column and increments the row. No domain knowledge is used — it explores the space exhaustively. For N=8 this takes 764 attempts.

**Heuristic Search (A\*)** starts from a randomly seeded board with all queens already placed. Each iteration picks the board state with the fewest conflicting pairs and expands it by trying every possible single-queen move. The heuristic guides the search toward valid configurations — N=8 needs only 47 attempts.

The contrast is the whole point: watching both boards animate in parallel makes the efficiency gap immediately visible.

---

## Origin

This started as **Assignment 1 for COSC 3P71 — Introduction to Artificial Intelligence** at Brock University, submitted in October 2017. The original implementation is preserved verbatim as a historical baseline — the algorithm logic in `NQueens.java` and `Board.java` is unchanged from the 2017 submission.

---

## Prerequisites

- Java 21+
- Maven 3.6+

---

## Installation and setup

```bash
git clone https://github.com/GrahlmanMatthew/N-Queens-Problem.git
cd N-Queens-Problem
```

---

## Usage

**Run the visualisation:**

```bash
mvn javafx:run
```

The app opens with two animated boards. Use the controls at the bottom to:
- Set N (board size, 4–20)
- Adjust animation speed
- Start or reset the race

**Run the original CLI solver (outputs to `output.txt`):**

```bash
mvn compile exec:java -Dexec.mainClass="nqueens.NQueens"
```

---

## Running the tests

```bash
mvn test
```

---

