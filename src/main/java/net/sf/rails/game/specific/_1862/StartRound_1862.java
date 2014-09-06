package net.sf.rails.game.specific._1862;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.rails.common.LocalText;
import net.sf.rails.common.ReportBuffer;
import net.sf.rails.game.GameManager;
import net.sf.rails.game.Player;
import net.sf.rails.game.PublicCertificate;
import net.sf.rails.game.PublicCompany;
import net.sf.rails.game.StartItem;
import net.sf.rails.game.StartRound_AuctionOnly;
import net.sf.rails.game.StockSpace;
import net.sf.rails.game.state.Currency;

/**
 * Implements an 1830-style initial auction.
 */
public class StartRound_1862 extends StartRound_AuctionOnly {
    protected final int bidIncrement;

    // TODO: Fix when stock market is created
    private final static int MIN_START_SPACE_PRICE = 54;

    /**
     * Constructed via Configure
     */
    public StartRound_1862(GameManager parent, String id) {
        super(parent, id);
        bidIncrement = startPacket.getModulus();
    }

    protected void assignItem(Player player, StartItem item, int price, int sharePrice) {
        PublicCompany company = companyManager.getPublicCompany(item.getName());
        PublicCertificate primary = company.getPresidentsShare();
        primary.moveTo(player);
        String priceText = Currency.toBank(player, price + (3 * sharePrice));
        ReportBuffer.add(
                this,
                LocalText.getText("BuysItemFor", player.getId(),
                        primary.getName(), priceText));

        item.setSold(player, price);
    }

    @Override
    public String getHelp() {
        return "1862 Start Round help text"; // TODO: Is this used?
    }

    @Override
    protected SortedSet<StockSpace> getStartSpaces(int maxCash) {
        SortedSet<StockSpace> startSpaces = new TreeSet<StockSpace>();

        for (StockSpace s : stockMarket.getStartSpaces()) {
            if ((s.getPrice() * 3) <= maxCash) {
                startSpaces.add(s);
            }
        }
        return startSpaces;
    }

    @Override
    protected boolean playerCanBid(Player currentPlayer, StartItem item) {
        if (currentPlayer.getFreeCash() + item.getBid(currentPlayer) >= item.getMinimumBid()
                                                                        + (3 * MIN_START_SPACE_PRICE)) {
            return true;
        }
        return false;
    }

}