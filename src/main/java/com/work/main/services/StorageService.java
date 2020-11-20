package com.work.main.services;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {

    private final String BUCKET_NAME = "theam-crm-images";
    private final String CLIENT_REGION = "us-east-1";

    public void store(Long customerId, String originalFilename, MultipartFile multipartFile) {


        try {
            /*AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(CLIENT_REGION)
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();
            
            s3Client.putObject(BUCKET_NAME, customerId + '/' + originalFilename, convertToFile(multipartFile));*/
        	File file = new File("C://photos/" + customerId);
        	if (!file.exists()) {
                if (file.mkdir()) {
                    System.out.println("Directory is created!");
                } else {
                    System.out.println("Failed to create directory!");
                }
            }
        	String folder = "/photos/" + customerId + "/";
        	byte[] bytes = multipartFile.getBytes();
        	Path path = Paths.get(folder + multipartFile.getOriginalFilename());
        	Files.write(path, bytes);


        } catch (SdkClientException | IOException e) {
            e.printStackTrace();
        }

    }

    public void delete(Long customerId, String originalFilename) {
        try {
           /*AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(CLIENT_REGION)
                    .build();

            s3Client.deleteObject(new DeleteObjectRequest(BUCKET_NAME, customerId + '/' + originalFilename));*/
        	
        	File fileToDelete = new File("/photos/" + customerId + "/" + originalFilename);
            boolean success = fileToDelete.delete();
            System.out.println(success);
        	
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

    private File convertToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
