package BusinessLayer;

/**
 * Class Name:					Employee
 * Description:					This class inherits from People and provides fields specific
 * 								to this class, along with get/set accessors/mutators and 
 * 								an overridden toString method.
 * @author Richard Stuart
 * @created Sunday, 5,29,16
 */
public class Employee extends People {
	
	private String employeeID;
	
	//Default Constructor
	public Employee() {
		super();
		employeeID = "";
	}
	
	//Overloaded Constructor
	public Employee(String employeeID, String addressID, String contactInfoID, String lastName,
						String firstName, String streetAddress, String city, String state,
						String zipCode, String unitNumber, String description, String phoneNumber,
						String cellPhone, String email, String title) {
		super(addressID, contactInfoID, lastName, firstName, streetAddress, city, state, zipCode,
				unitNumber, description, phoneNumber, cellPhone, email, title);
		this.employeeID = employeeID;
	}
	
	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}
	
	public String getEmployeeID() {
		return employeeID;
	}

	@Override
	public String toString() {
		
		if(!this.getTitle().equalsIgnoreCase("Employee")) {
			return super.toString();
		}
		else if(this.getTitle().equalsIgnoreCase("Blank")) {
			return "";
		}
		return super.toString() + " ID: " + employeeID + "\n";
	}
}
