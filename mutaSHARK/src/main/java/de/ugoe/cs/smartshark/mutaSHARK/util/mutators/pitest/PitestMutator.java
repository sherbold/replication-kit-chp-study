package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest;

import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatorType;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.TreeMutationOperator;

import java.util.List;

public abstract class PitestMutator extends TreeMutationOperator
{
    @Override
    public String getSourceName()
    {
        return "Pitest";
    }
}
