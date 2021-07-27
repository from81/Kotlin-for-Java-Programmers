package mastermind

import java.lang.Integer.min

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    var rightPosition = 0
    val secretChars: List<Char> = secret.toList()
    val guessChars: List<Char> = guess.toList()
    val secretCharsDiff = mutableListOf<Char>()
    val guessCharsDiff = mutableListOf<Char>()


    for (i in secretChars.indices) {
        if (secretChars[i] == guessChars[i]) rightPosition++
        else {
            secretCharsDiff += secretChars[i]
            guessCharsDiff += guessChars[i]
        }
    }

    val secretDiffFreq = secretCharsDiff.groupingBy{ it }.eachCount().filter{ it.value > 0 }
    val guestDiffFreq = guessCharsDiff.groupingBy{ it }.eachCount().filter{ it.value > 0 }
    var wrongPosition = 0
    guestDiffFreq.forEach{
        (char, n) -> if (char in secretDiffFreq) wrongPosition += min(
            secretDiffFreq.getOrDefault(char,0),
            guestDiffFreq.getOrDefault(char, 0)
        )
    }
    return Evaluation(rightPosition, wrongPosition)
}

fun main() {
    val eval: Evaluation = evaluateGuess("AABC", "ADFE")
    print(eval)
}