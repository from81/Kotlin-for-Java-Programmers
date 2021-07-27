package taxipark

import kotlin.math.floor
import kotlin.math.min

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
    this.allDrivers.filter{d: Driver -> !this.trips.map{it.driver}.toSet().contains(d)}.toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {
    if (minTrips == 0) return this.allPassengers.toSet()
    else return this.trips
        .flatMap { it.passengers.toList() }
        .groupingBy { it }
        .eachCount()
        .filter { it.value >= minTrips }
        .keys
    }

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
    this.trips.filter{it.driver == driver}
        .flatMap{ it.passengers.toList() }
        .groupingBy{ it }
        .eachCount()
        .filter{ it.value > 1}
        .keys

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
    this.allPassengers
        .filter { p: Passenger -> this.trips.filter { it -> p in it.passengers && it.discount != null }.count() >
                    this.trips.filter { it -> p in it.passengers && it.discount == null }.count()}
        .toSet()

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val mod10 = this.trips.groupingBy { it.duration / 10 }
        .eachCount()
        .toList()
        .sortedBy { it.component2()}
        .map{it.component1()}
        .lastOrNull()
    return if (mod10 != null) (mod10*10)..((mod10*10)+9)
        else null
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
@OptIn(ExperimentalStdlibApi::class)
fun TaxiPark.checkParetoPrinciple(): Boolean {
    val n = floor(this.allDrivers.size * 0.2).toInt()
    if (this.trips.isEmpty()) return false
    else return (this.trips.map{Pair(it.driver, it.cost)}
        .groupBy { it.component1() }
        .map{ it.component1() to it.component2().map{(driver, revenue) -> revenue}.sum() }
        .sortedBy {-it.component2()}
        .scan(
            0.0,
            { acc, (driver, revenue) -> acc+revenue}
        )
        .elementAt(n) / this.trips.map { it.cost }.sum()) >= 0.8
}