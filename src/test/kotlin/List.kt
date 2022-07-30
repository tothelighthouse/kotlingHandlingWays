import java.util.DoubleSummaryStatistics
import java.util.ListResourceBundle


sealed class List <out A> {
    abstract fun isEmpty() : Boolean
    abstract fun init() :  List<A>

    internal object Nil : List<Nothing>(){
        override fun isEmpty() = true
        override fun init(): List<Nothing> = throw IllegalStateException("lkjlkj")
    }

    internal class Cons<out A>(internal val head: A, internal val tail: List<A>) : List<A>() {
        override fun init(): List<A> = reverse().drop(1).reverse()
        override fun isEmpty(): Boolean = false
        override fun toString() : String = "[${toString("", this)}]"
        private tailrec fun toString(acc: String, list: List<A>): String =
            when(list) {
                Nil -> acc
                is Cons -> toString("$acc${list.head}", list.tail)
            }
    }
    companion object {
        fun<A> cons(a: A, list: List<A>): List<A> = Cons(a, list)
        tailrec fun <A> drop(list: List<A>, n: Int) : List<A> =
            when(list) {
                Nil -> list
                is Cons -> if(n <= 0) list else drop(list.tail, n - 1)
            }

        tailrec fun <A>  dropWhile(list: List<A>, p: (A)-> Boolean) : List<A> =
            when(list) {
                Nil -> list
                is Cons -> if(p(list.head)) dropWhile(list.tail, p) else list
            }

        fun <A, B> foldRight(list: List<A>, identity: B, f:(A)->(B)->B) : B =
            when(list) {
                Nil -> identity
                is Cons -> f(list.head)(foldRight(list.tail, identity, f))
            }

        fun <A, B> foldLeft(acc: B, list: List<A>, f: (B)->(A)->B) : B =
            when(list) {
                Nil -> acc
                is Cons -> foldLeft(f(acc)(list.head), list.tail, f)
            }

        operator fun <A>invoke(vararg az: A) : List<A> =
            az.foldRight(Nil){a: A , list: List<A> -> Cons(a, list)}

        fun <A> concat(list1: List<A>, list2: List<A>)  = list1.reverse().foldLeft(list2) {x -> x::cons}
    }

    fun cons(a: @UnsafeVariance A) = Cons(a, this)
    fun drop(n: Int) = drop(this, n)
    fun dropWhile(p: (A)-> Boolean) = dropWhile(this, p)
    fun <B> foldLeft(identity: B, f: (B)->(A)->B) : B = foldLeft(identity, this, f)
    fun <B> foldRight(identity: B, f: (A)->(B)->B): B = foldRight(this, identity, f)
    fun reverse()  : List<A> = foldLeft(Nil as List<A>) {acc -> { acc.cons(it)}}
    fun length() : Int = foldLeft(0) {{ _-> 1 + it}}
    fun concat(list: List<@UnsafeVariance A>) : List = concat(this, list)




}







fun sum(list: List<Int>) : Int = list.foldRight(0) {a -> {b -> a + b}}
fun product(list: List<Double>): Double = list.foldRight(1.0)  {a -> {b -> a * b}}

fun triple(list: List<Int>) : List<Int> =
    List.foldRight(list, List()) {h -> {t: List<Int> -> t.cons(h * 3)}}

fun doubleToString(list: List<Double>) : List<String> =
    List.foldRight(list, List()) {a -> {b: List<String> -> b.cons(a.toString())}}



































































































