/** Contains constants and configurations for the `CatSimulation`. */
class CatSimulation {
    companion object {
        /** The width of the simulation grid. */
        const val GRID_SIZE_X = 1920.0

        /** The height of the simulation grid. */
        const val GRID_SIZE_Y = 1080.0

        const val MIN_PARTICLE_COUNT = 100
        const val MAX_PARTICLE_COUNT = 50000
        const val MAX_LOGS_DISPLAYED = 50
        const val MIN_TAU = 1L
        const val MIN_CAT_RADIUS = 1
        const val MAX_CAT_RADIUS = 100
        const val MIN_CAT_SPEED = 1
        const val MAX_CAT_SPEED = 100
        const val MIN_HISS_DIST = 0
        const val MAX_HISS_DIST = 100
        const val MIN_FIGHT_DIST = 0
        const val MAX_FIGHT_DIST = 100
    }
}

/** Configuration class for collision detection algorithm. */
class CollisionDetection {
    companion object {
        /** Batch size for collision detection. */
        var batchSize = 5

        /** Number of threads to use for collision detection. */
        const val THREAD_COUNT = 8
    }
}
