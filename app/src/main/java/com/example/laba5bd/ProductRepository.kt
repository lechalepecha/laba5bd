package com.example.laba5bd

import androidx.lifecycle.LiveData

class ProductRepository(private val dao: ProductDao) {

    val allProducts: LiveData<List<Product>> = dao.getAllProducts()

    suspend fun insert(product: Product) {
        dao.insertProduct(product)
    }

    suspend fun deleteByName(name: String) {
        dao.deleteProduct(name)
    }

    suspend fun findByName(name: String): List<Product> {
        return dao.findProduct(name)
    }
}