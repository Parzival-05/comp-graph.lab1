class CatSimulation {
    companion object {
        const val GRID_SIZE_X = 1920.0
        const val GRID_SIZE_Y = 1080.0
        const val PARTICLE_COUNT = 50000
        const val MAX_INTERACTIONS_DISPLAYED = 50
        const val MIN_TAU = 500L
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

class CollisionDetection {
    companion object {
        var INITIAL_BATCH_SIZE = 5
        const val THREAD_COUNT = 8
    }
}