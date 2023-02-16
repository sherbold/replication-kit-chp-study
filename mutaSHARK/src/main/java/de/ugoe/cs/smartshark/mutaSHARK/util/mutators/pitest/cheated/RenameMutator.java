package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.cheated;

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

public class RenameMutator extends PitestMutator
{
    @Override
    public List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        ArrayList<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            if (actions.get(i) instanceof Insert)
            {
                Insert insert = (Insert) actions.get(i);
                if (!insert.getNode().getType().name.equals("SimpleName") && !insert.getNode().getType().name.equals("QualifiedName"))
                {
                    continue;
                }
                for (int j = 0; j < actions.size(); j++)
                {
                    if (actions.get(j) instanceof Delete)
                    {
                        Delete delete = (Delete) actions.get(j);
                        if (!delete.getNode().getType().name.equals("SimpleName") && !delete.getNode().getType().name.equals("QualifiedName"))
                        {
                            continue;
                        }
                        if (delete.getNode().getParent() != insert.getParent())
                        {
                            continue;
                        }
                        ITree deepCopy = treeNode.getTree().deepCopy();
                        String url = TreeHelper.getUrl(delete.getNode().getParent(), Integer.MAX_VALUE);
                        ITree parent = deepCopy.getChild(url);
                        TreeNode parentNode = new TreeNode(parent);
                        parentNode.removeChildAt(Math.min(insert.getPosition(), parentNode.getTree().getChildren().size() - 1));
                        parentNode.getTree().insertChild(insert.getNode().deepCopy(), Math.min(parentNode.getTree().getChildren().size(), insert.getPosition()));
                        results.add(new MutatedNode(new TreeNode(deepCopy), this, 100, "Cheated-Renamed " + delete.getNode().getLabel() + " to " + insert.getNode().getLabel() + " @~" + delete.getNode().getPos()));
                    }
                }
            }
        }
        return results;
    }
}
