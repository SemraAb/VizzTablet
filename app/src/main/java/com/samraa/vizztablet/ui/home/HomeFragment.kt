package com.samraa.vizztablet.ui.home

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.lifecycle.asLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.samraa.data.enums.OrderStatus
import com.samraa.data.models.dto.OrderDto
import com.samraa.vizztablet.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.samraa.vizztablet.databinding.FragmentHomeBinding
import com.samraa.vizztablet.manager.messaging.MyFirebaseMessagingService
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

    private val orderListRefreshReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == MyFirebaseMessagingService.ORDER_LIST_REFRESH_ACTION) {
                // Call getOrders() when the notification is received
                viewModel.getOrders()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Register the receiver when the view is created
        val filter = IntentFilter(MyFirebaseMessagingService.ORDER_LIST_REFRESH_ACTION)
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(orderListRefreshReceiver, filter)
    }

    override fun subscribeToObservables() {
        viewModel.filteredOrders
            .asLiveData()
            .observe(viewLifecycleOwner) { orders ->
                adapter.submitList(orders){
                    binding.tableRV.scrollToPosition(0)
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
//        val popupMenu =
//            androidx.appcompat.widget.PopupMenu(requireContext(), binding.orderStatusText)

        val popupMenu = PopupMenu(ContextThemeWrapper(requireContext(), R.style.PopupMenu), binding.orderStatusText)

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

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity(requireActivity())
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Unregister the receiver to prevent leaks
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(orderListRefreshReceiver)
    }

}