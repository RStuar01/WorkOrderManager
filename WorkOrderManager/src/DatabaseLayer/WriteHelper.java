package DatabaseLayer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class Name:		WriteHelper
 * Description:		This class contains the methods called from the DatabaseWriter class
 * 					to support the methods used there.
 * @author Richard Stuart
 * @created Sunday, 5,29,16
 */
public class WriteHelper {
	
	private Connection connObj = null;

	/**
	 * Constructor called from the DatabaseWriter class to create an instance of this class.
	 */
	public WriteHelper() {
	}

	/**
	 * This method uses supplied address information to obtain the ID number for an address which was just
	 * written to the database.
	 * @param stAddress				String variable for the street address.
	 * @param city					String variable for the city.
	 * @param state					String variable for the state.
	 * @param zipCode				String variable for the zip code.
	 * @param choice				String variable used to locate addresses associated with object types.
	 * @param unitNumber			String variable for the unit number.
	 * @param siteType				String variable used to describe the address type.
	 * @return addressID			String variable for the address ID number.
	 */
	public String obtainNewAddressID(String stAddress, String city, String state, String zipCode, String choice,
							String unitNumber, String siteType) {
		
		String addressID = null;
		String query = null;
		ResultSet rs = null;
		
		if(unitNumber.equals("NULL")) {
			query = "select address_id " +
					"from address " +
					"where street_address = '" + stAddress + "' and city = '" + city + "' and state = '" + state +
					"' and zip_code = '" + zipCode + "' and address_type = '" + choice + 
					"' and unit_number  is null and description = '" + siteType + "';";
		}
		else {
			query = "select address_id from address where street_address = '" + stAddress +
					"' and city = '" + city + "' and state = '" + state +
					"' and zip_code = '" + zipCode + "' and address_type = '" + choice +
					"' and unit_number = '" + unitNumber + "' and description = '" + siteType + "';";
		}
		
		Statement stmt = null;
								
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
	 * This method uses supplied information to locate and obtain the contact information ID number
	 * which corresponds to the information just written to the database.
	 * @param phoneNumber			String variable for the phone number.
	 * @param cellPhone				String variable for the cell phone number.
	 * @param emailAddress			String variable for the email address.
	 * @return contactInfoID		String variable for the contact info ID number.
	 */
	public String obtainNewContactInformationID(String phoneNumber, String cellPhone, String emailAddress) {
		
		String contactInfoID = null;
		String query = null;
		ResultSet rs = null;
		
		query = "select contact_info_id " +
				"from contact_info " +
				"where phone_number = '" + phoneNumber + "' and cell_phone = '" + cellPhone +
				"' and email = '" + emailAddress + "';";
		
		Statement stmt = null;
										
		connObj = DatabaseWriter.getDBConnection();
										
		try {	
			stmt = connObj.createStatement();
			rs = stmt.executeQuery(query);			
			while(rs.next()) {
				contactInfoID = rs.getString(1);
			}
		}											
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
						
		DatabaseWriter.closeConnection(connObj);		
		
		return contactInfoID;
	}
	
	/**
	 * This method uses the supplied information to obtain the work order number for the work order just
	 * written to the database.
	 * @param siteID				String variable for the JobSite ID number associated with this work order.
	 * @param custID				String variable for the Customer ID number associated with this work order.
	 * @param creationDate			String variable for the date the work order was created.
	 * @param appointmentDate		String variable for the date for which the next job is to be completed.
	 * @param appointmentTime		String variable for the time for which the next job is to be completed.
	 * @return woNum				String variable for the WorkOrder ID number.
	 */
	public String obtainWONum(String siteID, String custID, String creationDate, String appointmentDate,
			String appointmentTime) {
		
		String woNum = null;
		String woQuery = null;
		Statement stmt = null;
		
		woQuery = "select work_order_number from work_order where site_id = '" + siteID + "' and " +
				"customer_id = '" + custID + "' and creation_date = '" + creationDate + "' and " +
				"appointment_date = '" + appointmentDate + "' and appointment_time = '" + appointmentTime +
				"';";
		
		connObj = DatabaseWriter.getDBConnection();
		
		try {
			stmt = connObj.createStatement();
			ResultSet rs = stmt.executeQuery(woQuery);
			while (rs.next()) {
				woNum = rs.getString(1);
			}
		}
		catch (SQLException e) {
			System.out.println(e.toString());
		}
		
		DatabaseWriter.closeConnection(connObj);
		
		return woNum;
	}
	
	/**
	 * This method writes the information for a new address record to the database.
	 * @param streetAddress			String variable for the street address.
	 * @param city					String variable for the city.
	 * @param state					String variable for the state.
	 * @param zipCode				String variable for the zip code.
	 * @param choice				String variable for the address type.
	 * @param unitNumber			String variable for the unit number.
	 * @param siteDescription		String variable for the address site description.
	 */
	public void writeAddressInformation(String streetAddress, String city, String state, String zipCode,
			String choice, String unitNumber, String siteDescription) {
	
		String newAddressUpdate = null;
	
		newAddressUpdate = "insert into address " +
				"(address_id, street_address, city, state, zip_code, address_type, unit_number, description) " +
				"values (DEFAULT, '" + streetAddress + "', '" + city + "', '" + state + "', '" + zipCode +
				"', '" + choice + "', " + unitNumber + ", '" + siteDescription + "');";
		
		Statement stmt = null;
							
		connObj = DatabaseWriter.getDBConnection();
							
		try {	
			stmt = connObj.createStatement();
			stmt.executeUpdate(newAddressUpdate);			
		}											
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
			
		DatabaseWriter.closeConnection(connObj);		
	}
	
	/**
	 * This method writes a new contact information record to the database.
	 * @param phoneNumber			String variable for the phone number.
	 * @param cellPhone				String variable for the cell phone number.
	 * @param emailAddress			String variable for the email address.
	 */
	public void writeContactInformation(String phoneNumber, String cellPhone, String emailAddress) {
		
		String newContactInfoUpdate = null;
		
		newContactInfoUpdate = "insert into contact_info " +
				"(contact_info_id, phone_number, cell_phone, email) " +
				"values (DEFAULT, '" + phoneNumber + "', '" + cellPhone +
				"', '" + emailAddress + "');";
		
		Statement stmt = null;
		
		connObj = DatabaseWriter.getDBConnection();
		
		try {
			stmt = connObj.createStatement();
			stmt.executeUpdate(newContactInfoUpdate);
		}
		catch(SQLException e) {
			System.out.println(e.toString());
		}
		
		DatabaseWriter.closeConnection(connObj);
	}
	
	/**
	 * This method writes a new Contact Person record to the database.
	 * @param addressID				String variable for the address ID number.
	 * @param contactInfoID			String variable for the contact information ID number.
	 * @param lastName				String variable for the Contact Persons last name.
	 * @param firstName				String variable for the Contact Persons first name.
	 */
	public void writeContactPersonInformation(String addressID, String contactInfoID, String lastName, String firstName) {
		
		String newContactPersonUpdate = null;
		
		newContactPersonUpdate = "insert into site_contact_person " +
				"(contact_id, address_id, contact_info_id, last_name, first_name) " +
				"values (DEFAULT, '" + addressID + "', '" + contactInfoID + "', '" +
				lastName + "', '" + firstName + "');";
		
		Statement stmt = null;
		connObj = DatabaseWriter.getDBConnection();
		
		try {
			stmt = connObj.createStatement();
			stmt.executeUpdate(newContactPersonUpdate);
		}
		catch(SQLException e) {
			System.out.println(e.toString());
		}
		
		DatabaseWriter.closeConnection(connObj);
	}
	
	/**
	 * This method write a new Customer record to the database.
	 * @param addressID				String variable for the address ID number.
	 * @param contactInfoID			String variable for the contact information ID number.
	 * @param lastName				String variable for the Customers last name.
	 * @param firstName				String variable for the Customers first name.
	 */
	public void writeCustomerInformation(String addressID, String contactInfoID, String lastName, String firstName) {
		
		String newCustomerUpdate = null;
		
		newCustomerUpdate = "insert into customer " +
				"(customer_id, address_id, contact_info_id, last_name, first_name) " +
				"values (DEFAULT, '" + addressID + "', '" + contactInfoID + "', '" +
				lastName + "', '" + firstName + "');";
		
		Statement stmt = null;
										
		connObj = DatabaseWriter.getDBConnection();
										
		try {	
			stmt = connObj.createStatement();
			stmt.executeUpdate(newCustomerUpdate);			
		}											
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
						
		DatabaseWriter.closeConnection(connObj);		
	}
	
	/**
	 * This method writes edited address information for an address record.
	 * @param addressID				String variable for the address ID number.
	 * @param choice				String variable used to specify the object type the address is associated with.	
	 * @param stAddress				String variable for the street address.
	 * @param city					String variable for the city.
	 * @param state					String variable for the state.
	 * @param zipCode				String variable for the zip code.
	 * @param unitNumber			String variable for the unit number.
	 * @param siteType				String variable for the address site description.
	 */
	public void writeEditedAddressInformation(String addressID, String choice, String stAddress, String city,
			String state, String zipCode, String unitNumber, String siteType) {
		
		String editCustAddUpdate = null;
		
		editCustAddUpdate = "update address set street_address = '" + stAddress +
				"', city = '" + city + "', state = '" + state + "', zip_code = '" +
				zipCode + "', address_type = '" + choice + "', unit_number = " + unitNumber +
				", description = '" + siteType + "' where address_id = '" +
				addressID + "';";
		
		Statement stmt = null;
		connObj = DatabaseWriter.getDBConnection();
		
		try {
			stmt = connObj.createStatement();
			stmt.executeUpdate(editCustAddUpdate);
		}
		catch(SQLException e) {
			System.out.println(e.toString());
		}
		
		DatabaseWriter.closeConnection(connObj);
	}
	
	/**
	 * This method writes edited information for a contact information record.
	 * @param contactID				String variable for the contact information ID number.
	 * @param phoneNumber			String variable for the phone number.
	 * @param cellPhone				String variable for the cell phone number.
	 * @param emailAddress			String variable for the email address.
	 */
	public void writeEditedContactInformation(String contactID, String phoneNumber, String cellPhone, String emailAddress) {
		
		String editContactInfoUpdate = null;
		
		editContactInfoUpdate = "update contact_info set phone_number = '" + phoneNumber +
				"', cell_phone = '" + cellPhone + "', email = '" + emailAddress +
				"' where contact_info_id = '" + contactID + "';";
		
		Statement stmt = null;
		connObj = DatabaseWriter.getDBConnection();
		
		try {
			stmt = connObj.createStatement();
			stmt.executeUpdate(editContactInfoUpdate);
		}
		catch(SQLException e) {
			System.out.println(e.toString());
		}
		
		DatabaseWriter.closeConnection(connObj);
	}

	/**
	 * This method writes edited information for a ContactPerson record.
	 * @param contactPersonID		String variable for the contact person ID number.
	 * @param lastName				String variable for the contact persons last name.
	 * @param firstName				String variable for the contact persons first name.
	 */
	public void writeEditedContactPersonInformation(String contactPersonID, String lastName, String firstName) {
		
		String contactUpdate = null;
		
		contactUpdate = "update site_contact_person set last_name = '" + lastName +
				"', first_name = '" + firstName + "' where contact_id = '" + contactPersonID + "';";
						
		Statement stmt = null;
				
		connObj = DatabaseWriter.getDBConnection();
				
		try {
			stmt = connObj.createStatement();
			stmt.executeUpdate(contactUpdate);
		}
		catch(SQLException e) {
			System.out.println(e.toString());
		}
				
		DatabaseWriter.closeConnection(connObj);
			
	}
	
	/**
	 * This method writes edited information for a Customer record.
	 * @param customerID			String variable for the Customers ID number.
	 * @param lastName				String variable for the Customers last name.
	 * @param firstName				String variable for the Customers first name.
	 */
	public void writeEditedCustomerInformation(String customerID, String lastName, String firstName) {
		
		String editCustomerInfoUpdate = null;
		
		editCustomerInfoUpdate = "update customer set last_name = '" + lastName +
				"', first_name = '" + firstName + "' where customer_id = '" + customerID + "';";
		
		Statement stmt = null;
				
		connObj = DatabaseWriter.getDBConnection();
				
		try {
			stmt = connObj.createStatement();
			stmt.executeUpdate(editCustomerInfoUpdate);
		}
		catch(SQLException e) {
			System.out.println(e.toString());
		}
				
		DatabaseWriter.closeConnection(connObj);
	}
	
	/**
	 * This method writes edited information for an Employee record.
	 * @param employeeID			String variable for the employee ID number.
	 * @param lastName				String variable for the employees last name.
	 * @param firstName				String variable for the employees first name.
	 */
	public void writeEditedEmployeeInformation(String employeeID, String lastName, String firstName) {
		
		String employeeUpdate = null;
		
		employeeUpdate = "update employees set last_name = '" + lastName +
				"', first_name = '" + firstName + "' where employee_id = '" + employeeID + "';";
						
		Statement stmt = null;
				
		connObj = DatabaseWriter.getDBConnection();
				
		try {
			stmt = connObj.createStatement();
			stmt.executeUpdate(employeeUpdate);
		}
		catch(SQLException e) {
			System.out.println(e.toString());
		}
				
		DatabaseWriter.closeConnection(connObj);
	}

	/**
	 * This method writes edited information for a Job record.
	 * @param lineNumber			String variable for the Job line ID number.
	 * @param woNum					String variable for the associated work order ID number.
	 * @param employeeID			String variable for the associated employee ID number.
	 * @param jobDescription		String variable for the Job description.
	 * @param specialNotes			String variable for Job special notes.
	 * @param jobStatus				String variable for the Jobs' status.
	 * @param jobDate				String variable for the data which the Job is to be completed.
	 * @param jobTime				String variable for the time which the Job is to be completed.
	 */
	public void writeEditedJobInformation(String lineNumber, String woNum,  String employeeID, String jobDescription,
			String specialNotes, String jobStatus, String jobDate, String jobTime) {
		
		String editedJobUpdate = null;
		
		editedJobUpdate = "update work_order_line_items set employee_id = '" + employeeID + "', job_type = '" + jobDescription +
				"', special_notes = '" + specialNotes + "', job_status = '" + jobStatus + "', appointment_date = '" + jobDate +
				"', appointment_time = '" + jobTime +"' where line_number = '" + lineNumber + "' and work_order_number = '" + woNum + "';";
		
		Statement stmt = null;
									
		connObj = DatabaseWriter.getDBConnection();
		
		try {
			stmt = connObj.createStatement();
			stmt.executeUpdate(editedJobUpdate);
		}											
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
		
		DatabaseWriter.closeConnection(connObj);	
	}
	
	/**
	 * This method writes edited information for a JobSite record.
	 * @param siteID				String variable for the JobSite ID number.
	 * @param contID				String variable for the ContactPerson ID number.
	 * @param siteDescription		String variable for the JobSite description.
	 */
	public void writeEditedJobSiteInformation(String siteID, String contID, String siteDescription) {
		
		String editJSUpdate = null;
		Statement stmt = null;
		
		editJSUpdate = "update job_site set contact_id = '" + contID + "', site_description_name = '" +
				siteDescription + "' where site_id = '" + siteID + "';";
		
		connObj = DatabaseWriter.getDBConnection();
		
		try {
			stmt = connObj.createStatement();
			stmt.executeUpdate(editJSUpdate);
		}
		catch (SQLException e) {
			System.out.println(e.toString());
		}
		
		DatabaseWriter.closeConnection(connObj);
	}

	/**
	 * This method writes the information for a new Employee record to the database.
	 * @param addressID				String variable for the address ID number.
	 * @param contactInfoID			String variable for the contact information ID number.
	 * @param lastName				String variable for the Employees' last name.
	 * @param firstName				String variable for the Employees' first name.
	 */
	public void writeEmployeeInformation(String addressID, String contactInfoID, String lastName, String firstName) {
		
		String newEmployeeUpdate = null;
		
		newEmployeeUpdate = "insert into employees " +
				"(employee_id, address_id, contact_info_id, last_name, first_name) " +
				"values (DEFAULT, '" + addressID + "', '" + contactInfoID + "', '" +
				lastName + "', '" + firstName + "');";
		
		Statement stmt = null;
										
		connObj = DatabaseWriter.getDBConnection();
										
		try {	
			stmt = connObj.createStatement();
			stmt.executeUpdate(newEmployeeUpdate);			
		}											
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
						
		DatabaseWriter.closeConnection(connObj);		
	}
	
	/**
	 * This method writes the information for a new JobSite record to the database.
	 * @param addID					String variable for the address ID number.
	 * @param custID				String variable for the customer ID number.
	 * @param contID				String variable for the contact person ID number.
	 * @param siteDescription		String variable for the address site description.
	 */
	public void writeJobSite(String addID, String custID, String contID, String siteDescription) {
		
		String jobSiteUpdate = null;
		Statement stmt = null;
		
		jobSiteUpdate =  "insert into job_site (site_id, address_id, customer_id, contact_id, site_description_name) " +
				"values (default, '" + addID + "', '" + custID + "', '" + contID + "', '" + siteDescription + "');";
		
		connObj = DatabaseWriter.getDBConnection();
		
		try {
			stmt = connObj.createStatement();
			stmt.executeUpdate(jobSiteUpdate);
		}
		catch(SQLException e) {
			System.out.println(e.toString());
		}
		
		DatabaseWriter.closeConnection(connObj);
	}
	
	/**
	 * This method writes the information for a new work order record to the database.
	 * @param siteID				String variable for the associated JobSite ID number.
	 * @param custID				String variable for the associated Customer ID number.
	 * @param creationDate			String variable for the date the WorkOrder was created.
	 * @param appointmentDate		String variable for the data the next associated Job is to be completed.
	 * @param appointmentTime		String variable for the time the next associated Job is to be completed.
	 */
	public void writeWorkOrder(String siteID, String custID, String creationDate, String appointmentDate,
			String appointmentTime) {
		
		String completionDate = null;
		String newWorkOrderUpdate = null;
		Statement stmt = null;
		
		newWorkOrderUpdate = "insert into work_order (work_order_number, site_id, customer_id, creation_date, appointment_date, "
				+ "appointment_time, completion_date) values (DEFAULT, '" + siteID + "', '" + custID + "', '" +
				creationDate + "', '" + appointmentDate + "', '" + appointmentTime + "', " + completionDate + ");";
		
		connObj = DatabaseWriter.getDBConnection();
		
		try {
			stmt = connObj.createStatement();
			stmt.executeUpdate(newWorkOrderUpdate);
		}
		catch (SQLException e) {
			System.out.println(e.toString());
		}
		
		DatabaseWriter.closeConnection(connObj);
	}
}
