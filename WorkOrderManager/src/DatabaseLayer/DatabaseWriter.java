package DatabaseLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import BusinessLayer.*;

/**
 * Class Name:		DatabaseWriter
 * Description:		This class contains the methods called using data access objects to Write information
 * 					to the database, and also methods which call these methods directly.
 * @author Richard Stuart
 * @created Sunday, 5,29,16
 */
public class DatabaseWriter implements WriterDAO {
	
	private Connection connObj = null;
	private WriteHelper writerHelper = null;
	
	/**
	 * This is the Constructor that is called from the DAOFactory class.
	 * This method also calls the WriteHelper Constructor to provide an instance of that class.
	 */
	public DatabaseWriter() {
		
		writerHelper = new WriteHelper();
		connObj = getDBConnection();
		//getNumAppts10Days(connObj);
		closeConnection(connObj);
	}
	
	/**
	 * This method closes the database connection.
	 * @param connObj				The connection object.
	 */
	static void closeConnection(Connection connObj) {
		
		if(connObj != null) {
			try {
				connObj.close();
			}
			catch (SQLException ignore) {
			}
		}
	}
	
	/**
	 * This method obtains a connection to the database.
	 * @return connection			The database connection object.
	 */
	static Connection getDBConnection() {
		
		Connection connection = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		String url = "jdbc:mysql://localhost:3306/mydb";
		String username = "username";
		String password = "password";
		
		try {
			connection = DriverManager.getConnection(url, username, password);
		}
		catch (SQLException e) {
			System.out.println(e.toString());
		}
		
		//System.out.println("Connection Established!");
		return connection;
	}

	/**
	 * This methods retrieves Job information from an ArrayList and calls the required method
	 * to write the edited information to the database.
	 * @param woNum					String variable to specify the WorkOrder associated with the Jobs.
	 * @param jobs					ArrayList holding the Job objects to be written to the database.
	 */
	public void manageEditedJobsInformation(String woNum, ArrayList<Job> jobs) {
		
		String lineNumber = null;
		String employeeID = null;
		String jobDescription = null;
		String specialNotes = null;
		String jobStatus = null;
		String jobDate = null;
		String jobTime = null;
		
		for(int i = 0; i < jobs.size() - 1; i++) {
			Job j = (Job) jobs.get(i);
			lineNumber = j.getLineNumber();
			employeeID = j.getemployeeID();
			jobDescription = j.getJobType();
			specialNotes = j.getSpecialNote();
			jobStatus = j.getJobStatus();
			jobDate = j.getAppointmentDate();
			jobTime = j.getAppointmentTime();
			
			writerHelper.writeEditedJobInformation(lineNumber, woNum,  employeeID, jobDescription, specialNotes,
					jobStatus, jobDate, jobTime);
		}
	}
	
	/**
	 * This method manages the information and methods required to edit existing JobSite information
	 * in the database.
	 * @param siteID				The ID number for the JobSite.
	 * @param addressID				The ID number for the address associated with this JobSite.
	 * @param contID				The ID number for the ContactPerson associated with this JobSite.
	 * @param siteDescription		String variable for the JobSite description.
	 * @param streetAddress			String variable for the associated street address.
	 * @param city					String variable for the associated city.
	 * @param state					String variable for the associated state.
	 * @param zipCode				String variable for the associated zip code.
	 * @param unitNumber			String variable for the associated unit number.
	 */
	public void manageEditingJobSiteInformation(String siteID, String addressID,
			String contID, String siteDescription, String streetAddress, String city,
			String state, String zipCode, String unitNumber) {
		
		String choice = "JobSite";
		
		if(unitNumber.equals("NA") | unitNumber.length() == 0 | unitNumber.equals("NULL")) {
			unitNumber = "NULL";	
		}
		
		writerHelper.writeEditedAddressInformation(addressID, choice, streetAddress, city, state, zipCode, unitNumber, siteDescription);
		writerHelper.writeEditedJobSiteInformation(siteID, contID, siteDescription);		
	}
	
