package com.streamlinity.ct.springRestChallenge.solution;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.streamlinity.ct.springRestChallenge.api.Item;
import com.streamlinity.ct.springRestChallenge.api.SearchSvcInterface;

/*
 * This controller needs to expose the following rest endpoints.  You need to fill in the implementation here
 *
 * Required REST Endpoints
 *
 *      /item                       Get all items
 *      /item?category=C            Get all items in category specified by Category shortName
 *      /item/{itemShortName}       Get item that matches the specified Item shortName
 */

@Profile("default")
@RestController
public class SearchRestControllerImpl {
	
	@Autowired private SearchSvcInterface searchSvc;

	@Autowired private ApplicationContext context;

	private void initialize() {
		Objects.requireNonNull(searchSvc);
		try {
			File file = context.getResource("classpath:itemPrices.json").getFile();
			searchSvc.init(file);

		} catch (IOException e) {
			new RuntimeException("Failed to initialiuze");
		}
	}
	@GetMapping("item")
	public List<Item> getItems( HttpServletRequest request) {
		initialize();
		String category = request.getParameter("category");
		if (category != null)
			searchSvc.getItems(category);
		return searchSvc.getItems();
	}

	@GetMapping("item/{itemName}")
	public List<Item> getItemsByName(@PathVariable("itemName") String itemName) {
		initialize();
		return searchSvc.getItem(itemName);
	}	


}
