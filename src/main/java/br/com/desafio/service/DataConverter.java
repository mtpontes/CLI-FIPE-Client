package br.com.desafio.service;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class DataConverter implements IDataConverter{

	ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public <T> T convertData(String json, Class<T> classe) {
		try {
			return mapper.readValue(json, classe);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public <T> T convertData(String json, TypeReference<T> reference) {
		try {
			return mapper.readValue(json, reference);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> List<T> convertDataToList(String json, Class<T> classe) {
		CollectionType lista = mapper.getTypeFactory()
				.constructCollectionType(List.class, classe);
		try {
			return mapper.readValue(json, lista);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}