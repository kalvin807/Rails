/* $Header: /Users/blentz/rails_rcs/cvs/18xx/rails/game/PhaseManager.java,v 1.7 2007/10/07 20:14:54 evos Exp $ */
package rails.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rails.game.state.State;
import rails.util.Tag;

public class PhaseManager implements PhaseManagerI, ConfigurableComponentI
{

	protected static PhaseManagerI instance = null;
	protected static ArrayList<Phase> phaseList;
	protected static HashMap<String, Phase> phaseMap;

	protected static int numberOfPhases = 0;
	protected static State currentPhase = new State ("CurrentPhase", Phase.class);

	public PhaseManager()
	{

		instance = this;
	}

	public static PhaseManagerI getInstance()
	{
		return instance;
	}

	public void configureFromXML(Tag tag) throws ConfigurationException
	{
		/*
		 * Phase class name is now fixed but can be made configurable, if
		 * needed.
		 */
		List<Tag> phaseTags = tag.getChildren("Phase");
		numberOfPhases = phaseTags.size();
		phaseList = new ArrayList<Phase>();
		phaseMap = new HashMap<String, Phase>();
		Phase phase;
		String name;

		int n = 0;
		for (Tag phaseTag : phaseTags)
		{
			name = phaseTag.getAttributeAsString("name", ""
					+ (n + 1));
			phase = new Phase(n++, name);
			phaseList.add(phase);
			phaseMap.put(name, phase);
			phase.configureFromXML(phaseTag);
		}
		PhaseI initialPhase = (PhaseI) phaseList.get(0);
		setPhase (initialPhase);

	}

	public PhaseI getCurrentPhase()
	{
		return (PhaseI) currentPhase.getObject();
	}

	public int getCurrentPhaseIndex()
	{
		return getCurrentPhase().getIndex();
	}

	public void setPhase(String name)
	{
		setPhase (phaseMap.get(name));
	}
	
	protected void setPhase (PhaseI phase) {
		if (phase != null)
		{
			currentPhase.set (phase);
			GameManager.initialiseNewPhase(phase);
		}
	}
	
	public static PhaseI getPhaseNyName (String name) {
	    return phaseMap.get(name);
	}

}
