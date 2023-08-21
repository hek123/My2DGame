package Utility.DataStructures;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Tree<T> implements Iterable<T> {
    private Node root;

    private int size = 1;

    public Tree(T rootData) {
        this.root = new Node(rootData);
    }

    public Node getRoot() {
        if (root.parent != null) throw new BrokenTreeError("Root has a parent");
        return root;
    }

    public int size() {
        return size;
    }

    public boolean contains(Object o) {
        if (o instanceof Tree<?>.Node node)
            o = node.data;
        for (T data: this) {
            if (data.equals(o)) return true;
        }
        return false;
    }

    public Node getNode(T data) {
        for (Iterator<Node> it = nodeIterator(); it.hasNext(); ) {
            Node node = it.next();
            if (node.data == data) {
                if (node.isNotValid()) throw new BrokenTreeError("Tree contains invalid node");
                return node;
            }
        }
        return null;
    }

    public void makeRoot(Node newRoot) {}

    @Override
    public Iterator<T> iterator() {
        return new TreeIterator();
    }

    private class TreeIterator implements Iterator<T> {

        private final Deque<Node> nodes = new ArrayDeque<>();

        TreeIterator() {
            nodes.addLast(root);
        }

        @Override
        public boolean hasNext() {
            return !nodes.isEmpty();
        }

        @Override
        public T next() {
            Node node = nodes.pollFirst();
            assert node != null;
            for (Node n : node.children) {
                nodes.addLast(n);
            }
            return node.data;
        }
    }

    public Iterator<Node> nodeIterator() {
        return new NodeIterator();
    }

    private class NodeIterator implements Iterator<Node> {
        private final Deque<Node> nodes = new ArrayDeque<>();

        NodeIterator() {
            nodes.addLast(root);
        }

        @Override
        public boolean hasNext() {
            return !nodes.isEmpty();
        }

        @Override
        public Node next() {
            Node node = nodes.pollFirst();
            assert node != null;
            for (Node n : node.children) {
                nodes.addLast(n);
            }
            return node;
        }
    }


    public class Node {
        private T data;
        private Node parent;
        private final Set<Node> children = new HashSet<>();

        /**
         * Create a rootNode
         *
         * @param data data of the rootNode
         */
        private Node(@NotNull T data) {
            assert size == 1;

            this.data = data;
            this.parent = null;
        }

        private Node(@NotNull T data, @NotNull Node parent) throws InvalidNodeException {
            if (parent.isNotValid()) throw new InvalidNodeException();
            this.parent = parent;
            this.data = data;
        }

        public boolean isNotValid() {
            return data == null;
        }

        public T getData() throws InvalidNodeException {
            if (isNotValid()) throw new InvalidNodeException();
            return data;
        }

        public Node getParent() throws InvalidNodeException {
            if (isNotValid()) throw new InvalidNodeException();
            if (parent.isNotValid()) throw new BrokenTreeError("Parent of valid Node is not valid");
            return parent;
        }

        public int getLevel() throws InvalidNodeException {
            if (isNotValid()) throw new InvalidNodeException();
            if (parent.isNotValid()) throw new BrokenTreeError("Parent of valid Node is not valid");
            if (parent == null) {
                assert root == this;
                return 0;
            } else {
                return parent.getLevel() + 1;
            }
        }

        public Node addChild(T data) throws InvalidNodeException {
            if (isNotValid()) throw new InvalidNodeException();

            assert root != null;
            Node child = new Node(data, this);
            children.add(child);

            size++;
            return child;
        }

        public void removeNode() throws InvalidNodeException {
            if (isNotValid()) throw new InvalidNodeException();
            if (parent.isNotValid()) throw new BrokenTreeError("Parent of valid Node is not valid");

            assert root != null;
            assert this != root;

            for (Node child: children) {
                assert child.parent == this;
                if (child.isNotValid()) throw new BrokenTreeError("Child of valid Node is not valid; In removal");
                child.removeNode();
            }
            assert children.isEmpty();

            assert parent.children.contains(this);
            parent.children.remove(this);

            parent = null;

            data = null;
        }

        @Override
        public boolean equals(Object obj) {
            if (isNotValid()) throw new RuntimeException(new InvalidNodeException());
            if (obj instanceof Tree<?>.Node node) {
                return data.equals(node.data);
            } else {
                return data.equals(obj);
            }
        }
    }


    public static class InvalidNodeException extends Exception {
        public InvalidNodeException() {
            super("Tried to access an invalid node. This node does not belong to any Tree ...");
        }
    }

    private static class BrokenTreeError extends Error {
        BrokenTreeError(String message) {
            super(message);
        }
    }
}
