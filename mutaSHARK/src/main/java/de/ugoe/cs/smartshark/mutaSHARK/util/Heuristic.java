package de.ugoe.cs.smartshark.mutaSHARK.util;

import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.TreeMutationOperator;

import java.util.List;

public class Heuristic extends HeuristicBase
{
    protected Heuristic(TreeNode destinationNode, List<TreeMutationOperator> mutationOperators)
    {
        super(destinationNode, mutationOperators);
    }

    @Override
    protected double getHeuristicInternal(TreeNode fromNode) throws TooManyActionsException
    {
        int actionCount = new DiffTree(fromNode.getTree(), destinationNode.getTree()).getActions().size();

        double e = Math.exp(actionCount / 4.0);
        double result = e / (e + 1);
        result = result - 0.5;
        result = result * 2;

        return result;
    }
}
