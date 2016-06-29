package DatabaseLayer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

import BusinessLayer.ContactPerson;
import BusinessLayer.Customer;
import BusinessLayer.Job;
import BusinessLayer.JobSite;
import BusinessLayer.People;
import BusinessLayer.WorkOrder;

/**
 * Class Name:		DatabaseReader
 * Description:		This class contains the methods called using data access objects to Read information
 * 					from the database, and also methods which call these methods directly.
 * @author Richard Stuart
 * @created Sunday, 5,29,16
 */
public class DatabaseReader implements ReaderDAO {
	
	private Connection connObj = null;
	private ArrayList<People> people = null;
	private ArrayList<Job> jobs = null;
	private ArrayList<JobSite> jobSites = null;
	private ArrayList<WorkOrder> workOrders = null;
	private ReadHelper readHelper = null;

	/**
	 * This is the Constructor which is called from the DAOFactory class.
	 * This method also calls the ReadHelper Constructor to provide an instance of that class.
	 */
	public DatabaseReader() {
		readHelper = new ReadHelper();
	}
	
	/**
	 * This method manages the addition of ContactPeople which are not currently associated with
	 * a JobSite.  It also provides customized formatting to display Used and Unused ContactPeople
	 * along with messages to identify them to allow them to be displayed concurrently in the 
	 * drop down box.
	 * @param people			ArrayList to hold the ContactPerson/People objects and messages.
	 * @param choice			String variable used to identify which type of object is being added to the
	 * 							ArrayList.
	 * @return people			Formatted ArrayList of ContactPerson/People type objects.
	 */
	public ArrayList<People> addContactsWithNoJobSites(ArrayList<People> people, String choice) {
		
		ArrayList<String> idsWithNoJobSites = new ArrayList<>();
		
		idsWithNoJobSites = readHelper.getContactIdsWithNoJobSites();
		
		//If size is 0, then all contacts are associated with a current JobSite.
		if(idsWithNoJobSites.size() == 0) {
			people = readHelper.addAllContactsUsedMessage(people);
		}
		else {
			people = readHelper.addUnusedTitle(people, choice);
			
			for(String id: idsWithNoJobSites) {
				People newCP = (People) getCurrentContactPerson(id);
				people.add(newCP);
			}
		}
		
		return people;
	}
	
	/**
	 * This method manages the addition of Employees which are not currently associated with
	 * a Job.  It also provides customized formatting to display Used and Unused Employees
	 * along with messages to identify them to allow them to be displayed concurrently in the 
	 * drop down box.
	 * @param people			ArrayList to hold the Employee/People objects and messages.
	 * @param choice			String variable used to identify which type of object is being added to the
	 * 							ArrayList.
	 * @return people			Formatted ArrayList of Employee/People type objects.
	 */
	public ArrayList<People> addEmployeesWithNoJobs(ArrayList<People> people, String choice) {
		
		ArrayList<String> employeeIDsWithNoJobs = new ArrayList<>();
		
		employeeIDsWithNoJobs = readHelper.getEmployeeIDsWithNoJobs();
		people = readHelper.addUnusedTitle(people, choice);
		
		//If size is 0, then all Employees are associated with a current Job.
		if(employeeIDsWithNoJobs.size() == 0) {
			people = readHelper.addAllEmployeesUsedMessage(people);
		}
		else {
			
			for(String id: employeeIDsWithNoJobs) {
				People emp = (People) readHelper.getCurrentEmployee(id);
				people.add(emp);
			}
		}
		
		return people;
	}
	
	/**
	 * This method creates a Job type object and sets the title for use
	 * as a message in the customized Combo Box display.  It also adds
	 * this message to the arrayList.
	 * @param jobs				ArrayList to hold Job type objects.
	 * @return jobs				Formatted ArrayList of Job type objects.
	 */
	public ArrayList<Job> addNewJobMessage(ArrayList<Job> jobs) {
		
		ArrayList<Job> jobsCopy = new ArrayList<Job>();
		Job addNewJobMessage = new Job();
		
		addNewJobMessage.setTitle("Add New Job");
		jobsCopy.add(addNewJobMessage);
		
		//if the jobs arrayList contained items, add them to this ArrayList
		if(jobs != null) {
			for(Job j: jobs) {
				jobsCopy.add(j);
			}
		}
		
		return jobsCopy;
	}

