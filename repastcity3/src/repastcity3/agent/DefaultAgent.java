/*
ęCopyright 2012 Nick Malleson
This file is part of RepastCity.

RepastCity is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

RepastCity is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with RepastCity.  If not, see <http://www.gnu.org/licenses/>.
*/

package repastcity3.agent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vividsolutions.jts.geom.Coordinate;

import repastcity3.environment.Building;
import repastcity3.environment.Route;
import repastcity3.exceptions.NoIdentifierException;
import repastcity3.main.ContextManager;

public class DefaultAgent implements IAgent, Serializable {

	private static Logger LOGGER = Logger.getLogger(DefaultAgent.class.getName());

	private Building home; // Where the agent lives
	private Route route; // An object to move the agent around the world

	private boolean goingHome = false; // Whether the agent is going to or from their home

	private static int uniqueID = 0;
	private int id;
	
	//list that content the coordinate of the current position of the agent
	private List<Coordinate> currentCoord;
		
	//ArrayList that content the trajectory of the agent
	private ArrayList<List<Coordinate>> pathschedule=new ArrayList<List<Coordinate>>();


	public DefaultAgent() {
		this.id = uniqueID++;
	}

	@Override
	public void step() throws Exception {

		/*LOGGER.log(Level.FINE, "Agent " + this.id + " is stepping.");
		if (this.route == null) {
			this.goingHome = false; // Must be leaving home
			// Choose a new building to go to
			Building b = ContextManager.buildingContext.getRandomObject();
			this.route = new Route(this, b.getCoords(), b);
			LOGGER.log(Level.FINE, this.toString() + " created new route to " + b.toString());
		}
		if (!this.route.atDestination()) {
			this.route.travel();
			LOGGER.log(Level.FINE, this.toString() + " travelling to " + this.route.getDestinationBuilding().toString());
		} else {
			// Have reached destination, now either go home or onto another building
			if (this.goingHome) {
				this.goingHome = false;
				Building b = ContextManager.buildingContext.getRandomObject();
				this.route = new Route(this, b.getCoords(), b);
				LOGGER.log(Level.FINE, this.toString() + " reached home, now going to " + b.toString());
			} else {
				LOGGER.log(Level.FINE, this.toString() + " reached " + this.route.getDestinationBuilding().toString()
						+ ", now going home");
				this.goingHome = true;
				this.route = new Route(this, this.home.getCoords(), this.home);
			}

		}*/
		
		
		Building b= ContextManager.buildingContext.getRandomObject();
		
		// TODO Auto-generated method stub
		LOGGER.log(Level.FINE, "Agent " + this.id + " is stepping.");
		if (this.route == null) {
			this.goingHome = false; // Must be leaving home
			// Choose a new building to go to
			b = randomBuilding(3);
			
			
			this.route = new Route(this, b.getCoords(), b);
			
									
			System.out.println(this.toString() + " created new route to " + b.toString());
			LOGGER.log(Level.FINE, this.toString() + " created new route to " + b.toString());
		}
		
		if (!this.route.atDestination()) {
			this.route.travel();
			
			//Pick up the trajectory of the agent from his current place to his destination
			getCurrentRoute();
			
			
			LOGGER.log(Level.FINE, this.toString() + " travelling to " + this.route.getDestinationBuilding().toString());
		} else {
			//Store all the trajectory of the agent inside an arrayList
			int a=0;
			setpathSchedule(a);
			//this.featureCollectionToKML();
		

			
			
			// Have reached destination, now either go home or onto another building
			if (this.goingHome) {
				this.goingHome = false;
				/*Random randomGenerator = new Random();
				int choice = randomGenerator.nextInt(3);
				b = randomBuilding(choice);*/
				b= randomBuilding(3);
				
				
								
				this.route = new Route(this, b.getCoords(), b);
				
				
				
				System.out.println(this.toString() + " reached home, now going to " + b.toString());
				LOGGER.log(Level.FINE, this.toString() + " reached home, now going to " + b.toString());
			} else {
				System.out.println(this.toString() + " reached " + this.route.getDestinationBuilding().toString()
						+ ", now going home");
				LOGGER.log(Level.FINE, this.toString() + " reached " + this.route.getDestinationBuilding().toString()
						+ ", now going home");
				this.goingHome = true;
				this.route = new Route(this, this.home.getCoords(), this.home);
			}
			

		}
		serialiseMe();
		

	} // step()

	/**
	 * There will be no inter-agent communication so these agents can be executed simulataneously in separate threads.
	 */
	@Override
	public final boolean isThreadable() {
		return true;
	}

	@Override
	public void setHome(Building home) {
		this.home = home;
	}

	@Override
	public Building getHome() {
		return this.home;
	}

	@Override
	public <T> void addToMemory(List<T> objects, Class<T> clazz) {
	}

	@Override
	public List<String> getTransportAvailable() {
		return null;
	}

	@Override
	public String toString() {
		return "Agent " + this.id;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DefaultAgent))
			return false;
		DefaultAgent b = (DefaultAgent) obj;
		return this.id == b.id;
	}

	@Override
	public int hashCode() {
		return this.id;
	}

	public Building randomBuilding(int a) throws NumberFormatException, NoIdentifierException{
		Building b;
		/*Random randomGenerator = new Random();
		int choice = randomGenerator.nextInt(4);*/
		do{
			b = ContextManager.buildingContext.getRandomObject();
			//System.out.println("Ooooooooooo: "+ b.getCommune());
		} while(b.getCommune()!=a);
		//System.out.println("OLAH OLAH OLAH: "+ b.getCommune());
			return b;
	}

	@Override
	public void setpathSchedule(int compt) throws FileNotFoundException {
		// TODO Auto-generated method stub
		this.pathschedule.add(currentCoord);
		
	}

	@Override
	public ArrayList<List<Coordinate>> getpathSchedule() {
		// TODO Auto-generated method stub
		return this.pathschedule;
	}

	@Override
	public void serialiseMe() {
		// TODO Auto-generated method stub
		try
	    {
			FileOutputStream fileOut = new FileOutputStream(this.toString()+".ser");
	       ObjectOutputStream out = new ObjectOutputStream(fileOut);
	       out.writeObject(this);
	       out.close();
	       fileOut.close();
	       System.out.printf("Serialized data is saved for DefaultAgent "+this.toString());
	    }catch(IOException i)
	    {
	        i.printStackTrace();
	        System.out.printf("Serialization failed for DefaultAgent "+this.toString());
	    }
		
	}

	

	@Override
	public void getCurrentRoute() {
		this.currentCoord=this.route.getListRoute();
		
	}

	

}
