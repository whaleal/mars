
package com.whaleal.mars.core.domain;

import java.io.Serializable;

/**
 * Abstract Java Bean implementation of {@code Pageable}.
 *
 *
 */
public abstract class AbstractPageRequest implements Pageable, Serializable {

	private static final long serialVersionUID = 1232825578694716871L;

	private final int page;
	private final int size;

	/**
	 * Creates a new {@link AbstractPageRequest}. Pages are zero indexed, thus providing 0 for {@code page} will return
	 * the first page.
	 *
	 * @param page must not be less than zero.
	 * @param size must not be less than one.
	 */
	public AbstractPageRequest( int page, int size) {

		if (page < 0) {
			throw new IllegalArgumentException("Page index must not be less than zero!");
		}

		if (size < 1) {
			throw new IllegalArgumentException("Page size must not be less than one!");
		}

		this.page = page;
		this.size = size;
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable#getPageSize()
	 */
	public int getPageSize() {
		return size;
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable#getPageNumber()
	 */
	public int getPageNumber() {
		return page;
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable#getOffset()
	 */
	public long getOffset() {
		return (long) page * (long) size;
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable#hasPrevious()
	 */
	public boolean hasPrevious() {
		return page > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable#previousOrFirst()
	 */
	public Pageable previousOrFirst() {
		return hasPrevious() ? previous() : first();
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable#next()
	 */
	public abstract Pageable next();

	/**
	 *
	 *
	 * @return
	 */
	public abstract Pageable previous();

	/*
	 * (non-Javadoc)
	 * @see Pageable#first()
	 */
	public abstract Pageable first();

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;

		result = prime * result + page;
		result = prime * result + size;

		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		AbstractPageRequest other = (AbstractPageRequest) obj;
		return this.page == other.page && this.size == other.size;
	}
}
