//package com.bbms.i18n;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.i18n.LocaleContextHolder;
//import org.springframework.context.support.ResourceBundleMessageSource;
//import org.springframework.stereotype.Component;
//
//import java.util.Locale;
//import java.util.ResourceBundle;
//
//@Component
//public class Translator {
//    private static ResourceBundleMessageSource messageSource;
//    private static ResourceBundle messages;
//
//    @Autowired
//    Translator(ResourceBundleMessageSource messageSource) {
//        Translator.messageSource = messageSource;
//    }
//
//    public static ResourceBundle messageConverter(String lang){
//        if ("fr".equals(lang)) {
//            return messages = ResourceBundle.getBundle("messages_fr", Locale.FRENCH);
//        } else if ("jp".equals(lang)) {
//            return messages = ResourceBundle.getBundle("messages_jp", Locale.JAPANESE);
//        } else {
//            return messages = ResourceBundle.getBundle("messages", Locale.ENGLISH);
//        }
//    }
//
//    public static String toLocale(String msg) {
//        Locale locale = LocaleContextHolder.getLocale();
//        return messageSource.getMessage(msg, null, locale);
//    }
//}
