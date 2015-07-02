package me.michaelkrauty.PVPManager;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

/**
 * Created on 7/1/2015.
 *
 * @author michaelkrauty
 */
public class YamlFile {

    public final Main main;
    public final YamlConfiguration data = new YamlConfiguration();
    public final File dataFile;

    public YamlFile(Main main, File file) {
        this(main, file, false);
    }

    public YamlFile(Main main, File file, boolean copyDefault) {
        this.main = main;
        this.dataFile = file;
        if (!copyDefault)
            check();
        else {
            if (!file.exists()) {
                // copied code ftw

                InputStream inputStream = null;
                OutputStream outputStream = null;

                try {
                    // read this file into InputStream
                    inputStream = main.getResource(file.getName());

                    // write the inputStream to a FileOutputStream
                    outputStream = new FileOutputStream(file, true);

                    int read = 0;
                    byte[] bytes = new byte[1024];

                    while ((read = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, read);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
        load();
    }

    public void check() {
        if (!dataFile.exists())
            try {
                dataFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void load() {
        try {
            data.load(dataFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            data.save(dataFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAsync() {
        main.getServer().getScheduler().scheduleAsyncDelayedTask(main, new Runnable() {
            public void run() {
                save();
            }
        });
    }
}
