package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.optional;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.ITree;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeHelper;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.PitestMutator;
import java.util.ArrayList;
import java.util.List;

public class BitwiseOperatorMutator extends PitestMutator {
    @Override
    public List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        ArrayList<MutatedNode> results = new ArrayList<>();
        results.addAll(getPossibleOBBN1Mutations(treeNode, target, actions));
        results.addAll(getPossibleOBBN2Mutations(treeNode, target, actions));
        results.addAll(getPossibleOBBN3Mutations(treeNode, target, actions));
        return results;
    }

    private List<MutatedNode> getPossibleOBBN1Mutations(TreeNode treeNode, TreeNode target, List<Action> actions)
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
                        if (!supportsLabelTransition(originalLabel, newLabel)) {
                            continue;
                        }
                        ITree copy = treeNode.getTree().deepCopy();
                        TreeNode clonedTree = new TreeNode(copy);
                        String url = TreeHelper.getUrl(delete.getNode().getParent(), Integer.MAX_VALUE);
                        TreeNode newParent = new TreeNode(clonedTree.getTree().getChild(url));
                        int positionInParent = delete.getNode().positionInParent();
                        newParent.removeChildAt(positionInParent);
                        newParent.getTree().insertChild(insert.getNode().deepCopy(), positionInParent);
                        results.add(new MutatedNode(clonedTree, this, 25, "Replaced Bitwise " + originalLabel + " Operator with " + newLabel + " @~" + delete.getNode().getPos()));
                    }
                }
            }
            return results;
        }

        private boolean supportsLabelTransition (String originalLabel, String newLabel)
        {
            switch (originalLabel) {
                case "&":
                    return newLabel.equalsIgnoreCase("|");
                case "|":
                    return newLabel.equalsIgnoreCase("&");
            }
            return false;
        }

        private List<MutatedNode> getPossibleOBBN2Mutations (TreeNode treeNode, TreeNode target, List < Action > actions)
        {
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
                    if (!(deleteInfix.getNode().getChildren().get(1).getType().name.equals("INFIX_EXPRESSION_OPERATOR") && (deleteInfix.getNode().getChildren().get(1).getLabel().equals("&") || deleteInfix.getNode().getChildren().get(1).getLabel().equals("|")))) {
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
                                    /*if (insert.getParent().getPos() != parent.getPos()) {
                                        continue;
                                    }*/
                                    if (insert.getParent().getChildren().size() != parent.getChildren().size()) {
                                        continue;
                                    }
                                    if (!insert.getParent().getType().name.equals(parent.getType().name)) {
                                        continue;
                                    }

                                    //System.out.println("DELETED1: " + deleteInfix.getNode().getChildren());
                                    //System.out.println("INSERTED1: " + insert.getNode());
                                    String originalLabel = deleteInfix.getNode().getType().name;
                                    String newLabel = "first operand";

                                    ITree deepCopy = treeNode.getTree().deepCopy();
                                    TreeNode clonedTree = new TreeNode(deepCopy);
                                    String url = TreeHelper.getUrl(parent, Integer.MAX_VALUE);
                                    TreeNode parentNode = new TreeNode(clonedTree.getTree().getChild(url));
                                    parentNode.removeChildAt(insert.getPosition());
                                    parentNode.getTree().insertChild(insert.getNode().deepCopy(), insert.getPosition());
                                    results.add(new MutatedNode(clonedTree, this, 25, "Replaced Bitwise " + originalLabel + " Operator with " + newLabel + " @~" + parent.getPos()));
                                }
                            }
                        }
                    }
                }
            }
            return results;
        }
    private List<MutatedNode> getPossibleOBBN3Mutations(TreeNode treeNode, TreeNode target, List<Action> actions) {
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
                if (!(deleteInfix.getNode().getChildren().get(1).getType().name.equals("INFIX_EXPRESSION_OPERATOR") && (deleteInfix.getNode().getChildren().get(1).getLabel().equals("&") || deleteInfix.getNode().getChildren().get(1).getLabel().equals("|")))) {
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
                                results.add(new MutatedNode(clonedTree, this, 25, "Replaced Bitwise "+ originalLabel +" Operator with "+ newLabel + " @~" + parent.getPos()));
                            }
                        }
                    }
                }
            }
        }
        return results;
    }
}


