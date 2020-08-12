package io.quarkus.qrs.runtime.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyReader;

import io.quarkus.qrs.runtime.spi.BeanFactory;

public class ResourceReader {

    private BeanFactory<MessageBodyReader<?>> factory;
    private List<String> mediaTypeStrings = new ArrayList<>();
    private volatile List<MediaType> mediaTypes;
    private volatile MessageBodyReader<?> instance;

    public void setFactory(BeanFactory<MessageBodyReader<?>> factory) {
        this.factory = factory;
    }

    public BeanFactory<MessageBodyReader<?>> getFactory() {
        return factory;
    }

    public List<String> getMediaTypeStrings() {
        return mediaTypeStrings;
    }

    public ResourceReader setMediaTypeStrings(List<String> mediaTypeStrings) {
        this.mediaTypeStrings = mediaTypeStrings;
        return this;
    }

    public MessageBodyReader<?> getInstance() {
        if (instance == null) {
            synchronized (this) {
                if (instance == null) {
                    //todo: manage lifecycle of bean
                    instance = factory.createInstance().getInstance();
                }
            }
        }
        return instance;
    }

    public List<MediaType> mediaTypes() {
        if (mediaTypes == null) {
            //todo: does this actually need to be threadsafe?
            synchronized (this) {
                List<MediaType> ret = new ArrayList<>();
                for (String i : mediaTypeStrings) {
                    ret.add(MediaType.valueOf(i));
                }
                mediaTypes = Collections.unmodifiableList(ret);
            }
        }
        return mediaTypes;
    }

}
