package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.optional;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeHelper;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.PitestMutator;
import java.util.ArrayList;
import java.util.List;

public class ArithmeticOperatorReplacementMutator extends PitestMutator
{
    @Override
    public List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        ArrayList<MutatedNode> results = new ArrayList<>();
        results.addAll(getPossibleAOR_1Mutations(treeNode, target, actions));
        results.addAll(getPossibleAOR_2Mutations(treeNode, target, actions));
        results.addAll(getPossibleAOR_3Mutations(treeNode, target, actions));
        results.addAll(getPossibleAOR_4Mutations(treeNode, target, actions));
        return results;
    }

    private List<MutatedNode> getPossibleAOR_1Mutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i) instanceof Insert) {
                Insert insert = (Insert) actions.get(i);
                if (!insert.getParent().getType().name.equals("InfixExpression")) {
                    continue;
                }
                if (!insert.getNode().getType().name.equals("INFIX_EXPRESSION_OPERATOR")) {
                    continue;
                }
                for (int j = 0; j < actions.size(); j++) {
                    if (!(actions.get(j) instanceof Delete)) {
                        continue;
                    }
                    Delete delete = (Delete) actions.get(j);
                    if (delete.getNode().getParent() != insert.getParent()) {
                        continue;
                    }
                    if (!delete.getNode().getType().name.equals("INFIX_EXPRESSION_OPERATOR")) {
                        continue;
                    }
                    String originalLabel = delete.getNode().getLabel();
                    String newLabel = insert.getNode().getLabel();
                    if (!supportsLabelTransitionAOR_1(originalLabel, newLabel)) {
                        continue;
                    }
                    ITree copy = treeNode.getTree().deepCopy();
                    TreeNode clonedTree = new TreeNode(copy);
                    String url = TreeHelper.getUrl(delete.getNode().getParent(), Integer.MAX_VALUE);
                    TreeNode newParent = new TreeNode(clonedTree.getTree().getChild(url));
                    int positionInParent = delete.getNode().positionInParent();
                    newParent.removeChildAt(positionInParent);
                    newParent.getTree().insertChild(insert.getNode().deepCopy(), positionInParent);
                    results.add(new MutatedNode(clonedTree, this, 25, "Replaced math " + originalLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                }
            }
        }
        return results;
    }

    private boolean supportsLabelTransitionAOR_1(String originalLabel, String newLabel)
    {
        switch (originalLabel)
        {
            case "+":
                return newLabel.equalsIgnoreCase("-");
            case "-":
                return newLabel.equalsIgnoreCase("+");
            case "*":
                return newLabel.equalsIgnoreCase("/");
            case "/":
            case "%":
                return newLabel.equalsIgnoreCase("*");
        }
        return false;
    }

    private List<MutatedNode> getPossibleAOR_2Mutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i) instanceof Insert) {
                Insert insert = (Insert) actions.get(i);
                if (!insert.getParent().getType().name.equals("InfixExpression")) {
                    continue;
                }
                if (!insert.getNode().getType().name.equals("INFIX_EXPRESSION_OPERATOR")) {
                    continue;
                }
                for (int j = 0; j < actions.size(); j++) {
                    if (!(actions.get(j) instanceof Delete)) {
                        continue;
                    }
                    Delete delete = (Delete) actions.get(j);
                    if (delete.getNode().getParent() != insert.getParent()) {
                        continue;
                    }
                    if (!delete.getNode().getType().name.equals("INFIX_EXPRESSION_OPERATOR")) {
                        continue;
                    }
                    String originalLabel = delete.getNode().getLabel();
                    String newLabel = insert.getNode().getLabel();
                    if (!supportsLabelTransitionAOR_2(originalLabel, newLabel)) {
                        continue;
                    }
                    ITree copy = treeNode.getTree().deepCopy();
                    TreeNode clonedTree = new TreeNode(copy);
                    String url = TreeHelper.getUrl(delete.getNode().getParent(), Integer.MAX_VALUE);
                    TreeNode newParent = new TreeNode(clonedTree.getTree().getChild(url));
                    int positionInParent = delete.getNode().positionInParent();
                    newParent.removeChildAt(positionInParent);
                    newParent.getTree().insertChild(insert.getNode().deepCopy(), positionInParent);
                    results.add(new MutatedNode(clonedTree, this, 25, "Replaced math " + originalLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                }
            }
        }
        return results;
    }

    private boolean supportsLabelTransitionAOR_2(String originalLabel, String newLabel)
    {
        switch (originalLabel)
        {
            case "+":
            case "-":
                return newLabel.equalsIgnoreCase("*");
            case "*":
            case "/":
                return newLabel.equalsIgnoreCase("%");
            case "%":
                return newLabel.equalsIgnoreCase("/");
        }
        return false;
    }

    private List<MutatedNode> getPossibleAOR_3Mutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i) instanceof Insert) {
                Insert insert = (Insert) actions.get(i);
                if (!insert.getParent().getType().name.equals("InfixExpression")) {
                    continue;
                }
                if (!insert.getNode().getType().name.equals("INFIX_EXPRESSION_OPERATOR")) {
                    continue;
                }
                for (int j = 0; j < actions.size(); j++) {
                    if (!(actions.get(j) instanceof Delete)) {
                        continue;
                    }
                    Delete delete = (Delete) actions.get(j);
                    if (delete.getNode().getParent() != insert.getParent()) {
                        continue;
                    }
                    if (!delete.getNode().getType().name.equals("INFIX_EXPRESSION_OPERATOR")) {
                        continue;
                    }
                    String originalLabel = delete.getNode().getLabel();
                    String newLabel = insert.getNode().getLabel();
                    if (!supportsLabelTransitionAOR_3(originalLabel, newLabel)) {
                        continue;
                    }
                    ITree copy = treeNode.getTree().deepCopy();
                    TreeNode clonedTree = new TreeNode(copy);
                    String url = TreeHelper.getUrl(delete.getNode().getParent(), Integer.MAX_VALUE);
                    TreeNode newParent = new TreeNode(clonedTree.getTree().getChild(url));
                    int positionInParent = delete.getNode().positionInParent();
                    newParent.removeChildAt(positionInParent);
                    newParent.getTree().insertChild(insert.getNode().deepCopy(), positionInParent);
                    results.add(new MutatedNode(clonedTree, this, 25, "Replaced math " + originalLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                }
            }
        }
        return results;
    }

    private boolean supportsLabelTransitionAOR_3(String originalLabel, String newLabel)
    {
        switch (originalLabel)
        {
            case "+":
            case "-":
                return newLabel.equalsIgnoreCase("/");
            case "*":
            case "/":
            case "%":
                return newLabel.equalsIgnoreCase("+");
        }
        return false;
    }

    private List<MutatedNode> getPossibleAOR_4Mutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i) instanceof Insert) {
                Insert insert = (Insert) actions.get(i);
                if (!insert.getParent().getType().name.equals("InfixExpression")) {
                    continue;
                }
                if (!insert.getNode().getType().name.equals("INFIX_EXPRESSION_OPERATOR")) {
                    continue;
                }
                for (int j = 0; j < actions.size(); j++) {
                    if (!(actions.get(j) instanceof Delete)) {
                        continue;
                    }
                    Delete delete = (Delete) actions.get(j);
                    if (delete.getNode().getParent() != insert.getParent()) {
                        continue;
                    }
                    if (!delete.getNode().getType().name.equals("INFIX_EXPRESSION_OPERATOR")) {
                        continue;
                    }
                    String originalLabel = delete.getNode().getLabel();
                    String newLabel = insert.getNode().getLabel();
                    if (!supportsLabelTransitionAOR_4(originalLabel, newLabel)) {
                        continue;
                    }
                    ITree copy = treeNode.getTree().deepCopy();
                    TreeNode clonedTree = new TreeNode(copy);
                    String url = TreeHelper.getUrl(delete.getNode().getParent(), Integer.MAX_VALUE);
                    TreeNode newParent = new TreeNode(clonedTree.getTree().getChild(url));
                    int positionInParent = delete.getNode().positionInParent();
                    newParent.removeChildAt(positionInParent);
                    newParent.getTree().insertChild(insert.getNode().deepCopy(), positionInParent);
                    results.add(new MutatedNode(clonedTree, this, 25, "Replaced math " + originalLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                }
            }
        }
        return results;
    }

    private boolean supportsLabelTransitionAOR_4(String originalLabel, String newLabel)
    {
        switch (originalLabel)
        {
            case "+":
            case "-":
                return newLabel.equalsIgnoreCase("%");
            case "*":
            case "/":
            case "%":
                return newLabel.equalsIgnoreCase("-");
        }
        return false;
    }
}








