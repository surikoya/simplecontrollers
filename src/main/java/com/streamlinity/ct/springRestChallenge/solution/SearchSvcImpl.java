package com.streamlinity.ct.springRestChallenge.solution;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.streamlinity.ct.springRestChallenge.api.Item;
import com.streamlinity.ct.springRestChallenge.api.SearchSvcInterface;

/*
 * Provide your implementation of the SearchSvcImpl here.
 * Also annotate your methods with Rest end point wrappers as required in the problem statement
 *
 * You can create any auxiliary classes or interfaces in this package if you need them.
 *
 * Do NOT add annotations as a Bean or Component or Service.   This is being handled in the custom Config class
 * PriceAdjustConfiguration
 */
public class SearchSvcImpl implements SearchSvcInterface {
	private File dataFile;

	@Override
	public void init(String itemPriceJsonFileName) {
		Objects.requireNonNull(itemPriceJsonFileName);
		this.dataFile = new File(itemPriceJsonFileName);
	}

	@Override
	public void init(File itemPriceJsonFile) {
		this.dataFile = itemPriceJsonFile;
	}

	@Override
	public List<Item> getItems() {
		List<Item> readValue = new ArrayList<>();
		try {
			byte[] readAllBytes = Files.readAllBytes(this.dataFile.toPath());
			ObjectMapper om = new ObjectMapper();
			
			readValue = om.readValue(readAllBytes,  new TypeReference<List<Item>>(){});
		} catch (IOException e) {
			throw new RuntimeException("Incorrect data file");
		}
		return readValue;
	}

	@Override
	public List<Item> getItems(String category) {
		return getItems().stream().filter(item -> item.getCategory_short_name().equals(category)).collect(Collectors.toList());
	}

	@Override
	public List<Item> getItem(String itemShortName) {
		return getItems().stream().filter(item -> item.getShort_name().equals(itemShortName)).collect(Collectors.toList());
	}
}
