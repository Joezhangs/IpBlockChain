/*
 *
 *  *
 *  *   @copyright      Copyright ©  2017. [贵阳天德信链科技有限公司] All rights reserved.
 *  *   @project        tdchain-framework-aws3s
 *  *   @file            AmazonS3Client
 *  *   @author        warne
 *  *   @date           17-9-26 下午7:01
 *  *
 *  *   @lastModifie    17-9-26 下午7:01
 *  *OTE
 *
 */

package aws.s3;

import java.io.File; 
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.wevolution.common.utils.StringUtil;

import aws.s3.constants.IConstant;
import aws.s3.util.ConfigHelper;

/**
 * @desc: aws s3 service, upload file tools
 * @date: 2017/9/26 19:01
 * @author: warne
 */
@SuppressWarnings("deprecation")
public abstract class AWSS3Client {

	/**
	 * upload file to s3
	 *
	 * @param localeFile
	 * @return accessUrl
	 */
	public static String uploadToS3(String localeFile, String key) {
		return uploadToS3(new File(localeFile), key);
	}

	/**
	 * upload file to s3
	 *
	 * @param localeFile
	 * @return accessUrl
	 */
	public static String uploadToS3(File localeFile, String key) {
		String result = null;
		try {
			result = uploadToS3(new FileInputStream(localeFile), key);
		} catch (FileNotFoundException e) {
			logger.error(" ===> upload to s3 error . file= {} not exist ! desc: {} \n\n", localeFile.getAbsolutePath(),
					e.toString());
			return "";
		}
		return result;
	}

	/**
	 * upload file to s3
	 *
	 * @param is
	 * @return accessUrl
	 */
	public static String uploadToS3(InputStream is, String key) {
		if (!StringUtil.isEmpty(AWS_ROOT_UPLOAD))
			key = AWS_ROOT_UPLOAD.concat("/" + key);

		if (key.startsWith("/")) {
			logger.error(" ===> upload to s3 fail. key or filepath can't start with '/' ! \n");
			return "";
		}
		// key = AWS_ROOT_UPLOAD.concat(key);
		logger.info("\n-----------------------upload start ---------------------------------\n");
		if (null == is) {
			logger.error(" ===> upload to s3 fail. not found file stream ! \n");
			return "";
		}

		String accessUrl = null;
		try {
			// # 验证名称为bucketName的bucket是否存在，不存在则创建
			if (!checkBucketExists(amazonS3, AWS_BUCKET_NAME))
				amazonS3.createBucket(AWS_BUCKET_NAME);

			// # upload
			amazonS3.putObject(new PutObjectRequest(AWS_BUCKET_NAME, key, is, null));

			// S3Object s3Object = amazonS3.getObject(new
			// GetObjectRequest(AWS_BUCKET_NAME, key));
			// 获取一个request
			// GeneratePresignedUrlRequest urlRequest = new
			// GeneratePresignedUrlRequest(AWS_BUCKET_NAME, key);
			// # 设置访问有效期限=10年
			// LocalDateTime localDateTime = DateTools.date2localDateTime(new
			// Date());
			// localDateTime = localDateTime.plusYears(10);
			// urlRequest.setExpiration(DateTools.string2Date(localDateTime.toString()));

			// # 获取访问路径
			// accessUrl =
			// String.valueOf(amazonS3.generatePresignedUrl(urlRequest));
			accessUrl = AWS_S3_ENDPOINT + AWS_BUCKET_NAME + "/" + key;
			if (StringUtil.isEmpty(accessUrl)) {
				logger.error(" ===> upload to s3 fail. , file od uploaded haven`t get url  !!! \n");
				return "";
			}

			logger.info("-----------------------upload result---------------------------------");
			logger.info("---------------------url={}", accessUrl.toString());
			logger.info("-----------------------upload result---------------------------------");

		} catch (AmazonServiceException ase) {
			logger.error(" ===> upload to s3 error. server is error-ing . desc: {} \n\n", ase.toString());
			logger.error(
					" ===> Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.");
			logger.error(" ===> Error Message:    {}", ase.getMessage());
			logger.error(" ===> HTTP Status Code: {}", ase.getStatusCode());
			logger.error(" ===> AWS Error Code:   {}", ase.getErrorCode());
		} catch (AmazonClientException ace) {
			logger.error(" ===> upload to s3 error. client is error-ing . desc: {} \n\n", ace.toString());
			logger.error(
					" ===> Caught an AmazonClientException, which means the client encountered a serious internal problem while trying to communicate with S3, such as not being able to access the network.");
			logger.error(" ===> Error Message: {}", ace.getMessage());
		}
		logger.info("\n-----------------------upload end ---------------------------------\n");
		return accessUrl;
	}

	/**
	 * check bucket is existed
	 *
	 * @param s3
	 * @param bucketName
	 * @return
	 */
	private static boolean checkBucketExists(AmazonS3 s3, String bucketName) {
		List<Bucket> buckets = s3.listBuckets();
		for (Bucket bucket : buckets)
			if (Objects.equals(bucket.getName(), bucketName))
				return true;

		return false;
	}

	// #
	// ------------------------------------------------------------------------------------------------------------------------
	// #
	// ------------------------------------------------------------------------------------------------------------------------

	private final static String AWS_S3_ENDPOINT = ConfigHelper.getValue(IConstant.S3_ENDPOINT);
	private final static String AWS_ACCESS_KEY = ConfigHelper.getValue(IConstant.ACCESS_KEY);
	private final static String AWS_SECRET_KEY = ConfigHelper.getValue(IConstant.SECRET_KEY);
	private final static String AWS_BUCKET_NAME = ConfigHelper.getValue(IConstant.BUCKET_NAME);
	private final static String AWS_ROOT_UPLOAD = ConfigHelper.getValue(IConstant.ROOT_UPLOAD);
	private final static Logger logger = LoggerFactory.getLogger(AWSS3Client.class);
	private static AmazonS3 amazonS3;

	static {
//		AWSCredentials awsCredentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY);
//        AmazonS3ClientBuilder builder=  AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials));
//        builder.setRegion(Regions.AP_SOUTHEAST_1.getName());
//        amazonS3 = builder.build();
		amazonS3 = new AmazonS3Client(new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY));
		amazonS3.setRegion(Region.getRegion(Regions.CN_NORTH_1));
	}

	public static void main(String[] args) {
		String key = ("images/").concat("" + new Date().getTime()).concat("_116.png");
		String url = uploadToS3("C:\\jiangjian\\temple\\1_150111080328_19.jpg", key);
		System.out.println("url=" + url);
	}
}
