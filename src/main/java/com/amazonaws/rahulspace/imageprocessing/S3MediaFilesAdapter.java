package com.amazonaws.rahulspace.imageprocessing;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class S3MediaFilesAdapter {

	static AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();

	private GetObjectRequest getObjectRequest(String bucket, String key) {
		GetObjectRequest r = new GetObjectRequest(bucket, key);
		return r;
	}

	public void processFile(String bucket, String key) {
		S3ObjectInputStream ois = s3.getObject(getObjectRequest(bucket, key)).getObjectContent();
		if (!isImage(ois)) {
			// Process Video
		} else {
			// Process
		}
	}

	private boolean isImage(InputStream is) {
		try {
			BufferedImage i = ImageIO.read(is);
			if (i == null)
				return false;
		} catch (IOException ioe) {
			return false;
		}
		return true;
	}

}