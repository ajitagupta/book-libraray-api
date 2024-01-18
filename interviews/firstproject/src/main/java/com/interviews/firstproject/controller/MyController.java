package com.interviews.firstproject.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interviews.firstproject.model.Product;
import com.interviews.firstproject.repository.ProductRepository;
import com.interviews.firstproject.exception.*;

@RestController
@RequestMapping("/products")
public class MyController {

	@Autowired
	ProductRepository pR;
	
	@GetMapping("/greet")
	public String defaultMessage()	{
		return "hello";
	}
	
	@GetMapping
	public List<Product> getAllProducts(){
		return pR.getAllProducts();
	}
	
	@PostMapping
	public Product createProduct(@RequestBody Product product)	{
		if (pR.findByBrandName(product.getBrandName()) == null)
		{
			pR.save(product);// save is upsert (update + insert)
			return product;
		}
		throw new BrandException("Brandname " + product.getBrandName() + " must be unique");
	}
	
	@PutMapping("/{id}")
	public String updateProduct(@RequestBody Product product, @PathVariable Integer id)	{
		Optional<Product> dbProduct = pR.findById(id);
		if (dbProduct.isPresent()	) {
			//1st approach
			Product pr = dbProduct.get();
			if (pR.findByBrandName(product.getBrandName()) == null)
			{
				pr.setName(product.getName()!= null? product.getName() : pr.getName());
				pr.setBrandName(product.getBrandName() != null ? product.getBrandName() : pr.getBrandName());
				pr.setPrice(product.getPrice() != null ? product.getPrice() : pr.getPrice());
				pR.save(pr);
				return "Product updated";
			}	else	{
				throw new BrandException("Brandname " + product.getBrandName() + " must be unique");
			}
			//2nd approach
			
//			product.setId(id);
//			pR.save(product);
			
			
		}
		throw new ProductException("Product not valid");
	}
	
	@PatchMapping("/{id}")
	public String patchProduct(@RequestBody Product product, @PathVariable Integer id)	{
		Optional<Product> dbProduct = pR.findById(id);
		if (dbProduct.isPresent()	) {
			if (pR.findByBrandName(product.getBrandName()) == null)
			{			
				Product pr = dbProduct.get();
				pr.setBrandName(product.getBrandName()); // patch
				pR.save(pr);
				return "Product patched";
			}	else	{
				throw new ProductException("Brandname " + product.getBrandName() + " must be unique");				
			}
		}
		throw new ProductException("Product not valid");
	}
	
	
	@DeleteMapping("/{name}")
	public String deleteProduct(@PathVariable String name)	{
		List<Product> products = pR.getAllProducts();
		for (Product p : products)	{
			if (p.getName().equals(name))	pR.delete(p);
			return "product " + name + " deleted.";
		}
		return "product " + name + " is not valid.";
	}
}
