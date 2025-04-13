package kr.ollsy.itemImage.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemImageRequest {
    private String name;
    private MultipartFile multipartFile;
}
