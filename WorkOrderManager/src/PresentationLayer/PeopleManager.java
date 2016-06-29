package PresentationLayer;

import DatabaseLayer.*;
import BusinessLayer.*;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.util.ArrayList;

/**
 * Class Name:					PeopleManager
 * Description:					This class contains all the code and methods necessary to run
 * 								the main graphic user interface and calls customized dialogs
 * 								for other user interfaces.
 * @author Richard Stuart
 * @created Sunday, 5,29,16
 */
public class PeopleManager {

	private JFrame peopleManagerFrame;
	private final ButtonGroup viewItemButtonGroup = new ButtonGroup();
	private JTextField lastNameTextField;
	private JTextField firstNameTextField;
	private JTextField phoneNumberTextField;
	private JTextField streetAddressTextField;
	private JTextField cellPhoneTextField;
	private JTextField cityTextField;
	private JTextField stateTextField;
	private JTextField emailAddressTextField;
	private JTextField zipCodeTextField;
	private JTextField unitNumberTextField;
	private JTextField siteDescriptionTextField;
	private JRadioButton customerRadioButton;
	private JRadioButton contactPersonRadioButton;
	private JRadioButton employeeRadioButton;
	private JRadioButton jobSiteRadioButton;
	private JRadioButton workOrderRadioButton;
	private JCheckBox sortByCustomerCheckBox;
	private JComboBox<Object> selectItemToViewComboBox;					
	private JComboBox<People> customerComboBox;							
	private JButton newButton;
	private JButton editButton;
	private JButton deleteButton;
	private JButton acceptButton;
	private JButton cancelButton;
	private JButton exitButton;
	private JLabel itemToViewPrefixLabel;
	private JLabel itemToViewLabel;
	private JLabel itemToViewPostfixLabel;
	private JLabel verificationLabel;
	private JLabel selectACustomerLabel;
	
	private WriterDAO writerDAO;
	private ReaderDAO readerDAO;
	private DeleterDAO deleterDAO;
	private WorkOrderManagerDialog workOrderManagerDialog = null;
	private ArrayList<People> people;				
	private ArrayList<JobSite> jobSites;
	private ArrayList<WorkOrder> workOrders;
	private boolean filling = false;
	private boolean isOrderByCustomerSelected = false;
	private boolean dataEntered = true;
	private boolean addMessage = false;
	private boolean addCustMessage = false;
	private String choice = "";
	private String dataManipulationMode = "";
	private String customerID = null;
	private People currentPerson = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PeopleManager window = new PeopleManager();	
					window.peopleManagerFrame.setVisible(true);
					window.peopleManagerFrame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application items.
	 */
	public PeopleManager() {
		initialize();
		writerDAO = DAOFactory.getWriterDAO();
		readerDAO = DAOFactory.getReaderDAO();
		deleterDAO = DAOFactory.getDeleterDAO();
	}

	
	/**
	 * Pause the program for 2 seconds.
	 */
	/**
	private static void pause() {
		try { 
			 Thread.sleep(2000); 
			 } 
		catch(InterruptedException e) { 
			 } 
	}
	**/
	
	/**
	 * Fills the Customer Combo Box.
	 * @param p						ArrayList of Customer objects.
	 */
	private void fillCustomerComboBox(ArrayList<People> p) {	
		filling = true;											
																
		customerComboBox.removeAllItems();
		for(People person: p) {
			customerComboBox.addItem(person);		
		}
		filling = false;
	}
	
	/**
	 * Fills the Combo Box with People objects.
	 * @param p						ArrayList of People objects.
	 */
	private void fillComboBoxWithPeople(ArrayList<People> p) {
		filling = true;
		
		selectItemToViewComboBox.removeAllItems();
		for(People person: p) {
			selectItemToViewComboBox.addItem(person);
		}
		filling = false;
	}
	
	/**
	 * Fills the combo box with Job Sites.
	 * @param js					ArrayList of JobSite objects.
	 */
	private void fillComboBoxWithJobSites(ArrayList<JobSite> js) {
		filling = true;
		
		selectItemToViewComboBox.removeAllItems();
		for(JobSite site: js) {
			selectItemToViewComboBox.addItem(site);
		}
		selectItemToViewComboBox.setEnabled(true);
		filling = false;
	}
	
