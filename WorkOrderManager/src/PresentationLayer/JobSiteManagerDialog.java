package PresentationLayer;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.JCheckBox;

import DatabaseLayer.DAOFactory;
import DatabaseLayer.DeleterDAO;
import DatabaseLayer.WriterDAO;
import DatabaseLayer.ReaderDAO;

import BusinessLayer.Customer;
import BusinessLayer.ContactPerson;
import BusinessLayer.JobSite;
import BusinessLayer.People;

import java.util.ArrayList;

/**
 * Class Name:					JobSiteManagerDialog
 * Description:					This class contains all the code and methods necessary to 
 * 								manage the JobSiteManagerDialog user interface for creating,
 * 								editing, and deleting JobSites, and setting all the associated
 * 								information for these JobSites in the database.
 * @author Richard Stuart
 * @created Sunday, 5,29,16
 */
@SuppressWarnings("serial")
public class JobSiteManagerDialog extends JDialog {

	public static JDialog jobSiteManagerDialogFrame;
	private JTextField jobSiteTextField;
	private JTextField streetAddressTextField;
	private JTextField siteDescriptionTextField;
	private JTextField cityTextField;
	private JTextField stateTextField;
	private JTextField zipCodeTextField;
	private JTextField unitNumberTextField;
	private JTextField customerLastNameTextField;
	private JTextField customerFirstNameTextField;
	private JTextField contactLastNameTextField;
	private JTextField contactFirstNameTextField;
	private JLabel choseCustomerLabel;
	private JLabel choseContactLabel;
	private JLabel verificationLabel;
	private JComboBox<People> choseCustomerComboBox;
	private JComboBox<People> choseContactComboBox;
	private JCheckBox changeContactPersonCheckBox;
	private JButton newButton;
	private JButton editButton;
	private JButton deleteButton;
	private JButton acceptButton;
	private JButton cancelButton;

	private AddNewPeopleDialog addNewPeopleDialog = null;
	private JobSite currentJS = null;	
	private WriterDAO writerDAO;
	private ReaderDAO readerDAO;
	private DeleterDAO deleterDAO;
	private String choice = "";		
	private String dataManipulationMode = "";	
	private ArrayList<People> people = null;
	private boolean filling = false;
	private boolean isOrderByCustomerSelected = true;
	private boolean isContactSelected = false;
	private boolean dataEntered = true;
	private boolean customerPassed = false;
	private boolean contactPassed = false;
	private boolean jobSitePassed = false;
	private boolean addMessage = true;		
	private boolean addCustMessage = true;
	private static String custIDNumber;	
	private static String contIDNumber;
	private static String ownerName;
	
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
	
	public static void setOwnerName(String owner) {
		ownerName = owner;
	}
	
