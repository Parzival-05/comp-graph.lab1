class CatSimulation {
    companion object {
        const val GRID_SIZE_X = 1920
        const val GRID_SIZE_Y = 1080
        const val CAT_RADIUS = 1
        const val TAU = 500L
        const val ONE_SECOND = 1000.0
        const val FRAMES_PER_TAU = (60 / (ONE_SECOND / TAU)).toInt()
        const val PARTICLE_COUNT = 50000
    }
}