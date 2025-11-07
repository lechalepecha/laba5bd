package com.example.laba5bd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laba5bd.ui.theme.Laba5bdTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Laba5bdTheme {
                val VM: ProductViewModel = viewModel()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScreenSetup(modifier = Modifier.padding(innerPadding), viewModel = VM)
                }
            }
        }
    }
}

@Composable
fun ScreenSetup(modifier: Modifier = Modifier, viewModel: ProductViewModel) {
    MainScreen(modifier, viewModel)
}
@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: ProductViewModel) {

    var name by remember { mutableStateOf("") }
    var qtyText by remember { mutableStateOf("") }
    var searchResult by remember { mutableStateOf<List<Product>>(emptyList()) }

    val products by viewModel.products.observeAsState(emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Product Name") },
            singleLine = true,
            textStyle = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp, end = 35.dp, start = 35.dp)
        )

        OutlinedTextField(
            value = qtyText,
            onValueChange = { qtyText = it },
            label = { Text("Quantity") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp, end = 35.dp, start = 35.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            RoundedActionButton("Add") {
                viewModel.addProduct(name.trim(), qtyText.toIntOrNull() ?: 0)
            }
            RoundedActionButton("Search") {
                viewModel.search(name.trim()) { found -> searchResult = found }
            }
            RoundedActionButton("Delete") {
                viewModel.deleteProduct(name.trim())
            }
            RoundedActionButton("Clear") {
                name = ""
                qtyText = ""
                searchResult = emptyList()
            }
        }

        TableHeader()

        val data = if (searchResult.isNotEmpty()) searchResult else products

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(data) { p ->
                TableRow(p)
                Divider()
            }
        }
    }
}

@Composable
private fun RoundedActionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A65A8)),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text, color = Color.White)
    }
}

@Composable
private fun TableHeader() {
    val headerColor = Color(0xFF5A65A8)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(headerColor)
            .padding(vertical = 6.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("ID", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.5f))
        Text("Product", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f))
        Text("Quantity", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun TableRow(product: Product) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(product.id.toString(), modifier = Modifier.weight(0.5f))
        Text(product.productName, modifier = Modifier.weight(1.5f))
        Text(product.quantity.toString(), modifier = Modifier.weight(1f))
    }
}