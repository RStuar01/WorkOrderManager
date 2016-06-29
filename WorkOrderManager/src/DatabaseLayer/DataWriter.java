package DatabaseLayer;

import java.util.ArrayList;

import BusinessLayer.Job;
import BusinessLayer.People;
import BusinessLayer.WorkOrder;

/**
 * Interface Name:	DataWriter
 * Description:		This interface defines the methods which are employed by the DatabaseWriter class.
 * @author Richard Stuart
 * @created Sunday, 5,29,16
 */
public interface DataWriter {

	public void manageEditedJobsInformation(String woNum, ArrayList<Job> jobs);
	
	public void manageEditingJobSiteInformation(String siteID, String addressID,
			String contID, String siteDescription, String streetAddress, String city,
			String state, String zipCode, String unitNumber);
	
	public void manageEditingPeopleInformation(String choice, People p, String lastName, String firstName,
			String streetAddress, String city, String state, String zipCode, String unitNumber,
			String siteDescription, String phoneNumber, String cellPhone, String emailAddress);
	
	public void manageJobSiteCreation(String choice, String stAddress, String city, String state, String zipCode,
			String unitNumber, String siteDescriptionName, String custID, String contID);
	
	public void manageNewPersonCreation(String choice, String lastName, String firstName,
			String stAddress, String city, String state, String zipCode, String unitNumber,
			String siteType, String phoneNumber, String cellPhone, String emailAddress);
	
	public void manageWorkOrderCreation(WorkOrder newWorkOrder, ArrayList<Job> jobsToWrite);
	
	public void writeNewJob(Job j, String workOrderNumber);
}
