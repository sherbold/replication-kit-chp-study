package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.actions.model.Action;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.TreeMutationOperator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GreedySearch implements ISearchAlgorithm
{
    private final TreeNode fromNode;
    private final TreeNode toNode;

    public GreedySearch(TreeNode fromNode, TreeNode toNode)
    {
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    @Override
    public SearchResult findPaths(SearchSettings searchSettings) throws TooManyActionsException
    {
        return findPaths(new SearchNode(fromNode, searchSettings.heuristic), new SearchNode(toNode, searchSettings.heuristic), new ArrayList<SearchPath>(), new ArrayList<SearchPath>(), searchSettings, 0, Integer.MAX_VALUE, 0);
    }

    private SearchResult findPaths(SearchNode current,
                                   SearchNode to,
                                   ArrayList<SearchPath> foundPaths,
                                   ArrayList<SearchPath> closestPaths,
                                   SearchSettings searchSettings,
                                   int depth,
                                   int lastActionCount,
                                   int lastActionCountRepeating) throws TooManyActionsException
    {
        boolean newPathFound = false;
        if (current.getCurrentTreeNode().getTree().isIsomorphicTo(to.getCurrentTreeNode().getTree()))
        {
            SearchPath foundPath = new SearchPath(current, new DiffTree(fromNode.getTree(), toNode.getTree()).getActions().size(), new DiffTree(current.getCurrentTreeNode().getTree(), toNode.getTree()).getActions().size());
            if (!contains(foundPaths, foundPath))
            {
                foundPaths.add(foundPath);
                newPathFound = true;
            }
            return new SearchResult(new ArrayList<>(foundPaths), new ArrayList<>(closestPaths));
        }

        if (depth >= searchSettings.maxHops)
        {
            SearchPath closest = new SearchPath(current, new DiffTree(fromNode.getTree(), toNode.getTree()).getActions().size(), new DiffTree(current.getCurrentTreeNode().getTree(), toNode.getTree()).getActions().size());
            if (!contains(closestPaths, closest))
            {
                closestPaths.add(closest);
                newPathFound = true;
            }
        }
        if (foundPaths.size() + closestPaths.size() >= searchSettings.maxFoundPaths)
        {
            return new SearchResult(new ArrayList<>(foundPaths), new ArrayList<>(closestPaths));
        }
        List<Action> actions = new DiffTree(current.getCurrentTreeNode().getTree(), to.getCurrentTreeNode().getTree()).getActions();
        int actionSize = actions.size();
        if (actionSize == lastActionCount && lastActionCountRepeating >= 3)
        {
            SearchPath foundPath = new SearchPath(current, new DiffTree(fromNode.getTree(), toNode.getTree()).getActions().size(), new DiffTree(current.getCurrentTreeNode().getTree(), toNode.getTree()).getActions().size());
            if (!contains(closestPaths, foundPath))
            {
                closestPaths.add(foundPath);
                newPathFound = true;
            }
            return new SearchResult(new ArrayList<>(foundPaths), new ArrayList<>(closestPaths));
        }
        List<MutatedNode> possibleMutations = new ArrayList<>();
        for (TreeMutationOperator mutationOperator : searchSettings.mutationOperators)
        {
            List<MutatedNode> operatorPossibleMutations = mutationOperator.getPossibleMutations(current.getCurrentTreeNode(), to.getCurrentTreeNode(), actions);
            possibleMutations.addAll(operatorPossibleMutations);
        }
        actions = null;

        List<SearchNode> priorityQueue = new ArrayList<>();
        possibleMutations.sort(Comparator.comparingDouble(MutatedNode::getCost));
        for (MutatedNode operatorPossibleMutation : possibleMutations)
        {
            if (priorityQueue.size() >= searchSettings.maxFoundPaths)
            {
                break;
            }
            priorityQueue.add(new SearchNode(operatorPossibleMutation, current, searchSettings.heuristic));
        }
        possibleMutations.clear();
        while (priorityQueue.size() > 0)
        {
            possibleMutations.add((MutatedNode) priorityQueue.remove(0).getCurrentTreeNode());
        }
        for (MutatedNode possibleMutation : possibleMutations)
        {
            SearchNode searchNode = new SearchNode(possibleMutation, current, searchSettings.heuristic);
            SearchResult tempSearchResult = findPaths(searchNode, to, new ArrayList<>(foundPaths), new ArrayList<>(closestPaths), searchSettings, depth + 1, actionSize, actionSize == lastActionCount ? lastActionCountRepeating + 1 : 0);
            for (SearchPath foundPath : tempSearchResult.foundPaths)
            {
                if (!contains(foundPaths, foundPath))
                {
                    foundPaths.add(foundPath);
                    newPathFound = true;
                }
            }
            for (SearchPath foundPath : tempSearchResult.closestPaths)
            {
                if (!contains(closestPaths, foundPath))
                {
                    closestPaths.add(foundPath);
                    newPathFound = true;
                }
            }
            if (foundPaths.size() + closestPaths.size() >= searchSettings.maxFoundPaths)
            {
                return new SearchResult(new ArrayList<>(foundPaths), new ArrayList<>(closestPaths));
            }
        }

        if (possibleMutations.size() == 0)
        {
            SearchPath closestPath = new SearchPath(current, new DiffTree(fromNode.getTree(), toNode.getTree()).getActions().size(), new DiffTree(current.getCurrentTreeNode().getTree(), toNode.getTree()).getActions().size());
            if (!contains(closestPaths, closestPath))
            {
                closestPaths.add(closestPath);
                newPathFound = true;
            }
            return new SearchResult(new ArrayList<>(foundPaths), new ArrayList<>(closestPaths));
        }
        return new SearchResult(new ArrayList<>(foundPaths), new ArrayList<>(closestPaths));
    }

    private boolean contains(ArrayList<SearchPath> paths, SearchPath path)
    {
        for (SearchPath searchPath : paths)
        {
            if (searchPath.edges.size() == path.edges.size())
            {
                boolean allEqual = true;
                for (int i = 0; i < searchPath.edges.size(); i++)
                {
                    SearchEdge searchPathEdge = searchPath.edges.get(i);
                    SearchEdge pathEdge = path.edges.get(i);
                    if (!isEqual(searchPathEdge, pathEdge))
                    {
                        allEqual = false;
                    }

                }
                if (allEqual)
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isEqual(SearchEdge edge1, SearchEdge edge2)
    {
        if (!edge1.getToSearchNode().getCurrentTreeNode().getTree().isIsomorphicTo(edge2.getToSearchNode().getCurrentTreeNode().getTree()))
        {
            return false;
        }
        if (edge1.getToSearchNode().getCurrentTreeNode() instanceof MutatedNode && edge2.getToSearchNode().getCurrentTreeNode() instanceof MutatedNode)
        {
            MutatedNode mutated1 = (MutatedNode) edge1.getToSearchNode().getCurrentTreeNode();
            MutatedNode mutated2 = (MutatedNode) edge2.getToSearchNode().getCurrentTreeNode();
            if (!mutated1.getDescription().equals(mutated2.getDescription()))
            {
                return false;
            }
        }
        if (!edge1.getFromSearchNode().getCurrentTreeNode().getTree().isIsomorphicTo(edge2.getFromSearchNode().getCurrentTreeNode().getTree()))
        {
            return false;
        }
        if (edge1.getFromSearchNode().getCurrentTreeNode() instanceof MutatedNode && edge2.getFromSearchNode().getCurrentTreeNode() instanceof MutatedNode)
        {
            MutatedNode mutatedFrom1 = (MutatedNode) edge1.getToSearchNode().getCurrentTreeNode();
            MutatedNode mutatedFrom2 = (MutatedNode) edge2.getToSearchNode().getCurrentTreeNode();
            if (!mutatedFrom1.getDescription().equals(mutatedFrom2.getDescription()))
            {
                return false;
            }
        }
        return true;
    }
}