	/**
	 * This method executes an SQL query to read ContactPerson/People information from
	 * the database.  It then creates an object of this type using the information.
	 * @param contactID			String variable used to locate the correct database record.
	 * @return currentContact	ContactPerson/People type object.
	 */
	public ContactPerson getCurrentContactPerson(String contactID) {
		
		ContactPerson currentContact = null;
		String query = null;
		String addressID = null;
		String contInfoID = null;
		String lastName = null;
		String firstName = null;
		String stAddress = null;
		String city = null;
		String state = null;
		String zipCode = null;
		String unitNumber = null;
		String description = null;
		String phoneNumber = null;
		String cellPhone = null;
		String email = null;
		String title = "ContactPerson";
		
		query = "SELECT scp.address_id, scp.contact_info_id, scp.last_name, scp.first_name, " +
				"street_Address, city, state, zip_code, unit_number, description, " +
				"phone_number, cell_phone, email " +
				"FROM site_contact_person scp join address a on scp.address_id = a.address_id " +
				"join contact_info ci on scp.contact_info_id = ci.contact_info_id " +
				"and contact_id = '" + contactID + "';";
		
		Statement stmt = null;
											
		connObj =  DatabaseWriter.getDBConnection();
						
		try {	
			stmt = connObj.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				addressID = rs.getString(1);
				contInfoID = rs.getString(2);
				lastName = rs.getString(3);
				firstName = rs.getString(4);
				stAddress = rs.getString(5);
				city = rs.getString(6);
				state = rs.getString(7);
				zipCode = rs.getString(8);
				unitNumber = rs.getString(9);
				description = rs.getString(10);
				phoneNumber = rs.getString(11);
				cellPhone = rs.getString(12);
				email = rs.getString(13);
				
				currentContact = new ContactPerson(contactID, addressID, contInfoID, lastName, firstName, stAddress, city,
										state, zipCode, unitNumber, description, phoneNumber, cellPhone, email, title);
			}
		}
		catch (SQLException e) {
			System.out.println(e.toString());
		}
				
		 DatabaseWriter.closeConnection(connObj);		
				
