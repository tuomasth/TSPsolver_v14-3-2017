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
