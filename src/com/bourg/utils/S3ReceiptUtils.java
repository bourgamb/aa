package com.bourg.utils;

import java.io.File;
import java.net.URL;
import java.util.List;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectTextRequest;
import com.amazonaws.services.rekognition.model.DetectTextResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.TextDetection;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

public class S3ReceiptUtils {

	public static List<TextDetection> convertS3ReceiptToText(String bucket, String receiptName) {
		
		AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();

		DetectTextRequest request = new DetectTextRequest()
				.withImage(new Image().withS3Object(new S3Object().withName(receiptName).withBucket(bucket)));

		List<TextDetection> textDetections = null;
		
		try {
			DetectTextResult result = rekognitionClient.detectText(request);
			textDetections = result.getTextDetections();

			System.out.println("Detected lines and words for " + receiptName);
			for (TextDetection text : textDetections) {

				System.out.println("Detected: " + text.getDetectedText());
				System.out.println("Confidence: " + text.getConfidence().toString());
				System.out.println("Id : " + text.getId());
				System.out.println("Parent Id: " + text.getParentId());
				System.out.println("Type: " + text.getType());
				System.out.println();
			}
		} catch (AmazonRekognitionException e) {
			e.printStackTrace();
		}
		
		return textDetections;
	}
	
	public static URL loadReceiptToS3(String bucket, String receiptName, String filePath) {
		
		AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();		
		
		File file = new File(filePath, receiptName);
		
		PutObjectResult result = s3.putObject(new PutObjectRequest(bucket, receiptName, file).withCannedAcl(CannedAccessControlList.PublicRead));
		URL objUrl = s3.getUrl(bucket, receiptName);
		
		return objUrl;
		
	}
	
	public static URL loadReceiptToS3File(String bucket, String receiptName, File file) {
		
		AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();		
		
		PutObjectResult result = s3.putObject(new PutObjectRequest(bucket, receiptName, file).withCannedAcl(CannedAccessControlList.PublicRead));
		URL objUrl = s3.getUrl(bucket, receiptName);
		
		return objUrl;
		
	}
	
}
