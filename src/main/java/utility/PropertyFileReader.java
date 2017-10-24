package utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyFileReader {

  Properties props = null;
  String path=null;

  /**
   * Class constructor
   */
  public PropertyFileReader(String propertyFileLocation) {

	try {
		path=propertyFileLocation;
	  FileInputStream fis = new FileInputStream(path);
	  props = new Properties();
	  props.load(fis);
	  fis.close();
	} catch (Exception ex) {
	  ex.printStackTrace();
	}
  }

  /**
   * Method is used to get value of the key from application.properties file
   *
   * @param key - key name
   * @return value
   */
  public String getValue(String key) 
  {
	return props.getProperty(key);
  }

  /**
   * Method is used to set key and values in the application.properties file
   *
   * @param key - key name
   * @param value - key value
   */
  public void setValue(String key, String value) {

	try {

	  props.setProperty(key, value);
	  props.store(new FileOutputStream(path), null);

	} catch (FileNotFoundException e) {
	  e.printStackTrace();
	} catch (IOException e) {
	  e.printStackTrace();
	}

  }

  /**
   * Method is used to remove key and value from application.properties file
   *
   * @param keyName - key name
   */
  public void removeKey(String keyName) {
	props.remove(keyName);
  }

}
