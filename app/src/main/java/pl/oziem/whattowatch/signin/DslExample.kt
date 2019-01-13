package pl.oziem.whattowatch.signin

fun main() {
  val person =
    person {
      name = "Marcin"
      surname = "Oziemski"
      address {
        street = "al. Grunwaldzka"
        number = 143
        city = "Gdańsk"
      }
    }
}

fun person(block: Person.() -> Unit) = Person().apply { block() }

class Person(
  var name: String? = null,
  var surname: String? = null,
  var address: Address? = null
)

fun Person.address(block: Address.() -> Unit) {
  address = Address().apply { block() }
}

class Address(
  var street: String? = null,
  var number: Int? = null,
  var city: String? = null
)
