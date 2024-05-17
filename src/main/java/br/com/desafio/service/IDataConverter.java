package br.com.desafio.service;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

public interface IDataConverter {
	
	<T> T convertData(String json, Class<T> classe);
	
	<T> T convertData(String json, TypeReference<T> reference);
	
	<T> List<T> convertDataToList(String json, Class<T> classe);
}