import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class BonzeeBoard {
    public final String ANSI_RESET = "";
    public final String ANSI_RED = "R";
    public final String ANSI_GREEN = "G";

    boolean gameOver = false;
    boolean greenTurn = true;
    boolean validMove = false;
    int invalidAttempt = 0;
    int greenLeft = 22, redLeft = 22;
    int consecDefensiveMoves = 0;
    private AbstractToken[][] bonzeeBoard;




    public BonzeeBoard() {}

    public BonzeeBoard(BonzeeBoard bb){
        this.gameOver = bb.gameOver;
        this.greenTurn = bb.greenTurn;
        this.validMove = bb.validMove;
        this.invalidAttempt = bb.invalidAttempt;
        this.greenLeft = bb.greenLeft;
        this.redLeft = bb.redLeft;
        this.consecDefensiveMoves = bb.consecDefensiveMoves;
        this.bonzeeBoard = cloneBonzeeBoardArray(bb.bonzeeBoard);
    }

    private AbstractToken[][] cloneBonzeeBoardArray(AbstractToken[][] oldBoard){
        AbstractToken[][] newBoard = new AbstractToken[9][5];
        for (int j = 0; j < 5; j++){
            for (int i = 0; i < 9; i++){
                if (oldBoard[i][j] == null) {
                    newBoard[i][j] = null;
                }else if (oldBoard[i][j].getColor().equals("G")){
                    newBoard[i][j] = new GreenToken(oldBoard[i][j]);
                }else if (oldBoard[i][j].getColor().equals("R")){
                    newBoard[i][j] = new RedToken(oldBoard[i][j]);
                }
            }
        }
        return newBoard;
    }

    public AbstractToken[][] getBonzeeBoard() {
        return bonzeeBoard;
    }

    public void initializeBoard(){
        bonzeeBoard = new AbstractToken[9][5];
        // Red or Green tokens are placed on the board in their initial positions
        // each AbstractToken placed on the board has its position x, y attributes set accordingly
        for (int j = 0; j < 5; j++){
            for (int i = 0; i < 9; i++){
                // the two top rows of the board are filled with Red tokens
                if (j == 0 || j == 1){
                    AbstractToken token = new RedToken();
                    token.setPosX(i + 1);
                    token.setPosY(j + 1);
                    bonzeeBoard[i][j] = token;
                }
                // the left-hand half of the 3rd row is filled with Green tokens
                if (j == 2 && i <= 3){
                    AbstractToken token = new GreenToken();
                    token.setPosX(i + 1);
                    token.setPosY(j + 1);
                    bonzeeBoard[i][j] = token;
                }
                // the right-hand half of the 3rd row is filled with Red tokens
                if (j == 2 && i >= 5){
                    AbstractToken token = new RedToken();
                    token.setPosX(i + 1);
                    token.setPosY(j + 1);
                    bonzeeBoard[i][j] = token;
                }
                // the last two rows are filled with Green tokens
                if (j == 3 || j == 4){
                    AbstractToken token = new GreenToken();
                    token.setPosX(i + 1);
                    token.setPosY(j + 1);
                    bonzeeBoard[i][j] = token;
                }
            }
        }
    }

    public AbstractToken getByIndex(int x, int y) {
        return bonzeeBoard[x][y];
    }

    public AbstractToken getByTokenPosition( int posX, int posY){
        AbstractToken t;
        try{
            t = bonzeeBoard[posX - 1][posY - 1];
        }catch (ArrayIndexOutOfBoundsException aiobe){
            return null;
        }
        return t;
    }

    public void setByIndex(int x, int y, int newX, int newY){
        // change location of token on the game board
        bonzeeBoard[newX-1][newY-1] = bonzeeBoard[x-1][y-1];
        bonzeeBoard[x-1][y-1] = null;
        // set new coordinate attributes
        bonzeeBoard[newX-1][newY-1].setPosX(newX);
        bonzeeBoard[newX-1][newY-1].setPosY(newY);
    }

    public void setByIndex(int x, int y, AbstractToken token){
        bonzeeBoard[x][y] = token;
        token.setPosX(x);
        token.setPosY(y);
    }

    public void removeByIndex(int x, int y){
        if (bonzeeBoard[x-1][y-1].getColor() == "G"){
            greenLeft--;
        }else{
            redLeft--;
        }
        bonzeeBoard[x-1][y-1] = null;
    }

    public int letterToNumber(char letter) throws Exception{
        int number = 0;
        letter = Character.toLowerCase(letter);
        switch (letter){
            case 'a':
                number = 1;
                break;
            case 'b':
                number = 2;
                break;
            case 'c':
                number = 3;
                break;
            case 'd':
                number = 4;
                break;
            case 'e':
                number = 5;
                break;
            default:
                break;
        }
        return number;
    }

    public void displayBoard(AbstractToken[][] bonzeeBoard) {
//        return "BonzeeBoard{" +
//                "bonzeeBoard=" + Arrays.toString(bonzeeBoard) +
//                '}';
        StringBuilder bonzeeString = new StringBuilder(" \t\t  ");
        StringBuilder leadBlackCell = new StringBuilder("\t\t");
        StringBuilder leadWhiteCell = new StringBuilder("\t\t");

        // string representation of the number columns
        for (int i = 0; i < 9; i++) {
            bonzeeString.append((i + 1) + "   \t   ");
        }
        bonzeeString.append("\n\n");
        char letter = 'A';

        for (int j = 0; j < 5; j++) {
            // appending a string that represents black and white cells
            if (j > 0 && j%2==0){
                bonzeeString.append(leadBlackCell.toString() + "\n");
            }
            if (j%2!=0){
                bonzeeString.append(leadWhiteCell.toString() + "\n");
            }
            // string representation of the letter rows
            bonzeeString.append((letter) + "\t\t");
            letter++;
            for (int i = 0; i < 9; i++) {
                // black cells have x, y coordinates that are either both even or both odd
                if ((i%2==0 && j%2==0) || (i%2!=0 && j%2!=0)) {
                    if (getByIndex(i, j) == null) {
                        bonzeeString.append("\u2588\u2588\u2588\u2588\u2588\u2588\u2588\t");
                    } else {
                        bonzeeString.append("\u2588\u2588 " + getByIndex(i, j) + " \u2588\u2588\t");
                    }
                    // building a string to represent black and white cells
                    if (j==0) {
                        leadBlackCell.append("\u2588\u2588\u2588\u2588\u2588\u2588\u2588\t");
                    }
                    if (j==1){
                        leadWhiteCell.append("\u2588\u2588\u2588\u2588\u2588\u2588\u2588\t");
                    }
                }else{ // white cells have mismatched x, y coordinates
                    if (getByIndex(i, j) == null) {
                        bonzeeString.append("       \t");
                    } else {
                        bonzeeString.append("   " + getByIndex(i, j) + "   \t");
                    }
                    // building a string to represent black and white cells
                    if (j==0){
                        leadBlackCell.append("       \t");
                    }
                    if (j==1) {
                        leadWhiteCell.append("       \t");
                    }
                }
            }
            // even rows lead with a black cell
            if (j % 2 == 0) {
                bonzeeString.append("\n" + leadBlackCell.toString());
            }else{ // odd rows lead with a white cell
                bonzeeString.append("\n" + leadWhiteCell.toString());
            }
            bonzeeString.append("\n");
        }
        // more black and white cell formatting
        bonzeeString.insert(bonzeeString.indexOf("A"), leadBlackCell.toString() + "\n");
        bonzeeString.replace(bonzeeString.indexOf("B") - 3, bonzeeString.indexOf("B"), leadWhiteCell.toString() + "\n");

        System.out.println(bonzeeString.toString());

    }

    public Pair[] parseUserInput(String input) throws Exception {
        // check the format of the user input
        if (!input.matches("(\\s*)[a-eA-E]{1}(\\s*)[1-9]{1}(\\s*)[a-eA-E]{1}(\\s*)[1-9]{1}(\\s*)")){
            validMove = false;
            System.out.println("invalid input");
            return null;
        }else {
            // the coordinates entered are valid
            input = input.replace(" ", "");
            String start = input.substring(0, 2);
            String end = input.substring(2, 4);
            // coordinates of starting and end points are returned as pairs
            Pair<Integer, Integer> s = new Pair<>(letterToNumber(start.charAt(0)), Character.getNumericValue(start.charAt(1)));
            Pair<Integer, Integer> e = new Pair<>(letterToNumber(end.charAt(0)), Character.getNumericValue(end.charAt(1)));
            Pair[] result = new Pair[2];
            result[0] = s;
            result[1] = e;
            return result;
        }
    }

    public boolean isLegalMove(int startX, int startY, int endX, int endY){
        // if selecting an empty cell OR moving a token to an occupied cell
        if (getByTokenPosition(startX, startY) == null || getByTokenPosition(endX, endY) != null){
            validMove = false;
            return false;
        }
        // if a player attempts to move opponent's token
        if ((greenTurn && getByTokenPosition(startX, startY).color != "G") || (!greenTurn && getByTokenPosition(startX, startY).color != "R")){
            System.out.println("You may only move" + (!greenTurn? " red " : " green ") + "tokens");
            validMove = false;
            return false;
        }
        // if a player moves to a non adjacent cell
        if (Math.abs(startX - endX) > 1 || Math.abs(startY - endY) > 1){
            System.out.println("Tokens may only be moved to adjacent cells.");
            validMove = false;
        }
        // if a player moves a token diagonally from a white cell
        if (!getByTokenPosition(startX, startY).isOnBlackCell() && (startX != endX && startY != endY)){
            System.out.println("Tokens may not be moved diagonally from white cells.");
            validMove = false;
        }
        return true;
    }

    public boolean checkLegalMove(int startX, int startY, int endX, int endY){
        // if selecting an empty cell OR moving a token to an occupied cell
        if (getByTokenPosition(startX, startY) == null || getByTokenPosition(endX, endY) != null){
            return false;
        }
        // if a player attempts to move opponent's token
        if ((greenTurn && getByTokenPosition(startX, startY).color != "G") || (!greenTurn && getByTokenPosition(startX, startY).color != "R")){
            return false;
        }
        // if a player moves to a non adjacent cell
        if (Math.abs(startX - endX) > 1 || Math.abs(startY - endY) > 1){
            return false;
        }
        // if a player moves a token diagonally from a white cell
        if (!getByTokenPosition(startX, startY).isOnBlackCell() && (startX != endX && startY != endY)){
            return false;
        }
        return true;
    }

    public boolean isGameOver(){
        // check for a draw
        if (consecDefensiveMoves >= 10) {
            System.out.println("It's a draw! GAME OVER.");
            return true;
        }
        AbstractToken winner = null;
        for (int j = 0; j < 5; j++){
            for (int i = 0; i < 9; i++){
                // ignore empty cells
                if (bonzeeBoard[i][j] != null) {
                    // find the first token on the board and compare it to all others left
                    if (winner == null){
                        winner = bonzeeBoard[i][j];
                    }
                    // if there is a token of a different color from "winner" on the board
                    else if (winner.color != bonzeeBoard[i][j].color) {
                        return false;
                    }
                }
            }
        }
        // if all the tokens left are of the same color
        System.out.println("Congratulations! " + (winner.color == "G"? ANSI_GREEN + "GREEN" + ANSI_RESET : ANSI_RED + "RED" + ANSI_RESET) + " won!");
        return true;
    }

    private String prettyCoordinate(int y){
        String coord = "";
        switch(y)
        {
            case 1: coord = "A";
            break;
            case 2: coord = "B";
                break;
            case 3: coord = "C";
                break;
            case 4: coord = "D";
                break;
            case 5: coord = "E";
                break;
        }
        return coord;
    }


    public void startGame(){

        initializeBoard();
        displayBoard(bonzeeBoard);
        System.out.println("Green" + ANSI_RESET + " plays first. ");

        int startX = 0, startY = 0, endX = 0, endY = 0;
        while (!gameOver){
            System.out.print("Please enter your next move.\n");
            validMove = true;

            // parse user input for the next move
            Scanner scanner = new Scanner(System.in);
            String nextMove = scanner.nextLine();
            try {
                Pair<Integer, Integer>[] coordinates = parseUserInput(nextMove);
                startX = coordinates[0].second;
                startY = coordinates[0].first;
                endX = coordinates[1].second;
                endY = coordinates[1].first;
            } catch (Exception e) {
                validMove = false;
            }

            // make sure the move is legal (ie. token moved by right player, from a position with a token to an adjacent empty position)
            // if illegal move, boolean validMove will be set to false
            validMove = isLegalMove(startX, startY, endX, endY);

            // if the move was successfully parsed to valid values
            if (validMove) {
                moveToken(startX, startY, endX, endY);
                // display the new state of the game board
                displayBoard(bonzeeBoard);
                if (!greenTurn) {
                    System.out.println("GREEN" + ANSI_RESET + " moved from " +  prettyCoordinate(startY) + Integer.toString(startX) + " to " + prettyCoordinate(endY) + Integer.toString(endX) + ".");
                } else {
                    System.out.println("RED" + ANSI_RESET + " moved from " +  prettyCoordinate(startY) + Integer.toString(startX) + " to " + prettyCoordinate(endY) + Integer.toString(endX) + ".");
                }
                // check for a winner
                if (isGameOver()){
                    System.exit(0);
                    break;
                }


                if (greenTurn) {
                    System.out.println("It is now " + "GREEN" + ANSI_RESET + "\'s turn to play. ");
                } else {
                    System.out.println("It is now " + "RED" + ANSI_RESET + "\'s turn to play. ");
                }

                // pass the state of board after GREEN player's move, as well as the coordinates of that move (in the form Pair[])
                AIPlayerMinimax opponent = new AIPlayerMinimax(new BonzeeBoard(this), new Pair[]{new Pair(startX, startY), new Pair(endX, endY)});
                Pair<Integer, Integer>[] aiMove = opponent.minimax(3, "RED");
                moveToken(aiMove[0].first, aiMove[0].second, aiMove[1].first, aiMove[1].second);


                if (greenTurn) {
                    System.out.println("It is now " + "GREEN" + ANSI_RESET + "\'s turn to play. ");
                } else {
                    System.out.println("It is now " + "RED" + ANSI_RESET + "\'s turn to play. ");
                }

                // display the new state of the game board
                displayBoard(bonzeeBoard);

                // check for a winner
                if (isGameOver()){
                    System.exit(0);
                    break;
                }

            }else if (invalidAttempt < 1){
                System.out.println("Invalid move. Please try again.");
                invalidAttempt++;
            }else{
                gameOver = true;
                System.out.println("Too many attempted invalid moves. Game Over.");
            }
        }
    }

    public BonzeeBoard moveToken(int startX, int startY, int endX, int endY){
        // adds previous state to the stack
        //previousStates.push(new BonzeeBoard(gameOver, greenTurn, validMove, invalidAttempt, greenLeft, redLeft, consecDefensiveMoves, bonzeeBoard, previousStates));
        //previousStates.push(this);
        invalidAttempt = 0;     // user is given two attempts to enter a valid move

        // Single out token to be moved from the board (2D array)
        AbstractToken movingToken = getByTokenPosition(startX, startY);
        // Determine if token is being moved horizontally, vertically or diagonally
        movingToken.setDirection(determineDirection(startX, startY, endX, endY));

        int previousGreenLeft = greenLeft, previousRedLeft = redLeft;
        // Token will attack or defend itself, depending on where and how it moves on the board
        play(movingToken, endX, endY);

        if (greenTurn){
            // if none of the opponents were removed following a move, count as a defensive move
            // else reset number of consecutive moves to zero
            if (previousRedLeft == redLeft){
                consecDefensiveMoves++;
            }else{
                consecDefensiveMoves = 0;
            }
        }else{
            // if none of the opponents were removed following a move, count as a defensive move
            // else reset number of consecutive moves to zero
            if (previousGreenLeft == greenLeft){
                consecDefensiveMoves++;
            }else{
                consecDefensiveMoves = 0;
            }
        }
        greenTurn = !greenTurn; // TODO: check to see if works
        return this;
    }

    public void play(AbstractToken token, int newPosX, int newPosY){
        switch (token.direction){
            case "horizontal":
                // FORWARD
                // if attacking token moves to the left, and an opponent's token is adjacent to that new position
                if ((token.posX - newPosX > 0) &&                                                                       // LEFT move
                        (getByTokenPosition(newPosX - 1, newPosY) != null) &&
                        !token.getColor().equals((getByTokenPosition(newPosX - 1, newPosY) == null)? "" : getByTokenPosition(newPosX - 1, newPosY).getColor())){
                    forwardAttack(token, newPosX, newPosY, "LEFT");
                }
                // if attacking token moves to the right, and an opponent's token is adjacent to that new position
                else if ((token.posX - newPosX < 0) &&                                                                  // RIGHT move
                        (getByTokenPosition(newPosX + 1, newPosY) != null) &&
                        !token.getColor().equals((getByTokenPosition(newPosX + 1, newPosY) == null)? "" : getByTokenPosition(newPosX + 1, newPosY).getColor())){
                    forwardAttack(token, newPosX, newPosY, "RIGHT");
                }
                // BACKWARD
                // if attacking token moves to the left, and an opponent's token is one cell away from that new position
                else if ((token.posX - newPosX > 0) &&
                        !token.getColor().equals((getByTokenPosition(newPosX + 2, newPosY) == null)? "" : getByTokenPosition(newPosX + 2, newPosY).getColor())){                     // LEFT move
                    backwardAttack(token, newPosX, newPosY, "LEFT");
                }
                // if attacking token moves to the right, and an opponent's token is one cell away from that new position
                else if ((token.posX - newPosX < 0) &&
                        !token.getColor().equals((getByTokenPosition(newPosX - 2, newPosY) == null)? "" : getByTokenPosition(newPosX - 2, newPosY).getColor())){                     // RIGHT move
                    backwardAttack(token, newPosX, newPosY, "RIGHT");
                }
                // in any other case, the move is defensive (the token simply changes location)
                else{
                    defend(token, newPosX, newPosY);
                }
                break;
            case "vertical":
                // FORWARD
                // if attacking token moves up, and an opponent's token is adjacent to that new position
                if ((token.posY - newPosY > 0) &&                                                                       // UP move
                        (getByTokenPosition(newPosX, newPosY - 1) != null) &&
                        !token.getColor().equals((getByTokenPosition(newPosX, newPosY - 1) == null)? "": getByTokenPosition(newPosX, newPosY - 1).getColor())){
                    forwardAttack(token, newPosX, newPosY, "UP");
                }
                // if attacking token moves down, and an opponent's token is adjacent to that new position
                else if ((token.posY - newPosY < 0) &&                                                                  // DOWN move
                        (getByTokenPosition(newPosX, newPosY + 1) != null) &&
                        !token.getColor().equals((getByTokenPosition(newPosX, newPosY + 1) == null)? "": getByTokenPosition(newPosX, newPosY + 1).getColor())){
                    forwardAttack(token, newPosX, newPosY, "DOWN");
                }
                // BACKWARD
                // if attacking token moves up, and an opponent's token is one cell away from that new position
                else if ((token.posY - newPosY > 0) &&
                        !token.getColor().equals((getByTokenPosition(newPosX, newPosY + 2) == null)? "": getByTokenPosition(newPosX, newPosY + 2).getColor())){                      // UP move
                    backwardAttack(token, newPosX, newPosY, "UP");
                }
                // if attacking token moves down, and an opponent's token is one cell away from that new position
                else if ((token.posY - newPosY < 0) &&
                        !token.getColor().equals((getByTokenPosition(newPosX, newPosY - 2) == null)? "": getByTokenPosition(newPosX, newPosY - 2).getColor())){                      // DOWN move
                    backwardAttack(token, newPosX, newPosY, "DOWN");
                }
                // in any other case, the move is defensive (the token simply changes location)
                else{
                    defend(token, newPosX, newPosY);
                }
                break;
            case "diagonal":
                // FORWARD
                // if attacking token moves up to its left, and an opponent's token is adjacent to that new position
                if ((token.posX - newPosX > 0 && token.posY - newPosY > 0) &&                                                   // TOP LEFT move
                        (getByTokenPosition(newPosX - 1, newPosY - 1) != null) &&
                        !token.getColor().equals((getByTokenPosition(newPosX - 1, newPosY - 1) == null)? "" : getByTokenPosition(newPosX - 1, newPosY - 1).getColor())){//getByTokenPosition()(newPosX - 1, newPosY - 1).getColor()) || getByTokenPosition()(newPosX - 1, newPosY - 1) != null)){
                    forwardAttack(token, newPosX, newPosY, "TOP-LEFT");
                }
                // if attacking token moves up to its right, and an opponent's token is adjacent to that new position
                else if ((token.posX - newPosX < 0 && token.posY - newPosY > 0) &&                                              // TOP RIGHT move
                        (getByTokenPosition(newPosX + 1, newPosY - 1) != null) &&
                        !token.getColor().equals((getByTokenPosition(newPosX + 1, newPosY - 1) == null)? "": getByTokenPosition(newPosX + 1, newPosY - 1).getColor())){
                    forwardAttack(token, newPosX, newPosY, "TOP-RIGHT");
                }
                // if attacking token moves down to its left, and an opponent's token is adjacent to that new position
                else if ((token.posX - newPosX > 0 && token.posY - newPosY < 0) &&                                              // BOTTOM LEFT move
                        (getByTokenPosition(newPosX - 1, newPosY + 1) != null) &&
                        !token.getColor().equals((getByTokenPosition(newPosX - 1, newPosY + 1) == null)? "": getByTokenPosition(newPosX - 1, newPosY + 1).getColor())){
                    forwardAttack(token, newPosX, newPosY, "BOTTOM-LEFT");
                }
                // if attacking token moves down to its right, and an opponent's token is adjacent to that new position
                else if ((token.posX - newPosX < 0 && token.posY - newPosY < 0) &&                                              // BOTTOM RIGHT move
                        (getByTokenPosition(newPosX + 1, newPosY + 1) != null) &&
                        !token.getColor().equals((getByTokenPosition(newPosX + 1, newPosY + 1) == null)? "": getByTokenPosition(newPosX + 1, newPosY + 1).getColor())){
                    forwardAttack(token, newPosX, newPosY, "BOTTOM-RIGHT");
                }
                // BACKWARD
                // if attacking token moves up to its left, and an opponent's token is one cell away from that new position
                else if ((token.posX - newPosX > 0 && token.posY - newPosY > 0) &&                                                   // TOP LEFT move
                        !token.getColor().equals((getByTokenPosition(newPosX + 2, newPosY + 2) == null)? "": getByTokenPosition(newPosX + 2, newPosY + 2).getColor())){
                    backwardAttack(token, newPosX, newPosY, "TOP-LEFT");
                }
                // if attacking token moves up to its right, and an opponent's token is one cell away from that new position
                else if ((token.posX - newPosX < 0 && token.posY - newPosY > 0) &&                                              // TOP RIGHT move
                        !token.getColor().equals((getByTokenPosition(newPosX - 2, newPosY + 2) == null)? "": getByTokenPosition(newPosX - 2, newPosY + 2).getColor())){
                    backwardAttack(token, newPosX, newPosY, "TOP-RIGHT");
                }
                // if attacking token moves down to its left, and an opponent's token is one cell away from that new position
                else if ((token.posX - newPosX > 0 && token.posY - newPosY < 0) &&                                              // BOTTOM LEFT move
                        !token.getColor().equals((getByTokenPosition(newPosX + 2, newPosY - 2) == null)? "": getByTokenPosition(newPosX + 2, newPosY - 2).getColor())){
                    backwardAttack(token, newPosX, newPosY, "BOTTOM-LEFT");
                }
                // if attacking token moves down to its right, and an opponent's token is one cell away from that new position
                else if ((token.posX - newPosX < 0 && token.posY - newPosY < 0) &&                                              // BOTTOM RIGHT move
                        !token.getColor().equals((getByTokenPosition(newPosX - 2, newPosY - 2) == null)? "": getByTokenPosition(newPosX - 2, newPosY - 2).getColor())){
                    backwardAttack(token, newPosX, newPosY, "BOTTOM-RIGHT");
                }
                // in any other case, the move is defensive (the token simply changes location)
                else{
                    defend(token, newPosX, newPosY);
                }
                break;
            default:
                break;
        }
    }

    public void defend(AbstractToken token, int newPosX, int newPosY){
        move(token, newPosX, newPosY);
    }

    public void move(AbstractToken token, int newPosX, int newPosY){
        setByIndex(token.posX, token.posY, newPosX, newPosY);

    }

    public String determineDirection(int posX, int posY, int newPosX, int newPosY){
        String direction = "";
        if (posX == newPosX){
            direction = "vertical";
        }
        else if(posY == newPosY){
            direction = "horizontal";
        }
        else{
            direction = "diagonal";
        }
        return direction;
    }

    public void forwardAttack(AbstractToken token, int newPosX, int newPosY, String direction){
        move(token, newPosX, newPosY);

        int m, n;
        switch (direction) {
            // horizontal
            case "LEFT":
                int count = newPosX - 1;
                while (getByTokenPosition(count, newPosY) != null && count > 0) {
                    if (token.color != getByTokenPosition(count, newPosY).getColor()) {
                        removeByIndex(count, newPosY);
                        count--;
                    }else{
                        break;
                    }
                }
                break;
            case "RIGHT":
                count = newPosX + 1;
                while (getByTokenPosition(count, newPosY) != null && count <= 9) {
                    if (token.color != getByTokenPosition(count, newPosY).getColor()) {
                        removeByIndex(count, newPosY);
                        count++;
                    }else{
                        break;
                    }
                }
                break;

            // vertical
            case "UP":
                count = newPosY - 1;
                while (getByTokenPosition(newPosX, count) != null && count > 0) {
                    if (token.color != getByTokenPosition(newPosX, count).getColor()) {
                        removeByIndex(newPosX, count);
                        count--;
                    }else{
                        break;
                    }
                }
                break;
            case "DOWN":
                count = newPosY + 1;
                while (getByTokenPosition(newPosX, count) != null && count <= 5){
                    if (token.color != getByTokenPosition(newPosX, count).getColor()) {
                        removeByIndex(newPosX, count);
                        count++;
                    }else{
                        break;
                    }
                }
                break;

            // diagonal
            case "TOP-LEFT":
                m = newPosX - 1;
                n = newPosY - 1;
                while (getByTokenPosition(m, n) != null) {
                    if ((m > 0 && n > 0) && token.color != getByTokenPosition(m, n).getColor()) {
                        removeByIndex(m, n);
                        m--;
                        n--;
                    }else{
                        break;
                    }
                }
                break;
            case "TOP-RIGHT":
                m = newPosX + 1;
                n = newPosY - 1;
                while (getByTokenPosition(m, n) != null) {
                    if ((m <= 9 && n > 0) && token.color != getByTokenPosition(m, n).getColor()) {
                        removeByIndex(m, n);
                        m++;
                        n--;
                    }else{
                        break;
                    }
                }
                break;
            case "BOTTOM-LEFT":
                m = newPosX - 1;
                n = newPosY + 1;
                while (getByTokenPosition(m, n) != null) {
                    if ((m > 0 && n <= 5) && token.color != getByTokenPosition(m, n).getColor()) {
                        removeByIndex(m, n);
                        m--;
                        n++;
                    }else{
                        break;
                    }
                }
                break;
            case "BOTTOM-RIGHT":
                m = newPosX + 1;
                n = newPosY + 1;
                while (getByTokenPosition(m, n) != null) {
                    if ((m <= 9 && n <= 5) && token.color != getByTokenPosition(m, n).getColor()) {
                        removeByIndex(m, n);
                        m++;
                        n++;
                    }else{
                        break;
                    }
                }
                break;
            default:
                break;
        }
    }

    public void backwardAttack(AbstractToken token, int newPosX, int newPosY, String direction){
        move(token, newPosX, newPosY);

        int m, n;
        switch (direction) {
            // horizontal
            case "LEFT":
                int count = newPosX + 2;
                while (getByTokenPosition(count, newPosY) != null && count <= 9) {
                    if (token.color != getByTokenPosition(count, newPosY).getColor()) {
                        removeByIndex(count, newPosY);
                        count++;
                    }else{
                        break;
                    }
                }
                break;
            case "RIGHT":
                count = newPosX - 2;
                while (getByTokenPosition(count, newPosY) != null && count > 0) {
                    if (token.color != getByTokenPosition(count, newPosY).getColor()) {
                        removeByIndex(count, newPosY);
                        count--;
                    }else{
                        break;
                    }
                }
                break;

            // vertical
            case "UP":
                count = newPosY + 2;
                while (getByTokenPosition(newPosX, count) != null && count <= 5) {
                    if (token.color != getByTokenPosition(newPosX, count).getColor()) {
                        removeByIndex(newPosX, count);
                        count++;
                    }else{
                        break;
                    }
                }
                break;
            case "DOWN":
                count = newPosY - 2;
                while (getByTokenPosition(newPosX, count) != null && count > 0) {
                    if (token.color != getByTokenPosition(newPosX, count).getColor()) {
                        removeByIndex(newPosX, count);
                        count--;
                    }else{
                        break;
                    }
                }
                break;

            // diagonal
            case "TOP-LEFT":
                m = newPosX + 2;
                n = newPosY + 2;
                while (getByTokenPosition(m, n) != null){
                    if ((m <= 9 && n <= 5) && token.color != getByTokenPosition(m, n).getColor()) {
                        removeByIndex(m, n);
                        m++;
                        n++;
                    }else{
                        break;
                    }
                }
                break;
            case "TOP-RIGHT":
                m = newPosX - 2;
                n = newPosY + 2;
                while (getByTokenPosition(m, n) != null){
                    if ((m > 0 && n <= 5) && token.color != getByTokenPosition(m, n).getColor()){
                        removeByIndex(m, n);
                        m--;
                        n++;
                    }else{
                        break;
                    }
                }
                break;
            case "BOTTOM-LEFT":
                m = newPosX + 2;
                n = newPosY - 2;
                while (getByTokenPosition(m, n) != null){
                    if ((m <= 9 && n > 0) && token.color != getByTokenPosition(m, n).getColor()) {
                        removeByIndex(m, n);
                        m++;
                        n--;
                    }else{
                        break;
                    }
                }
                break;
            case "BOTTOM-RIGHT":
                m = newPosX - 2;
                n = newPosY - 2;
                while (getByTokenPosition(m, n) != null){
                    if ((m > 0 && n > 0) && token.color != getByTokenPosition(m, n).getColor()) {
                        removeByIndex(m, n);
                        m--;
                        n--;
                    }else{
                        break;
                    }
                }
                break;
            default:
                break;
        }
    }

    public int[] evaluate(){
        int greenScore = 0;
        int redScore = 0;

        for (int j = 0; j < 5; j++){
            for (int i = 0; i < 9; i++){
                if (bonzeeBoard[i][j].getColor().equals("G")){
                    greenScore += i * 50;
                    greenScore += j * 100;
                }
                if (bonzeeBoard[i][j].getColor().equals("R")){
                    redScore += i * -50;
                    redScore += j * -100;
                }
            }
        }
        return new int[]{greenScore, redScore};
    }

    public static void main(String[] args){
        BonzeeBoard bb = new BonzeeBoard();
        bb.startGame();
    }
}
