package trainer.kotlinlang.l4

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class VarianceInOutTest {

    @Test
    fun `covariant producer of Cat satisfies a Producer of Animal`() {
        val catProducer = object : VarianceInOut.Producer<VarianceInOut.Cat> {
            override fun produce() = VarianceInOut.Cat("Whiskers")
        }

        val animal = VarianceInOut.adoptAnyAnimal(catProducer)
        assertThat(animal.name).isEqualTo("Whiskers")
    }

    @Test
    fun `contravariant consumer of Animal satisfies a Consumer of Cat`() {
        var fedName: String? = null
        val animalConsumer = object : VarianceInOut.Consumer<VarianceInOut.Animal> {
            override fun consume(value: VarianceInOut.Animal) {
                fedName = value.name
            }
        }

        VarianceInOut.feedCat(VarianceInOut.Cat("Tom"), animalConsumer)
        assertThat(fedName).isEqualTo("Tom")
    }
}
