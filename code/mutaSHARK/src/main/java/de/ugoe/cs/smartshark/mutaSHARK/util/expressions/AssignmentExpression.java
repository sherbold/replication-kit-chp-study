package de.ugoe.cs.smartshark.mutaSHARK.util.expressions;

public final class AssignmentExpression extends Expression
{

    public AssignmentExpression(SimpleNameExpression simpleNameExpression, AssignmentOperatorExpression assignmentOperatorExpression, Expression valueExpression)
    {
        children.clear();
        children.add(simpleNameExpression);
        children.add(assignmentOperatorExpression);
        children.add(valueExpression);
    }

    @Override
    protected String getTypeName()
    {
        return "Assignment";
    }

    @Override
    protected String getLabel()
    {
        return "";
    }
}
