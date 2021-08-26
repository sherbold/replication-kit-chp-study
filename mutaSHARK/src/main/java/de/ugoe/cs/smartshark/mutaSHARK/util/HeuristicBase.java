package de.ugoe.cs.smartshark.mutaSHARK.util;

import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.TreeMutationOperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class HeuristicBase
{
    protected final TreeNode destinationNode;
    protected final List<TreeMutationOperator> mutationOperators;
    private Map<TreeNode, Double> cachedHeuristics = new HashMap<>();

    protected HeuristicBase(TreeNode destinationNode, List<TreeMutationOperator> mutationOperators)
    {
        this.destinationNode = destinationNode;
        this.mutationOperators = mutationOperators;
    }

    public double getHeuristic(TreeNode fromNode) throws TooManyActionsException
    {
        if (!cachedHeuristics.containsKey(fromNode))
        {
            double heuristic = getHeuristicInternal(fromNode);
            cachedHeuristics.put(fromNode, heuristic);
        }
        return cachedHeuristics.get(fromNode);
    }

    protected abstract double getHeuristicInternal(TreeNode fromNode) throws TooManyActionsException;
}

