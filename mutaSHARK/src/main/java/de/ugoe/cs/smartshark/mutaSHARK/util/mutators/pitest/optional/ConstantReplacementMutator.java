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

public class ConstantReplacementMutator extends PitestMutator
{
    @Override
    public List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        results.addAll(getPossibleCRCR1Mutations(treeNode, actions));
        results.addAll(getPossibleCRCR2Mutations(treeNode, actions));
        results.addAll(getPossibleCRCR3Mutations(treeNode, actions));
        results.addAll(getPossibleCRCR4Mutations(treeNode, actions));
        results.addAll(getPossibleCRCR5Mutations(treeNode, actions));
        results.addAll(getPossibleCRCR6Mutations(treeNode, actions));
        return results;
    }

    private List<MutatedNode> getPossibleCRCR1Mutations(TreeNode treeNode, List<Action> actions)
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
                if (!Arrays.asList(new String[]{"int", "byte", "short", "Integer", "Byte", "Short", "float", "Float", "double", "Double", "long", "Long"}).contains(TreeHelper.getDeclarationType(insert.getParent().getChildren().get(0), TreeHelper.getLabelInside(insert.getParent().getChildren().get(0)))))
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
                        if (!isCRCR1ReplaceSupported(oldLabel, newLabel))
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
                        results.add(new MutatedNode(clonedTree, this, 25, "Replaced inline constant " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                    }
                }
            }
        }
        return results;
    }

    private List<MutatedNode> getPossibleCRCR2Mutations(TreeNode treeNode, List<Action> actions)
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
                if (!Arrays.asList(new String[]{"int", "byte", "short", "Integer", "Byte", "Short", "float", "Float", "double", "Double", "long", "Long"}).contains(TreeHelper.getDeclarationType(insert.getParent().getChildren().get(0), TreeHelper.getLabelInside(insert.getParent().getChildren().get(0)))))
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
                        if (!isCRCR2ReplaceSupported(oldLabel, newLabel))
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
                        results.add(new MutatedNode(clonedTree, this, 25, "Replaced inline constant " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                    }
                }
            }
        }
        return results;
    }

    private List<MutatedNode> getPossibleCRCR3Mutations(TreeNode treeNode, List<Action> actions)
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
                if (!Arrays.asList(new String[]{"int", "byte", "short", "Integer", "Byte", "Short", "float", "Float", "double", "Double", "long", "Long"}).contains(TreeHelper.getDeclarationType(insert.getParent().getChildren().get(0), TreeHelper.getLabelInside(insert.getParent().getChildren().get(0)))))
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
                        if (!isCRCR3ReplaceSupported(oldLabel, newLabel))
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
                        results.add(new MutatedNode(clonedTree, this, 25, "Replaced inline constant " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                    }
                }
            }
        }
        return results;
    }

    private List<MutatedNode> getPossibleCRCR4Mutations(TreeNode treeNode, List<Action> actions)
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
                if (!Arrays.asList(new String[]{"int", "byte", "short", "Integer", "Byte", "Short", "float", "Float", "double", "Double", "long", "Long"}).contains(TreeHelper.getDeclarationType(insert.getParent().getChildren().get(0), TreeHelper.getLabelInside(insert.getParent().getChildren().get(0)))))
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
                        if (!isCRCR4ReplaceSupported(oldLabel, newLabel))
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
                        results.add(new MutatedNode(clonedTree, this, 25, "Replaced inline constant " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                    }
                }
            }
        }
        return results;
    }

    private List<MutatedNode> getPossibleCRCR5Mutations(TreeNode treeNode, List<Action> actions)
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
                if (!Arrays.asList(new String[]{"int", "byte", "short", "Integer", "Byte", "Short", "float", "Float", "double", "Double", "long", "Long"}).contains(TreeHelper.getDeclarationType(insert.getParent().getChildren().get(0), TreeHelper.getLabelInside(insert.getParent().getChildren().get(0)))))
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
                        if (!isCRCR5ReplaceSupported(oldLabel, newLabel))
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
                        results.add(new MutatedNode(clonedTree, this, 25, "Replaced inline constant " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                    }
                }
            }
        }
        return results;
    }

    private List<MutatedNode> getPossibleCRCR6Mutations(TreeNode treeNode, List<Action> actions)
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
                if (!Arrays.asList(new String[]{"int", "byte", "short", "Integer", "Byte", "Short", "float", "Float", "double", "Double", "long", "Long"}).contains(TreeHelper.getDeclarationType(insert.getParent().getChildren().get(0), TreeHelper.getLabelInside(insert.getParent().getChildren().get(0)))))
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
                        if (!isCRCR6ReplaceSupported(oldLabel, newLabel))
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
                        results.add(new MutatedNode(clonedTree, this, 25, "Replaced inline constant " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                    }
                }
            }
        }
        return results;
    }

    private boolean isCRCR1ReplaceSupported(String oldLabel, String newLabel)
    {
       try
        {
            double newValue = Double.parseDouble(newLabel);
            return newValue == 1;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    private boolean isCRCR2ReplaceSupported(String oldLabel, String newLabel)
    {
        try
        {
            double newValue = Double.parseDouble(newLabel);
            return newValue == 0;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    private boolean isCRCR3ReplaceSupported(String oldLabel, String newLabel)
    {
        try
        {
            double newValue = Double.parseDouble(newLabel);
            return newValue == -1;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    private boolean isCRCR4ReplaceSupported(String oldLabel, String newLabel)
    {
        try
        {
            double oldValue = Double.parseDouble(oldLabel);
            double newValue = Double.parseDouble(newLabel);
            return newValue == -oldValue;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    private boolean isCRCR5ReplaceSupported(String oldLabel, String newLabel)
    {
        try
        {
            double oldValue = Double.parseDouble(oldLabel);
            double newValue = Double.parseDouble(newLabel);
            return newValue == oldValue + 1;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
    private boolean isCRCR6ReplaceSupported(String oldLabel, String newLabel)
    {
        try
        {
            double oldValue = Double.parseDouble(oldLabel);
            double newValue = Double.parseDouble(newLabel);
            return newValue == oldValue - 1;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
}
