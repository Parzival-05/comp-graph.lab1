package radar.scene

import core.base.BaseParticle

data class CatParticle(override var coordinates: Point2D, val radius: Int, var state: CatStates = CatStates.CALM) :
    BaseParticle<Point2D, Offset2D>() {

    //    companion object {
//        private var lastID = 0
//        fun fresh(): Int {
//            lastID += 1
//            return lastID
//        }
//    }
//
//    val id: Int = fresh()
    fun setCatState(state: CatStates) {
        this.state = state
    }
}