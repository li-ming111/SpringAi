package com.zmx.domain;

import java.util.List;

/**
 * 演员的电影
 */
public record ActorFilms(String actor, List<String> movies) {}
