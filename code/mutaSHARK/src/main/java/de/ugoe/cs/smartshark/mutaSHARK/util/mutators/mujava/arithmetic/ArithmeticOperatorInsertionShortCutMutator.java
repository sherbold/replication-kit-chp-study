package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.mujava.arithmetic;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeHelper;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.expressions.*;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatorType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArithmeticOperatorInsertionShortCutMutator extends ArithmeticOperatorInsertionMutator
{

    @Override
    public List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        List<String> names = TreeHelper.getNames(target.getTree()).stream().distinct().collect(Collectors.toList());
        List<MutatedNode> result = new ArrayList<>();
        for (String name : names)
        {
            result.addAll(getPossiblePrefixExpressions(treeNode, target, new ExpressionStatement(new PrefixExpression(new DecrementPrefixExpressionOperatorExpression(), new SimpleNameExpression(name)))));
            result.addAll(getPossiblePrefixExpressions(treeNode, target, new ExpressionStatement(new PrefixExpression(new IncrementPrefixExpressionOperatorExpression(), new SimpleNameExpression(name)))));
            result.addAll(getPossiblePrefixExpressions(treeNode, target, new ExpressionStatement(new PostfixExpression(new SimpleNameExpression(name), new DecrementPostfixExpressionOperatorExpression()))));
            result.addAll(getPossiblePrefixExpressions(treeNode, target, new ExpressionStatement(new PostfixExpression(new SimpleNameExpression(name), new IncrementPostfixExpressionOperatorExpression()))));
        }
        //result.addAll(getPossiblePrefixExpressions(treeNode, target));
        return result;
    }

    private List<MutatedNode> getPossiblePrefixExpressions(TreeNode treeNode,
                                                           TreeNode target,
                                                           ExpressionStatement expressionStatement)
    {
        List<MutatedNode> result = new ArrayList<>();

        List<ITree> statements = TreeHelper.findStatements(treeNode.getTree());
        for (int i = 0; i < statements.size() + 1; i++)
        {
            ITree currentStatement = statements.get(Math.min(i, statements.size() - 1));
            ITree current = currentStatement.getParent();

            int depth = TreeHelper.getDepthFrom(treeNode.getTree(), current);
            int childPosition = current.getChildPosition(currentStatement);
            for (int statementPos = childPosition; statementPos <= childPosition + 1; statementPos++)
            {
                ITree clonedTree = ((ITree) treeNode).deepCopy();
                String url = TreeHelper.getUrl(current, depth);
                ITree parent = clonedTree.getChild(url);
                expressionStatement.insertTree(parent, statementPos);
                result.add(new MutatedNode(new TreeNode(clonedTree),this, 1, expressionStatement.toString()));
                String treeString = clonedTree.toTreeString();
                System.out.println(treeString);
            }
        }

        return result;
    }

}
