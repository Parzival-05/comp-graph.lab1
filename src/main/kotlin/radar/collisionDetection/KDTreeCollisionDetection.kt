package radar.collisionDetection

import CollisionDetection.Companion.INITIAL_BATCH_SIZE
import org.tinspin.index.PointDistance
import org.tinspin.index.kdtree.KDTree
import radar.scene.*
import java.util.*
import java.util.concurrent.*

const val DIMS = 2

class KDTreeCollisionDetection(private val workerPool: ExecutorService, private val threadPoolSize: Int) {

    val kValues = Collections.newSetFromMap(ConcurrentHashMap<Int, Boolean>())
    private val batchSize: Int
        get() {
            return if (kValues.size == 0) {
                INITIAL_BATCH_SIZE
            } else {
                kValues.sum() / kValues.size + 1
            }
        }

    fun findCollisions(scene: CatScene): Array<CatCollision> {
        val cats = scene.particles
        val kdTree = KDTree.create<CatParticle>(DIMS)
        for (cat in cats) {
            val coordinates = cat.coordinates
            kdTree.insert(doubleArrayOf(coordinates.x, coordinates.y), cat)
        }
        val collisions = Collections.newSetFromMap(ConcurrentHashMap<CatCollision, Boolean>())
        val pd = PointDistance { p1: DoubleArray, p2: DoubleArray ->
            scene.sceneConfig.metricFunction(
                Point2D(p1[0], p1[1]), Point2D(p2[0], p2[1])
            )
        }
        val handledCats = Collections.newSetFromMap(ConcurrentHashMap<Set<Int>, Boolean>())
        val chunkedCats = cats.chunked(cats.size / threadPoolSize)
        val jobs = mutableListOf<Future<*>>()
        for (chunkCat in chunkedCats) {
            val job = workerPool.submit {
                for (cat in chunkCat) {
                    val point = cat.coordinates
                    var catsAreClose = true
                    var batch = 0
                    var n = 0
                    while (catsAreClose) {
                        val currentBatchSize = batchSize
                        batch += currentBatchSize
                        val nearestCats = kdTree.queryKnn(doubleArrayOf(point.x, point.y), 1 + batch, pd)
                        for (i in 1..1 + (batch - currentBatchSize)) {
                            if (nearestCats.hasNext()) {
                                nearestCats.next()
                            } else {
                                break
                            }
                        }
                        for (neighbourCat in nearestCats) {
                            n++
                            val catNeighbour = neighbourCat.value()
                            if (catNeighbour == cat) {
                                continue
                            }
                            val dist = neighbourCat.dist()
                            val catIds = setOf(cat.id, catNeighbour.id)
                            if (dist < scene.sceneConfig.hissDist && !handledCats.contains(catIds)) {
                                handledCats.add(catIds)
                                val state = scene.calcNewState(dist)
                                if (state != CatStates.CALM) {
                                    val collision = CatCollision(cat, catNeighbour, dist, state)
                                    collisions.add(collision)
                                    break
                                }
                            } else {
                                catsAreClose = false
                                kValues.add(n)
                                break
                            }
                        }
                    }
                }
            }
            jobs.add(job)
        }
        for (job in jobs) {
            job.get()
        }
        INITIAL_BATCH_SIZE = batchSize
        return collisions.toTypedArray()
    }
}