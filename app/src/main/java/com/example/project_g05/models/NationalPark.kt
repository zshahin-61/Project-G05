package com.example.project_g05.models

import java.io.Serializable

data class NationalPark(
    val fullName:String,
    val description :String,
    val Url :String,
    val latitude :String,
    val longitude :String,
    val addresses :MutableList<AddressModel>,
    val images :MutableList<ImageModel>
):Serializable{}
