package classes

@Suppress("ktlint:standard:no-consecutive-comments")
/** Represents the different UI states during the simulation. */
enum class UIStates {
    /** TODO: rewrite it The state where the simulation is being modeled and computed. */
    READY_TO_DRAW,

    /** TODO: rewrite it The state indicating that the data within the scene needs to be updated. */
    DRAWING_IS_FINISHED,

    /** TODO: rewrite it or remove The state where the updated data is being drawn onto the UI. */
    DRAWING,
}

enum class ModelingStates {
    MODELING,
    FINISHED,
}
