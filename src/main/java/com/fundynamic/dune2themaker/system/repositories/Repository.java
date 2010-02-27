package com.fundynamic.dune2themaker.system.repositories;

/**
 * A repository represents a 'bucket' of 'things'. We can access these 'things' (ie,
 * images, sound files, movies, etc) from several Repositories. The repository interface
 * provides a single way of getting things from a repository.
 * 
 * @author Stefan
 *
 */
public interface Repository<T> {

	public T getItem(String key);
	
	public void addItem(String key, T item);
}
