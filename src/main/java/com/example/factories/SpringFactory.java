package com.example.factories;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

import picocli.CommandLine.IFactory;

@Component
public class SpringFactory implements IFactory {
    private final AutowireCapableBeanFactory beanFactory;

    public SpringFactory(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public <K> K create(Class<K> cls) throws Exception {
        return beanFactory.createBean(cls);
    }
}
