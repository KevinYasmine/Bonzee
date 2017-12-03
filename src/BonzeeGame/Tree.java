package BonzeeGame;

import java.util.*;

public class Tree {
	private HashMap<String, Node> treeMap;
	String rootID;
	private final static int ROOT = 0;
	
	public Tree() {
		this.treeMap = new HashMap<String, Node>();
	}

	public String getRootID() {
		return rootID;
	}

	public void setRootID(String rootID) {
		this.rootID = rootID;
	}

	public int depth(String nodeId) {
		return treeMap.get(nodeId).depth;
	}

	public Node getRootNode(){
		return treeMap.get(rootID);
	}

	public List<Node> nodesAtLevel(int level){
		// will add the nodes at the specified level
		List<Node> nodes = new ArrayList<>();
		for (Node node : treeMap.values()){
			if (node.depth == level) {
				nodes.add(node);
			}
		}
		return nodes;
	}

	// in the case of a root, id and parent will be identical (i.e. both == rootID)
	public Node addNode(String id, String parent, Pair[] coordinates, BonzeeBoard bb) {
		// case: ROOT -- coordinates are the GREEN move that lead to the state of the board at root node (passed through BonzeeBoard)
		Node n = new Node(id, coordinates, bb, parent);
		treeMap.put(id, n);
		if (!parent.equals(id)) { // the String parent will never be null; need to detect if String parent matches the ID of the root node
			treeMap.get(parent).addChild(id);
		}else{
			rootID = id;
		}
		return n;
	}

	// in the case of a root, id and parent will be identical (i.e. both == rootID)
	public Node addNode(String id, String parent, BonzeeBoard bb) {
		Node n = new Node(id, bb);
		treeMap.put(id, n);
		// if not root, add the ID of the child to its parent's list of children (ArrayList<String>)
		if (!parent.equals(id)) { // see above; only a root will have its own ID equal that of its 'parent'
			treeMap.get(parent).addChild(id);
		}
		return n;
	}

	
	public Node getNode(String id) {
		return treeMap.get(id);
	}

	public HashMap<String, Node> getTreeMap() {
		return treeMap;
	}

	public void setTreeMap(HashMap<String, Node> treeMap) {
		this.treeMap = treeMap;
	}

	public List<Node> generateChildren(Node node){
		List<Pair<Integer, Integer>[]> kids = node.findChildren();
		List<Node> addedNodes = new ArrayList<>();

		for (Pair<Integer, Integer>[] kid : kids){
			// move token on bonzee board according to kid coordinates

			BonzeeBoard newBoard = new BonzeeBoard(node.boardState);
			newBoard.moveToken(kid[0].first, kid[0].second, kid[1].first, kid[1].second);
			// new node has the coordinates of the move, as well as the new state of the board after that move
			// this newly created node is added to the list of children nodes created at that level
			addedNodes.add(addNode(UUID.randomUUID().toString(), node.getId(), kid, newBoard));
		}
		return addedNodes;
	}

	// TODO: test display method
	public void display(String identifier) {
		this.display(identifier, ROOT);
	}

	public void display(String identifier, int depth) {
		ArrayList<String> children = treeMap.get(identifier).getChildren();

		if (depth == ROOT) {
			System.out.println(treeMap.get(identifier).getId());
		} else {
			String tabs = String.format("%0" + depth + "d", 0).replace("0", "    "); // 4 spaces
			System.out.println(tabs + treeMap.get(identifier).getId());
		}
		depth++;
		for (String child : children) {

			// Recursive call
			this.display(child, depth);
		}
	}
}
