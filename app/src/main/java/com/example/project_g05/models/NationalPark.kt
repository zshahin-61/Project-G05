package com.example.project_g05.models

import java.io.Serializable

data class NationalPark(
    val fullName:String,
    val description :String,
    val url :String,
    val latitude :String,
    val longitude :String,
    val addresses :List<AddressModel>,
    val images :List<ImageModel>,
    val id:String
):Serializable{}
