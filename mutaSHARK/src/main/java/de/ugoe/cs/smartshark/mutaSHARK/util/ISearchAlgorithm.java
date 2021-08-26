package de.ugoe.cs.smartshark.mutaSHARK.util;

public interface ISearchAlgorithm
{
    SearchResult findPaths(SearchSettings searchSettings) throws TooManyActionsException;
}
