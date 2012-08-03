package com.fundynamic.dune2themaker.infrastructure.repositories;

/**
 * A repository represents a 'bucket' of 'things'. We can access these 'things' (ie,
 * images, sound files, movies, etc) from several Repositories. The repository interface
 * provides a single way of getting things from a repository.
 *
 * @author Stefan
 */
public interface Repository<T> {

	public T getItem(String key);

	/**
	 * Add item with given key in repository. Key must be unique. When an item
	 * already exists under that name; this method should throw an IllegalArgumentException
	 *
	 * @param key
	 * @param item
	 * @throws IllegalArgumentException when item is null or key already exists
	 */
	public void addItem(String key, T item);

	/**
	 * Remove item from repository.
	 * Throws IllegalArgumentException when key does not exist in repository.
	 *
	 * @param key
	 * @throws IllegalArgumentException when key does not exists
	 */
	public void removeItem(String key);
}
