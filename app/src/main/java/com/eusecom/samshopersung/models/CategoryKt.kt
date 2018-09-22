package com.eusecom.samshopersung

data class CategoryKt(var cat : String, var nac: String, var prm1: String) {

    override fun toString(): String {
        return cat + " " + nac
    }

}
