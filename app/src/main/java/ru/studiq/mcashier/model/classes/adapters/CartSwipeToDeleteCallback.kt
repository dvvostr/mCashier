package ru.studiq.mcashier.model.classes.adapters

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.DisplayMetrics
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.studiq.mcashier.R
import ru.studiq.mcashier.model.Settings
import ru.studiq.mcashier.model.classes.App

abstract class CartSwipeToDeleteCallback (context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.icon_trashbin)
    private var intrinsicSize: Int = Settings.Application.Companion.buttonSize // deleteIcon?.intrinsicWidth ?: 36
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }


    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
//        if (viewHolder?.adapterPosition == 10) return 0
        return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        val displayMetrics: DisplayMetrics = App.appContext.getResources().getDisplayMetrics();
        this.intrinsicSize = (Settings.Application.Companion.buttonSize * displayMetrics.density).toInt()
//        val dpHeight = displayMetrics.heightPixels / displayMetrics.density
//        val dpWidth = displayMetrics.widthPixels / displayMetrics.density

        if (isCanceled) {
            clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = ContextCompat.getColor(App.appContext, R.color.dangerous)
        val radius = 20f * displayMetrics.density
        c.drawRoundRect((itemView.right + dX.toInt()).toFloat(), itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat(), radius, radius, paint);
        // Calculate position of delete icon
        deleteIcon?.setTint(ContextCompat.getColor(App.appContext, R.color.background))
        val deleteIconTop = itemView.top + (itemHeight - intrinsicSize) / 2
        val deleteIconMargin = (itemHeight - intrinsicSize) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicSize
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + intrinsicSize

        // Draw the delete icon
        deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon?.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }
}
