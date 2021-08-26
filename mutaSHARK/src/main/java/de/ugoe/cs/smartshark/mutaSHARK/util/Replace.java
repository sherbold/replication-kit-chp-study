package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;

public class Replace extends Action
{
    private final ITree newNode;

    public Replace(ITree originalNode, ITree newNode)
    {
        super(originalNode);
        this.newNode = newNode;
    }

    @Override
    public String getName() {
        return "replace-node";
    }

    public ITree getOriginalNode()
    {
        return this.getNode();
    }
    public ITree getNewNode()
    {
        return newNode;
    }
}
