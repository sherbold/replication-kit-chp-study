package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.optional;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.ITree;
import de.ugoe.cs.smartshark.mutaSHARK.util.*;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.PitestMutator;
import java.util.ArrayList;
import java.util.List;

public class UnaryOperatorInsertionMutator extends PitestMutator
{
    @Override
    public List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        ArrayList<MutatedNode> results = new ArrayList<>();
        results.addAll(getPossibleUOI1Mutations(treeNode, target, actions));
        results.addAll(getPossibleUOI2Mutations(treeNode, target, actions));
        results.addAll(getPossibleUOI3Mutations(treeNode, target, actions));
        results.addAll(getPossibleUOI4Mutations(treeNode, target, actions));
        return results;
    }

    private List<MutatedNode> getPossibleUOI1Mutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        ArrayList<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert)
            {
                Insert insertPostfix = (Insert) actions.get(i);
                if (!insertPostfix.getNode().getType().name.equals("PostfixExpression"))
                {
                    continue;
                }
                if (insertPostfix.getNode().getChildren().size() != 2)
                {
                    continue;
                }
                if (!(insertPostfix.getNode().getChildren().get(1).getType().name.equals("POSTFIX_EXPRESSION_OPERATOR") && insertPostfix.getNode().getChildren().get(1).getLabel().equals("++")))
                {
                    continue;
                }
                ITree parent = insertPostfix.getNode().getParent();
                for (int j = 0; j < actions.size(); j++)
                {
                    if (actions.get(j) instanceof TreeInsert)
                    {
                        TreeInsert innerInsert = (TreeInsert) actions.get(j);
                        if (innerInsert.getNode().getParent() != insertPostfix.getNode())
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
                                if (!delete.getNode().getParent().getType().name.equals(parent.getType().name))
                                {
                                    continue;
                                }
                                ITree deepCopy = treeNode.getTree().deepCopy();
                                TreeNode clonedTree = new TreeNode(deepCopy);
                                String url = TreeHelper.getUrl(delete.getNode().getParent(), Integer.MAX_VALUE);
                                TreeNode parentNode = new TreeNode(clonedTree.getTree().getChild(url));
                                parentNode.removeChildAt(insertPostfix.getPosition());
                                parentNode.getTree().insertChild(insertPostfix.getNode().deepCopy(), insertPostfix.getPosition());
                                results.add(new MutatedNode(clonedTree, this, 25, "Added postfix ++ @~" + parent.getPos()));

                            }
                        }
                    }
                }
            }
        }
        return results;
    }

    private List<MutatedNode> getPossibleUOI2Mutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        ArrayList<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert)
            {
                Insert insertPostfix = (Insert) actions.get(i);
                if (!insertPostfix.getNode().getType().name.equals("PostfixExpression"))
                {
                    continue;
                }
                if (insertPostfix.getNode().getChildren().size() != 2)
                {
                    continue;
                }
                if (!(insertPostfix.getNode().getChildren().get(1).getType().name.equals("POSTFIX_EXPRESSION_OPERATOR") && insertPostfix.getNode().getChildren().get(1).getLabel().equals("--")))
                {
                    continue;
                }
                ITree parent = insertPostfix.getNode().getParent();
                for (int j = 0; j < actions.size(); j++)
                {
                    if (actions.get(j) instanceof TreeInsert)
                    {
                        TreeInsert innerInsert = (TreeInsert) actions.get(j);
                        if (innerInsert.getNode().getParent() != insertPostfix.getNode())
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
                                if (!delete.getNode().getParent().getType().name.equals(parent.getType().name))
                                {
                                    continue;
                                }
                                ITree deepCopy = treeNode.getTree().deepCopy();
                                TreeNode clonedTree = new TreeNode(deepCopy);
                                String url = TreeHelper.getUrl(delete.getNode().getParent(), Integer.MAX_VALUE);
                                TreeNode parentNode = new TreeNode(clonedTree.getTree().getChild(url));
                                parentNode.removeChildAt(insertPostfix.getPosition());
                                parentNode.getTree().insertChild(insertPostfix.getNode().deepCopy(), insertPostfix.getPosition());
                                results.add(new MutatedNode(clonedTree, this, 25, "Added postfix -- @~" + parent.getPos()));
                            }
                        }
                    }
                }
            }
        }
        return results;
    }

    private List<MutatedNode> getPossibleUOI3Mutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        ArrayList<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert)
            {
                Insert insertPostfix = (Insert) actions.get(i);
                if (!insertPostfix.getNode().getType().name.equals("PrefixExpression"))
                {
                    continue;
                }
                if (insertPostfix.getNode().getChildren().size() != 2)
                {
                    continue;
                }
                if (!(insertPostfix.getNode().getChildren().get(0).getType().name.equals("PREFIX_EXPRESSION_OPERATOR") && insertPostfix.getNode().getChildren().get(0).getLabel().equals("++")))
                {
                    continue;
                }
                ITree parent = insertPostfix.getNode().getParent();
                for (int j = 0; j < actions.size(); j++)
                {
                    if (actions.get(j) instanceof TreeInsert)
                    {
                        TreeInsert innerInsert = (TreeInsert) actions.get(j);
                        if (innerInsert.getNode().getParent() != insertPostfix.getNode())
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
                                if (!delete.getNode().getParent().getType().name.equals(parent.getType().name))
                                {
                                    continue;
                                }
                                ITree deepCopy = treeNode.getTree().deepCopy();
                                TreeNode clonedTree = new TreeNode(deepCopy);
                                String url = TreeHelper.getUrl(delete.getNode().getParent(), Integer.MAX_VALUE);
                                TreeNode parentNode = new TreeNode(clonedTree.getTree().getChild(url));
                                parentNode.removeChildAt(insertPostfix.getPosition());
                                parentNode.getTree().insertChild(insertPostfix.getNode().deepCopy(), insertPostfix.getPosition());
                                results.add(new MutatedNode(clonedTree, this, 25, "Added prefix ++ @~" + parent.getPos()));
                            }
                        }
                    }
                }
            }
        }
        return results;
    }

    private List<MutatedNode> getPossibleUOI4Mutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        ArrayList<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert)
            {
                Insert insertPostfix = (Insert) actions.get(i);
                if (!insertPostfix.getNode().getType().name.equals("PrefixExpression"))
                {
                    continue;
                }
                if (insertPostfix.getNode().getChildren().size() != 2)
                {
                    continue;
                }
                if (!(insertPostfix.getNode().getChildren().get(0).getType().name.equals("PREFIX_EXPRESSION_OPERATOR") && insertPostfix.getNode().getChildren().get(0).getLabel().equals("--")))
                {
                    continue;
                }
                ITree parent = insertPostfix.getNode().getParent();
                for (int j = 0; j < actions.size(); j++)
                {
                    if (actions.get(j) instanceof TreeInsert)
                    {
                        TreeInsert innerInsert = (TreeInsert) actions.get(j);
                        if (innerInsert.getNode().getParent() != insertPostfix.getNode())
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
                                if (!delete.getNode().getParent().getType().name.equals(parent.getType().name))
                                {
                                    continue;
                                }
                                ITree deepCopy = treeNode.getTree().deepCopy();
                                TreeNode clonedTree = new TreeNode(deepCopy);
                                String url = TreeHelper.getUrl(delete.getNode().getParent(), Integer.MAX_VALUE);
                                TreeNode parentNode = new TreeNode(clonedTree.getTree().getChild(url));
                                parentNode.removeChildAt(insertPostfix.getPosition());
                                parentNode.getTree().insertChild(insertPostfix.getNode().deepCopy(), insertPostfix.getPosition());
                                results.add(new MutatedNode(clonedTree, this, 25, "Added prefix -- @~" + parent.getPos()));
                            }
                        }
                    }
                }
            }
        }
        return results;
    }
}

