package com.sk.heb.myimages.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageMetadata {
    private String label;
    private Boolean enableObjectDetection;
}
