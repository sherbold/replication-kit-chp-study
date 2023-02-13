package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.optional;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.ITree;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeHelper;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.PitestMutator;
import java.util.ArrayList;
import java.util.List;

public class ArithmeticOperatorDeletionMutator extends PitestMutator {
    @Override
    public List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target, List<Action> actions) {
        ArrayList<MutatedNode> results = new ArrayList<>();
        results.addAll(getPossibleAOD_1Mutations(treeNode, target, actions));
        results.addAll(getPossibleAOD_2Mutations(treeNode, target, actions));
        return results;
    }

    private List<MutatedNode> getPossibleAOD_1Mutations(TreeNode treeNode, TreeNode target, List<Action> actions) {
        ArrayList<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i) instanceof Delete) {
                Delete deleteInfix = (Delete) actions.get(i);
                if (!deleteInfix.getNode().getType().name.equals("InfixExpression")) {
                    continue;
                }
                if (deleteInfix.getNode().getChildren().size() != 3) {
                    continue;
                }
                if (!(deleteInfix.getNode().getChildren().get(1).getType().name.equals("INFIX_EXPRESSION_OPERATOR") && (deleteInfix.getNode().getChildren().get(1).getLabel().equals("+") || deleteInfix.getNode().getChildren().get(1).getLabel().equals("-")|| deleteInfix.getNode().getChildren().get(1).getLabel().equals("*")|| deleteInfix.getNode().getChildren().get(1).getLabel().equals("/")|| deleteInfix.getNode().getChildren().get(1).getLabel().equals("%")))) {
                    continue;
                }
                ITree parent = deleteInfix.getNode().getParent();
                for (int j = 0; j < actions.size(); j++) {
                    if (actions.get(j) instanceof TreeDelete) {
                        TreeDelete innerDelete = (TreeDelete) actions.get(j);
                        if (innerDelete.getNode().getParent() != deleteInfix.getNode()) {
                            continue;
                        }
                        for (int k = 0; k < actions.size(); k++) {
                            if (actions.get(k) instanceof TreeInsert) {
                                TreeInsert insert = (TreeInsert) actions.get(k);
                                if(!deleteInfix.getNode().getChildren().get(0).getLabel().equals(insert.getNode().getLabel()))
                                {
                                    continue;
                                }
                                if (insert.getParent().getPos() != parent.getPos()) {
                                    continue;
                                }
                                if (insert.getParent().getChildren().size() != parent.getChildren().size()) {
                                    continue;
                                }
                                if (!insert.getParent().getType().name.equals(parent.getType().name)) {
                                    continue;
                                }
                                String originalLabel = deleteInfix.getNode().getType().name;;
                                String newLabel = "first operand";

                                ITree deepCopy = treeNode.getTree().deepCopy();
                                TreeNode clonedTree = new TreeNode(deepCopy);
                                String url = TreeHelper.getUrl(parent, Integer.MAX_VALUE);
                                TreeNode parentNode = new TreeNode(clonedTree.getTree().getChild(url));
                                parentNode.removeChildAt(insert.getPosition());
                                parentNode.getTree().insertChild(insert.getNode().deepCopy(), insert.getPosition());
                                results.add(new MutatedNode(clonedTree, this, 25, "Replaced arithmetic "+ originalLabel +" with "+ newLabel + " @~" + parent.getPos()));
                            }
                        }
                    }
                }
            }
        }
        return results;
    }
    private List<MutatedNode> getPossibleAOD_2Mutations(TreeNode treeNode, TreeNode target, List<Action> actions) {
        ArrayList<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i) instanceof Delete) {
                Delete deleteInfix = (Delete) actions.get(i);
                if (!deleteInfix.getNode().getType().name.equals("InfixExpression")) {
                    continue;
                }
                if (deleteInfix.getNode().getChildren().size() != 3) {
                    continue;
                }
                if (!(deleteInfix.getNode().getChildren().get(1).getType().name.equals("INFIX_EXPRESSION_OPERATOR") && (deleteInfix.getNode().getChildren().get(1).getLabel().equals("+") || deleteInfix.getNode().getChildren().get(1).getLabel().equals("-")|| deleteInfix.getNode().getChildren().get(1).getLabel().equals("*")|| deleteInfix.getNode().getChildren().get(1).getLabel().equals("/")|| deleteInfix.getNode().getChildren().get(1).getLabel().equals("%")))) {
                    continue;
                }
                ITree parent = deleteInfix.getNode().getParent();
                for (int j = 0; j < actions.size(); j++) {
                    if (actions.get(j) instanceof TreeDelete) {
                        TreeDelete innerDelete = (TreeDelete) actions.get(j);
                        if (innerDelete.getNode().getParent() != deleteInfix.getNode()) {
                            continue;
                        }
                        for (int k = 0; k < actions.size(); k++) {
                            if (actions.get(k) instanceof TreeInsert) {
                                TreeInsert insert = (TreeInsert) actions.get(k);
                                if(!deleteInfix.getNode().getChildren().get(2).getLabel().equals(insert.getNode().getLabel()))
                                {
                                    continue;
                                }
                                if (insert.getParent().getPos() != parent.getPos()) {
                                    continue;
                                }
                                if (insert.getParent().getChildren().size() != parent.getChildren().size()) {
                                    continue;
                                }
                                if (!insert.getParent().getType().name.equals(parent.getType().name)) {
                                    continue;
                                }

                                String originalLabel = deleteInfix.getNode().getType().name;;
                                String newLabel = "second operand";

                                ITree deepCopy = treeNode.getTree().deepCopy();
                                TreeNode clonedTree = new TreeNode(deepCopy);
                                String url = TreeHelper.getUrl(parent, Integer.MAX_VALUE);
                                TreeNode parentNode = new TreeNode(clonedTree.getTree().getChild(url));
                                parentNode.removeChildAt(insert.getPosition());
                                parentNode.getTree().insertChild(insert.getNode().deepCopy(), insert.getPosition());
                                results.add(new MutatedNode(clonedTree, this, 25, "Replaced arithmetic "+ originalLabel +" with "+ newLabel + " @~" + parent.getPos()));

                            }
                        }
                    }
                }
            }
        }
        return results;
    }
}
