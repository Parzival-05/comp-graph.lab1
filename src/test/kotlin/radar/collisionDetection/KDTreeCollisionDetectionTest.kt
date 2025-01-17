package radar.collisionDetection

import BaseTest
import CollisionDetection.Companion.THREAD_COUNT
import core.base.BaseCollisionDetection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import radar.generators.CatGenerator
import radar.scene.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.random.Random
import kotlin.test.assertTrue

class KDTreeCollisionDetectionTest : BaseTest() {
    companion object {
        private const val COMPARE_WITH_BF_MAX_CAT_COUNT = 10
        private const val COMPARE_WITH_BF_TEST_COUNT = 20

        @JvmStatic
        fun particlesAndCollisionCount() =
            listOf(
                Arguments.of(
                    arrayListOf(
                        CatParticle(Point2D(0.0, 0.0), Point2D(0.0, 0.0)),
                        CatParticle(Point2D(1.0, 1.0), Point2D(1.0, 1.0)),
                        CatParticle(Point2D(4.0, 4.0), Point2D(4.0, 4.0)),
                    ),
                    1,
                    2,
                    SceneConfig.apply {
                        fightDist = 2
                        hissDist = 5
                        metric = MetricType.EUCLIDEAN
                    },
                ),
                Arguments.of(
                    arrayListOf(
                        CatParticle(Point2D(0.0, 0.0), Point2D(0.0, 0.0)),
                        CatParticle(Point2D(1.0, 1.0), Point2D(1.0, 1.0)),
                        CatParticle(Point2D(4.0, 4.0), Point2D(4.0, 4.0)),
                    ),
                    1,
                    1,
                    SceneConfig.apply {
                        fightDist = 3
                        hissDist = 5
                        metric = MetricType.MANHATTAN
                    },
                ),
            )

        @JvmStatic
        fun randomParticles(): List<Arguments> {
            val bruteForceCollisionDetection = BruteForceCollisionDetection()
            val workerPool: ExecutorService = Executors.newFixedThreadPool(THREAD_COUNT)
            val kdTreeCollisionDetection = KDTreeCollisionDetection(workerPool, THREAD_COUNT)
            val catEmitter = CatEmitter(CatGenerator())
            val config =
                SceneConfig.apply {
                    fightDist = 3
                    hissDist = 5
                    metric = MetricType.EUCLIDEAN
                }

            fun generateCats(n: Int): ArrayList<CatParticle> {
                val catsList = ArrayList<CatParticle>(n)
                catsList.addAll(catEmitter.emit(n))
                return catsList
            }

            return mutableListOf<Arguments>().apply {
                repeat(COMPARE_WITH_BF_TEST_COUNT) {
                    add(
                        Arguments.of(
                            generateCats(Random.nextInt(COMPARE_WITH_BF_MAX_CAT_COUNT)),
                            config,
                            bruteForceCollisionDetection,
                            kdTreeCollisionDetection,
                        ),
                    )
                }
            }
        }
    }

    @ParameterizedTest
    @MethodSource("particlesAndCollisionCount")
    fun `test findCollisions detects correct number of collisions`(
        particles: ArrayList<CatParticle>,
        minCollisionCount: Int,
        maxCollisionCount: Int,
        config: SceneConfig,
    ) {
        val scene =
            CatScene(
                particles,
                config,
                object : CatEmitter(
                    CatGenerator(),
                ) {
                    override fun emit(n: Int): Set<CatParticle> {
                        return setOf() // don't let cats spawn
                    }
                },
            )
        val collisions = scene.findCollisions()
        assertTrue {
            collisions.size in minCollisionCount..maxCollisionCount
        }
    }

    @ParameterizedTest
    @MethodSource("randomParticles")
    fun `test KDTree calculates same distance as brute force`(
        particles: ArrayList<CatParticle>,
        config: SceneConfig,
        bfCD: BruteForceCollisionDetection,
        kdTreeCD: KDTreeCollisionDetection,
    ) {
        fun getCollisions(cd: BaseCollisionDetection<CatScene, CatParticle, Point2D, Offset2D, CatCollision>): Array<CatCollision> {
            val scene =
                CatScene(
                    particles,
                    config,
                    object : CatEmitter(
                        CatGenerator(),
                    ) {
                        override fun emit(n: Int): Set<CatParticle> = setOf()
                    },
                    cd,
                )
            return scene.findCollisions()
        }

        val bfFightCollisions = getCollisions(bfCD).toSet()
        val kdTreeFightCollisions = getCollisions(kdTreeCD).toSet()
        assertEquals(bfFightCollisions, kdTreeFightCollisions)
    }
}
