package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.ITree;
import de.ugoe.cs.smartshark.mutaSHARK.util.defects4j.Defects4JBugFix;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;

public class ActionExecutor
{
    public ActionExecutor()
    {
    }


    public void executeAction(Action action)
    {
        if (action instanceof Insert)
        {
            executeInsert((Insert) action);
        }
        else if (action instanceof Addition)
        {
            executeAddition((Addition) action);
        }
        else if (action instanceof Move)
        {
            executeMove((Move) action);
        }
        else if (action instanceof Update)
        {
            executeUpdate((Update) action);
        }
        else if (action instanceof Delete)
        {
            executeDelete((Delete) action);
        }
        else if (action instanceof Replace)
        {
            executeReplace((Replace) action);
        }
        else
        {
            throw new UnsupportedOperationException("The action " + action.getName() + " is unsupported");
        }
    }

    private void executeReplace(Replace action)
    {
        ITree parent = action.getOriginalNode().getParent();
        assert parent == action.getNewNode().getParent();
        ArrayList<ITree> newChildren = new ArrayList<>(parent.getChildren());

        for (int i = 0; i < newChildren.size(); i++)
        {
            ITree oldChild = newChildren.get(i);
            if (oldChild == action.getOriginalNode())
            {
                newChildren.remove(oldChild);
                newChildren.add(i, action.getNewNode());
            }
        }
        parent.setChildren(newChildren);
    }

    private void executeDelete(Delete action)
    {
        TreeNode parent = new TreeNode(action.getNode().getParent());
        parent.removeChild(action.getNode());
    }

    private void executeUpdate(Update action)
    {
        throw new NotImplementedException("executeUpdate not implemented for " + action);
    }

    private void executeMove(Move action)
    {
        TreeNode treeNode = new TreeNode(action.getNode().getParent());
        treeNode.removeChild(action.getNode());
        action.getParent().insertChild(action.getNode(), action.getPosition());
    }

    private void executeAddition(Addition action)
    {
        throw new NotImplementedException("executeUpdate not implemented for " + action);
    }

    private void executeInsert(Insert action)
    {
        ITree node = action.getNode();
        action.getParent().insertChild(node, action.getPosition());
    }

    public Move reassignTree(Move move, ITree clonedTree)
    {
        ITree originalParent = move.getParent();
        ITree originalNode = move.getNode();
        String originalParentUrl = TreeHelper.getUrl(originalParent, Integer.MAX_VALUE);
        String originalNodeUrl = TreeHelper.getUrl(originalNode, Integer.MAX_VALUE);
        ITree newParent = clonedTree.getChild(originalParentUrl);
        ITree newNode = clonedTree.getChild(originalNodeUrl);
        return new Move(newNode, newParent, move.getPosition());
    }

    public Replace reassignTree(Replace replace, ITree clonedTree)
    {
        ITree originalOriginalNode = replace.getOriginalNode();
        ITree originalNewNode = replace.getNewNode();
        String originalOriginalNodeUrl = TreeHelper.getUrl(originalOriginalNode, Integer.MAX_VALUE);
        String originalNewNodeUrl = TreeHelper.getUrl(originalNewNode, Integer.MAX_VALUE);
        ITree newOriginalNode = clonedTree.getChild(originalOriginalNodeUrl);
        ITree newNewNode = clonedTree.getChild(originalNewNodeUrl);
        return new Replace(newOriginalNode, newNewNode);
    }

    public Delete reassignTree(Delete delete, ITree clonedTree)
    {
        ITree originalNode = delete.getNode();
        String originalNodeUrl = TreeHelper.getUrl(originalNode, Integer.MAX_VALUE);
        ITree newNode = clonedTree.getChild(originalNodeUrl);
        return new Delete(newNode);
    }

    public Insert reassignTree(Insert insert, ITree clonedTree)
    {
        ITree originalParent = insert.getParent();
        ITree originalNode = insert.getNode().deepCopy();
        String originalParentUrl = TreeHelper.getUrl(originalParent, Integer.MAX_VALUE);
        ITree newParent = clonedTree.getChild(originalParentUrl);
        return new Insert(originalNode, newParent, insert.getPosition());
    }
}
