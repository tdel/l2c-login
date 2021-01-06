package kernel.configuration;

import com.google.inject.Inject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private Properties properties;

    @Inject
    public Config() {

    }

    public void load(String _filename) throws IOException {
        this.properties = new Properties();
        this.properties.load(new FileInputStream(_filename));
    }

    public String getString(String _key) {
        return this.properties.getProperty(_key);
    }

    public int getInt(String _key) {
        return Integer.parseInt(this.getString(_key));
    }

    public float getFloat(String _key) {
        return Float.parseFloat(this.getString(_key));
    }

    public double getDouble(String _key) {
        return Double.parseDouble(this.getString(_key));
    }

}
