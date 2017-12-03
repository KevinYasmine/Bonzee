import java.util.*;

public class AIPlayerMinimax {

    public BonzeeBoard board;
    public AbstractToken[][] boardTokens;
    String maxPlayer = "RED";
    String minPlayer = "GREEN";
    int greenCount, redCount;
    Pair[] previousMove;


    public AIPlayerMinimax(BonzeeBoard bonzeeBoard){
        this.board = bonzeeBoard;
        this.boardTokens = bonzeeBoard.getBonzeeBoard();
        this.greenCount = board.greenLeft;
        this.redCount = board.redLeft;
    }

    public AIPlayerMinimax(BonzeeBoard bonzeeBoard, Pair[] previousMove){
        this.board = bonzeeBoard;
        this.boardTokens = bonzeeBoard.getBonzeeBoard();
        this.greenCount = board.greenLeft;
        this.redCount = board.redLeft;
        this.previousMove = previousMove;
    }


    public List<Pair<Integer, Integer>[]> generateMoves(){
        List<int[]> emptyCells = new ArrayList<int[]>();
        List<AbstractToken> tokens = new ArrayList<AbstractToken>();
        List<Pair<Integer, Integer>[]> nextMoves = new ArrayList<Pair<Integer, Integer>[]>();

        if (board.isGameOver()){
            return nextMoves;       // return empty list
        }

        for (int j = 0; j < 5; j++){
            for (int i = 0; i < 9; i++){
                if (boardTokens[i][j] == null){
                    emptyCells.add(new int[] {i+1, j+1});
                }else{
                    tokens.add(boardTokens[i][j]);
                }
            }
        }
        // try to move every token to each empty cell to see if the move if valid (if valid add to nextMoves)
        int emptyCellsIndex = 0;
        int tokensIndex = 0;
        while (tokensIndex < tokens.size()){
            while (emptyCellsIndex < emptyCells.size()){
                if (board.checkLegalMove(tokens.get(tokensIndex).posX, tokens.get(tokensIndex).posY, emptyCells.get(emptyCellsIndex)[0], emptyCells.get(emptyCellsIndex)[1])){
                    Pair[] pair = new Pair[] {new Pair(tokens.get(tokensIndex).posX, tokens.get(tokensIndex).posY), new Pair(emptyCells.get(emptyCellsIndex)[0], emptyCells.get(emptyCellsIndex)[1])};
                    nextMoves.add(pair);
                }
                emptyCellsIndex++;
            }
            emptyCellsIndex = 0;
            tokensIndex++;
        }
//        for (int j = 0; j < emptyCells.size(); j++){
//            for (int i = 0; i < tokens.size(); i++){
//                if (board.checkLegalMove(tokens.get(i).posX, tokens.get(i).posY, emptyCells.get(j)[0], emptyCells.get(j)[0])){
//                    nextMoves.add(new Pair[] {new Pair(tokens.get(i).posX, tokens.get(i).posY), new Pair(emptyCells.get(j)[0], emptyCells.get(j)[1])});
//                }
//            }
//        }
        return nextMoves;
    }

    public Pair<Integer, Integer>[] minimax(int depth, String player) {
        Tree boardMap = generateTree();
        //Pair<Pair<Integer, Integer>[], Integer> result = miniMaxAlgorithm(boardMap.getNode(boardMap.getRootID()),3, maxPlayer, boardMap);
        List<Node> root = new ArrayList<>();                                                    // make a list containing just the root node
        root.add(boardMap.getTreeMap().get(boardMap.rootID));
        Pair<Pair<Integer, Integer>[], Integer> result = miniMaxAlgorithm(3, boardMap);
        return result.first;
    }

    private Tree generateTree() {
        Tree tree = new Tree();
        // set root: pass the board (after GREEN has played)
        String rootID = UUID.randomUUID().toString();
        Node root = tree.addNode(rootID, rootID, previousMove, board); // level 0
        root.depth = 0;
        //System.out.println("depth 0: \n" + root);

        // build 3 levels down
        List<Node> nodes = tree.generateChildren(root);                             // level 1
        //System.out.println("depth 1:");

        List<Node> levelThreeNodes = new ArrayList<>();
        for (Node node : nodes){
            node.depth = 1;
            //System.out.println(node);
            List<Node> subtreeNodes = tree.generateChildren(node);                  // level 2
            // for every child added to a parent node, add that child to the list levelThreeNodes
            levelThreeNodes.addAll(subtreeNodes);
            // formerly:
//            for (Node n : subtreeNodes){
//                levelThreeNodes.add(n);
//            }
        }

        //System.out.println("depth 2:");
        List<Node> leafNodes = new ArrayList<>();
        for (Node node : levelThreeNodes){
            node.depth = 2;
            //System.out.println(node);
            List<Node> subtreeNodes = tree.generateChildren(node);                  // level 3
            // for every child added to a parent node, add that child to the list leafNodes
            leafNodes.addAll(subtreeNodes);
            //formerly:
//            for (Node n : subtreeNodes){
//                leafNodes.add(n);
//            }

        }

        ////System.out.println("depth 3:");
        for (Node n : leafNodes){
            n.depth = 3;
            n.evaluate();
            ////System.out.println(n);
        }

    	return tree;
    }

