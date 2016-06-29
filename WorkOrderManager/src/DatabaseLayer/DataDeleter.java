package DatabaseLayer;

import BusinessLayer.People;

/**
 * Interface Name:			DataDeleter
 * Description:				This interface defines the methods which are employed by the DatabaseDeleter class.
 * @author Richard Stuart
 * @created Sunday, 5,29,16
 */
public interface DataDeleter {

	public void deletePeopleData(String choice, People p);
	
	public void deleteJobSiteData(String siteID);
	
	public void deleteWorkOrder(String woNum);
	
	public void deleteJob(String lineNumber);
}
