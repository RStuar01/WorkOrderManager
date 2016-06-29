package PresentationLayer;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import DatabaseLayer.DAOFactory;
import DatabaseLayer.WriterDAO;
import DatabaseLayer.ReaderDAO;

/**
 * Class Name:					AddNewPeopleDialog
 * Description:					This class is called from the JobSite and WorkOrder dialogs
 * 								to add Customers and Contact People during the JobSite and
 * 								WorkOrder creation stages, allowing for completion of the 
 * 								process without the loss of data.
 * @author Richard Stuart
 * @created Sunday, 5,29,16
 */
@SuppressWarnings("serial")
public class AddNewPeopleDialog extends JDialog {

	public JDialog addNewPeopleDialogFrame;
	private JTextField addPersonLastNameTextField;
	private JTextField addPersonFirstNameTextField;
	private JTextField addStreetAddressTextField;
	private JTextField addCityTextField;
	private JTextField addStateTextField;
	private JTextField addZipCodeTextField;
	private JTextField addUnitNumberTextField;
	private JTextField addSiteDescriptionTextField;
	private JTextField addPhoneNumberTextField;
	private JTextField addCellPhoneTextField;
	private JTextField addEmailAddressTextField;
	private JLabel addPersonLastNameLabel;
	private JLabel plnErrorLabel;
	private JLabel pfnErrorLabel;
	private JLabel saErrorLabel;
	private JLabel cErrorLabel;
	private JLabel sErrorLabel;
	private JLabel zcErrorLabel;
	private JLabel unErrorLabel;
	private JLabel sdErrorLabel;
	private JLabel pnErrorLabel;
	private JLabel cpErrorLabel;
	private JLabel eaErrorLabel;
	
	WriterDAO writerDAO;
	ReaderDAO readerDAO;
	String newChoice;
	
	public static String ownerName;
	private boolean dataEntered = true;
	
