package PresentationLayer;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JDialog;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import BusinessLayer.Customer;
import BusinessLayer.Employee;
import BusinessLayer.Job;
import BusinessLayer.JobSite;
import BusinessLayer.People;
import BusinessLayer.WorkOrder;
import DatabaseLayer.DAOFactory;
import DatabaseLayer.DeleterDAO;
import DatabaseLayer.WriterDAO;
import DatabaseLayer.ReaderDAO;

import java.util.ArrayList;

/**
 * Class Name:					WorkOrderManagerDialog
 * Description:					This class contains all the code and methods necessary to 
 * 								manage the WorkOrderManagerDialog user interface for creating,
 * 								editing, and deleting WorkOrders, and setting all the associated
 * 								information for these WorkOrders in the database.
 * @author Richard Stuart
 * @created Sunday, 5,29,16
 */
@SuppressWarnings("serial")
public class WorkOrderManagerDialog extends JDialog {
	
	public JDialog workOrderManagerDialogFrame;
	private WorkOrderManagerDialog workOrderManagerDialog = null;
	private AddNewPeopleDialog addNewPeopleDialog = null;
	@SuppressWarnings("unused")
	private JobSiteManagerDialog jobSiteManagerDialog = null;
	private JTextField workOrderNumberTextField;
	private JTextField customerLastNameTextField;
	private JTextField customerFirstNameTextField;
	private JTextField jobSiteTextField;
	private JTextField contactLastNameTextField;
	private JTextField contactFirstNameTextField;
	private JTextField jobDescriptionTextField;
	private JTextField jobDateTextField;
	private JTextField jobTimeTextField;
	private JTextField employeeTextField;
	private JComboBox<People> choseCustomerComboBox;
	private JComboBox<JobSite> choseJobSiteComboBox;
	private JComboBox<People> selectEmployeeComboBox;
	private JComboBox<Job> jobsComboBox;
	private JCheckBox jobCompletedCheckBox;
	private JCheckBox cancelJobCheckBox;
	private JButton newButton;
	private JButton editButton;
	private JButton deleteButton;
	private JButton acceptButton;
	private JButton cancelButton;
	private JButton addThisJobButton;
	private JTextArea specialNotesTextArea;
	private JLabel verificationLabel;
	private JLabel choseCustomerLabel;
	private JLabel orLabel;
	private JLabel choseJobSiteLabel;
	private JLabel jobDateLabel;
	private JLabel selectEmployeeLabel;
	
	
	private ArrayList<Job> jobs = null;						
	private ArrayList<Job> jobsToWrite = null;				
	private ArrayList<People> people = null;	
	private ArrayList<JobSite> jobSites = null;
	
	private WorkOrder currentWO = null;				
	private WorkOrder newWorkOrder = null;
	
	private WriterDAO writerDAO;
	private ReaderDAO readerDAO;
	private DeleterDAO deleterDAO;
	
	private Boolean filling = false;
	private Boolean dataEntered = true;
	private Boolean calledToDisplay = false;
	private Boolean isCustomerSelected = false;
	private Boolean jobSiteManagerCalled = false;
	private Boolean addMessage = true;
	
	private static String custIDNumber;
	private static String employeeIDNumber;
	private static String employeeLastName;
	private static String employeeFirstName;
	private static String contIDNumber;
	private static String newSiteID;
	private String jobStatusValue = "";
	private String dataManipulationMode = "";
	
	//Get/Set Accessors/Mutators
	public static void setCustIDNumber(String number) {
		custIDNumber = number;
	}
	
	public static String getCustIDNumber() {
		return custIDNumber;
	}
	
	public static void setContIDNumber(String number) {
		contIDNumber = number;
	}
	
	public static String getContIDNumber() {
		return contIDNumber;
	}
	
	public static void setNewSiteID(String number) {
		newSiteID = number;
	}
	
	public static String getNewSiteID() {
		return newSiteID;
	}
	
	public static void setEmployeeIDNumber(String number) {
		employeeIDNumber = number;
	}
	
	public static String getEmployeeIDNumber() {
		return employeeIDNumber;
	}
	
	public static void setEmployeeLastName(String lastName) {
		employeeLastName = lastName;
	}
	
	public static String getEmployeeLastName() {
		return employeeLastName;
	}
	
	public static void setEmployeeFirstName(String firstName) {
		employeeFirstName = firstName;
	}
	
	public static String getEmployeeFirstName() {
		return employeeFirstName;
	}
	
	public void setJobStatusValue(String status) {
		jobStatusValue = status;
	}
	
