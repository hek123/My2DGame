package Utility.DataStructures;

public interface ImmutableMap2D<P> {
    Solid getElement(int i, int j);

    Solid getElement(P pos);
}
