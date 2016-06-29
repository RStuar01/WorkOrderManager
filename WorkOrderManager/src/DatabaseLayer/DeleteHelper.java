package DatabaseLayer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

/**
 * Class Name:		DeleteHelper
 * Description:		This class contains the methods called from the DatabaseDeleter class
 * 					to support the methods used there.
 * @author Richard Stuart
 * @created Sunday, 5,29,16
 */
public class DeleteHelper {

	private Connection connObj = null;
	
	/**
	 * Constructor called from the DatabaseDeleter class to create an instance of this class.
	 */
	public DeleteHelper() {
	}

	/**
	 * This method manages a showConfirmDialog to guarantee the users desire to delete
	 * JobSite and WorkOrder records associated with a specific Customer.
	 * @param numAttached				int variable specifying the number of associated records.
	 * @param type						String variable used to specify the record object type
	 * 									associated with the Customer.
	 * @return deletionConfirmed		Boolean value used to hold the users response.
	 */
	public Boolean askUserToConfirmDeletion(int numAttached, String type) {
		
		Boolean deletionConfirmed = false;
		int optionReturn;
		String numAttachedMessage = null;
		
		if(type.equalsIgnoreCase("jobSite")) {
			numAttachedMessage = "There are " + numAttached + " job sites attached to this customer!";
		}
		else if(type.equalsIgnoreCase("workOrder")) {
			numAttachedMessage = "There are " + numAttached + " work orders attached to this customer!";
		}
		
		optionReturn = JOptionPane.showConfirmDialog(null, numAttachedMessage + "\nDo you want to delete these?",
				"Confirm Deletion!", JOptionPane.YES_NO_OPTION);
		
		if(optionReturn == 0) {
			deletionConfirmed = true;
		}
		
		return deletionConfirmed;
	}
	
	/**
	 * This method deletes an address record from the database.
	 * @param addressID					String variable containing the address ID number.
	 */
	public void deleteAddress(String addressID) {
		
		String addressUpdate = null;
		
		addressUpdate = "delete from address where address_id = '" + addressID + "';";
						
		Statement stmt = null;
				
		connObj =  DatabaseWriter.getDBConnection();
		
		try {	
			stmt = connObj.createStatement();
			stmt.executeUpdate(addressUpdate);			
		}											
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
						
		 DatabaseWriter.closeConnection(connObj);
	}
	
	/**
	 * This method deletes a ContactInformation record from the database.
	 * @param contactID					String variable containin the contact information record ID number.
	 */
	public void deleteContactInfo(String contactID) {
		
		String contactInfoUpdate = null;
		
		contactInfoUpdate = "delete from contact_info where contact_info_id = '" + contactID + "';";
		
		Statement stmt = null;
						
		connObj =  DatabaseWriter.getDBConnection();
				
		try {	
			stmt = connObj.createStatement();
			stmt.executeUpdate(contactInfoUpdate);			
		}											
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
								
		 DatabaseWriter.closeConnection(connObj);
	}
	
	/**
	 * This method deletes a Contact Person record from the database.
	 * @param contactPersonID			String variable containing the contact person ID number.
	 */
	public void deleteContactPerson(String contactPersonID) {
		
		String contactUpdate = null;
		
		contactUpdate = "delete from site_contact_person where contact_id = '" + contactPersonID + "';";
		
		Statement stmt = null;
								
		connObj = DatabaseWriter.getDBConnection();
						
		try {	
			stmt = connObj.createStatement();
			stmt.executeUpdate(contactUpdate);			
		}											
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
										
		DatabaseWriter.closeConnection(connObj);
	}
	
	/**
	 * This method manages the deletion of records associated with a specific Contact Person.
	 * @param contactPersonID			String variable containing the contact person ID number.
	 */
	public void deleteContactPersonData(String contactPersonID) {
		
		String addressID = null;
		String contactID = null;
		String contactQuery = null;
		ResultSet rs = null;
		
		contactQuery = "select address_id, contact_info_id " +
				"from site_contact_person " +
				"where contact_id = '" + contactPersonID + "';";
						
		Statement stmt = null;
				
		connObj = DatabaseWriter.getDBConnection();
				
		try {
			stmt = connObj.createStatement();
			rs = stmt.executeQuery(contactQuery);
			while(rs.next()) {
				addressID = rs.getString(1);
				contactID = rs.getString(2);
			}
		}
		catch(SQLException e) {
			System.out.println(e.toString());
		}
				
		 DatabaseWriter.closeConnection(connObj);
		
		//delete the cp, address, contactInfo
		deleteContactPerson(contactPersonID);
		deleteAddress(addressID);
		deleteContactInfo(contactID);
	}
	
