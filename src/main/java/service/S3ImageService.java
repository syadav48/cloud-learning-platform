package service;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class S3ImageService {

    private final S3Client s3Client;

    public S3ImageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public BufferedImage downloadImage(String bucketName, String objectKey) throws IOException {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(request);
        return ImageIO.read(s3Object);
    }

    public void uploadThumbnail(String bucketName, String originalKey, BufferedImage thumbnail) throws IOException {
        String thumbnailKey = "thumbnails/" + originalKey;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(thumbnail, "jpg", baos);
        byte[] imageBytes = baos.toByteArray();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(thumbnailKey)
                .contentType("image/jpeg")
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(imageBytes));
    }
}
