package com.niltonrc.simplef.configuration;

import com.niltonrc.simplef.contracts.IEntryService;
import com.niltonrc.simplef.interceptors.ForwardingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer
{
    @Autowired
    private IEntryService entryService;

    @Override
    public void addInterceptors( InterceptorRegistry registry )
    {
        registry.addInterceptor( new ForwardingInterceptor( entryService ) );
    }
}
