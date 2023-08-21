package Utility.DataStructures;

import java.util.*;

public class TreeNode<T> implements Iterable<TreeNode<T>> {
    final public T data;
    public TreeNode<T> parent;
    public final List<TreeNode<T>> children = new ArrayList<>();

    final public int level;

    public TreeNode(T data, TreeNode<T> parent) {
        this.parent = parent;
        this.data = data;

        if (parent == null)
            level = 0;
        else {
            parent.children.add(this);
            level = parent.level + 1;
        }
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public boolean contains(T data) {
        if (data.equals(this.data)) return true;
        for (TreeNode<T> node : children) {
            if (node.contains(data)) return true;
        }
        return false;
    }

    public TreeNode<T> getNode(T data) {
        if (data.equals(this.data)) return this;
        for (TreeNode<T> node : children) {
            TreeNode<T> result = node.getNode(data);
            if (result != null) return result;
        }
        return null;
    }

    public void removeChildren() {
        children.clear();
    }

    public void makeRoot() {
        parent.removeChildren();
        parent = null;
    }

    public int getNbNodes() {
        int nbNodes = 1;
        for (TreeNode<T> node : children) {
            nbNodes += node.getNbNodes();
        }
        return nbNodes;
    }

    @Override
    public Iterator<TreeNode<T>> iterator() {
        return new TreeIterator();
    }

    private class TreeIterator implements Iterator<TreeNode<T>> {

        private final Deque<TreeNode<T>> nodes = new ArrayDeque<>();

        TreeIterator() {
            nodes.addLast(TreeNode.this);
        }

        @Override
        public boolean hasNext() {
            return !nodes.isEmpty();
        }

        @Override
        public TreeNode<T> next() {
            TreeNode<T> node = nodes.pollFirst();
            assert node != null;
            for (TreeNode<T> n : node.children) {
                nodes.addLast(n);
            }
            return node;
        }
    }
}

