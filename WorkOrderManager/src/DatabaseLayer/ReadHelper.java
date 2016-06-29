package DatabaseLayer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

import BusinessLayer.ContactPerson;
import BusinessLayer.Customer;
import BusinessLayer.Employee;
import BusinessLayer.Job;
import BusinessLayer.JobSite;
import BusinessLayer.People;
import BusinessLayer.WorkOrder;

/**
 * Class Name:		ReadHelper
 * Description:		This class contains the methods called from the DatabaseReader class
 * 					to support the methods used there.
 * @author Richard Stuart
 * @created Sunday, 5,29,16
 */
public class ReadHelper {
	
	private Connection connObj = null;

	/**
	 * Constructor called from the DatabaseReader class to create an instance of this class.
	 */
	public ReadHelper() {
	}

	/**
	 * This methods adds a ContactPerson object used as a message in the Combo Box.
	 * @param people			ArrayList to hold the ContactPerson objects.
	 * @return people			ArrayList holding the ContactPerson objects.
	 */
	public ArrayList<People> addAllContactsUsedMessage(ArrayList<People> people) {
		
		ContactPerson allContactsUsedMessage = new ContactPerson();
		allContactsUsedMessage.setTitle("----------All Contacts are linked to Job Sites----------");
		people.add(allContactsUsedMessage);
	
		return people;
	}

	/**
	 * This method helps format the ComboBox by adding a title.
	 * @param people			ArrayList to hold the Employee objects.
	 * @return people			ArrayList holding the Employee objects.
	 */
	public ArrayList<People> addAllEmployeesMessage(ArrayList<People> people) {		
		
		Employee allEmployeesMessage = new Employee();
		allEmployeesMessage.setTitle("----------All Employees----------");
		people.add(allEmployeesMessage);
	
		return people;
	}

	/**
	 * This method adds a message to inform the user that all Employees are in use.
	 * @param people			ArrayList to hold the Employee objects.
	 * @return people			ArrayList holding the Employee objects.
	 */
	public ArrayList<People> addAllEmployeesUsedMessage(ArrayList<People> people) {
		
		People allEmployeesUsedMessage = (People) new Employee();
		allEmployeesUsedMessage.setTitle("All Employees have Jobs to perform");
		people.add(allEmployeesUsedMessage);
	
		return people;
	}

	/**
	 * This method uses the String variable to determine which title to add, then adds the
	 * title to help format the layout of the ComboBox objects.
	 * @param people			ArrayList to hold the People objects.
	 * @param choice			String variable used to determine which message to add.
	 * @return people			ArrayList holding the People objects.
	 */
	public ArrayList<People> addAssociatedTitle(ArrayList<People> people, String choice) {
		
		People associatedTitle = null;
		
		if(choice.equalsIgnoreCase("ContactPerson")) {
			associatedTitle = new ContactPerson();
			associatedTitle.setTitle("----------Associated Contact People----------");
		}
		else {
			associatedTitle = new Employee();
			associatedTitle.setTitle("----------Associated Employees----------");
		}
	
		people.add(associatedTitle);
		return people;
	}

	/**
	 * This method adds a black space to the ArrayList to help format the layout of the Combo Box.
	 * @param formattedList		ArrayList holding the People objects.
	 * @param choice			String variable to determine which message to add.
	 * @return formattedList	ArrayList holding the People objects.
	 */
	public ArrayList<People> addComboBoxSpace(ArrayList<People> formattedList, String choice) {	
		
		if(choice.equalsIgnoreCase("ContactPerson")) {
			ContactPerson blankSpace = new ContactPerson();
			blankSpace.setTitle("Blank");
			formattedList.add(blankSpace);
		}
		else if(choice.equalsIgnoreCase("Employee")) {
			Employee blankSpace = new Employee();
			blankSpace.setTitle("Blank");
			formattedList.add(blankSpace);
		}
	
		return formattedList;
	}
	
	/**
	 * This method adds a message used to allow the addition of a new object to the ArrayList.
	 * @param people			ArrayList to hold the ContactPerson objects.
	 * @return people			ArrayList holding the ContactPerson objects.
	 */
	public ArrayList<People> addNewContactPersonMessage(ArrayList<People> people) {			
		
		ContactPerson blankContact = new ContactPerson();
		blankContact.setTitle("Add New Contact Person");
		people.add(blankContact);
		
		return people;
	}

