/*******************************************************************************
 * Copyright (C) 2013  Stefan Schroeder
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package jsprit.core.algorithm;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import jsprit.core.algorithm.acceptor.GreedyAcceptance;
import jsprit.core.algorithm.listener.IterationStartsListener;
import jsprit.core.algorithm.module.RuinAndRecreateModule;
import jsprit.core.algorithm.recreate.BestInsertionBuilder;
import jsprit.core.algorithm.recreate.InsertionStrategy;
import jsprit.core.algorithm.ruin.RadialRuinStrategyFactory;
import jsprit.core.algorithm.ruin.RandomRuinStrategyFactory;
import jsprit.core.algorithm.ruin.RuinStrategy;
import jsprit.core.algorithm.ruin.distance.AvgServiceDistance;
import jsprit.core.algorithm.selector.SelectBest;
import jsprit.core.algorithm.state.StateManager;
import jsprit.core.algorithm.state.UpdateVariableCosts;
import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.constraint.ConstraintManager;
import jsprit.core.problem.io.VrpXMLReader;
import jsprit.core.problem.solution.SolutionCostCalculator;
import jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import jsprit.core.problem.solution.route.VehicleRoute;
import jsprit.core.problem.solution.route.state.StateFactory;
import jsprit.core.problem.vehicle.InfiniteFleetManagerFactory;
import jsprit.core.problem.vehicle.VehicleFleetManager;
import jsprit.core.util.Solutions;

import org.junit.Before;
import org.junit.Test;


public class BuildCVRPAlgoFromScratch_IT {
	
	VehicleRoutingProblem vrp;
	
	VehicleRoutingAlgorithm vra;
	
	@Before
	public void setup(){
		VehicleRoutingProblem.Builder builder = VehicleRoutingProblem.Builder.newInstance();
		new VrpXMLReader(builder).read("src/test/resources/vrpnc1-jsprit.xml");
		vrp = builder.build();
		
		final StateManager stateManager = new StateManager(vrp);
		stateManager.updateLoadStates();
		stateManager.updateTimeWindowStates();
		stateManager.addStateUpdater(new UpdateVariableCosts(vrp.getActivityCosts(), vrp.getTransportCosts(), stateManager));


		ConstraintManager cManager = new ConstraintManager(vrp, stateManager);
		cManager.addLoadConstraint();
		cManager.addTimeWindowConstraint();
				
		VehicleFleetManager fleetManager = new InfiniteFleetManagerFactory(vrp.getVehicles()).createFleetManager();
		
		InsertionStrategy bestInsertion = new BestInsertionBuilder(vrp, fleetManager, stateManager, cManager).build();
		
		RuinStrategy radial = new RadialRuinStrategyFactory(0.15, new AvgServiceDistance(vrp.getTransportCosts())).createStrategy(vrp);
		RuinStrategy random = new RandomRuinStrategyFactory(0.25).createStrategy(vrp);
		
		SolutionCostCalculator solutionCostCalculator = new SolutionCostCalculator() {
			
			@Override
			public double getCosts(VehicleRoutingProblemSolution solution) {
				double costs = 0.0;
				for(VehicleRoute route : solution.getRoutes()){
					costs += stateManager.getRouteState(route, StateFactory.COSTS).toDouble();
				}
				return costs;
			}
		};
		
		SearchStrategy randomStrategy = new SearchStrategy(new SelectBest(), new GreedyAcceptance(1), solutionCostCalculator);
		RuinAndRecreateModule randomModule = new RuinAndRecreateModule("randomRuin_bestInsertion", bestInsertion, random);
		randomStrategy.addModule(randomModule);
		
		SearchStrategy radialStrategy = new SearchStrategy(new SelectBest(), new GreedyAcceptance(1), solutionCostCalculator);
		RuinAndRecreateModule radialModule = new RuinAndRecreateModule("radialRuin_bestInsertion", bestInsertion, radial);
		radialStrategy.addModule(radialModule);
		
		SearchStrategyManager strategyManager = new SearchStrategyManager();
		strategyManager.addStrategy(radialStrategy, 0.5);
		strategyManager.addStrategy(randomStrategy, 0.5);
		
		vra = new VehicleRoutingAlgorithm(vrp, strategyManager);
		vra.addListener(stateManager);
		vra.addListener(new RemoveEmptyVehicles(fleetManager));
		
//		vra.getAlgorithmListeners().addListener(stateManager);
//		vra.getSearchStrategyManager().addSearchStrategyModuleListener(stateManager);
//		vra.getSearchStrategyManager().addSearchStrategyModuleListener(new RemoveEmptyVehicles(fleetManager));
		
		VehicleRoutingProblemSolution iniSolution = new InsertionInitialSolutionFactory(bestInsertion, solutionCostCalculator).createSolution(vrp);

		vra.addInitialSolution(iniSolution);
		
		vra.setNuOfIterations(2000);

	}
	
	@Test
	public void testVRA(){
		Collection<VehicleRoutingProblemSolution> solutions = vra.searchSolutions();
		System.out.println("costs="+Solutions.bestOf(solutions).getCost()+";#routes="+Solutions.bestOf(solutions).getRoutes().size());
		assertEquals(530.0, Solutions.bestOf(solutions).getCost(),15.0);
		assertEquals(5, Solutions.bestOf(solutions).getRoutes().size());
	}

}
