package com.zackehh.rssdemo.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

/**
 * Allows the reading/writing of an object from/to a file
 * inside the application's /data directory. Allows for
 * more efficient storing of objects such as arrays instead
 * of within SharedPreferences
 * 
 * @author Isaac Whitfield
 * @version 31/07/2013
 */
public class WriteObjectFile {

	private Context parent;
	private FileInputStream fileIn;
	private FileOutputStream fileOut;
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	private Object outputObject;

	public WriteObjectFile(Context c){
		parent = c;
	}

	public Object readObject(String fileName){
		try {
			fileIn = parent.getApplicationContext().openFileInput(fileName);
			objectIn = new ObjectInputStream(fileIn);
			outputObject = objectIn.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (objectIn != null) {
				try {
					objectIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return outputObject;
	}

	public void writeObject(Object inputObject, String fileName){
		try {
			fileOut = parent.openFileOutput(fileName, Context.MODE_PRIVATE);
			objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(inputObject);
			fileOut.getFD().sync();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (objectOut != null) {
				try {
					objectOut.close();
				} catch (IOException e) { 
					e.printStackTrace();
				}
			}
		}
	}
}