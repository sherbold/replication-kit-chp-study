package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.cheated;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.ITree;
import de.ugoe.cs.smartshark.mutaSHARK.util.InsertWrapper;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeHelper;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.PitestMutator;

import java.util.ArrayList;
import java.util.List;

public class RelaxedEmptyReturnsMutator extends PitestMutator
{
    @Override
    public List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();

        results.addAll(getPossibleNumberMutations(treeNode, actions));
        results.addAll(getPossibleStringMutations(treeNode, actions));
        results.addAll(getPossibleOptionalsMutations(treeNode, actions));
        results.addAll(getPossibleListMutations(treeNode, actions));
        results.addAll(getPossibleSetMutations(treeNode, actions));

        return results;
    }

    private List<MutatedNode> getPossibleNumberMutations(TreeNode treeNode, List<Action> actions)
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
                if (!isInMethodWithReturnType(insert.getNode(), "int", "byte", "short", "Integer", "Byte", "Short", "long", "Long", "float", "Float", "double", "Double", "char", "Character"))
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
                        if (!isInMethodWithReturnType(insert.getNode(), "int", "byte", "short", "Integer", "Byte", "Short", "long", "Long", "float", "Float", "double", "Double", "char", "Character"))
                        {
                            continue;
                        }
                        String oldLabel = TreeHelper.getLabelInside(delete.getNode());
                        if (!isNumberReplaceSupported(oldLabel, newLabel))
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
                        results.add(new MutatedNode(clonedTree, this, 75, "Cheated-Replaced empty return number " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                    }
                }
            }
        }
        return results;
    }

    private List<MutatedNode> getPossibleStringMutations(TreeNode treeNode, List<Action> actions)
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
                if (!isInMethodWithReturnType(insert.getNode(), "String"))
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
                        if (!isInMethodWithReturnType(insert.getNode(), "String"))
                        {
                            continue;
                        }
                        String oldLabel = TreeHelper.getLabelInside(delete.getNode());
                        if (!isStringReplaceSupported(oldLabel, newLabel))
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
                        results.add(new MutatedNode(clonedTree, this, 75, "Cheated-Replaced empty return String " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                    }
                }
            }
        }
        return results;
    }

    private List<MutatedNode> getPossibleOptionalsMutations(TreeNode treeNode, List<Action> actions)
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
                if (!insert.getNode().getType().name.equals("MethodInvocation"))
                {
                    continue;
                }
                if (insert.getNode().getChildren().size() != 2)
                {
                    continue;
                }
                if (!insert.getNode().getChildren().get(1).getType().name.equals("SimpleName"))
                {
                    continue;
                }
                if (!insert.getNode().getChildren().get(1).getLabel().equals("empty"))
                {
                    continue;
                }
                if (!insert.getNode().getChildren().get(0).getType().name.equals("METHOD_INVOCATION_RECEIVER"))
                {
                    continue;
                }
                if (!TreeHelper.getLabelInside(insert.getNode().getChildren().get(0)).equals("Optional"))
                {
                    continue;
                }
                if (!isInMethodWithReturnType(insert.getNode(), "Optional"))
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
                        if (!isInMethodWithReturnType(insert.getNode(), "Optional"))
                        {
                            continue;
                        }
                        String oldLabel = TreeHelper.getLabelInside(delete.getNode());
                        if (!isOptionalReplaceSupported(oldLabel, newLabel))
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
                        results.add(new MutatedNode(clonedTree, this, 75, "Cheated-Replaced empty return Optional " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                    }
                }
            }
        }
        return results;
    }

    private List<MutatedNode> getPossibleListMutations(TreeNode treeNode, List<Action> actions)
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
                if (!insert.getNode().getType().name.equals("MethodInvocation"))
                {
                    continue;
                }
                if (insert.getNode().getChildren().size() != 2)
                {
                    continue;
                }
                if (!insert.getNode().getChildren().get(1).getType().name.equals("SimpleName"))
                {
                    continue;
                }
                if (!insert.getNode().getChildren().get(1).getLabel().equals("emptyList"))
                {
                    continue;
                }
                if (!insert.getNode().getChildren().get(0).getType().name.equals("METHOD_INVOCATION_RECEIVER"))
                {
                    continue;
                }
                if (!TreeHelper.getLabelInside(insert.getNode().getChildren().get(0)).equals("Collections"))
                {
                    continue;
                }
                if (!isInMethodWithReturnType(insert.getNode(), "List", "Collection", "ArrayList"))
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
                        if (!isInMethodWithReturnType(insert.getNode(), "List", "Collection", "ArrayList"))
                        {
                            continue;
                        }
                        String oldLabel = TreeHelper.getLabelInside(delete.getNode());
                        if (!isListReplaceSupported(oldLabel, newLabel))
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
                        results.add(new MutatedNode(clonedTree, this, 75, "Cheated-Replaced empty return List " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                    }
                }
            }
        }
        return results;
    }

    private List<MutatedNode> getPossibleSetMutations(TreeNode treeNode, List<Action> actions)
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
                if (!insert.getNode().getType().name.equals("MethodInvocation"))
                {
                    continue;
                }
                if (insert.getNode().getChildren().size() != 2)
                {
                    continue;
                }
                if (!insert.getNode().getChildren().get(1).getType().name.equals("SimpleName"))
                {
                    continue;
                }
                if (!insert.getNode().getChildren().get(1).getLabel().equals("emptySet"))
                {
                    continue;
                }
                if (!insert.getNode().getChildren().get(0).getType().name.equals("METHOD_INVOCATION_RECEIVER"))
                {
                    continue;
                }
                if (!TreeHelper.getLabelInside(insert.getNode().getChildren().get(0)).equals("Collections"))
                {
                    continue;
                }
                if (!isInMethodWithReturnType(insert.getNode(), "Set", "HashSet"))
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
                        if (!isInMethodWithReturnType(insert.getNode(), "Set", "HashSet"))
                        {
                            continue;
                        }
                        String oldLabel = TreeHelper.getLabelInside(delete.getNode());
                        if (!isSetReplaceSupported(oldLabel, newLabel))
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
                        results.add(new MutatedNode(clonedTree, this, 75, "Cheated-Replaced empty return Set " + oldLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                    }
                }
            }
        }
        return results;
    }

    private boolean isNumberReplaceSupported(String oldLabel, String newLabel)
    {
        return newLabel.equals("0");
    }

    private boolean isSetReplaceSupported(String oldLabel, String newLabel)
    {
        return newLabel.equals("CollectionsemptySet");
    }

    private boolean isListReplaceSupported(String oldLabel, String newLabel)
    {
        return newLabel.equals("CollectionsemptyList");
    }

    private boolean isOptionalReplaceSupported(String oldLabel, String newLabel)
    {
        return newLabel.equals("Optionalempty");
    }

    private boolean isStringReplaceSupported(String oldLabel, String newLabel)
    {
        return newLabel.equals("\"\"");
    }

    private boolean isInMethodWithReturnType(ITree node, String... returnTypes)
    {
        return true;
    }
}
