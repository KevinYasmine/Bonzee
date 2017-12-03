package BonzeeGame;

import BonzeeGame.BonzeeBoard;
import BonzeeGame.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Node {

	Pair<Integer, Integer>[] coordinates;
	private String id;
	BonzeeBoard boardState;
	int score = 0;
	String parentID;
	String bestChildID;
	ArrayList<String> children;
	int depth;
	
	public Node(String id, Pair<Integer, Integer>[] coordinates, BonzeeBoard bb) {
	    this.id = id;
	    this.coordinates = coordinates;
	    this.boardState = bb;
	    this.score = score;
		children = new ArrayList<String>();
	}

	public Node(String id, Pair<Integer, Integer>[] coordinates, BonzeeBoard bb, String parentID) {
		this.id = id;
		this.coordinates = coordinates;
		this.boardState = bb;
		this.score = score;
		this.parentID = parentID;
		children = new ArrayList<String>();
	}

	@Override
	public String toString() {
		return "Node{" +
				Arrays.toString(coordinates) +
				", id='" + id.substring(0, 7) + '\'' +
				"} " + (!boardState.greenTurn? "GREEN" : "RED") +
				" depth: " + depth +
				", score: " + score +
				"\t parentID: " + parentID +
				" best child: " + bestChildID;
	}

	public Node(String id, BonzeeBoard boardState) {
		this.id = id;
		this.boardState = boardState;
	}

	public Pair[] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Pair[] coordinates) {
		this.coordinates = coordinates;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BonzeeBoard getBoardState() {
		return boardState;
	}

	public void setBoardState(BonzeeBoard boardState) {
		this.boardState = boardState;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public ArrayList<String> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<String> children) {
		this.children = children;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public void addChild(String id) {
		children.add(id);
	}

	// will generate all of the coordinates of possible moves at this state of the game (according to the game board this node holds)
	public List<Pair<Integer, Integer>[]> findChildren(){
		List<int[]> emptyCells = new ArrayList<int[]>();
		List<AbstractToken> tokens = new ArrayList<AbstractToken>();
		List<Pair<Integer, Integer>[]> nextMoves = new ArrayList<Pair<Integer, Integer>[]>();

		if (boardState.isGameOver()){
			return nextMoves;       // return empty list
		}

		for (int j = 0; j < 5; j++){
			for (int i = 0; i < 9; i++){
				if (boardState.getBonzeeBoard()[i][j] == null){
					// find all empty cells
					emptyCells.add(new int[] {i+1, j+1});
				}else{
					// find all tokens on the board
					tokens.add(boardState.getBonzeeBoard()[i][j]);
				}
			}
		}
		// try to move every token to each empty cell to see if the move is valid (if valid add to nextMoves)
		int emptyCellsIndex = 0;
		int tokensIndex = 0;
		while (tokensIndex < tokens.size()){
			while (emptyCellsIndex < emptyCells.size()){
				if (boardState.checkLegalMove(tokens.get(tokensIndex).posX, tokens.get(tokensIndex).posY, emptyCells.get(emptyCellsIndex)[0], emptyCells.get(emptyCellsIndex)[1])){
					Pair[] pair = new Pair[] {new Pair(tokens.get(tokensIndex).posX, tokens.get(tokensIndex).posY), new Pair(emptyCells.get(emptyCellsIndex)[0], emptyCells.get(emptyCellsIndex)[1])};
					nextMoves.add(pair);
				}
				emptyCellsIndex++;
			}
			emptyCellsIndex = 0;
			tokensIndex++;
		}
		return nextMoves;
	}
	
	public void evaluate(){
        int greenScore = 0;
        int redScore = 0;

        for (int j = 0; j < 5; j++){
            for (int i = 0; i < 9; i++){
            	if (boardState.getBonzeeBoard()[i][j] == null){
            		break;
				}
                else if (boardState.getBonzeeBoard()[i][j].getColor().equals("G")){
                    greenScore += i * 50;
                    greenScore += j * 100;
                }
                else if (boardState.getBonzeeBoard()[i][j].getColor().equals("R")){
                    redScore += i * -50;
                    redScore += j * -100;
                }
            }
        }
        this.score = greenScore + redScore;
    }
}
