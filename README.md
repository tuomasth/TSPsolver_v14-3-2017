Traveling Salesman Problem solver for Java with three algorithms: Nearest Neighbo(u)r, Double Minimum Spanning Tree and Convex Hull Heuristic.
The Java codes in this repository have not been edited since 2017.

A newer version is available. 
https://github.com/tuomasth/TSPsolver_v17-6-2026 (21.6.2026)

Java (JRE or JDK) must be installed, obviously, before executing the JAR file in the "dist" folder. 
https://www.java.com/en/download/manual.jsp (21.6.2026)

For huge graphs for example, sometimes the 2MST is slower than the CHH. The Master's thesis might have been too sceptical/critical about CHH's time consumption.
http://urn.fi/urn:nbn:fi:uef-20180562 (21.6.2026)
It stated that NNH -> 2MST -> CHH in speed, on average, NNH being the fastest. 

The convex hull has a minor bug, sometimes leaving a node unremoved, but this does not significally affect the TSP solution.

Also, the 2MST heuristic can find the best tour for example in special "cluster" cases such as:

1 3.1 3.0
2 3.2 3.9
3 1.0 5.0
4 1.0 1.0
5 5.0 1.0
6 5.0 5.0
7 13.1 13.0
8 13.2 13.9
9 11.0 15.0
10 11.0 11.0
11 15.0 11.0
12 15.0 15.0

This was not in the Master's thesis. It probably should have been.
