package io.github.melin.spark.jobserver.driver.util;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public abstract class ParentAwareURLStreamHandlerFactory implements URLStreamHandlerFactory{

    protected URLStreamHandlerFactory parentFactory;

    public void setParentFactory(URLStreamHandlerFactory factory){
        this.parentFactory = factory;
    }

    public URLStreamHandlerFactory getParent(){
        return parentFactory;
    }

    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        URLStreamHandler handler = this.create(protocol);
        if (handler == null && this.parentFactory != null) {
            handler = this.parentFactory.createURLStreamHandler(protocol);
        }
        return handler;
    }

    protected abstract URLStreamHandler create(String protocol);
}
