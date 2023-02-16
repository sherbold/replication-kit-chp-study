package de.ugoe.cs.smartshark.mutaSHARK.util.defects4j;

import de.ugoe.cs.smartshark.mutaSHARK.MutaShark;
import de.ugoe.cs.smartshark.mutaSHARK.util.SearchPath;
import de.ugoe.cs.smartshark.mutaSHARK.util.SearchResult;
import de.ugoe.cs.smartshark.mutaSHARK.util.TooManyActionsException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static java.lang.Math.round;

public class Defects4JRunner
{
    final private static List<String> results = new ArrayList<>();
    public static int total = 0;
    public static int skipped = 0;
    public static int fixed = 0;
    public static int closest = 0;
    public static int none = 0;

    public static int maxedges = 0;
    static FileWriter fileWriter, fileCompletionPercentage, filePathLengthPartial, filePathLengthComplete, filePathLengthNone, fileEstimationToComplete;

    static
    {
        try
        {
            fileWriter = new FileWriter("/home/zaheed/javawork/results.csv");
            //Visualizations data
            /*fileCompletionPercentage = new FileWriter("/home/zaheed/javawork/resultscompletionpercentage.txt");
            filePathLengthPartial = new FileWriter("/home/zaheed/javawork/resultspathlengthpartial.txt");
            filePathLengthComplete = new FileWriter("/home/zaheed/javawork/resultspathlengthComplete.txt");
            filePathLengthNone = new FileWriter("/home/zaheed/javawork/resultspathlengthNone.txt");
            fileEstimationToComplete = new FileWriter("/home/zaheed/javawork/resultsestimatedpathlengths.txt");*/
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Defects4JRunner() throws IOException
    {
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, InterruptedException
    {
        List<Defects4JBugFix> defects4JBugFixes = new Defects4JLoader(Defects4JRunner::handleBugFix).LoadAll();
        fileWriter.close();
    }

    private static void handleBugFix(Defects4JBugFix bugFix) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException
    {
        if (total < 0)
        {
            return;
        }
        try
        {
            MutaShark.main(new String[]{bugFix.buggyClassFile, bugFix.fixedClassFile, "-m", "active", "optional", "cheated", "-p", "1", "-d", "55"});
            final SearchResult searchResult = MutaShark.getSearchResult();
            fileWriter.write(bugFix.name + " - " + bugFix.buggyClassFile + " - " + bugFix.fixedClassFile);
            fileWriter.write(" ");
            fileWriter.flush();
            addResultString(bugFix, searchResult);

            if (searchResult.foundPaths.size() > 0)
            {
                fixed++;
            }
        }
        catch (TooManyActionsException e)
        {
            skipped++;
        }
        total++;
        System.out.println("Fixed: " + fixed + " Partial: "+ closest +" None: " + none + " Skipped: " + skipped +" Sum: " + total);
        fileWriter.flush();
    }

    private static void addResultString(Defects4JBugFix bugFix, SearchResult searchResult) throws IOException
    {
        results.add(String.join("°", new String[]{bugFix.name, bugFix.buggyClassFile, bugFix.fixedClassFile, searchResult.foundPaths.size() + "", searchResult.closestPaths.size() + ""}));
        for (SearchPath foundPath : searchResult.foundPaths)
        {
            if (foundPath.edges.size() == 0)
            {
                fileWriter.write("\n\r");
                continue;
            }
            final ArrayList<String> paras = new ArrayList<>();
            paras.add("f");
            paras.add(foundPath.totalCost + "");
            paras.add(foundPath.edges.size() + "");
            paras.add(foundPath.totalActionCount + "");
            paras.add(foundPath.remainingActionCount + "");
            paras.addAll(foundPath.edges.stream().map(e -> e.getToSearchNode().getCurrentTreeNode().toString()).collect(Collectors.toList()));
            String result = String.join("|", paras);
            fileWriter.write(result);
            fileWriter.write("\n\r");
            fileWriter.flush();
            results.add(result);

//fully recreated bugs path lengths in a separate file
            /*filePathLengthComplete.write(String.valueOf(foundPath.edges.size()));
            filePathLengthComplete.write("\n");
            filePathLengthComplete.flush();*/
//////////////////////////////
        }
        for (SearchPath foundPath : searchResult.closestPaths)
        {
            if (foundPath.edges.size() == 0)
            {
                none++; //if no mutator applied
                fileWriter.write("\n\r");

//zero path lengths in a separate file
                /*filePathLengthNone.write(String.valueOf(foundPath.edges.size()));
                filePathLengthNone.write("\n");
                filePathLengthNone.flush();*/
//////////////////////////
                continue;
            }
            final ArrayList<String> paras = new ArrayList<>();
            paras.add("c");
            closest++;
            paras.add(foundPath.totalCost + "");
            paras.add(foundPath.edges.size() + "");
            paras.add(foundPath.totalActionCount + "");
            paras.add(foundPath.remainingActionCount + "");

//partially recreated bugs path lengths in a separate file
            /*filePathLengthPartial.write(String.valueOf(foundPath.edges.size()));
            filePathLengthPartial.write("\n");
            filePathLengthPartial.flush();*/

//partial paths completeness data in a separate file
            /*float percentComplete = (1-((float) foundPath.remainingActionCount / (float) foundPath.totalActionCount)) * 100;
            fileCompletionPercentage.write(String.valueOf(percentComplete));
            fileCompletionPercentage.write("\n");
            fileCompletionPercentage.flush();*/

//Estimation for the expected path lengths
            /*float estimationToComplete = foundPath.edges.size()/(1-((float) foundPath.remainingActionCount / (float) foundPath.totalActionCount));
            fileEstimationToComplete.write(String.valueOf(round(estimationToComplete)));
            fileEstimationToComplete.write("\n");
            fileEstimationToComplete.flush();*/
///////////////////////////////////////
            paras.addAll(foundPath.edges.stream().map(e -> e.getToSearchNode().getCurrentTreeNode().toString()).collect(Collectors.toList()));
            String result = String.join("°", paras);
            fileWriter.write(result);
            fileWriter.write("\n\r");
            fileWriter.flush();
            results.add(result);
        }
    }
}
