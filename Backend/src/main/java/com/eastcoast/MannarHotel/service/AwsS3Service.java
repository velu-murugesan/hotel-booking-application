package com.eastcoast.MannarHotel.service;
import com.eastcoast.MannarHotel.exception.OurException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@Service
public class AwsS3Service {
    private final String bucketName = "mannar-hotel-images";

    @Value("${aws.s3.access.key}")
    private String accessKey;

    @Value("${aws.s3.secret.key}")
    private String secretKey;

    public String saveImageToS3(MultipartFile photo){
      String s3LocationImage = null;

      try{

          AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey,secretKey);
          S3Client s3 = S3Client.builder()
                    .region(Region.EU_NORTH_1)
                     .credentialsProvider(StaticCredentialsProvider.create(credentials))
                      .build();

          InputStream inputStream = photo.getInputStream();
          long size = photo.getSize();
          String contentType = photo.getContentType();
          String fileName = photo.getOriginalFilename();

          PutObjectRequest request = PutObjectRequest.builder()
                  .bucket(bucketName)
                  .key(fileName)
                  .contentType(contentType)
                  .build();

          s3.putObject(
                  request, RequestBody.fromInputStream(inputStream,size)
          );

          s3LocationImage = "https://" + bucketName + ".s3.amazonaws.com/" + fileName;

      } catch (Exception e) {
          e.printStackTrace();
          throw new OurException("Unable to upload image to s3 bucket" + e.getMessage());
      }

      return s3LocationImage;

    }


}
