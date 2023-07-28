package com.techno.mpm.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.techno.mpm.exception.FailedToUploadException;

@Component
public class SSSUploadFile {

	/**
	 * This is the end point url for amazon s3 bucket
	 */
	@Value("${amazon-properties.endpoint-url}")
	private String endpointUrl;

	/**
	 * This is the bucketName for amazon s3 bucket
	 */
	@Value("${amazon-properties.bucket-name}")
	public String bucketName;

	/**
	 * This enables automatic dependency injection of AmazonS3 interface. This
	 * object is used by methods in the SimpleProductServiceImplementation to call
	 * the respective methods.
	 */
	@Autowired
	private AmazonS3 s3client;

	public String uploadFile(MultipartFile multipartFile) {
		String fileUrl = "";
		try {
			File file = convertMultiPartFiletoFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			fileUrl = uploadFileTos3bucketConfig(file, fileName, multipartFile.getOriginalFilename());
		} catch (Exception e) {
			e.printStackTrace();
			throw new FailedToUploadException("Fail to upload");
		}
		return fileUrl;
	}

	private File convertMultiPartFiletoFile(MultipartFile file) throws IllegalStateException, IOException {
		File convFile = new File(System.getProperty("java.io.tmpdir") + File.separator + file.getOriginalFilename());
		file.transferTo(convFile);
		return convFile;
	}

	private String generateFileName(MultipartFile multiPart) {
		String fileName = multiPart.getOriginalFilename();
		if (fileName != null) {
			return UUID.randomUUID().toString().concat("-" + fileName.replaceAll("[^a-zA-Z0-9\\.]", ""));
		} else {
			throw new FailedToUploadException("File Name is  Empty");
		}

	}

	public String uploadFileTos3bucketConfig(File file, String fileName, String originalName) {
		String filePath = fileName;
		s3client.putObject(
				new PutObjectRequest(bucketName, filePath, file).withCannedAcl(CannedAccessControlList.PublicRead));
		String url = s3client.getUrl(bucketName, filePath).toString();
		Path path = Paths.get((System.getProperty("java.io.tmpdir") + File.separator + originalName));
		try {
			Files.delete(path);
		} catch (IOException e) {
			return url;
		}
		return url;
	}

	public void deleteS3Folder(String folderPath) {
		if (folderPath.length() > 1) {
			String path = folderPath.replace("https://technoelevate-test.s3.ap-south-1.amazonaws.com/", "");
			for (S3ObjectSummary file : s3client.listObjects(bucketName, path).getObjectSummaries()) {
				s3client.deleteObject(bucketName, file.getKey());
			}
		}
	}

	public byte[] getS3File(String fileName) {
		String path = fileName.replace("https://technoelevate-test.s3.ap-south-1.amazonaws.com/", "");
		S3Object s3Object = s3client.getObject(new GetObjectRequest(bucketName, path));
		S3ObjectInputStream objectContent = s3Object.getObjectContent();

		
		try {
			return IOUtils.toByteArray(objectContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

}
