package com.example.chessappgroupd.stockfish;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;


// note: this class has been created using this source: https://www.educative.io/answers/how-to-get-a-current-working-directory-in-java and ChatGPT


public class StockfishWrapper {

    //        "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR": This part represents the current arrangement of pieces on the chessboard.
//        In FEN notation, lowercase letters represent black pieces, and uppercase letters represent white pieces. For example, "r" is a black rook, "P" is a white pawn, and "8" represents an empty space.
//        The string represents the starting position of a chess game.
//            "w": This indicates that it's white's turn to move.
//            "KQkq": These are flags for castling availability.
//            In this example, it means that white can castle kingside (on the kingside, short castling), kingside (on the queenside, long castling), black can castle kingside, and black can castle queenside.
//            "-": This indicates that there are no en passant target squares (no valid squares for an en passant capture).
//            "0": This represents the half-move clock, which is the number of half-moves (plies) since the last pawn move or capture. It's often set to "0" in the initial position.
//            "1": This is the full move number, representing the number of the current move. It's set to "1" for the starting position.
    private Process stockfishProcess;
    private BufferedReader engineIn;
    private PrintWriter engineOut;


//    // running only in tests bc path differs!
//
//    public StockfishWrapper() {
//        String linuxPath = "/usr/app/stockfish-ubuntu-x86-64-modern";
//        Path RelativePath = Paths.get(""); // Initialize RelativePath
//        String windowsPath = RelativePath.toAbsolutePath().toString() + "\\Backend\\chess-app-group-d\\src\\main\\java\\com\\example\\chessappgroupd\\stockfish\\stockfish-windows-x86-64-modern";
//        String stockfishPath = isExecutableAvailable(linuxPath) ? linuxPath : windowsPath;
//        String os = System.getProperty("os.name");
//
//        if (os.contains("Windows") || os.contains("windows")) {
//            //executed in windows
//            try {
//                Path relativePath = Paths.get("");
//                String fallbackPath = relativePath.toAbsolutePath().toString() + "\\src\\main\\java\\com\\example\\chessappgroupd\\stockfish\\stockfish-windows-x86-64-modern";
//                startStockfish(fallbackPath);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            //executed in docker
//            try {
//                stockfishProcess = Runtime.getRuntime().exec(stockfishPath);
//                engineIn = new BufferedReader(new InputStreamReader(stockfishProcess.getInputStream()));
//                engineOut = new PrintWriter(stockfishProcess.getOutputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

    //running in app
    public StockfishWrapper() {
        String linuxPath = "/usr/app/stockfish-ubuntu-x86-64-modern";
        Path RelativePath = Paths.get(""); // Initialize RelativePath
        String windowsPath = RelativePath.toAbsolutePath().toString() + "\\Backend\\chess-app-group-d\\src\\main\\java\\com\\example\\chessappgroupd\\stockfish\\stockfish-windows-x86-64-modern";
        String stockfishPath = isExecutableAvailable(linuxPath) ? linuxPath : windowsPath;

        try {
            stockfishProcess = Runtime.getRuntime().exec(stockfishPath);
            engineIn = new BufferedReader(new InputStreamReader(stockfishProcess.getInputStream()));
            engineOut = new PrintWriter(stockfishProcess.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startStockfish(String stockfishPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(stockfishPath);
        stockfishProcess = processBuilder.start();
        engineIn = new BufferedReader(new InputStreamReader(stockfishProcess.getInputStream()));
        engineOut = new PrintWriter(stockfishProcess.getOutputStream());
    }

    private boolean isExecutableAvailable(String executablePath) {
        File file = new File(executablePath);
        return file.exists() && file.canExecute();
    }


    public String getBestMove(String fenPosition, int searchDepth) {
        sendCommand("position fen " + fenPosition);
        sendCommand("go depth " + searchDepth);

        return waitForBestMove();
    }

    private void sendCommand(String command) {
        engineOut.println(command);
        engineOut.flush();
    }

    private String waitForBestMove() {
        String line;
        StringBuilder bestMove = new StringBuilder();

        try {
            while ((line = engineIn.readLine()) != null) {
                if (line.startsWith("bestmove")) {
                    bestMove.append(line);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bestMove.toString();
    }

    public void stop() {
        sendCommand("quit");
        stockfishProcess.destroy();
    }
}
