package com.example.keiji.contacts;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();
    private String BLUE_COLOR_STRING = "#1A237E";
    private int NUM_REQ_FIELDS = 9;
    private boolean isExisting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button addButton = findViewById(R.id.add_button);
        final Button mainButton = findViewById(R.id.main_button);
        final EditText lastNameEdit = findViewById(R.id.LastNameEdit);
        final EditText firstNameEdit = findViewById(R.id.FirstNameEdit);
        final EditText address1Edit = findViewById(R.id.Address1Edit);
        final EditText address2Edit = findViewById(R.id.Address2Edit);
        final EditText cityEdit = findViewById(R.id.CityEdit);
        final EditText stateEdit = findViewById(R.id.StateEdit);
        final EditText zipEdit = findViewById(R.id.ZipEdit);
        final EditText countryEdit = findViewById(R.id.CountryEdit);
        final EditText phoneNumberEdit = findViewById(R.id.PhoneNumberEdit);
        final EditText emailEdit = findViewById(R.id.EmailEdit);

        // add all elements into an array to be verified
        final EditText[] fieldsArray =
                {lastNameEdit,firstNameEdit,address1Edit,address2Edit,cityEdit,stateEdit,zipEdit,
                countryEdit,phoneNumberEdit,emailEdit };

        // check if an existing contact was tapped from Contacts View
        final int idFromContactView = getIntent().getIntExtra("contactId",-1);
        if(idFromContactView != -1) {
            this.configureUIForExistingContact(idFromContactView, fieldsArray,false,addButton,mainButton);
            this.isExisting = true;
            addButton.setText("Edit");
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isExisting) {

                    try {
                        verifyRequiredFields(fieldsArray);
                    } catch (Exception e) {
                        Log.d(TAG,"Exception thrown when adding contact: " + e);
                        Toast toast = Toast.makeText(MainActivity.this, "Please fill in all required fields",Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                        Contact contact = new Contact(
                                lastNameEdit.getText().toString(),
                                firstNameEdit.getText().toString(),
                                address1Edit.getText().toString(),
                                address2Edit.getText().toString(),
                                cityEdit.getText().toString(),
                                stateEdit.getText().toString(),
                                zipEdit.getText().toString(),
                                countryEdit.getText().toString(),
                                phoneNumberEdit.getText().toString(),
                                emailEdit.getText().toString()
                        );
                        // attempt to add to database
                        try {
                            DatabaseHandler db = new DatabaseHandler(MainActivity.this);
                            db.getWritableDatabase();
                            db.addContact(contact);
                            Toast.makeText(MainActivity.this,"Contact added",Toast.LENGTH_SHORT).show();
                            goToContactsListActivity();
                        } catch (Exception e) {
                            Log.d(TAG, "Error adding new contact to database: "+ e);
                            Toast.makeText(MainActivity.this,"Error adding contact to Database",Toast.LENGTH_LONG).show();
                        }
                } else {
                    // USER IS TRYING TO EDIT CONTACT!
                    Log.d(TAG,"Attempting to edit contact");
                    configureUIForExistingContact(idFromContactView,fieldsArray,true,addButton,mainButton);
                }
            }
        });

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Going to Contacts view");
                goToContactsListActivity();
            }
        });
    }

    /**
     * Verify all the fields required for
     * contact creation are populated.
     *
     * @throws Exception: thrown if a required field is found
     */
    private void verifyRequiredFields(EditText[] fields) throws Exception{

        Log.d(TAG,"Verifying Required Contact Fields");

        for (int i = 0; i < fields.length; i++) {
            // skip verification for address 2 (not required)
            if (i == 3) {
                continue;
            }

            String value = fields[i].getText().toString();
            if (value.matches("")) {
                throw new Exception(" Field cannot be empty: " + fields[i].getId());
            }
        }
    }

    /**
     * Show activity for displaying Listview
     * or existing contacts
     */
    private void goToContactsListActivity() {

        Intent intent = new Intent(MainActivity.this,ViewContacts.class);
        startActivity(intent);
    }


    /**
     * Configure the MainActivity UI depending
     * on the current state of existing Contact: Add, Edit, Delete, etc
     *
     * @param contactId: table id for the current contact
     * @param fields: reference to the Activity's UI elements
     * @param isEditing: is Contact being modified
     * @param actionButton: button for confirming an edit to the contact
     * @param mainButton: Back to Contacts List, or Cancel Edit
     */
    private void configureUIForExistingContact(final int contactId, final EditText[] fields, boolean isEditing, Button actionButton, Button mainButton) {

        DatabaseHandler db = new DatabaseHandler(this);

        Contact existingContact = db.getContact(contactId);

        Log.d(TAG, "Configuring UI for existing Contact: "+ existingContact.get_Last_Name());
        fields[0].setText(existingContact.get_Last_Name());
        fields[1].setText(existingContact.get_First_Name());
        fields[2].setText(existingContact.get_Address_1());
        fields[3].setText(existingContact.get_Address_2());
        if (existingContact.get_Address_2().matches("")) {
            fields[3].setHint("");
        }
        fields[4].setText(existingContact.get_City());
        fields[5].setText(existingContact.get_State());
        fields[6].setText(existingContact.get_Zip());
        fields[7].setText(existingContact.get_Country());
        fields[8].setText(existingContact.get_Phone_Number());
        fields[9].setText(existingContact.get_Email());

        // enable or disable fields based on if editing contact
        for (int i = 0; i < fields.length; i++) {
            fields[i].setEnabled(isEditing);
            fields[i].setTextColor(Color.parseColor(BLUE_COLOR_STRING));
        }

        // configure for editing
        if (isEditing) {
            actionButton.setText("Save");
            mainButton.setText("Cancel");

            // In 'Edit' mode, show hint for Address 2 if empty.
            if (existingContact.get_Address_2().matches("")) {
                fields[3].setHint("Address 2");
            }

            // show 'Delete' button
            Button deleteButton = findViewById(R.id.delete_button);
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Attempting to delete contact");
                    try {
                        saveContactChanges(contactId, fields, true);
                        Toast.makeText(getApplicationContext(),"Contact Deleted",Toast.LENGTH_LONG).show();
                        goToContactsListActivity();
                    } catch (Exception e) {
                        Log.d(TAG, "Error deleting contact");
                        Toast.makeText(MainActivity.this,"Unable to delete contact",Toast.LENGTH_LONG).show();
                    }
                }
            });

            // Action for Cancel
            mainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(getIntent());
                }
            });

            // Action for Save
            actionButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // Save Edits to the database, id = contactId
                    try {
                        saveContactChanges(contactId, fields,false);
                        finish();
                        startActivity(getIntent());
                        Toast.makeText(MainActivity.this,"Contact Updated", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.d(TAG, "Error when updating contact: "+ e);
                        Toast.makeText(MainActivity.this,"Error updating Contact",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    /**
     * Modify or delete an existing contact
     *
     * @param contactId: contact's table id
     * @param fields: fields for contact data
     * @param isDelete: bool designating if user trying to delete
     * @throws Exception: thrown from database helper.
     */
    private void saveContactChanges(int contactId, EditText[] fields, boolean isDelete) throws Exception {

        DatabaseHandler db = new DatabaseHandler(MainActivity.this);
        String lastName = fields[0].getText().toString();
        String firstName = fields[1].getText().toString();
        String address1 = fields[2].getText().toString();
        String address2 = fields[3].getText().toString();
        String city = fields[4].getText().toString();
        String state = fields[5].getText().toString();
        String zip = fields[6].getText().toString();
        String country = fields[7].getText().toString();
        String phoneNumber = fields[8].getText().toString();
        String email = fields[9].getText().toString();

        Contact updatedContact = new Contact(
                contactId,
                lastName,
                firstName,
                address1,
                address2,
                city,
                state,
                zip,
                country,
                phoneNumber,
                email
        );

        if (!isDelete) {
            db.modifyContact(updatedContact);
        } else {
            db.deleteContact(updatedContact);
        }
    }
}