	//Get/set accessors/mutators
	public static void setOwnerName(String ownerName) {
		AddNewPeopleDialog.ownerName = ownerName;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	private void setDataEntered(boolean value) {
		dataEntered = value;
	}
	
	private boolean getDataEntered() {
		return dataEntered;
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
	 * Constructor when the "Add New Person" message is chosen in JobSiteManagerDialog or WorkOrderManagerDialog.
	 * @param jobSiteManagerDialogFrame
	 */
	public AddNewPeopleDialog(Dialog owner, String choice) {
		if(choice.equals("Customer")) {
			newChoice = "Customer";
			addNewPeopleDialogFrame = new JDialog(owner, "Add New Customer", Dialog.ModalityType.APPLICATION_MODAL);
		}
		else {
			newChoice = "ContactPerson";
			addNewPeopleDialogFrame = new JDialog(owner, "Add New Contact Person", Dialog.ModalityType.APPLICATION_MODAL);
		}
		initialize();
		writerDAO = DAOFactory.getWriterDAO();
		readerDAO = DAOFactory.getReaderDAO();
	}
	
	/**
	 * This method checks a text field to see if data is present and either returns the 
	 * data, or if is it missing, sets the associated label to notify the user.
	 * @param name							JTextField name to be checked.
	 * @param labelName						JLabel to be set if data is not present.
	 * @return dataItem						Data obtained from the JTextField.
	 */
	private String checkData(JTextField name, JLabel labelName) {
		Font font = new Font("ariel", Font.BOLD, 25);
		String dataItem = name.getText().trim();
		
		if(dataItem.length() == 0) {
			labelName.setText("*");
			labelName.setForeground(Color.RED);
			labelName.setFont(font);
			setDataEntered(false);
		}
		else {
			labelName.setText("");
		}
		
		return dataItem;
	}
	
	/**
	 * This method manages the process of creating a new People object by calling the necessary
	 * methods to create the object as well as all the set mutators to set the associated field
	 * values in this class as well as in other classes.
	 * @param newChoice						String variable used to identify the Object type. 
	 * @param lastName						String variable containing the People objects last name.
	 * @param firstName						String variable containing the People objects first name.
	 * @param streetAddress					String variable containing the People objects street address.
	 * @param city							String variable containing the People objects city.
	 * @param state							String variable containing the People objects state.
	 * @param zipCode						String variable containing the People objects zip code.
	 * @param unitNumber					String variable containing the People objects unit number.
	 * @param siteDescription				String variable containing the People objects site description.
	 * @param phoneNumber					String variable containing the People objects phone number.
	 * @param cellPhone						String variable containing the People objects cell phone number.
	 * @param emailAddress					String variable containing the People objects email address.
	 */
	private void makePerson(String newChoice, String lastName, String firstName, String streetAddress,
			String city, String state, String zipCode, String unitNumber, String siteDescription,
			String phoneNumber, String cellPhone, String emailAddress) {
		
		writerDAO.manageNewPersonCreation(newChoice, lastName, firstName, streetAddress, city, state,
				zipCode, unitNumber, siteDescription, phoneNumber, cellPhone, emailAddress);
		
		String idNumber = readerDAO.getPersonIDNumber(newChoice, lastName, firstName);
		String callerName = getOwnerName();
		
		if(callerName.equalsIgnoreCase("WorkOrderManagerDialog")) {
			if(newChoice.equalsIgnoreCase("Customer")) {
				WorkOrderManagerDialog.setCustIDNumber(idNumber);
			}
			else {
				WorkOrderManagerDialog.setContIDNumber(idNumber);
			}
		}
		else if(callerName.equalsIgnoreCase("JobSiteManagerDialog")) {
			if(newChoice.equalsIgnoreCase("Customer")) {
				JobSiteManagerDialog.setCustIDNumber(idNumber);
			}
			else {
				JobSiteManagerDialog.setContIDNumber(idNumber);
			}
		}
		
		addNewPeopleDialogFrame.dispose();
	}
	
	/**
	 * This method manages the data collection process by calling a method to obtain the data,
	 * as well as calling a method to create the new object if all the data is entered.
	 */
	private void manageData() {
		
		setDataEntered(true);
		
		String lastName = checkData(addPersonLastNameTextField, plnErrorLabel);
		String firstName = checkData(addPersonFirstNameTextField, pfnErrorLabel);
		String streetAddress = checkData(addStreetAddressTextField, saErrorLabel);
		String city = checkData(addCityTextField, cErrorLabel);
		String state = checkData(addStateTextField, sErrorLabel);
		String zipCode = checkData(addZipCodeTextField, zcErrorLabel);
		String siteDescription = checkData(addSiteDescriptionTextField, sdErrorLabel);
		String phoneNumber = checkData(addPhoneNumberTextField, pnErrorLabel);
		String cellPhone = checkData(addCellPhoneTextField, cpErrorLabel);
		String emailAddress = checkData(addEmailAddressTextField, eaErrorLabel);
		
		String unitNumber = addUnitNumberTextField.getText().trim();
		if(unitNumber.equals("") || unitNumber.equalsIgnoreCase("NA")){
			unitNumber = "NULL";
		}
		
		System.out.println("ANPD dataEntered: " + getDataEntered());
		if(getDataEntered()) {
			makePerson(newChoice, lastName, firstName, streetAddress, city, state, zipCode,
					unitNumber, siteDescription, phoneNumber, cellPhone, emailAddress);
		}
	}
	
	/**
	 * Create the dialog.
	 */
	private void initialize() {
		addNewPeopleDialogFrame.setBounds(100, 100, 675, 425);
		addNewPeopleDialogFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		addNewPeopleDialogFrame.setModalityType(ModalityType.APPLICATION_MODAL);
		addNewPeopleDialogFrame.getContentPane().setLayout(null);
		
		addPersonLastNameLabel = new JLabel();
		addPersonLastNameLabel.setText("Last Name:");
		addPersonLastNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		addPersonLastNameLabel.setBounds(10, 58, 131, 14);
		addNewPeopleDialogFrame.getContentPane().add(addPersonLastNameLabel);
		
		JLabel addPersonFirstNameLabel = new JLabel();
		addPersonFirstNameLabel.setText("First Name:");
		addPersonFirstNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		addPersonFirstNameLabel.setBounds(343, 58, 131, 14);
		addNewPeopleDialogFrame.getContentPane().add(addPersonFirstNameLabel);
		
		addPersonLastNameTextField = new JTextField();
		addPersonLastNameTextField.setBounds(151, 55, 140, 20);
		addNewPeopleDialogFrame.getContentPane().add(addPersonLastNameTextField);
		addPersonLastNameTextField.setColumns(10);
		
		addPersonFirstNameTextField = new JTextField();
		addPersonFirstNameTextField.setBounds(484, 55, 140, 20);
		addNewPeopleDialogFrame.getContentPane().add(addPersonFirstNameTextField);
		addPersonFirstNameTextField.setColumns(10);
		
		JLabel addStreetAddressLabel = new JLabel("Street Address:");
		addStreetAddressLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		addStreetAddressLabel.setBounds(10, 105, 131, 14);
		addNewPeopleDialogFrame.getContentPane().add(addStreetAddressLabel);
		
		addStreetAddressTextField = new JTextField();
		addStreetAddressTextField.setBounds(151, 102, 250, 20);
		addNewPeopleDialogFrame.getContentPane().add(addStreetAddressTextField);
		addStreetAddressTextField.setColumns(10);
		
		JLabel addCityLabel = new JLabel("City:");
		addCityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		addCityLabel.setBounds(10, 152, 46, 14);
		addNewPeopleDialogFrame.getContentPane().add(addCityLabel);
		
		addCityTextField = new JTextField();
		addCityTextField.setBounds(66, 149, 140, 20);
		addNewPeopleDialogFrame.getContentPane().add(addCityTextField);
		addCityTextField.setColumns(10);
		
		JLabel addStateLabel = new JLabel("State:");
		addStateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		addStateLabel.setBounds(259, 152, 46, 14);
		addNewPeopleDialogFrame.getContentPane().add(addStateLabel);
		
		addStateTextField = new JTextField();
		addStateTextField.setBounds(315, 149, 86, 20);
		addNewPeopleDialogFrame.getContentPane().add(addStateTextField);
		addStateTextField.setColumns(10);
		
		JLabel addZipCodeLabel = new JLabel("Zip Code:");
		addZipCodeLabel.setBounds(458, 152, 70, 14);
		addNewPeopleDialogFrame.getContentPane().add(addZipCodeLabel);
		
		addZipCodeTextField = new JTextField();
		addZipCodeTextField.setBounds(538, 149, 86, 20);
		addNewPeopleDialogFrame.getContentPane().add(addZipCodeTextField);
		addZipCodeTextField.setColumns(10);
		
		JLabel addUnitNumberLabel = new JLabel("Unit Number:");
		addUnitNumberLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		addUnitNumberLabel.setBounds(10, 203, 86, 14);
		addNewPeopleDialogFrame.getContentPane().add(addUnitNumberLabel);
		
		addUnitNumberTextField = new JTextField();
		addUnitNumberTextField.setBounds(120, 200, 86, 20);
		addNewPeopleDialogFrame.getContentPane().add(addUnitNumberTextField);
		addUnitNumberTextField.setColumns(10);
		
		JLabel addSiteDescriptionLabel = new JLabel("Site Description:");
		addSiteDescriptionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		addSiteDescriptionLabel.setBounds(259, 203, 100, 14);
		addNewPeopleDialogFrame.getContentPane().add(addSiteDescriptionLabel);
		
		addSiteDescriptionTextField = new JTextField();
		addSiteDescriptionTextField.setBounds(369, 200, 105, 20);
		addNewPeopleDialogFrame.getContentPane().add(addSiteDescriptionTextField);
		addSiteDescriptionTextField.setColumns(10);
		
		JLabel addPhoneNumberLabel = new JLabel("Phone Number:");
		addPhoneNumberLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		addPhoneNumberLabel.setBounds(10, 264, 100, 14);
		addNewPeopleDialogFrame.getContentPane().add(addPhoneNumberLabel);
		
		addPhoneNumberTextField = new JTextField();
		addPhoneNumberTextField.setBounds(120, 261, 129, 20);
		addNewPeopleDialogFrame.getContentPane().add(addPhoneNumberTextField);
		addPhoneNumberTextField.setColumns(10);
		
		JLabel addCellPhoneLabel = new JLabel("Cell Phone:");
		addCellPhoneLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		addCellPhoneLabel.setBounds(259, 264, 86, 14);
		addNewPeopleDialogFrame.getContentPane().add(addCellPhoneLabel);
		
		addCellPhoneTextField = new JTextField();
		addCellPhoneTextField.setBounds(369, 261, 129, 20);
		addNewPeopleDialogFrame.getContentPane().add(addCellPhoneTextField);
		addCellPhoneTextField.setColumns(10);
		
		JLabel addEmailAddressLabel = new JLabel("email Address:");
		addEmailAddressLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		addEmailAddressLabel.setBounds(10, 303, 100, 14);
		addNewPeopleDialogFrame.getContentPane().add(addEmailAddressLabel);
		
		addEmailAddressTextField = new JTextField();
		addEmailAddressTextField.setBounds(120, 300, 129, 20);
		addNewPeopleDialogFrame.getContentPane().add(addEmailAddressTextField);
		addEmailAddressTextField.setColumns(10);
		
		JButton addCancelButton = new JButton("Cancel");
		addCancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addNewPeopleDialogFrame.dispose();
			}
		});
		addCancelButton.setBounds(560, 352, 89, 23);
		addNewPeopleDialogFrame.getContentPane().add(addCancelButton);
		
