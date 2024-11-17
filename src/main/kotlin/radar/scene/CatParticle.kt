package radar.scene

import core.base.BaseParticle

data class CatParticle(override var coordinates: Point2D, var state: CatStates = CatStates.CALM) :
    BaseParticle<Point2D, Offset2D>() {

    val id = COUNT++

    fun setCatState(state: CatStates) {
        this.state = state
    }

    companion object {
        var COUNT = 0
    }
}
