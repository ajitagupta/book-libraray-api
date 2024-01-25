package com.interviews.openfeignconsumer.openfeign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.interviews.openfeignconsumer.model.Product;

@FeignClient("client")
public interface ProductFeign {

	
	@GetMapping("/products")
	public List<Product> getAllProducts();

	@PostMapping("/products")
	public Product createProduct(@RequestBody Product requestBody);
	
	@PutMapping("/products/{id}")
	public String updateProduct(@RequestBody Product product, @PathVariable Integer id);
	
	@PatchMapping("/products/{id}")
	public String patchProduct(@RequestBody Product product, @PathVariable Integer id);

	@DeleteMapping("/products/{name}")
	public String deleteProduct(@PathVariable String name);
		
}
