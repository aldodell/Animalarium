package org.philosophicas.animalarium


enum class Status(val status: String) {
    unknow("unknow"),
    delivered("delivered"),
    processing("processing"),
    send("send"),
    received("received")
}

class Order {
    var email = ""
    var lastname = ""
    var name = ""
    var personID = ""
    var phone = ""
    var products = ""
    var date = ""
    var price = 0.0
    var status: Status = Status.unknow
}