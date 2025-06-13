package kr.ollsy.itemImage.dto.reponse;

import lombok.Builder;

@Builder
public record ItemImageResponse(
        Long id,
        String url
) {
}