	public static String getOwnerName() {
		return ownerName;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			} 
			catch (Exception e) {
					e.printStackTrace();
			}
	}

	/**
	 * Constructor used to call this class by passing a JobSite object.
	 * @param owner							Class which called this constructor.
	 * @param currentJS						JobSite object passed to this class.
	 * @param dataManipulationMode			String variable to define the data manipulation mode
	 * 										in use, such as: new, display, edit, or delete.
	 */
	public JobSiteManagerDialog(Window owner, JobSite currentJS, String dataManipulationMode) {
		
		this.currentJS = currentJS;
		this.dataManipulationMode = dataManipulationMode;
		
		jobSiteManagerDialogFrame = new JDialog(owner, "Job Site Manager", Dialog.ModalityType.APPLICATION_MODAL);
		jobSiteManagerDialogFrame.setTitle("Job Site Manager Dialog");
		
		initialize();
		writerDAO = DAOFactory.getWriterDAO();
		readerDAO = DAOFactory.getReaderDAO();
		deleterDAO = DAOFactory.getDeleterDAO();
		
		if(dataManipulationMode.equalsIgnoreCase("NEW")) {
			if(getOwnerName().equalsIgnoreCase("WorkOrderManagerDialog")) {
				jobSitePassed = true;
			}
			setNewMode();
		}
		
		else if(dataManipulationMode.equals("Display")) {
			displayJobSiteData(currentJS);
			disableACButtons();
		}
	}
	
	/**
	 * Constructor used when the class is called by passing a Customer record ID number.
	 * @param owner							Class which called this constructor.
	 * @param customerID					String variable to define the Customer record ID number.
	 * @param dataManipulationMode			String variable to define the data manipulation mode
	 * 										in use, such as: new, display, edit, or delete.
	 */
	public JobSiteManagerDialog(Window owner, String customerID, String dataManipulationMode) {
		setCustIDNumber(customerID);
		this.dataManipulationMode = dataManipulationMode;
		customerPassed = true;
		
		jobSiteManagerDialogFrame = new JDialog(owner, "Job Site Manager", Dialog.ModalityType.APPLICATION_MODAL);
		jobSiteManagerDialogFrame.setTitle("Job Site Manager Dialog");
		
		initialize();
		writerDAO = DAOFactory.getWriterDAO();
		readerDAO = DAOFactory.getReaderDAO();
		deleterDAO = DAOFactory.getDeleterDAO();
		
		isContactSelected = false;
		enableTextFields();
		nEDButtonChosen();
		clearAllDataFields();
		enableContactItems();
		Customer c = readerDAO.getCurrentCustomer(customerID);
		showCustomerData(c);
		fillComboBox(getCustIDNumber());
	}
	
	/**
	 * Fills the Customer Combo Box.
	 * @param p								ArrayList of People objects.
	 */
	private void fillCustomerComboBox(ArrayList<People> p) {
		filling = true;
		
		choseCustomerComboBox.removeAllItems();
		for(People person: p) {
			choseCustomerComboBox.addItem(person);
		}
		filling = false;
	}
	
	/**
	 * Fills the Contact Combo Box.
	 * @param p								ArrayList of People objects.
	 */
	private void fillContactComboBox(ArrayList<People> p) {	
		filling = true;
		
		choseContactComboBox.removeAllItems();
		for(People person: p) {
			choseContactComboBox.addItem(person);
		}
		filling = false;
	}
	
	/**
	 * Displays the job site information in the text fields.
	 * @param cjs							JobSite object containing the information
	 * 										to display.
	 */
	private void displayJobSiteData(JobSite cjs) {
		jobSiteTextField.setText(cjs.getCustomerFirstName() + " " + 
				cjs.getCustomerLastName() + "'s " + cjs.getSiteName());
		streetAddressTextField.setText(cjs.getStreetAddress());
		cityTextField.setText(cjs.getCity());
		stateTextField.setText(cjs.getState());
		zipCodeTextField.setText(cjs.getZipCode());
		if(cjs.getUnitNumber() != null) {
			unitNumberTextField.setText(cjs.getUnitNumber());
		}
		else {
			unitNumberTextField.setText("NA");
		}
		customerLastNameTextField.setText(cjs.getCustomerLastName());
		customerFirstNameTextField.setText(cjs.getCustomerFirstName());
		contactLastNameTextField.setText(cjs.getContactLastName());
		contactFirstNameTextField.setText(cjs.getContactFirstName());
		siteDescriptionTextField.setText(cjs.getSiteName());
	}
	
	/**
	 * Displays the Customers first and last names.
	 * @param c								Customer object containing the information
	 * 										to display.
	 */
	private void showCustomerData(Customer c) {
		
		if(c != null) {
			customerLastNameTextField.setText(c.getLastName());
			customerFirstNameTextField.setText(c.getFirstName());
		}
	}
	
	/**
	 * Displays the Contact Persons' first and last names.
	 * @param cp							ContactPerson object containing the
	 * 										information to display.
	 */
	private void showContactData(ContactPerson cp) {	
		contactLastNameTextField.setText(cp.getLastName());
		contactFirstNameTextField.setText(cp.getFirstName());
	}
	
	/**
	 * Enables all the text fields for use.
	 */
	private void enableTextFields() {
		streetAddressTextField.setEditable(true);
		siteDescriptionTextField.setEditable(true);
		cityTextField.setEditable(true);
		stateTextField.setEditable(true);
		zipCodeTextField.setEditable(true);
		unitNumberTextField.setEditable(true);
	}
	
	/**
	 * Disables the New/Edit/Delete Buttons.
	 */
	private void disableNEDButtons() {
		newButton.setEnabled(false);
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}
	
	/**
	 * Disables the Accept/Cancel Buttons.
	 */
	private void disableACButtons() {
		acceptButton.setEnabled(false);
		cancelButton.setEnabled(false);
	}
	
	/**
	 * Enables the Accept/Cancel Buttons.
	 */
	private void enableACButtons() {
		acceptButton.setEnabled(true);
		cancelButton.setEnabled(true);
	}
	
	/**
	 * Sets all components to the state to enter a New JobSite.
	 */
	private void setNewMode() {	
		dataManipulationMode = "New";
		isContactSelected = false;
		setCustIDNumber(null);	
		enableTextFields();
		nEDButtonChosen();
		clearAllDataFields();
		disableContactItems();
		enableCustomerItems();
		fillComboBox(getCustIDNumber());
	}
	
	/**
	 * Sets Common components used when the New/Edit/Delete Buttons are selected.
	 */
	private void nEDButtonChosen() {
		disableNEDButtons();
		enableACButtons();
		setVerificationLabel(dataManipulationMode);
	}
	
	/**
	 * Enables Customer components.
	 */
	private void enableCustomerItems() {
		choseCustomerLabel.setForeground(Color.BLACK);
		choseCustomerLabel.setVisible(true);
		choseCustomerLabel.setEnabled(true);
		choseCustomerComboBox.setVisible(true);
		choseCustomerComboBox.setEnabled(true);
	}
	
	/**
	 * Disables Customer components.
	 */
	private void disableCustomerItems() {
		choseCustomerLabel.setEnabled(false); 						
		choseCustomerLabel.setVisible(false);
		choseCustomerComboBox.setEnabled(false);
		choseCustomerComboBox.setVisible(false);
	}
	
	/**
	 * Obtains and Fills the Correct Combo Box based on Object Type.
	 * @param custID						String variable used to determine if a Customer
	 * 										has been specified by the user.
	 */
	private void fillComboBox(String custID) {
		if(custID == null) {
			people = readerDAO.getCustomers(addCustMessage);
			fillCustomerComboBox(people);
		}
		else {	
			people = readerDAO.manageContactPeople(isOrderByCustomerSelected, custID, "ContactPerson", addMessage);
			fillContactComboBox(people);
		}
	}
	
	/**
	 * Clears all text displayed in the Text Fields.
	 */
	private void clearAllDataFields() {
		streetAddressTextField.setText("");
		siteDescriptionTextField.setText("");
		cityTextField.setText("");
		stateTextField.setText("");
		zipCodeTextField.setText("");
		unitNumberTextField.setText("");
		customerLastNameTextField.setText("");
		customerFirstNameTextField.setText("");
		contactLastNameTextField.setText("");
		contactFirstNameTextField.setText("");
		jobSiteTextField.setText("");			
	}
	
	/**
	 * Verifies that all data is entered and Passes the data to the Correct Method for further Processing.
	 */
	private void processData() {
		dataEntered = true;
		String streetAddress = verifyEntry(streetAddressTextField);
		String siteDescription = verifyEntry(siteDescriptionTextField);
		String city = verifyEntry(cityTextField);
		String state = verifyEntry(stateTextField);
		String zipCode = verifyEntry(zipCodeTextField);
		String unitNumber = unitNumberTextField.getText();
		if(unitNumber.length() == 0 | unitNumber.equals("N.A.")) {
			unitNumberTextField.setText("N.A.");
			unitNumber = "NULL";
		}
		else {
			unitNumber = verifyEntry(unitNumberTextField);
		}
		
		if(dataEntered) {
			if(dataManipulationMode.equalsIgnoreCase("New")) {
				processJSCreation(streetAddress, city, state, zipCode, unitNumber, siteDescription);
			}
			else if(dataManipulationMode.equalsIgnoreCase("Edit")) {
				editExistingJobSite(siteDescription, streetAddress, city, state, zipCode, unitNumber);
			}
		}
	}
	
	/**
	 * Processes the Editing of an Existing JobSite object.
	 * @param siteDescription				String variable containing the JobSite description.
	 * @param streetAddress					String variable containing the JobSite address.
	 * @param city							String variable containing the JobSite city.
	 * @param state							String variable containing the JobSite state.
	 * @param zipCode						String variable containing the JobSite zip code.
	 * @param unitNumber					String variable containing the JobSite unit number.
	 */
	private void editExistingJobSite(String siteDescription, String streetAddress, String city,
			String state, String zipCode, String unitNumber) {
		
		String siteID = null;
		String addressID = null;
		String contID = null;
		
		siteID = currentJS.getSiteID();
		addressID = currentJS.getAddressID();
		
		if(isContactSelected) {  		
			ContactPerson cp = (ContactPerson) choseContactComboBox.getSelectedItem();
			if(cp != null) {
				contID = cp.getContactID();
				setContIDNumber(contID);	
			}
		}
		else if(contactPassed) {
			contID = getContIDNumber();
		}
		else {
			contID = currentJS.getContactID();
			setContIDNumber(contID);
		}
		writerDAO.manageEditingJobSiteInformation(siteID, addressID, getContIDNumber(), siteDescription,
				streetAddress, city, state, zipCode, unitNumber);
		jobSiteManagerDialogFrame.dispose();
	}
	
	/**
	 * This method manages the process of creating a new JobSite.  It verifies that the objects
	 * all exist and passes the information to the required methods to write the data to the 
	 * database.  In addition, it relays the JobSite and ContactPerson record ID numbers to the
	 * calling class.
	 * @param streetAddress					String variable containing the JobSite street address.
	 * @param city							String variable containing the JobSite city.
	 * @param state							String variable containing the JobSite state.
	 * @param zipCode						String variable containing the JobSite zip code.
	 * @param unitNumber					String variable containing the JobSite unit number.
	 * @param siteDescription				String variable containing the JobSite description.
	 */
	private void processJSCreation(String streetAddress, String city, String state,
			String zipCode, String unitNumber, String siteDescription) {
		
		String newJSSiteID;
		
		Customer c = (Customer) readerDAO.getCurrentCustomer(getCustIDNumber());
		if(c != null) {
			ContactPerson cp = (ContactPerson) readerDAO.getCurrentContactPerson(getContIDNumber());
			if(cp != null) {
				
				writerDAO.manageJobSiteCreation(choice, streetAddress, city,
					state, zipCode, unitNumber, siteDescription, getCustIDNumber(),
					getContIDNumber());

					newJSSiteID = readerDAO.getJobSiteIDNumber(streetAddress, city, state, zipCode,
						siteDescription, getCustIDNumber(), getContIDNumber());
					if(getOwnerName().equalsIgnoreCase("WorkOrderManagerDialog")) {
						WorkOrderManagerDialog.setNewSiteID(newJSSiteID);
						WorkOrderManagerDialog.setContIDNumber(getContIDNumber());
					}
			}
			jobSiteManagerDialogFrame.dispose();
		}
	}
	
	/**
	 * Verifies that a Text Field contains data or Displays a "Data Missing" message.
	 * @param name							JTextField containing the data to be obtained.
	 * @return dataItem						String variable containing the text field information.
	 */
	private String verifyEntry(JTextField name) {
		String dataItem = "";
		
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
	
	/**
	 * Process the Deletion of a JobSite record from the database.
	 */
	private void processDeletion() {
		String siteID = currentJS.getSiteID();		
		
		deleterDAO.deleteJobSiteData(siteID);
		jobSiteManagerDialogFrame.dispose();
	}
	
	/**
	 * Checks that the Text Field held the Data Missing message before resetting the color.
	 * @param name							JTextField name to be verified.
	 */
	private void checkField(JTextField name) {
		if(name.getText().equals("Data Missing")) {  
			name.setText("");
			name.setForeground(Color.BLACK);					
		}
	}
	
	/**
	 * Enables ContactPerson Components.
	 */
	private void enableContactItems() {
		choseContactLabel.setVisible(true);
		choseContactLabel.setEnabled(true);
		choseContactComboBox.setVisible(true);
		choseContactComboBox.setEnabled(true);
	}
	
	/**
	 * Disables ContactPerson Components.
	 */
	private void disableContactItems() {
		changeContactPersonCheckBox.setEnabled(false);		
		changeContactPersonCheckBox.setVisible(false);		
		choseContactLabel.setForeground(Color.BLACK);		
		choseContactLabel.setVisible(false);
		choseContactLabel.setEnabled(false);
		choseContactComboBox.setVisible(false);
		choseContactComboBox.setEnabled(false);
		
	}
	
	/**
	 * Calls a New Dialog to Add New People information.
	 * @param choice						String variable defining the People type to be added. 
	 */
	private void callNewPeopleDialog(String choice) {
		addNewPeopleDialog = new AddNewPeopleDialog(jobSiteManagerDialogFrame, choice);
		addNewPeopleDialog.addNewPeopleDialogFrame.setVisible(true);
	}
	
	/**
	 * Manages the Verification Label Usage.
	 * @param dataManipulationMode			String variable defining the data mode in use.
	 */
	private void setVerificationLabel(String dataManipulationMode) {
		String modeWord = "";
		
		if(dataManipulationMode.equalsIgnoreCase("New")) {
			modeWord = "Enter";
		}
		else {
			modeWord = dataManipulationMode;
		}
		
		verificationLabel.setForeground(Color.RED);
		verificationLabel.setText(modeWord + " this Job Site?");
		
	}
	
	/**
	 * Initialize the Contents of the Frame.
	 */
	private void initialize() {
		jobSiteManagerDialogFrame.setBounds(100, 100, 701, 551);
		jobSiteManagerDialogFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		jobSiteManagerDialogFrame.getContentPane().setLayout(null);		
		
		JLabel jobSiteLabel = new JLabel("Job Site:");
		jobSiteLabel.setBounds(117, 41, 91, 14);
		jobSiteManagerDialogFrame.getContentPane().add(jobSiteLabel);
		
		jobSiteTextField = new JTextField();
		jobSiteTextField.setEditable(false);
		jobSiteTextField.setBounds(256, 38, 195, 20);
		jobSiteTextField.setColumns(10);
		jobSiteManagerDialogFrame.getContentPane().add(jobSiteTextField);
		
		JLabel streetAddressLabel = new JLabel("Street Address:");
		streetAddressLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		streetAddressLabel.setBounds(10, 105, 122, 14);
		jobSiteManagerDialogFrame.getContentPane().add(streetAddressLabel);
		
		JLabel siteDescriptionLabel = new JLabel("Site Description:");
		siteDescriptionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		siteDescriptionLabel.setBounds(345, 105, 108, 14);
		jobSiteManagerDialogFrame.getContentPane().add(siteDescriptionLabel);
		
		streetAddressTextField = new JTextField();
		streetAddressTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				checkField(streetAddressTextField);
			}
		});
		streetAddressTextField.setEditable(false);
		streetAddressTextField.setBounds(142, 102, 144, 20);
		streetAddressTextField.setColumns(10);
		jobSiteManagerDialogFrame.getContentPane().add(streetAddressTextField);
		
		siteDescriptionTextField = new JTextField();
		siteDescriptionTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				checkField(siteDescriptionTextField);
			}
		});
		siteDescriptionTextField.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				String input = siteDescriptionTextField.getText();
				String firstName = customerFirstNameTextField.getText();
				String lastName = customerLastNameTextField.getText();
				jobSiteTextField.setText(firstName + " " + lastName + "'s " + input);
			}
		});
		siteDescriptionTextField.setEditable(false);
		siteDescriptionTextField.setBounds(463, 102, 199, 20);
		siteDescriptionTextField.setColumns(10);
		jobSiteManagerDialogFrame.getContentPane().add(siteDescriptionTextField);
		
		JLabel cityLabel = new JLabel("City:");
		cityLabel.setBounds(10, 165, 46, 14);
		jobSiteManagerDialogFrame.getContentPane().add(cityLabel);
		
		cityTextField = new JTextField();
		cityTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				checkField(cityTextField);
			}
		});
		cityTextField.setEditable(false);
		cityTextField.setBounds(48, 162, 86, 20);
		cityTextField.setColumns(10);
		jobSiteManagerDialogFrame.getContentPane().add(cityTextField);
		
		JLabel stateLabel = new JLabel("State:");
		stateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		stateLabel.setBounds(144, 165, 46, 14);
		jobSiteManagerDialogFrame.getContentPane().add(stateLabel);
		
		JLabel zipCodeLabel = new JLabel("Zip Code:");
		zipCodeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		zipCodeLabel.setBounds(296, 165, 56, 14);
		jobSiteManagerDialogFrame.getContentPane().add(zipCodeLabel);
		
		JLabel unitNumberLabel = new JLabel("Unit Number:");
		unitNumberLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		unitNumberLabel.setBounds(463, 165, 86, 14);
		jobSiteManagerDialogFrame.getContentPane().add(unitNumberLabel);
		
		stateTextField = new JTextField();
		stateTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				checkField(stateTextField);
			}
		});
		stateTextField.setEditable(false);
		stateTextField.setBounds(200, 162, 86, 20);
		stateTextField.setColumns(10);
		jobSiteManagerDialogFrame.getContentPane().add(stateTextField);
		
		zipCodeTextField = new JTextField();
		zipCodeTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				checkField(zipCodeTextField);
			}
		});
		zipCodeTextField.setEditable(false);
		zipCodeTextField.setBounds(362, 162, 86, 20);
		zipCodeTextField.setColumns(10);
		jobSiteManagerDialogFrame.getContentPane().add(zipCodeTextField);
		
		unitNumberTextField = new JTextField();
		unitNumberTextField.setEditable(false);
		unitNumberTextField.setBounds(576, 162, 86, 20);
		unitNumberTextField.setColumns(10);
		jobSiteManagerDialogFrame.getContentPane().add(unitNumberTextField);
		
		choseCustomerLabel = new JLabel("Chose A Customer");
		choseCustomerLabel.setEnabled(false);
		choseCustomerLabel.setBounds(10, 217, 122, 14);
		choseCustomerLabel.setVisible(false);
		jobSiteManagerDialogFrame.getContentPane().add(choseCustomerLabel);
		
		choseCustomerComboBox = new JComboBox<People>();
		choseCustomerComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!filling) {
					Customer c = (Customer) choseCustomerComboBox.getSelectedItem();
					if(c != null) {
						if(c.getTitle().equals("Add New Customer")) {
							choice = "Customer";
							System.out.println("JSMD L-840 add new customer");
							AddNewPeopleDialog.setOwnerName("JobSiteManagerDialog");
							callNewPeopleDialog(choice);
							c = readerDAO.getCurrentCustomer(getCustIDNumber());
							customerPassed = true;
						}
						else {
							System.out.println("JSMD L-850 existing customer");
							String localCustID = c.getCustomerID();
							setCustIDNumber(localCustID);
						}
						
						disableCustomerItems();
						showCustomerData(c);
						jobSiteTextField.setText(customerFirstNameTextField.getText() + " " + customerLastNameTextField.getText() +
								"'s " + siteDescriptionTextField.getText());
						enableContactItems();
						fillComboBox(getCustIDNumber());
					}
				}
			}
		});
		choseCustomerComboBox.setEnabled(false);
		choseCustomerComboBox.setBounds(128, 214, 195, 20);
		choseCustomerComboBox.setVisible(false);
		jobSiteManagerDialogFrame.getContentPane().add(choseCustomerComboBox);
		
		JLabel customerLastNameLabel = new JLabel("Customer Last Name:");
		customerLastNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		customerLastNameLabel.setBounds(10, 255, 134, 14);
		jobSiteManagerDialogFrame.getContentPane().add(customerLastNameLabel);
		
		customerLastNameTextField = new JTextField();
		customerLastNameTextField.setEditable(false);
		customerLastNameTextField.setBounds(154, 252, 169, 20);
		customerLastNameTextField.setColumns(10);
		jobSiteManagerDialogFrame.getContentPane().add(customerLastNameTextField);
		
		JLabel customerFirstNameLabel = new JLabel("Customer First Name:");
		customerFirstNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		customerFirstNameLabel.setBounds(349, 255, 134, 14);
		jobSiteManagerDialogFrame.getContentPane().add(customerFirstNameLabel);
		
		customerFirstNameTextField = new JTextField();
		customerFirstNameTextField.setEditable(false);
		customerFirstNameTextField.setBounds(493, 252, 169, 20);
		customerFirstNameTextField.setColumns(10);
		jobSiteManagerDialogFrame.getContentPane().add(customerFirstNameTextField);
		
		choseContactLabel = new JLabel("Chose A Contact");
		choseContactLabel.setEnabled(false);
		choseContactLabel.setBounds(10, 295, 134, 14);
		choseContactLabel.setVisible(false);
		jobSiteManagerDialogFrame.getContentPane().add(choseContactLabel);
		
		choseContactComboBox = new JComboBox<People>();
		choseContactComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!filling) {
					String contactID = "";
					ContactPerson cp = (ContactPerson) choseContactComboBox.getSelectedItem();
					if(cp != null) {
						if(cp.getTitle().equalsIgnoreCase("Add New Contact Person")) {
							choice = "ContactPerson";
							AddNewPeopleDialog.setOwnerName("JobSiteManagerDialog");
							callNewPeopleDialog(choice);
							contactID = getContIDNumber();
							cp = readerDAO.getCurrentContactPerson(getContIDNumber());
							contactPassed = true;
						}
						else {
							System.out.println("JSMD L-915 existing contact");
							contactID = cp.getContactID();
							setContIDNumber(contactID);
							isContactSelected = true;		
							if(jobSitePassed) {
								WorkOrderManagerDialog.setContIDNumber(getContIDNumber());
							}
							else if(customerPassed) {
								WorkOrderManagerDialog.setContIDNumber(getContIDNumber());
							}
						}
						showContactData(cp);
						disableContactItems();
					}
				}
			}
		});
		choseContactComboBox.setEnabled(false);
		choseContactComboBox.setBounds(128, 292, 279, 20);
		choseContactComboBox.setVisible(false);
		jobSiteManagerDialogFrame.getContentPane().add(choseContactComboBox);
		
		JLabel contactLastNameLabel = new JLabel("Contact Last Name:");
		contactLastNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		contactLastNameLabel.setBounds(10, 334, 122, 14);
		jobSiteManagerDialogFrame.getContentPane().add(contactLastNameLabel);
		
		contactLastNameTextField = new JTextField();
		contactLastNameTextField.setEditable(false);
		contactLastNameTextField.setBounds(154, 331, 169, 20);
		contactLastNameTextField.setColumns(10);
		jobSiteManagerDialogFrame.getContentPane().add(contactLastNameTextField);
		
		JLabel contactFirstNameLabel = new JLabel("Contact First Name:");
		contactFirstNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		contactFirstNameLabel.setBounds(362, 334, 121, 14);
		jobSiteManagerDialogFrame.getContentPane().add(contactFirstNameLabel);
		
		contactFirstNameTextField = new JTextField();
		contactFirstNameTextField.setEditable(false);
		contactFirstNameTextField.setBounds(493, 331, 169, 20);
		contactFirstNameTextField.setColumns(10);
		jobSiteManagerDialogFrame.getContentPane().add(contactFirstNameTextField);
		
		newButton = new JButton("New");
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setNewMode();
			}
		});
		newButton.setBounds(10, 446, 89, 23);
		jobSiteManagerDialogFrame.getContentPane().add(newButton);
		
		editButton = new JButton("Edit");
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dataManipulationMode = "Edit";
				enableTextFields();
				changeContactPersonCheckBox.setVisible(true);
				changeContactPersonCheckBox.setEnabled(true);
				nEDButtonChosen();
			}
		});
		editButton.setBounds(117, 446, 89, 23);
		jobSiteManagerDialogFrame.getContentPane().add(editButton);
		
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dataManipulationMode = "Delete";
				nEDButtonChosen();
			}
		});
		deleteButton.setBounds(216, 446, 89, 23);
		jobSiteManagerDialogFrame.getContentPane().add(deleteButton);
		
		acceptButton = new JButton("Accept");
		acceptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!dataManipulationMode.equalsIgnoreCase("Delete")) {
					processData();
				}
				else {
					processDeletion();
				}
			}
		});
		acceptButton.setBounds(318, 426, 89, 23);
		jobSiteManagerDialogFrame.getContentPane().add(acceptButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setNewMode();
			}
		});
		cancelButton.setBounds(318, 462, 89, 23);
		jobSiteManagerDialogFrame.getContentPane().add(cancelButton);
		
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jobSiteManagerDialogFrame.dispose();
			}
		});
		closeButton.setBounds(586, 478, 89, 23);
		jobSiteManagerDialogFrame.getContentPane().add(closeButton);
		
		changeContactPersonCheckBox = new JCheckBox("Change Contact Person");
		changeContactPersonCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					enableContactItems();
					String customerID = currentJS.getCustomerID();
					setCustIDNumber(customerID);
					fillComboBox(getCustIDNumber());
				}
				else if(e.getStateChange() == ItemEvent.DESELECTED) {
					disableContactItems();
				}
			}
		});
		changeContactPersonCheckBox.setEnabled(false);
		changeContactPersonCheckBox.setBounds(48, 355, 183, 23);
		changeContactPersonCheckBox.setVisible(false);
		jobSiteManagerDialogFrame.getContentPane().add(changeContactPersonCheckBox);
		
		verificationLabel = new JLabel("");
		verificationLabel.setBounds(425, 450, 124, 14);
		jobSiteManagerDialogFrame.getContentPane().add(verificationLabel);
	
	}
}