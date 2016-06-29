package DatabaseLayer;

import java.util.ArrayList;

import BusinessLayer.*;

/**
 * Interface Name:			DataReader
 * Description:				This interface defines the methods which are employed by the DatabaseReader class.
 * @author Richard Stuart
 * @created Sunday, 5,29,16
 */
public interface DataReader {
	
	ArrayList<People> addContactsWithNoJobSites(ArrayList<People> people, String choice);
	
	ArrayList<People> addEmployeesWithNoJobs(ArrayList<People> people, String choice);
	
	ArrayList<Job> addNewJobMessage(ArrayList<Job> jobs);
	
	ContactPerson getCurrentContactPerson(String contactID);
	
	Customer getCurrentCustomer(String customerID);
	
	JobSite getCurrentJobSite(String siteID);
	
	ArrayList<People> getCustomers(boolean addCustMessage);
	
	ArrayList<Job> getJobs(String woNum);
	
	String getJobSiteIDNumber(String streetAddress, String city, String state, String zipCode, String siteDescription,
					String custID, String contID);
	
	ArrayList<JobSite> getJobSites(String customerID, boolean isOrderByCustomerSelected, boolean addMessage);
	
	ArrayList<String> getLineNumbers(String woNum);
	
	String getPersonIDNumber(String newChoice, String lastName, String firstName);
	
	ArrayList<WorkOrder> getWorkOrders(boolean isOrderByCustomerSelected, String cID);
	
	ArrayList<People> manageContactPeople(boolean isOrderByCustomerSelected, String cID, String choice, boolean addMessage);
	
	ArrayList<People> manageEmployees(boolean isOrderByCustomerSelected, String customerID, String choice, boolean addAll);
}
