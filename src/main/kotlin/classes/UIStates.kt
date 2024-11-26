package classes

/**
 * Represents the different UI states during the simulation.
 */
enum class UIStates {
    /**
     * The state where the simulation is being modeled and computed.
     */
    MODELING,

    /**
     * The state indicating that the data within the scene needs to be updated.
     */
    UPDATE_DATA,

    /**
     * The state where the updated data is being drawn onto the UI.
     */
    DRAWING
}
