package com.samraa.vizztablet.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.samraa.data.enums.OrderStatus
import com.samraa.data.models.dto.OrderDto
import com.samraa.vizztablet.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.samraa.vizztablet.databinding.FragmentHomeBinding
import com.samraa.vizztablet.ui.base.BaseFragment
import com.samraa.vizztablet.ui.home.adapter.SwipeGesture
import com.samraa.vizztablet.ui.home.adapter.TableAdapter
import com.samraa.vizztablet.utils.bindingAdapters.setOnSingleClickListener
import com.samraa.vizztablet.utils.extension.observe

class HomeFragment : BaseFragment() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentHomeBinding.inflate(
            layoutInflater
        )
    }
    override val viewModel by viewModel<HomeVM>()

    private val adapter by lazy {
        TableAdapter().also {
            binding.tableRV.adapter = it
            setupSwipeActions(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun subscribeToObservables() {
        viewModel.filteredOrders
            .asLiveData()
            .observe(viewLifecycleOwner) { orders ->
                adapter.submitList(orders)
            }

        viewModel.search
            .asLiveData()
            .observe(this) {
                if (it.isEmpty()) {
                    binding.searchBar.error = null // Clear error if input is empty
                } else {
                    if (it == "salam") {
                        Log.e("emil", "not error: ${viewModel.search.value} ")
                        binding.searchBar.error = null // Clear error if matches are found
                    } else {
                        binding.searchBar.error =
                            "Item not found" // Show error if no match is found
                        Log.e("emil", "error: ${viewModel.search.value} ")

                    }
                }

            }
        viewModel.organizationQr.observe(this) {
            binding.qrCodeImage.setImageBitmap(it)
        }
        viewModel.organizationLogo.observe(this) {
            binding.organizationLogoImage.setImageBitmap(it)
        }
    }


    private fun setupSwipeActions(adapter: TableAdapter) {
        val swipeCallback = SwipeGesture(
            adapter = adapter,
            onSwipeToFail = { order ->
                showConfirmationDialog(
                    order = order,
                    orderStatus = OrderStatus.FAILED
                )
            },
            onSwipeToDone = { order ->
                showConfirmationDialog(
                    order = order,
                    orderStatus = OrderStatus.DONE
                )
            }
        )

        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.tableRV)
    }

    private fun setupMenu() {
        val popupMenu =
            androidx.appcompat.widget.PopupMenu(requireContext(), binding.orderStatusText)

        popupMenu.menuInflater.inflate(
            R.menu.order_status_menu,
            popupMenu.menu
        )

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.pendingStatus -> {
                    viewModel.setSelectedStatus(OrderStatus.IN_PROGRESS)
                    Toast.makeText(context, "Pending selected", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.doneStatus -> {
                    viewModel.setSelectedStatus(OrderStatus.DONE)

                    Toast.makeText(context, "Done selected", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.failStatus -> {
                    viewModel.setSelectedStatus(OrderStatus.FAILED)

                    Toast.makeText(context, "Fail selected", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.allStatus -> {
                    viewModel.setSelectedStatus(null)  // Null means no filter (show all)
                    Toast.makeText(context, "All orders selected", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
        popupMenu.show()

    }

    private fun showConfirmationDialog(order: OrderDto, orderStatus: OrderStatus) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Confirm Order Status Change")
            .setMessage("Are you sure you want to change the status of this order to ${orderStatus.name}?")
            .setPositiveButton("Yes") { _, _ ->
                // If the user clicks "Yes", update the order status
                viewModel.updateOrderStatus(order, orderStatus)
                adapter.notifyDataSetChanged()
                Toast.makeText(context, "${orderStatus.name} status updated", Toast.LENGTH_SHORT)
                    .show()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // Close the dialog without making any changes
            }
            .create()

        dialog.show()
    }

    override fun setClicks() {
        binding.orderStatusText.setOnSingleClickListener {
            setupMenu()
        }

    }

}