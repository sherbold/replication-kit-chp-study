package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.actions.EditScript;
import com.github.gumtreediff.actions.model.Action;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.TreeMutationOperator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AStarSearch implements ISearchAlgorithm
{
    public final int maxActions = 10000;
    public final int maxMutationsPerMutator = 10000;
    public final int maxOpenListSize = 10;
    private final TreeNode fromNode;
    private final TreeNode toNode;

    private PriorityQueue openList = new PriorityQueue();
    private List<SearchNode> closedList = new ArrayList<>();

    public AStarSearch(TreeNode fromNode, TreeNode toNode)
    {
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    public SearchResult findPaths(SearchSettings searchSettings) throws TooManyActionsException
    {
        List<SearchPath> foundPaths = new ArrayList<>();
        openList.clear();
        closedList.clear();

        SearchNode searchNode = new SearchNode(fromNode, searchSettings.heuristic);
        openList.enqueue(searchNode);

        while (openList.getSize() > 0 && foundPaths.size() < searchSettings.maxFoundPaths)
        {
            SearchNode currentNode = openList.dequeue();
            List<SearchNode> prunedNodes = openList.prune(maxOpenListSize);
            closedList.addAll(prunedNodes);
            if (currentNode.getCurrentTreeNode().getTree().isIsomorphicTo(toNode.getTree()))
            {
                foundPaths.add(new SearchPath(currentNode, new DiffTree(fromNode.getTree(), toNode.getTree()).getActions().size(), new DiffTree(currentNode.getCurrentTreeNode().getTree(), toNode.getTree()).getActions().size()));
            }
            else
            {
                closedList.add(currentNode);
                expandNode(currentNode, searchSettings);
            }
        }
        return new SearchResult(foundPaths, getClostestPaths(searchSettings));
    }

    private List<SearchPath> getClostestPaths(SearchSettings searchSettings) throws TooManyActionsException
    {
        ArrayList<SearchPath> result = new ArrayList<>();
        closedList.sort(Comparator.comparingDouble(SearchNode::getHeuristicCost));
        for (int i = 0; i < closedList.size() && i < searchSettings.maxFoundPaths; i++)
        {
            SearchNode searchNode = closedList.get(i);
            result.add(new SearchPath(searchNode, new DiffTree(fromNode.getTree(), toNode.getTree()).getActions().size(), new DiffTree(searchNode.getCurrentTreeNode().getTree(), toNode.getTree()).getActions().size()));
        }
        return result;
    }

    private void expandNode(SearchNode searchNode, SearchSettings searchSettings) throws TooManyActionsException
    {
        TreeNode treeNode = searchNode.getCurrentTreeNode();
        if (searchNode.getTotalHopCount() >= searchSettings.maxHops)
        {
            return;
        }
        List<Action> actions = new DiffTree(treeNode.getTree(), toNode.getTree()).getActions();
        if (actions.size() > maxActions)
        {
            throw new TooManyActionsException(actions.size());
        }
        for (TreeMutationOperator mutationOperator : searchSettings.mutationOperators)
        {
            List<MutatedNode> possibleMutations = mutationOperator.getPossibleMutations(treeNode, toNode, actions);
            for (int i = 0; i < possibleMutations.size() && i < maxMutationsPerMutator; i++)
            {
                MutatedNode newNode = possibleMutations.get(i);
                if (closedList.stream().anyMatch(n -> n.getCurrentTreeNode().equals(newNode)))
                {
                    continue;
                }

                SearchNode newSearchNode = new SearchNode(newNode, searchNode, searchSettings.heuristic);
                if (openList.find(newNode) != null)
                {
                    if (newSearchNode.getTotalCost() >= openList.find(newNode).getTotalCost())
                    {
                        continue;
                    }
                    openList.replace(openList.find(newNode), newSearchNode);
                }
                else
                {
                    openList.enqueue(newSearchNode);
                }
            }
        }
    }
}

