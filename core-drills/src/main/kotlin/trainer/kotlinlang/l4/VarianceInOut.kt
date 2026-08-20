package trainer.kotlinlang.l4

// @task kotlinlang.l4.VarianceInOut
// @tags variance,in,out,generics
// @time 30m
// @src  new
object VarianceInOut {

    /** out T: интерфейс только ПРОИЗВОДИТ T — поэтому Producer<Cat> подходит вместо Producer<Animal>. */
    interface Producer<out T> {
        fun produce(): T
    }

    /** in T: интерфейс только ПОТРЕБЛЯЕТ T — поэтому Consumer<Animal> подходит вместо Consumer<Cat>. */
    interface Consumer<in T> {
        fun consume(value: T)
    }

    open class Animal(val name: String)
    class Cat(name: String) : Animal(name)

    /** Ковариантность: Producer<Cat> — подтип Producer<Animal>, поэтому его можно передать сюда. */
    fun adoptAnyAnimal(producer: Producer<Animal>): Animal {
        // ---8<--- solution
        return producer.produce()
        // --->8--- solution
    }

    /** Контравариантность: Consumer<Animal> — подтип Consumer<Cat>, поэтому его можно передать сюда. */
    fun feedCat(cat: Cat, consumer: Consumer<Cat>) {
        // ---8<--- solution
        consumer.consume(cat)
        // --->8--- solution
    }
}
