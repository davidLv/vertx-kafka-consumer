package com.md.vertx.util;

import lombok.Data;
import lombok.experimental.Builder;

/**
 * User: mdyminski
 */
@Data
@Builder
public class Configuration {
    private String kafkaTopic;
    private String vertxTopic;
    private String zkConnect;
    private String groupId;
}
