package cn.game.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 */
@Configuration
public class Config {

	public interface Values {
		String getStaticUrlPrefix();

		String getUploadedImagesDir();

		String getUploadedImagesUrlPrefix();
	}

	@Value("${admin.static.url-prefix}")
	private String staticUrlPrefix;

	@Value("${admin.uploaded.images.dir}")
	private String uploadedImagesDir;

	@Value("${admin.uploaded.images.url-prefix}")
	private String uploadedImagesUrlPrefix;

	@Bean(name = "values")
	public Values values() {
		return new Values() {
			@Override
			public String getStaticUrlPrefix() {
				return staticUrlPrefix;
			}

			@Override
			public String getUploadedImagesDir() {
				return uploadedImagesDir;
			}

			@Override
			public String getUploadedImagesUrlPrefix() {
				return uploadedImagesUrlPrefix;
			}
		};
	}

}
