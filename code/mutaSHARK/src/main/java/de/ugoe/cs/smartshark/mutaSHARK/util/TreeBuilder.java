package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.tree.ITree;

public class TreeBuilder
{
    private ITree currentTree;

    public TreeBuilder(ITree currentTree)
    {
        this.currentTree = currentTree;
    }

    public static TreeBuilder newBuilder(ITree currentTree)
    {
        return new TreeBuilder(currentTree);
    }

}