	/**
	 * This method adds a message to allow the addition of a new Customer.
	 * @param people			ArrayList to hold the Customer objects.
	 * @return people			ArrayList holding the Customer objects.
	 */
	public ArrayList<People> addNewCustomerMessage(ArrayList<People> people) {
		
		Customer blankCustomer = new Customer();
		blankCustomer.setTitle("Add New Customer");
		people.add(blankCustomer);
		
		return people;
	}

	/**
	 * This method adds a message to allow the addition of a new Job.
	 * @param jobs				ArrayList to hold the Job objects.
	 * @return jobs				ArrayList holding the Job objects.
	 */
	public ArrayList<Job> addNewJobMessage(ArrayList<Job> jobs) {
		
		jobs = new ArrayList<Job>();
		Job addNewJobMessage = new Job();		
		addNewJobMessage.setTitle("Add New Job");
		jobs.add(addNewJobMessage);
		return jobs;
	}

	/**
	 * This method adds a message to allow the addition of a new JobSite.
	 * @param jobSites			ArrayList to hold the JobSite objects.
	 * @return jobSites			ArrayList holding the JobSite objects.
	 */
	public ArrayList<JobSite> addNewJobSiteMessage(ArrayList<JobSite> jobSites) {		
		
		JobSite addNewJobSite = new JobSite();
		addNewJobSite.setTitle("Add New Job Site");
		jobSites.add(addNewJobSite);
	
		return jobSites;
	}
	
	/**
	 * This method adds a message to inform the user that there are no JobSites
	 * associated with this Customer.
	 * @param jobSites			ArrayList to hold the JobSite objects.
	 * @return jobSites			ArrayList holding the JobSite objects.
	 */
	public ArrayList<JobSite> addNoJSTitle(ArrayList<JobSite> jobSites) {
		
		JobSite noJSTitle = new JobSite();
		noJSTitle.setTitle("There are no Job Sites for this Customer");
		jobSites.add(noJSTitle);
	
		return jobSites;
	}

	/**
	 * This method adds a message to inform the user there are no WorkOrders
	 * associated with this Customer.
	 * @param workOrders		ArrayList to hold the WorkOrder objects.
	 * @return workOrders		ArrayList holding the WorkOrder objects.
	 */
	public ArrayList<WorkOrder> addNoWOsForThisCustomerMessage(ArrayList<WorkOrder> workOrders) {
		
		WorkOrder noWOsForThisCustomer = new WorkOrder();
		noWOsForThisCustomer.setTitle("There are No WorkOrders for this Customer!");
		workOrders.add(noWOsForThisCustomer);
	
		return workOrders;
	}
	
	/**
	 * This method uses a String variable to determine which message to add, then 
	 * adds the message to help format the ComboBox layout.
	 * @param people			ArrayList to hold the People objects.
	 * @param choice			String variable to aid in determining which message to add.
	 * @return people			ArrayList holding the People objects.
	 */
	public ArrayList<People> addUnusedTitle(ArrayList<People> people, String choice) {
		
		People unusedTitle = null;
	
		if(choice.equalsIgnoreCase("ContactPerson")) {
			unusedTitle = (People) new ContactPerson();
			unusedTitle.setTitle("----------Unused Contact People----------");
		}
		else {
			unusedTitle = (People) new Employee();
			unusedTitle.setTitle("----------Employees with No Jobs----------");
		}
	
		people.add(unusedTitle);
	
		return people;
	}

	/**
	 * This method executes an SQL statement to read the ContactPerson ID's 
	 * which are not currently linked to an existing JobSite.
	 * @return contactIDs		String ArrayList to hold the ContactPerson ID numbers.
	 */
	public ArrayList<String> getContactIdsWithNoJobSites() {
		
		String query = null;
		ArrayList<String> contactIDs = new ArrayList<>();
		String contactID = null;
	
		query = "select contact_id from site_contact_person " +
			"where contact_id not in " +
			"(select distinct contact_id from job_site);";
	
		Statement stmt = null;
						
		connObj = DatabaseWriter.getDBConnection();
						
		try {	
			stmt = connObj.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				contactID = rs.getString(1);
				contactIDs.add(contactID);
			}
		}
		catch (SQLException e) {
			System.out.println(e.toString());
		}
		
