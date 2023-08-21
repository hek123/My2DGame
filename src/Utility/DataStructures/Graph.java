package Utility.DataStructures;

import java.util.List;

public interface Graph<T> {
    List<T> getNeighbours(T node);
}
