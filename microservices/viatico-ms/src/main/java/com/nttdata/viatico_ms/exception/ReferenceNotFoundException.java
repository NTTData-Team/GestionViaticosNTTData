package com.nttdata.viatico_ms.exception;

public class ReferenceNotFoundException extends RuntimeException {
    private final String resource;
    private final Long id;

    public ReferenceNotFoundException(String resource, Long id) {
        super(resource + " no encontrado con id=" + id);
        this.resource = resource;
        this.id = id;
    }
    public String getResource() { return resource; }
    public Long getId() { return id; }
}