	/**
	 * This method deletes a Customer record from the database.
	 * @param customerID				String variable containing the Customer record ID number.
	 */
	public void deleteCustomer(String customerID) {
		
		String customerUpdate = null;
		
		customerUpdate = "delete from customer where customer_id = '" + customerID + "';";
		
		Statement stmt = null;
								
		connObj =  DatabaseWriter.getDBConnection();
						
		try {	
			stmt = connObj.createStatement();
			stmt.executeUpdate(customerUpdate);			
		}											
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
										
		 DatabaseWriter.closeConnection(connObj);
	}
	
	/**
	 * This method manages the deletion of address and contact information records
	 * associated with a specific Customer.
	 * @param customerID				String variable containing the customer ID number.
	 */
	public void deleteCustomerData(String customerID) {		
		
		String addressID = null;
		String contactID = null;
		String customerQuery = null;
		ResultSet rs = null;
		
		customerQuery = "select address_id, contact_info_id " +
				"from customer " +
				"where customer_id = '" + customerID + "';";
						
		Statement stmt = null;
				
		connObj = DatabaseWriter.getDBConnection();
				
		try {
			stmt = connObj.createStatement();
			rs = stmt.executeQuery(customerQuery);
			while(rs.next()) {
				addressID = rs.getString(1);
				contactID = rs.getString(2);
			}
		}
		catch(SQLException e) {
			System.out.println(e.toString());
		}
				
		DatabaseWriter.closeConnection(connObj);
		
		//now delete address, contactInfo, customer
		deleteCustomer(customerID);
		deleteAddress(addressID);
		deleteContactInfo(contactID);
	}

	/**
	 * This method deletes an Employee record from the database.
	 * @param employeeID				String variable containing the Employee ID number.
	 */
	public void deleteEmployee(String employeeID) {
	
		String employeeUpdate = null;
	
		employeeUpdate = "delete from employees where employee_id = '" + employeeID + "';";
	
		Statement stmt = null;
							
		connObj = DatabaseWriter.getDBConnection();
					
		try {	
			stmt = connObj.createStatement();
			stmt.executeUpdate(employeeUpdate);			
		}											
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
									
		DatabaseWriter.closeConnection(connObj);
	}
	
	/**
	 * This method manages the deletion of address and contact information records
	 * associated with a specific Employee.
	 * @param employeeID				String variable containing the employee record ID number.
	 */
	public void deleteEmployeeData(String employeeID) {
		
		String addressID = null;
		String contactID = null;
		String employeeQuery = null;
		ResultSet rs = null;
		
		employeeQuery = "select address_id, contact_info_id " +
				"from employees " +
				"where employee_id = '" + employeeID + "';";
						
		Statement stmt = null;
				
		connObj =  DatabaseWriter.getDBConnection();
				
		try {
			stmt = connObj.createStatement();
			rs = stmt.executeQuery(employeeQuery);
			while(rs.next()) {
				addressID = rs.getString(1);
				contactID = rs.getString(2);
			}
		}
		catch(SQLException e) {
			System.out.println(e.toString());
		}
				
		DatabaseWriter.closeConnection(connObj);
		
		deleteEmployee(employeeID);
		deleteAddress(addressID);
		deleteContactInfo(contactID);
	}
	