	/**
	 * This method manages the information and function calling to edit People information in the database.
	 * @param choice				String variable used to identify Person type being edited.
	 * @param p						People object containing the associated information.
	 * @param lastName				String variable for Persons' last name.
	 * @param firstName				String variable for Persons' first name.
	 * @param streetAddress			String variable for Persons street address.
	 * @param city					String variable for Persons city.
	 * @param state					String variable for Persons state.
	 * @param zipCode				String variable for Persons zip code.
	 * @param unitNumber			String variable for Persons unit number.
	 * @param siteDescription		String variable for Address description.
	 * @param phoneNumber			String variable for Persons phone number.
	 * @param cellPhone				String variable for Persons cell phone number.
	 * @param emailAddress			String variable for Persons email address.
	 */
	public void manageEditingPeopleInformation(String choice, People p, String lastName, String firstName,
			String streetAddress, String city, String state, String zipCode, String unitNumber,
			String siteDescription, String phoneNumber, String cellPhone, String emailAddress) {
		
		String customerID = null;
		String contactID = null;
		String employeeID = null;
		String addressID = null;
		String contactInfoID = null;
		
		if(choice.equalsIgnoreCase("Customer")) {
			Customer c = (Customer) p;
			if(c != null) {
				customerID = c.getCustomerID();
				addressID = c.getAddressID();
				contactInfoID = c.getContactInfoID();
				
				writerHelper.writeEditedCustomerInformation(customerID, lastName, firstName);
			}
		}
		else if(choice.equalsIgnoreCase("ContactPerson")) {
			ContactPerson cp = (ContactPerson) p;
			if(cp != null) {
				contactID = cp.getContactID();
				addressID = cp.getAddressID();
				contactInfoID = cp.getContactInfoID();
				
				writerHelper.writeEditedContactPersonInformation(contactID, lastName, firstName);
			}
		}
		else if(choice.equalsIgnoreCase("Employee")) {
			Employee e = (Employee) p;
			if(e != null) {
				employeeID = e.getEmployeeID();
				addressID = e.getAddressID();
				contactInfoID = e.getContactInfoID();
				
				writerHelper.writeEditedEmployeeInformation(employeeID, lastName, firstName);
			}
		}
		
		writerHelper.writeEditedAddressInformation(addressID, choice, streetAddress, city, state, zipCode, unitNumber, siteDescription);
		writerHelper.writeEditedContactInformation(contactInfoID, phoneNumber, cellPhone, emailAddress);
	}
	
	/**
	 * This method directs the information and method calling necessary to create a new JobSite in the database.
	 * @param choice				String variable used by called methods to identify the object type the 
	 * 									address information corresponds to.
	 * @param stAddress				String variable for JobSite street address.
	 * @param city					String variable for JobSite city.
	 * @param state					String variable for JobSite state.
	 * @param zipCode				String variable for JobSite zipCode.
	 * @param unitNumber			String variable for JobSite unit number.
	 * @param siteDescription		String variable for JobSite description.
	 * @param custID				String variable for Customer ID number.
	 * @param contID				String variable for Contact Person ID number.
	 */
	public void manageJobSiteCreation(String choice, String stAddress, String city, String state, String zipCode,
			String unitNumber, String siteDescription, String custID, String contID) {
		
		String newAddID = null;
		
		writerHelper.writeAddressInformation(stAddress, city, state, zipCode, choice, unitNumber, siteDescription);
		newAddID = writerHelper.obtainNewAddressID(stAddress, city, state, zipCode, choice, unitNumber, siteDescription);
		writerHelper.writeJobSite(newAddID, custID, contID, siteDescription);
	}
	