    private Pair<Integer,Integer>[] miniMaxAlgorithm(Tree boardMap) {
        return null;
    }


    private Pair<Pair<Integer, Integer>[], Integer> miniMaxAlgorithm(int depth, Tree tree){
        List<Node> levelNodes = tree.nodesAtLevel(depth);

        // if a node part of levelNodes does not have children (List<String> children is empty), bestScore = node.getScore
        int bestScore = levelNodes.get(0).getBoardState().greenTurn? Integer.MAX_VALUE : Integer.MIN_VALUE;
        int currentScore;
        Pair<Integer, Integer> bestStartToken = new Pair<Integer, Integer>(-1, -1);
        Pair<Integer, Integer> bestEndToken = new Pair<Integer, Integer>(-1, -1);

        if (levelNodes.isEmpty() || depth == 0) {               // leaf nodes
            // Game over or depth reached, evaluate score
//            bestScore = tree.getTreeMap().get(tree.rootID).score; // the final score that was percolated up to the root (?)
//            bestStartToken = new Pair<Integer, Integer>(tree.getTreeMap().get(tree.getTreeMap().get(tree.rootID).bestChildID).coordinates[0].first, tree.getTreeMap().get(tree.getTreeMap().get(tree.rootID).bestChildID).coordinates[0].second);
//            bestEndToken = new Pair<Integer, Integer>(tree.getTreeMap().get(tree.getTreeMap().get(tree.rootID).bestChildID).coordinates[1].first, tree.getTreeMap().get(tree.getTreeMap().get(tree.rootID).bestChildID).coordinates[1].second);
            for (Node node : levelNodes){
                node.evaluate();            // a priori, at leaf node
                bestScore = node.score;
            }
        }else{
            for (Node node : levelNodes){
                if (!node.getBoardState().greenTurn) {   // RED, MAX -- will choose move played by MIN
                    //bestScore = Integer.MIN_VALUE;
                    //currentScore = miniMaxAlgorithm(depth - 1, tree).second;
                    currentScore = miniMaxAlgorithm(depth - 1, tree).second;
                    if (currentScore > bestScore) { // bestScore = Integer.MIN_VALUE at first
                        bestScore = currentScore;
                        tree.getTreeMap().get(node.parentID).score = bestScore;                 // update the parent node with the best score (so far)
                        tree.getTreeMap().get(node.parentID).bestChildID = node.getId();        // let parent know which child is best
                        bestStartToken = new Pair<Integer, Integer>(node.coordinates[0].first, node.coordinates[0].second);
                        bestEndToken = new Pair<Integer, Integer>(node.coordinates[1].first, node.coordinates[1].second);
                    }
                } else {                                // GREEN, MIN
                    //currentScore = miniMaxAlgorithm(depth - 1, tree).second;
                   // bestScore = Integer.MAX_VALUE;
                    currentScore = miniMaxAlgorithm(depth - 1, tree).second;
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        tree.getTreeMap().get(node.parentID).score = bestScore;
                        tree.getTreeMap().get(node.parentID).bestChildID = node.getId();
                        bestStartToken = new Pair<Integer, Integer>(node.coordinates[0].first, node.coordinates[0].second);
                        bestEndToken = new Pair<Integer, Integer>(node.coordinates[1].first, node.coordinates[1].second);
                    }
                }
            }
//            miniMaxAlgorithm(depth - 1, tree);
        }
//
//        for (Node node : levelNodes){
//            // if the node does not have children, update the current score to this node's score
//            if (node.children.isEmpty()){
//                currentScore = node.score;
//            }
//            else {                                       // if the node has children
//                List<Node> nextLevelNodes = new ArrayList<Node>();
//                for (String childID : node.children) {
//                    nextLevelNodes.add(tree.getTreeMap().get(childID));         // retrieve this children nodes from the tree and call minimaxAlgorithm on that level of Nodes
//                }
//                if (node.getBoardState().greenTurn) {
//                    currentScore = miniMaxAlgorithm(depth - 1, tree).second;   // should make recursive calls until no more children are found
//                    if (currentScore > bestScore) {
//                        bestScore = currentScore;
//                        bestStartToken = new Pair<Integer, Integer>(node.coordinates[0]);
//                        bestEndToken = new Pair<Integer, Integer>(node.coordinates[1]);
//                    }
//                } else {
//                    currentScore = miniMaxAlgorithm(depth - 1, tree).second;
//                    if (currentScore < bestScore) {
//                        bestScore = currentScore;
//                        bestStartToken = new Pair<Integer, Integer>(node.coordinates[0]);
//                        bestEndToken = new Pair<Integer, Integer>(node.coordinates[1]);
//                    }
//                }
//            }
//        }
        Pair<Pair<Integer, Integer>[], Integer> result;
//        Pair[] bestMove = new Pair[] {new Pair<Integer, Integer>(bestStartToken.first, bestStartToken.second), new Pair<Integer, Integer>(bestEndToken.first, bestEndToken.second)};
//        result = new Pair<Pair[], Integer>(bestMove, bestScore);
//        result = new Pair<Pair[], Integer>(new Pair[] {new Pair(tree.getRootNode().coordinates[0].first, tree.getRootNode().coordinates[0].second),
//                                                        new Pair(tree.getRootNode().coordinates[1].first, tree.getRootNode().coordinates[1].second)},
//                                                        tree.getRootNode().score);
        result = new Pair<Pair<Integer, Integer>[], Integer>(new Pair[] {new Pair<Integer, Integer>(bestStartToken.first, bestStartToken.second), new Pair<Integer, Integer>(bestEndToken.first, bestEndToken.second)}, bestScore);
        return result;

