package com.idaltchion.ifxmoney.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.Tag;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;
import com.amazonaws.services.s3.model.lifecycle.LifecycleTagPredicate;
import com.idaltchion.ifxmoney.api.config.property.IfxmoneyApiProperty;

@Configuration
public class S3Config {

	@Autowired
	private IfxmoneyApiProperty property;
	
	@Bean
	public AmazonS3 amazonS3() {
		AWSCredentials credentials = new BasicAWSCredentials(
				property.getS3().getAccessKey(), property.getS3().getSecretAccessKey());
		AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.US_EAST_2)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
		
		String bucketName = property.getS3().getBucket();
		if(!amazonS3.doesBucketExistV2(bucketName)) {
			amazonS3.createBucket(new CreateBucketRequest(bucketName));
			BucketLifecycleConfiguration.Rule bucketRule = new BucketLifecycleConfiguration.Rule()
					.withId("IfxMoney - Regra de expiracao de arquivos temporarios")
					.withExpirationInDays(1)
					.withFilter(new LifecycleFilter(new LifecycleTagPredicate(new Tag("expirar", "true"))))
					.withStatus(BucketLifecycleConfiguration.ENABLED);
			BucketLifecycleConfiguration bucketConfiguration = new BucketLifecycleConfiguration().withRules(bucketRule);
			amazonS3.setBucketLifecycleConfiguration(bucketName, bucketConfiguration);
		}
		
		return amazonS3;
	}

}