		DatabaseWriter.closeConnection(connObj);		
	
		return contactIDs;
	}
	
	/**
	 * This method executes an SQL statement to read ContactPerson information from the database,
	 * uses the data to create ContactPerson objects, and adds them to an ArrayList.
	 * @param people			ArrayList to hold ContactPerson objects.
	 * @param isOrderByCustomerSelected		Boolean used to determine what data to read.
	 * @param cID				String variable used to locate data associated with a particular Customer.
	 * @return people			ArrayList holding the ContactPerson objects.
	 */
	public ArrayList<People> getContactPeople(ArrayList<People> people, boolean isOrderByCustomerSelected, String cID) {	
		
		String query = null;
		String contactID = null;
		String addressID = null;
		String contactInfoID = null;
		String lastName = null;
		String firstName = null;
		String streetAddress = null;
		String city = null;
		String state = null;
		String zipCode = null;
		String unitNumber = null;
		String description = null;
		String phoneNumber = null;
		String cellPhone = null;
		String email = null;
		String title = "ContactPerson";
		
		query = selectCPQuery(query, isOrderByCustomerSelected, cID);
		Statement stmt = null;
				
		connObj = DatabaseWriter.getDBConnection();
				
		try {	
			stmt = connObj.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				contactID = rs.getString(1);
				addressID = rs.getString(2);
				contactInfoID = rs.getString(3);
				lastName = rs.getString(4);
				firstName = rs.getString(5);
				streetAddress = rs.getString(6);
				city = rs.getString(7);
				state = rs.getString(8);
				zipCode = rs.getString(9);
				unitNumber = rs.getString(10);
				description = rs.getString(11);
				phoneNumber = rs.getString(12);
				cellPhone = rs.getString(13);
				email = rs.getString(14);
				
				ContactPerson cp = new ContactPerson(contactID, addressID, contactInfoID, lastName,
						firstName, streetAddress, city, state, zipCode, unitNumber,
						description, phoneNumber, cellPhone, email, title);
				people.add(cp);		
			}
		}
		catch (SQLException e) {
			System.out.println(e.toString());
		}
			
		 DatabaseWriter.closeConnection(connObj);
		
		return people;
	}
	
	/**
	 * This method executes an SQL statement to obtain Employee data associated with
	 * the supplied ID number, creates an Employee object with the data and returns that object.
	 * @param id				String variable used to identify the associated information.
	 * @return newEmployee		Employee object.
	 */
	public Employee getCurrentEmployee(String id) {
		
		Employee newEmployee = null;
		String query = null;
		String contactID = null;
		String addressID = null;
		String contactInfoID = null;
		String lastName = null;
		String firstName = null;
		String streetAddress = null;
		String city = null;
		String state = null;
		String zipCode = null;
		String unitNumber = null;
		String description = null;
		String phoneNumber = null;
		String cellPhone = null;
		String email = null;
		String title = "Employee";

		query = "select employee_id, e.address_id, e.contact_info_id, last_name, first_name, " +
				"street_address, city, state, zip_code, unit_number, description, " +
				"phone_number, cell_phone, email " +
				"from employees e join address a on e.address_id = a.address_id " +
				"join contact_info ci on e.contact_info_id = ci.contact_info_id " +
				"where employee_id = '" + id + "';";

		Statement stmt = null;
					
		connObj = DatabaseWriter.getDBConnection();

		try {	
			stmt = connObj.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				contactID = rs.getString(1);
				addressID = rs.getString(2);
				contactInfoID = rs.getString(3);
				lastName = rs.getString(4);
				firstName = rs.getString(5);
				streetAddress = rs.getString(6);
				city = rs.getString(7);
				state = rs.getString(8);
				zipCode = rs.getString(9);
				unitNumber = rs.getString(10);
				description = rs.getString(11);
				phoneNumber = rs.getString(12);
				cellPhone = rs.getString(13);
				email = rs.getString(14);
		
				newEmployee = new Employee(contactID, addressID, contactInfoID, lastName,
					firstName, streetAddress, city, state, zipCode, unitNumber,
					description, phoneNumber, cellPhone, email, title);
			}
		}
		catch (SQLException e) {
			System.out.println(e.toString());
		}

		DatabaseWriter.closeConnection(connObj);

		return newEmployee;
	}
	
	/**
	 * This method executes an SQL statement to obtain ID numbers for all Employees
	 * that are not currently associated with an existing Job.
	 * @return employeeIDs		ArrayList holding the Employee ID numbers.
	 */
	public ArrayList<String> getEmployeeIDsWithNoJobs() {
		
		String query = null;
		ArrayList<String> employeeIDs = new ArrayList<>();
		String employeeID = null;
	
		query = "select employee_id from employees " +
			"where employee_id not in " +
			"(select distinct employee_id from work_order_line_items);";
	
		Statement stmt = null;
		
		connObj = DatabaseWriter.getDBConnection();
						
		try {	
			stmt = connObj.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				employeeID = rs.getString(1);
				employeeIDs.add(employeeID);
			}
		}
		catch (SQLException e) {
			System.out.println(e.toString());
		}
		
		DatabaseWriter.closeConnection(connObj);		
	
		return employeeIDs;
	}

	/**
	 * This method executes an SQL statement to obtain Employee information, create Employee
	 * objects with the information, and adds the objects to an ArrayList.
	 * @param people			ArrayList to hold the Employee objects.
	 * @param isOrderByCustomerSelected	Boolean used to determine what data to read.
	 * @param cID				String variable used to locate data associated with a particular Customer.
	 * @return people			ArrayList holding the Employee objects.
	 */
	public ArrayList<People> getEmployees(ArrayList<People> people, boolean isOrderByCustomerSelected, String cID) {
		
		String query = null;
		String employeeID = null;
		String addressID = null;
		String contactInfoID = null;
		String lastName = null;
		String firstName = null;
		String streetAddress = null;
		String city = null;
		String state = null;
		String zipCode = null;
		String unitNumber = null;
		String description = null;
		String phoneNumber = null;
		String cellPhone = null;
		String email = null;
		String title = "Employee";
		
		query = selectEmployeeQuery(isOrderByCustomerSelected, cID);
	
		Statement stmt = null;
				
		connObj = DatabaseWriter.getDBConnection();
				
		try {	
			stmt = connObj.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				employeeID = rs.getString(1);
				addressID = rs.getString(2);
				contactInfoID = rs.getString(3);
				lastName = rs.getString(4);
				firstName = rs.getString(5);
				streetAddress = rs.getString(6);
				city = rs.getString(7);
				state = rs.getString(8);
				zipCode = rs.getString(9);
				unitNumber = rs.getString(10);
				description = rs.getString(11);
				phoneNumber = rs.getString(12);
				cellPhone = rs.getString(13);
				email = rs.getString(14);
				
				Employee e = new Employee(employeeID, addressID, contactInfoID, lastName,
					firstName, streetAddress, city, state, zipCode, unitNumber,
					description, phoneNumber, cellPhone, email, title);
				people.add(e);		
			}
		}
		catch (SQLException e) {
			System.out.println(e.toString());
		}
			
		DatabaseWriter.closeConnection(connObj);		

		return people;
	}

	/**
	 * This method uses a boolean to determine which version of SQL statements should be used,
	 * and returns the appropriate statement.
	 * @param query				String variable to hold the necessary SQL statement.
	 * @param isOrderByCustomerSelected Boolean used to determine which SQL statement to return.
	 * @param cID				String variable used in the statement to identify what data to read.
	 * @return query			String variable holding the necessary SQL statement.
	 */
	public String selectCPQuery(String query, boolean isOrderByCustomerSelected, String cID) {
		
		if(isOrderByCustomerSelected) {
		
			//create a query for a specific customer
			query = "select distinct js.contact_id, scp.address_id, scp.contact_info_id, last_name, first_name, " +
				"street_address, city, state, zip_code, unit_number, description, phone_number, cell_phone, email " +
				"from job_site js, site_contact_person scp, address a, contact_info ci " +
				"where js.contact_id = scp.contact_id " +
				"and scp.address_id = a.address_id " +
				"and scp.contact_info_id = ci.contact_info_id " +
				"and js.customer_id = " + cID + ";";
		}
		else {
		
			// otherwise select all contact people
			query = "select contact_id, scp.address_id, scp.contact_info_id, last_name, first_name, " +
				"street_address, city, state, zip_code, unit_number, description, " +
				"phone_number, cell_phone, email " +
				"from site_contact_person scp join address a on scp.address_id = a.address_id " +
				"join contact_info ci on scp.contact_info_id = ci.contact_info_id;";
		}
	
		return query;
	}
	
	/**
	 * This method uses a boolean to determine which version of SQL statements should be used,
	 * and returns the appropriate statement.
	 * @param isOrderByCustomerSelected Boolean used to determine which SQL statement is required.
	 * @param cID				String variable used in the statement to identify what data to read.
	 * @return query			String variable holding the necessary SQL statement.			
	 */
	public String selectEmployeeQuery(boolean isOrderByCustomerSelected, String cID) {
		
		String query = null;
	
		if(isOrderByCustomerSelected) {
			//Selects all employees that are listed on work orders associated with that customer
			query = "SELECT DISTINCT woli.employee_id, e.address_id, e.contact_info_id, last_name, first_name, " +
					"street_address, city, state, zip_code, unit_number, description, phone_number, cell_phone, email " +
					"FROM work_order_line_items woli, employees e, address a, contact_info ci, work_order wo " +
					"WHERE woli.employee_id = e.employee_id and e.address_id = a.address_id " +
					"and e.contact_info_id = ci.contact_info_id and woli.work_order_number = wo.work_order_number " +
				    "and wo.customer_id = " + cID + ";";
		}
		else {
	
			//else create this query using all employees
			query = "select employee_id, e.address_id, e.contact_info_id, last_name, first_name, " +
					"street_address, city, state, zip_code, unit_number, description, " +
					"phone_number, cell_phone, email " +
					"from employees e join address a on e.address_id = a.address_id " +
					"join contact_info ci on e.contact_info_id = ci.contact_info_id;";
		}
	
		return query;
	}
	
	/**
	 * This method uses a boolean to determine which version of SQL statements should be used,
	 * and returns the appropriate statement.
	 * @param isOrderByCustomerSelected Boolean used to determine which SQL statement is required.
	 * @param cID				String variable used in the statement to identify what data to read.
	 * @return query			String variable holding the necessary SQL statement.			
	 */
	public String selectJobSiteQuery(boolean isOrderByCustomerSelected, String cID) {
		
		String query = null;
	
		if(isOrderByCustomerSelected) { 		
			query = "select distinct js.site_id, js.address_id, js.customer_id, js.contact_id, site_description_name, " +
				"street_address, city, state, zip_code, unit_number, " +
				"c.last_name as cust_last_name, c.first_name as cust_first_name, " +
				"scp.last_name as contact_last_name, scp.first_name as contact_first_name " +
				"from job_site js join address a " +
				"on js.address_id = a.address_id " +
				"join site_contact_person scp " +
				"on js.contact_id = scp.contact_id " +
				"join customer c " +
				"on js.customer_id = c.customer_id " +
				"and js.customer_id = " + cID + ";";		
		}
		else {
			query = "select site_id, js.address_id, js.customer_id, js.contact_id, site_description_name, " +
					"street_address, city, state, zip_code, unit_number, " +
					"c.last_Name as cust_last_name, c.first_name as cust_first_name, " +
					"scp.last_name as contact_last_name, scp.first_name as contact_first_name " +
					"from job_site js join address a on js.address_id = a.address_id " +
					"join customer c on js.customer_id = c.customer_id " +
					"join site_contact_person scp on js.contact_id = scp.contact_id;";
		}
	
		return query;
	}
	
	/**
	 * This method uses a boolean to determine which version of SQL statements should be used,
	 * and returns the appropriate statement.
	 * @param isOrderByCustomerSelected Boolean used to determine which SQL statement is required.
	 * @param cID				String variable used in the statement to identify what data to read.
	 * @return query			String variable holding the necessary SQL statement.			
	 */
	public String selectWorkOrderQuery(boolean isOrderByCustomerSelected, String cID) {
		
		String query = null;
	
		if(isOrderByCustomerSelected) { 
			query =	"select work_order_number, site_id, customer_id, " +
				"appointment_date, appointment_time, completion_date " +
				"from work_order " +
				"where customer_id = '" + cID + "' and completion_date is null;";
		}
		else {
			query = "select work_order_number, site_id, customer_id, " +
					"appointment_date, appointment_time, completion_date " +
					"from work_order where completion_date is null;";
		}
	
		return query;
	}
}
