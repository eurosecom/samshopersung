package com.eusecom.samfantozzi

data class Account(var accname : String, var accnumber: String, var accdoc: String
                   , var accdov: String, var acctype: String, var datm: String, var logprx: String) {

    //acctype 1=receipt cash, 2=expense cash...
    override fun toString(): String {
        return accnumber + " " + accname
    }
}
