package classes

@Suppress("ktlint:standard:no-consecutive-comments")
/**
 * Represents the different UI states during the simulation.
 */
enum class UIStates {
    /**
     * The state where the simulation has been initialized and is ready for visualization.
     */
    READY_TO_DRAW,

    /**
     * The state indicating that the rendering process is complete and the UI reflects the final output.
     */
    DRAWING_IS_FINISHED,
}

/**
 * Represents the different modeling states during the simulation lifecycle.
 */
enum class ModelingStates {
    /**
     * The state where the system is performing modeling and calculations.
     */
    MODELING,

    /**
     * The state indicating that all modeling computations are complete.
     */
    FINISHED,
}
