package com.amazonaws.rahulspace.imageprocessing;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;

public class DynamoDataSource {

	static AmazonDynamoDB dynamo = AmazonDynamoDBClientBuilder.defaultClient();
	private static String TABLE_NAME = "";

	private static BigDecimal getLikelyhood(String hashKey, String objectName, BigDecimal percentage) {
		GetItemSpec spec = new GetItemSpec();
		spec.withPrimaryKey("Label", hashKey).withProjectionExpression("Likelyhood, Object_Name");

		Table table = new Table(dynamo, TABLE_NAME);
		Item item = table.getItem(spec);
		if (item == null)
			return percentage;
		String o = item.getString("Object_Name");
		BigDecimal likelyhood = item.getNumber("Likelyhood");

		if (objectName.equalsIgnoreCase(o)) {
			if (percentage.compareTo(likelyhood) == 1) {
				return percentage;
			}
		}
		return likelyhood;
	}

	public static void saveMetaData(Map data, String s3ObjectKey, String s3BucketName) {
		PutItemRequest putRequest = new PutItemRequest();
		for (Object label : data.entrySet()) {
			AttributeValue attr = new AttributeValue((String) label);
			Map item = new HashMap();
			item.put("Label", attr);
			item.put("Likelyhood", getLikelyhood((String) label, s3ObjectKey, (BigDecimal) data.get("label")));
			putRequest.setItem(item);
		}
		dynamo.putItem(putRequest);
	}

	public static void saveMetaData(Map labels, String s3Path) {
		// TODO Auto-generated method stub
		if (s3Path != null) {
			String path = s3Path.substring(5);
			String bucketName = path.substring(0, path.indexOf("/") + 1);
			String objectKey = path.substring(path.indexOf("/") + 1);
			saveMetaData(labels, objectKey, bucketName);
		}
	}
}
