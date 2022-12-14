import java.util.DoubleSummaryStatistics
import java.util.ListResourceBundle





sealed class List<out A>  {
    abstract fun isEmpty() : Boolean
    abstract fun init() : List<A>
    internal object Nil: List<Nothing>() {
        override fun isEmpty(): Boolean = true
        override fun init(): List<Nothing> = throw IllegalStateException("")
        override fun toString() = "[Nil]"
    }
    internal class Cons<out A>(internal val head: A, internal val tail : List<A>): List<A>() {
        override fun isEmpty(): Boolean = false
        override fun init(): List<A> = reverse().drop(1).reverse()
        override fun toString() = "[${toString("", this)}]"
        private tailrec fun toString(acc: String, list: List<A>) : String =
            when(list) {
               Nil -> acc
                is Cons -> toString("$acc${list.head}", list.tail)
            }
    }

    companion object {
       fun <A> cons(a: A, list: List<A>) = Cons(a, list)
       tailrec fun <A> drop(list: List<A>, n: Int) : List<A>  =
          when(list)  {
            Nil -> list
            is Cons -> if(n <= 0) list else drop(list.tail, n - 1)
          }
        tailrec fun <A> dropWhile(list: List<A>, p: (A)->Boolean) : List<A> =
            when(list) {
                Nil -> list
                is Cons -> if(p(list.head)) dropWhile(list.tail, p) else list
            }

        fun <A, B> foldRight(list: List<A>, identity: B, f: (A)->(B)->B) : B =
            when(list) {
                Nil -> identity
                is Cons -> f(list.head)(foldRight(list.tail, identity, f))
            }
        fun <A, B> foldLeft(acc: B, list: List<A>, f: (B)->(A)->B) : B =
            when(list) {
                Nil -> acc
                is Cons -> foldLeft(f(acc)(list.head), list.tail, f)
            }

        operator fun <A> invoke(vararg az: A) : List<A> =
            az.foldRight(Nil as List<A>) {a: A, list: List<A> -> Cons(a, list)}
    }



}








































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