	/**
	 * Fills the combo box with Work Orders.
	 * @param wo					ArrayList of WorkOrder objects.
	 */
	private void fillComboBoxWithWorkOrders(ArrayList<WorkOrder> wo) {
		filling = true;
		
		selectItemToViewComboBox.removeAllItems();
		for(WorkOrder wOrder: wo) {
			selectItemToViewComboBox.addItem(wOrder);
		}
		selectItemToViewComboBox.setEnabled(true);
		filling = false;
	}
	
	/**
	 * Clears all data from the text fields.
	 */
	private void clearAllViewDataFields() {
		lastNameTextField.setText("");
		firstNameTextField.setText("");
		streetAddressTextField.setText("");
		cityTextField.setText("");
		stateTextField.setText("");
		zipCodeTextField.setText("");
		unitNumberTextField.setText("");
		siteDescriptionTextField.setText("");
		phoneNumberTextField.setText("");
		cellPhoneTextField.setText("");
		emailAddressTextField.setText("");
	}
	
	/**
	 * Displays People data in the text fields.
	 * @param p						People object.
	 */
	private void showData(People p) {
		lastNameTextField.setText(p.getLastName());
		firstNameTextField.setText(p.getFirstName());
		streetAddressTextField.setText(p.getStreetAddress());
		cityTextField.setText(p.getCity());
		stateTextField.setText(p.getState());
		zipCodeTextField.setText(p.getZipCode());
		String unitNumber = p.getUnitNumber();
		if(unitNumber != null) {
			unitNumberTextField.setText(p.getUnitNumber());
		}
		else {
			unitNumberTextField.setText("N.A.");
		}
		siteDescriptionTextField.setText(p.getDescription());
		phoneNumberTextField.setText(p.getPhoneNumber());
		cellPhoneTextField.setText(p.getCellPhone());
		emailAddressTextField.setText(p.getEmail());
	}
	
	/**
	 * Enables the Edit and Delete buttons.
	 */
	private void enableEDButtons() {
		editButton.setEnabled(true);
		deleteButton.setEnabled(true);
	}
	
	/**
	 * Disables the Edit and Delete buttons.
	 */
	private void disableEDButtons() {
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}
	
	/**
	 * Enables the Accept and Cancel buttons.
	 */
	private void enableACButtons() {
		acceptButton.setEnabled(true);
		cancelButton.setEnabled(true);
	}
	
	/**
	 * Disables the Accept and Cancel buttons.
	 */
	private void disableACButtons() {
		acceptButton.setEnabled(false);
		cancelButton.setEnabled(false);
	}
	
	/**
	 * Disables the Radio Buttons in the button group.
	 */
	private void disableViewItemButtonGroup() {
		customerRadioButton.setEnabled(false);
		contactPersonRadioButton.setEnabled(false);
		employeeRadioButton.setEnabled(false);
		jobSiteRadioButton.setEnabled(false);
		workOrderRadioButton.setEnabled(false);
	}
	
	/**
	 * Enables the Radio Buttons in the button group.
	 */
	private void enableViewItemButtonGroup() {
		customerRadioButton.setEnabled(true);
		contactPersonRadioButton.setEnabled(true);
		employeeRadioButton.setEnabled(true);
		jobSiteRadioButton.setEnabled(true);
		workOrderRadioButton.setEnabled(true);
	}
	
	/**
	 * Disables the Labels for the selectItemToViewComboBox.
	 */
	private void disableViewItemLabels() {
		itemToViewPrefixLabel.setVisible(false);
		itemToViewLabel.setVisible(false);
		itemToViewPostfixLabel.setVisible(false);
	}
	
	/**
	 * Enables the Labels for the selectItemToViewComboBox.
	 */
	private void enableViewItemLabels() {
		itemToViewPrefixLabel.setVisible(true);
		itemToViewLabel.setVisible(true);
		itemToViewPostfixLabel.setVisible(true);
	}
	
