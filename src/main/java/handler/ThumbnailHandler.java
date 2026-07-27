package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.core.ResponseInputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ThumbnailHandler implements RequestHandler<S3Event, String> {

    @Override
    public String handleRequest(S3Event s3event, Context context) {
        context.getLogger().log("Lambda Started");

        // Build S3 client (credentials come from Lambda role automatically)
        S3Client s3Client = S3Client.builder()
                .region(Region.AP_SOUTH_1)
                .build();

        for (S3EventNotification.S3EventNotificationRecord record : s3event.getRecords()) {
            String bucket = record.getS3().getBucket().getName();
            String key = record.getS3().getObject().getKey();

            context.getLogger().log("Bucket: " + bucket);
            context.getLogger().log("Object: " + key);

            try {
                // Download object
                GetObjectRequest request = GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build();

                ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(request);

                // Print metadata
                GetObjectResponse metadata = s3Object.response();
                context.getLogger().log("Content Type: " + metadata.contentType());
                context.getLogger().log("Content Length: " + metadata.contentLength());
                context.getLogger().log("Last Modified: " + metadata.lastModified());

                // Read image
                BufferedImage image = ImageIO.read(s3Object);
                if (image != null) {
                    context.getLogger().log("Width: " + image.getWidth());
                    context.getLogger().log("Height: " + image.getHeight());
                } else {
                    context.getLogger().log("ImageIO could not read the file.");
                }

            } catch (IOException e) {
                context.getLogger().log("Error reading image: " + e.getMessage());
            }
        }

        return "S3 Event processed successfully";
    }
}
