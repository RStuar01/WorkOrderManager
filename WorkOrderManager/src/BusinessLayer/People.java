package BusinessLayer;

/**
 * Class Name:						People
 * Description:						This is the SuperClass for Customer, ContactPerson, and Employee classes,
 * 									and provides data fields for the basic elements of each of these along
 * 									with get/set accessors/mutators, and the toString method.
 * @author Richard Stuart
 * @created Sunday, 5,29,16
 */
public class People {
	
	private String addressID;
	private String contactInfoID;
	private String lastName;
	private String firstName;
	private String streetAddress;
	private String city;
	private String state;
	private String zipCode;
	private String unitNumber;
	private String description;
	private String phoneNumber;
	private String cellPhone;
	private String email;
	private String title;
	
	//Default Constructor.
	public People() {
		this("", "", "", "", "", "", "", "", "", "", "", "", "", "");
	}
	
	//Overloaded Constructor.
	public People(String addressID, String contactInfoID, String lastName,
				String firstName, String streetAddress, String city, String state,
				String zipCode, String unitNumber, String description, 
				String phoneNumber, String cellPhone, String email, String title) {
		
		this.addressID = addressID;
		this.contactInfoID = contactInfoID;
		this.lastName = lastName;
		this.firstName = firstName;
		this.streetAddress = streetAddress;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.unitNumber = unitNumber;
		this.description = description;
		this.phoneNumber = phoneNumber;
		this.cellPhone = cellPhone;
		this.email = email;
		this.title = title;
	}
	
	//Get and Set Accessors/Mutators
	public void setAddressID(String addressID) {
		this.addressID = addressID;
	}
	
	public String getAddressID() {
		return addressID;
	}
	
	public void setContactInfoID(String contactInfoID) {
		this.contactInfoID = contactInfoID;
	}
	
	public String getContactInfoID() {
		return contactInfoID;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getFirstName() {
		return firstName;
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
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	
	public String getCellPhone() {
		return cellPhone;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	@Override
	public String toString() {
		if(this.title.equalsIgnoreCase("Customer") | this.title.equalsIgnoreCase("ContactPerson") | this.title.equalsIgnoreCase("Employee")) {
			return lastName + ", " + firstName;
		}
		else if(this.title.equalsIgnoreCase("Blank")) {
			return "";
		}
		else {
			return title;
		}
		
	}
}