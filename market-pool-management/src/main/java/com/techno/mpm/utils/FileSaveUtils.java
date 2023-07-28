package com.techno.mpm.utils;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.techno.mpm.exception.DataNotFoundException;

@Component
public class FileSaveUtils {

	private String dir = "E:\\";

	public String addFile(String folderName, MultipartFile multipartFile) {
		try {
			Path dirLocation = getPath(folderName);
			if (multipartFile != null) {
				Files.createDirectories(dirLocation);
				String filename = multipartFile.getOriginalFilename();
				Path filePath = dirLocation.resolve(filename);
				Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				return filePath.toString();
			} else {
				throw new FileNotFoundException("");
			}
		} catch (Exception exception) {
			throw new DataNotFoundException(exception.getMessage());
		}
	}
	

	private Path getPath(String fileName) {
		return Paths.get(this.dir + "\\" + fileName).toAbsolutePath().normalize();
	}
}
