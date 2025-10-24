package com.github.logs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnZipLogFile {

	@SuppressWarnings("resource")
	public static void tounzipArtifact(String zipFilePath, String destDir) throws IOException {
		File dir = new File(destDir);
		if (!dir.exists())
			dir.mkdirs();

		byte[] buffer = new byte[1024];

		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath));
		ZipEntry zipEntry = zis.getNextEntry();

		while (zipEntry != null) {
			File newFile = newFile(dir, zipEntry);

			if (zipEntry.isDirectory()) {
				if (!newFile.isDirectory() && !newFile.mkdirs()) {
					throw new IOException("Failed to create directory " + newFile);
				}
			} else {
				// Create parent directories if necessary
				File parent = newFile.getParentFile();
				if (!parent.isDirectory() && !parent.mkdirs()) {
					throw new IOException("Failed to create directory " + parent);
				}

				// Write file content
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
			}

			zipEntry = zis.getNextEntry();
		}

		zis.closeEntry();
		zis.close();
	}

	// Ensure file paths don't lead outside target directory (security)
	private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());
		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}

		return destFile;
	}

}
