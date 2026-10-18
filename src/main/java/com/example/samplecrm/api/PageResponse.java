package com.example.samplecrm.api;

import java.util.List;

/** One page of a list: the items, the page number (from 1), the page size, and the total matching items. */
public record PageResponse<T>(List<T> items, int page, int size, long total) {}
