/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.runtime.registry;

import java.io.*;

final class FileUtils {

    static void copyInputStreamToFile(InputStream source, File destination) throws IOException {
        try {
            FileOutputStream output = openOutputStream(destination);
            try {
                copy(source, output);
                output.close();
            } finally {
                closeQuietly(output);
            }
        } finally {
            closeQuietly(source);
        }
    }

    private static FileOutputStream openOutputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canWrite() == false) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file, false);
    }

    private static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output, new byte[104 * 4]);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    private static long copyLarge(InputStream input, OutputStream output, byte[] buffer)
            throws IOException {
        long count = 0;
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    private static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ignored) {
        }
    }
}
