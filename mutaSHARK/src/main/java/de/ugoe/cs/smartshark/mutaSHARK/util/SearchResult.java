package de.ugoe.cs.smartshark.mutaSHARK.util;

import java.util.List;

public class SearchResult
{
    public final List<SearchPath> foundPaths;
    public final List<SearchPath> closestPaths;

    public SearchResult(List<SearchPath> foundPaths, List<SearchPath> closestPaths)
    {
        this.foundPaths = foundPaths;
        this.closestPaths = closestPaths;
    }
}
