import java.util.*;

enum PieceType {
    X,
    O
}

class Piece {
    private PieceType pieceType;

    public Piece(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    public PieceType getPieceType() {
        return pieceType;
    }
}

class Player {
    private String name;
    private Piece piece;

    public Player(String name, Piece piece) {
        this.name = name;
        this.piece = piece;
    }

    public String getName() {
        return name;
    }

    public Piece getPiece() {
        return piece;
    }
}

class Cell {
    int row;
    int col;
    Piece piece;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean isEmpty() {
        return piece == null;
    }
}

class Board {
    private int size;
    private Cell[][] grid;

    public Board(int size) {
        this.size = size;
        grid = new Cell[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }
    }

    public boolean placePiece(int row, int col, Piece piece) {
        if (row < 0 || col < 0 || row >= size || col >= size) {
            return false;
        }

        if (!grid[row][col].isEmpty()) {
            return false;
        }

        grid[row][col].piece = piece;
        return true;
    }

    public boolean checkWinner(int row, int col, Piece piece) {

        // Check Row
        boolean rowWin = true;
        for (int j = 0; j < size; j++) {
            if (grid[row][j].piece == null ||
                grid[row][j].piece.getPieceType() != piece.getPieceType()) {
                rowWin = false;
                break;
            }
        }

        // Check Column
        boolean colWin = true;
        for (int i = 0; i < size; i++) {
            if (grid[i][col].piece == null ||
                grid[i][col].piece.getPieceType() != piece.getPieceType()) {
                colWin = false;
                break;
            }
        }

        // Check Main Diagonal
        boolean diagWin = true;
        if (row == col) {
            for (int i = 0; i < size; i++) {
                if (grid[i][i].piece == null ||
                    grid[i][i].piece.getPieceType() != piece.getPieceType()) {
                    diagWin = false;
                    break;
                }
            }
        } else {
            diagWin = false;
        }

        // Check Anti-Diagonal
        boolean antiDiagWin = true;
        if (row + col == size - 1) {
            for (int i = 0; i < size; i++) {
                if (grid[i][size - 1 - i].piece == null ||
                    grid[i][size - 1 - i].piece.getPieceType() != piece.getPieceType()) {
                    antiDiagWin = false;
                    break;
                }
            }
        } else {
            antiDiagWin = false;
        }

        return rowWin || colWin || diagWin || antiDiagWin;
    }

    public void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j].piece == null) {
                    System.out.print("- ");
                } else {
                    System.out.print(grid[i][j].piece.getPieceType() + " ");
                }
            }
            System.out.println();
        }
    }

    public int getSize() {
        return size;
    }
}

class Game {
    private Board board;
    private Queue<Player> players;
    private int movesPlayed;

    public Game(int size) {
        board = new Board(size);

        players = new LinkedList<>();
        players.offer(new Player("Player1", new Piece(PieceType.X)));
        players.offer(new Player("Player2", new Piece(PieceType.O)));

        movesPlayed = 0;
    }

    public void startGame() {
        Scanner sc = new Scanner(System.in);

        while (true) {

            board.printBoard();

            Player currentPlayer = players.poll();

            System.out.println(
                currentPlayer.getName() +
                " enter row and column:"
            );

            int row = sc.nextInt();
            int col = sc.nextInt();

            boolean placed =
                board.placePiece(row, col, currentPlayer.getPiece());

            if (!placed) {
                System.out.println("Invalid Move");
                players.offer(currentPlayer);
                continue;
            }

            movesPlayed++;

            if (board.checkWinner(
                    row,
                    col,
                    currentPlayer.getPiece())) {

                board.printBoard();
                System.out.println(
                    currentPlayer.getName() + " Wins!"
                );
                break;
            }

            if (movesPlayed ==
                board.getSize() * board.getSize()) {

                board.printBoard();
                System.out.println("Game Draw");
                break;
            }

            players.offer(currentPlayer);
        }

        sc.close();
    }
}

public class TicTacToeDemo {
    public static void main(String[] args) {
        Game game = new Game(3);
        game.startGame();
    }
}
