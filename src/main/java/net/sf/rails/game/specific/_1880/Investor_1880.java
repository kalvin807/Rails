package net.sf.rails.game.specific._1880;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sf.rails.algorithms.NetworkEdge;
import net.sf.rails.algorithms.NetworkGraph;
import net.sf.rails.algorithms.NetworkVertex;
import net.sf.rails.algorithms.RevenueAdapter;
import net.sf.rails.algorithms.RevenueStaticModifier;
import net.sf.rails.common.parser.ConfigurationException;
import net.sf.rails.game.BaseToken;
import net.sf.rails.game.CompanyManager;
import net.sf.rails.game.Player;
import net.sf.rails.game.PublicCompany;
import net.sf.rails.game.RailsItem;
import net.sf.rails.game.RailsRoot;
import net.sf.rails.game.Stop;
import net.sf.rails.game.Train;
import net.sf.rails.game.TrainManager;
import net.sf.rails.game.state.Owner;
import net.sf.rails.game.state.State;

import org.jgrapht.graph.SimpleGraph;



public class Investor_1880 extends PublicCompany implements RevenueStaticModifier {
    /*
     * Investors in 1880 get chosen at start after the initial starting package is sold out. They get one share from a new company 
     * 

     */
    protected boolean canOwnShare=true;

    protected int maxPercofShares=1;

    protected boolean hasStockPrice=false;

    protected boolean hasParPrice=false;

    protected PublicCompany linkedCompany;  // An Investor is always linked to a (exactly one) Public Major Company..

    /* Investors in 1880 operate with the newest train model on lease from the bank for zero costs.
     */
    protected boolean canBorrowTrain=true;

    private BuildingRights_1880 buildingRights = new BuildingRights_1880(this,"buildingRights"); 

    /*
     * 
     */

    public Investor_1880(RailsItem parent, String id) {
        super(parent, id);    
    }

    public boolean canOwnShare(){
        return canOwnShare;
    }

    public int maxPercofShares(){
        return maxPercofShares;
    }
    public boolean hasStockPrice(){
        return hasStockPrice;
    }

    public boolean hasParPrice(){
        return hasParPrice;
    }

    public boolean setLinkedCompany(PublicCompany linkedCompany){
        if (linkedCompany != null){
            //Check if Company is valid i.e. not Closed maybe check if theres already the President sold and just the president...
            if(!linkedCompany.isClosed()){
                this.linkedCompany=linkedCompany;
                return true;}
        }
        return false; 
    }

    public PublicCompany getLinkedCompany(){
        return linkedCompany;
    }

    public void finishConfiguration(RailsRoot root)
            throws ConfigurationException {
        super.finishConfiguration(root);
        getRoot().getRevenueManager().addStaticModifier(this);
    }

    public boolean modifyCalculator(RevenueAdapter revenueAdapter) {
        // check if running company is this company, otherwise quit
        if (revenueAdapter.getCompany() == this) {
            TrainManager trainManager=getRoot().getTrainManager();
            Set<Train> NewTrains = trainManager.getAvailableNewTrains();
            Train [] NewTrainArray = NewTrains.toArray(new Train[NewTrains.size()]);
            revenueAdapter.addTrainByString( NewTrainArray[0].getType().getName());
        }
        return false;
    }

    public String prettyPrint(RevenueAdapter revenueAdapter) {
        return null;
    }

    public boolean canRunTrains() {
        // By the time communism hits, this company can't run anyway.
        return true;       
    }

    public int getCurrentTrainLimit() {
        return 0;
    }

    public boolean isConnectedToLinkedCompany() {
        NetworkGraph nwGraph = NetworkGraph.createMapGraph(getRoot());
        NetworkGraph companyGraph =
                NetworkGraph.createRouteGraph(nwGraph, this, true);
        SimpleGraph<NetworkVertex, NetworkEdge> graph =
                companyGraph.getGraph();
        Set<NetworkVertex> verticies = graph.vertexSet();

        PublicCompany_1880 linkedCompany =
                (PublicCompany_1880) ((Investor_1880) this).getLinkedCompany();
        if (linkedCompany != null) {
            for (BaseToken token : linkedCompany.getLaidBaseTokens()) {
                Owner holder = token.getOwner();
                if (!(holder instanceof Stop)) continue;
                Stop stop = (Stop) holder;
                for (NetworkVertex vertex : verticies) {
                    if (vertex.getType() == NetworkVertex.VertexType.STATION) {
                        if ((stop.getRelatedStation() == vertex.getStation())
                                && (stop.getParent() == vertex.getHex())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    static public Investor_1880 getInvestorForPlayer(CompanyManager companyManager, Player player) {
        for (Investor_1880 investor : getInvestors(companyManager)) {
            if (investor.getPresident() == player) {
                return investor;
            }
        }
        return null;
    }

    static public List<Investor_1880> getInvestors(CompanyManager companyManager) {
        List<Investor_1880> investors = new ArrayList<Investor_1880>();
        for (PublicCompany company : companyManager.getAllPublicCompanies()) {
            if (company instanceof Investor_1880) {
                investors.add((Investor_1880) company);
            }
        }
        return investors;
    }

    public BuildingRights_1880 getRightsModel() {
        return buildingRights;
    }

    /* (non-Javadoc)
     * @see rails.game.PublicCompany#hasLaidHomeBaseTokens()
     */
    @Override
    public boolean hasLaidHomeBaseTokens() {
        if (this.hasOperated()) {
            return true;
        }
        return baseTokens.nbLaidTokens() > 0;
    }

 
}