package de.ugoe.cs.smartshark.mutaSHARK.util;

import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.TreeMutationOperator;

import java.util.List;

public class SearchSettings
{
    public final int maxFoundPaths;
    public final int maxHops;
    public final HeuristicBase heuristic;
    public final List<TreeMutationOperator> mutationOperators;

    public SearchSettings(int maxFoundPaths, int maxHops, TreeNode destinationNode, List<TreeMutationOperator> mutationOperators)
    {
        this.maxHops = maxHops;
        this.mutationOperators = mutationOperators;
        this.maxFoundPaths = maxFoundPaths;
        this.heuristic = new Heuristic(destinationNode, mutationOperators);
    }
}
