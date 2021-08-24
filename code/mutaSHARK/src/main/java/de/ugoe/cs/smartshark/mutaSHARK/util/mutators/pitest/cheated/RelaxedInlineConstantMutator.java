package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.cheated;

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

public class RelaxedInlineConstantMutator extends PitestMutator
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
                        results.add(new MutatedNode(clonedTree, this, 75, "Cheated-Replaced inline boolean " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
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
                        results.add(new MutatedNode(clonedTree, this, 75, "Cheated-Replaced inline shortbyteint " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
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
                        results.add(new MutatedNode(clonedTree, this, 75, "Cheated-Replaced inline long " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
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
                        results.add(new MutatedNode(clonedTree, this, 75, "Cheated-Replaced inline float " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
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
                        results.add(new MutatedNode(clonedTree, this, 75, "Cheated-Replaced return double " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                    }

                }
            }
        }
        return results;
    }

    private boolean isIntByteShortReplaceSupported(String oldLabel, String newLabel)
    {
        try
        {
            Integer.parseInt(oldLabel);
            Integer.parseInt(newLabel);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    private boolean isLongReplaceSupported(String oldLabel, String newLabel)
    {
        try
        {
            Long.parseLong(oldLabel);
            Long.parseLong(newLabel);
            return true;
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
            Float.parseFloat(oldLabel);
            Float.parseFloat(newLabel);
            return true;
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
            Double.parseDouble(oldLabel);
            Double.parseDouble(newLabel);
            return true;
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
