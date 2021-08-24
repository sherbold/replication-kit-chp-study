package de.ugoe.cs.smartshark.mutaSHARK.util;

public class SearchEdge
{
    private final SearchNode fromSearchNode;
    private final SearchNode toSearchNode;
    private final double cost;

    public SearchEdge(SearchNode fromSearchNode, double cost, SearchNode toSearchNode)
    {
        this.fromSearchNode = fromSearchNode;
        this.cost = cost;
        this.toSearchNode = toSearchNode;
    }

    public SearchNode getToSearchNode()
    {
        return toSearchNode;
    }

    public double getCost()
    {
        return cost;
    }

    public SearchNode getFromSearchNode()
    {
        return fromSearchNode;
    }
}
