jsprit
======
jsprit is a java based, open source toolkit for solving a set of well-known <a href="http://en.wikipedia.org/wiki/Travelling_salesman_problem" target="_blank">traveling salesman</a> (TSP) and <a href="http://neo.lcc.uma.es/vrp/vehicle-routing-problem/" target="_blank">vehicle routing problems</a> (VRP). 
It is lightweight and easy-to-use, and based on heuristics currently solving 
- Capacitated VRP
- Multiple Depot VRP
- VRP with Time Windows
- VRP with Backhauls
- VRP with Pickups and Deliveries
- VRP with Heterogeneous Fleet
- Time-dependent VRP
- Traveling Salesman Problem
- Dial-a-Ride Problem
- Various combination of these types

Setting up the problem, modifying the algorithms and visualising the discovered solutions is as easy and handy as 
reading classical VRP instances to benchmark your algorithm.

Additionally, jsprit can be used along with <a href="http://www.matsim.org" target="blank_">MATSim</a> 
to solve the above problem-types in real networks (i.e. without preprocessing transport times and costs). A variety of least cost path algorithms such as Dijkstra and A*
can be used, and a dynamic and interactive visualiser greatly enhances the analysis.

##In Development
- continues improvement of code, handling and performance
- soft constraints
- various capacity dimensions and multiple time-windows
- open routes (currently they have to return to the depot)
- large scale instances

##Documentation

Please visit [jsprit-wiki](https://github.com/jsprit/jsprit/wiki) to learn more.

##License
This software is released under [LGPL](http://opensource.org/licenses/LGPL-3.0).

##Getting started in 2 seconds

[Add the latest snapshot to your pom](https://github.com/jsprit/jsprit/wiki/Add-latest-snapshot-to-your-pom).

[Add the latest release to your pom](https://github.com/jsprit/jsprit/wiki/Add-latest-release-to-your-pom).

##About
The jsprit-project is created and maintained by Stefan Schröder. It is motivated by two issues. 

First, there is an almost endless list of papers and algorithms to tackle vehicle routing problems, **BUT** there are (as far as I know) only a [very few open source implementations](https://github.com/jsprit/jsprit/wiki/Other-Projects) of one of these thousands algorithms. 

Second, it is motivated by my PhD-project at [KIT](http://www.kit.edu/english/index.php) where I apply vehicle routing algorithms to solve behavioural models of freight agents to assess (freight) transport policy measures. 

It is mainly inspired by my research group at [KIT-ECON](http://netze.econ.kit.edu/21.php), and by an awesome open-source project called [MATSim](www.matsim.org) and its developers.

If you have questions or if you use jsprit, it would be great you give feedback and let me know your experience:

Email: jsprit.vehicle.routing@gmail.com

[![](https://cruel-carlota.pagodabox.com/ba53806a8cc8ff439c1a51d152245dee "githalytics.com")](http://githalytics.com/jsprit/jsprit)
