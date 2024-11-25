package radar.metrics

import CatSimulation.Companion.GRID_SIZE_X
import CatSimulation.Companion.GRID_SIZE_Y
import radar.scene.Point2D
import kotlin.math.*

fun euclidean(point1: Point2D, point2: Point2D): Double {
    return sqrt(
        (point1.x - point2.x)
            .pow(2) + (point1.y - point2.y)
            .pow(2)
    )
    // extracting the square root in our task is redundant, and should be avoided due to inefficiency
}

fun manhattan(point1: Point2D, point2: Point2D): Double {
    return abs(point1.x - point2.x) + abs(point1.y - point2.y)
}

const val radius = GRID_SIZE_X * 12f
fun greatCircle(point1: Point2D, point2: Point2D): Double {
    // Converting x coordinates to longitude (in radians)
    // [0; GRID_SIZE_X] -> [-180°; 180°] -> [-π; π]
    val lon1 = (point1.x / GRID_SIZE_X * 360 - 180) * PI.toFloat() / 180
    val lon2 = (point2.x / GRID_SIZE_X * 360 - 180) * PI.toFloat() / 180

    // Converting y coordinates to latitude (in radians)
    // [0; GRID_SIZE_Y] -> [-90°; 90°] -> [-π/2; π/2]
    val lat1 = (point1.y / GRID_SIZE_Y * 180 - 90) * PI.toFloat() / 180
    val lat2 = (point2.y / GRID_SIZE_Y * 180 - 90) * PI.toFloat() / 180

    val diffLat = Math.toRadians((lon2 - lon1))
    val diffLon = Math.toRadians((lat2 - lat1))

    val radiusStartLat = Math.toRadians(lon1)
    val radiusEndLat = Math.toRadians(lat1)

    val a = sin(diffLat / 2).pow(2.0) + sin(diffLon / 2).pow(2.0) * cos(radiusStartLat) * cos(radiusEndLat)
    val c = 2 * asin(sqrt(a))

    return radius * c
}