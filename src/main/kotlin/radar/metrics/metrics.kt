package radar.metrics

import radar.scene.Point2D
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

fun euclidean(point1: Point2D, point2: Point2D): Float {
    return sqrt(
        (point1.x - point2.x)
            .pow(2) + (point1.y - point2.y)
            .pow(2)
    )
    // extracting the square root in our task is redundant, and should be avoided due to inefficiency
}

fun manhattan(point1: Point2D, point2: Point2D): Float {
    return (abs(point1.x - point2.x) + abs(point1.y - point2.y))
}
