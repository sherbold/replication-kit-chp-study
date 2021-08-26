package de.ugoe.cs.smartshark.mutaSHARK.util.expressions;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TypeSet;

import java.util.ArrayList;
import java.util.List;

import static de.ugoe.cs.smartshark.mutaSHARK.util.TreeHelper.updateTree;

public abstract class Expression
{
    protected final List<Expression> children = new ArrayList<>();

    private ITree createTree(ITree parent)
    {
        String typeName = getTypeName();
        Tree tree = new Tree(TypeSet.type(typeName));
        tree.setLabel(getLabel());
        for (Expression child : children)
        {
            ITree childTree = child.createTree(tree);
            tree.addChild(childTree);
        }

        return tree;
    }

    public ITree insertTree(ITree parent, int pos)
    {
        ITree tree = createTree(parent);
        if (parent != null)
        {
            tree.setParent(parent);
            parent.insertChild(tree, pos);
        }
        if (parent != null)
            updateTree(parent);
        else
            updateTree(tree);
        return tree;
    }



    protected abstract String getTypeName();

    protected abstract String getLabel();
}
