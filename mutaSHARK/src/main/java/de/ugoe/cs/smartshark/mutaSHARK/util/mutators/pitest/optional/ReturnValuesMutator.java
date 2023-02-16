package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.optional;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.ITree;
import de.ugoe.cs.smartshark.mutaSHARK.util.InsertWrapper;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeHelper;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.PitestMutator;
import java.util.ArrayList;
import java.util.List;

public class ReturnValuesMutator extends PitestMutator
{
    @Override
    public List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        results.addAll(getPossibleBooleanMutations(treeNode, actions));
        results.addAll(getPossibleObjectMutations(treeNode, actions));
        results.addAll(getPossibleIntByteShortNumberMutations(treeNode, actions));
        results.addAll(getPossibleLongNumberMutations(treeNode, actions));
        results.addAll(getPossibleFloatDoubleNumberMutations(treeNode, actions));

        return results;
    }

    private List<MutatedNode> getPossibleObjectMutations(TreeNode treeNode, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert || actions.get(i) instanceof TreeInsert)
            {
                InsertWrapper insert = new InsertWrapper(actions.get(i));
                if (!insert.getNode().getType().name.equals("ReturnStatement"))
                {
                    continue;
                }
                if (insert.getNode().getChildren().size() != 1)
                {
                    continue;
                }
                if (!insert.getNode().getChildren().get(0).getType().name.equals("NullLiteral"))
                {
                    continue;
                }
                for (int j = 0; j < actions.size(); j++)
                {
                    if (actions.get(j) instanceof Delete || actions.get(j) instanceof TreeDelete)
                    {
                        Action delete = actions.get(j);
                        if (!delete.getNode().getType().name.equals("ReturnStatement"))
                        {
                            continue;
                        }
                        if (delete.getNode().getParent() != insert.getParent())
                        {
                            continue;
                        }
                        if (delete.getNode().getChildren().size() != 1)
                        {
                            continue;
                        }
                        if (!delete.getNode().getChildren().get(0).getType().name.equals("ClassInstanceCreation"))
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
                        results.add(new MutatedNode(clonedTree, this, 25, "Replaced return object " + TreeHelper.findNodes(delete.getNode(), "SimpleName", Integer.MAX_VALUE).get(0).getLabel() + " with null @~" + delete.getNode().getPos()));
                    }
                }
            }
        }
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
                if (!insert.getParent().getType().name.equals("ReturnStatement"))
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
                        results.add(new MutatedNode(clonedTree, this, 25, "Replaced return boolean " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
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
                if (!insert.getParent().getType().name.equals("ReturnStatement"))
                {
                    continue;
                }
                if (!isInMethodWithReturnType(insert.getNode(), "int", "byte", "short", "Integer", "Byte", "Short"))
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
                        if (!isInMethodWithReturnType(delete.getNode(), "int", "byte", "short", "Integer", "Byte", "Short"))
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
                        results.add(new MutatedNode(clonedTree, this, 25, "Replaced return shortbyteint " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
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
                if (!insert.getParent().getType().name.equals("ReturnStatement"))
                {
                    continue;
                }
                if (!isInMethodWithReturnType(insert.getNode(), "long", "Long"))
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
                        if (!isInMethodWithReturnType(delete.getNode(), "long", "Long"))
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
                        results.add(new MutatedNode(clonedTree, this, 25, "Replaced return long " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                    }
                }
            }
        }
        return results;
    }

    private List<MutatedNode> getPossibleFloatDoubleNumberMutations(TreeNode treeNode, List<Action> actions)
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
                if (!insert.getParent().getType().name.equals("ReturnStatement"))
                {
                    continue;
                }
                if (!isInMethodWithReturnType(insert.getNode(), "double", "Double", "float", "Float"))
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
                        if (!isInMethodWithReturnType(delete.getNode(), "double", "Double", "float", "Float"))
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
                        if (!isFloatDoubleReplaceSupported(oldLabel, newLabel))
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
                        results.add(new MutatedNode(clonedTree, this, 25, "Replaced return floatdouble " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                    }

                }
            }
        }
        return results;
    }

    private boolean isIntByteShortReplaceSupported(String oldLabel, String newLabel)
    {
        if (oldLabel.equals("0"))
        {
            return newLabel.equals("1");
        }
        return newLabel.equals("0");
    }

    private boolean isLongReplaceSupported(String oldLabel, String newLabel)
    {
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

    private boolean isFloatDoubleReplaceSupported(String oldLabel, String newLabel)
    {
        try
        {
            final double newValue = Double.parseDouble(newLabel);
            if (oldLabel.equals("Double.NaN"))
            {
                return newValue == 0;
            }
            final double oldValue = Double.parseDouble(oldLabel);
            return newValue == -(oldValue + 1.0);
        }
        catch (NumberFormatException e)
        {
            return false;
        }
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
        }
        return false;
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

