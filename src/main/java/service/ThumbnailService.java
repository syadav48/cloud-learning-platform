package service;

import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class ThumbnailService {

    public static final int THUMBNAIL_WIDTH = 200;
    public static final int THUMBNAIL_HEIGHT = 200;

    public BufferedImage generateThumbnail(BufferedImage original) throws IOException {
        return Thumbnails.of(original)
                .size(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT)
                .asBufferedImage();
    }
}
