/*
 * $Header: /Users/blentz/rails_rcs/cvs/18xx/game/Attic/CompanyType.java,v 1.6 2005/08/08 20:08:27 evos Exp $
 * Created on 19mar2005 by Erik Vos
 * Changes: 
 */
package game;

import org.w3c.dom.Element;

/**
 * Objects of this class represent a particular type of company, of which
 * typically multiple instances exist in a game. Examples: "Private",
 * "Minor", "Major", "Mountain" etc.
 * <p>This class contains common properties of the companies of one type,
 * and aids in configuring the companies by reducing the need to repeatedly 
 * specify common properties with different companies. 
 * @author Erik Vos
 */
public class CompanyType implements CompanyTypeI {
	
	/*--- Class attributes ---*/
	
	/*--- Instance attributes ---*/
	protected String name;
	protected String className;
	protected String auctionType;
	protected int allClosePhase;
	//protected ArrayList defaultCertificates;
	protected int capitalisation = PublicCompanyI.CAPITALISE_FULL;
	
	private CompanyI dummyCompany;
	
	/**
	 * The constructor.
	 * @param name Company type name ("Private", "Public", "Minor" etc.).
	 * @param className Name of the class that will instantiate this type of company.
	 * @param element The &lt;CompanyType&gt; DOM element, used to define this company type.
	 */
	public CompanyType (String name, String className) {
		this.name = name;
		this.className = className;
	}
	
	public void configureFromXML(Element element) throws ConfigurationException {
	    
	    /* Create a dummy company implementing this company type */
        try
        {
        dummyCompany = (Company) Class.forName(className).newInstance();
        }
        catch (Exception e)
        {
           throw new ConfigurationException("Class " + className
                 + " cannot be instantiated", e);
        }
        dummyCompany.init("", this);
        dummyCompany.configureFromXML(element);

	    
	    /* Must be rewritten to a new tag 
        String capitalMode = XmlUtils.extractStringAttribute(nnp,
         		"capitalisation", "full");
         setCapitalisation(capitalMode);
         */

	    
	}
	
	public CompanyI createCompany (String name, Element element) 
			throws ConfigurationException 
		 {
	    CompanyI newCompany = null;
	    try { 
		    newCompany = (CompanyI) dummyCompany.clone();
		    newCompany.init (name, this);
		    newCompany.configureFromXML(element);
	    } catch (CloneNotSupportedException e) {
	        Log.error ("Cannot create company "+name+" by cloning");
	    }
        return newCompany;
	}
	
	/*--- Getters and setters ---*/
	/**
	 * @return
	 */
	public int getAllClosePhase() {
		return allClosePhase;
	}

	/**
	 * @return
	 */
	public String getAuctionType() {
		return auctionType;
	}

	/**
	 * Get the company type name
	 * @return The name of this company type.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param i
	 */
	public void setAllClosePhase(int i) {
		allClosePhase = i;
	}

	/**
	 * @param string
	 */
	public void setAuctionType(String string) {
		auctionType = string;
	}

	/**
	 * Get the name of the class that will implement this type of company.
	 * @return The full class name.
	 */
	public String getClassName() {
		return className;
	}

	/** Duuring initialisation (XML parsing), the &lt;CompanyType&gt; DOM element
	 * will be retained to allow the various companies to reuse it.
	 * To save memory space, this DOM element must be released when all companies have
	 * been instantiated. 
	 * <p><i>(Note: this feature may be replaced with the creation of a cloneable
	 * "dummy company" inside this class; but that will eventually also need to be released)</i>.
	 */
	/*
	public void releaseDomElement () {
		domElement = null;
	}
	*/
	/**
	 * Get the saved &lt;CompanyType&gt; DOM element.
	 * @return
	 */
	/*
	public Element getDomElement() {
		return domElement;
	}
	*/

	/** 
	 * Add a certificate to the dummy company represented by this type.
	 * @param certificate The certificate to add.
	 */
	/*
	public void addCertificate (PublicCertificateI certificate) {
		if (defaultCertificates == null) defaultCertificates = new ArrayList();
		defaultCertificates.add (certificate);
	}
	*/
	
	/**
	 * Get the dummy certificate array for cloning in a real company.
	 * return The dummy certificate array.
	 */
	/*
	public List getDefaultCertificates () {
		return defaultCertificates;
	}
	*/
	
	public void setCapitalisation (int mode) {
		this.capitalisation = mode;
	}
	public void setCapitalisation (String mode) {
		if (mode.equalsIgnoreCase("full")) {
			this.capitalisation = PublicCompanyI.CAPITALISE_FULL;
		} else if (mode.equalsIgnoreCase("incremental")) {
			this.capitalisation = PublicCompanyI.CAPITALISE_INCREMENTAL;
		}
	}
	public int getCapitalisation () {
		return capitalisation;
	}
}
