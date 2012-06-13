package rails.game.specific._18AL;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import rails.algorithms.NetworkVertex;
//import rails.algorithms.RevenueAdapter;
//import rails.algorithms.RevenueBonus;
//import rails.algorithms.RevenueManager;
//import rails.algorithms.RevenueStaticModifier;
import rails.common.parser.ConfigurableComponent;
import rails.common.parser.ConfigurationException;
import rails.common.parser.Tag;
import rails.game.*;
import rails.game.state.Item;
import rails.util.Util;

public class NamedTrainToken extends Token implements ConfigurableComponent /*, RevenueStaticModifier */ {

    protected static Logger log =
        LoggerFactory.getLogger(NamedTrainToken.class.getPackage().getName());

    private String name;
    private String longName;
    private int value;
    private String hexesString;
    private List<MapHex> hexes;
    private String description;

    public NamedTrainToken() {
        super();
    }

    public void configureFromXML(Tag tag) throws ConfigurationException {
        value = tag.getAttributeAsInteger("value");
        if (value <= 0) {
            throw new ConfigurationException("Missing or invalid value "
                                             + value);
        }

        name = tag.getAttributeAsString("name");
        if (!Util.hasValue(name)) {
            throw new ConfigurationException(
                    "Named Train token must have a name");
        }

        longName = tag.getAttributeAsString("longName");
        if (!Util.hasValue(longName)) {
            throw new ConfigurationException(
                    "Named Train token must have a long name");
        }

        hexesString = tag.getAttributeAsString("ifRouteIncludes");

        description =
                longName + " [" + hexesString + "] +" + Bank.format(value);
    }

    @Override
    public void init(Item parent) {
        super.checkedInit(parent, name, NameTrains.class);
    }
    
    public void finishConfiguration (GameManager gameManager)
    throws ConfigurationException {

        if (hexesString != null) {
            hexes = gameManager.getMapManager().parseLocations(hexesString);
        }
        
        // add them to the call list of the RevenueManager
//        GameManager.getInstance().getRevenueManager().addStaticModifier(this);
        
    }

    public String getId() {
        return name;
    }

    public String getLongName() {
        return longName;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return description;
    }

    public List<MapHex> getHexesToPass() {
        return hexes;
    }


//    public void modifyCalculator(RevenueAdapter revenueAdapter) {
//
//        // 1. check if holder is a NameableTrain
//        if (!(this.holder instanceof NameableTrain)) return;
//        NameableTrain train = (NameableTrain)this.holder;
//        
//        // 2. check if operating company is owner of the train
//        if (train.getOwner() == revenueAdapter.getCompany()) {
//            // 2. define revenue bonus
//            RevenueBonus bonus = new RevenueBonus(value, name);
//            bonus.addTrain(train);
//            for (NetworkVertex vertex:NetworkVertex.getVerticesByHexes(revenueAdapter.getVertices(), hexes)) {
//                if (!vertex.isStation()) continue;
//                bonus.addVertex(vertex);
//            }
//            revenueAdapter.addRevenueBonus(bonus);
//        }
//    }

}
