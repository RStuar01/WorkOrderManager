package DatabaseLayer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import BusinessLayer.ContactPerson;
import BusinessLayer.Customer;
import BusinessLayer.Employee;
import BusinessLayer.People;

/**
 * Class Name:		DatabaseDeleter
 * Description:		This class contains the methods called using data access objects to Delete information
 * 					from the database, and also methods which call these methods directly.
 * @author Richard Stuart
 * @created Sunday, 5,29,16
 */
public class DatabaseDeleter implements DeleterDAO {

	private Connection connObj = null;
	private DeleteHelper deleteHelper;
	
	/**
	 * This is the Constructor that is called from the DAOFactory class.
	 * This method also calls the DeleteHelper Constructor to provide an instance of that class.
	 */
	public DatabaseDeleter() {	
		deleteHelper = new DeleteHelper();
	}
	
	/**
	 * This method deletes a Job record from the database.
	 * @param lineNumber				String variable used to identify the Job record.
	 */
	public void deleteJob(String lineNumber) {
		
		String update = "delete from work_order_line_items where line_number = '" + lineNumber + "';";
	
		Statement stmt = null;
	
		connObj = DatabaseWriter.getDBConnection();
	
		try {
			stmt = connObj.createStatement();
			stmt.executeUpdate(update);
		}											
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
	
		DatabaseWriter.closeConnection(connObj);	
	}
	
	/**
	 * This method manages calling the methods required to delete all data associated with 
	 * a specific JobSite record.
	 * @param siteID					String variable containing the JobSite ID number.
	 */
	public void deleteJobSiteData(String siteID) {
		
		Boolean deletionAllowed = false;
		String addressID = null;
	
		deletionAllowed = deleteHelper.manageJSReferentialIntegrityConstraints(siteID);
		if(deletionAllowed) {
			deleteHelper.getAddressID(siteID);
			deleteHelper.deleteJobSite(siteID);
			deleteHelper.deleteAddress(addressID);
		}
	}
	
	/**
	 * This method manages the process of deleting a Person record and all associated records
	 * from the database.
	 * @param choice					String variable used to determine which type of object 
	 * 									to delete from the database.
	 * @param p							People object containing the information associated
	 * 									with the object.							
	 */
	public void deletePeopleData(String choice, People p) {
		
		String customerID = null;
		String contactID = null;
		String employeeID = null;
		Boolean deletionAccepted = false;
		Boolean deletionAllowed = false;
		
		if(choice.equalsIgnoreCase("Customer")) {	
			Customer c = (Customer) p;
			if(c != null) {
				customerID = c.getCustomerID();
				deletionAccepted = manageReferentialIntegrityConstraints(customerID);		
				if(deletionAccepted) {
					deleteHelper.deleteCustomerData(customerID);	
				}
			}
		}
		else if(choice.equalsIgnoreCase("ContactPerson")) {	
			ContactPerson cp = (ContactPerson) p;
			if(cp != null) {							
				contactID = cp.getContactID();
				deletionAllowed = deleteHelper.manageCPReferentialIntegrityConstraints(contactID);
				if(deletionAllowed) {
					deleteHelper.deleteContactPersonData(contactID);
				}
			}
		}
		else if(choice.equalsIgnoreCase("Employee")) {	
			Employee e = (Employee) p;
			if(e != null) {							
				employeeID = e.getEmployeeID();
				deletionAllowed = deleteHelper.manageEmployeeReferentialIntegrityConstraints(employeeID);
				if(deletionAllowed) {
					deleteHelper.deleteEmployeeData(employeeID);
				}
			}
		}
	}
	
	/**
	 * This method deletes a WorkOrder record from the database.
	 * @param woNum						String variable containing the work order ID number.
	 */
	public void deleteWorkOrder(String woNum) {
		
		String update = null;
		
		update = "delete from work_order where work_order_number = '" + woNum + "';";
		
		Statement stmt = null;
		connObj = DatabaseWriter.getDBConnection();
		
		try {
			stmt = connObj.createStatement();
			stmt.executeUpdate(update);
		}
		catch(SQLException e) {
			System.out.println(e.toString());
		}
		
		DatabaseWriter.closeConnection(connObj);
	}
	
	/**
	 * This method manages the deletion of all work order records associated with a specific Customer.
	 * @param customerID				String variable containing the Customer ID number.
	 * @return workOrdersAttached		Boolean value used to determine if deletion was successful.
	 */
	public Boolean manageAttachedWorkOrders(String customerID) {
		
		Boolean workOrdersAttached = true;
		Boolean deletionConfirmed = false;
		int numAttachedWorkOrders = 0;
		String type = "workOrder";
		
		numAttachedWorkOrders = deleteHelper.getNumAttachedWorkOrders(customerID);
		if(numAttachedWorkOrders == 0) {
			workOrdersAttached = false;
		}
		else {
			deletionConfirmed = deleteHelper.askUserToConfirmDeletion(numAttachedWorkOrders, type);
			if(deletionConfirmed) {
				String[] workOrderNumbers = new String[numAttachedWorkOrders];
				workOrderNumbers = deleteHelper.getAttachedWorkOrderNumbers(customerID, numAttachedWorkOrders);
				for(int a = 0; a < workOrderNumbers.length; a++) {
					String woNum = workOrderNumbers[a];
					deleteHelper.deleteWOLineItems(woNum);
					deleteWorkOrder(woNum);	
				}
				workOrdersAttached = false;
			}
		}
		
		return workOrdersAttached;
	}

	/**
	 * This method ensures that database referential integrity constraints are met before
	 * allowing the attempted deletion of a Customer record from the database.
	 * @param customerID				String variable containing the Customer ID number.
	 * @return deletionAccepted			Boolean value to show that deletion of a Customer
	 * 									record is allowed.
	 */
	public Boolean manageReferentialIntegrityConstraints(String customerID) {
		
		Boolean deletionAccepted = false;
		Boolean jobSitesAttached = true;
		Boolean workOrdersAttached = true;
	
	
		workOrdersAttached = manageAttachedWorkOrders(customerID);
		if(!workOrdersAttached) {
			jobSitesAttached = deleteHelper.manageAttachedJobSites(customerID);		
			if(!jobSitesAttached) {
				deletionAccepted = true;
			}
		}
	
	
		return deletionAccepted;
	}
}
