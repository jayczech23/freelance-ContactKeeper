package com.example.keiji.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.keiji.contacts.Contact;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keiji on 3/24/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private String TAG = this.getClass().getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static  final  String DATABASE_NAME = "Contacts";
    private static final String TABLE_CONTACTS = "contacts";
    private static Context appContext;

    // columns
    private static final String Key_ID = "id";
    private static final String Key_LASTNAME = "Last_Name";
    private static final String Key_FIRSTNAME = "First_Name";
    private static final String Key_ADDRESS1 = "Address_1";
    private static final String Key_ADDRESS2 = "Address_2";
    private static final String Key_CITY = "City";
    private static final String Key_STATE = "State";
    private static final String Key_ZIP = "Zip";
    private static final String Key_COUNTRY = "Country";
    private static final String Key_PHONENUMBER = "Phone_Number";
    private static final String Key_EMAIL = "Email";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.appContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS +
                "("
                + Key_ID + " INTEGER PRIMARY KEY,"
                + Key_LASTNAME + " TEXT," +
                Key_FIRSTNAME + " TEXT,"+
                Key_ADDRESS1 + " TEXT,"+
                Key_ADDRESS2 + " TEXT,"+
                Key_CITY + " TEXT,"+
                Key_STATE + " TEXT,"+
                Key_ZIP + " TEXT,"+
                Key_COUNTRY + " TEXT,"+
                Key_PHONENUMBER + " TEXT,"+
                Key_EMAIL + " TEXT"
                +")";

        Log.d(TAG, "queryString: " + CREATE_CONTACTS_TABLE );
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    //Add contact
    void addContact(Contact contact){

        if (contact.get_Address_2() == null) {
            contact.set_Address_2("?");
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_LASTNAME, contact.get_Last_Name());
        values.put(Key_FIRSTNAME, contact.get_First_Name());
        values.put(Key_ADDRESS1, contact.get_Address_1());
        values.put(Key_ADDRESS2, contact.get_Address_2());
        values.put(Key_CITY, contact.get_City());
        values.put(Key_STATE, contact.get_State());
        values.put(Key_ZIP, contact.get_Zip());
        values.put(Key_COUNTRY, contact.get_Country());
        values.put(Key_PHONENUMBER, contact.get_Phone_Number());
        values.put(Key_EMAIL, contact.get_Email());

        Log.d(TAG, "Adding " + contact.get_Last_Name() + ", " + contact.get_First_Name() + " to table: " + TABLE_CONTACTS);

        try {
            db.insert(TABLE_CONTACTS, null, values);
            db.close();
        } catch (Exception e) {
            Log.d(TAG,"Error inserting new contact into SQLite DB!: " + e);
            //todo: show a toast telling user something went wrong.
            if (this.appContext != null) {
                Toast.makeText(this.appContext,"Something went wrong, please try again.",Toast.LENGTH_LONG);
            }
        }

    }

    //extrapolating a contact
//    Contact getContact(int id) {
//        SQLiteDatabase db =this.getReadableDatabase();
//        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {Key_ID, Key_LASTNAME}, Key_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
//        if(cursor!=null) {
//            cursor.moveToFirst();
//        }
////        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
//        // TODO: put in for loop to loop over cursor contents
//        String[] values = new String[cursor.getCount()];
//        int contact_id = 0;
//            for (int i = 0; i < cursor.getCount(); i++) {
//
//                // get id
//                if (i == 0) {
//                    contact_id = Integer.parseInt(cursor.getString(i));
//                }
//
//                // all other iterations
//                values[i] = cursor.getString(i);
//
//            }
//
//        Contact contact = new Contact(contact_id, values[0],values[1],values[2],values[3],values[4],values[5], values[6],values[7],values[8],values[9]);
//
//        return contact;
//    }

    Contact getContact(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        String queryString = "SELECT * FROM "+ TABLE_CONTACTS+ " WHERE ID="+id;
        Cursor cursor = db.rawQuery(queryString,null);
        cursor.moveToFirst();

        String lastName = cursor.getString(1);
        String firstName = cursor.getString(2);
        String address1 = cursor.getString(3);
        String address2 = cursor.getString(4);
        String city = cursor.getString(5);
        String state = cursor.getString(6);
        String zip = cursor.getString(7);
        String country = cursor.getString(8);
        String phoneNumber = cursor.getString(9);
        String email = cursor.getString(10);
        cursor.close();

        Contact existingContact = new Contact(lastName,firstName,address1,address2,city,state,zip,country,phoneNumber,email);

        return existingContact;
    }

    //Contact extrapolation
    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> contactList = new ArrayList<>();
        // All queries
        String selectQuery = "SELECT * FROM "+TABLE_CONTACTS+ " ORDER BY "+Key_LASTNAME+" COLLATE NOCASE ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            do {
                Contact contact =new Contact();
                contact.set_id(Integer.parseInt(cursor.getString(0)));
                contact.set_Last_Name(cursor.getString(1));
                contact.set_First_Name(cursor.getString(2));
                contact.set_Address_1(cursor.getString(3));
                contact.set_Address_2(cursor.getString(4));
                contact.set_City(cursor.getString(5));
                contact.set_State(cursor.getString(6));
                contact.set_Zip(cursor.getString(7));
                contact.set_Country(cursor.getString(8));
                contact.set_Phone_Number(cursor.getString(9));
                contact.set_Email(cursor.getString(10));
                contactList.add(contact);
            } while(cursor.moveToNext());
        }
        return contactList;
    }

    //Modify Contact
    public int modifyContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_LASTNAME, contact.get_Last_Name());
        values.put(Key_FIRSTNAME, contact.get_First_Name());
        values.put(Key_ADDRESS1, contact.get_Address_1());
        values.put(Key_ADDRESS2, contact.get_Address_2());
        values.put(Key_CITY, contact.get_City());
        values.put(Key_STATE, contact.get_State());
        values.put(Key_ZIP, contact.get_Zip());
        values.put(Key_COUNTRY, contact.get_Country());
        values.put(Key_PHONENUMBER, contact.get_Phone_Number());
        values.put(Key_EMAIL, contact.get_Email());
        return db.update(TABLE_CONTACTS, values, Key_ID + " = ?", new String[] {String.valueOf(contact.get_id())});
//        db.close();
    }

    //Remove Contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, Key_ID + " = ?", new String[]{String.valueOf(contact.get_id())});
        db.close();
    }

    //Total number of contacts
    public int getContactsTotal() {
        String totalQuery = " SELECT * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(totalQuery, null);
        cursor.close();
        return cursor.getCount();
    }
}