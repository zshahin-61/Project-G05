package com.example.project_g05.models

import java.io.Serializable

data class NationalPark(
    val fullName:String,
    val description :String,
    val url :String,
    val latitude :Double,
    val longitude :Double,
    val addresses :MutableList<String>,
    val images :MutableList<String>
):Serializable{}
