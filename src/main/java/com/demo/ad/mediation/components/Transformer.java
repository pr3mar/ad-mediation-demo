package com.demo.ad.mediation.components;

import java.util.List;

import static java.util.stream.Collectors.toList;

interface Transformer<E, B> {

    B toBean(E entity);

    E toEntity(B bean);

    default List<B> toBean(List<E> entityList) {
        return entityList.stream().map(this::toBean).collect(toList());
    }

    default List<E> toEntity(List<B> beanList) {
        return beanList.stream().map(this::toEntity).collect(toList());
    }
}
