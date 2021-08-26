package de.ugoe.cs.smartshark.mutaSHARK.util.expressions;

public class PostfixExpression extends Expression
{
    public PostfixExpression(SimpleNameExpression simpleNameExpression, PostfixExpressionOperatorExpression expressionOperator)
    {
        children.clear();
        children.add(simpleNameExpression);
        children.add(expressionOperator);
    }

    @Override
    protected String getTypeName()
    {
        return "PostfixExpression";
    }

    @Override
    protected String getLabel()
    {
        return "";
    }
}

