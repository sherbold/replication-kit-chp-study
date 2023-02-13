package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.optional;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import de.ugoe.cs.smartshark.mutaSHARK.util.*;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.PitestMutator;
import java.util.ArrayList;
import java.util.List;

public class RelationalOperatorReplacementMutator extends PitestMutator
{
    @Override
    public List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        ArrayList<MutatedNode> results = new ArrayList<>();
        results.addAll(getPossibleROR1Mutations(treeNode, target, actions));
        results.addAll(getPossibleROR2Mutations(treeNode, target, actions));
        results.addAll(getPossibleROR3Mutations(treeNode, target, actions));
        results.addAll(getPossibleROR4Mutations(treeNode, target, actions));
        results.addAll(getPossibleROR5Mutations(treeNode, target, actions));
        return results;
    }

    private List<MutatedNode> getPossibleROR1Mutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert)
            {
                Insert insert = (Insert) actions.get(i);
                if (!insert.getParent().getType().name.equals("InfixExpression"))
                {
                    continue;
                }
                if (!insert.getNode().getType().name.equals("INFIX_EXPRESSION_OPERATOR"))
                {
                    continue;
                }
                for (int j = 0; j < actions.size(); j++)
                {
                    if (!(actions.get(j) instanceof Delete))
                    {
                        continue;
                    }
                    Delete delete = (Delete) actions.get(j);
                    if (delete.getNode().getParent() != insert.getParent())
                    {
                        continue;
                    }
                    if (!delete.getNode().getType().name.equals("INFIX_EXPRESSION_OPERATOR"))
                    {
                        continue;
                    }
                    String originalLabel = delete.getNode().getLabel();
                    String newLabel = insert.getNode().getLabel();
                    if (!supportsLabelTransitionROR1(originalLabel, newLabel))
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
                    results.add(new MutatedNode(clonedTree, this, 25, "Replaced boundary " + originalLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                }
            }
        }
        return results;
    }

    private boolean supportsLabelTransitionROR1(String originalLabel, String newLabel)
    {
        switch (originalLabel)
        {
            case "<":
                return newLabel.equalsIgnoreCase("<=");
            case "<=":
            case ">":
            case ">=":
            case "==":
            case "!=":
                return newLabel.equalsIgnoreCase("<");
        }
        return false;
    }

    private List<MutatedNode> getPossibleROR2Mutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert)
            {
                Insert insert = (Insert) actions.get(i);
                if (!insert.getParent().getType().name.equals("InfixExpression"))
                {
                    continue;
                }
                if (!insert.getNode().getType().name.equals("INFIX_EXPRESSION_OPERATOR"))
                {
                    continue;
                }
                for (int j = 0; j < actions.size(); j++)
                {
                    if (!(actions.get(j) instanceof Delete))
                    {
                        continue;
                    }
                    Delete delete = (Delete) actions.get(j);
                    if (delete.getNode().getParent() != insert.getParent())
                    {
                        continue;
                    }
                    if (!delete.getNode().getType().name.equals("INFIX_EXPRESSION_OPERATOR"))
                    {
                        continue;
                    }
                    String originalLabel = delete.getNode().getLabel();
                    String newLabel = insert.getNode().getLabel();
                    if (!supportsLabelTransitionROR2(originalLabel, newLabel))
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
                    results.add(new MutatedNode(clonedTree, this, 25, "Replaced boundary " + originalLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                }
            }
        }
        return results;
    }

    private boolean supportsLabelTransitionROR2(String originalLabel, String newLabel)
    {
        switch (originalLabel)
        {
            case "<":
            case "<=":
                return newLabel.equalsIgnoreCase(">");
            case ">":
            case ">=":
            case "==":
            case "!=":
                return newLabel.equalsIgnoreCase("<=");
        }
        return false;
    }

    private List<MutatedNode> getPossibleROR3Mutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert)
            {
                Insert insert = (Insert) actions.get(i);
                if (!insert.getParent().getType().name.equals("InfixExpression"))
                {
                    continue;
                }
                if (!insert.getNode().getType().name.equals("INFIX_EXPRESSION_OPERATOR"))
                {
                    continue;
                }
                for (int j = 0; j < actions.size(); j++)
                {
                    if (!(actions.get(j) instanceof Delete))
                    {
                        continue;
                    }
                    Delete delete = (Delete) actions.get(j);
                    if (delete.getNode().getParent() != insert.getParent())
                    {
                        continue;
                    }
                    if (!delete.getNode().getType().name.equals("INFIX_EXPRESSION_OPERATOR"))
                    {
                        continue;
                    }
                    String originalLabel = delete.getNode().getLabel();
                    String newLabel = insert.getNode().getLabel();
                    if (!supportsLabelTransitionROR3(originalLabel, newLabel))
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
                    results.add(new MutatedNode(clonedTree, this, 25, "Replaced boundary " + originalLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                }
            }
        }
        return results;
    }

    private boolean supportsLabelTransitionROR3(String originalLabel, String newLabel)
    {
        switch (originalLabel)
        {
            case "<":
            case "<=":
            case ">":
                return newLabel.equalsIgnoreCase(">=");
            case ">=":
            case "==":
            case "!=":
                return newLabel.equalsIgnoreCase(">");
        }
        return false;
    }

    private List<MutatedNode> getPossibleROR4Mutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert)
            {
                Insert insert = (Insert) actions.get(i);
                if (!insert.getParent().getType().name.equals("InfixExpression"))
                {
                    continue;
                }
                if (!insert.getNode().getType().name.equals("INFIX_EXPRESSION_OPERATOR"))
                {
                    continue;
                }
                for (int j = 0; j < actions.size(); j++)
                {
                    if (!(actions.get(j) instanceof Delete))
                    {
                        continue;
                    }
                    Delete delete = (Delete) actions.get(j);
                    if (delete.getNode().getParent() != insert.getParent())
                    {
                        continue;
                    }
                    if (!delete.getNode().getType().name.equals("INFIX_EXPRESSION_OPERATOR"))
                    {
                        continue;
                    }
                    String originalLabel = delete.getNode().getLabel();
                    String newLabel = insert.getNode().getLabel();
                    if (!supportsLabelTransitionROR4(originalLabel, newLabel))
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
                    results.add(new MutatedNode(clonedTree, this, 25, "Replaced boundary " + originalLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                }
            }
        }
        return results;
    }

    private boolean supportsLabelTransitionROR4(String originalLabel, String newLabel)
    {
        switch (originalLabel)
        {
            case "<":
            case "<=":
            case ">":
            case ">=":
                return newLabel.equalsIgnoreCase("==");
            case "==":
            case "!=":
                return newLabel.equalsIgnoreCase(">=");
        }
        return false;
    }

    private List<MutatedNode> getPossibleROR5Mutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert)
            {
                Insert insert = (Insert) actions.get(i);
                if (!insert.getParent().getType().name.equals("InfixExpression"))
                {
                    continue;
                }
                if (!insert.getNode().getType().name.equals("INFIX_EXPRESSION_OPERATOR"))
                {
                    continue;
                }
                for (int j = 0; j < actions.size(); j++)
                {
                    if (!(actions.get(j) instanceof Delete))
                    {
                        continue;
                    }
                    Delete delete = (Delete) actions.get(j);
                    if (delete.getNode().getParent() != insert.getParent())
                    {
                        continue;
                    }
                    if (!delete.getNode().getType().name.equals("INFIX_EXPRESSION_OPERATOR"))
                    {
                        continue;
                    }
                    String originalLabel = delete.getNode().getLabel();
                    String newLabel = insert.getNode().getLabel();
                    if (!supportsLabelTransitionROR5(originalLabel, newLabel))
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
                    results.add(new MutatedNode(clonedTree, this, 25, "Replaced boundary " + originalLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                }
            }
        }
        return results;
    }

    private boolean supportsLabelTransitionROR5(String originalLabel, String newLabel)
    {
        switch (originalLabel)
        {
            case "<":
            case "<=":
            case ">":
            case ">=":
            case "==":
                return newLabel.equalsIgnoreCase("!=");
            case "!=":
                return newLabel.equalsIgnoreCase("==");
        }
        return false;
    }
}