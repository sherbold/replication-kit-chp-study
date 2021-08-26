package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.active;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import de.ugoe.cs.smartshark.mutaSHARK.util.*;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.PitestMutator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class InvertNegativesMutator extends PitestMutator
{
    @Override
    public List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        ArrayList<MutatedNode> results = new ArrayList<>();
        results.addAll(getPossibleInsertMutations(treeNode, target, actions));
        results.addAll(getPossibleRemoveMutations(treeNode, target, actions));
        results.addAll(getPossibleReplaceMutations(treeNode, target, actions));
        return results;
    }

    private List<MutatedNode> getPossibleReplaceMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        ArrayList<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert)
            {
                Insert insert = (Insert) actions.get(i);
                if (!insert.getNode().getType().name.equals("PREFIX_EXPRESSION_OPERATOR"))
                {
                    continue;
                }
                ITree parent = insert.getParent();
                for (int j = 0; j < actions.size(); j++)
                {
                    if (actions.get(j) instanceof Delete)
                    {
                        Delete delete = (Delete) actions.get(j);
                        if (!delete.getNode().getType().name.equals("PREFIX_EXPRESSION_OPERATOR"))
                        {
                            continue;
                        }
                        if (delete.getNode().getParent() != parent)
                        {
                            continue;
                        }
                        String oldLabel = delete.getNode().getLabel();
                        String newLabel = insert.getNode().getLabel();
                        if (!isSupportedReplacePrefix(oldLabel, newLabel))
                        {
                            continue;
                        }
                        TreeNode clonedTree = new TreeNode(treeNode.getTree().deepCopy());
                        String url = TreeHelper.getUrl(parent, Integer.MAX_VALUE);
                        TreeNode parentNode = new TreeNode(clonedTree.getTree().getChild(url));
                        parentNode.removeChildAt(insert.getPosition());
                        parentNode.getTree().insertChild(insert.getNode().deepCopy(), insert.getPosition());
                        results.add(new MutatedNode(clonedTree, this, 1, "Replaced prefix " + oldLabel + " with " + newLabel + " @~" + parent.getPos()));
                    }

                }
            }
        }
        return results;
    }

    private boolean isSupportedReplacePrefix(String oldLabel, String newLabel)
    {
        switch (oldLabel)
        {
            case "-":
                return newLabel.equals("+");
            case "+":
                return newLabel.equals("-");
        }
        return false;
    }

    private List<MutatedNode> getPossibleInsertMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        ArrayList<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert)
            {
                Insert insertPrefix = (Insert) actions.get(i);
                if (!insertPrefix.getNode().getType().name.equals("PrefixExpression"))
                {
                    continue;
                }
                if (insertPrefix.getNode().getChildren().size() != 2)
                {
                    continue;
                }
                if (!insertPrefix.getNode().getChildren().get(0).getType().name.equals("PREFIX_EXPRESSION_OPERATOR") && !insertPrefix.getNode().getChildren().get(0).getLabel().equals("-"))
                {
                    continue;
                }
                ITree parent = insertPrefix.getNode().getParent();
                for (int j = 0; j < actions.size(); j++)
                {
                    if (actions.get(j) instanceof TreeInsert)
                    {
                        TreeInsert innerInsert = (TreeInsert) actions.get(j);
                        if (innerInsert.getNode().getParent() != insertPrefix.getNode())
                        {
                            continue;
                        }
                        for (int k = 0; k < actions.size(); k++)
                        {
                            if (actions.get(k) instanceof TreeDelete)
                            {
                                TreeDelete delete = (TreeDelete) actions.get(k);
                                if (delete.getNode().getParent().getPos() != parent.getPos())
                                {
                                    continue;
                                }
                                if (delete.getNode().getParent().getChildren().size() != parent.getChildren().size())
                                {
                                    continue;
                                }
                                if (!delete.getNode().getParent().getType().name.equals(parent.getType().name))
                                {
                                    continue;
                                }
                                ITree deepCopy = treeNode.getTree().deepCopy();
                                TreeNode clonedTree = new TreeNode(deepCopy);
                                String url = TreeHelper.getUrl(delete.getNode().getParent(), Integer.MAX_VALUE);
                                TreeNode parentNode = new TreeNode(clonedTree.getTree().getChild(url));
                                parentNode.removeChildAt(insertPrefix.getPosition());
                                parentNode.getTree().insertChild(insertPrefix.getNode().deepCopy(), insertPrefix.getPosition());
                                results.add(new MutatedNode(clonedTree, this, 1, "Added - prefix @~" + parent.getPos()));
                            }
                        }
                    }
                }
            }
        }
        return results;
    }

    private List<MutatedNode> getPossibleRemoveMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        ArrayList<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Delete)
            {
                Delete deletePrefix = (Delete) actions.get(i);
                if (!deletePrefix.getNode().getType().name.equals("PrefixExpression"))
                {
                    continue;
                }
                if (deletePrefix.getNode().getChildren().size() != 2)
                {
                    continue;
                }
                if (!deletePrefix.getNode().getChildren().get(0).getType().name.equals("PREFIX_EXPRESSION_OPERATOR") && !deletePrefix.getNode().getChildren().get(0).getLabel().equals("-"))
                {
                    continue;
                }
                ITree parent = deletePrefix.getNode().getParent();
                for (int j = 0; j < actions.size(); j++)
                {
                    if (actions.get(j) instanceof TreeDelete)
                    {
                        TreeDelete innerDelete = (TreeDelete) actions.get(j);
                        if (innerDelete.getNode().getParent() != deletePrefix.getNode())
                        {
                            continue;
                        }
                        for (int k = 0; k < actions.size(); k++)
                        {
                            if (actions.get(k) instanceof TreeInsert)
                            {
                                TreeInsert insert = (TreeInsert) actions.get(k);
                                if (insert.getParent().getPos() != parent.getPos())
                                {
                                    continue;
                                }
                                if (insert.getParent().getChildren().size() != parent.getChildren().size())
                                {
                                    continue;
                                }
                                if (!insert.getParent().getType().name.equals(parent.getType().name))
                                {
                                    continue;
                                }
                                ITree deepCopy = treeNode.getTree().deepCopy();
                                TreeNode clonedTree = new TreeNode(deepCopy);
                                String url = TreeHelper.getUrl(parent, Integer.MAX_VALUE);
                                TreeNode parentNode = new TreeNode(clonedTree.getTree().getChild(url));
                                parentNode.removeChildAt(insert.getPosition());
                                parentNode.getTree().insertChild(insert.getNode().deepCopy(), insert.getPosition());
                                results.add(new MutatedNode(clonedTree, this, 1, "Removed - prefix @~" + parent.getPos()));
                            }
                        }
                    }
                }
            }
        }
        return results;
    }
}

