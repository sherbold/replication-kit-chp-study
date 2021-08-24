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

public class ConstructCallMutator extends PitestMutator
{
    @Override
    public List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        List<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Delete || actions.get(i) instanceof TreeDelete)
            {
                Action delete = actions.get(i);
                if (!delete.getNode().getType().name.equals("ClassInstanceCreation"))
                {
                    continue;
                }
                for (int j = 0; j < actions.size(); j++)
                {
                    if (actions.get(j) instanceof Insert || actions.get(j) instanceof TreeInsert)
                    {
                        InsertWrapper insert = new InsertWrapper(actions.get(j));
                        if (!insert.getNode().getType().name.equals("NullLiteral"))
                        {
                            continue;
                        }
                        if (insert.getParent() != delete.getNode().getParent())
                        {
                            continue;
                        }
                        ITree deepCopy = treeNode.getTree().deepCopy();
                        String url = TreeHelper.getUrl(delete.getNode().getParent(), Integer.MAX_VALUE);
                        ITree parent = deepCopy.getChild(url);
                        TreeNode parentNode = new TreeNode(parent);
                        parentNode.removeChildAt(Math.min(insert.getPosition(), parentNode.getTree().getChildren().size() - 1));
                        parentNode.getTree().insertChild(insert.getNode().deepCopy(), Math.min(parentNode.getTree().getChildren().size(), insert.getPosition()));
                        results.add(new MutatedNode(new TreeNode(deepCopy), this, 25, "Optional-Constructor call removed (new " + TreeHelper.getLabelInside(delete.getNode()) + ") @~" + delete.getNode().getPos()));
                    }
                }
            }
        }
        return results;
    }
}

