package Utility;

import Utility.DataStructures.Graph;
import Utility.DataStructures.Tree;

import java.util.*;

public class AStarPath<T> {
    private final Graph<T> graph;

    private final Tree<T> tree;
    private final PriorityQueue<Tree<T>.Node> openWayPoints;

    private Tree<T>.Node head;

    private final Deque<Tree<T>.Node> path = new ArrayDeque<>();

    private boolean foundTarget = false;

    public AStarPath(Graph<T> graph, T start, T target, Comparator<Tree<T>.Node> heuristic) {
        this.graph = graph;
        tree = new Tree<>(start);
        openWayPoints = new PriorityQueue<>(heuristic);

        openWayPoints.add(tree.getRoot());

        findPath(target);
    }

    public void findPath(T target) {
        assert !foundTarget;
        if (tree.contains(target)) {
            head = tree.getNode(target);
        } else {
            while (!foundTarget) {
                grow(target);
            }
        }
        retracePath();
    }

    private void grow(T target) {
        assert !foundTarget;
        Tree<T>.Node wp = openWayPoints.poll();
        assert wp != null;
        while (wp.isNotValid()) {
            wp = openWayPoints.poll();
            assert wp != null;
        }
        try {
            if (wp.getData().equals(target)) {
                head = wp;
                foundTarget = true;
            } else {
                for (T neighbour : graph.getNeighbours(wp.getData())) {
                    if (!tree.contains(neighbour)) {
                        Tree<T>.Node child = wp.addChild(neighbour);
                        openWayPoints.add(child);
                    }
                }
            }
        } catch (Tree.InvalidNodeException e) {
            System.err.println("impossible");
            throw new RuntimeException(e);
        }
    }

    private void retracePath() {
        assert foundTarget;

        path.clear();
        try {
            while (head != null) {
                path.addLast(head);
                head = head.getParent();
            }
        } catch (Tree.InvalidNodeException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStart(T start) {
        assert path.stream().anyMatch((Tree<T>.Node n) -> {
            try {
                return n.getData() == start;
            } catch (Tree.InvalidNodeException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            while (!(path.peekLast().getData() == start)) {
                path.removeLast();
            }
            tree.makeRoot(path.peekLast());
        } catch (Tree.InvalidNodeException e) {
            throw new RuntimeException(e);
        }

        assert path.contains(start);
    }
}
