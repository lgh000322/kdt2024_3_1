package shop.shopBE.domain.member.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    MALE("남성"),
    FEMALE("여성"),;

    private final String description;
}
