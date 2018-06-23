package com.eusecom.samfantozzi

import java.math.BigDecimal
import java.math.RoundingMode


data class CalcVatKt(var zk0 : Double, var zk1 : Double, var zk2 : Double, var dn1 : Double, var dn2 : Double,
                     var hod : Double, var nod : Double, var winp: Int, var logprx: Boolean ){


    fun setSumnod() {

        val sumdn1: Double = 0.1 * zk1
        dn1 = round(sumdn1, 2)

        val sumdn2: Double = 0.2 * zk2
        dn2 = round(sumdn2, 2)

        val sumhod: Double = zk0 + zk1 + zk2 + dn1 + dn2
        this.nod = sumhod

    }

    fun round(value: Double, places: Int): Double {
        var value = value
        if (places < 0) throw IllegalArgumentException()

        val factor = Math.pow(10.0, places.toDouble()).toLong()
        value = value * factor
        val tmp = Math.round(value)
        return tmp.toDouble() / factor
    }

}
