package de.ugoe.cs.smartshark.mutaSHARK;

import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.TreeMutationOperator;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StartUpOptions
{
    public final String pathToBug;
    public final String pathToFix;
    public final TreeMutationOperator[] mutations;
    public final int maxPathCount;
    public final int maxPathDepth;

    public StartUpOptions(String pathToBug,
                          String pathToFix,
                          TreeMutationOperator[] mutations,
                          int maxPathCount,
                          int maxPathDepth)
    {
        this.pathToBug = pathToBug;
        this.pathToFix = pathToFix;
        this.mutations = mutations;
        this.maxPathCount = maxPathCount;
        this.maxPathDepth = maxPathDepth;
    }

    public static StartUpOptions parseArgs(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        int maxPathCount = 4;
        int maxPathDepth = 100;
        String pathToBug = "";
        String pathToFix = "";
        List<String> mutatorPackages = new ArrayList<>();

        String currentParameter = "b";
        for (String arg : args)
        {
            if (arg.startsWith("-"))
            {
                currentParameter = arg.substring(1);
            }
            else
            {
                switch (currentParameter.trim())
                {
                    case "b":
                    case "bug":
                    {
                        pathToBug = arg.trim();
                        currentParameter = "f";
                        break;
                    }
                    case "f":
                    case "fix":
                    {
                        pathToFix = arg.trim();
                        currentParameter = "m";
                        break;
                    }
                    case "m":
                    case "mutators":
                    {
                        mutatorPackages.add(arg.trim());
                        break;
                    }
                    case "p":
                    case "paths":
                    {
                        maxPathCount = Integer.parseInt(arg.trim());
                        break;
                    }
                    case "d":
                    case "depth":
                    {
                        maxPathDepth = Integer.parseInt(arg.trim());
                        break;
                    }
                }
            }
        }

        TreeMutationOperator[] treeMutationOperators = initiateTreeMutators(mutatorPackages);

        return new StartUpOptions(pathToBug, pathToFix, treeMutationOperators, maxPathCount, maxPathDepth);
    }

    private static TreeMutationOperator[] initiateTreeMutators(List<String> mutatorPackages) throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        List<TreeMutationOperator> result = new ArrayList<>();

        List<String> mutators = new Reflections("de.ugoe.cs.smartshark.mutaSHARK.util.mutators").getSubTypesOf(TreeMutationOperator.class).stream().filter(c ->
                                                                                                                                                           {
                                                                                                                                                               if (Modifier.isAbstract(c.getModifiers()))
                                                                                                                                                               {
                                                                                                                                                                   return false;
                                                                                                                                                               }
                                                                                                                                                               for (String mutatorPackage : mutatorPackages)
                                                                                                                                                               {
                                                                                                                                                                   if (c.getName().contains(mutatorPackage))
                                                                                                                                                                   {
                                                                                                                                                                       return true;
                                                                                                                                                                   }
                                                                                                                                                               }
                                                                                                                                                               return false;
                                                                                                                                                           }).map(Class::getName).collect(Collectors.toList());

        for (String mutator : mutators)
        {
            TreeMutationOperator treeMutationOperator = (TreeMutationOperator) Class.forName(mutator).newInstance();
            result.add(treeMutationOperator);
        }
        TreeMutationOperator[] resultArray = new TreeMutationOperator[mutators.size()];
        result.toArray(resultArray);
        return resultArray;
    }

}
