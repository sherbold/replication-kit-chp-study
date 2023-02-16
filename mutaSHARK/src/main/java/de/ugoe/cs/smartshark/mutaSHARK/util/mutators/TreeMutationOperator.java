package de.ugoe.cs.smartshark.mutaSHARK.util.mutators;

import com.github.gumtreediff.actions.model.Action;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;

import java.util.List;

public abstract class TreeMutationOperator
{
    public abstract List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target, List<Action> actions);

    public abstract String getSourceName();
}

