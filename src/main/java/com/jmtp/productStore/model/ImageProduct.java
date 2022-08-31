package com.jmtp.productStore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageProduct {

    private String contentType=null;
    private Binary contentData=null;

}
