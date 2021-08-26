package de.ugoe.cs.smartshark.mutaSHARK.util.defects4j;

import java.io.IOException;

public interface ILoadCallback
{
    void BugFixLoaded(Defects4JBugFix defects4JBugFix) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException;
}
