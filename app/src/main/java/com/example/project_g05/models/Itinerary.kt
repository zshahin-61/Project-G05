package com.example.project_g05.models

import java.io.Serializable

data class Itinerary(
   val parkId :String,
   val parkName:String,
   val address:String,
   val tripDate: String,
   val notes:String
): Serializable {}