	/**
	 * Enables all Text Fields.
	 */
	private void enableTextFields() {
		lastNameTextField.setEditable(true);
		firstNameTextField.setEditable(true);
		streetAddressTextField.setEditable(true);
		cityTextField.setEditable(true);
		stateTextField.setEditable(true);
		zipCodeTextField.setEditable(true);
		unitNumberTextField.setEditable(true);
		siteDescriptionTextField.setEditable(true);
		phoneNumberTextField.setEditable(true);
		cellPhoneTextField.setEditable(true);
		emailAddressTextField.setEditable(true);
	}
	
	/**
	 * Disables all Text Fields.
	 */
	private void disableTextFields() {
		lastNameTextField.setEditable(false);
		firstNameTextField.setEditable(false);
		streetAddressTextField.setEditable(false);
		cityTextField.setEditable(false);
		stateTextField.setEditable(false);
		zipCodeTextField.setEditable(false);
		unitNumberTextField.setEditable(false);
		siteDescriptionTextField.setEditable(false);
		phoneNumberTextField.setEditable(false);
		cellPhoneTextField.setEditable(false);
		emailAddressTextField.setEditable(false);
	}
	
	/**
	 * This method obtains all the information from the text fields for People objects, 
	 * and directs calling the correct method to either create, edit, or delete a People object.
	 */
	private void processData() {
		
		String lastName = verifyEntry(lastNameTextField);
		String firstName = verifyEntry(firstNameTextField);
		String phoneNumber = verifyEntry(phoneNumberTextField);
		String streetAddress = verifyEntry(streetAddressTextField);
		String cellPhone = verifyEntry(cellPhoneTextField);
		String city = verifyEntry(cityTextField);
		String state = verifyEntry(stateTextField);
		String emailAddress = verifyEntry(emailAddressTextField);
		String zipCode = verifyEntry(zipCodeTextField);
		String unitNumber = unitNumberTextField.getText();
		
		if(unitNumber.length() == 0 | unitNumber.equals("N.A.")) {
			unitNumberTextField.setText("N.A.");
			unitNumber = "NULL";
		}
		else {
			unitNumber = verifyEntry(unitNumberTextField);
		}
		String siteDescription = verifyEntry(siteDescriptionTextField);
		
		if(dataEntered) {																
			if(dataManipulationMode.equals("New")) {
				writerDAO.manageNewPersonCreation(choice, lastName, firstName, streetAddress, city, state, zipCode, unitNumber,
						siteDescription, phoneNumber, cellPhone, emailAddress);
			}
			if(dataManipulationMode.equalsIgnoreCase("Edit")){
				writerDAO.manageEditingPeopleInformation(choice, currentPerson, lastName, firstName, streetAddress, city, state, zipCode, unitNumber,
						siteDescription, phoneNumber, cellPhone, emailAddress);
			}
			if(dataManipulationMode.equalsIgnoreCase("Delete")) {			
				deleterDAO.deletePeopleData(choice, currentPerson);
			}
			selectItemToViewComboBox.removeAllItems();
			setStartMode();
		}
		dataEntered = true;
	}
	
	/**
	 * Verifies that data is held in Text Fields, or Displays the Data Missing message.
	 * @param name					JTextField name where the data is to be obtained.
	 * @return dataItem				String variable to hold the data from the text field.
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
	 * Checks that the Text Field held the Data Missing message before resetting the color.
	 * @param name					JTextField name to be checked.
	 */
	private void checkField(JTextField name) {			
		if(name.getText().equals("Data Missing")) {  
			name.setText("");
			name.setForeground(Color.BLACK);
		}
	}
	
	/**
	 * Set all Components to their Initial states.
	 */
	private void setStartMode() {
		clearAllViewDataFields();
		disableACButtons();
		if(sortByCustomerCheckBox.isSelected()) {
			sortByCustomerCheckBox.doClick();	
		}
		disableSitvCB();
		disableTextFields();
		enableViewItemButtonGroup();
		newButton.setEnabled(false);
		itemToViewLabel.setText("");
		verificationLabel.setText("");
		viewItemButtonGroup.clearSelection();
	}
	
