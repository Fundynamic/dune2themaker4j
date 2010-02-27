package com.fundynamic.dune2themaker.system.repositories;

/**
 * Class representing a key in a repository.
 * 
 * @author Stefan
 *
 */
public final class RepositoryKey {
	private final String name;

	public RepositoryKey(String name) {
		super();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	/**
	 * Equals on name (uses equals of string)
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof RepositoryKey) {
			RepositoryKey otherKey = (RepositoryKey)other;
			if (otherKey.getName().equals(getName())) { 
				return true;
			}
		}
		return super.equals(other);
	}
}