		return currentContact;	
	}

	/**
	 * This method executes an SQL query to read Customer/People information from
	 * the database.  It then creates an object of this type using the information.
	 * @param customerID		String variable to identify the correct database record.
	 * @return cCustomer		Customer/People type object.
	 */
	public Customer getCurrentCustomer(String customerID) {
		
		Customer cCustomer = null;
		String query = null;
		String addressID = null;
		String contInfoID = null;
		String lastName = null;
		String firstName = null;
		String stAddress = null;
		String city = null;
		String state = null;
		String zipCode = null;
		String unitNumber = null;
		String description = null;
		String phoneNumber = null;
		String cellPhone = null;
		String email = null;
		String title = "Customer";
		
		query = "SELECT c.address_id, c.contact_info_id, c.last_name, c.first_name, " +
					"street_Address, city, state, zip_code, unit_number, description, " +
					"phone_number, cell_phone, email " +
					"FROM customer c join address a on c.address_id = a.address_id " +
					"join contact_info ci on c.contact_info_id = ci.contact_info_id " +
					"and customer_id = '" + customerID + "';";
		
		Statement stmt = null;
									
		connObj = DatabaseWriter.getDBConnection();
				
		try {	
			stmt = connObj.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				addressID = rs.getString(1);
				contInfoID = rs.getString(2);
				lastName = rs.getString(3);
				firstName = rs.getString(4);
				stAddress = rs.getString(5);
				city = rs.getString(6);
				state = rs.getString(7);
				zipCode = rs.getString(8);
				unitNumber = rs.getString(9);
				description = rs.getString(10);
				phoneNumber = rs.getString(11);
				cellPhone = rs.getString(12);
				email = rs.getString(13);
				
				cCustomer = new Customer(customerID, addressID, contInfoID, lastName, firstName, stAddress, city,
										state, zipCode, unitNumber, description, phoneNumber, cellPhone, email, title);
				
			}
		}
		catch (SQLException e) {
			System.out.println(e.toString());
		}
			
		DatabaseWriter.closeConnection(connObj);		
		
		return cCustomer;
	}
	
	/**
	 * This method executes an SQL query to read JobSite information from
	 * the database.  It then creates an object of this type using the information.
	 * @param siteID			String variable to identify the correct database record.
	 * @return jobSite			JobSite type object.
	 */
	public JobSite getCurrentJobSite(String siteID) {
		
		JobSite jobSite = null;
		String query = null;
		String addressID = null;
		String customerID = null;
		String contactID = null;
		String siteName = null;
		String streetAddress = null;
		String city = null;
		String state = null;
		String zipCode = null;
		String unitNumber = null;
		String customerLastName = null;
		String customerFirstName = null;
		String contactLastName = null;
		String contactFirstName = null;
		String title = "JobSite";
		
		query = "select js.address_id, js.customer_id, js.contact_id, site_description_name, " +
				"street_address, city, state, zip_code, unit_number, " +
				"c.last_Name as cust_last_name, c.first_name as cust_first_name, " +
				"scp.last_name as contact_last_name, scp.first_name as contact_first_name " +
				"from job_site js join address a on js.address_id = a.address_id " +
				"join customer c on js.customer_id = c.customer_id " +
				"join site_contact_person scp on js.contact_id = scp.contact_id " +
				"where site_id = " + siteID + ";";
		
		Statement stmt = null;
										
		connObj =  DatabaseWriter.getDBConnection();
										
		try {	
			stmt = connObj.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				addressID = rs.getString(1);
				customerID = rs.getString(2);
				contactID = rs.getString(3);
				siteName = rs.getString(4);
				streetAddress = rs.getString(5);
				city = rs.getString(6);
				state = rs.getString(7);
				zipCode = rs.getString(8);
				unitNumber = rs.getString(9);
				customerLastName = rs.getString(10);
				customerFirstName = rs.getString(11);
				contactLastName = rs.getString(12);
				contactFirstName = rs.getString(13);
					
			jobSite = new JobSite(siteID, addressID, customerID, contactID,
					siteName, streetAddress, city, state, zipCode, unitNumber,
					customerLastName, customerFirstName, contactLastName, 
					contactFirstName, title);
			}
		}
		catch (SQLException e) {
			System.out.println(e.toString());
		}
				
		 DatabaseWriter.closeConnection(connObj);		
		
		return jobSite;				
	}

	/**
	 * This method executes an SQL query to read Customer/People records, create 
	 * Customer/People type objects from the information, and add these objects
	 * to an ArrayList.  It also allows for a customized message to be added to
	 * the ArrayList by using a boolean to determine if the message should be added.
	 * @param addCustMessage	Boolean variable to determine if the message should
	 * 							be added to the arrayList.
	 * @return people			ArrayList containing Customer/People type objects.
	 */
	public ArrayList<People> getCustomers(boolean addCustMessage) {
		
		people = new ArrayList<People>();
		String customerID = null;
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
		String title = "Customer";
		
		String query = "select customer_id, c.address_id, c.contact_info_id, last_name, first_name, " +
				"street_address, city, state, zip_code, unit_number, description, " +
				"phone_number, cell_phone, email " +
				"from customer c join address a on c.address_id = a.address_id " +
				" join contact_info ci on c.contact_info_id = ci.contact_info_id;";
				
		Statement stmt = null;
			
		connObj = DatabaseWriter.getDBConnection();
		
		try {	
			stmt = connObj.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				customerID = rs.getString(1);
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
				
				Customer c = new Customer(customerID, addressID, contactInfoID, lastName,
						firstName, streetAddress, city, state, zipCode, unitNumber,
						description, phoneNumber, cellPhone, email, title);
				People p = (People) c;
				people.add(p);
			}
		}
		catch (SQLException e) {
			System.out.println(e.toString());
		}
		
		DatabaseWriter.closeConnection(connObj);
		
		if(addCustMessage) {
			readHelper.addNewCustomerMessage(people);
		}
		
		return people;
	}
	
	/**
	 * This method executes an SQL query to read Job records, create Job type
	 * objects using the information, and to load these objects into an 
	 * ArrayList.
	 * @param woNum				String variable to identify associated records.
	 * @return jobs				ArrayList of Job type objects.
	 */
	public ArrayList<Job> getJobs(String woNum) {
		
		jobs = new ArrayList<Job>();
		String query = null;
		String lineNumber = null;
		String employeeID = null;
		String jobType = null;
		String specialNote = null;
		String jobStatus = null;
		String appointmentDate = null;
		String appointmentTime = null;
		String lastName = null;
		String firstName = null;
		String title = "Job";
		
		query = "SELECT line_number, woli.employee_id, job_type, special_notes, job_status, " +
				"appointment_date, appointment_time, e.last_name, e.first_name " +
				"from employees e join work_order_line_items woli " +
				"where e.employee_id = woli.employee_id and work_order_number = '" + woNum + "';";
					
		Statement stmt = null;
		ResultSet rs = null;
				
		connObj = DatabaseWriter.getDBConnection();
						
		try {	
			stmt = connObj.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				lineNumber = rs.getString(1);
				employeeID = rs.getString(2);
				jobType = rs.getString(3);
				specialNote = rs.getString(4);
				jobStatus = rs.getString(5);
				appointmentDate = rs.getString(6);
				appointmentTime = rs.getString(7);
				lastName = rs.getString(8);
				firstName = rs.getString(9);
				
				Job j = new Job(lineNumber, employeeID, jobType, specialNote, jobStatus, appointmentDate,
						appointmentTime, lastName, firstName, title);
				jobs.add(j);
			}
		}
		catch (SQLException e) {
			System.out.println(e.toString());
		}
		
		DatabaseWriter.closeConnection(connObj);
					
		return jobs;
	}
	
	/**
	 * This method executes an SQL query to identify and obtain the JobSite ID Number
	 * associated with the information.
	 * @param streetAddress		String variable for JobSite address.
	 * @param city				String variable for JobSite city.
	 * @param state				String variable for JobSite state.
	 * @param zipCode			String variable for JobSite zip code.
	 * @param siteDescription	String variable for JobSite description.
	 * @param custID			String variable for Customer ID number.
	 * @param contID			String variable for ContactPerson ID number.
	 * @return idNumber			String variable for JobSite ID number.
	 */
	public String getJobSiteIDNumber(String streetAddress, String city,
			String state, String zipCode, String siteDescription,
			String custID, String contID) {
		
		String idNumber = "";
		String query = "";
		
		query = "select js.site_id from job_site js join address a on js.address_id = a.address_id " +
				"and street_address = '" + streetAddress + "' and city = '" + city + "' and state = '" +
				state + "' and zip_Code = '" + zipCode + "' and site_description_name = '" + siteDescription +
				"' and customer_id = '" + custID + "' and contact_ID = '" + contID + "';";
		
		Statement stmt = null;
		ResultSet rs = null;
				
		connObj = DatabaseWriter.getDBConnection();
						
		try {
			stmt = connObj.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				idNumber = rs.getString(1);
			}
		}											
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
							
		DatabaseWriter.closeConnection(connObj);
		
		return idNumber;
	}
	
	/**
	 * This method executes an SQL query to read JobSite records, creates JobSite type objects using
	 * this information and adds these objects to an ArrayList.  This method also uses a boolean and 
	 * a String variable to allow for only JobSite records associated with a specific customer to 
	 * be read from the database.  In addition, it uses another boolean to allow for a message to
	 * be added to the ArrayList for used in a customized display in the Combo Box.
	 * @param cID				String variable to identify a specific Customer.
	 * @param isOrderByCustomerSelected
	 * 							Boolean variable to allow for reading either specific records
	 * 							associated with a specific Customer, or all records.
	 * @param addMessage		Boolean variable used to identify whether a message should be added
	 * 							to the ArrayList.
	 */
	public ArrayList<JobSite> getJobSites(String cID, boolean isOrderByCustomerSelected, boolean addMessage) {
		
		jobSites =  new ArrayList<JobSite>();
		String query = null;
		String siteID = null;
		String addressID = null;
		String customerID = null;
		String contactID = null;
		String siteName = null;
		String streetAddress = null;
		String city = null;
		String state = null;
		String zipCode = null;
		String unitNumber = null;
		String customerLastName = null;
		String customerFirstName = null;
		String contactLastName = null;
		String contactFirstName = null;
		String title = "Job Site";
	
		//selects one of two query versions, one is associated with a particular Customer,
		//the other is all JobSites.
		query = readHelper.selectJobSiteQuery(isOrderByCustomerSelected, cID);
	
		Statement stmt = null;
							
		connObj = DatabaseWriter.getDBConnection();
							
		try {	
			stmt = connObj.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				siteID = rs.getString(1);
				addressID = rs.getString(2);
				customerID = rs.getString(3);
				contactID = rs.getString(4);
				siteName = rs.getString(5);
				streetAddress = rs.getString(6);
				city = rs.getString(7);
				state = rs.getString(8);
				zipCode = rs.getString(9);
				unitNumber = rs.getString(10);
				customerLastName = rs.getString(11);
				customerFirstName = rs.getString(12);
				contactLastName = rs.getString(13);
				contactFirstName = rs.getString(14);
		
				JobSite js = new JobSite(siteID, addressID, customerID, contactID,
					siteName, streetAddress, city, state, zipCode, unitNumber,
					customerLastName, customerFirstName, contactLastName, 
					contactFirstName, title);
				jobSites.add(js);	
				}
			}
			catch (SQLException e) {
				System.out.println(e.toString());
			}
				
		DatabaseWriter.closeConnection(connObj);		
		
		//if size is 0, there are no JobSites associated with this Customer.
		if(jobSites.size() == 0) {
			jobSites = readHelper.addNoJSTitle(jobSites);
		}
		
		//Inserts a message used to add a new JobSite.
		if(addMessage) {
			jobSites = readHelper.addNewJobSiteMessage(jobSites);
		}
	
		return jobSites;
	}
	
	/**
	 * This method obtains the ID numbers for all Jobs associated with a specified WorkOrder.
	 * @param woNum				String variable used to identify the associated WorkOrder.
	 * @return lineNumbers		String ArrayList containing the ID numbers of all associated Jobs.
	 */
	public ArrayList<String> getLineNumbers(String woNum) {
		
		ArrayList<String> lineNumbers = new ArrayList<String>();
		String lineNumber = null;
		String query = null;
	
		query = "select line_number from work_order_line_items where work_order_number = '" + woNum +"';";
		
		Statement stmt = null;
		ResultSet rs = null;
	
		connObj =  DatabaseWriter.getDBConnection();
			
		try {
			stmt = connObj.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				lineNumber = rs.getString(1);
				lineNumbers.add(lineNumber);
			}
		}											
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
			
		DatabaseWriter.closeConnection(connObj);	
	
		return lineNumbers;
	}
	
	/**
	 * This method uses a String variable to identify which version of the SQL statement
	 * to execute, then executes the statement to obtain the associated ID number of
	 * the Customer or ContactPerson record.
	 * @param newChoice			String variable to identify which type of record to obtain.
	 * @param lastName			String variable to hold persons last name.
	 * @param firstName			String variable to hold persons first name.
	 * @return idNumber			String variable to hold the associated record ID number.
	 */
	public String getPersonIDNumber(String newChoice, String lastName, String firstName) {
		
		String idNumber = "";
		String query = "";
		
		if(newChoice.equalsIgnoreCase("Customer")) {
			query = "select customer_id from customer where last_name = '" + lastName + 
					"' and first_name = '" + firstName + "';";
		}
		else if(newChoice.equalsIgnoreCase("ContactPerson")) {
			query = "select contact_id from site_contact_person where last_name = '" +
					lastName + "' and first_name = '" + firstName + "';";
		}
		
		Statement stmt = null;
		ResultSet rs = null;
				
		connObj = DatabaseWriter.getDBConnection();
						
		try {
			stmt = connObj.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				idNumber = rs.getString(1);
			}
		}											
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
						
		DatabaseWriter.closeConnection(connObj);
		
		return idNumber;
	}
	
	/**
	 * This method executes an SQL statement to read WorkOrder records from the database.
	 * It uses a boolean value to determine whether to read all records, or only those 
	 * associated with a specific Customer.  It also allows for a message to be added
	 * to identify a Customer who does not have any associated WorkOrders.
	 * @param isOrderByCustomerSelected
	 * 							Boolean variable to specify whether to read all records,
	 * 							or only records associated with a specific Customer.
	 * @param cID				String variable to identify a specific Customer.
	 * @return workOrders		ArrayList of WorkOrder objects.
	 */
	public ArrayList<WorkOrder> getWorkOrders(boolean isOrderByCustomerSelected, String cID) {
	
		String query = null;
		workOrders =  new ArrayList<WorkOrder>();
		String workOrderNumber = null;
		String siteID = null;
		String customerID = null;
		String appointmentDate = null;
		String appointmentTime = null;
		String title = "Work Order";
	
		//selects the query based on the value of the boolean.
		query = readHelper.selectWorkOrderQuery(isOrderByCustomerSelected, cID);
	
		Statement stmt = null;
									
		connObj = DatabaseWriter.getDBConnection();
									
		try {	
			stmt = connObj.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				workOrderNumber = rs.getString(1);
				siteID = rs.getString(2);
				customerID = rs.getString(3);
				appointmentDate = rs.getString(4);
				appointmentTime = rs.getString(5);
				
				WorkOrder wo = new WorkOrder(workOrderNumber, siteID,
					customerID, appointmentDate, 
					appointmentTime, title);
				workOrders.add(wo);
			}
		}
		catch (SQLException e) {
			System.out.println(e.toString());
		}
					
		DatabaseWriter.closeConnection(connObj);
	
		//if size is 0, there are no WorkOrders associated with this Customer.
		if(workOrders.size() == 0) {
			workOrders = readHelper.addNoWOsForThisCustomerMessage(workOrders);
		}
	
		return workOrders;
	}

	/**
	 * This methods manages the formatting and loading of the people ArrayList with 
	 * ContactPerson objects.  It provides messages to help the user differentiate
	 * between associated ContactPersons, unused ContactPersons, and an optional
	 * message to select to add a new ContactPerson object.
	 * @param isOrderByCustomerSelected
	 * 						Boolean variable used to determine whether to read
	 * 						all records, or only those associated with a specific Customer.
	 * @param cID			String variable to identify a specific Customer.
	 * @param choice		String variable passed to other methods to identify the object type.
	 * @param addMessage	Boolean variable to identify whether to add the "add new" message.
	 * @return people		ArrayList of ContactPerson type objects.
	 */
	public ArrayList<People> manageContactPeople(boolean isOrderByCustomerSelected, String cID, String choice, boolean addMessage) {
	
		people = new ArrayList<People>();
		
		if(!isOrderByCustomerSelected) {
			people = readHelper.getContactPeople(people, isOrderByCustomerSelected, cID);
			return people;
		}
		else {
			
			//this section adds ContactPeople that are associated with the Customer.
			people = readHelper.addAssociatedTitle(people, choice);
			people = readHelper.getContactPeople(people, isOrderByCustomerSelected, cID);
			
			//if size is 1, the arrayList only contains the "Associated" title.
			if(people.size() == 1) {	
				people = readHelper.addComboBoxSpace(people, "ContactPerson");
			}

			people = addContactsWithNoJobSites(people, choice);
		
			//This adds a message used to add new ContactPeople.
			if(addMessage) {
				readHelper.addNewContactPersonMessage(people);
			}
		}
		
		return people;
	}
	
	/**
	 * This methods manages the formatting and loading of the people ArrayList with 
	 * Employee objects.  It provides messages to help the user differentiate
	 * between associated Employees, and unused Employees.
	 * @param isOrderByCustomerSelected
	 * 						Boolean variable used to determine whether to read
	 * 						all records, or only those associated with a specific Customer.
	 * @param cID			String variable to identify a specific Customer.
	 * @param choice		String variable passed to other methods to identify the object type.
	 * @param addAll		Boolean record to specify if all Employee objects should be added.
	 * @return people		ArrayList of Employee type objects.
	 */
	public ArrayList<People> manageEmployees(boolean isOrderByCustomerSelected, String cID, String choice, boolean addAll) {
			
		people = new ArrayList<People>();
			
		if(!isOrderByCustomerSelected) {
			people = readHelper.getEmployees(people, isOrderByCustomerSelected, cID);
			return people;
		}
		else {
			if(isOrderByCustomerSelected) { 
				
				//This section adds Employees associated with a particular Customer.
				people = readHelper.addAssociatedTitle(people, choice);
				people = readHelper.getEmployees(people, isOrderByCustomerSelected, cID);
			}
			
			//if size is 1, the arrayList only contains the "Associated" title.
			if(people.size() == 1) {
				people = readHelper.addComboBoxSpace(people, choice);
			}
			
			if(!addAll) {
				addEmployeesWithNoJobs(people, choice);	
			}
			else {
				
				//this section adds all Employees in the database.
				people = readHelper.addAllEmployeesMessage(people);
				people = readHelper.getEmployees(people, false, null);
			}
		}
			
		return people;
	}	
}