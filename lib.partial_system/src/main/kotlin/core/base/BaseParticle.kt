package core.base

interface BasePoint<O> {
    fun isInScene(): Boolean
}

interface BaseOffset<T> {
    fun move(coordinates: T)
}

abstract class BaseParticle<T : BasePoint<O>, O : BaseOffset<T>> {
    open lateinit var coordinates: T


    fun isInScene(): Boolean = coordinates.isInScene()
}