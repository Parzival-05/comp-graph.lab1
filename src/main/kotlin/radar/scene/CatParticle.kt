package radar.scene

import core.base.BaseParticle

data class CatParticle(override var coordinates: Point2D, var state: CatStates = CatStates.CALM) :
    BaseParticle<Point2D, Offset2D>() {

    fun setCatState(state: CatStates) {
        this.state = state
    }
}
