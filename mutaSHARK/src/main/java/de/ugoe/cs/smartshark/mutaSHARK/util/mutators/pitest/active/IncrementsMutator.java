package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.active;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.ITree;
import de.ugoe.cs.smartshark.mutaSHARK.util.*;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.PitestMutator;
import java.util.ArrayList;
import java.util.List;

public class IncrementsMutator extends PitestMutator
{
    @Override
    public List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        ArrayList<MutatedNode> results = new ArrayList<>();
        results.addAll(getPossibleMutations(treeNode, target, actions, "PrefixExpression", "PREFIX_EXPRESSION_OPERATOR"));
        results.addAll(getPossibleMutations(treeNode, target, actions, "PostfixExpression", "POSTFIX_EXPRESSION_OPERATOR"));
        results.addAll(getPossibleMutations(treeNode, target, actions, "InfixExpression", "INFIX_EXPRESSION_OPERATOR"));
        results.addAll(getPossibleShortCutMutations(treeNode, target, actions));
        results.addAll(getPossibleLongFormMutations(treeNode, target, actions));
        return results;
    }

    private List<MutatedNode> getPossibleShortCutMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        ArrayList<MutatedNode> results = new ArrayList<>();

        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert)
            {
                Insert assignmentInsert = (Insert) actions.get(i);
                if (!assignmentInsert.getNode().getType().name.equals("Assignment"))
                {
                    continue;
                }
                if (assignmentInsert.getNode().getChildren().size() != 3)
                {
                    continue;
                }
                if (!assignmentInsert.getNode().getChildren().get(1).getType().name.equals("ASSIGNEMENT_OPERATOR"))
                {
                    continue;
                }
                if (!assignmentInsert.getNode().getChildren().get(2).getType().name.equals("InfixExpression"))
                {
                    continue;
                }
                if (assignmentInsert.getNode().getChildren().get(2).getChildren().size() != 3)
                {
                    continue;
                }
                if (!assignmentInsert.getNode().getChildren().get(2).getChildren().get(1).getType().name.equals("INFIX_EXPRESSION_OPERATOR"))
                {
                    continue;
                }
                if (!assignmentInsert.getNode().getChildren().get(2).getChildren().get(2).getType().name.equals("NumberLiteral"))
                {
                    continue;
                }
                if (!assignmentInsert.getNode().getChildren().get(2).getChildren().get(2).getLabel().equals("1"))
                {
                    continue;
                }
                if (!assignmentInsert.getNode().getChildren().get(0).getType().name.equals("SimpleName") && !assignmentInsert.getNode().getChildren().get(0).getType().name.equals("QualifiedName"))
                {
                    continue;
                }
                if (!assignmentInsert.getNode().getChildren().get(2).getChildren().get(0).getType().name.equals("SimpleName") && !assignmentInsert.getNode().getChildren().get(2).getChildren().get(0).getType().name.equals("QualifiedName"))
                {
                    continue;
                }
                if (!assignmentInsert.getNode().getChildren().get(2).getChildren().get(0).getLabel().equals(assignmentInsert.getNode().getChildren().get(0).getLabel()))
                {
                    continue;
                }
                String newLabel = assignmentInsert.getNode().getChildren().get(2).getChildren().get(1).getLabel();
                for (int j = 0; j < actions.size(); j++)
                {
                    if (actions.get(j) instanceof TreeInsert)
                    {
                        TreeInsert referenceInsert = (TreeInsert) actions.get(j);
                        if (referenceInsert.getParent() != assignmentInsert.getNode())
                        {
                            continue;
                        }
                        for (int k = 0; k < actions.size(); k++)
                        {
                            if (actions.get(k) instanceof Delete)
                            {
                                Delete postfixDelete = (Delete) actions.get(k);
                                if (!postfixDelete.getNode().getType().name.equals("PostfixExpression"))
                                {
                                    continue;
                                }
                                if (!postfixDelete.getNode().getParent().getType().name.equals("ExpressionStatement"))
                                {
                                    continue;
                                }
                                if (postfixDelete.getNode().getChildren().size() != 2)
                                {
                                    continue;
                                }
                                String postfixOperator = postfixDelete.getNode().getChildren().get(1).getLabel();
                                if (!isSupportedShortCutReplacement(newLabel, postfixOperator))
                                {
                                    continue;
                                }
                                for (int h = 0; h < actions.size(); h++)
                                {
                                    if (actions.get(h) instanceof TreeDelete)
                                    {
                                        TreeDelete deleteReference = (TreeDelete) actions.get(h);
                                        if (deleteReference.getNode().getParent() != postfixDelete.getNode())
                                        {
                                            continue;
                                        }
                                        if (deleteReference.getNode().getChildren().size() > 0)
                                        {
                                            continue;
                                        }
                                        ITree deepCopy = treeNode.getTree().deepCopy();
                                        TreeNode clonedTree = new TreeNode(deepCopy);
                                        String url = TreeHelper.getUrl(assignmentInsert.getParent(), Integer.MAX_VALUE);
                                        ITree parent = clonedTree.getTree().getChild(url);
                                        TreeNode parentNode = new TreeNode(parent);
                                        parentNode.removeChildAt(assignmentInsert.getPosition());
                                        parentNode.getTree().insertChild(assignmentInsert.getNode().deepCopy(), assignmentInsert.getPosition());
                                        results.add(new MutatedNode(clonedTree, this, 1, "Replaced shortcut de-/increment (" + postfixOperator + " to " + newLabel + ") @~" + deleteReference.getNode().getPos()));
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }

        return results;
    }

    private boolean isSupportedShortCutReplacement(String newLabel, String oldLabel)
    {
        switch (newLabel)
        {
            case "-":
                return oldLabel.equals("--");
            case "+":
                return oldLabel.equals("++");
            case "--":
                return oldLabel.equals("-");
            case "++":
                return oldLabel.equals("+");
        }
        return false;
    }

    private List<MutatedNode> getPossibleLongFormMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        ArrayList<MutatedNode> results = new ArrayList<>();

        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Delete)
            {
                Delete assignmentDelete = (Delete) actions.get(i);
                if (!assignmentDelete.getNode().getType().name.equals("Assignment"))
                {
                    continue;
                }
                if (assignmentDelete.getNode().getChildren().size() != 3)
                {
                    continue;
                }
                if (!assignmentDelete.getNode().getChildren().get(1).getType().name.equals("ASSIGNEMENT_OPERATOR"))
                {
                    continue;
                }
                if (!assignmentDelete.getNode().getChildren().get(2).getType().name.equals("InfixExpression"))
                {
                    continue;
                }
                if (assignmentDelete.getNode().getChildren().get(2).getChildren().size() != 3)
                {
                    continue;
                }
                if (!assignmentDelete.getNode().getChildren().get(2).getChildren().get(1).getType().name.equals("INFIX_EXPRESSION_OPERATOR"))
                {
                    continue;
                }
                if (!assignmentDelete.getNode().getChildren().get(2).getChildren().get(2).getType().name.equals("NumberLiteral"))
                {
                    continue;
                }
                if (!assignmentDelete.getNode().getChildren().get(2).getChildren().get(2).getLabel().equals("1"))
                {
                    continue;
                }
                if (!assignmentDelete.getNode().getChildren().get(0).getType().name.equals("SimpleName") && !assignmentDelete.getNode().getChildren().get(0).getType().name.equals("QualifiedName"))
                {
                    continue;
                }
                if (!assignmentDelete.getNode().getChildren().get(2).getChildren().get(0).getType().name.equals("SimpleName") && !assignmentDelete.getNode().getChildren().get(2).getChildren().get(0).getType().name.equals("QualifiedName"))
                {
                    continue;
                }
                if (!assignmentDelete.getNode().getChildren().get(2).getChildren().get(0).getLabel().equals(assignmentDelete.getNode().getChildren().get(0).getLabel()))
                {
                    continue;
                }
                String oldLabel = assignmentDelete.getNode().getChildren().get(2).getChildren().get(1).getLabel();
                for (int j = 0; j < actions.size(); j++)
                {
                    if (actions.get(j) instanceof TreeDelete)
                    {
                        TreeDelete referenceDelete = (TreeDelete) actions.get(j);
                        if (referenceDelete.getNode().getParent() != assignmentDelete.getNode())
                        {
                            continue;
                        }
                        for (int k = 0; k < actions.size(); k++)
                        {
                            if (actions.get(k) instanceof Insert)
                            {
                                Insert postfixInsert = (Insert) actions.get(k);
                                if (!postfixInsert.getNode().getType().name.equals("PostfixExpression"))
                                {
                                    continue;
                                }
                                if (!postfixInsert.getNode().getParent().getType().name.equals("ExpressionStatement"))
                                {
                                    continue;
                                }
                                if (postfixInsert.getNode().getChildren().size() != 2)
                                {
                                    continue;
                                }
                                String postfixOperator = postfixInsert.getNode().getChildren().get(1).getLabel();
                                if (!isSupportedShortCutReplacement(postfixOperator, oldLabel))
                                {
                                    continue;
                                }
                                for (int h = 0; h < actions.size(); h++)
                                {
                                    if (actions.get(h) instanceof TreeInsert)
                                    {
                                        TreeInsert insertReference = (TreeInsert) actions.get(h);
                                        if (insertReference.getNode().getParent() != postfixInsert.getNode())
                                        {
                                            continue;
                                        }
                                        if (insertReference.getNode().getChildren().size() > 0)
                                        {
                                            continue;
                                        }
                                        ITree deepCopy = treeNode.getTree().deepCopy();
                                        TreeNode clonedTree = new TreeNode(deepCopy);
                                        String url = TreeHelper.getUrl(postfixInsert.getParent(), Integer.MAX_VALUE);
                                        ITree parent = clonedTree.getTree().getChild(url);
                                        TreeNode parentNode = new TreeNode(parent);
                                        parentNode.removeChildAt(postfixInsert.getPosition());
                                        parentNode.getTree().insertChild(postfixInsert.getNode().deepCopy(), postfixInsert.getPosition());
                                        results.add(new MutatedNode(clonedTree, this, 1, "Replaced shortcut de-/increment (" + oldLabel + " to " + postfixOperator + ") @~" + insertReference.getNode().getPos()));
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }

        return results;
    }

    private boolean supportsLabelTransition(String originalLabel, String newLabel)
    {
        switch (originalLabel)
        {
            case "--":
                return newLabel.equalsIgnoreCase("++");
            case "++":
                return newLabel.equalsIgnoreCase("--");
            case "-":
                return newLabel.equalsIgnoreCase("+");
            case "+":
                return newLabel.equalsIgnoreCase("-");
        }
        return false;
    }

    private List<MutatedNode> getPossibleMutations(TreeNode treeNode,
                                                   TreeNode target,
                                                   List<Action> actions,
                                                   String expressionName,
                                                   String operatorName)
    {
        List<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert)
            {
                Insert insert = (Insert) actions.get(i);
                if (!insert.getParent().getType().name.equals(expressionName))
                {
                    continue;
                }
                if (!insert.getNode().getType().name.equals(operatorName))
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
                    if (!delete.getNode().getType().name.equals(operatorName))
                    {
                        continue;
                    }
                    String originalLabel = delete.getNode().getLabel();
                    String newLabel = insert.getNode().getLabel();
                    if (!supportsLabelTransition(originalLabel, newLabel))
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
                    results.add(new MutatedNode(clonedTree, this, 1, "Replaced " + originalLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                }
            }
        }
        return results;
    }
}
