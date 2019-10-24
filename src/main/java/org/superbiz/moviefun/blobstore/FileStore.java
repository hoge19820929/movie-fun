package org.superbiz.moviefun.blobstore;

import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;

import java.io.*;
import java.nio.file.Path;
import java.util.Optional;

import static java.lang.String.format;

public class FileStore implements BlobStore {
    private static final String IMAGES_FOLDER = "covers";

    @Override
    public void put(Blob blob) throws IOException {
        String coverFileName = format("%s/%s", IMAGES_FOLDER, blob.name);
        File targetFile = new File(coverFileName);

        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            outputStream.write(IOUtils.toByteArray(blob.inputStream));
        }
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        String coverFileName = format("%s/%s", IMAGES_FOLDER, name);

        File file = new File(coverFileName);

        if (file.exists()) {
            Path coverFilePath = file.toPath();

            String contentType = new Tika().detect(coverFilePath);
            InputStream inputStream = new FileInputStream(file);

            Blob blob = new Blob(name, inputStream, contentType);

            return Optional.of(blob);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void deleteAll() {
    }
}
