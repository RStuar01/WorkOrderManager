package BusinessLayer;

/**
 * Class Name:					ContactPerson
 * Description:					This class inherits from People and provides fields specific
 * 								to this class, along with get/set accessors/mutators and 
 * 								an overridden toString method.
 * @author Richard Stuart
 * @created Sunday, 5,29,16
 */
public class ContactPerson extends People {
	
	private String contactID;
	
	//Default Constructor.
	public ContactPerson() {
		super();
		contactID = "";
	}
	
	//Overloaded Constructor
	public ContactPerson(String contactID, String addressID, String contactInfoID,
			String lastName, String firstName, String streetAddress, String city,
			String state, String zipCode, String unitNumber, String description,
			String phoneNumber, String cellPhone, String email, String title) {
		
		super(addressID, contactInfoID, lastName, firstName, streetAddress, city,
			state, zipCode, unitNumber, description, phoneNumber, cellPhone, email, title);
		this.contactID = contactID;
		
	}
	
	public void setContactID(String contactID) {
		this.contactID = contactID;
	}
	
	public String getContactID() {
		return contactID;
	}
	
	@Override					
	public String toString() {
		if(this.getTitle().equals("Add New Contact Person") | this.getTitle().equalsIgnoreCase("----------Associated Contact People----------") |
				this.getTitle().equalsIgnoreCase("") | this.getTitle().equalsIgnoreCase("Blank")) {
			return super.toString();
		}
		else if(this.getTitle().equalsIgnoreCase("----------Unused Contact People----------") | this.getTitle().equalsIgnoreCase("----------All Contacts are linked to Job Sites----------")) {
			return super.toString();
		}
		else {
			return super.toString() + " ID: " + contactID + "\n";
		}
	}	
}
