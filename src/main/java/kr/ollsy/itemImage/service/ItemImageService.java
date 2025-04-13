package kr.ollsy.itemImage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import kr.ollsy.global.exception.CustomException;
import kr.ollsy.global.exception.GlobalExceptionCode;
import kr.ollsy.itemImage.dto.request.ItemImageRequest;
import kr.ollsy.itemImage.domain.ItemImage;
import kr.ollsy.itemImage.repository.ItemImageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final ItemImageRepository itemImageRepository;

    @Transactional
    public String uploadItemImage(ItemImageRequest itemImageRequest) {

        String uploadUrl = uploadFile(itemImageRequest.getMultipartFile());

        ItemImage itemImage = ItemImage.builder()
                .name(itemImageRequest.getName())
                .url(uploadUrl)
                .build();
        itemImageRepository.save(itemImage);

        return uploadUrl;
    }

    private String uploadFile(MultipartFile multipartFile) {
        try {
            String originalName = multipartFile.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String ext = originalName.substring(originalName.lastIndexOf("."));
            String fileName = "item-images" + uuid + ext;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            amazonS3.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            return amazonS3.getUrl(bucket, fileName).toString();

        } catch (IOException e) {
            throw new CustomException(GlobalExceptionCode.ITEM_IMAGE_BAD_REQUEST);
        }
    }

    @Transactional
    public void deleteItemImage(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
        itemImageRepository.deleteItemImageByUrl(fileName);
    }
}