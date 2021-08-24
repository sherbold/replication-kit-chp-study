package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.io.TreeIoUtils;
import com.github.gumtreediff.tree.*;

import java.util.*;

public class TreeNode
{
    private final ITree tree;

    public TreeNode(ITree tree)
    {
        this.tree = tree;
    }

    public ITree getTree()
    {
        return tree;
    }

    public void removeChild(ITree child)
    {
        ArrayList<ITree> children = new ArrayList<>(tree.getChildren());
        children.remove(child);
        child.setParent(null);
        tree.setChildren(children);
    }

    public void removeChildAt(int index)
    {
        ArrayList<ITree> children = new ArrayList<>(tree.getChildren());
        children.get(index).setParent(null);
        children.remove(index);
        tree.setChildren(children);
    }

    @Override
    public String toString()
    {
        return "TreeNode{" + "tree=" + tree + '}';
    }
}

