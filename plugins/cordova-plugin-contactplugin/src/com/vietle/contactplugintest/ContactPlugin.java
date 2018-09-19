package com.vietle.contactplugintest;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContactPlugin extends CordovaPlugin {
  public static final String READ = Manifest.permission.READ_CONTACTS;
  public static final int READ_CONTACT_CODE = 0;
  private CallbackContext callbackContext;
  private Context context;
  private List<ContactData> contactDatas;

  @Override
  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) {
    this.callbackContext = callbackContext;
    if(action.equals("getContacts")) {
      if(cordova.hasPermission(READ)) {
        this.retrieveContact();
      } else {
        this.getReadPermission(READ_CONTACT_CODE);
      }
    }
    return true;
  }

  private void getReadPermission(int requestCode) {
    cordova.requestPermission(this, requestCode, READ);
  }

  /**
   * callback of Cordova requestPermission method
   * @param requestCode
   * @param permissions
   * @param grantResults
   * @throws JSONException
   */
  @Override
  public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
    for(int result : grantResults) {
      if(result == PackageManager.PERMISSION_DENIED) {
        return;
      }
    }
    switch (requestCode) {
      case READ_CONTACT_CODE:
        this.retrieveContact();
        break;
      default:
        break;
    }
  }

  public PluginResult retrieveContact() {
    PluginResult pluginResult;
    JSONObject contact = new JSONObject();

    try {
      ContactResultWrapper contactResultWrapper = this.importContacts();
//      name.put("contactResult", contactResultWrapper.);
      String receivedContactJSON = this.constructJSON(contactResultWrapper);
      contact.put("name", "viet le");
      contact.put("phone", "404-900-0980");
      pluginResult = new PluginResult(PluginResult.Status.OK, receivedContactJSON);
      this.callbackContext.sendPluginResult(pluginResult);
    }catch (Exception e) {
      pluginResult = new PluginResult(PluginResult.Status.ERROR, "error");
      this.callbackContext.sendPluginResult(pluginResult);
    }
    return pluginResult;
  }

  private String constructJSON(ContactResultWrapper contactResultWrapper) throws Exception{
    JSONObject contactResult = new JSONObject();
    JSONArray jsonArray = new JSONArray();
    List<ContactData> contacts = contactResultWrapper.getContactData();
    ImportDeviceContactResult result = contactResultWrapper.getImportDeviceContactResult();
    JSONObject o = new JSONObject();
    if(!result.isErrorOccurred()) {
      o.put("success", true);
      for(ContactData c : contacts) {
        long id = c.getContactID();
        String name = c.getName();
        JSONObject contactObj = new JSONObject();
        contactObj.put("name", name);
        contactObj.put("id", id);
        jsonArray.put(contactObj);
      }
    } else {
      o.put("success", false);
    }
    contactResult.put("success", o);
    contactResult.put("contacts", jsonArray);
    return contactResult.toString();
  }

  private String fixNull(String inputString) {
    if (inputString == null) {
      return "";
    }
    return inputString;
  }

  public ContactResultWrapper importContacts() {
    List<ContactData> contactDatas = new ArrayList<ContactData>();
    ContactResultWrapper contactResultWrapper = new ContactResultWrapper();
    ImportDeviceContactResult importDeviceContactResult = new ImportDeviceContactResult();
    try
    {
      this.context = this.cordova.getActivity().getApplicationContext();
      ContentResolver cr = this.context.getContentResolver();
      Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[] {ContactsContract.Contacts.IN_VISIBLE_GROUP, ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER},null, null, "upper(" + (ContactsContract.Contacts.DISPLAY_NAME + ") ASC"));
      if (cursor.getCount() > 0)
      {
        final int IN_VISIBLE_GROUP = cursor.getColumnIndex(ContactsContract.Contacts.IN_VISIBLE_GROUP);
        final int DISPLAY_NAME = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        final int _ID = cursor.getColumnIndex(ContactsContract.Contacts._ID);

        while (cursor.moveToNext() == true) {
          String visibleString = fixNull(cursor.getString(IN_VISIBLE_GROUP));
          if (visibleString.equals("1"))
          {
            ContactData contactData = new ContactData();
            long contactID = cursor.getLong(_ID);
            String contactDisplayName = fixNull(cursor.getString(DISPLAY_NAME));
            contactData.setName(contactDisplayName);
            contactData.setContactID(contactID);
            contactDatas.add(contactData);
          }
        }
        importDeviceContactResult.errorOccurred = false;
        importDeviceContactResult.message = "success";
        contactResultWrapper.setContactData(contactDatas);
        contactResultWrapper.setImportDeviceContactResult(importDeviceContactResult);
      }
      cursor.close();
    } catch (Exception e) {
      importDeviceContactResult.errorOccurred = true;
      importDeviceContactResult.message = "GetContact - " + e.getMessage();
      contactResultWrapper.setContactData(null);
      contactResultWrapper.setImportDeviceContactResult(importDeviceContactResult);
    }
    return contactResultWrapper;
  }
}

class ContactData {
  ImportDeviceContactResult importDeviceContactResult;
  String name;
  long contactID;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getContactID() {
    return contactID;
  }

  public void setContactID(long contactID) {
    this.contactID = contactID;
  }

  public ImportDeviceContactResult getImportDeviceContactResult() {
    return importDeviceContactResult;
  }

  public void setImportDeviceContactResult(ImportDeviceContactResult importDeviceContactResult) {
    this.importDeviceContactResult = importDeviceContactResult;
  }
}

class ImportDeviceContactResult {
  boolean errorOccurred;
  String message;

  public boolean isErrorOccurred() {
    return errorOccurred;
  }

  public void setErrorOccurred(boolean errorOccurred) {
    this.errorOccurred = errorOccurred;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}

class ContactResultWrapper {
  List<ContactData> contactData;
  ImportDeviceContactResult importDeviceContactResult;

  public List<ContactData> getContactData() {
    return contactData;
  }

  public void setContactData(List<ContactData> contactData) {
    this.contactData = contactData;
  }

  public ImportDeviceContactResult getImportDeviceContactResult() {
    return importDeviceContactResult;
  }

  public void setImportDeviceContactResult(ImportDeviceContactResult importDeviceContactResult) {
    this.importDeviceContactResult = importDeviceContactResult;
  }
}