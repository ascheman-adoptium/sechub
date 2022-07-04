package com.mercedesbenz.sechub.adapter;

import java.io.File;
import java.io.IOException;

import com.mercedesbenz.sechub.commons.TextFileReader;
import com.mercedesbenz.sechub.commons.TextFileWriter;
import com.mercedesbenz.sechub.commons.model.JSONConverter;

/**
 * A file based meta data callback implementation
 *
 * @author Albert Tregnaghi
 *
 */
public class FileStoreAdapterMetaDataCallback implements AdapterMetaDataCallback {

    private File file;

    public FileStoreAdapterMetaDataCallback(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file may not be null!");
        }
        this.file = file;
    }

    @Override
    public void persist(AdapterMetaData metaData) {
        TextFileWriter writer = new TextFileWriter();
        String metaDataJson = JSONConverter.get().toJSON(metaData);
        try {
            writer.save(file, metaDataJson, true);
        } catch (IOException e) {
            throw new IllegalStateException("Was not able to store meta data!", e);
        }

    }

    @Override
    public AdapterMetaData getMetaDataOrNull() {
        if (!file.exists()) {
            return null;
        }
        TextFileReader reader = new TextFileReader();
        try {

            String data = reader.loadTextFile(file);
            if (data == null || data.isEmpty()) {
                return null;
            }
            AdapterMetaData result = JSONConverter.get().fromJSON(AdapterMetaData.class, data);
            return result;

        } catch (IOException e) {
            throw new IllegalStateException("Was not able to load meta data from " + file, e);

        }
    }
}
