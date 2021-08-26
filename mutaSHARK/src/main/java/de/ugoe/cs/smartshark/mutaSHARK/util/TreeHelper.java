package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.tree.FakeTree;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TreeHelper
{
    private static final String methodTreeTypeName = "MethodDeclaration";
    private static final String variableAssignmentTreeTypeName = "Assignment";
    private static final String variableDeclarationTreeTypeName = "VariableDeclarationFragment";
    private static final String methodInvocationTreeTypeName = "MethodInvocation";
    private static final String blockTreeTypeName = "Block";
    private static final String statementSuffixTypeName = "Statement";
    private static final String simpleNameTypeName = "SimpleName";

    public static List<ITree> findMethods(ITree tree)
    {
        return tree.getDescendants().stream().filter(t -> t.getType().name.equalsIgnoreCase(methodTreeTypeName)).collect(Collectors.toList());
    }

    public static List<ITree> findStatements(ITree tree)
    {
        return tree.getDescendants().stream().filter(t -> t.getType().name.toLowerCase().endsWith(statementSuffixTypeName.toLowerCase())).collect(Collectors.toList());
    }

    public static List<ITree> findBlocks(ITree tree)
    {
        return tree.getDescendants().stream().filter(t -> t.getType().name.equalsIgnoreCase(blockTreeTypeName)).collect(Collectors.toList());
    }

    public static ITree updateTree(ITree tree)
    {
        int length = tree.getLabel().length();
        tree.setLength(length);
        int pos = tree.getPos() + tree.getLength();
        for (ITree child : tree.getChildren())
        {
            child.setPos(pos);
            updateTree(child);
            int childLength = child.getLength();
            pos += childLength;
            tree.setLength(tree.getLength() + childLength);
        }
        return tree;
    }

    public static List<ITree> findMethodInvocations(ITree tree)
    {
        return tree.getDescendants().stream().filter(t -> t.getType().name.equalsIgnoreCase(methodInvocationTreeTypeName)).collect(Collectors.toList());
    }

    public static List<ITree> findVariableAssignments(ITree tree, boolean includeDeclarations)
    {
        List<ITree> result = tree.getDescendants().stream().filter(t -> t.getType().name.equalsIgnoreCase(variableAssignmentTreeTypeName)).collect(Collectors.toList());
        if (includeDeclarations)
        {
            result.addAll(tree.getDescendants().stream().filter(t -> t.getType().name.equalsIgnoreCase(variableDeclarationTreeTypeName)).collect(Collectors.toList()));
        }
        return result;
    }

    public static List<String> getNames(ITree tree)
    {
        return tree.getDescendants().stream().filter(t -> t.getType().name.equalsIgnoreCase(simpleNameTypeName)).map(ITree::getLabel).collect(Collectors.toList());
    }

    public static ITree findTreeIsomophic(ITree tree, ITree elementToFind)
    {
        List<ITree> findings = tree.getDescendants().stream().filter(t -> t.isIsomorphicTo(elementToFind)).collect(Collectors.toList());
        if (findings.size() > 1)
        {
            throw new ArrayIndexOutOfBoundsException("Too many fitting tree nodes");
        }
        for (ITree finding : findings)
        {
            return finding;
        }
        return null;
    }

    public static int getDepthFrom(ITree level1, ITree level2)
    {
        int level = 0;
        while (level2 != level1)
        {
            level++;
            level2 = level2.getParent();
        }
        return level;
    }

    public static String getUrl(ITree tree, int maxLevels)
    {
        String url = "";
        int level = 0;
        while (tree.getParent() != null && level < maxLevels && !(tree.getParent() instanceof FakeTree))
        {
            int childPosition = getChildPosition(tree.getParent(), tree);
            tree = tree.getParent();
            url = childPosition + "." + url;
            level++;
        }
        url = url.substring(0, url.length() - 1);
        return url;
    }

    public static String getLabelInside(ITree node)
    {
        if (node == null)
        {
            return "";
        }
        StringBuilder returnValue = new StringBuilder(node.getLabel());
        for (ITree child : node.getChildren())
        {
            returnValue.append(getLabelInside(child));
        }
        return returnValue.toString();
    }

    public static boolean urlEqual(ITree node1, ITree node2, boolean removeLeadingZeros)
    {
        String url1 = getUrl(node1, Integer.MAX_VALUE);
        String url2 = getUrl(node2, Integer.MAX_VALUE);

        if (removeLeadingZeros)
        {
            while (url1.startsWith("0."))
            {
                url1 = url1.substring(2);
            }
            while (url2.startsWith("0."))
            {
                url2 = url2.substring(2);
            }
        }

        return url1.equalsIgnoreCase(url2);
    }

    public static List<ITree> findTypeDeclarations(ITree tree, String declaration)
    {
        ArrayList<ITree> results = new ArrayList<>();
        for (ITree candidate : tree.breadthFirst())
        {
            if (candidate.getType().name.equalsIgnoreCase("TYPE_DECLARATION_KIND") && candidate.getLabel().contains(declaration))
            {
                results.add(candidate.getParent());
            }
        }
        return results;
    }

    public static List<ITree> findNodes(ITree tree, String typeName, int maxDepth)
    {
        ArrayList<ITree> result = new ArrayList<>();
        for (ITree child : tree.getChildren())
        {
            if (child.getType().name.equals(typeName))
            {
                result.add(child);
            }
            if (maxDepth > 0)
            {
                result.addAll(findNodes(child, typeName, maxDepth - 1));
            }
        }
        return result;
    }

    public static String getDeclarationType(ITree node, String name)
    {
        ITree current = node.getParent();
        while (current != null)
        {
            for (ITree child : current.getChildren())
            {
                final String childName = child.getType().name;
                if (childName.equals("SingleVariableDeclaration") || childName.equals("VariableDeclarationStatement"))
                {
                    if (child.getChildren().size() < 2)
                    {
                        continue;
                    }
                    final String type = child.getChildren().get(0).getLabel();
                    if (child.getChildren().get(1).getType().name.equals("SimpleName") || child.getChildren().get(1).getType().name.equals("QualifiedName"))
                    {
                        if (!child.getChildren().get(1).getLabel().equals(name))
                        {
                            continue;
                        }
                        return type;
                    }
                    if (child.getChildren().get(1).getType().name.equals("VariableDeclarationFragment"))
                    {
                        if (child.getChildren().get(1).getChildren().size() != 2)
                        {
                            continue;
                        }
                        if (!child.getChildren().get(1).getChildren().get(0).getLabel().equals(name))
                        {
                            continue;
                        }
                        return type;
                    }
                }
            }
            current = current.getParent();
        }
        return "";
    }

    private static int getChildPosition(ITree parent, ITree child)
    {
        List<ITree> children = parent.getChildren();
        for (int i = 0; i < children.size(); i++)
        {
            ITree actualChild = children.get(i);
            if (actualChild.isIsomorphicTo(child))
            {
                return i;
            }
        }
        return -1;
    }
}
