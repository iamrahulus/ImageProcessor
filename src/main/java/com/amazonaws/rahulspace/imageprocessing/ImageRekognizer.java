package com.amazonaws.rahulspace.imageprocessing;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;

public class ImageRekognizer {

	static AmazonRekognition rekognition = AmazonRekognitionClient.builder().build();

	private static List<Label> getData(byte[] imageData) {
		DetectLabelsRequest request = new DetectLabelsRequest();
		Image image = new Image();
		image.setBytes(ByteBuffer.wrap(imageData));
		request.setImage(image);
		DetectLabelsResult result = rekognition.detectLabels(request);
		return result.getLabels();
	}

	public static Map<String, BigDecimal> getImageLabels(byte[] imageData) {
		List<Label> labels = getData(imageData);
		Map imageLabels = new HashMap();
		for (Label l : labels) {
			String name = l.getName();
			Float f = l.getConfidence();
			BigDecimal b = BigDecimal.valueOf(f);
			imageLabels.put(name, b);
		}
		return imageLabels;
	}
}