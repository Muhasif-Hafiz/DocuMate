package com.muhasib.documate.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhasib.documate.R
import com.muhasib.documate.adapters.ItemAdapter
import com.muhasib.documate.data.ItemEntity
import com.muhasib.documate.databinding.FragmentHomeBinding
import com.muhasib.documate.mvvm.HomeViewModel
import com.muhasib.documate.utils.MySharedPreferences


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: ItemAdapter
    private lateinit var sharedPref: MySharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.setPadding(0, statusBarInsets.top, 0, 0)
            insets
        }

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        setupRecyclerView()
        setupObservers()

        sharedPref = MySharedPreferences(requireContext())

        viewModel.allItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
            val isEmpty = items.isEmpty()
            binding.emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
            binding.recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
            binding.lottieAnimationView.visibility = if (isEmpty) View.VISIBLE else View.GONE
            binding.emptyText.visibility = if (isEmpty) View.VISIBLE else View.GONE
        }


        binding.swipeRefreshLayout.setOnRefreshListener {
            if(sharedPref.getSwiped()){
                sharedPref.setSwiped(true)
                showAlertDialog()
            }else{
               viewModel.fetchItems()
                sharedPref.setSwiped(true)
            }
        }


    }

    private fun setupRecyclerView() {
        adapter = ItemAdapter(
            onItemClick = { item ->

                showEditDialog(item)
            },
            onDeleteClick = { item ->

                showDeleteConfirmationDialog(item)
            }
        )
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    private fun setupObservers() {
        viewModel.allItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
            binding.emptyState.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefreshLayout.isRefreshing = isLoading
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun showDeleteConfirmationDialog(item: ItemEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete ${item.name}?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteItem(item)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun showEditDialog(item: ItemEntity) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_item, null)

        val nameEdit = dialogView.findViewById<EditText>(R.id.editName)
        val colorEdit = dialogView.findViewById<EditText>(R.id.editColor)
        val capacityEdit = dialogView.findViewById<EditText>(R.id.editCapacity)
        val priceEdit = dialogView.findViewById<EditText>(R.id.editPrice)
        val generationEdit = dialogView.findViewById<EditText>(R.id.editGeneration)
        val yearEdit = dialogView.findViewById<EditText>(R.id.editYear)
        val cpuModelEdit = dialogView.findViewById<EditText>(R.id.editCpuModel)
        val hardDiskEdit = dialogView.findViewById<EditText>(R.id.editHardDiskSize)
        val strapColorEdit = dialogView.findViewById<EditText>(R.id.editStrapColor)
        val caseSizeEdit = dialogView.findViewById<EditText>(R.id.editCaseSize)
        val descriptionEdit = dialogView.findViewById<EditText>(R.id.editDescription)
        val screenSizeEdit = dialogView.findViewById<EditText>(R.id.editScreenSize)

        // Pre-fill data
        nameEdit.setText(item.name)
        colorEdit.setText(item.color)
        capacityEdit.setText(item.capacity)
        priceEdit.setText(item.price?.toString() ?: "")
        generationEdit.setText(item.generation)
        yearEdit.setText(item.year?.toString() ?: "")
        cpuModelEdit.setText(item.cpuModel)
        hardDiskEdit.setText(item.hardDiskSize)
        strapColorEdit.setText(item.strapColor)
        caseSizeEdit.setText(item.caseSize)
        descriptionEdit.setText(item.description)
        screenSizeEdit.setText(item.screenSize?.toString() ?: "")

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Item")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val updatedItem = item.copy(
                    name = nameEdit.text.toString(),
                    color = colorEdit.text.toString().ifBlank { null },
                    capacity = capacityEdit.text.toString().ifBlank { null },
                    price = priceEdit.text.toString().toDoubleOrNull(),
                    generation = generationEdit.text.toString().ifBlank { null },
                    year = yearEdit.text.toString().toIntOrNull(),
                    cpuModel = cpuModelEdit.text.toString().ifBlank { null },
                    hardDiskSize = hardDiskEdit.text.toString().ifBlank { null },
                    strapColor = strapColorEdit.text.toString().ifBlank { null },
                    caseSize = caseSizeEdit.text.toString().ifBlank { null },
                    description = descriptionEdit.text.toString().ifBlank { null },
                    screenSize = screenSizeEdit.text.toString().toDoubleOrNull()
                )
                viewModel.updateItem(updatedItem)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun showAlertDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Fetch Items Again?")
            .setMessage("Do you want to reload the items?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.fetchItems()
                binding.swipeRefreshLayout.isRefreshing = false
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                binding.swipeRefreshLayout.isRefreshing = false
            }
            .show()
    }

}