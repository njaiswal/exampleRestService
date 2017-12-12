package com.nj;


import com.nj.service.MarvelService;
import org.glassfish.jersey.server.ResourceConfig;

public class MarvelApplication extends ResourceConfig {
    /**
     * Register JAX-RS application components.
     */
    public MarvelApplication() {
        register(MarvelService.class);
    }
}