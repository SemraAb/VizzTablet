package com.samraa.vizztablet.ui.home.adapter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.samraa.data.models.dto.OrderDto
import com.samraa.vizztablet.R
//
//class SwipeLeftRight(
//    private val adapter: TableAdapter,
//    private val onSwipeToDelete: (OrderDto) -> Unit,
//    private val onSwipeToDone: (OrderDto) -> Unit
//) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
//
//    override fun onMove(
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder,
//        target: RecyclerView.ViewHolder
//    ): Boolean = false
//
//    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//        val position = viewHolder.adapterPosition
//        val item = adapter.currentList[position]
//
//        when (direction) {
//            ItemTouchHelper.LEFT -> onSwipeToDelete(item)  // Changed this
//            ItemTouchHelper.RIGHT -> onSwipeToDone(item)   // Changed this
//        }
//    }
//
//    override fun onChildDraw(
//        c: Canvas,
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder,
//        dX: Float,
//        dY: Float,
//        actionState: Int,
//        isCurrentlyActive: Boolean
//    ) {
//        val itemView = viewHolder.itemView
//        val paint = Paint()
//        val textPaint = Paint().apply {
//            color = Color.WHITE
//            textSize = 16f * itemView.context.resources.displayMetrics.density
//            typeface = Typeface.DEFAULT_BOLD
//        }
//
//        val cornerRadius = 8f * itemView.context.resources.displayMetrics.density
//        val padding = 32f * itemView.context.resources.displayMetrics.density  // 32dp padding
//
//
//        when {
//            dX > 0 -> { // Swipe right (Done)
//                paint.color = ContextCompat.getColor(itemView.context, R.color.bright_green)
////                c.drawRoundRect(
////                    itemView.left.toFloat(),
////                    itemView.top.toFloat(),
////                    dX,
////                    itemView.bottom.toFloat(),
////                    cornerRadius , cornerRadius, paint
////                )
//
//                val rect = android.graphics.RectF(
//                    itemView.left.toFloat(),
//                    itemView.top.toFloat(),
//                    itemView.left + dX, // Right boundary after swipe
//                    itemView.bottom.toFloat()
//                )
//
//                val corners = floatArrayOf(
//                    cornerRadius, cornerRadius,       // Top left radius
//                    0f, 0f,     // Top right radius
//                    0f, 0f,       // Bottom right radius
//                    cornerRadius, cornerRadius      // Bottom left radius
//                )
//
//                val path = Path()
//                path.addRoundRect(rect, corners, Path.Direction.CW)
//                c.drawPath(path, paint)
//
//                c.drawText(
//                    "Done",
//                    itemView.left + padding,
//                    itemView.top + ((itemView.height.toFloat() + 13f) / 2f),
//                    textPaint
//                )
//            }
//
////            dX < 0 -> { // Swipe left (Delete)
////                paint.color = ContextCompat.getColor(itemView.context, R.color.bright_red)
//////                c.drawRoundRect(
//////                    itemView.right + dX,
//////                    itemView.top.toFloat(),
//////                    itemView.right.toFloat(),
//////                    itemView.bottom.toFloat(),
//////                    cornerRadius ,cornerRadius, paint
//////                )
////                val rect = android.graphics.RectF(
////                    itemView.left.toFloat() + dX,
////                    itemView.top.toFloat(),
////                    itemView.right.toFloat(), // Right boundary after swipe
////                    itemView.bottom.toFloat()
////                )
////
////                val corners = floatArrayOf(
////                    0f, 0f,       // Top left radius
////                    cornerRadius, cornerRadius,     // Top right radius
////                    cornerRadius, cornerRadius,       // Bottom right radius
////                    0f, 0f      // Bottom left radius
////                )
////
////                val path = Path()
////                path.addRoundRect(rect, corners, Path.Direction.CW)
////                c.drawPath(path, paint)
////
////                c.drawText(
////                    "Delete",
////                    itemView.right - textPaint.measureText("Delete") - padding,
////                    itemView.top + ((itemView.height.toFloat() + 13f) / 2f),
////                    textPaint
////                )
////            }
//            dX < 0 -> { // Swipe left (Delete)
//                paint.color = ContextCompat.getColor(itemView.context, R.color.bright_red)
//
//                // Correctly define the rectangle for the left swipe
//                val rect = android.graphics.RectF(
//                    itemView.right + dX, // Left boundary
//                    itemView.top.toFloat(),
//                    itemView.right.toFloat() , // Right boundary
//                    itemView.bottom.toFloat()
//                )
//
//                // Correct the corner radii for the left swipe
//                val corners = floatArrayOf(
//                    0f, 0f,                       // Top left radius
//                    cornerRadius, cornerRadius,   // Top right radius
//                    cornerRadius, cornerRadius,   // Bottom right radius
//                    0f, 0f                        // Bottom left radius
//                )
//
//                val path = Path()
//                path.addRoundRect(rect, corners, Path.Direction.CW)
//                c.drawPath(path, paint)
//
//                // Draw the "Delete" text
//                c.drawText(
//                    "Delete",
//                    itemView.right - textPaint.measureText("Delete") - padding,
//                    itemView.top + ((itemView.height.toFloat() + 13f) / 2f),
//                    textPaint
//                )
//            }
//
//        }
//
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//    }
//}