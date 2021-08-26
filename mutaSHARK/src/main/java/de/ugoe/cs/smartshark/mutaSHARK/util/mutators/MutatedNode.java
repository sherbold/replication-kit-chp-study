package de.ugoe.cs.smartshark.mutaSHARK.util.mutators;

import de.ugoe.cs.smartshark.mutaSHARK.util.TreeHelper;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.expressions.ExpressionStatement;

public class MutatedNode extends TreeNode
{
    private final TreeMutationOperator mutationOperator;
    private final double cost;
    private final String description;

    public MutatedNode(TreeNode treeNode, TreeMutationOperator mutationOperator, double cost, String description)
    {
        super(TreeHelper.updateTree(treeNode.getTree()));
        this.mutationOperator = mutationOperator;
        this.cost = cost;
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public double getCost()
    {
        return cost;
    }

    public TreeMutationOperator getMutationOperator()
    {
        return mutationOperator;
    }

    @Override
    public String toString()
    {
        return "MutatedNode{ description='\"" + description +"\", mutationOperator=" + mutationOperator + ", cost=" + cost + "}";
    }
}
