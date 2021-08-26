package de.ugoe.cs.smartshark.mutaSHARK.util.expressions;

public class PrefixExpression extends Expression
{
    public PrefixExpression(PrefixExpressionOperatorExpression expressionOperator, SimpleNameExpression simpleNameExpression)
    {
        children.clear();
        children.add(expressionOperator);
        children.add(simpleNameExpression);
    }

    @Override
    protected String getTypeName()
    {
        return "PrefixExpression";
    }

    @Override
    protected String getLabel()
    {
        return "";
    }
}
