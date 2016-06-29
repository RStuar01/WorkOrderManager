package BusinessLayer;

/**
 * Class Name:					WorkOrder
 * Description:					This class provides fields specific to WorkOrders,
 * 								along with get/set accessors/mutators and a toString method.
 * @author Richard Stuart
 * @created Sunday, 5,29,16
 */
public class WorkOrder {

		//WorkOrder Fields
		private String workOrderNumber;
		private String siteID;
		private String customerID;
		private String appointmentDate;
		private String appointmentTime;
		private String title;
		
		//Default Constructor
		public WorkOrder() {
			this("", "", "", "", "", "");
		}
		
		public WorkOrder(String workOrderNumber, String siteID, String customerID,
				String appointmentDate, String appointmentTime, String title) {
			this.workOrderNumber = workOrderNumber;
			this.siteID = siteID;
			this.customerID = customerID;
			this.appointmentDate = appointmentDate;
			this.appointmentTime = appointmentTime;
			this.title = title;
		}
		
		//Get and Set Accessors and Mutators
		public void setWorkOrderNumber(String workOrderNumber) {
			this.workOrderNumber = workOrderNumber;
		}
		
		public String getWorkOrderNumber() {
			return workOrderNumber;
		}
		
		public void setSiteID(String siteID) {
			this.siteID = siteID;
		}
		
		public String getSiteID() {
			return siteID;
		}
		
		public void setCustomerID(String customerID) {
			this.customerID = customerID;
		}
		
		public String getCustomerID() {
			return customerID;
		}
		
		public void setAppointmentDate(String appointmentDate) {
			this.appointmentDate = appointmentDate;
		}
		
		public String getAppointmentDate() {
			return appointmentDate;
		}
		
		public void setAppointmentTime(String appointmentTime) {
			this.appointmentTime = appointmentTime;
		}
		
		public String getAppointmentTime() {
			return appointmentTime;
		}
		
		public void setTitle(String title) {
			this.title = title;
		}
		
		public String getTitle() {
			return title;
		}
		
		public String toString() {
			String woCode = null;
			
			if(this.getTitle().equalsIgnoreCase("There are No WorkOrders for this Customer!")) {
				return this.getTitle();
			}
			else {
				woCode = appointmentDate + "-" + appointmentTime + "-" + workOrderNumber +
					"-" + customerID + "-" + siteID;
			}
			
			return woCode;
		}
}