        // else make recursive calls by passing list of children of previous level and update the best score

    }


    //    private Pair<Pair<Integer, Integer>[], Integer> miniMaxAlgorithm(Node node, int depth, String player, Tree tree) {
//        Pair<Pair<Integer, Integer>[], Integer> bestPair = null;
//        if (depth == 0) {
//            bestPair = new Pair<Pair<Integer, Integer>[], Integer>(node.getCoordinates(), node.getScore());
//            return bestPair;
//        }
//        if (player == maxPlayer) {
//            bestPair.second = Integer.MIN_VALUE;
//            for (String kidId: node.children) {
//                Node child = tree.getNode(kidId);
//                Pair<Pair<Integer, Integer>[], Integer> temp = miniMaxAlgorithm(child,depth-1, minPlayer, tree);
//                bestPair = bestPair.second > temp.second ? bestPair : temp;
//            }
//            return bestPair;
//        }
//        else {
//            bestPair.second = Integer.MAX_VALUE;
//            for (String kidId: node.children) {
//                Node child = tree.getNode(kidId);
//                Pair<Pair<Integer, Integer>[], Integer> temp = miniMaxAlgorithm(child,depth-1, maxPlayer, tree);
//                bestPair = bestPair.second < temp.second ? bestPair : temp;
//            }
//            return bestPair;
//        }
//    }
//    private Pair<Integer, Integer>[] miniMaxAlgorithm(Tree tree) {
//    	// mySeed is maximizing; while oppSeed is minimizing
//        int bestScore = (player == maxPlayer) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
//        int currentScore;
//        Pair<Integer, Integer> bestStartToken = new Pair<Integer, Integer>(-1, -1);
//        Pair<Integer, Integer> bestEndToken = new Pair<Integer, Integer>(-1, -1);
//
//        if (nextMoves.isEmpty() || depth == 0) {
//            // Gameover or depth reached, evaluate score
//            bestScore = evaluate();
//        } else {
//            for (Pair<Integer, Integer>[] move : nextMoves) {
//                // Try this move for the current "player"
//                board.moveToken(move[0].first, move[0].second, move[1].first, move[1].second);
//                if (player == maxPlayer) {  // computer is maximizing player
//                    currentScore = minimax(depth - 1, minPlayer)[0].first;
//                    if (currentScore > bestScore) {
//                        bestScore = currentScore;
//                        bestStartToken = new Pair(move[0].first, move[0].second);
//                        bestEndToken = new Pair(move[1].first, move[1].second);
//                    }
//                } else {  // minimizing player
//                    currentScore = minimax(depth - 1, maxPlayer)[0].first;
//                    if (currentScore < bestScore) {
//                        bestScore = currentScore;
//                        bestStartToken = new Pair(move[0].first, move[0].second);
//                        bestEndToken = new Pair(move[1].first, move[1].second);
//                    }
//                }
//            }
//        }
//        Pair<Integer, Integer>[] result = new Pair[3];
//        result[0] = new Pair<>(bestScore, -1); // second value is arbitrary/ never used
//        result[1] = bestStartToken;
//        result[2] = bestEndToken;
//        return result;
//    }
}
