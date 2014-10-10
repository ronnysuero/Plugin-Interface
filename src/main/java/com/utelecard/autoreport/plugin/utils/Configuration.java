/**
 * Configuration.java
 *
 * This class defines methods to read the configuration file
 * 
 *@author Ronny Z. Suero
 * */
package com.utelecard.autoreport.plugin.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.ho.yaml.Yaml;

public class Configuration implements Map<Object, Object> {

	private final Map<Object, Object> config;

	/**
	 * class constructor
	 * 
	 * @param config the config param defines a map that put the value of the configuration
	 * 
	 * */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Configuration(final Map config) {
		this.config = config;
	}

	/**
	 * class constructor
	 * 
	 * @param fileName the fileName defines the name of the configuration's file
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public Configuration(final String fileName) throws Exception {
		final InputStream input = new FileInputStream(new File(fileName));
		this.config = (Map<Object, Object>) Yaml.load(input);
	}

	@Override
	public void clear() {
		this.config.clear();
	}

	@Override
	public boolean containsKey(final Object key) {
		return this.config.containsKey(key);
	}

	@Override
	public boolean containsValue(final Object value) {
		return this.config.containsValue(value);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Set entrySet() {
		return this.config.entrySet();
	}

	@Override
	public Object get(final Object key) {
		return this.config.get(key);
	}

	@SuppressWarnings({ "rawtypes" })
	public Configuration getConfiguration(final String key) {
		return new Configuration((Map) this.config.get(key));
	}

	@Override
	public boolean isEmpty() {
		return this.config.isEmpty();
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Set keySet() {
		return this.keySet();
	}

	@Override
	public Object put(final Object key, final Object value) {
		return this.put(key, value);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void putAll(final Map m) {
		this.config.putAll(m);
	}

	@Override
	public Object remove(final Object key) {
		return this.remove(key);
	}

	@Override
	public int size() {
		return this.config.size();
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection values() {
		return this.values();
	}
}
