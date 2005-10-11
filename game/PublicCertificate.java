/* $Header: /Users/blentz/rails_rcs/cvs/18xx/game/Attic/PublicCertificate.java,v 1.3 2005/10/11 17:35:29 wakko666 Exp $
 * 
 * Created on 09-Apr-2005 by Erik Vos
 * 
 * Change Log:
 */
package game;

/**
 * @author Erik
 */
public class PublicCertificate implements PublicCertificateI, Cloneable
{

	/** From which public company is this a certificate */
	protected PublicCompanyI company;
	/**
	 * Share percentage represented by this certificate
	 * 
	 * @deprecated
	 */
	protected int shares;
	/** President's certificate? */
	protected boolean president;
	/** Availability */
	protected boolean available;
	/** Current holder of the certificate */
	protected Portfolio portfolio;

	public PublicCertificate(int shares)
	{
		this(shares, false, true);
	}

	public PublicCertificate(int shares, boolean president)
	{
		this(shares, president, true);
	}

	public PublicCertificate(int shares, boolean president, boolean available)
	{
		this.shares = shares;
		this.president = president;
		this.available = available;
	}

	/**
	 * @return if Certificate is Available
	 */
	public boolean isAvailable()
	{
		return available;
	}

	/**
	 * @return Portfolio this certificate belongs to.
	 */
	public Portfolio getPortfolio()
	{
		return portfolio;
	}

	/**
	 * @return if this is a president's share
	 */
	public boolean isPresidentShare()
	{
		return president;
	}

	/**
	 * Get the number of shares that this certificate represents.
	 * 
	 * @return The number of shares.
	 */
	public int getShares()
	{
		return shares;
	}

	/**
	 * Get the percentage of ownership that this certificate represents. This is
	 * equal to the number of shares * the share unit.
	 * 
	 * @return The share percentage.
	 */
	public int getShare()
	{
		return shares * company.getShareUnit();
	}

	/**
	 * Get the current price of this certificate.
	 * 
	 * @return The current certificate price.
	 */
	public int getCertificatePrice()
	{
		if (company.getCurrentPrice() != null)
		{
			return company.getCurrentPrice().getPrice() * shares;
		}
		else
		{
			return 0;
		}
	}

	public String getName()
	{
		return company.getName() + " " + getShare() + "% "
				+ (president ? "president " : "") + "share";
	}

	/**
	 * @param b
	 */
	public void setAvailable(boolean b)
	{
		available = b;
	}

	/**
	 * @param portfolio
	 */
	public void setPortfolio(Portfolio portfolio)
	{
		this.portfolio = portfolio;
	}

	/**
	 * @param b
	 */
	public void setPresident(boolean b)
	{
		president = b;
	}

	/**
	 * @return
	 */
	public PublicCompanyI getCompany()
	{
		return company;
	}

	/**
	 * @param companyI
	 */
	public void setCompany(PublicCompanyI companyI)
	{
		company = companyI;
	}

	protected Object clone()
	{
		try
		{
			return super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			System.out.println("Cannot clone certificate:\n"
					+ e.getStackTrace());
			return null;
		}
	}

	public PublicCertificateI copy()
	{
		return (PublicCertificateI) this.clone();
	}

	public String toString()
	{
		return "PublicCertificate: " + company.getName() + ", Shares: "
				+ shares;
	}
}
