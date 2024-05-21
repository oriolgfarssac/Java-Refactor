package com.directi.training.codesmells.smelly.chess;

import com.directi.training.codesmells.smelly.Color;
import com.directi.training.codesmells.smelly.Position;
import com.directi.training.codesmells.smelly.pieces.*;

import java.util.Scanner;

/**
 * @author Oriol
 * @author Arman
 * @see <a href="https://github.com/harshmanocha/Refactoring.git">Repositori de GitHub</a>
 */

public class GameEngine
{
    private static final Scanner scanner = new Scanner(System.in);
    private final ChessBoard _chessBoard;
    private Player _player1, _player2;
    private Player _currentPlayer;

    public GameEngine(Player player1, Player player2)
    {
        _chessBoard = new ChessBoard();
        _player1 = player1;
        _player2 = player2;
        resetBoard();
    }

    public void initGame()
    {
        if (_currentPlayer == null || _player1.getColor() == Color.BLACK) {
            _currentPlayer = _player1;
            _player1.setColor(Color.WHITE);
            _player2.setColor(Color.BLACK);
        } else {
            _currentPlayer = _player2;
            _player1.setColor(Color.BLACK);
            _player2.setColor(Color.WHITE);
        }
        System.out.println("\nGame initialized");
        System.out.println("Player " + _player1.getName() + " has Color " + _player1.getColor());
        System.out.println("Player " + _player2.getName() + " has Color " + _player2.getColor());
        System.out.println("");
        resetBoard();
        System.out.println(_chessBoard);
    }

    public void startGame()
    {
        while (true) {
            System.out.println("Next move is of " + _currentPlayer.getName()
                               + " [" + _currentPlayer.getColor() + "]");
            System.out.print("Enter position (row col) of piece to move: ");
            Position from = inputPosition();
            System.out.print("Enter destination position: ");
            Position to = inputPosition();
            Move move = new Move(from, to);
            if (isValidMove(move)) {
                makeMove(move);
            } else {
                System.out.println("Invalid move!");
            }
        }
    }

    private Position inputPosition()
    {
        int row = scanner.nextInt() - 1;
        int col = scanner.nextInt() - 1;
        return new Position(row, col);
    }


    /**
     * Fa un reset del taulell d'escacs a la seva posicio inicial.
     * Posa les peces tant blanques com negres a les posicions d'inici.
     */

    public void resetBoard()
    {
        for (int column = 0; column < 8; column++) {
            if (column == 0) {
                _chessBoard.getBoard()[7][column].setPiece(new LeftRook(Color.WHITE));
            } else if (column == 1) {
                _chessBoard.getBoard()[7][column].setPiece(new LeftKnight(Color.WHITE));
            } else if (column == 2) {
                _chessBoard.getBoard()[7][column].setPiece(new LeftBishop(Color.WHITE));
            } else if (column == 3) {
                _chessBoard.getBoard()[7][column].setPiece(new King(Color.WHITE));
            } else if (column == 4) {
                _chessBoard.getBoard()[7][column].setPiece(new Queen(Color.WHITE));
            } else if (column == 5) {
                _chessBoard.getBoard()[7][column].setPiece(new RightBishop(Color.WHITE));
            } else if (column == 6) {
                _chessBoard.getBoard()[7][column].setPiece(new RightKnight(Color.WHITE));
            } else if (column == 7) {
                _chessBoard.getBoard()[7][column].setPiece(new RightRook(Color.WHITE));
            }
        }
        for (int column = 0; column < 8; column++) {
            _chessBoard.getBoard()[6][column].setPiece(new Pawn(Color.WHITE));
        }

        for (int column = 0; column < 8; column++) {
            if (column == 0) {
                _chessBoard.getBoard()[0][column].setPiece(new LeftRook(Color.BLACK));
            } else if (column == 1) {
                _chessBoard.getBoard()[0][column].setPiece(new LeftKnight(Color.BLACK));
            } else if (column == 2) {
                _chessBoard.getBoard()[0][column].setPiece(new LeftBishop(Color.BLACK));
            } else if (column == 3) {
                _chessBoard.getBoard()[0][column].setPiece(new King(Color.BLACK));
            } else if (column == 4) {
                _chessBoard.getBoard()[0][column].setPiece(new Queen(Color.BLACK));
            } else if (column == 5) {
                _chessBoard.getBoard()[0][column].setPiece(new RightBishop(Color.BLACK));
            } else if (column == 6) {
                _chessBoard.getBoard()[0][column].setPiece(new RightKnight(Color.BLACK));
            } else if (column == 7) {
                _chessBoard.getBoard()[0][column].setPiece(new RightRook(Color.BLACK));
            }
        }
        for (int column = 0; column < 8; column++) {
            _chessBoard.getBoard()[1][column].setPiece(new Pawn(Color.BLACK));
        }

        _chessBoard._kingDead = false;
    }

    private void endGame()
    {
        System.out.println("Game Ended");
        Player winner = _currentPlayer;
        winner.increase();
        System.out.println("WINNER - " + winner + "\n\n");
    }

    private Player getOtherPlayer()
    {
        return _player1 == _currentPlayer ? _player2 : _player1;
    }

    /**
     * Mou una peça a una nova posició al tauler d'escacs si el moviment és vàlid.
     * Aquest mètode intenta moure una peça des de la seva posició actual a una posició especificada.
     * Després de realitzar el moviment, imprimeix l'estat actual del tauler d'escacs. Si un rei ha estat capturat,
     * acaba el joc actual i n'inicia un de nou. Si no, canvia al jugador contrari.
     *
     * @param move L'objecte move que conté les posicions d'inici i final per a la peça que es mourà.
     */

    public void makeMove(Move move)
    {
        _chessBoard.movePiece(move.getFrom().getRow(), move.getFrom().getColumn(), move.getTo().getRow(),
                move.getTo().getColumn());
        System.out.println("");
        System.out.println(_chessBoard);
        if (_chessBoard.isKingDead()) {
            endGame();
            initGame();
        } else {
            _currentPlayer = getOtherPlayer();
        }
    }

    /**
     * Un moviment es considera vàlid si el jugador està movent la seva pròpia peça
     * i si el moviment es legal per les regles del joc i de la peça.
     *
     * @param move El moviment a de veure si les posiciones de inici i desti son legals i correctes
     * @return true es true si el moviment es valid, false sino no es legal o no cumpleix les normes.
     */

    public boolean isValidMove(Move move)
    {
        return isPlayerMovingItsOwnColoredPiece(move.getFrom())
               && _chessBoard.isValidMove(move.getFrom().getRow(), move.getFrom().getColumn(),
            move.getTo().getRow(), move.getTo().getColumn());
    }

    private boolean isPlayerMovingItsOwnColoredPiece(Position from) {
        return !_chessBoard.isEmpty(from)
               && _chessBoard.getPiece(from).getColor() == _currentPlayer.getColor();
    }

    public ChessBoard getChessBoard()
    {
        return _chessBoard;
    }

}
