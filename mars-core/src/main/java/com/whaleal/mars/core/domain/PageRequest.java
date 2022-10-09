
package com.whaleal.mars.core.domain;


import com.mongodb.lang.Nullable;
import com.whaleal.icefrog.core.lang.Precondition;

/**
 * Basic Java Bean implementation of {@link Pageable}.
 *
 * @author wh
 */
public class PageRequest extends AbstractPageRequest {

	private static final long serialVersionUID = -4541509938956089562L;

	private final ISort sort;

	/**
	 * Creates a new {@link PageRequest} with sort parameters applied.
	 *
	 * @param page zero-based page index, must not be negative.
	 * @param size the size of the page to be returned, must be greater than 0.
	 * @param sort must not be {@literal null}, use {@link Sort#unsorted()} instead.
	 */
	protected PageRequest(int page, int size, ISort sort) {

		super(page, size);

		Precondition.notNull(sort, "Sort must not be null!");

		this.sort = sort;
	}

	/**
	 * Creates a new unsorted {@link PageRequest}.
	 *
	 * @param page zero-based page index, must not be negative.
	 * @param size the size of the page to be returned, must be greater than 0.
	 *
	 */
	public static PageRequest of(int page, int size) {
		return of(page, size, Sort.unsorted());
	}

	/**
	 * Creates a new {@link PageRequest} with sort parameters applied.
	 *
	 * @param page zero-based page index.
	 * @param size the size of the page to be returned.
	 * @param sort must not be {@literal null}, use {@link Sort#unsorted()} instead.
	 * 
	 */
	public static PageRequest of(int page, int size, Sort sort) {
		return new PageRequest(page, size, sort);
	}

	/**
	 * Creates a new {@link PageRequest} with sort direction and properties applied.
	 *
	 * @param page zero-based page index, must not be negative.
	 * @param size the size of the page to be returned, must be greater than 0.
	 * @param direction must not be {@literal null}.
	 * @param properties must not be {@literal null}.
	 *
	 */
	public static PageRequest of(int page, int size, Direction direction, String... properties) {
		return of(page, size, Sort.by(direction, properties));
	}

	/**
	 * Creates a new {@link PageRequest} for the first page (page number {@code 0}) given {@code pageSize} .
	 *
	 * @param pageSize the size of the page to be returned, must be greater than 0.
	 * @return a new {@link PageRequest}.
	 *
	 */
	public static PageRequest ofSize(int pageSize) {
		return PageRequest.of(0, pageSize);
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable#getSort()
	 */
	public ISort getSort() {
		return sort;
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable#next()
	 */
	@Override
	public PageRequest next() {
		return new PageRequest(getPageNumber() + 1, getPageSize(), getSort());
	}

	/*
	 * (non-Javadoc)
	 * @see AbstractPageRequest#previous()
	 */
	@Override
	public PageRequest previous() {
		return getPageNumber() == 0 ? this : new PageRequest(getPageNumber() - 1, getPageSize(), getSort());
	}

	/*
	 * (non-Javadoc)
	 * @see Pageable#first()
	 */
	@Override
	public PageRequest first() {
		return new PageRequest(0, getPageSize(), getSort());
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(@Nullable Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PageRequest)) {
			return false;
		}

		PageRequest that = (PageRequest) obj;

		return super.equals(that) && this.sort.equals(that.sort);
	}

	/**
	 * Creates a new {@link PageRequest} with {@code pageNumber} applied.
	 *
	 * @param pageNumber
	 * @return a new {@link PageRequest}.
	 * 
	 */
	@Override
	public PageRequest withPage(int pageNumber) {
		return new PageRequest(pageNumber, getPageSize(), getSort());
	}

	/**
	 * Creates a new {@link PageRequest} with {@link Direction} and {@code properties} applied.
	 *
	 * @param direction must not be {@literal null}.
	 * @param properties must not be {@literal null}.
	 * @return a new {@link PageRequest}.
	 * 
	 */
	public PageRequest withSort(Direction direction, String... properties) {
		return new PageRequest(getPageNumber(), getPageSize(), Sort.by(direction, properties));
	}

	/**
	 * Creates a new {@link PageRequest} with {@link Sort} applied.
	 *
	 * @param sort must not be {@literal null}.
	 * @return a new {@link PageRequest}.
	 * 
	 */
	public PageRequest withSort(Sort sort) {
		return new PageRequest(getPageNumber(), getPageSize(), sort);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 31 * super.hashCode() + sort.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Page request [number: %d, size %d, sort: %s]", getPageNumber(), getPageSize(), sort);
	}

}
