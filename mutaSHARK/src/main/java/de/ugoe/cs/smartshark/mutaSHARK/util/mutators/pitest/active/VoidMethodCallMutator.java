package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.active;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.TreeDelete;
import com.github.gumtreediff.tree.ITree;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeHelper;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.PitestMutator;
import java.util.ArrayList;
import java.util.List;

public class VoidMethodCallMutator extends PitestMutator
{
    @Override
    public List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        final ArrayList<MutatedNode> results = new ArrayList<>();

        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Delete || actions.get(i) instanceof TreeDelete)
            {
                Action delete = actions.get(i);
                if (!delete.getNode().getType().name.equals("ExpressionStatement"))
                {
                    continue;
                }
                if (delete.getNode().getChildren().size() != 1)
                {
                    continue;
                }
                final ITree methodInvocation = delete.getNode().getChildren().get(0);
                if (!methodInvocation.getType().name.equals("MethodInvocation"))
                {
                    continue;
                }
                ITree deepCopy = treeNode.getTree().deepCopy();
                String parentUrl = TreeHelper.getUrl(delete.getNode().getParent(), Integer.MAX_VALUE);
                ITree parent = deepCopy.getChild(parentUrl);
                String nodeUrl = TreeHelper.getUrl(delete.getNode(), Integer.MAX_VALUE);
                ITree node = deepCopy.getChild(nodeUrl);
                TreeNode parentNode = new TreeNode(parent);
                parentNode.removeChild(node);
                results.add(new MutatedNode(new TreeNode(deepCopy), this, 1, "Removed (void)-method invocation " + TreeHelper.getLabelInside(delete.getNode()) + " @~" + delete.getNode().getPos()));
            }
        }

        return results;
    }
}
