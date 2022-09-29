
package com.whaleal.mars.core.domain;


/**
 * {@link Pageable} implementation to represent the absence of pagination information.
 *
 * @author Oliver Gierke
 */
enum Unpaged implements Pageable {

	INSTANCE;

	/*
	 * (non-Javadoc)
	 * @see Pageable#isPaged()
	 */
	@Override
	public boolean isPaged() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable#previousOrFirst()
	 */
	@Override
	public Pageable previousOrFirst() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable#next()
	 */
	@Override
	public Pageable next() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable#hasPrevious()
	 */
	@Override
	public boolean hasPrevious() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable#getSort()
	 */
	@Override
	public Sort getSort() {
		return Sort.unsorted();
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable#getPageSize()
	 */
	@Override
	public int getPageSize() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable#getPageNumber()
	 */
	@Override
	public int getPageNumber() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable#getOffset()
	 */
	@Override
	public long getOffset() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable#first()
	 */
	@Override
	public Pageable first() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable#withPage(int)
	 */
	@Override
	public Pageable withPage(int pageNumber) {

		if (pageNumber == 0) {
			return this;
		}

		throw new UnsupportedOperationException();
	}

}
