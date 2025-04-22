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
import kr.ollsy.itemImage.dto.reponse.ItemImageResponse;
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
    public ItemImageResponse uploadItemImage(MultipartFile file) {

        String uploadUrl = uploadFile(file);

        ItemImage itemImage = ItemImage.builder()
                .url(uploadUrl)
                .build();

        itemImageRepository.save(itemImage);

        return ItemImageResponse.builder()
                .id(itemImage.getId())
                .url(itemImage.getUrl())
                .build();
    }

    private String uploadFile(MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String ext = originalName.substring(originalName.lastIndexOf("."));
            String fileName = "item-images" + uuid + ext;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            return amazonS3.getUrl(bucket, fileName).toString();

        } catch (IOException e) {
            throw new CustomException(GlobalExceptionCode.ITEM_IMAGE_BAD_REQUEST);
        }
    }

    @Transactional
    public void deleteItemImageInS3(String url) {
        String keyUrl = extractKeyFromUrl(url);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, keyUrl));
    }

    private String extractKeyFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    @Transactional
    public void deleteItemImage(Long id) {
        ItemImage itemImage = itemImageRepository.findById(id)
                .orElseThrow(()-> new CustomException(GlobalExceptionCode.ITEM_IMAGE_NOT_FOUND));

        String keyUrl = extractKeyFromUrl(itemImage.getUrl());
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, keyUrl));

        itemImageRepository.deleteById(id);
    }
}