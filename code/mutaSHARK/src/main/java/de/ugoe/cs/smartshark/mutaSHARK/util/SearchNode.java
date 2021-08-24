package de.ugoe.cs.smartshark.mutaSHARK.util;

import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.TreeMutationOperator;

import java.util.Objects;

public class SearchNode
{
    private final TreeNode currentTreeNode;
    private final SearchNode previousSearchNode;
    private final double costToPrevious;
    private final TreeMutationOperator mutationOperator;
    private final HeuristicBase heuristic;

    private Double heuristicCost;

    public SearchNode(TreeNode currentTreeNode, HeuristicBase heuristic)
    {
        this.currentTreeNode = currentTreeNode;
        this.previousSearchNode = null;
        this.costToPrevious = 0;
        this.mutationOperator = null;
        this.heuristic = heuristic;
    }

    public SearchNode(MutatedNode mutatedNode, SearchNode previousSearchNode, HeuristicBase heuristic)
    {
        this.currentTreeNode = mutatedNode;
        this.previousSearchNode = previousSearchNode;
        this.costToPrevious = mutatedNode.getCost();
        this.mutationOperator = mutatedNode.getMutationOperator();
        this.heuristic = heuristic;
    }

    public double getCostToPrevious()
    {
        return costToPrevious;
    }

    public SearchNode getPreviousSearchNode()
    {
        return previousSearchNode;
    }

    public double getTotalCost() throws TooManyActionsException
    {
        return getPreviousCost() + getHeuristicCost();
    }

    public TreeNode getCurrentTreeNode()
    {
        return currentTreeNode;
    }

    private double getPreviousCost()
    {
        double cost = costToPrevious;
        if (previousSearchNode != null)
            cost += previousSearchNode.getPreviousCost();
        return cost;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchNode that = (SearchNode) o;
        return Objects.equals(currentTreeNode, that.currentTreeNode);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(currentTreeNode);
    }

    public Double getHeuristicCost()
    {
        if (heuristicCost == null)
        {
            try
            {
                heuristicCost = heuristic.getHeuristic(currentTreeNode);
            }
            catch (TooManyActionsException e)
            {
                return null;
            }
        }
        return heuristicCost;
    }

    public int getTotalHopCount()
    {
        int count = 0;
        SearchNode current = this;
        while (current != null)
        {
            count++;
            SearchNode previousTreeNode = current.getPreviousSearchNode();
            if (previousTreeNode != null)
                current = previousTreeNode.getPreviousSearchNode();
            else
                current = null;
        }
        return count;
    }

    public TreeMutationOperator getMutationOperator()
    {
        return mutationOperator;
    }
}
