package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest;

import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.TreeMutationOperator;

public abstract class PitestMutator extends TreeMutationOperator
{
    @Override
    public String getSourceName()
    {
        return "Pitest";
    }
}
