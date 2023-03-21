package de.ugoe.cs.smartshark.mutaSHARK.util.defects4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
public class Defects4JLoader
{
    public static final String defects4jPath = "/home/zaheed/javawork/defects4j";
    public static final String defects4jBinPath = defects4jPath + "/framework/bin";

    public static final String checkouttargetRootPath = "/Defects4JCheckouts";
    private final ILoadCallback loadCallback;

    public Defects4JLoader(ILoadCallback loadCallback)
    {
        this.loadCallback = loadCallback;
    }

    public List<Defects4JBugFix> Load(Defects4JProjectName projectName,
                                      int index,
                                      List<String> changedClasses) throws IOException, InterruptedException, IllegalAccessException, ClassNotFoundException, InstantiationException
    {
        String rootPath = checkouttargetRootPath + "/" + projectName.name + "/" + index;
        String buggyPath = rootPath + "/" + "buggy";
        String fixedPath = rootPath + "/" + "fixed";
        String defects4jPath = Paths.get(defects4jBinPath, "defects4j").toString();

        if (!new File(Paths.get(defects4jBinPath, fixedPath).toString()).exists())
        {
            String fullFixedPath = Paths.get(defects4jBinPath, fixedPath).toString();
            String fixedCommand = "" + defects4jPath + " checkout -p " + projectName.name + " -v " + index + "f" + " -w " + fullFixedPath;
            Runtime.getRuntime().exec(fixedCommand).waitFor();
        }

        if (!new File(Paths.get(defects4jBinPath, buggyPath).toString()).exists())
        {
            String fullBuggyPath = Paths.get(defects4jBinPath, buggyPath).toString();
            String buggyCommand = "" + defects4jPath + " checkout -p " + projectName.name + " -v " + index + "b" + " -w " + fullBuggyPath;
            Runtime.getRuntime().exec(buggyCommand).waitFor();
        }

        ArrayList<Defects4JBugFix> defects4JBugFixes = new ArrayList<>();
        for (String changedClass : changedClasses)
        {
            Defects4JBugFix defects4JBugFix = LoadBugFix(projectName.name + "(" + index + ")", Paths.get(defects4jBinPath, buggyPath).toString(), Paths.get(defects4jBinPath, fixedPath).toString(), changedClass);
            if (defects4JBugFix != null)
            {
                defects4JBugFixes.add(defects4JBugFix);
            }
            else
            {
                System.out.println("Skipped bugfix: " + changedClass + "/" + index);
                Defects4JRunner.skipped++; //if bug is deprecated then we will deduct it in findJavaFileForClassPath method below
                Defects4JRunner.total++; //if bug is deprecated then we will deduct it in findJavaFileForClassPath method below
            }
        }

        return defects4JBugFixes;
    }

    private Defects4JBugFix LoadBugFix(String name,
                                       String buggyPath,
                                       String fixedPath,
                                       String changedClass) throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        try
        {
            String buggyClassFile = findJavaFileForClassPath(buggyPath, changedClass);
            String fixedClassFile = findJavaFileForClassPath(fixedPath, changedClass);
            //Skipp following bugs as these are skipping in extended mutators set
            if (buggyClassFile.contains("/Jsoup/65/buggy/src/main/java/org/jsoup/parser/HtmlTreeBuilderState.java") || (buggyClassFile.contains("/Closure/95/buggy/src/com/google/javascript/jscomp/TypedScopeCreator.java")))
                return null;
            Defects4JBugFix defects4JBugFix = new Defects4JBugFix(name, buggyClassFile, fixedClassFile);
            loadCallback.BugFixLoaded(defects4JBugFix);
            return defects4JBugFix;
        } catch (IOException e)
        {
            return null;
        }
    }

    private String findJavaFileForClassPath(String path, String classPath) throws IOException
    {
        List<Path> foundClasses = Files.find(Paths.get(path), Integer.MAX_VALUE, (filePath, fileAttribute) -> isPathEqualToClassFilePath(filePath, classPath)).collect(Collectors.toList());
        if (foundClasses.size() != 1)
        {
            Defects4JRunner.total--; //Do not count the Deprecated bugs
            Defects4JRunner.skipped--; //Do not count the Deprecated bugs
            throw new IOException("Class could not be found distinctively");
        }
        return foundClasses.get(0).toString();
    }

    private boolean isPathEqualToClassFilePath(Path filePath, String classPath)
    {
        String path = filePath.toString();

        path = path.replace('\\', '.');
        path = path.replace('/', '.');
        return path.endsWith(classPath + ".java");
    }

    public List<Defects4JBugFix> LoadAll() throws IOException, InterruptedException, IllegalAccessException, ClassNotFoundException, InstantiationException
    {
        ArrayList<Defects4JBugFix> results = new ArrayList<>();

        String projectsFolder = Paths.get(defects4jPath, "framework", "projects").toString();
        File[] directories = new File(projectsFolder).listFiles(File::isDirectory);

        for (File directory : Objects.requireNonNull(directories))
        {
            if (directory.getName().equals("project ID to exclude"))
            {
                continue;
            }
            File modified_classes = new File(directory.getAbsolutePath(), "modified_classes");
            if (!modified_classes.exists())
            {
                continue;
            }
            for (File commit : Objects.requireNonNull(modified_classes.listFiles()))
            {
                List<Defects4JBugFix> defects4JBugFixes = getDefects4JBugFixes(directory, commit);
                results.addAll(defects4JBugFixes);
            }
        }
        return results;
    }

    public List<Defects4JBugFix> getDefects4JBugFixes(File directory,
                                                      File commit) throws IOException, InterruptedException, IllegalAccessException, InstantiationException, ClassNotFoundException
    {
        String commitName = commit.getName();
        List<String> classPaths = Files.lines(Paths.get(commit.getAbsolutePath())).collect(Collectors.toList());
        commitName = commitName.substring(0, commitName.indexOf('.'));
        return Load(new Defects4JProjectName(directory.getName()), Integer.parseInt(commitName), classPaths);
    }

}

