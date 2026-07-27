package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.awt.image.BufferedImage;

import service.S3ImageService;
import service.ThumbnailService;

public class ThumbnailHandler implements RequestHandler<S3Event, String> {

    @Override
    public String handleRequest(S3Event s3event, Context context) {
        context.getLogger().log("Lambda Started");

        S3Client s3Client = S3Client.builder()
                .region(Region.AP_SOUTH_1) // ⚠️ match your bucket region
                .build();

        S3ImageService s3ImageService = new S3ImageService(s3Client);
        ThumbnailService thumbnailService = new ThumbnailService();

        for (S3EventNotification.S3EventNotificationRecord record : s3event.getRecords()) {
            String bucket = record.getS3().getBucket().getName();
            String key = record.getS3().getObject().getKey();

            context.getLogger().log("Bucket: " + bucket);
            context.getLogger().log("Object: " + key);
            context.getLogger().log("Downloading Image...");

            try {
                BufferedImage original = s3ImageService.downloadImage(bucket, key);
                context.getLogger().log("Original Size: " + original.getWidth() + " x " + original.getHeight());

                context.getLogger().log("Generating Thumbnail...");
                BufferedImage thumbnail = thumbnailService.generateThumbnail(original);
                context.getLogger().log("Thumbnail Size: " + thumbnail.getWidth() + " x " + thumbnail.getHeight());

                s3ImageService.uploadThumbnail(bucket, key, thumbnail);
                context.getLogger().log("Thumbnail Uploaded Successfully");

            } catch (Exception e) {
                context.getLogger().log("Error processing image: " + e.getMessage());
            }
        }

        return "S3 Event processed successfully";
    }
}
