package pg.lib.awsfiles.service.api;

public interface AmazonConfig {
    String getBucketName();
    String getAwsUrl();
    String getAccessKey();
    String getSecretKey();
}
