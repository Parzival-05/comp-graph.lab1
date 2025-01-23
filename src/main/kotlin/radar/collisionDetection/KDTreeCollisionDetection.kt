package radar.collisionDetection

import CollisionDetection
import core.base.BaseCollisionDetection
import org.tinspin.index.PointDistance
import org.tinspin.index.kdtree.KDTree
import radar.scene.CatCollision
import radar.scene.CatParticle
import radar.scene.CatScene
import radar.scene.Offset2D
import radar.scene.Point2D
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future
import kotlin.math.max

const val DIMS = 2

/**
 * A collision detection class using KD-Tree to efficiently find collisions in a scene of particles.
 *
 * @property workerPool: [ExecutorService] - The thread pool used for parallel collision handling.
 * @property threadPoolSize: [Int] - The size of the thread pool.
 */
class KDTreeCollisionDetection(
    private val workerPool: ExecutorService,
    private val threadPoolSize: Int,
) : BaseCollisionDetection<CatScene, CatParticle, Point2D, Offset2D, CatCollision> {
    private val kValues: MutableSet<Int> = Collections.newSetFromMap(ConcurrentHashMap())

    /**
     * Calculates the batch size used for querying nearest neighbors in the KD-Tree.
     * If no k-values have been collected, it defaults to a predefined batch size.
     *
     * @return [Int] - The calculated batch size.
     */
    private val batchSize: Int
        get() =
            if (kValues.isEmpty()) {
                CollisionDetection.batchSize
            } else {
                kValues.sum() / kValues.size + 1
            }

    /**
     * Finds collisions among the particles in the given scene using a KD-Tree for efficient nearest neighbor search.
     *
     * @param scene: [CatScene] - The scene containing particles to check for collisions.
     * @return [Array<CatCollision>] - An array of detected collisions.
     *
     * The algorithm:
     * 1. Builds a KD-tree from the current coordinates of the particles
     * 2. Processes particles in chunks in parallel using a thread pool
     * 3. For each particle, it searches for the nearest neighbors in batches until it goes beyond the hissDist radius
     * 4. Uses a synchronized set to track processed particle pairs
     *
     */
    override fun findCollisions(scene: CatScene): Array<CatCollision> {
        val cats = scene.particles
        val kdTree = KDTree.create<CatParticle>(DIMS)
        // Insert all particles into the KD-Tree
        for (cat in cats) {
            val coordinates = cat.coordinates
            kdTree.insert(doubleArrayOf(coordinates.x, coordinates.y), cat)
        }
        val collisions = Collections.newSetFromMap(ConcurrentHashMap<CatCollision, Boolean>())
        val pd =
            PointDistance { p1: DoubleArray, p2: DoubleArray ->
                scene.sceneConfig.metricFunction(
                    Point2D(p1[0], p1[1]),
                    Point2D(p2[0], p2[1]),
                )
            }
        val handledCats = Collections.newSetFromMap(ConcurrentHashMap<Set<Int>, Boolean>())
        val chunkedCats = cats.chunked(max(cats.size / threadPoolSize, 1))
        val jobs = mutableListOf<Future<*>>()

        for (chunkCat in chunkedCats) {
            val job =
                workerPool.submit {
                    for (cat in chunkCat) {
                        val point = cat.coordinates
                        var catsAreClose = true
                        var batch = 0
                        var n = 0
                        while (catsAreClose) {
                            // catsAreClose indicates that there are still cats nearby, so we have to keep checking
                            // the cat's neighbors.
                            val currentBatchSize = batchSize
                            batch += currentBatchSize
                            val nearestCats =
                                kdTree.queryKnn(
                                    doubleArrayOf(point.x, point.y),
                                    1 + batch,
                                    pd,
                                )
                            // Skipping the first (batch - current batchSize) elements cause these are checked
                            for (i in 1..1 + (batch - currentBatchSize)) {
                                if (nearestCats.hasNext()) {
                                    nearestCats.next()
                                } else {
                                    catsAreClose = false
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
                                if (dist >= scene.sceneConfig.hissDist) {
                                    catsAreClose = false
                                    kValues.add(n)
                                    break
                                } else {
                                    val isHandled =
                                        synchronized(handledCats) {
                                            if (!handledCats.contains(catIds)) {
                                                handledCats.add(catIds)
                                                false
                                            } else {
                                                true
                                            }
                                        }
                                    if (!isHandled) {
                                        val collision = CatCollision(cat, catNeighbour, dist)
                                        collisions.add(collision)
                                    } else {
                                        break
                                    }
                                }
                            }
                        }
                    }
                }
            jobs.add(job)
        }

        // Wait for all tasks to complete
        for (job in jobs) {
            job.get()
        }

        // Update the global batch size based on the current calculations
        CollisionDetection.batchSize = batchSize
        return collisions.toTypedArray()
    }
}
