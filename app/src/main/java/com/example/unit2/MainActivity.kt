package com.example.unit2

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unit2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val items = mutableListOf<WishlistItem>()
    private lateinit var adapter: WishlistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)

        // Set up RecyclerView
        adapter = WishlistAdapter(items)
        binding.contentMain.wishlistRecyclerView.adapter = adapter
        binding.contentMain.wishlistRecyclerView.layoutManager = LinearLayoutManager(this)

        // Set up Submit button
        binding.contentMain.submitButton.setOnClickListener {
            val name = binding.contentMain.nameEditText.text.toString().trim()
            val rawPrice = binding.contentMain.priceEditText.text.toString().trim()
            val url = binding.contentMain.urlEditText.text.toString().trim()

            if (name.isEmpty() || rawPrice.isEmpty() || url.isEmpty()) {
                Toast.makeText(this, "Please enter Item Name, Price, and URL", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val price = if (rawPrice.startsWith("$")) rawPrice else "$$rawPrice"
            val newItem = WishlistItem(name, price, url)

            items.add(newItem)
            adapter.notifyItemInserted(items.size - 1)
            binding.contentMain.wishlistRecyclerView.scrollToPosition(items.size - 1)

            // Clear input fields
            binding.contentMain.nameEditText.text?.clear()
            binding.contentMain.priceEditText.text?.clear()
            binding.contentMain.urlEditText.text?.clear()

            // Clear focus and hide keyboard
            binding.contentMain.nameEditText.clearFocus()
            binding.contentMain.priceEditText.clearFocus()
            binding.contentMain.urlEditText.clearFocus()

            val imm = getSystemService(InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }
    }
}