	/**
	 * This method deletes a JobSite record from the database.
	 * @param siteID					String variable containing the JobSite record ID number.
	 */
	public void deleteJobSite(String siteID) {
		
		String update = null;
		
		update = "delete from job_site where site_id = '" + siteID + "';";
		
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
	 * This method deletes all Job records associated with a specific work order record ID number.
	 * @param woNum						String variable containing the work order record ID number.
	 */
	public void deleteWOLineItems(String woNum) {
		
		String update = null;
		
		update = "delete from work_order_line_items where work_order_number = '" + woNum + "';";
		
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
	 * This method uses a JobSite record ID number to obtain the associated address record ID number.
	 * @param siteID					String variable containing the JobSite record ID number.
	 * @return addressID				String variable to hold the address record ID number.
	 */
	public String getAddressID(String siteID) {
		
		String addressID = null;
		String query = null;
		
		query = "select address_id from job_site where site_id = '" + siteID + "';";
		
		Statement stmt = null;
		
		ResultSet rs = null;
		
		connObj = DatabaseWriter.getDBConnection();
										
		try {	
			stmt = connObj.createStatement();
			rs = stmt.executeQuery(query);			
			while(rs.next()) {
				addressID = rs.getString(1);
			}
		}
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
				
		DatabaseWriter.closeConnection(connObj);	
		
		return addressID;
	}
	
	/**
	 * This method obtains the JobSite record ID numbers associated with a specific Customer record.
	 * @param customerID				String variable containing the Customer record ID number.		
	 * @param numAttachedJobSites		int variable used to specify the size of the string array 
	 * 									to be created.
	 * @return jobSiteIDs				String array to hold the JobSite record ID numbers associated
	 * 									with the customer ID number.
	 */
	public String[] getAttachedJobSiteIDs(String customerID, int numAttachedJobSites) {
		
		String[] jobSiteIDs = new String[numAttachedJobSites];
		String siteID = null;
		int arrayElement = 0;
		String query = null;
		
		query = "select site_id from job_site where customer_id = '" + customerID + "';";
		
		Statement stmt = null;
		
		ResultSet rs = null;
		
		connObj = DatabaseWriter.getDBConnection();
										
		try {	
			stmt = connObj.createStatement();
			rs = stmt.executeQuery(query);			
			while(rs.next()) {
				siteID = rs.getString(1);
				jobSiteIDs[arrayElement] = siteID;
				arrayElement++;
			}
		}
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
				
		DatabaseWriter.closeConnection(connObj);	
		
		return jobSiteIDs;
	}
	
	/**
	 * This method obtains the Work Order record ID numbers associated with a specific Customer record.
	 * @param customerID				String variable containing the Customer record ID number.
	 * @param numAttachedWorkOrders		int variable used to specify array size during creation.
	 * @return woNums					String array to hold the associated work order record ID numbers.
	 */
	public String[] getAttachedWorkOrderNumbers(String customerID, int numAttachedWorkOrders) {
		
		String[] woNums = new String[numAttachedWorkOrders];
		String woNum = null;
		int arrayElement = 0;
		String query = null;
		
		query = "select work_order_number from work_order where customer_id = '" + customerID + "';";
		
		Statement stmt = null;
		
		ResultSet rs = null;
		
		connObj = DatabaseWriter.getDBConnection();
										
		try {	
			stmt = connObj.createStatement();
			rs = stmt.executeQuery(query);			
			while(rs.next()) {
				woNum = rs.getString(1);
				woNums[arrayElement] = woNum;
				arrayElement++;
			}
		}
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
				
		DatabaseWriter.closeConnection(connObj);	
		
		return woNums;
	}
	
	/**
	 * This method determines the number of JobSites associated with a specific Site Contact Person.
	 * @param contactID					String variable containing the Contact Person record ID number.
	 * @return numAttachedJobSites		int variable to provide the number of JobSites associated with
	 * 									a specific contact person.
	 */
	public int getNumAttachedCPJobSites(String contactID) {
		
		int numAttachedJobSites = 0;
		String summaryQuery = null;
		
		summaryQuery = "select count(site_id) as num_sites_attached " +
				"from job_site where contact_id = '" + contactID + "';";
		
		Statement stmt = null;
		
		ResultSet rs = null;
		
		connObj =  DatabaseWriter.getDBConnection();
										
		try {	
			stmt = connObj.createStatement();
			rs = stmt.executeQuery(summaryQuery);			
			while(rs.next()) {
				numAttachedJobSites = rs.getInt(1);
			}
		}
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
				
		 DatabaseWriter.closeConnection(connObj);	
		
		return numAttachedJobSites;
	}
	
	/**
	 * This method determines the number of JobSites associated with a specific Customer.
	 * @param customerID				String variable containing the Customer record ID number.
	 * @return numAttachedJobSites		int variable to provide the number of JobSites associated with
	 * 									a specific Customer.
	 */
	public int getNumAttachedJobSites(String customerID) {
		
		int numAttachedJobSites = 0;
		String summaryQuery = null;
		
		summaryQuery = "select count(site_id) as num_sites_attached " +
				"from job_site where customer_id = '" + customerID + "';";
		
		Statement stmt = null;
		
		ResultSet rs = null;
		
		connObj = DatabaseWriter.getDBConnection();
										
		try {	
			stmt = connObj.createStatement();
			rs = stmt.executeQuery(summaryQuery);			
			while(rs.next()) {
				numAttachedJobSites = rs.getInt(1);
			}
		}
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
				
		DatabaseWriter.closeConnection(connObj);	
		
		return numAttachedJobSites;
	}
	
	/**
	 * This method determines the number of work orders associated with a specific Customer.
	 * @param customerID				String variable containing the Customer record ID number.
	 * @return numAttachedWorkOrders	int variable to supply the number of work orders associated
	 * 									with a specific Customer.
	 */
	public int getNumAttachedWorkOrders(String customerID) {			
		
		int numAttachedWorkOrders = 0;
		String summaryQuery = null;
		
		summaryQuery = "select count(work_order_number) as num_work_orders_attached " +
				"from work_order where customer_id = '" + customerID + "';";
		
		Statement stmt = null;
				
		ResultSet rs = null;
				
		connObj = DatabaseWriter.getDBConnection();
												
		try {	
			stmt = connObj.createStatement();
			rs = stmt.executeQuery(summaryQuery);			
			while(rs.next()) {
				numAttachedWorkOrders = rs.getInt(1);
			}
		}
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
						
		DatabaseWriter.closeConnection(connObj);	
		
		return numAttachedWorkOrders;
	}
	
	/**
	 * This method determines the number of current work orders associated with a specific JobSite.
	 * @param siteID					String variable containing the JobSite record ID number.
	 * @return numAttached				int variable specifying the number or work orders associated
	 * 									with a specific Jobsite.
	 */
	public int getNumberAttachedWorkOrders(String siteID) {
		
		String summaryQuery = null;
		int numAttached = 0;
		
		summaryQuery = "select count(work_order_number) as num_work_orders_attached " +
				"from work_order where site_id = '" + siteID + 
				"' and completion_date is Null;";
				
		Statement stmt = null;
				
		ResultSet rs = null;
				
		connObj = DatabaseWriter.getDBConnection();
												
		try {	
			stmt = connObj.createStatement();
			rs = stmt.executeQuery(summaryQuery);			
			while(rs.next()) {
				numAttached = rs.getInt(1);
			}
		}
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
						
		DatabaseWriter.closeConnection(connObj);
		return numAttached;
	}

	/**
	 * This method determines the number of current Jobs associated with a specific Employee.
	 * @param employeeID				String variable containing the employee record ID number.
	 * @return numAttachedJobs			int variable specifying the number of current Jobs
	 * 									associated with a specific Employee.
	 */
	public int getNumJobsAttached(String employeeID) {
		
		String summaryQuery = null;
		int numAttachedJobs = 0;
		
		summaryQuery = "select count(employee_id) as jobs_attached " +
				"from work_order_line_items where employee_id = '" + employeeID + "';";
				
		Statement stmt = null;
				
		ResultSet rs = null;
				
		connObj = DatabaseWriter.getDBConnection();
												
		try {	
			stmt = connObj.createStatement();
			rs = stmt.executeQuery(summaryQuery);			
			while(rs.next()) {
				numAttachedJobs = rs.getInt(1);
			}
		}
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
						
		DatabaseWriter.closeConnection(connObj);	
		
		return numAttachedJobs;
	}
	
	/**
	 * This method checks that referential integrity constraints are maintained before attempting
	 * to delete a Customer record from the database.
	 * @param customerID				String variable containing the Customer record ID number.
	 * @return jobSitesAttached			Boolean variable to ensure that referential integrity
	 * 									constraints are maintained within the database before
	 * 									attempting to delete a Customer record.
	 */
	public Boolean manageAttachedJobSites(String customerID) {
		
		Boolean jobSitesAttached = true;
		Boolean deletionConfirmed = false;
		int numAttachedJobSites = 0;
		String type = "jobSite";

		numAttachedJobSites = getNumAttachedJobSites(customerID);
		if(numAttachedJobSites == 0) {
			jobSitesAttached = false;
		}
		else {
			deletionConfirmed = askUserToConfirmDeletion(numAttachedJobSites, type);
			if(deletionConfirmed) {
				String[] siteIDs = new String[numAttachedJobSites];
				siteIDs = getAttachedJobSiteIDs(customerID, numAttachedJobSites);
				for(int a = 0; a < siteIDs.length; a++) {
					String addressID = getAddressID(siteIDs[a]);			
					deleteJobSite(siteIDs[a]);
					deleteAddress(addressID);
				}
				jobSitesAttached = false;
			}
		}
		
		return jobSitesAttached;
	}
	
	/**
	 * This method checks that referential integrity constraints are maintained before attempting
	 * to delete a Contact Person record from the database.
	 * @param contactID					String variable containing the contact person record ID number.
	 * @return deletionAllowed			Boolean variable to ensure that referential integrity
	 * 									constraints are maintained within the database before
	 * 									attempting to delete a Contact Person record.
	 */
	public Boolean manageCPReferentialIntegrityConstraints(String contactID) {
		
		Boolean deletionAllowed = false;
		int jobSitesAttached = 0;
		
		jobSitesAttached = getNumAttachedCPJobSites(contactID);
		
		if(jobSitesAttached == 0) {
			deletionAllowed = true;
		}
		else {
			JOptionPane.showMessageDialog(null, "There are " + jobSitesAttached + " Job Sites attached to this Contact Person.\n" +
					"These Job Sites must be assigned a new Contact Person prior to deletion!",
					"Deletion Not Allowed!", JOptionPane.WARNING_MESSAGE);
		}
		
		return deletionAllowed;
	}
	
	/**
	 * This method checks that referential integrity constraints are maintained before attempting
	 * to delete a Employee record from the database.
	 * @param employeeID				String variable containing the employee record ID number.
	 * @return deletionAllowed			Boolean variable to ensure that referential integrity
	 * 									constraints are maintained within the database before
	 * 									attempting to delete an Employee record.
	 */
	public Boolean manageEmployeeReferentialIntegrityConstraints(String employeeID) {
		
		Boolean deletionAllowed = false;
		int jobsAttached = 0;
		
		jobsAttached = getNumJobsAttached(employeeID);
		if(jobsAttached == 0) {
			deletionAllowed = true;
		}
		else {
			JOptionPane.showMessageDialog(null, "There are " + jobsAttached + " Jobs attached to this Employee.\n" +
					"These Jobs must be assigned to a new Employee prior to deletion!",
					"Deletion Not Allowed!", JOptionPane.WARNING_MESSAGE);
		}
		
		return deletionAllowed;
	}
	
	/**
	 * This method checks that referential integrity constraints are maintained before attempting
	 * to delete a JobSite record from the database.
	 * @param siteID					String variable containing the JobSite record ID number.
	 * @return deletionAllowed			Boolean variable to ensure that referential integrity
	 * 									constraints are maintained within the database before
	 * 									attempting to delete a Jobsite record.
	 */
	public Boolean manageJSReferentialIntegrityConstraints(String siteID) {
		
		Boolean deletionAllowed = false;
		int numWorkOrdersAttached = 1;									
		
		numWorkOrdersAttached = getNumberAttachedWorkOrders(siteID);
		if(numWorkOrdersAttached == 0) {
			deletionAllowed = true;
		}
		else {
			JOptionPane.showMessageDialog(null, "There are " + numWorkOrdersAttached + " Active Work Orders attached to this Job Site.\n" +
					"These Work Orders must be Completed prior to deletion!",
					"Deletion Not Allowed!", JOptionPane.WARNING_MESSAGE);
		}
	
		return deletionAllowed;
	}
}