	/**
	 * Manages and Displays the Appropriate Label for the selectItemToViewComboBox.
	 * @param choice				String variable to identify object type being manipulated.
	 */
	private void setItemToViewLabels(String choice) {
		itemToViewPrefixLabel.setText("Select a/an");
		itemToViewLabel.setForeground(Color.red);
		itemToViewPostfixLabel.setText("to View");
		if(choice.equals("Customer")) {
			itemToViewLabel.setText("CUSTOMER");	
		}
		else if(choice.equals("ContactPerson")) {
			itemToViewLabel.setText("CONTACT PERSON");
		}
		else if(choice.equals("Employee")) {
			itemToViewLabel.setText("Employee");
		}
		else if(choice.equals("JobSite")) {
			itemToViewLabel.setText("Job Site");
		}
		else if(choice.equals("WorkOrder")) {
			itemToViewLabel.setText("Work Order");
		}
	}
	
	/**
	 * Enables and sets the Label Message for the Customer Combo Box.
	 */
	private void enableSelectCustomerLabel() {
		selectACustomerLabel.setText("Select a Customer");
		selectACustomerLabel.setForeground(Color.red);
		selectACustomerLabel.setEnabled(true);
		selectACustomerLabel.setVisible(true);
	}
	
	/**
	 * Disables the Label used with the Customer Combo Box.
	 */
	private void disableSelectCustomerLabel() {
		selectACustomerLabel.setEnabled(false);
		selectACustomerLabel.setVisible(false);
	}

	/**
	 * Manages Components and Combo Boxes when a Radio Button is Selected.
	 * @param choice				String variable to identify object types being manipulated.
	 */
	private void processRadioButtonSelection(String choice) {
		if(sortByCustomerCheckBox.isSelected()) {
			sortByCustomerCheckBox.doClick();	
		}
		clearAllViewDataFields();
		disableEDButtons();
		enableViewItemLabels();
		fillComboBox(choice);
		setItemToViewLabels(choice);
		newButton.setEnabled(true);
		selectItemToViewComboBox.setEnabled(true);
		if(!choice.equalsIgnoreCase("Customer")) {
			sortByCustomerCheckBox.setEnabled(true);				
		}
	}
	
	/**
	 * This method calls the correct data access method to obtain the chosen objects in an ArrayList.
	 * @param choice				String variable to identify the object type to obtain.
	 * @return people				ArrayList holding the objects for use.
	 */
	private ArrayList<People> getPeople(String choice) {
		
		if(choice.equalsIgnoreCase("Customer")) {
			people = readerDAO.getCustomers(addCustMessage);
			sortByCustomerCheckBox.setEnabled(false);
		}
		else if(choice.equalsIgnoreCase("ContactPerson")) {
			people = readerDAO.manageContactPeople(isOrderByCustomerSelected, customerID, choice, addMessage);
		}
		else {
			boolean addAll = false;
			people = readerDAO.manageEmployees(isOrderByCustomerSelected, customerID, choice, addAll);
		}
		
		return people;
	}
	
	/**
	 * Disables the selectItemToViewComboBox and sortByCustomerCheckBox.
	 */
	private void disableSitvCB() {
		filling = true;
		sortByCustomerCheckBox.setEnabled(false);
		selectItemToViewComboBox.removeAllItems();
		selectItemToViewComboBox.setEnabled(false);
		filling = false;
	}
	
	/**
	 * Returns all text fields to original color.
	 */
	private void setFieldColor() {
		lastNameTextField.setForeground(Color.BLACK);
		firstNameTextField.setForeground(Color.BLACK);
		phoneNumberTextField.setForeground(Color.BLACK);
		streetAddressTextField.setForeground(Color.BLACK);
		cellPhoneTextField.setForeground(Color.BLACK);
		cityTextField.setForeground(Color.BLACK);
		stateTextField.setForeground(Color.BLACK);
		emailAddressTextField.setForeground(Color.BLACK);
		zipCodeTextField.setForeground(Color.BLACK);
		unitNumberTextField.setForeground(Color.BLACK);
		siteDescriptionTextField.setForeground(Color.BLACK);
	}
	
	/**
	 * Obtains the desired objects in an ArrayList and calls the correct method to fill the combo box.
	 * @param choice				String variable to identify the object type to be manipulated.
	 */
	private void fillComboBox(String choice) {
		if(choice.equalsIgnoreCase("JobSite")) {
			jobSites = readerDAO.getJobSites(customerID, isOrderByCustomerSelected, addMessage);
			fillComboBoxWithJobSites(jobSites);
		}
		else if(choice.equalsIgnoreCase("WorkOrder")) {
			workOrders = readerDAO.getWorkOrders(isOrderByCustomerSelected, customerID);
			fillComboBoxWithWorkOrders(workOrders);
		}
		else {
			people = getPeople(choice); 
			fillComboBoxWithPeople(people);
		}
	}
	
