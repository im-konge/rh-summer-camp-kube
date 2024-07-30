package io.http.end;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * A simple RESTful HTTP server built with Spring Boot.
 * This server handles basic CRUD operations for items using a RESTful architecture.
 */
@SpringBootApplication
@RestController
@RequestMapping("/items")
public class RestfulHttpSpringServer {

    private Map<Integer, String> items = new ConcurrentHashMap<>();
    private int currentId = 0;

    /**
     * The main method that starts the Spring Boot application.
     *
     * @param args  Command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(RestfulHttpSpringServer.class, args);
    }

    /**
     * Handles GET requests to retrieve all items.
     *
     * @return      A map of all items with their IDs.
     */
    @GetMapping
    public Map<Integer, String> getAllItems() {
        return items;
    }

    /**
     * Handles GET requests to retrieve a single item by its ID.
     *
     * @param id    The ID of the item to retrieve.
     * @return      The item if found, or a "Item not found" message.
     */
    @GetMapping("/{id}")
    public String getItem(@PathVariable("id") int id) {
        return items.getOrDefault(id, "Item not found");
    }

    /**
     * Handles POST requests to create a new item.
     *
     * @param item  The description of the item to create.
     * @return      A confirmation message with the created item's ID.
     */
    @PostMapping
    public String createItem(@RequestBody String item) {
        items.put(++currentId, item);
        return "Item created with ID: " + currentId + "\n";
    }

    /**
     * Handles DELETE requests to remove an item by its ID.
     *
     * @param id    The ID of the item to delete.
     * @return      A confirmation message if the item was deleted, or a "Item not found" message if the item does not exist.
     */
    @DeleteMapping("/{id}")
    public String deleteItem(@PathVariable("id") int id) {
        if (items.remove(id) != null) {
            return "Item deleted successfully";
        } else {
            return "Item not found";
        }
    }
}
