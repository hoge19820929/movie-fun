package org.superbiz.moviefun.blobstore;

import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;

import java.io.*;
import java.nio.file.Path;
import java.util.Optional;

import static java.lang.String.format;

public class FileStore implements BlobStore {
    @Override
    public void put(Blob blob) throws IOException {
        String coverFileName = format("covers/%s", blob.name);
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
        File file = new File(name);

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