	public String getJobStatusValue() {
		return jobStatusValue;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public WorkOrderManagerDialog(Window peopleManagerFrame, WorkOrder workOrder, String dataMode) {
		
		currentWO = workOrder;
		dataManipulationMode = dataMode;
		
		workOrderManagerDialogFrame = new JDialog(peopleManagerFrame, "Work Order Manager", Dialog.ModalityType.APPLICATION_MODAL);
		workOrderManagerDialogFrame.setTitle("Work Order Manager Dialog");
		
		initialize();
		writerDAO = DAOFactory.getWriterDAO();
		readerDAO = DAOFactory.getReaderDAO();
		deleterDAO = DAOFactory.getDeleterDAO();
		resetStaticFieldValues();
		
		if(dataManipulationMode.equals("Display")) {
			calledToDisplay = true;				
			manageDataDisplay(currentWO);
		}
		
		if(dataManipulationMode.equalsIgnoreCase("NEW")) {
			setNewMode();
			newWorkOrder = createNewWorkOrder();	
		}											
	}
	
	/**
	 * This method manages the process of adding a new Job to a workOrder by calling the necessary methods
	 * to create the Job, and to manage the associated components. 
	 * @param employeeID						String variable containing Employee record ID number.
	 * @param jobDescription					String variable containing Job description.
	 * @param specialNotes						String variable containing Job special notes.
	 * @param jobStatus							String variable containing Job status.
	 * @param jobDate							String variable containing date Job is to be completed.
	 * @param jobTime							String variable containing Job appointment time.
	 * @param employeeLastName					String variable containing Employees' last name.
	 * @param employeeFirstName					String variable containing Employees' first name.
	 * @param title								String variable containing object type.
	 */
	private void addNewJob(String employeeID, String jobDescription, String specialNotes, String jobStatus,
			String jobDate, String jobTime, String employeeLastName, String employeeFirstName, String title) {
			
		Job newJob = new Job(null, employeeID, jobDescription, specialNotes,  jobStatus,
				jobDate, jobTime, employeeLastName, employeeFirstName, title);
		jobs.add(newJob);
		jobsToWrite.add(newJob);
		jobsComboBox.setEnabled(true);
		fillComboBoxWithJobs(jobs);
		dataEntered = true;
	}
	
	/**
	 * This method sets the text field value to an empty string and the color to black when the
	 * focus listener is fired.
	 * @param name								JTextField name to receive focus.
	 */
	private void applyTextFieldFocus(JTextField name) {
		name.setText("");
		name.setForeground(Color.black);
	}
	
	/**
	 * Remove all Data from Text Fields.
	 */
	private void clearAllDataFields() {
		workOrderNumberTextField.setText("");
		jobSiteTextField.setText("");
		customerLastNameTextField.setText("");
		customerFirstNameTextField.setText("");
		contactLastNameTextField.setText("");
		contactFirstNameTextField.setText("");
		employeeTextField.setText("");
		jobsComboBox.removeAllItems();				
		clearJobDataFields();
	}
	
	/**
	 * This method clears all data from fields associated with Job objects.
	 */
	private void clearJobDataFields() {
		jobDescriptionTextField.setText("");
		jobDateTextField.setText("");
		jobTimeTextField.setText("");
		specialNotesTextArea.setText("");
	}
	
	/**
	 * This method creates an returns a new blank WorkOrder object.
	 * @return newWorkOrder						WorkOrder object created.
	 */
	private WorkOrder createNewWorkOrder() {			
		newWorkOrder = new WorkOrder();
		newWorkOrder.setTitle("WorkOrder");
		return newWorkOrder;
	}
	
	/**
	 * This method determines the status of the "Complete" or "Cancel" Job check boxes and calls
	 * the set mutator to set the value of the jobStatusValue variable. 
	 * @return jobStatusValue					String Field value associated with the Job status.
	 */
	private String determineCheckBoxStatus() {
		
		if(jobCompletedCheckBox.isSelected()) {
			setJobStatusValue("Completed");
		}
		else if(cancelJobCheckBox.isSelected()) {		
			setJobStatusValue("Canceled");
		}
		else {
			setJobStatusValue("Active");
		}
		
		return jobStatusValue;
	}
	
	/**
	 * Disable the Accept and Cancel Buttons.
	 */
	private void disableACButtons() {
		acceptButton.setEnabled(false);
		acceptButton.setVisible(false);
		cancelButton.setEnabled(false);
		cancelButton.setVisible(false);
		verificationLabel.setVisible(false);
	}
	
	/**
	 * This method disables components associated with Customer selection.
	 */
	private void disableCustomerComponents() {
		choseCustomerLabel.setVisible(false);
		choseCustomerComboBox.setEnabled(false);
		choseCustomerComboBox.setVisible(false);
	}
	
	/**
	 * This method disables components associated with JobSite selection.
	 */
	private void disableJobSiteComponents() {
		choseJobSiteLabel.setVisible(false);	
		choseJobSiteComboBox.setEnabled(false);	
		choseJobSiteComboBox.setVisible(false);
	}
	
	/**
	 * Disable the New, Edit, and Delete Buttons.
	 */
	private void disableNEDButtons() {
		newButton.setEnabled(false);
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}
	
	/**
	 * This method displays the workOrder number and manages the display of messages.
	 * @param cJS
	 * @param workOrderNumber
	 */
	private void displayData(JobSite cJS, String workOrderNumber) {			
		workOrderNumberTextField.setForeground(Color.red);
		workOrderNumberTextField.setText(workOrderNumber);
		showJobSiteData(cJS);
		setSpecialNote(Color.RED, "Select a Job to view any Special Notes!");
	}
	
	/**
	 * Display the Job Data.
	 * @param j									Job object containing the data to be displayed.
	 */
	private void displayJobData(Job j) {
		jobDescriptionTextField.setText(j.getJobType());
		employeeTextField.setText(j.getFirstName() + " " + j.getLastName());
		if(j.getJobStatus().equalsIgnoreCase("Active")) {
			jobDateTextField.setText(j.getAppointmentDate());
			jobTimeTextField.setText(j.getAppointmentTime());
		}
		else {
			jobDateTextField.setText("Job " + j.getJobStatus());		
			jobTimeTextField.setText("Job " + j.getJobStatus());
		}
		setSpecialNote(Color.BLACK, j.getSpecialNote());
	}
	
	/**
	 * This method manages the components and methods needed to Edit an existing Job object.
	 * @param j									Job object to be edited.
	 */
	private void editExistingJobData(Job j) {
		
		displayJobData(j);
		manageJobComponentsToAddNewJob();
		setVerificationLabel(Color.RED, "Accept These Changes?");
		jobCompletedCheckBox.setEnabled(true);
		jobCompletedCheckBox.setVisible(true);
		cancelJobCheckBox.setEnabled(true);
		cancelJobCheckBox.setVisible(true);
		if(jobCompletedCheckBox.isSelected()) {
			jobCompletedCheckBox.doClick();
		}
		if(cancelJobCheckBox.isSelected()) {
			cancelJobCheckBox.doClick();
		}
		addThisJobButton.setText("Apply Changes");
		String localEmployeeID = j.getemployeeID();
		setEmployeeIDNumber(localEmployeeID);
	}
	
	/**
	 * This method manages the editing of an existing Job object by calling the Job class set mutators 
	 * to set the values of the Job fields.
	 * @param currentJob						Job object representing the Job to be edited.
	 * @param jobDescription					String variable containing the Job description.
	 * @param specialNotes						String variable containing the Job special notes.
	 * @param jobStatus							String variable containing the Jobs' status.
	 * @param jobDate							String variable containing the date for Job completion.
	 * @param jobTime							String variable containing the appointment time for the Job.
	 */
	private void editJob(Job currentJob, String jobDescription, String specialNotes, String jobStatus,
			String jobDate, String jobTime) {
		
		Employee currentEmployee =  (Employee) selectEmployeeComboBox.getSelectedItem();
		if(currentEmployee != null) {
			
			//if a new employee was chosen, set the new data, otherwise use the existing employee data.
			if(currentEmployee.getTitle().equalsIgnoreCase("Employee")) {	
				currentJob.setEmployeeID(getEmployeeIDNumber());			
				currentJob.setLastName(getEmployeeLastName());
				currentJob.setFirstName(getEmployeeFirstName());
			}
		}
		
		currentJob.setJobType(jobDescription);
		currentJob.setSpecialNote(specialNotes);
		currentJob.setJobStatus(jobStatus);
		currentJob.setAppointmentDate(jobDate);
		currentJob.setAppointmentTime(jobTime);
	}
	
	/**
	 * Enable the Accept and Cancel Buttons.
	 */
	private void enableACButtons() {
		acceptButton.setEnabled(true);
		acceptButton.setVisible(true);
		cancelButton.setEnabled(true);
		cancelButton.setVisible(true);	
	}
	
	/**
	 * This method enable components used when selecting a Customer object.
	 */
	private void enableCustomerComponents() {		
		choseCustomerLabel.setVisible(true);	
		choseCustomerComboBox.setEnabled(true);	
		choseCustomerComboBox.setVisible(true);	
	}
	
	/**
	 * This method enables components associated with JobSite selection.
	 */
	private void enableJobSiteComponents() {
		choseJobSiteLabel.setVisible(true);	
		choseJobSiteComboBox.setEnabled(true);	
		choseJobSiteComboBox.setVisible(true);	
	}
	
	/**
	 * Enable the New, Edit, and Delete Buttons.
	 */
	private void enableNEDButtons() {
		newButton.setEnabled(true);
		editButton.setEnabled(true);
		deleteButton.setEnabled(true);
	}
	
	/**
	 * This method fills the Customer combo box with objects.
	 * @param people							ArrayList of People objects.
	 */
	private void fillChooseCustomerComboBox(ArrayList<People> people) {
		filling = true;
			
		choseCustomerComboBox.removeAllItems();
		for(People p: people) {
			choseCustomerComboBox.addItem(p);
		}
		filling = false;
	}
	
	/**
	 * This method fills the JobSite combo box.
	 * @param jobSites							ArrayList of JobSite objects.
	 */
	private void fillChoseJobSiteComboBox(ArrayList<JobSite> jobSites) {
		filling = true;
		
		choseJobSiteComboBox.removeAllItems();
		for(JobSite js: jobSites) {
			choseJobSiteComboBox.addItem(js);
		}
		filling = false;
	}
	
	/**
	 * This method displays the list of Jobs in the combo box for the user to view.
	 * @param jobs								ArrayList containing the Job objects.
	 */
	private void fillComboBoxWithJobs(ArrayList<Job> jobs) {		
		filling = true;
		
		jobsComboBox.removeAllItems();
		for(Job j: jobs) {
			jobsComboBox.addItem(j);
		}
		
		filling = false;
	}
	
	/**
	 * This method fills the Select Employee combo box.
	 * @param employees							ArrayList containing the Employee objects.
	 */
	private void fillSelectEmployeeComboBox(ArrayList<People> employees) {
		
		filling = true;
		selectEmployeeComboBox.removeAllItems();
		for(People e: employees) {
			selectEmployeeComboBox.addItem(e);
		}
		filling = false;
	}
	
	/**
	 * This method manages the display of information associated with a WorkOrder.
	 * @param currentWO							WorkOrder object containing the information 
	 * 											to be displayed.
	 */
	private void manageDataDisplay(WorkOrder currentWO) {
		
		String siteIDNum = currentWO.getSiteID();		
		String workOrderNumber = currentWO.toString();
		JobSite cJS = readerDAO.getCurrentJobSite(siteIDNum);
		
		displayData(cJS, workOrderNumber);
		String localWONum = currentWO.getWorkOrderNumber();
		jobs = readerDAO.getJobs(localWONum);	
		fillComboBoxWithJobs(jobs);	
	}
	
	/**
	 * This method manages the components associated with the process of adding a new Job object.
	 */
	private void manageJobComponentsToAddNewJob() {		
		jobDescriptionTextField.setEditable(true);		
		specialNotesTextArea.setEditable(true);
		jobsComboBox.setEnabled(false);
		jobDateTextField.setEditable(true);
		jobTimeTextField.setEditable(true);
		selectEmployeeLabel.setEnabled(true);
		selectEmployeeLabel.setVisible(true);
		selectEmployeeLabel.setForeground(Color.red);
		selectEmployeeComboBox.setEnabled(true);
		selectEmployeeComboBox.setVisible(true);
		disableACButtons();									
		addThisJobButton.setEnabled(true);			
		addThisJobButton.setVisible(true);
		
		if(dataManipulationMode.equalsIgnoreCase("Edit")) {
			String localCustomerID = currentWO.getCustomerID();
			setCustIDNumber(localCustomerID);
		}

		boolean addAll = true;
		boolean isCustomerSelected = true;
		System.out.println("WOMD L-512 custID: " + getCustIDNumber());
		System.out.println("WOMD L-513 isCustomerSelected: " + isCustomerSelected);
		people = readerDAO.manageEmployees(isCustomerSelected, getCustIDNumber(), "Employee", addAll);
		
		fillSelectEmployeeComboBox(people);
	}
	
	/**
	 * This method processes the information entered in text fields used for Job objects by
	 * determining the data mode is use and user choice, and calling the appropriate method
	 * to manage the Job processing.
	 */
	private void processJobData() {
		dataEntered = true;
		String jobDescription = verifyEntry(jobDescriptionTextField);
		String jobDate = verifyEntry(jobDateTextField);
		String jobTime = verifyEntry(jobTimeTextField);
		String specialNotes = specialNotesTextArea.getText();
		String jobStatus = "Active";
		String title = "Job";
		
		if(dataEntered) {
			if(dataManipulationMode.equalsIgnoreCase("New")) {
				addNewJob(getEmployeeIDNumber(), jobDescription, specialNotes, jobStatus,	
					jobDate, jobTime, getEmployeeLastName(), getEmployeeFirstName(), title);
				}
			else if(dataManipulationMode.equalsIgnoreCase("Edit")) {
				Job comboEntry = (Job) jobsComboBox.getSelectedItem();
				
				//This section is used if the user selects the "Add New Job" message in the combo box while in Edit mode.
				if(comboEntry.getTitle().equalsIgnoreCase("Add New Job")) {	
					System.out.println("Here WOMD L-594************************************************");
					addNewJob(getEmployeeIDNumber(), jobDescription, specialNotes,  jobStatus,
							jobDate, jobTime, getEmployeeLastName(), getEmployeeFirstName(), title);
				}
				//This section is used to edit an existing Job object.
				else {													
					Job currentJob = (Job) jobsComboBox.getSelectedItem();
					editJob(currentJob, jobDescription, specialNotes, getJobStatusValue(), jobDate, jobTime);
					fillComboBoxWithJobs(jobs);
				}
			}
		}
		else {
			JOptionPane.showMessageDialog(this, "Job Not Added", "Missing Data!",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	/**
	 * This method calls the set Mutators to reset the values of all static fields.
	 */
	private void resetStaticFieldValues() {
		setCustIDNumber(null);
		setEmployeeIDNumber(null);
		setEmployeeLastName(null);
		setEmployeeFirstName(null);
		setContIDNumber(null);
		setNewSiteID(null);
		setJobStatusValue(null);
	}
	
	/**
	 * This method returns components to their original state to create a new workOrder.
	 */
	private void setNewMode() {
		dataManipulationMode = "NEW";
		disableNEDButtons();
		enableCustomerComponents();
		orLabel.setVisible(true);	
		enableJobSiteComponents();
		clearAllDataFields();	
		people = readerDAO.getCustomers(addMessage);
		fillChooseCustomerComboBox(people);
		boolean isOrderByCustomerSelected = false;
		String customerID = getCustIDNumber();
		if(customerID != null) {
			isOrderByCustomerSelected = true;
		}
		jobSites = readerDAO.getJobSites(getCustIDNumber(), isOrderByCustomerSelected, addMessage);
		fillChoseJobSiteComboBox(jobSites);
		jobsToWrite = new ArrayList<Job>();
	}

	/**
	 * This method displays special notes for the user.
	 * @param color								Color to be used for the display.
	 * @param note								String variable containing a message
	 * 											for the user to view.
	 */
	private void setSpecialNote(Color color, String note) {
		specialNotesTextArea.setForeground(color);
		specialNotesTextArea.setText(note);
	}
	
	/**
	 * This method manages the use of the verification label.
	 * @param color								Color to be used on the label.
	 * @param message							String variable containing the message to be
	 * 											displayed on the label.
	 */
	private void setVerificationLabel(Color color, String message) {
		verificationLabel.setVisible(true);
		verificationLabel.setForeground(color);
		verificationLabel.setText(message);
	}
	
	/**
	 * This method displays the Customers' first and last names in the text fields.
	 * @param c									Customer object containing the information to be displayed.
	 */
	private void showCustomerData(Customer c) {
		
		if(c != null) {
			customerLastNameTextField.setText(c.getLastName());
			customerFirstNameTextField.setText(c.getFirstName());
		}
	}
	
	/**
	 * Display the JobSite data in the text fields.
	 * @param js								JobSite object containing the information to be displayed.
	 */
	private void showJobSiteData(JobSite js) {
		if(!js.getSiteName().equals("Add New Job Site")) {
			jobSiteTextField.setForeground(Color.RED);
			jobSiteTextField.setText(js.getCustomerFirstName() + " " + js.getCustomerLastName() +
					"'s " + js.getSiteName());
			customerLastNameTextField.setText(js.getCustomerLastName());
			customerFirstNameTextField.setText(js.getCustomerFirstName());
			contactLastNameTextField.setText(js.getContactLastName());
			contactFirstNameTextField.setText(js.getContactFirstName());
			disableJobSiteComponents();
		}
	}
	
	/**
	 * Verify that a text field contains Data.
	 * @param name								JTextField name to be checked for data entry.
	 * @return dataItem							String variable to hold the information from the text field.
	 */
	private String verifyEntry(JTextField name) {
		String dataItem = null;
		
		dataItem = name.getText();
		if(dataItem.length() == 0) {
			name.setForeground(Color.RED);
			name.setText("Data Missing");
			dataEntered = false;								
		}
		else if(dataItem.equals("Data Missing")) {
			dataEntered = false;
		}
		
		return dataItem;
	}
	
	private void initialize() {
		workOrderManagerDialogFrame.setBounds(100, 100, 657, 620);
		workOrderManagerDialogFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		workOrderManagerDialogFrame.getContentPane().setLayout(null);
		
		choseCustomerComboBox = new JComboBox<People>();
		choseCustomerComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!filling) {
					
					Customer c = (Customer) choseCustomerComboBox.getSelectedItem();
					isCustomerSelected = true;
					if(c != null) {
						
						//If the user selected the "Add New Customer" message in the combo box, this section is used.
						if(c.getTitle().equalsIgnoreCase("Add New Customer")) {
							
							AddNewPeopleDialog.setOwnerName("WorkOrderManagerDialog");
							addNewPeopleDialog = new AddNewPeopleDialog(workOrderManagerDialog, "Customer");
							addNewPeopleDialog.addNewPeopleDialogFrame.setVisible(true);
							
							clearAllDataFields();
							c = readerDAO.getCurrentCustomer(getCustIDNumber());
						}
						
						//If the user selected an existing Customer object, this section is used.
						else {
							String localCustomerID = c.getCustomerID();
							setCustIDNumber(localCustomerID);
						}
						
						showCustomerData(c);
						boolean isOrderByCustomerSelected = false;
						if(custIDNumber != null) {
							isOrderByCustomerSelected = true;
						}
						jobSites = readerDAO.getJobSites(getCustIDNumber(), isOrderByCustomerSelected, addMessage);
						fillChoseJobSiteComboBox(jobSites);
						disableCustomerComponents();
						orLabel.setVisible(false);
					}
				}
			}
		});
		choseCustomerComboBox.setBounds(10, 94, 161, 20);
		choseCustomerComboBox.setVisible(false);		
		workOrderManagerDialogFrame.getContentPane().add(choseCustomerComboBox);
		
		choseJobSiteComboBox = new JComboBox<JobSite>();
		choseJobSiteComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!filling) {
					
					JobSite js = (JobSite) choseJobSiteComboBox.getSelectedItem();
					if(js != null) {
						
						//The following section is used regardless of which mode the user is in, new or edit, etc.
						if(js.getTitle().equalsIgnoreCase("Add New Job Site")) {
							jobSiteManagerCalled = true;
							
							//If the user has already selected a Customer, pass the customer ID to the constructor.
							if(isCustomerSelected) {
								JobSiteManagerDialog.setOwnerName("WorkOrderManagerDialog");
								jobSiteManagerDialog = new JobSiteManagerDialog(workOrderManagerDialogFrame,
										getCustIDNumber(), dataManipulationMode);
								JobSiteManagerDialog.jobSiteManagerDialogFrame.setVisible(true);
							}
							
							//If no Customer has been chosen first, pass the JobSite object to the constructor.
							else {
								JobSiteManagerDialog.setOwnerName("WorkOrderManagerDialog");
								jobSiteManagerDialog = new JobSiteManagerDialog(workOrderManagerDialog,
										js, dataManipulationMode);	
								JobSiteManagerDialog.jobSiteManagerDialogFrame.setVisible(true);
							}
							
							js = readerDAO.getCurrentJobSite(newSiteID);
						}
						
						//The following section is used if the user chose an existing JobSite, or after the 
						//new JobSite has been added in the JobSiteManagerDialog and the dialog disposed.
						if(js != null) {	
							showJobSiteData(js);	
							disableCustomerComponents();
							orLabel.setVisible(false);
							jobs = readerDAO.addNewJobMessage(jobs);
							fillComboBoxWithJobs(jobs);						
							String localCustomerID = js.getCustomerID();
							setCustIDNumber(localCustomerID);
							String localSiteID = js.getSiteID();
							setNewSiteID(localSiteID);
						}
					}
				}
			}
		});
		choseJobSiteComboBox.setBounds(417, 94, 207, 20);
		choseJobSiteComboBox.setVisible(false);
		workOrderManagerDialogFrame.getContentPane().add(choseJobSiteComboBox);
		
		JLabel workOrderNumberLabel = new JLabel("Work Order Number:");
		workOrderNumberLabel.setBounds(104, 11, 120, 14);
		workOrderManagerDialogFrame.getContentPane().add(workOrderNumberLabel);
		
		workOrderNumberTextField = new JTextField();
		workOrderNumberTextField.setEditable(false);
		workOrderNumberTextField.setBounds(234, 8, 200, 20);
		workOrderNumberTextField.setColumns(10);
		workOrderManagerDialogFrame.getContentPane().add(workOrderNumberTextField);
		
		choseCustomerLabel = new JLabel("Chose a Customer");
		choseCustomerLabel.setBounds(10, 69, 120, 14);
		choseCustomerLabel.setVisible(false);
		choseCustomerLabel.setForeground(Color.red);
		workOrderManagerDialogFrame.getContentPane().add(choseCustomerLabel);
		
		choseJobSiteLabel = new JLabel("Chose a Job Site");
		choseJobSiteLabel.setBounds(417, 69, 120, 14);
		choseJobSiteLabel.setVisible(false);
		choseJobSiteLabel.setForeground(Color.red);
		workOrderManagerDialogFrame.getContentPane().add(choseJobSiteLabel);
		
		orLabel = new JLabel("OR");
		orLabel.setBounds(286, 69, 46, 14);
		orLabel.setVisible(false);
		orLabel.setForeground(Color.red);
		workOrderManagerDialogFrame.getContentPane().add(orLabel);
		
		JLabel customerLastNameLabel = new JLabel("Customer Last Name:");
		customerLastNameLabel.setBounds(10, 191, 138, 14);
		workOrderManagerDialogFrame.getContentPane().add(customerLastNameLabel);
		
		customerLastNameTextField = new JTextField();
		customerLastNameTextField.setEditable(false);
		customerLastNameTextField.setBounds(140, 188, 120, 20);
		customerLastNameTextField.setColumns(10);
		workOrderManagerDialogFrame.getContentPane().add(customerLastNameTextField);
		
		JLabel customerFirstNameLabel = new JLabel("Customer First Name:");
		customerFirstNameLabel.setBounds(324, 188, 138, 20);
		workOrderManagerDialogFrame.getContentPane().add(customerFirstNameLabel);
		
		customerFirstNameTextField = new JTextField();
		customerFirstNameTextField.setEditable(false);
		customerFirstNameTextField.setBounds(454, 188, 170, 20);
		customerFirstNameTextField.setColumns(10);
		workOrderManagerDialogFrame.getContentPane().add(customerFirstNameTextField);
		
		JLabel jobSiteLabel = new JLabel("Job Site:");
		jobSiteLabel.setBounds(140, 145, 67, 14);
		workOrderManagerDialogFrame.getContentPane().add(jobSiteLabel);
		
		jobSiteTextField = new JTextField();
		jobSiteTextField.setEditable(false);
		jobSiteTextField.setBounds(217, 142, 217, 20);
		jobSiteTextField.setColumns(10);
		workOrderManagerDialogFrame.getContentPane().add(jobSiteTextField);
		
		JLabel contactLastNameLabel = new JLabel("Contact Last Name:");
		contactLastNameLabel.setBounds(10, 223, 120, 14);
		workOrderManagerDialogFrame.getContentPane().add(contactLastNameLabel);
		
		JLabel contactFirstName = new JLabel("Contact First Name:");
		contactFirstName.setBounds(324, 223, 120, 14);
		workOrderManagerDialogFrame.getContentPane().add(contactFirstName);
		
		contactLastNameTextField = new JTextField();
		contactLastNameTextField.setEditable(false);
		contactLastNameTextField.setBounds(140, 220, 120, 20);
		contactLastNameTextField.setColumns(10);
		workOrderManagerDialogFrame.getContentPane().add(contactLastNameTextField);
		
		contactFirstNameTextField = new JTextField();
		contactFirstNameTextField.setEditable(false);
		contactFirstNameTextField.setBounds(454, 220, 170, 20);
		contactFirstNameTextField.setColumns(10);
		workOrderManagerDialogFrame.getContentPane().add(contactFirstNameTextField);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 252, 614, 5);
		workOrderManagerDialogFrame.getContentPane().add(separator);
		
		JLabel workOrderJobsLabel = new JLabel("Work Order Jobs");
		workOrderJobsLabel.setBounds(238, 260, 101, 14);
		workOrderJobsLabel.setForeground(Color.red);
		workOrderManagerDialogFrame.getContentPane().add(workOrderJobsLabel);
		
		jobsComboBox = new JComboBox<Job>();
		jobsComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!filling) {
					
					Job j = (Job) jobsComboBox.getSelectedItem();
					if(j != null) {	
						
						if(j.getTitle().equalsIgnoreCase("Add New Job")) {		
							manageJobComponentsToAddNewJob();
							clearJobDataFields();
							employeeTextField.setText("");
						}
						else if(dataManipulationMode.equalsIgnoreCase("Edit")) {
							if(j.getTitle().equalsIgnoreCase("job")) {
								String localStatus = j.getJobStatus();
								setJobStatusValue(localStatus);
								if(!j.getJobStatus().equalsIgnoreCase("Completed") && 
									!j.getJobStatus().equalsIgnoreCase("Canceled")) {
									editExistingJobData(j);
								}
								else {
									JOptionPane.showMessageDialog(null, "This job is " + j.getJobStatus() + 
												" and can not be edited",
											"Non Editable", JOptionPane.INFORMATION_MESSAGE);
								}
							}
						}
						else {
							displayJobData(j);		
						}
					}
				}
			}
		});
		jobsComboBox.setBounds(10, 310, 200, 20);
		workOrderManagerDialogFrame.getContentPane().add(jobsComboBox);
		
		JLabel selectJobLabel = new JLabel("Select a Job");
		selectJobLabel.setBounds(10, 285, 101, 14);
		workOrderManagerDialogFrame.getContentPane().add(selectJobLabel);
		
		JLabel jobDescriptionLabel = new JLabel("Job Description:");
		jobDescriptionLabel.setBounds(10, 341, 101, 14);
		workOrderManagerDialogFrame.getContentPane().add(jobDescriptionLabel);
		
		jobDescriptionTextField = new JTextField();
		jobDescriptionTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				applyTextFieldFocus(jobDescriptionTextField);
			}
		});
		jobDescriptionTextField.setEditable(false);
		jobDescriptionTextField.setBounds(105, 338, 200, 20);
		jobDescriptionTextField.setColumns(10);
		workOrderManagerDialogFrame.getContentPane().add(jobDescriptionTextField);
		
		jobDateLabel = new JLabel("Job Date:");
		jobDateLabel.setBounds(327, 341, 68, 14);
		workOrderManagerDialogFrame.getContentPane().add(jobDateLabel);
		
		jobDateTextField = new JTextField();
		jobDateTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				applyTextFieldFocus(jobDateTextField);
			}
		});
		jobDateTextField.setEditable(false);
		jobDateTextField.setBounds(392, 338, 86, 20);
		jobDateTextField.setColumns(10);
		workOrderManagerDialogFrame.getContentPane().add(jobDateTextField);
		
		JLabel jobTimeLabel = new JLabel("Time:");
		jobTimeLabel.setBounds(484, 341, 46, 14);
		workOrderManagerDialogFrame.getContentPane().add(jobTimeLabel);
		
		jobTimeTextField = new JTextField();
		jobTimeTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				applyTextFieldFocus(jobTimeTextField);
			}
		});
		jobTimeTextField.setEditable(false);
		jobTimeTextField.setBounds(538, 338, 86, 20);
		jobTimeTextField.setColumns(10);
		workOrderManagerDialogFrame.getContentPane().add(jobTimeTextField);
		
		specialNotesTextArea = new JTextArea();
		specialNotesTextArea.setEditable(false);
		specialNotesTextArea.setBounds(15, 393, 609, 77);
		specialNotesTextArea.setLineWrap(true);
		specialNotesTextArea.setWrapStyleWord(true);
		workOrderManagerDialogFrame.getContentPane().add(specialNotesTextArea);
		
		newButton = new JButton("New");
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setNewMode();
				createNewWorkOrder();
			}
		});
		newButton.setBounds(10, 550, 90, 20);
		workOrderManagerDialogFrame.getContentPane().add(newButton);
		
		editButton = new JButton("Edit");
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				dataManipulationMode = "Edit";
				disableNEDButtons();
				enableACButtons();
				employeeTextField.setText("");
				clearJobDataFields();
				setSpecialNote(Color.RED, "\n\nSelect a Job to Edit");
				
				if(jobCompletedCheckBox.isSelected()) {
					jobCompletedCheckBox.doClick();
				}
				if(cancelJobCheckBox.isSelected()) {
					cancelJobCheckBox.doClick();
				}
				
				jobsToWrite = new ArrayList<Job>();				
				jobs = readerDAO.addNewJobMessage(jobs);
				System.out.println("WOMD Line 1035 " + jobs);	
				fillComboBoxWithJobs(jobs);	
			}
		});
		editButton.setBounds(106, 549, 89, 23);
		workOrderManagerDialogFrame.getContentPane().add(editButton);
		
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent e) {
				
				int n = JOptionPane.showConfirmDialog(workOrderManagerDialogFrame,
						"Delete this Work Order?\nJobs can NOT be deleted.\nThey can be edited for completion/cancellation!",
						"Delete Work Order!", JOptionPane.YES_NO_OPTION);			
				
				if(n == 0) {	//Yes option chosen
					ArrayList<String> lineNumbers = new ArrayList<String>();
					String localWONum = currentWO.getWorkOrderNumber();
					lineNumbers = readerDAO.getLineNumbers(localWONum);
					for(String lineNumber: lineNumbers) {
						deleterDAO.deleteJob(lineNumber);
					}
					deleterDAO.deleteWorkOrder(localWONum);
					workOrderManagerDialogFrame.dispose();
				}
			}
		});
		deleteButton.setBounds(205, 549, 89, 23);
		workOrderManagerDialogFrame.getContentPane().add(deleteButton);
		
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				workOrderManagerDialogFrame.dispose();
			}
		});
		closeButton.setBounds(542, 549, 89, 23);
		workOrderManagerDialogFrame.getContentPane().add(closeButton);
		
		acceptButton = new JButton("Accept");
		acceptButton.setEnabled(false);
		acceptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(dataManipulationMode.equalsIgnoreCase("New")) {
					if(jobsToWrite.size() > 0) {
						if(jobSiteManagerCalled) {
							JobSite newPassedJobSite = readerDAO.getCurrentJobSite(getNewSiteID());
							String localCustomerID = newPassedJobSite.getCustomerID();
							setCustIDNumber(localCustomerID);
						}
						newWorkOrder.setCustomerID(getCustIDNumber());
						newWorkOrder.setSiteID(getNewSiteID());
						writerDAO.manageWorkOrderCreation(newWorkOrder, jobsToWrite);
						workOrderManagerDialogFrame.dispose();
					}
					else {
						JOptionPane.showMessageDialog(null, "You must enter at least 1 Job to create a work order",
								"No Jobs Added!", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else if(dataManipulationMode.equalsIgnoreCase("Edit")) {
					String localWONum = currentWO.getWorkOrderNumber();
					if(jobsToWrite.size() > 0) {		
						System.out.println("WOMD L-1105 jobsToWrite " + jobsToWrite);
						for(Job j: jobsToWrite) {
							writerDAO.writeNewJob(j, localWONum);
						}
					}
					writerDAO.manageEditedJobsInformation(localWONum, jobs);
					workOrderManagerDialogFrame.dispose();
				}
			}
		});
		acceptButton.setBounds(355, 515, 89, 23);
		workOrderManagerDialogFrame.getContentPane().add(acceptButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setEnabled(false);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(dataManipulationMode.equalsIgnoreCase("New")) {
					if(!calledToDisplay) {
						workOrderManagerDialogFrame.dispose();
					}
				}
				else if(dataManipulationMode.equalsIgnoreCase("Edit")) {
					dataManipulationMode = "Display";
				}
				manageDataDisplay(currentWO);
				enableNEDButtons();
				disableACButtons();
			}
		});
		cancelButton.setBounds(355, 549, 89, 23);
		workOrderManagerDialogFrame.getContentPane().add(cancelButton);
		
		JLabel specialNotesLabel = new JLabel("Special Notes");
		specialNotesLabel.setBounds(246, 373, 86, 14);
		workOrderManagerDialogFrame.getContentPane().add(specialNotesLabel);
		
		verificationLabel = new JLabel("");
		verificationLabel.setBounds(417, 481, 207, 20);
		verificationLabel.setVisible(false);
		workOrderManagerDialogFrame.getContentPane().add(verificationLabel);
		
		addThisJobButton = new JButton("Add This Job");
		addThisJobButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				
				String message = null;
				processJobData();
				jobsComboBox.setEnabled(true);

				if(dataManipulationMode.equalsIgnoreCase("New")) {
					setJobStatusValue("Active");
					message = "Create this Work Order?";
				}	
				else if(dataManipulationMode.equalsIgnoreCase("Edit")) {
					setSpecialNote(Color.RED, "\n\nSelect A Job to Edit!");
					message = "FinishEditing this Work Order?";
				}

				if(dataEntered) {
					clearJobDataFields();
					employeeTextField.setText("");
					addThisJobButton.setEnabled(false);
					addThisJobButton.setVisible(false);
					enableACButtons();
					jobCompletedCheckBox.setVisible(false);
					cancelJobCheckBox.setVisible(false);
					selectEmployeeComboBox.setVisible(false);
					setVerificationLabel(Color.RED, message);
				}
			}
		});
		addThisJobButton.setEnabled(false);
		addThisJobButton.setVisible(false);
		addThisJobButton.setBounds(10, 480, 138, 23);
		workOrderManagerDialogFrame.getContentPane().add(addThisJobButton);
		
		selectEmployeeComboBox = new JComboBox<People>();
		selectEmployeeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!filling) {
					
					Employee e = (Employee) selectEmployeeComboBox.getSelectedItem();
					if(e != null) {
						String localEmployeeID = e.getEmployeeID();
						setEmployeeIDNumber(localEmployeeID);
						String lastName = e.getLastName();
						setEmployeeLastName(lastName);
						String firstName = e.getFirstName();
						setEmployeeFirstName(firstName);
						selectEmployeeComboBox.setEnabled(false);
						selectEmployeeComboBox.setVisible(false);
						employeeTextField.setText(e.getFirstName() + " " + e.getLastName());
						selectEmployeeLabel.setForeground(Color.black);
						selectEmployeeLabel.setVisible(false);
						employeeTextField.setForeground(Color.black);
					}
				}
			}
		});
		selectEmployeeComboBox.setEnabled(false);
		selectEmployeeComboBox.setVisible(false);
		selectEmployeeComboBox.setBounds(349, 282, 275, 20);
		workOrderManagerDialogFrame.getContentPane().add(selectEmployeeComboBox);
		
		selectEmployeeLabel = new JLabel("Select an Employee");
		selectEmployeeLabel.setEnabled(false);
		selectEmployeeLabel.setVisible(false);
		selectEmployeeLabel.setBounds(349, 260, 138, 14);
		workOrderManagerDialogFrame.getContentPane().add(selectEmployeeLabel);
		
		employeeTextField = new JTextField();
		employeeTextField.setEditable(false);
		employeeTextField.setBounds(392, 310, 210, 20);
		employeeTextField.setColumns(10);
		workOrderManagerDialogFrame.getContentPane().add(employeeTextField);
		
		JLabel employeeLabel = new JLabel("Employee");
		employeeLabel.setBounds(324, 313, 67, 14);
		workOrderManagerDialogFrame.getContentPane().add(employeeLabel);
		
		jobCompletedCheckBox = new JCheckBox("Complete this Job?");
		jobCompletedCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {		
					if(cancelJobCheckBox.isSelected()) {
						cancelJobCheckBox.doClick();
					}
					setJobStatusValue("Completed");
				}
				else if(e.getStateChange() == ItemEvent.DESELECTED) {
					determineCheckBoxStatus();
				}
			}
		});
		jobCompletedCheckBox.setEnabled(false);
		jobCompletedCheckBox.setVisible(false);
		jobCompletedCheckBox.setBounds(174, 515, 158, 23);
		workOrderManagerDialogFrame.getContentPane().add(jobCompletedCheckBox);
		
		cancelJobCheckBox = new JCheckBox("Cancel this Job?");
		cancelJobCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {		
					if(jobCompletedCheckBox.isSelected()) {
						jobCompletedCheckBox.doClick();
					}
					setJobStatusValue("Canceled");
				}
				else if(e.getStateChange() == ItemEvent.DESELECTED) {
					determineCheckBoxStatus();
				}
			}
		});
		cancelJobCheckBox.setEnabled(false);
		cancelJobCheckBox.setVisible(false);
		cancelJobCheckBox.setBounds(15, 515, 133, 23);
		workOrderManagerDialogFrame.getContentPane().add(cancelJobCheckBox);
	}
}
