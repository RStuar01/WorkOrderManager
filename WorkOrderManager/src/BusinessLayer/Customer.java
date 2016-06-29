package BusinessLayer;

/**
 * Class Name:					Customer
 * Description:					This class inherits from People and provides fields specific
 * 								to this class, along with get/set accessors/mutators and 
 * 								an overridden toString method.
 * @author Richard Stuart
 * @created Sunday, 5,29,16
 */
public class Customer extends People {
	
	private String customerID;
	
	//Default Constructor
	public Customer() {
		super();		
		customerID = "";
	}
	
	//Overloaded Constructor
	public Customer(String customerID, String addressID, String contactInfoID,
			String lastName, String firstName, String streetAddress, String city,
			String state, String zipCode, String unitNumber, String description,
			String phoneNumber, String cellPhone, String email, String title) {
		
		super(addressID, contactInfoID, lastName, firstName, streetAddress, city,
			state, zipCode, unitNumber, description,
			phoneNumber, cellPhone, email, title);
		this.customerID = customerID;
	}
	
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	
	public String getCustomerID() {
		return customerID;
	}
	
	@Override
	public String toString() {
		
		if(this.getTitle().equals("Add New Customer")){
			return super.toString();
		}
		else {
			return super.toString() + " ID: " + customerID + "\n";
		}
	}

}
