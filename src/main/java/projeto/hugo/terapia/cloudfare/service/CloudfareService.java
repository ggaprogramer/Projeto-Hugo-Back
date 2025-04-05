package projeto.hugo.terapia.cloudfare.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import projeto.hugo.terapia.cloudfare.PolicyBucket;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.List;

@Service
public class CloudfareService {

    @Value("${spring.cloudfare.secret-key}")
    private String secretKey;

    @Value("${spring.cloudfare.access-key}")
    private String accessKey;

    @Value("${spring.cloudfare.r2-region}")
    private String r2Region;

    @Value("${spring.cloudfare.account-id}")
    private String accountId;

    private S3Client s3;

    @PostConstruct
    public void initCloudFareService() {
        this.s3 = S3Client.builder()
                .endpointOverride(URI.create("https://" + this.accountId + ".r2.cloudflarestorage.com"))
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(this.accessKey, this.secretKey))
                )
                .region(Region.of(this.r2Region))
                .build();
    }

    public Boolean createBucket(String bucketName, boolean isPublic) {
        try {
            // Cria o request para criar o bucket
            CreateBucketRequest request = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            // Cria o bucket
            CreateBucketResponse response = s3.createBucket(request);

            // Se o bucket for público, configuramos a política de acesso
            if (isPublic) {
                // Define a política de bucket para permitir acesso público
                this.putPublicAccessPolicy(bucketName);
            }

            return true;
        } catch (S3Exception e) {
            return false;
        }
    }

    private void putPublicAccessPolicy(String bucketName) {
        // Define uma política de acesso público para o bucket
        String policy = "{"
                + "\"Version\": \"2012-10-17\","
                + "\"Statement\": ["
                + "{"
                + "\"Effect\": \"Allow\","
                + "\"Principal\": \"*\","
                + "\"Action\": \"s3:GetObject\","
                + "\"Resource\": \"arn:aws:s3:::" + bucketName + "/*\""
                + "}"
                + "]"
                + "}";

        // Aplica a política de bucket
        PutBucketPolicyRequest policyRequest = PutBucketPolicyRequest.builder()
                .bucket(bucketName)
                .policy(policy)
                .build();

        s3.putBucketPolicy(policyRequest);
    }

    /*public PolicyBucket checkBucketAccessPolicy(String bucketName) {
        try {
            // Obtém a política de acesso do bucket
            GetBucketPolicyRequest getBucketPolicyRequest = GetBucketPolicyRequest.builder()
                    .bucket(bucketName)
                    .build();

            // Tenta obter a política do bucket
            GetBucketPolicyResponse response = s3.getBucketPolicy(getBucketPolicyRequest);

            // Verifica se a política permite acesso público
            String policy = response.policy();

            if (policy != null && policy.contains("Allow") && policy.contains("Principal\": \"*\"")) {
                // Se a política contiver "Principal": "*" e "Allow", é um bucket público
                return PolicyBucket.PUBLIC;
            } else {
                return PolicyBucket.PRIVATE;
            }
        } catch (S3Exception e) {
            // Se não houver política definida, o bucket é privado por padrão
            return PolicyBucket.PRIVATE;
        }
    }*/

    public PolicyBucket isPublic(String bucketName, String fileName) {
        try {
            // Construa a URL pública do arquivo
            String publicUrlString = "https://" + bucketName + ".s3.amazonaws.com/" + fileName;

            // Usando URI para validar a string de URL
            URI uri = URI.create(publicUrlString);  // Cria o URI a partir da string

            // Verifique se o URI é válido
            URL url = uri.toURL();  // Converte o URI validado em URL

            // Tente acessar o arquivo diretamente
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");  // Usar o método HEAD para verificar sem baixar o conteúdo
            connection.connect();

            int responseCode = connection.getResponseCode();
            // Se a resposta for 200 OK, o arquivo é acessível publicamente
            if(responseCode == HttpURLConnection.HTTP_OK){
                return PolicyBucket.PUBLIC;
            } else {
                return PolicyBucket.PRIVATE;
            }

        } catch (IOException e) {
            // Se houver exceção, significa que o arquivo não é acessível publicamente
            return PolicyBucket.PRIVATE;
        }
    }


    public String generateLinkFile(String bucketName, String fileName){
        return generatePrivateLink(bucketName, fileName);
        /*PolicyBucket policyBucket = isPublic(bucketName, fileName);
        if(policyBucket.equals(PolicyBucket.PRIVATE)){
            return generatePrivateLink(bucketName, fileName);
        } else {
            return generatePublicLink(bucketName, fileName);
        }*/
    }

    public String generatePublicLink(String bucketName, String fileName) {
        // Para um link público, basta gerar a URL pública
        return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
    }

    public String generatePrivateLink(String bucketName, String fileName) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(this.accessKey, this.secretKey);

        // Criando o cliente para a API S3
        S3Presigner s3Presigner = S3Presigner.builder()
                .region(Region.of(this.r2Region)) // Ajuste conforme a região do seu bucket
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .endpointOverride(URI.create("https://" + this.accountId + ".r2.cloudflarestorage.com"))
                .build();

        // Criar a solicitação para obter um objeto
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        // Gerar o URL pré-assinado
        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(
                presignedGetObjectRequest -> presignedGetObjectRequest.getObjectRequest(getObjectRequest)
                        .signatureDuration(Duration.ofMinutes(15)) // Link válido por 15 minutos
        );

        return presignedRequest.url().toString();
    }

    public Boolean doesBucketExist(String bucketName) {
        try {
            HeadBucketRequest headRequest = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            HeadBucketResponse response = s3.headBucket(headRequest);
            return response != null;
        } catch (S3Exception e) {
            return false;
        }
    }

    public boolean isBucketEmpty(String bucketName) {
        try {
            ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .build();
            ListObjectsResponse listObjectsResponse = s3.listObjects(listObjectsRequest);

            // Se a lista de objetos estiver vazia, o bucket está vazio
            return listObjectsResponse.contents().isEmpty();
        } catch (S3Exception e) {
            return false; // Em caso de erro, consideramos que o bucket não está vazio
        }
    }

    public Boolean deleteBucket(String bucketName) {
        try {
            // Verifica se o bucket existe antes de tentar deletá-lo
            if (!doesBucketExist(bucketName)) {
                return false;
            }

            // Verifica se o bucket está vazio
            if (!isBucketEmpty(bucketName)) {
                return false;
            }

            // Cria o objeto DeleteBucketRequest
            DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            // Exclui o bucket
            s3.deleteBucket(deleteBucketRequest);

            return true;

        } catch (S3Exception e) {
            return false;
        }
    }

    public Boolean uploadFile(String bucketName, String fileName, InputStream fileContent,
                              String mimeType, Boolean isPublic) {
        try {
            // Verifica se o bucket existe antes de tentar enviar o arquivo
            if (!doesBucketExist(bucketName)) {
                this.createBucket(bucketName, isPublic);
            }

            // Cria o objeto PutObjectRequest
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName) // Nome do arquivo no bucket
                    .contentType(mimeType) // Adiciona o tipo MIME
                    .build();

            // Envia o arquivo para o bucket
            s3.putObject(putObjectRequest, RequestBody.fromInputStream(fileContent, fileContent.available()));

            return true;
        } catch (S3Exception | IOException e) {
            return false;
        }
    }

    public Boolean deleteFile(String bucketName, String fileName) {
        try {
            // Verifica se o bucket existe antes de tentar deletar o arquivo
            if (!doesBucketExist(bucketName)) {
                return false;
            }

            // Cria o objeto DeleteObjectRequest
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName) // Nome do arquivo a ser deletado
                    .build();

            // Deleta o arquivo do bucket
            s3.deleteObject(deleteObjectRequest);

            return true;

        } catch (S3Exception e) {
            return false;
        }
    }

    public ResponseEntity<?> listFiles(String bucketName) {
        try {
            // Verifica se o bucket existe antes de tentar listar os arquivos
            if (!doesBucketExist(bucketName)) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Bucket not found: " + bucketName);
            }

            // Cria o objeto ListObjectsRequest
            ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .build();

            // Lista os objetos do bucket
            ListObjectsResponse listObjectsResponse = s3.listObjects(listObjectsRequest);

            // Obtém a lista de arquivos (objetos)
            List<S3Object> objects = listObjectsResponse.contents();

            if (objects.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body("Bucket is empty: " + bucketName);
            }

            // Se houver arquivos, retorna a lista
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(objects);

        } catch (S3Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error listing files: " + e.getMessage());
        }
    }
}