		JButton addAcceptButton = new JButton("Accept");
		addAcceptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manageData();
			}
		});
		addAcceptButton.setBounds(461, 352, 89, 23);
		addNewPeopleDialogFrame.getContentPane().add(addAcceptButton);
		
		plnErrorLabel = new JLabel("");
		plnErrorLabel.setBounds(301, 58, 46, 14);
		addNewPeopleDialogFrame.getContentPane().add(plnErrorLabel);
		
		pfnErrorLabel = new JLabel("");
		pfnErrorLabel.setBounds(624, 58, 25, 14);
		addNewPeopleDialogFrame.getContentPane().add(pfnErrorLabel);
		
		saErrorLabel = new JLabel("");
		saErrorLabel.setBounds(411, 105, 25, 14);
		addNewPeopleDialogFrame.getContentPane().add(saErrorLabel);
		
		cErrorLabel = new JLabel("");
		cErrorLabel.setBounds(216, 152, 25, 14);
		addNewPeopleDialogFrame.getContentPane().add(cErrorLabel);
		
		sErrorLabel = new JLabel("");
		sErrorLabel.setBounds(411, 152, 25, 14);
		addNewPeopleDialogFrame.getContentPane().add(sErrorLabel);
		
		zcErrorLabel = new JLabel("");
		zcErrorLabel.setBounds(624, 152, 25, 14);
		addNewPeopleDialogFrame.getContentPane().add(zcErrorLabel);
		
		unErrorLabel = new JLabel("");
		unErrorLabel.setBounds(216, 203, 25, 14);
		addNewPeopleDialogFrame.getContentPane().add(unErrorLabel);
		
		sdErrorLabel = new JLabel("");
		sdErrorLabel.setBounds(484, 203, 25, 14);
		addNewPeopleDialogFrame.getContentPane().add(sdErrorLabel);
		
		pnErrorLabel = new JLabel("");
		pnErrorLabel.setBounds(259, 264, 25, 14);
		addNewPeopleDialogFrame.getContentPane().add(pnErrorLabel);
		
		cpErrorLabel = new JLabel("");
		cpErrorLabel.setBounds(508, 264, 25, 14);
		addNewPeopleDialogFrame.getContentPane().add(cpErrorLabel);
		
		eaErrorLabel = new JLabel("");
		eaErrorLabel.setBounds(259, 303, 25, 14);
		addNewPeopleDialogFrame.getContentPane().add(eaErrorLabel);

	}
}
