package nicestring

// It doesn't contain substrings bu, ba or be;
fun String.checkCondition1(): Boolean {
    return this.zipWithNext()
        .toSet()
        .intersect(setOf(Pair('b', 'u'), Pair('b', 'a'), Pair('b', 'e')))
        .isEmpty()
}

// It contains at least three vowels (vowels are a, e, i, o and u);
fun String.checkCondition2(): Boolean {
    return this.groupingBy { it }
        .eachCount()
        .filterKeys { c -> c in setOf('a','e','i','o','u') }
        .map { it.value }
        .sum() >= 3
}
// contains a double letter (at least two similar letters following one another), like b in "abba".
fun String.checkCondition3(): Boolean {
    return this.zipWithNext().any { (a: Char, b: Char) -> a == b }
}

fun String.isNice(): Boolean {

    val a: Int = if (this.checkCondition1()) 1 else 0
    val b: Int = if (this.checkCondition2()) 1 else 0
    val c: Int = if (this.checkCondition3()) 1 else 0
    val ret = a + b + c
//    println("$a $b $c")
    return ret >= 2
}

//fun main(){
//    println("abaca".isNice())
//}