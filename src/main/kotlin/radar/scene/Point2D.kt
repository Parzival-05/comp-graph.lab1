package radar.scene

import CatSimulation.Companion.GRID_SIZE_X
import CatSimulation.Companion.GRID_SIZE_Y
import core.base.BaseOffset
import core.base.BasePoint

/**
 * Represents a point in 2D space.
 *
 * @property x The x-coordinate.
 * @property y The y-coordinate.
 */
data class Point2D(var x: Double, var y: Double) : BasePoint<Offset2D> {
    /**
     * Checks if the point is within the bounds of the scene.
     *
     * @return True if within bounds, false otherwise.
     */
    override fun isInScene(): Boolean {
        return (x in 0.0..GRID_SIZE_X) && (y in 0.0..GRID_SIZE_Y)
    }
}

/**
 * Represents an offset in 2D space for moving a point.
 *
 * @property x The x-offset.
 * @property y The y-offset.
 */
data class Offset2D(val x: Double, val y: Double) : BaseOffset<Point2D> {
    /**
     * Moves the given coordinates by this offset.
     *
     * @param coordinates The coordinates to move.
     */
    override fun move(coordinates: Point2D) {
        coordinates.x += this.x
        coordinates.y += this.y
    }
}
