package de.ugoe.cs.smartshark.mutaSHARK;

import com.github.gumtreediff.gen.Generators;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import de.ugoe.cs.smartshark.mutaSHARK.util.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MutaShark
{
    private static SearchResult searchResult;

    // -b|-bug path/to/bug.java -f|-fix path/to/fix.java -m|-mutator full.name.of.mutator1 full.name.of.mutator2 ...
    // path/to/bug.java path/to/fix.java  full.name.of.mutator1 full.name.of.mutator2 ...
    // path/to/bug.java path/to/fix.java (in this case all mutations in package util.mutators are used)
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, TooManyActionsException
    {
        searchResult = null;
        StartUpOptions startUpOptions = StartUpOptions.parseArgs(args);

        com.github.gumtreediff.client.Run.initGenerators(); // registers the available parsers

        TreeContext treeFrom = Generators.getInstance().getTree(startUpOptions.pathToFix);
        TreeContext treeTo = Generators.getInstance().getTree(startUpOptions.pathToBug);

        TreeNode toNode = getCleanedUpTree(treeTo); // new TreeNode(TreeHelper.updateTree(treeTo.getRoot()));
        TreeNode fromNode = getCleanedUpTree(treeFrom); // new TreeNode(TreeHelper.updateTree(treeFrom.getRoot()));
        GreedySearch search = new GreedySearch(fromNode, toNode);

        searchResult = search.findPaths(new SearchSettings(startUpOptions.maxPathCount, startUpOptions.maxPathDepth, toNode, Arrays.asList(startUpOptions.mutations)));

        if (searchResult.foundPaths.size() == 0)
        {
            for (SearchPath foundPath : searchResult.closestPaths)
            {
                List<TreeNode> mutators = foundPath.edges.stream().map(e -> e.getToSearchNode().getCurrentTreeNode()).collect(Collectors.toList());
                System.out.println(mutators);
            }
        }
        else
        {
            for (SearchPath foundPath : searchResult.foundPaths)
            {
                List<TreeNode> mutators = foundPath.edges.stream().map(e -> e.getToSearchNode().getCurrentTreeNode()).collect(Collectors.toList());
                System.out.println(mutators);
            }
        }
    }

    private static TreeNode getCleanedUpTree(TreeContext treeContext)
    {
        ITree tree = TreeHelper.updateTree(treeContext.getRoot());
        TreeNode treeNode = new TreeNode(tree);
        removeNodes(tree, "PackageDeclaration", 1);
        removeNodes(tree, "ImportDeclaration", 1);
        removeNodes(tree, "Javadoc", Integer.MAX_VALUE);
        TreeHelper.updateTree(treeNode.getTree());
        return treeNode;
    }

    private static void removeNodes(ITree tree, String nodeName, int maxDepth)
    {
        List<ITree> nodes = TreeHelper.findNodes(tree, nodeName, maxDepth);
        for (ITree node : nodes)
        {
            TreeNode parent = new TreeNode(node.getParent());
            parent.removeChild(node);
        }
    }

    public static SearchResult getSearchResult()
    {
        return searchResult;
    }
}

