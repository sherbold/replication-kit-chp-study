package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.active;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.ITree;
import de.ugoe.cs.smartshark.mutaSHARK.util.InsertWrapper;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeHelper;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.PitestMutator;
import java.util.ArrayList;
import java.util.List;

public class FalseReturnsMutator extends PitestMutator
{
    @Override
    public List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        results.addAll(getPossibleBooleanMutations(treeNode, actions));
        return results;
    }

    private List<MutatedNode> getPossibleBooleanMutations(TreeNode treeNode, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert || actions.get(i) instanceof TreeInsert)
            {
                InsertWrapper insert = new InsertWrapper(actions.get(i));
                if (!insert.getParent().getType().name.equals("ReturnStatement"))
                {
                    continue;
                }
                if (!isInMethodWithReturnType(insert.getNode(), "boolean", "Boolean"))
                {
                    continue;
                }
                String newLabel = TreeHelper.getLabelInside(insert.getNode());
                for (int j = 0; j < actions.size(); j++)
                {
                    if (actions.get(j) instanceof TreeDelete || actions.get(j) instanceof Delete)
                    {
                        Action delete = actions.get(j);
                        if (delete.getNode().getParent() != insert.getParent())
                        {
                            continue;
                        }
                        if (!isInMethodWithReturnType(insert.getNode(), "boolean", "Boolean"))
                        {
                            continue;
                        }
                        String oldLabel = TreeHelper.getLabelInside(delete.getNode());
                        if (!isBooleanReplaceSupported(oldLabel, newLabel))
                        {
                            continue;
                        }
                        ITree copy = treeNode.getTree().deepCopy();
                        TreeNode clonedTree = new TreeNode(copy);
                        String url = TreeHelper.getUrl(delete.getNode().getParent(), Integer.MAX_VALUE);
                        TreeNode newParent = new TreeNode(clonedTree.getTree().getChild(url));
                        int positionInParent = delete.getNode().positionInParent();
                        newParent.removeChildAt(positionInParent);
                        newParent.getTree().insertChild(insert.getNode().deepCopy(), positionInParent);
                        results.add(new MutatedNode(clonedTree, this, 1, "Replaced return boolean " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                    }
                }
            }
        }
        return results;
    }



    private boolean isBooleanReplaceSupported(String oldLabel, String newLabel)
    {
        return newLabel.equals("false");
    }

    private boolean isInMethodWithReturnType(ITree node, String... returnTypes)
    {
        ITree current = node;
        while (current != null && !current.getType().name.equals("MethodDeclaration"))
        {
            current = current.getParent();
        }
        if (current == null)
        {
            return false;
        }
        for (ITree child : current.getChildren())
        {
            if (child.getType().name.equals("PrimitiveType"))
            {
                for (String returnType : returnTypes)
                {
                    if (returnType.equals(child.getLabel()))
                    {
                        return true;
                    }
                }
            }

            if (child.getType().name.equals("SimpleType"))
            {
                for (String returnType : returnTypes)
                {
                    if (returnType.equals(TreeHelper.getLabelInside(child)))
                    {
                        return true;
                    }
                }
            }

            if (child.getType().name.equals("ParameterizedType"))
            {
                for (String returnType : returnTypes)
                {
                    if (child.getChildren().size() == 2)
                    {
                        if (returnType.equals(TreeHelper.getLabelInside(child.getChildren().get(0))))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}

