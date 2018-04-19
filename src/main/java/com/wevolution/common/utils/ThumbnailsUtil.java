package com.wevolution.common.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;

public class ThumbnailsUtil {
	public static InputStream operateBySize(InputStream in, int width, int height) throws IOException {
		BufferedImage image = ImageIO.read(in);
		Builder<BufferedImage> builder = null;

		int imageWidth = image.getWidth();
		int imageHeitht = image.getHeight();
		if ((float) width / height != (float) imageWidth / imageHeitht) {
			if (imageWidth > imageHeitht) {
				image = Thumbnails.of(in).height(height).asBufferedImage();
			} else {
				image = Thumbnails.of(in).width(width).asBufferedImage();
			}
			builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, width, height).size(width, height);
		} else {
			builder = Thumbnails.of(image).size(width, height);
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		builder.outputFormat("jpg").toOutputStream(out);
		ByteArrayInputStream stream = new ByteArrayInputStream(out.toByteArray());
		return stream;

	}

}
