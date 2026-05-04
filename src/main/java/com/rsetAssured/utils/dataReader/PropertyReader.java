package com.rsetAssured.utils.dataReader;

import com.rsetAssured.utils.logs.LogsManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.Properties;

public class PropertyReader {

    // ✅ الحل هنا: هذا البلوك سيتم تنفيذه أوتوماتيكياً بمجرد استخدام الكلاس
    static {
        loadProperties();
    }

    // Load Properties function to load all data exists on .properties files into system properties
    public static Properties loadProperties() {
        try {
            Properties properties = new Properties();
            Collection<File> propertiesFiles;
            propertiesFiles = FileUtils.listFiles(new File("src/main/resources"), new String[]{"properties"}, true);

            propertiesFiles.forEach(file -> {
                try {
                    properties.load(new FileInputStream(file));
                } catch (Exception e) {
                    LogsManager.error("Error loading properties file:", file.getName(), e.getMessage());
                }
                properties.putAll(System.getProperties());
                System.getProperties().putAll(properties);
            });
            LogsManager.info("Properties files loaded successfully into System Properties");
            return properties;
        } catch (Exception e) {
            LogsManager.error("Error loading properties file", e.getMessage());
            return null;
        }
    }

    public static String getProperty(String key) {
        try {
            // الآن سيبحث في النظام بعد أن تم تحميل الملفات بنجاح
            String value = System.getProperty(key);
            if (value == null) {
                LogsManager.info("Warning: Property key '" + key + "' not found!");
            }
            return value;
        } catch (Exception e) {
            LogsManager.error("Error getting property for key:", key, e.getMessage());
            return "";
        }
    }
}