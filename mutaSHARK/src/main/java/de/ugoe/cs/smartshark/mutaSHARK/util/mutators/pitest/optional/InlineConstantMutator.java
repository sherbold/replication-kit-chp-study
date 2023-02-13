package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.optional;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.ITree;
import de.ugoe.cs.smartshark.mutaSHARK.util.InsertWrapper;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeHelper;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.PitestMutator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InlineConstantMutator extends PitestMutator
{
    @Override
    public List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        results.addAll(getPossibleBooleanMutations(treeNode, actions));
        results.addAll(getPossibleIntByteShortNumberMutations(treeNode, actions));
        results.addAll(getPossibleLongNumberMutations(treeNode, actions));
        results.addAll(getPossibleFloatNumberMutations(treeNode, actions));
        results.addAll(getPossibleDoubleNumberMutations(treeNode, actions));
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
                if (!insert.getNode().getType().name.equals("BooleanLiteral"))
                {
                    continue;
                }
                if (!insert.getParent().getType().name.equals("Assignment") && !insert.getParent().getType().name.equals("VariableDeclarationFragment"))
                {
                    continue;
                }
                if (!Arrays.asList(new String[]{"boolean", "Boolean"}).contains(TreeHelper.getDeclarationType(insert.getParent().getChildren().get(0), TreeHelper.getLabelInside(insert.getParent().getChildren().get(0)))))
                {
                    continue;
                }
                String newLabel = insert.getNode().getLabel();
                for (int j = 0; j < actions.size(); j++)
                {
                    if (actions.get(j) instanceof Delete || actions.get(j) instanceof TreeDelete)
                    {
                        Action delete = actions.get(j);
                        if (!delete.getNode().getType().name.equals("BooleanLiteral"))
                        {
                            continue;
                        }
                        if (delete.getNode().getParent() != insert.getParent())
                        {
                            continue;
                        }
                        String oldLabel = delete.getNode().getLabel();
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
                        results.add(new MutatedNode(clonedTree, this, 25, "Replaced inline boolean " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                    }
                }
            }
        }
        return results;
    }

    private List<MutatedNode> getPossibleIntByteShortNumberMutations(TreeNode treeNode, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert || actions.get(i) instanceof TreeInsert)
            {
                InsertWrapper insert = new InsertWrapper(actions.get(i));
                if (!insert.getNode().getType().name.equals("NumberLiteral") && !insert.getNode().getType().name.equals("PrefixExpression"))
                {
                    continue;
                }
                if (!insert.getParent().getType().name.equals("Assignment") && !insert.getParent().getType().name.equals("VariableDeclarationFragment"))
                {
                    continue;
                }
                if (!Arrays.asList(new String[]{"int", "byte", "short", "Integer", "Byte", "Short"}).contains(TreeHelper.getDeclarationType(insert.getParent().getChildren().get(0), TreeHelper.getLabelInside(insert.getParent().getChildren().get(0)))))
                {
                    continue;
                }
                String newLabel = TreeHelper.getLabelInside(insert.getNode());
                for (int j = 0; j < actions.size(); j++)
                {
                    if (actions.get(j) instanceof TreeDelete || actions.get(j) instanceof Delete)
                    {
                        Action delete = actions.get(j);
                        if (!delete.getNode().getType().name.equals("NumberLiteral") && !delete.getNode().getType().name.equals("PrefixExpression"))
                        {
                            continue;
                        }
                        if (delete.getNode().getParent() != insert.getParent())
                        {
                            continue;
                        }
                        String oldLabel = TreeHelper.getLabelInside(delete.getNode());
                        if (!isIntByteShortReplaceSupported(oldLabel, newLabel))
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
                        results.add(new MutatedNode(clonedTree, this, 25, "Replaced inline shortbyteint " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                    }
                }
            }
        }
        return results;
    }

    private List<MutatedNode> getPossibleLongNumberMutations(TreeNode treeNode, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert || actions.get(i) instanceof TreeInsert)
            {
                InsertWrapper insert = new InsertWrapper(actions.get(i));
                if (!insert.getNode().getType().name.equals("NumberLiteral") && !insert.getNode().getType().name.equals("PrefixExpression"))
                {
                    continue;
                }
                if (!insert.getParent().getType().name.equals("Assignment") && !insert.getParent().getType().name.equals("VariableDeclarationFragment"))
                {
                    continue;
                }
                if (!Arrays.asList(new String[]{"long", "Long"}).contains(TreeHelper.getDeclarationType(insert.getParent().getChildren().get(0), TreeHelper.getLabelInside(insert.getParent().getChildren().get(0)))))
                {
                    continue;
                }
                String newLabel = TreeHelper.getLabelInside(insert.getNode());
                for (int j = 0; j < actions.size(); j++)
                {
                    if (actions.get(j) instanceof Delete || actions.get(j) instanceof TreeDelete)
                    {
                        Action delete = actions.get(j);
                        if (!delete.getNode().getType().name.equals("NumberLiteral") && !delete.getNode().getType().name.equals("PrefixExpression"))
                        {
                            continue;
                        }
                        if (delete.getNode().getParent() != insert.getParent())
                        {
                            continue;
                        }
                        String oldLabel = TreeHelper.getLabelInside(delete.getNode());
                        if (!isLongReplaceSupported(oldLabel, newLabel))
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
                        results.add(new MutatedNode(clonedTree, this, 25, "Replaced inline long " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                    }
                }
            }
        }
        return results;
    }

    private List<MutatedNode> getPossibleFloatNumberMutations(TreeNode treeNode, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert || actions.get(i) instanceof TreeInsert)
            {
                InsertWrapper insert = new InsertWrapper(actions.get(i));
                if (!insert.getNode().getType().name.equals("NumberLiteral") && !insert.getNode().getType().name.equals("PrefixExpression"))
                {
                    continue;
                }
                if (!insert.getParent().getType().name.equals("Assignment") && !insert.getParent().getType().name.equals("VariableDeclarationFragment"))
                {
                    continue;
                }
                if (!Arrays.asList(new String[]{"float", "Float"}).contains(TreeHelper.getDeclarationType(insert.getParent().getChildren().get(0), TreeHelper.getLabelInside(insert.getParent().getChildren().get(0)))))
                {
                    continue;
                }
                String newLabel = TreeHelper.getLabelInside(insert.getNode());
                for (int j = 0; j < actions.size(); j++)
                {
                    if (actions.get(j) instanceof Delete || actions.get(j) instanceof TreeDelete)
                    {
                        Action delete = actions.get(j);
                        if (delete.getNode().getParent() != insert.getParent())
                        {
                            continue;
                        }
                        String oldLabel = TreeHelper.getLabelInside(delete.getNode());
                        if (delete.getNode().getType().name.equals("QualifiedName"))
                        {
                            if (!oldLabel.equals("Float.NaN"))
                            {
                                continue;
                            }
                        }
                        else
                        {
                            if (!delete.getNode().getType().name.equals("NumberLiteral") && !delete.getNode().getType().name.equals("PrefixExpression"))
                            {
                                continue;
                            }
                        }
                        if (!isFloatReplaceSupported(oldLabel, newLabel))
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
                        results.add(new MutatedNode(clonedTree, this, 25, "Replaced inline float " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                    }

                }
            }
        }
        return results;
    }

    private List<MutatedNode> getPossibleDoubleNumberMutations(TreeNode treeNode, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert || actions.get(i) instanceof TreeInsert)
            {
                InsertWrapper insert = new InsertWrapper(actions.get(i));
                if (!insert.getNode().getType().name.equals("NumberLiteral") && !insert.getNode().getType().name.equals("PrefixExpression"))
                {
                    continue;
                }
                if (!insert.getParent().getType().name.equals("Assignment") && !insert.getParent().getType().name.equals("VariableDeclarationFragment"))
                {
                    continue;
                }
                if (!Arrays.asList(new String[]{"double", "Double"}).contains(TreeHelper.getDeclarationType(insert.getParent().getChildren().get(0), TreeHelper.getLabelInside(insert.getParent().getChildren().get(0)))))
                {
                    continue;
                }
                String newLabel = TreeHelper.getLabelInside(insert.getNode());
                for (int j = 0; j < actions.size(); j++)
                {
                    if (actions.get(j) instanceof Delete || actions.get(j) instanceof TreeDelete)
                    {
                        Action delete = actions.get(j);
                        if (delete.getNode().getParent() != insert.getParent())
                        {
                            continue;
                        }
                        String oldLabel = TreeHelper.getLabelInside(delete.getNode());
                        if (delete.getNode().getType().name.equals("QualifiedName"))
                        {
                            if (!oldLabel.equals("Double.NaN"))
                            {
                                continue;
                            }
                        }
                        else
                        {
                            if (!delete.getNode().getType().name.equals("NumberLiteral") && !delete.getNode().getType().name.equals("PrefixExpression"))
                            {
                                continue;
                            }
                        }
                        if (!isDoubleReplaceSupported(oldLabel, newLabel))
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
                        results.add(new MutatedNode(clonedTree, this, 25, "Replaced return double " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                    }

                }
            }
        }
        return results;
    }

    private boolean isIntByteShortReplaceSupported(String oldLabel, String newLabel)
    {
        if (oldLabel.equals("1"))
        {
            return newLabel.equals("0");
        }
        if (oldLabel.equals("-1"))
        {
            return newLabel.equals("1");
        }
        if (oldLabel.equals("5"))
        {
            return newLabel.equals("-1");
        }
        try
        {
            int oldValue = Integer.parseInt(oldLabel);
            int newValue = Integer.parseInt(newLabel);
            return oldValue + 1 == newValue;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    private boolean isLongReplaceSupported(String oldLabel, String newLabel)
    {
        if (oldLabel.equals("1"))
        {
            return newLabel.equals("0");
        }
        try
        {
            final long oldValue = Long.parseLong(oldLabel);
            final long newValue = Long.parseLong(newLabel);
            return newValue == oldValue + 1;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    private boolean isFloatReplaceSupported(String oldLabel, String newLabel)
    {
        try
        {
            final float oldValue = Float.parseFloat(oldLabel);
            final float newValue = Float.parseFloat(newLabel);
            if (oldValue == 1.0f || oldValue == 2.0f)
            {
                return newValue == 0.0f;
            }
            return newValue == 1.0f;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    private boolean isDoubleReplaceSupported(String oldLabel, String newLabel)
    {
        try
        {
            final double oldValue = Double.parseDouble(oldLabel);
            final double newValue = Double.parseDouble(newLabel);
            if (oldValue == 1.0)
            {
                return newValue == 0.0;
            }
            return newValue == 1.0;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    private boolean isBooleanReplaceSupported(String oldLabel, String newLabel)
    {
        switch (oldLabel)
        {
            case "false":
                return newLabel.equals("true");
            case "true":
                return newLabel.equals("false");
        }
        return false;
    }
}
