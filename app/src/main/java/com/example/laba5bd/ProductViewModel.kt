package com.example.laba5bd


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(app: Application) : AndroidViewModel(app) {

    private val repository: ProductRepository
    val products: LiveData<List<Product>>

    init {
        val db = ProductRoomDatabase.getInstance(app)
        repository = ProductRepository(db.productDao())
        products = repository.allProducts
    }

    fun addProduct(name: String, qty: Int) {
        if (name.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(Product(name, qty))
        }
    }

    fun deleteProduct(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteByName(name)
        }
    }

    fun search(name: String, onFound: (List<Product>) -> Unit) {
        if (name.isBlank()) { onFound(emptyList()); return }
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.findByName(name)
            onFound(result)
        }
    }
}