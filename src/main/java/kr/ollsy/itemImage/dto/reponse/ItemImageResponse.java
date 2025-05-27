package kr.ollsy.itemImage.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
public record ItemImageResponse(
        Long id,
        String url
) {
}