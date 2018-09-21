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
  private static final String READ_CONTACTS = Manifest.permission.READ_CONTACTS;
  private static final String WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;
  private static final int CONTACT_CODE = 0;
  private CallbackContext callbackContext;
  private Context context;

  @Override
  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) {
    try {
      this.callbackContext = callbackContext;
      if(action.equals("getContacts")) {
        if(cordova.hasPermission(READ_CONTACTS)) {
          this.retrieveContact();
        } else {
          this.requestContactPermission(CONTACT_CODE);
        }
      }
    } catch (Exception e) {
      this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "error:" + e.getMessage()));
    }
    return true;
  }

  private void requestContactPermission(int requestCode) {
    cordova.requestPermissions(this, requestCode, new String[] {READ_CONTACTS, WRITE_CONTACTS});
  }

  /**
   * callback of Cordova requestPermission method
   * @param requestCode
   * @param permissions
   * @param grantResults
   * @throws JSONException
   */
  @Override
  public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
    try {
      for(int result : grantResults) {
        if(result == PackageManager.PERMISSION_DENIED) {
          JSONObject obj = new JSONObject();
          obj.put("message", "user denied permission");
          obj.put("isUserGrantedPermission", false);
          this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, obj.toString()));
          return;
        }
      }
      switch (requestCode) {
        case CONTACT_CODE:
          this.retrieveContact();
          break;
        default:
          JSONObject obj = new JSONObject();
          obj.put("message", "invalid request code");
          obj.put("isUserGrantedPermission", false);
          this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, obj.toString()));
          break;
      }
    } catch (JSONException e) {
       try {
         JSONObject obj = new JSONObject();
         obj.put("message", "error occurs while calling onRequestPermissionResult");
         obj.put("isUserGrantedPermission", true);
         this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, obj.toString()));
       } catch (JSONException je) {
         this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "error:" + je.getMessage()));
       }
    }
  }

  public void retrieveContact() throws JSONException {
    try {
      ContactResultWrapper contactResultWrapper = this.importContacts();
      if(contactResultWrapper.importDeviceContactResult.errorOccurred) {
        JSONObject obj = new JSONObject();
        obj.put("message", "error retrieving contacts:" + contactResultWrapper.getImportDeviceContactResult().message);
        obj.put("isUserGrantedPermission", true);
        this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, obj.toString()));
      } else {
        List<ContactData> contactDataList = contactResultWrapper.getContactData();
        JSONObject obj = new JSONObject();
        obj.put("message", "success retreiving contacts");
        obj.put("isUserGrantedPermission", true);
        JSONArray contacts = new JSONArray();
        for(ContactData contactData : contactDataList) {
          JSONObject tempObj = new JSONObject();
          tempObj.put("contactID", contactData.getContactID());
          tempObj.put("contactName", contactData.getName());
          contacts.put(tempObj);
        }
        obj.put("contacts", contacts);
        this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, obj));
      }
    } catch (Exception e) {
      JSONObject obj = new JSONObject();
      obj.put("message", "error while building the contacts, please try again");
      obj.put("isUserGrantedPermission", true);
      this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "error"));
    }
  }

  private String fixNull(String inputString) {
    if (inputString == null) {
      return "";
    }
    return inputString;
  }

  private ContactResultWrapper importContacts() {
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
            String contactDisplayName = this.fixNull(cursor.getString(DISPLAY_NAME));
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