package com.fthon.save_track.common.properties;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@ConfigurationProperties(prefix = "ios")
public class APNProperty {
    private String p8FilePath;
    private String teamId;
    private String keyId;
    private String bundleId;
}
