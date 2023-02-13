package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.optional;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.TreeDelete;
import com.github.gumtreediff.tree.ITree;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeHelper;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.PitestMutator;
import java.util.ArrayList;
import java.util.List;

public class RemoveConditionalsMutator extends PitestMutator
{
    @Override
    public List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert)
            {
                Insert insert = (Insert) actions.get(i);
                if (!insert.getNode().getType().name.equals("BooleanLiteral"))
                {
                    continue;
                }
                for (int j = 0; j < actions.size(); j++)
                {
                    //System.out.println(j+" ACTION: " + actions.get(j).getNode().getParent().getType()); //ZD
                    if (!(actions.get(j) instanceof TreeDelete)) //ZD added TREE
                    {
                        continue;
                    }
                    TreeDelete delete = (TreeDelete) actions.get(j); //ZD added TREE
                    //System.out.println("Parent delete: " + delete.getNode().getParent() + " Parent Insert: " + insert.getParent());//ZD
                    if (delete.getNode().getParent() != insert.getParent())
                    {
                        continue;
                    }
                    if (!(delete.getNode().getType().name.equals("InfixExpression") && (delete.getNode().getChildren().get(1).getType().name.equals("INFIX_EXPRESSION_OPERATOR")) && ((delete.getNode().getChildren().get(1).getLabel().equals("==")) || ((delete.getNode().getChildren().get(1).getLabel().equals("!="))||(delete.getNode().getChildren().get(1).getLabel().equals("<"))||(delete.getNode().getChildren().get(1).getLabel().equals("<="))||(delete.getNode().getChildren().get(1).getLabel().equals(">"))||(delete.getNode().getChildren().get(1).getLabel().equals(">="))))))
                    {
                        continue;
                    }
                    String originalLabel = delete.getNode().getType().name;
                    String newLabel = insert.getNode().getLabel();
                    //System.out.println("OLD LABEL: " + originalLabel + " New LABEL: " + newLabel);//ZD
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
                    results.add(new MutatedNode(clonedTree, this, 25, "Replaced " + delete.getNode().getParent().getType().name + " conditional " + originalLabel + " with " + newLabel + " @~" + delete.getNode().getPos()));
                }
            }
        }
        return results;
    }

    private boolean supportsLabelTransition(String originalLabel, String newLabel)
    {
        switch (originalLabel)
        {
            case "InfixExpression":
                if(newLabel.equalsIgnoreCase("true") || newLabel.equalsIgnoreCase("false")) {
                    return true;
                }
        }
        return false;
    }
}
