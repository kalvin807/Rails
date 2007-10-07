/* $Header: /Users/blentz/rails_rcs/cvs/18xx/rails/game/TileManager.java,v 1.5 2007/10/07 20:14:54 evos Exp $ */package rails.game;import java.util.*;import rails.util.*;public class TileManager implements TileManagerI, ConfigurableComponentI{	protected static TileManagerI instance = null;	protected Map<Integer, TileI> tileMap = new HashMap<Integer, TileI>();		private static List directories = new ArrayList();	/**	 * No-args constructor.	 */	public TileManager()	{		instance = this;	}	public static TileManagerI get()	{		return instance;	}	/**	 * @see rails.game.ConfigurableComponentI#configureFromXML(org.w3c.dom.Element)	 */	public void configureFromXML(Tag tileSetTop) throws ConfigurationException	{		/*		 * Note: prefix se is used for elements from TileSet.xml, prefix te for		 * elements from Tiles.xml.		 */		String tileDefFileName = tileSetTop.getAttributeAsString("tiles");		if (tileDefFileName == null)			throw new ConfigurationException(LocalText.getText("NoTilesXML"));		directories.add("data/" + ComponentManager.getGameName());		Tag tileDefTop = Tag.findTopTagInFile(tileDefFileName, directories, "Tiles");		if (tileDefTop == null)			throw new ConfigurationException(LocalText.getText("NoTilesTag"));		List<Tag> tileSetList = tileSetTop.getChildren("Tile");		List<Tag> tileDefList = tileDefTop.getChildren("Tile");		/*		 * The XML files TileSet.xml and Tiles.xml are read side by side, as		 * each one configures different tile aspects. The reason for having two		 * XML files is, that Tiles.xml defines per-tile aspects that are the		 * same for all games (such as the colour, tracks and stations; this		 * file is an automatically generated subset of the generic file		 * tiles/Tiles.xml), whereas TileSet.xml specifies the aspects that are		 * (or can be) specific to each rails.game (such as the possible upgrades).		 * <p>TileSet.xml is leading.		 */		int tileId;		TileI tile;		// Creates maps to the tile definitions in both files.		Map<Integer, Tag> tileSetMap = new HashMap<Integer, Tag>();		Map<Integer, Tag> tileDefMap = new HashMap<Integer, Tag>();		for (Tag tileSetTag : tileSetList)		{			tileId = tileSetTag.getAttributeAsInteger("id");			/*			 * Check for duplicates (this also covers missing tile ids, as this			 * returns 0, and we always have a tile numbered 0!			 */			if (tileSetMap.containsKey(tileId))			{				throw new ConfigurationException(				        LocalText.getText("DuplicateTilesetID", String.valueOf(tileId)));			}			tileSetMap.put(tileId, tileSetTag);		}		for (Tag tileDefTag : tileDefList)		{			tileId = tileDefTag.getAttributeAsInteger("id");			/*			 * Check for duplicates (this also covers missing tile ids, as this			 * returns 0, and we always have a tile numbered 0!			 */			if (tileDefMap.containsKey(tileId))			{				throw new ConfigurationException(				        LocalText.getText("DuplicateTileID", String.valueOf(tileId)));			}			else if (!tileSetMap.containsKey(tileId))			{				throw new ConfigurationException(				        LocalText.getText("TileMissingInTileSet", String.valueOf(tileId)));			}			tileDefMap.put(tileId, tileDefTag);		}		// Create the Tile objects (must be done before further parsing)		for (Integer id : tileSetMap.keySet())		{			tile = new Tile(id);			tileMap.put(id, tile);		}		// Finally, parse the <Tile> subtags		for (Integer id : tileMap.keySet())		{			tile = tileMap.get(id);			tile.configureFromXML(tileSetMap.get(id),					tileDefMap.get(id));		}	}	public TileI getTile(int id)	{		return tileMap.get(id);	}}