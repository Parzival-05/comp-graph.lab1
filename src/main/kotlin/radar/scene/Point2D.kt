package radar.scene

import CatSimulation.Companion.GRID_SIZE_X
import CatSimulation.Companion.GRID_SIZE_Y
import core.base.BaseOffset
import core.base.BasePoint

data class Point2D(var x: Double, var y: Double) : BasePoint<Offset2D> {
    override fun isInScene(): Boolean {
        return (x in 0.0..GRID_SIZE_X) && (y in 0.0..GRID_SIZE_Y)
    }
}

data class Offset2D(val x: Double, val y: Double) : BaseOffset<Point2D> {
    override fun move(coordinates: Point2D) {
        coordinates.x += this.x
        coordinates.y += this.y
    }
}
