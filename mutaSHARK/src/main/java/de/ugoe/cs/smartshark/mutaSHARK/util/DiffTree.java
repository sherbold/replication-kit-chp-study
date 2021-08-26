package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.actions.*;
import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeUtils;

import java.util.*;

public class DiffTree
{
    private List<Action> actions;

    public DiffTree(ITree treeFrom, ITree treeTo) throws TooManyActionsException
    {
        classify(treeFrom, treeTo);
    }

    public List<Action> getActions()
    {
        return actions;
    }

    private void classify(ITree treeFrom, ITree treeTo) throws TooManyActionsException
    {
        boolean recursive = true;
        EditScriptGenerator chawatheScriptGenerator = new InsertDeleteChawatheScriptGenerator();
        ITree toClone = treeTo.deepCopy();
        ITree fromClone = treeFrom.deepCopy();
        MappingStore mappings = match(fromClone, toClone, new MappingStore(fromClone, toClone));
        actions = chawatheScriptGenerator.computeActions(mappings).asList();
        if (recursive)
        {
            cleanUp(actions);
        }
    }

    private void cleanUp(List<Action> actions)
    {
        List<Action> additionalActions = new ArrayList<>();
        for (Action action : actions)
        {
            for (ITree descendant : action.getNode().getDescendants())
            {
                if (action instanceof Insert)
                {
                    Insert insert = new Insert(descendant, descendant.getParent(), descendant.positionInParent());
                    if (notContains(additionalActions, insert) && notContains(actions, insert))
                    {
                        additionalActions.add(insert);
                    }
                }
                if (action instanceof TreeInsert)
                {
                    TreeInsert insert = new TreeInsert(descendant, descendant.getParent(), descendant.positionInParent());
                    if (notContains(additionalActions, insert) && notContains(actions, insert))
                    {
                        additionalActions.add(insert);
                    }
                }
                if (action instanceof Delete)
                {
                    Delete delete = new Delete(descendant);
                    if (notContains(additionalActions, delete) && notContains(actions, delete))
                    {
                        additionalActions.add(delete);
                    }
                }
                if (action instanceof TreeDelete)
                {
                    TreeDelete delete = new TreeDelete(descendant);
                    if (notContains(additionalActions, delete) && notContains(actions, delete))
                    {
                        additionalActions.add(delete);
                    }
                }
            }
        }
        actions.addAll(additionalActions);
    }

    private boolean notContains(List<Action> actions, Delete delete)
    {
        for (Action action : actions)
        {
            if (action instanceof Delete)
            {
                if (action.getNode().isIsomorphicTo(delete.getNode()))
                {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean notContains(List<Action> actions, TreeDelete delete)
    {
        for (Action action : actions)
        {
            if (action instanceof Delete)
            {
                if (action.getNode().isIsomorphicTo(delete.getNode()))
                {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean notContains(List<Action> actions, Insert insert)
    {
        for (Action action : actions)
        {
            if (action instanceof Insert || action instanceof TreeInsert)
            {
                InsertWrapper tempInsert = new InsertWrapper(action);
                if (tempInsert.getNode().isIsomorphicTo(insert.getNode()) && tempInsert.getParent().isIsomorphicTo(insert.getParent()) && insert.getName().equals(tempInsert.getName()))
                {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean notContains(List<Action> actions, TreeInsert insert)
    {
        for (Action action : actions)
        {
            if (action instanceof Insert || action instanceof TreeInsert)
            {
                InsertWrapper tempInsert = new InsertWrapper(action);
                if (tempInsert.getNode().isIsomorphicTo(insert.getNode()) && tempInsert.getParent().isIsomorphicTo(insert.getParent()) && insert.getName().equals(tempInsert.getName()))
                {
                    return false;
                }
            }
        }
        return true;
    }

    private MappingStore match(ITree src, ITree dst, MappingStore mappings) throws TooManyActionsException
    {
        Implementation impl = new Implementation(src, dst, mappings);
        impl.match();
        return impl.mappings;
    }

    private static class Implementation
    {
        private final ITree src;
        private final ITree dst;
        private final MappingStore mappings;

        public Implementation(ITree src, ITree dst, MappingStore mappings)
        {
            this.src = src;
            this.dst = dst;
            this.mappings = mappings;
        }

        private static List<int[]> longestCommonSubsequenceWithTypeAndLabel(List<ITree> s0,
                                                                            List<ITree> s1) throws TooManyActionsException
        {
            final int s0Size = s0.size();
            final int s1Size = s1.size();
            if (Math.abs(s0Size - s1Size) * (s0Size + s1Size) > 1200000 || s0.size() > 12500 && s1.size() > 12500)
            {
                throw new TooManyActionsException(s0Size);
            }
            short[][] lengths = new short[s0Size + 1][s1Size + 1];
            for (int i = 0; i < s0Size; i++)
            {
                for (int j = 0; j < s1Size; j++)
                {
                    if (s0.get(i).hasSameTypeAndLabel(s1.get(j)))
                    {
                        lengths[i + 1][j + 1] = (short) (lengths[i][j] + 1);
                    }
                    else
                    {
                        lengths[i + 1][j + 1] = (short) Math.max(lengths[i + 1][j], lengths[i][j + 1]);
                    }
                }
            }

            return extractIndexes(lengths, s0Size, s1Size);
        }

        private static List<int[]> extractIndexes(short[][] lengths, int length1, int length2)
        {
            List<int[]> indexes = new ArrayList<>();

            for (int x = length1, y = length2; x != 0 && y != 0; )
            {
                if (lengths[x][y] == lengths[x - 1][y])
                {
                    x--;
                }
                else if (lengths[x][y] == lengths[x][y - 1])
                {
                    y--;
                }
                else
                {
                    indexes.add(new int[]{x - 1, y - 1});
                    x--;
                    y--;
                }
            }
            Collections.reverse(indexes);
            return indexes;
        }

        public void match() throws TooManyActionsException
        {
            List<ITree> srcSeq = TreeUtils.preOrder(src);
            List<ITree> dstSeq = TreeUtils.preOrder(dst);
            List<int[]> lcs = longestCommonSubsequenceWithTypeAndLabel(srcSeq, dstSeq);
            for (int[] x : lcs)
            {
                ITree t1 = srcSeq.get(x[0]);
                ITree t2 = dstSeq.get(x[1]);
                mappings.addMapping(t1, t2);
            }
        }
    }
}

