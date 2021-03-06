/*
  Copyright (C) 2012, Tórur Biskopstø Strøm (torur.strom@gmail.com)

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.reprap.commands;

import org.reprap.Command;
import org.reprap.CommandController;
import org.reprap.HostController;
import org.reprap.Parameter;
import org.reprap.RepRapController;

public class G1 extends Command
{
	private static final int POOL_SIZE = 30;
	private static G1 first;
	private static G1 last;
	private static Object lock = new Object();
	private static boolean initialized = initialize(); //Ensures that pool is created in immortal memory so that all PEH have access
	
	private G1 next;
	private Parameter parameters = new Parameter();
	private boolean executed = false;
	
	private static boolean initialize()
	{
		//No need for mutex as the pool is empty
		G1 current = new G1();
		first = current;
		for(int i = 0; i < POOL_SIZE-1; i++) //@WCA loop=30
		{
			G1 temp = new G1();
			current.next = temp;
			current = temp;
		}
		last = current;
		initialized = true;
		return true;
	}
	
	//The G1 command is put into the Command queue, NOT the G1 pool
	public static boolean enqueue(Parameter parameters)
	{
		G1 temp;
		synchronized (lock) 
		{
			if(first == null)
			{
				//Empty pool
				return false;
			}
			temp = first;
			first = temp.next;
			if(first == null)
			{
				last = null;
			}
		}
		temp.parameters.X = parameters.X;
		temp.parameters.Y = parameters.Y;
		temp.parameters.Z = parameters.Z;
		temp.parameters.E = parameters.E;
		temp.parameters.F = parameters.F;
		temp.parameters.S = parameters.S;
		temp.executed = false;
		return temp.addToQueue();
	}
	
	@Override
	public boolean execute() 
	{
		if(!executed)
		{
			RepRapController.instance.setTarget(parameters);
			executed=true;
		}
		if(RepRapController.instance.inPosition())
		{
			returnToPool();
			return true;
		}
		return false;
	}
	
	private void returnToPool()
	{
		synchronized (lock) 
		{
			if(last == null)
			{
				//Empty queue
				first = this;
			}
			else
			{
				last.next = this;
			}
			last = this;
			next = null;
		}
	}
	
	@Override
	protected void respond() 
	{
		//Do nothing, already confirmed
	}
}
