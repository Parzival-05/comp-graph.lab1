package radar.collisionDetection

import CollisionDetection.Companion.THREAD_COUNT
import core.base.BaseCollisionDetection
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import radar.generators.CatGenerator
import radar.generators.MoveGenerator
import radar.scene.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.random.Random
import kotlin.test.assertTrue


class KDTreeCollisionDetectionTest {
    companion object {
        private const val COMPARE_WITH_BF_MAX_CAT_COUNT = 1000
        private const val COMPARE_WITH_BF_TEST_COUNT = 10000

        @JvmStatic
        fun particlesAndCollisionCount() = listOf(Arguments.of(arrayListOf(
            CatParticle(Point2D(0.0, 0.0)), CatParticle(Point2D(1.0, 1.0)), CatParticle(Point2D(4.0, 4.0))
        ), 1, 2, SceneConfig().apply {
            fightDist = 2
            hissDist = 5
            metric = SceneConfig.Companion.MetricType.EUCLIDEAN
        }), Arguments.of(arrayListOf(
            CatParticle(Point2D(0.0, 0.0)), CatParticle(Point2D(1.0, 1.0)), CatParticle(Point2D(4.0, 4.0))
        ), 1, 1, SceneConfig().apply {
            fightDist = 3
            hissDist = 5
            metric = SceneConfig.Companion.MetricType.MANHATTAN
        }))

        @JvmStatic
        fun randomParticles(): List<Arguments> {
            val bruteForceCollisionDetection = BruteForceCollisionDetection()
            val workerPool: ExecutorService = Executors.newFixedThreadPool(THREAD_COUNT)
            val kdTreeCollisionDetection = KDTreeCollisionDetection(workerPool, THREAD_COUNT)
            val catEmitter = CatEmitter(CatGenerator())
            val config = SceneConfig().apply {
                fightDist = 3
                hissDist = 5
                metric = SceneConfig.Companion.MetricType.EUCLIDEAN
            }

            fun generateCats(n: Int): ArrayList<CatParticle> {
                return arrayListOf(*catEmitter.emit(n).toTypedArray())
            }

            return mutableListOf<Arguments>().apply {
                repeat(COMPARE_WITH_BF_TEST_COUNT) {
                    add(
                        Arguments.of(
                            generateCats(Random.nextInt(COMPARE_WITH_BF_MAX_CAT_COUNT)),
                            config,
                            bruteForceCollisionDetection,
                            kdTreeCollisionDetection
                        )
                    )
                }
            }
        }
    }

    @ParameterizedTest
    @MethodSource("particlesAndCollisionCount")
    fun `test findCollisions detects correct number of collisions`(
        particles: ArrayList<CatParticle>, minCollisionCount: Int, maxCollisionCount: Int, config: SceneConfig
    ) {
        val scene = CatScene(particles, config, object : CatEmitter(
            CatGenerator()
        ) {
            override fun emit(n: Int): Set<CatParticle> {
                return setOf() // don't let cats spawn
            }
        })
        val collisions = scene.findCollisions()
        assertTrue {
            collisions.size in minCollisionCount..maxCollisionCount
        }
    }

    @ParameterizedTest
    @MethodSource("randomParticles")
    fun `test KDTree detects same fighters as brute force`(
        particles: ArrayList<CatParticle>,
        config: SceneConfig,
        bfCD: BruteForceCollisionDetection,
        kdTreeCD: KDTreeCollisionDetection
    ) {
        fun getCollisions(cd: BaseCollisionDetection<CatScene, CatParticle, Point2D, Offset2D, CatCollision, MoveGenerator>): Array<CatCollision> {
            val scene = CatScene(particles, config, object : CatEmitter(
                CatGenerator()
            ) {
                override fun emit(n: Int): Set<CatParticle> {
                    return setOf()
                }
            }, cd)
            return scene.findCollisions()
        }

        fun Array<CatCollision>.onlyFightCollisions(): List<CatCollision> = this.filter {
            it.catState == CatStates.FIGHT
        }

        val bfFightCollisions = getCollisions(bfCD).onlyFightCollisions()
        val kdTreeFightCollisions = getCollisions(kdTreeCD).onlyFightCollisions()
        assertTrue {
            bfFightCollisions.all { bfCollision ->
                kdTreeFightCollisions.count { kdTreeCollision ->
                    kdTreeCollision.particle1 == bfCollision.particle1 && kdTreeCollision.particle2 == bfCollision.particle2 || kdTreeCollision.particle2 == bfCollision.particle1 && kdTreeCollision.particle1 == bfCollision.particle2
                } == 1
            }
        }
    }
}