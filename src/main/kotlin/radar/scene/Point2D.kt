package radar.scene

import GRID_SIZE_X
import GRID_SIZE_Y
import core.base.BaseOffset
import core.base.BasePoint

data class Point2D(var x: Float, var y: Float) : BasePoint<Offset2D> {
    override fun isInScene(): Boolean {
        return (x in 0.0..GRID_SIZE_X.toDouble()) && (y in 0.0..GRID_SIZE_Y.toDouble())
    }
}

data class Offset2D(val x: Int, val y: Int) : BaseOffset<Point2D> {
    override fun move(coordinates: Point2D) {
        coordinates.x += this.x
        coordinates.y += this.y
    }
}
