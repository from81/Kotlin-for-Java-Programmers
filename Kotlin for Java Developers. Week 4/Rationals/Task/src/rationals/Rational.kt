package rationals
import java.lang.IllegalArgumentException
import java.math.BigInteger

class Rational(): Comparable<Rational> {

    var numerator: BigInteger = (0).toBigInteger()
    var denominator: BigInteger = (0).toBigInteger()

    init {
        println("init called")
    }

    constructor(num: Int) : this((num).toBigInteger(), (1).toBigInteger()) {
    }

    constructor(num: Int, denom: Int) : this((num).toBigInteger(), (denom).toBigInteger()) {
    }

    constructor(num: Long, denom: Long) : this((num).toBigInteger(), (denom).toBigInteger()) {
    }

    constructor(num: BigInteger) : this(num, (1).toBigInteger()) {}

    constructor(num: BigInteger, denom: BigInteger) : this() {
        if (denom == 0.toBigInteger()) throw IllegalArgumentException("denominator cannot be 0")

        val absNum = num.abs()
        val absDenom = denom.abs()
        val gcd = absNum.gcd(absDenom)
        val sign = if ((num < 0.toBigInteger()) == (denom < 0.toBigInteger())) 1 else -1
        this.numerator = (absNum / gcd) * sign.toBigInteger()
        this.denominator = absDenom / gcd
    }

    public override fun hashCode(): Int = numerator.hashCode() * 31 + denominator.hashCode();

    public override fun equals(other: Any?): Boolean {
        if (other is Rational) {
            return numerator * other.denominator == other.numerator * denominator
        } else {
            return false
        }
    }

    public override fun compareTo(other: Rational): Int {
        val mNum = numerator * other.denominator;
        val oNum = other.numerator * denominator;
        return mNum.compareTo(oNum)
    }

    public operator fun plus(other: Rational): Rational {
        val mn = numerator * other.denominator
        val md = other.numerator * denominator
        return Rational(mn + md, denominator * other.denominator)
    }

    public operator fun minus(other: Rational): Rational {
        val mn = numerator * other.denominator
        val md = other.numerator * denominator
        return Rational(mn - md, denominator * other.denominator)
    }


    public operator fun times(other: Rational): Rational {
        return Rational(numerator * other.numerator, denominator * other.denominator)
    }

    public operator fun div(other: Rational): Rational {
        return Rational(numerator * other.denominator, denominator * other.numerator)
    }

    public operator fun unaryMinus(): Rational {
        return Rational(-numerator, denominator)
    }

    public fun component1() : BigInteger = numerator

    public fun component2() : BigInteger = denominator

    public override fun toString(): String = if (denominator != 1.toBigInteger()) "$numerator/$denominator" else "$numerator"
}

infix fun Int.divBy(that: Int): Rational = Rational(this, that)

infix fun Long.divBy(that: Long): Rational = Rational(this, that)

infix fun BigInteger.divBy(that: BigInteger): Rational = Rational(this, that)

fun String.toRational(): Rational {
    if(this.contains("/")) {
        return Rational(
            this.substringBefore('/').toBigInteger(),
            this.substringAfter('/').toBigInteger()
        )
    } else {
        return Rational(this.toBigInteger())
    }
}

fun main() {
    println("-912141".toRational())
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}