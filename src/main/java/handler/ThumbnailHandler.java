package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;

public class ThumbnailHandler implements RequestHandler<S3Event, String> {

    @Override
    public String handleRequest(S3Event s3event, Context context) {
        context.getLogger().log("Lambda Started");

        // Iterate over S3 event records
        for (S3EventNotification.S3EventNotificationRecord record : s3event.getRecords()) {
            String bucket = record.getS3().getBucket().getName();
            String key = record.getS3().getObject().getKey();

            context.getLogger().log("Received S3 Event - Bucket: " + bucket + ", Key: " + key);
            String eventName = record.getEventName();
            // Here you can add your thumbnail generation logic
            // For now, just log and return a message
            context.getLogger().log("eventName: {}" + eventName);
        }

        return "S3 Event processed successfully";
    }
}
