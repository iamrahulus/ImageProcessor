package com.amazonaws.rahulspace.imageprocessing;

import java.util.Map;

public class ImageProcessor {

	public void processImage(byte[] image, String s3Path){
		if (image != null){
			Map labels = ImageRekognizer.getImageLabels(image);
			DynamoDataSource.saveMetaData(labels, s3Path);
		}
	}
	
}
