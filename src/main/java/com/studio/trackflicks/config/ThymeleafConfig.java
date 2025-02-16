package com.studio.trackflicks.config;

import com.studio.trackflicks.utils.ProjectConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
@RequiredArgsConstructor
public class ThymeleafConfig {

    private final MessageSource emailMessageSource;

    private static final String URL_TEMPLATES = "classpath:/templates/";
    private static final String HTML_EXTENSION = ".html";

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        final SpringResourceTemplateResolver  resolver = new SpringResourceTemplateResolver ();
        resolver.setPrefix(URL_TEMPLATES);
        resolver.setSuffix(HTML_EXTENSION);
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(ProjectConstants.DEFAULT_ENCODING);
        resolver.setCacheable(false);
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        // Using TemplateEngineMessageSource() because spring container reset message source
        // with default one when using setMessageSource()
        templateEngine.setTemplateEngineMessageSource(emailMessageSource);
        templateEngine.setEnableSpringELCompiler(true);

        return templateEngine;
    }

}
