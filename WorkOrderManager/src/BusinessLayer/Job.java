package BusinessLayer;

/**
 * Class Name:					Job
 * Description:					This class provides fields specific to Jobs,
 * 								along with get/set accessors/mutators and a toString method.
 * @author Richard Stuart
 * @created Sunday, 5,29,16
 */
public class Job {

	private String lineNumber;
	private String employeeID;
	private String jobType;
	private String specialNote;
	private String jobStatus;
	private String appointmentDate;
	private String appointmentTime;
	private String lastName;
	private String firstName;
	private String title;
	
	
	//Default Constructor
	public Job() {
		this("", "", "", "", "", "", "", "", "", "");
	}
	
	//Overloaded Constructor
	public Job(String lineNumber, String employeeID, String jobType,
				String specialNote, String jobStatus, String appointmentDate,
				String appointmentTime, String lastName, String firstName, String title) {
		this.lineNumber = lineNumber;
		this.employeeID = employeeID;
		this.jobType = jobType;
		this.specialNote = specialNote;
		this.jobStatus = jobStatus;
		this.appointmentDate = appointmentDate;
		this.appointmentTime = appointmentTime;
		this.lastName = lastName;
		this.firstName = firstName;
		this.title = title;
	}
	
	//Get and Set Accessors and Mutators
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	public String getLineNumber() {
		return lineNumber;
	}
	
	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}
	
	public String getemployeeID() {
		return employeeID;
	}
	
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	
	public String getJobType() {
		return jobType;
	}
	
	public void setSpecialNote(String specialNote) {
		this.specialNote = specialNote;
	}
	
	public String getSpecialNote() {
		return specialNote;
	}
	
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	
	public String getJobStatus() {
		return jobStatus;
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
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	@Override
	public String toString() {
		
		if(title.equalsIgnoreCase("Job")) {
			return "job " + lineNumber + " " + jobType + "\n";
		}
		else {
			return title;
		}
	}
}
