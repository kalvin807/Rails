/* $Header: /Users/blentz/rails_rcs/cvs/18xx/rails/ui/swing/MapPanel.java,v 1.11 2009/11/02 23:30:34 evos Exp $*/
package rails.ui.swing;

import javax.swing.*;

import org.apache.log4j.Logger;

import rails.game.*;
import rails.game.action.LayTile;
import rails.game.action.LayToken;
import rails.ui.swing.hexmap.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * MapWindow class displays the Map Window. It's shocking, I know.
 */
public class MapPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private MapManager mmgr;
    private HexMap map;
    private JScrollPane scrollPane;
    private GameUIManager gameUIManager;

    protected static Logger log =
            Logger.getLogger(MapPanel.class.getPackage().getName());

    public MapPanel(GameUIManager gameUIManager) {
        this.gameUIManager = gameUIManager;
        Scale.set(15);
        setLayout(new BorderLayout());

        mmgr = gameUIManager.getGameManager().getMapManager();
        try {
            map =
                    (HexMap) Class.forName(mmgr.getMapUIClassName()).newInstance();
            map.init(gameUIManager.getORUIManager(), mmgr);
        } catch (Exception e) {
            log.fatal("Map class instantiation error:", e);
            e.printStackTrace();
            return;
        }

        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

        scrollPane = new JScrollPane(map);
        scrollPane.setSize(map.getPreferredSize());

        add(scrollPane, BorderLayout.CENTER);

        setSize(map.getPreferredSize().width, map.getPreferredSize().height);
        setLocation(25, 25);
    }

    public void setAllowedTileLays(List<LayTile> allowedTileLays) {
        map.setAllowedTileLays(allowedTileLays);
    }

    public <T extends LayToken> void setAllowedTokenLays(
            List<T> allowedTokenLays) {
        map.setAllowedTokenLays(allowedTokenLays);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F1) {
            HelpWindow.displayHelp(gameUIManager.getHelp());
            e.consume();
        }
    }

    public void keyReleased(KeyEvent e) {}

    public HexMap getMap() {
        return map;
    }
}
