package BonzeeGame;

/*
 * Copyright (C) 2007-2014 by Brett Alistair Kromkamp <brett@perfectlearn.com>.
 */

// source: http://www.quesucede.com/page/show/id/java-tree-implementation


import java.util.*;

public class DepthFirstTreeIterator implements Iterator<Node> {
    private LinkedList<Node> list;

    public DepthFirstTreeIterator(HashMap<String, Node> tree, String identifier) {
        list = new LinkedList<>();

        if (tree.containsKey(identifier)) {
            this.buildList(tree, identifier);
        }
    }

    private void buildList(HashMap<String, Node> tree, String identifier) {
        list.add(tree.get(identifier));
        ArrayList<String> children = tree.get(identifier).getChildren();
        for (String child : children) {

            // Recursive call
            this.buildList(tree, child);
        }
    }

    @Override
    public boolean hasNext() {
        return !list.isEmpty();
    }

    @Override
    public Node next() {
        return list.poll();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}

