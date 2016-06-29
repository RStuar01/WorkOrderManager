package BusinessLayer;

/**
 * Class Name:					JobSite
 * Description:					This class provides fields specific to JobSites,
 * 								along with get/set accessors/mutators and a toString method.
 * @author Richard Stuart
 * @created Sunday, 5,29,16
 */
public class JobSite {
	
	private String siteID;
	private String addressID;
	private String customerID;
	private String contactID;
	private String siteName;
	private String streetAddress;
	private String city;
	private String state;
	private String zipCode;
	private String unitNumber;
	private String customerLastName;
	private String customerFirstName;
	private String contactLastName;
	private String contactFirstName;
	private String title;
		
	//default constructor
	public JobSite() {
		this("", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
	}
		
	//Overloaded Constructor
	public JobSite(String siteID, String addressID, String customerID,
			String contactID, String siteName, String streetAddress,
			String city, String state, String zipCode, String unitNumber,
			String customerLastName, String customerFirstName,
			String contactLastName, String contactFirstName, String title) {
		this.siteID = siteID;
		this.addressID = addressID;
		this.customerID = customerID;
		this.contactID = contactID;
		this.siteName = siteName;
		this.streetAddress = streetAddress;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.unitNumber = unitNumber;
		this.customerLastName = customerLastName;
		this.customerFirstName = customerFirstName;
		this.contactLastName = contactLastName;
		this.contactFirstName = contactFirstName;
		this.title = title;	
	}
		
	//Get and Set Accessors and Mutators
	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}
		
	public String getSiteID() {
		return siteID;
	}
		
	public void setAddressID(String addressID) {
		this.addressID = addressID;
	}
		
	public String getAddressID() {
		return addressID;
	}
		
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
		
	public String getCustomerID() {
		return customerID;
	}
		
	public void setContactID(String contactID) {
		this.contactID = contactID;
	}
		
	public String getContactID() {
		return contactID;
	}
		
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
		
	public String getSiteName() {
		return siteName;
	}
		
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
		
	public String getStreetAddress() {
		return streetAddress;
	}
		
	public void setCity(String city) {
		this.city = city;
	}
		
	public String getCity() {
		return city;
	}
		
	public void setState(String state) {
		this.state = state;
	}
		
	public String getState() {
		return state;
	}
		
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
		
	public String getZipCode() {
		return zipCode;
	}
		
	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}
		
	public String getUnitNumber() {
		return unitNumber;
	}
		
	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}
		
	public String getCustomerLastName() {
		return customerLastName;
	}
		
	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}
		
	public String getCustomerFirstName() {
		return customerFirstName;
	}
		
	public void setContactLastName(String contactLastName) {
		this.contactLastName = contactLastName;
	}
		
	public String getContactLastName() {
		return contactLastName;
	}
		
	public void setContactFirstName(String contactFirstName) {
		this.contactFirstName = contactFirstName;
	}
		
	public String getContactFirstName() {
		return contactFirstName;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
		
	public String toString() {
		String jobSiteName = null;
		
		if(title.equalsIgnoreCase("There are no Job Sites for this Customer") | title.equalsIgnoreCase("Add New Job Site") |
				this.title.equalsIgnoreCase("Add New Contact Person")) {
			return title;
		}
		else {
			jobSiteName = customerFirstName + " " + customerLastName + "'s " + siteName;
		}
		
		return jobSiteName;
	}
}
