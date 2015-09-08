package com.sam.app;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="crawler")
public class CrawlerConfig {
	private List<CrawlerTarget> targets;
}
