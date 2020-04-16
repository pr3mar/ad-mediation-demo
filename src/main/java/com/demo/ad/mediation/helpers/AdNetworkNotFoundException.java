package com.demo.ad.mediation.helpers;

import javax.persistence.EntityNotFoundException;

public class AdNetworkNotFoundException extends EntityNotFoundException {

    public AdNetworkNotFoundException() {
        super();
    }

    public AdNetworkNotFoundException(String message) {
        super(message);
    }
}
