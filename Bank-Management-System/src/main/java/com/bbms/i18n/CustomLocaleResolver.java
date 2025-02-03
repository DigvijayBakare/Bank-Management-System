//package com.bbms.i18n;
//
//import org.springframework.context.MessageSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.support.ResourceBundleMessageSource;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
//import jakarta.servlet.http.HttpServletRequest;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Locale;
//
//@Configuration
//public class CustomLocaleResolver extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {
//    List<Locale> LOCALES = Arrays.asList(
//            new Locale("en"),
//            new Locale("fr"));
//    @Override
//    public Locale resolveLocale(HttpServletRequest httpRequest) {
//        String langHeader = httpRequest.getHeader("Accept-Language");
//        return langHeader == null || langHeader.isEmpty()
//                ? Locale.getDefault()
//                : Locale.lookup(Locale.LanguageRange.parse(langHeader), LOCALES);
//    }
//    @Bean
//    public MessageSource messageSource() {
//        ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
//        rs.setBasename("messages");
//        rs.setDefaultEncoding("UTF-8");
//        rs.setUseCodeAsDefaultMessage(true);
//        return rs;
//    }
//}