	/**
	 * Manages Components when the New/Edit/Delete buttons are used.
	 */
	private void manageNEDComponents() {
		String modeWord = "";
		if(dataManipulationMode.equalsIgnoreCase("New")) {
			modeWord = "Enter";
		}
		else {
			modeWord = dataManipulationMode;
		}
		if(sortByCustomerCheckBox.isSelected()) {	
			sortByCustomerCheckBox.doClick();	
		}
		disableEDButtons();
		disableViewItemButtonGroup();
		disableViewItemLabels();
		enableACButtons();
		newButton.setEnabled(false);
		verificationLabel.setForeground(Color.RED);
		verificationLabel.setText(modeWord + " this " + choice + " data?");
	}
	
	/**
	 * Calls the JobSiteManagerDialog Constructor passing the JobSite Object and dataManipulationMode.
	 * @param js					JobSite object to be passed.
	 */
	private void passToConstructor(JobSite js) {
		JobSiteManagerDialog.setOwnerName("PeopleManager");
		new JobSiteManagerDialog(peopleManagerFrame, js, dataManipulationMode);
		JobSiteManagerDialog.jobSiteManagerDialogFrame.setVisible(true);
		setStartMode();
	}
	
	/**
	 * Calls the WorkOrderManagerDialog Constructor passing the WorkOrder Object and dataManipulationMode.
	 * @param workOrder				WorkOrder object to be passed.
	 */
	private void passToConstructor(WorkOrder workOrder) {
		workOrderManagerDialog = new WorkOrderManagerDialog(peopleManagerFrame,
				workOrder, dataManipulationMode);
		workOrderManagerDialog.workOrderManagerDialogFrame.setVisible(true);
		setStartMode();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		peopleManagerFrame = new JFrame();
		peopleManagerFrame.setTitle("Database Manager");
		peopleManagerFrame.setBounds(100, 100, 726, 428);
		peopleManagerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		peopleManagerFrame.getContentPane().setLayout(null);
		
		JPanel viewItemPanel = new JPanel();
		viewItemPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		viewItemPanel.setBounds(10, 11, 145, 153);
		viewItemPanel.setLayout(null);
		peopleManagerFrame.getContentPane().add(viewItemPanel);
		
		customerRadioButton = new JRadioButton("Customer");
		customerRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				choice = "Customer";
				processRadioButtonSelection(choice);
			}
		});
		viewItemButtonGroup.add(customerRadioButton);
		customerRadioButton.setBounds(6, 7, 109, 23);
		viewItemPanel.add(customerRadioButton);
		
		contactPersonRadioButton = new JRadioButton("Contact Person");
		contactPersonRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				choice = "ContactPerson";
				processRadioButtonSelection(choice);
			}
		});
		viewItemButtonGroup.add(contactPersonRadioButton);
		contactPersonRadioButton.setBounds(6, 33, 133, 23);
		viewItemPanel.add(contactPersonRadioButton);
		
		employeeRadioButton = new JRadioButton("Employee");
		employeeRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				choice = "Employee";
				processRadioButtonSelection(choice);
			}
		});
		viewItemButtonGroup.add(employeeRadioButton);
		employeeRadioButton.setBounds(6, 59, 109, 23);
		viewItemPanel.add(employeeRadioButton);
		
		jobSiteRadioButton = new JRadioButton("Job Site");
		jobSiteRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				choice = "JobSite";
				processRadioButtonSelection(choice);
			}
		});
		viewItemButtonGroup.add(jobSiteRadioButton);
		jobSiteRadioButton.setBounds(6, 85, 109, 23);
		viewItemPanel.add(jobSiteRadioButton);
		
		workOrderRadioButton = new JRadioButton("Work Order");
		workOrderRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				choice = "WorkOrder";
				processRadioButtonSelection(choice);
			}
		});
		viewItemButtonGroup.add(workOrderRadioButton);
		workOrderRadioButton.setBounds(6, 111, 109, 23);
		viewItemPanel.add(workOrderRadioButton);
		
		selectItemToViewComboBox = new JComboBox<Object>();
		selectItemToViewComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!filling) {
					if(choice.equals("JobSite")) {
						JobSite js = (JobSite) selectItemToViewComboBox.getSelectedItem();
						if(js != null) {
							dataManipulationMode = "Display";
							passToConstructor(js);
						}
					}
					else if(choice.equals("WorkOrder")) {
						WorkOrder workOrder = (WorkOrder) selectItemToViewComboBox.getSelectedItem();
						if(workOrder != null) {
							dataManipulationMode = "Display";
							passToConstructor(workOrder);
						}
					}
					else {
						currentPerson = (People) selectItemToViewComboBox.getSelectedItem();
						if(currentPerson != null) {
							showData(currentPerson);
							enableEDButtons();
						}
					}
				}
			}
		});
		selectItemToViewComboBox.setEnabled(false);
		selectItemToViewComboBox.setBounds(165, 144, 535, 20);
		peopleManagerFrame.getContentPane().add(selectItemToViewComboBox);
		
		sortByCustomerCheckBox = new JCheckBox("Sort By Customer");
		sortByCustomerCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					clearAllViewDataFields();
					disableSitvCB();
					disableEDButtons();
					disableViewItemLabels();
					enableSelectCustomerLabel();
					customerComboBox.setEnabled(true);
					isOrderByCustomerSelected = true;
					sortByCustomerCheckBox.setEnabled(true);
					people = readerDAO.getCustomers(addCustMessage);
					fillCustomerComboBox(people);			
				}
				else if(e.getStateChange() == ItemEvent.DESELECTED) {
					if(!dataManipulationMode.equalsIgnoreCase("Edit") && !dataManipulationMode.equalsIgnoreCase("Delete")) {
						clearAllViewDataFields();
					}
					disableSelectCustomerLabel();
					enableViewItemLabels();
					customerComboBox.removeAllItems();
					customerComboBox.setEnabled(false);
					isOrderByCustomerSelected = false;
					fillComboBox(choice);					
					selectItemToViewComboBox.setEnabled(true);
				}
			}
		});
		sortByCustomerCheckBox.setEnabled(false);
		sortByCustomerCheckBox.setBounds(165, 11, 200, 20);
		peopleManagerFrame.getContentPane().add(sortByCustomerCheckBox);
		
		itemToViewPrefixLabel = new JLabel("");
		itemToViewPrefixLabel.setBounds(165, 119, 77, 14);
		peopleManagerFrame.getContentPane().add(itemToViewPrefixLabel);
		
		itemToViewLabel = new JLabel("");
		itemToViewLabel.setBounds(247, 119, 108, 14);
		peopleManagerFrame.getContentPane().add(itemToViewLabel);
		
		itemToViewPostfixLabel = new JLabel("");
		itemToViewPostfixLabel.setBounds(375, 119, 46, 14);
		peopleManagerFrame.getContentPane().add(itemToViewPostfixLabel);
		
		customerComboBox = new JComboBox<People>();
		customerComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!filling) {
					Customer c = (Customer) customerComboBox.getSelectedItem();
					disableEDButtons();
					
					if(c != null) {					
						customerID = c.getCustomerID();
						disableSelectCustomerLabel();
						enableViewItemLabels();
						fillComboBox(choice);
						selectItemToViewComboBox.setEnabled(true);
					}
				}
			}
		});
		customerComboBox.setEnabled(false);
		customerComboBox.setBounds(165, 37, 246, 20);
		peopleManagerFrame.getContentPane().add(customerComboBox);
		
		JPanel viewAreaPanel = new JPanel();
		viewAreaPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		viewAreaPanel.setBounds(10, 193, 690, 117);
		viewAreaPanel.setLayout(null);
		peopleManagerFrame.getContentPane().add(viewAreaPanel);
		
		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameLabel.setBounds(10, 10, 77, 14);
		viewAreaPanel.add(lastNameLabel);
		
		JLabel firstNameLabel = new JLabel("First Name:");
		firstNameLabel.setBounds(269, 10, 77, 14);
		viewAreaPanel.add(firstNameLabel);
		
		JLabel phoneNumberLabel = new JLabel("Phone Number:");
		phoneNumberLabel.setBounds(454, 10, 88, 14);
		viewAreaPanel.add(phoneNumberLabel);
		
		JLabel streetAddressLabel = new JLabel("Street Address:");
		streetAddressLabel.setBounds(10, 38, 98, 14);
		viewAreaPanel.add(streetAddressLabel);
		
		JLabel cellPhoneLabel = new JLabel("Cell Phone:");
		cellPhoneLabel.setBounds(454, 38, 88, 14);
		viewAreaPanel.add(cellPhoneLabel);
		
		JLabel cityLabel = new JLabel("City:");
		cityLabel.setBounds(10, 61, 46, 14);
		viewAreaPanel.add(cityLabel);
		
		JLabel stateLabel = new JLabel("State:");
		stateLabel.setBounds(269, 61, 55, 14);
		viewAreaPanel.add(stateLabel);
		
		JLabel emailAddressLabel = new JLabel("email Address:");
		emailAddressLabel.setBounds(454, 61, 88, 14);
		viewAreaPanel.add(emailAddressLabel);
		
		JLabel zipCodeLabel = new JLabel("Zip Code");
		zipCodeLabel.setBounds(10, 86, 77, 14);
		viewAreaPanel.add(zipCodeLabel);
		
		JLabel unitNumberLabel = new JLabel("Unit Number:");
		unitNumberLabel.setBounds(269, 86, 88, 14);
		viewAreaPanel.add(unitNumberLabel);
		
		JLabel siteDescriptionLabel = new JLabel("Site Description:");
		siteDescriptionLabel.setBounds(454, 86, 103, 14);
		viewAreaPanel.add(siteDescriptionLabel);
		
		lastNameTextField = new JTextField();
		lastNameTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				checkField(lastNameTextField);
			}
		});
		lastNameTextField.setEditable(false);
		lastNameTextField.setBounds(140, 10, 103, 20);
		lastNameTextField.setColumns(10);
		viewAreaPanel.add(lastNameTextField);
		
		firstNameTextField = new JTextField();
		firstNameTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				checkField(firstNameTextField);
			}
		});
		firstNameTextField.setEditable(false);
		firstNameTextField.setBounds(356, 10, 88, 20);
		firstNameTextField.setColumns(10);
		viewAreaPanel.add(firstNameTextField);
		
		phoneNumberTextField = new JTextField();
		phoneNumberTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				checkField(phoneNumberTextField);
			}
		});
		phoneNumberTextField.setEditable(false);
		phoneNumberTextField.setBounds(552, 10, 128, 20);
		phoneNumberTextField.setColumns(10);
		viewAreaPanel.add(phoneNumberTextField);
		
		streetAddressTextField = new JTextField();
		streetAddressTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				checkField(streetAddressTextField);
			}
		});
		streetAddressTextField.setEditable(false);
		streetAddressTextField.setBounds(140, 35, 184, 20);
		streetAddressTextField.setColumns(10);
		viewAreaPanel.add(streetAddressTextField);
		
		cellPhoneTextField = new JTextField();
		cellPhoneTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				checkField(cellPhoneTextField);
			}
		});
		cellPhoneTextField.setEditable(false);
		cellPhoneTextField.setBounds(552, 35, 128, 20);
		cellPhoneTextField.setColumns(10);
		viewAreaPanel.add(cellPhoneTextField);
		
		cityTextField = new JTextField();
		cityTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				checkField(cityTextField);
			}
		});
		cityTextField.setEditable(false);
		cityTextField.setBounds(140, 58, 103, 20);
		cityTextField.setColumns(10);
		viewAreaPanel.add(cityTextField);
		
		stateTextField = new JTextField();
		stateTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				checkField(stateTextField);
			}
		});
		stateTextField.setEditable(false);
		stateTextField.setBounds(356, 58, 88, 20);
		stateTextField.setColumns(10);
		viewAreaPanel.add(stateTextField);
		
		emailAddressTextField = new JTextField();
		emailAddressTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				checkField(emailAddressTextField);
			}
		});
		emailAddressTextField.setEditable(false);
		emailAddressTextField.setBounds(552, 61, 128, 20);
		emailAddressTextField.setColumns(10);
		viewAreaPanel.add(emailAddressTextField);
		
		zipCodeTextField = new JTextField();
		zipCodeTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				checkField(zipCodeTextField);
			}
		});
		zipCodeTextField.setEditable(false);
		zipCodeTextField.setBounds(140, 83, 103, 20);
		zipCodeTextField.setColumns(10);
		viewAreaPanel.add(zipCodeTextField);
		
		unitNumberTextField = new JTextField();
		unitNumberTextField.setEditable(false);
		unitNumberTextField.setBounds(356, 83, 88, 20);
		unitNumberTextField.setColumns(10);
		viewAreaPanel.add(unitNumberTextField);
		
		siteDescriptionTextField = new JTextField();
		siteDescriptionTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				checkField(siteDescriptionTextField);
			}
		});
		siteDescriptionTextField.setEditable(false);
		siteDescriptionTextField.setBounds(552, 86, 128, 20);
		siteDescriptionTextField.setColumns(10);
		viewAreaPanel.add(siteDescriptionTextField);
		
		JLabel lastNameTextFieldMissingDataLabel = new JLabel("");
		lastNameTextFieldMissingDataLabel.setBounds(253, 10, 18, 14);
		viewAreaPanel.add(lastNameTextFieldMissingDataLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 175, 690, 7);
		peopleManagerFrame.getContentPane().add(separator);
		
		newButton = new JButton("New");
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dataManipulationMode = "New";
				
				if(choice.equals("JobSite")) {
					disableViewItemLabels();
					JobSite js = new JobSite();												
					if(js != null) {
						js.setTitle("Job Site");
						passToConstructor(js);
					}
				}
				else if(choice.equals("WorkOrder")) {
					disableViewItemLabels(); 
					WorkOrder workOrder = new WorkOrder();
					if(workOrder != null) {
						workOrder.setTitle("Work Order");
						passToConstructor(workOrder);
					}
				}
				else {	
					clearAllViewDataFields();
					manageNEDComponents();
					disableSitvCB();						
					enableTextFields();
				}
			}
		});
		newButton.setEnabled(false);
		newButton.setBounds(10, 339, 89, 23);
		peopleManagerFrame.getContentPane().add(newButton);
		
		editButton = new JButton("Edit");
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				dataManipulationMode = "Edit";
				manageNEDComponents();
				enableTextFields();
				selectItemToViewComboBox.setEnabled(false);			
				disableSitvCB();		
			}															
		});																
		editButton.setEnabled(false);
		editButton.setBounds(109, 339, 89, 23);
		peopleManagerFrame.getContentPane().add(editButton);
		
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent e) {
				dataManipulationMode = "Delete";
				manageNEDComponents();
				selectItemToViewComboBox.setEnabled(false);
			}
		});
		deleteButton.setEnabled(false);
		deleteButton.setBounds(208, 339, 89, 23);
		peopleManagerFrame.getContentPane().add(deleteButton);
		
		acceptButton = new JButton("Accept");
		acceptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processData();
			}
		});
		acceptButton.setEnabled(false);
		acceptButton.setBounds(307, 321, 89, 23);
		peopleManagerFrame.getContentPane().add(acceptButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setStartMode();
				setFieldColor();
				selectItemToViewComboBox.setEnabled(true);
			}
		});
		cancelButton.setEnabled(false);
		cancelButton.setBounds(307, 355, 89, 23);
		peopleManagerFrame.getContentPane().add(cancelButton);
		
		exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		exitButton.setBounds(611, 355, 89, 23);
		peopleManagerFrame.getContentPane().add(exitButton);
		
		verificationLabel = new JLabel("");
		verificationLabel.setBounds(406, 343, 195, 14);
		peopleManagerFrame.getContentPane().add(verificationLabel);
		
		selectACustomerLabel = new JLabel("Select a Customer");
		selectACustomerLabel.setEnabled(false);
		selectACustomerLabel.setVisible(false);
		selectACustomerLabel.setBounds(165, 68, 108, 14);
		peopleManagerFrame.getContentPane().add(selectACustomerLabel);
	}
}