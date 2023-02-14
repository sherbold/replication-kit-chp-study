Replication Kit
======
---
mutaSHARK -- version 2.0.0
================
mutaSHARK repo contains the data obtained from the bug reproduction study we conducted which is currently under review.

This repo contains the following:

- The data for the bug reproductions with the default PITest mutators.

- The data for the bug reproductions with the optional PITest mutators.

- The data for the bug reproductions with our extended mutators.

- The script we used for visualizing the results.

Contents of mutaSHARK
================

Development Environment
---------------
mutaSHARK is developed and executed in following environment:

- **Operating System:** Ubuntu 20.04.5 LTS.

- **Processor:** 11<sup>th</sup> Gen Intel Core i5-1145G7 @ 2.60GHz.

- **Memory:** 16 GB.

- **Programming Language:** Java (SDK: 11 Version 11.0.16)

- **Integrated Development Environment:** IntelliJ IDEA 2022.2.1 (Community Edition).

Major Components
---------------
Here is the list of main components of the mutaSHARK:

- **GumTree:** used to generate and compare the Abstract Syntax Trees of bug-fix pairs. 

- **Mutators:** default and optional PITest mutators' sets along with our 5 extended mutators are implemented. 

- **Pathfinder:** A* algorithm is modified to find a path from fixed version to buggy version.

- **Data:** defects4j -- version 2.0.0 dataset is used for bug-fix pairs.

Setting up mutaSHARK
================

Requirements
---------------- 
#### Data Acquisition
 - Set up and test the [Defects4J](https://github.com/rjust/defects4j) according to the given instructions.

#### Java IDE Installation
 - Install the required [Java IDE and SDK](https://www.jetbrains.com/idea).

	
Steps to Set Up mutaSHARK
----------------

1. Clone [mutaSHARK](https://github.com/sherbold/replication-kit-chp-study):
    
	- `git clone https://github.com/sherbold/replication-kit-chp-study`
 <br/><br/>
2. Click `Load` to run the gradle build script from the pop-up menu.

3. Set-up SDK for the mutaSHARK project.

4. Assign your defects4j path to the `defects4jPath` in [mutaSHARK loader](https://github.com/sherbold/replication-kit-chp-study/blob/main/mutaSHARK/src/main/java/de/ugoe/cs/smartshark/mutaSHARK/util/defects4j/Defects4JLoader.java):

5. Set path of the `fileWriter` for `result.csv` file in [mutaSHARK runner](https://github.com/sherbold/replication-kit-chp-study/blob/main/mutaSHARK/src/main/java/de/ugoe/cs/smartshark/mutaSHARK/util/defects4j/Defects4JRunner.java):

**Note:** Please make sure that the drive where defects4j is placed should have more than 85 GB of free space.

Using mutaSHARK
================


## Program Arguments

- **Syntax:**
	- -b path to buggy version -f path to fixed version -m mutators/package names -p maximum number of paths should be returned -d maximum depth of a path

- **Example:** 
	- `-b /home/bug.java -f /home/fix.java -m active cheated -p 1 -d 55`
<br/><br/>

- **Description:**
	- This example argument will try to apply the active and cheated sets of mutators on fix.java to recreate the bug.java. It will return the 1 path having least cost and the path can have maximum length of 55.

## Running the mutaSHARK
To replicate the results for each of the mutators' set i.e. default, optional and extended, before running the [Defects4JRunner.java](https://github.com/sherbold/replication-kit-chp-study/blob/main/mutaSHARK/src/main/java/de/ugoe/cs/smartshark/mutaSHARK/util/defects4j/Defects4JRunner.java), program arguments of its **MutaShark.main() method call** will be adjusted accordingly. To reproduce the results included in this replication kit the program arguments will be defined as follows:
  
- **Reproduce:** `results_pit_default.csv`
	- `"-m", "active", "-p", "1", "-d", "55"`
<br/><br/>
		
- **Reproduce:** `results_pit_optional.csv`
	- `"-m", "active", "optional", "-p", "1", "-d", "55"`
<br/><br/>

- **Reproduce:** `results_extended.csv`
	- `"-m", "active", "optional", "cheated", "-p", "1", "-d", "55"`	

## Data Output Format
- **Format:**
	- Each bug-fix pair will be represented by following sections in output:
<br/><br/>
		- − project name(bug-fix ID) - path to buggy file - path to fixed file (f)ull path indicator|Total cost|Edge count|Total GumTree actions|Remaining GumTree actions|Mutator<sub>1</sub>|Mutator<sub>2</sub>, ... |Mutator<sub>n</sub> 
- **Sample:** 
    - Here is an example of fully recreated bug:
 <br/>
 <pre>- Chart(1) <br>- /home/defects4j/framework/bin/Defects4JCheckouts/Chart/1/buggy/source/org/jfree/chart/renderer/category/AbstractCategoryItemRenderer.java <br>- /home/defects4j/framework/bin/Defects4JCheckouts/Chart/1/fixed/source/org/jfree/chart/renderer/category/AbstractCategoryItemRenderer.java <br>f|1.0|1|2|0|MutatedNode{ description='"Replaced conditional == with != @~19736", mutationOperator=de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest.active.NegateConditionalsMutator@153231e, cost=1.0}</pre>

**Note:** For the partially recreated bugs, (c)losest is used instead of 'f' and a special character '°' instead of '|' character as a separator in output.


Additional Resources
================

Article
------------------
* "A new perspective on the competent programmer hypothesis through the reproduction of bugs with repeated mutation", 
	Zaheed Ahmed, Eike Stein, Steffen Herbold, Fabian Trautsch and Jens Grabowski
[download]().

Implementation Details
----------------------
The (important) directory structure of [mutaSHARK](https://github.com/sherbold/replication-kit-chp-study) is as follows:

    replication-kit-chp-study	
       |--- mutaSHARK:					Containts source code, result files and visualizations data etc.
         |--- src
           |--- main
             |--- java
               |--- de
                 |--- ugoe
                   |--- cs
                     |--- smartshark
                       |--- mutaSHARK
                         |--- MutaShark.java:		Contains the main method
                         |--- StartUpOptions.java:		Contains settings for the executor
                         |--- util
                           |---defects4j
                             |--- Defects4JLoader.java: 	Contains defects4j dataset path
                             |--- Defects4JRunner.java: 	Contains runner arguments and results.csv path

           
License
---------
Apache License 2.0, see [`LICENSE`](https://github.com/sherbold/replication-kit-chp-study/blob/main/LICENSE) for more information.