	/**
	 * This method manages the information and method calling necessary to create a new Person object
	 * in the database.
	 * @param choice				String variable used to determine what method to call for writing
	 * 									address Information, and also used by called methods.
	 * @param lastName				String variable for Persons last name.
	 * @param firstName				String variable for Persons first name.
	 * @param stAddress				String variable for Persons street address.
	 * @param city					String variable for Persons city.
	 * @param state					String variable for Persons state.
	 * @param zipCode				String variable for Persons zipCode.
	 * @param unitNumber			String variable for Persons unit number.
	 * @param siteType				String variable for Persons residence description.
	 * @param phoneNumber			String variable for Persons phone number.
	 * @param cellPhone				String variable for Persons cell phone number.
	 * @param emailAddress			String variable for Persons email address.
	 */
	public void manageNewPersonCreation(String choice, String lastName, String firstName,
			String stAddress, String city, String state, String zipCode, String unitNumber,
			String siteType, String phoneNumber, String cellPhone, String emailAddress) {
		
		String addressID = null;
		String contactInfoID = null;
		
		writerHelper.writeAddressInformation(stAddress, city, state, zipCode, choice, unitNumber, siteType);
		addressID = writerHelper.obtainNewAddressID(stAddress, city, state, zipCode, choice, unitNumber, siteType);
		writerHelper.writeContactInformation(phoneNumber, cellPhone, emailAddress);
		contactInfoID = writerHelper.obtainNewContactInformationID(phoneNumber, cellPhone, emailAddress);
		
		if(choice == "Customer") {
			writerHelper.writeCustomerInformation(addressID, contactInfoID, lastName, firstName);
		}
		else if(choice == "ContactPerson") {
			writerHelper.writeContactPersonInformation(addressID, contactInfoID, lastName, firstName);
		}
		else if(choice == "Employee") {
			writerHelper.writeEmployeeInformation(addressID, contactInfoID, lastName, firstName);
		}
	}
	
	/**
	 * This method manages the information and method calling to create a new Work Order object,
	 * and also calls the method to create new Jobs for this work order.
	 * @param newWorkOrder			Work Order object containing information to write to the database.
	 * @param jobsToWrite			ArrayList containing jobs associated with this work order.
	 */
	public void manageWorkOrderCreation(WorkOrder newWorkOrder, ArrayList<Job> jobsToWrite) {
		
		String workOrderNumber = null;
		String siteID = null;
		String custID = null;
		String creationDate = null;
		String appointmentDate = null;
		String appointmentTime = null;
		
		custID = newWorkOrder.getCustomerID();
		siteID = newWorkOrder.getSiteID();
		
		//The following three items are used in the database, but have not yet been implemented in the application.
		//These will be used to notify the user of work orders that need attention in upcoming days.
		creationDate = "2016-07-01";
		appointmentDate = "2016-07-10";
		appointmentTime = "14:00:00";
		
		writerHelper.writeWorkOrder(siteID, custID, creationDate, appointmentDate, appointmentTime);
		workOrderNumber = writerHelper.obtainWONum(siteID, custID, creationDate, appointmentDate, appointmentTime);
		
		for(Job j: jobsToWrite) {
			writeNewJob(j, workOrderNumber);
		}
	}
	
	/**
	 * This method writes the information for a new Job record to the database.
	 * @param j						Job object containing the information for a new Job.
	 * @param workOrderNumber		String variable for the work order ID number associated with this job.
	 */
	public void writeNewJob(Job j, String workOrderNumber) {
		
		String newJobUpdate = null;
		String employeeID = j.getemployeeID();
		String jobType = j.getJobType();
		String specialNotes = j.getSpecialNote();
		String jobStatus = "Active";	//allways active when writing a NEW job
		String appointmentDate = j.getAppointmentDate();
		String appointmentTime = j.getAppointmentTime();
		
		newJobUpdate = "insert into work_order_line_items " +
				"(line_number, work_order_number, employee_id, job_type, special_notes, job_status, appointment_date, appointment_time) " +
				"Values (default, '" + workOrderNumber + "', '" + employeeID + "', '" + jobType + "', '" + specialNotes + 
				"', '" + jobStatus + "', '" + appointmentDate + "', '" + appointmentTime + "');";
		
		Statement stmt = null;
											
		connObj = DatabaseWriter.getDBConnection();
				
		try {
			stmt = connObj.createStatement();
			stmt.executeUpdate(newJobUpdate);
		}											
		catch (SQLException e) {					
			System.out.println(e.toString());
		}
				
		DatabaseWriter.closeConnection(connObj);	
	}
}
