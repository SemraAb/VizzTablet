package com.samraa.vizztablet.ui.home.adapter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.samraa.data.enums.OrderStatus
import com.samraa.data.models.dto.OrderDto
import com.samraa.vizztablet.R

class SwipeGesture(
    private val adapter: TableAdapter,
    private val onSwipeToFail: (OrderDto) -> Unit,
    private val onSwipeToDone: (OrderDto) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val item = adapter.currentList[position]

        if (item.isStatusLocked) {
            adapter.notifyItemChanged(position) // Reset swipe animation
            return
        }

        when (direction) {
            ItemTouchHelper.LEFT -> {
                if (item.orderStatus == OrderStatus.FAILED) {
                    adapter.notifyItemChanged(position)
                } else {
                    onSwipeToFail(item)
                    adapter.notifyItemChanged(position)
                }
            }

            ItemTouchHelper.RIGHT -> {
                if (item.orderStatus == OrderStatus.DONE) {
                    adapter.notifyItemChanged(position)
                } else {
                    onSwipeToDone(item)
                    adapter.notifyItemChanged(position)
                }
            }
        }

    }


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val position = viewHolder.adapterPosition
        val item = adapter.currentList[position]

        // Return 0 if the order is locked to disable swiping
        if (item.isStatusLocked) {
            return 0
        }

        return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val cornerRadius = 8f * itemView.context.resources.displayMetrics.density
        val padding = 32f * itemView.context.resources.displayMetrics.density
        val paint = Paint().apply { isAntiAlias = true }
        val textPaint = Paint().apply {
            color = Color.WHITE
            textSize = 16f * itemView.context.resources.displayMetrics.density
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }

        if (dX > 0) { // Swipe right (Done)
            drawRoundedBackground(
                c, paint, viewHolder, dX, cornerRadius, true, R.color.bright_green
            )
            drawCenteredText(c, "Done", itemView.left + padding, viewHolder, textPaint)
        } else if (dX < 0) { // Swipe left (Fail)
            drawRoundedBackground(
                c, paint, viewHolder, dX, cornerRadius, false, R.color.bright_red
            )
            drawCenteredText(
                c,
                "Fail",
                itemView.right - textPaint.measureText("Fail") - padding,
                viewHolder,
                textPaint
            )
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun drawRoundedBackground(
        canvas: Canvas,
        paint: Paint,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        cornerRadius: Float,
        isRightSwipe: Boolean,
        colorRes: Int
    ) {
        val itemView = viewHolder.itemView
        paint.color = ContextCompat.getColor(itemView.context, colorRes)

        val rect = if (isRightSwipe) {
            android.graphics.RectF(
                itemView.left.toFloat(),
                itemView.top.toFloat(),
                itemView.left + dX,
                itemView.bottom.toFloat()
            )
        } else {
            android.graphics.RectF(
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
        }

        val corners = if (isRightSwipe) {
            floatArrayOf(cornerRadius, cornerRadius, 0f, 0f, 0f, 0f, cornerRadius, cornerRadius)
        } else {
            floatArrayOf(0f, 0f, cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0f, 0f)
        }

        val path = Path()
        path.addRoundRect(rect, corners, Path.Direction.CW)
        canvas.drawPath(path, paint)
    }

    private fun drawCenteredText(
        canvas: Canvas,
        text: String,
        x: Float,
        viewHolder: RecyclerView.ViewHolder,
        paint: Paint
    ) {
        val itemView = viewHolder.itemView
        val y = itemView.top + (itemView.height / 2f) - ((paint.descent() + paint.ascent()) / 2f)
        canvas.drawText(text, x, y, paint)
    }
}
