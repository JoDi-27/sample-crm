package com.example.samplecrm.contact;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/** A {@link Pageable} that starts at a given row offset instead of a 0-based page number. */
record OffsetPage(long offset, int size, Sort sort) implements Pageable {

    @Override
    public int getPageNumber() {
        return (int) (offset / size);
    }

    @Override
    public int getPageSize() {
        return size;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetPage(offset + size, size, sort);
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? new OffsetPage(Math.max(0, offset - size), size, sort) : first();
    }

    @Override
    public Pageable first() {
        return new OffsetPage(0, size, sort);
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new OffsetPage((long) pageNumber * size, size, sort);
    }

    @Override
    public boolean hasPrevious() {
        return offset > 0;
    }
}
