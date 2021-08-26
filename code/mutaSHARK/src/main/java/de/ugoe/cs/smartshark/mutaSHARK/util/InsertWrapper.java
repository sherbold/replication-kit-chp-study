package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.TreeInsert;
import com.github.gumtreediff.tree.ITree;

public class InsertWrapper
{
    private final Action action;

    public InsertWrapper(Action action)
    {
        this.action = action;
    }

    public ITree getNode()
    {
        return action.getNode();
    }

    public ITree getParent()
    {
        if (action instanceof Insert)
        {
            return ((Insert) action).getParent();
        }
        return ((TreeInsert) action).getParent();
    }

    public String getName()
    {
        return action.getName();
    }

    public int getPosition()
    {
        if (action instanceof Insert)
        {
            return ((Insert) action).getPosition();
        }
        return ((TreeInsert) action).getPosition();
    }
}
