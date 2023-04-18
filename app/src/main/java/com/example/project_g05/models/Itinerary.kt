package com.example.project_g05.models

import com.google.type.Date
import java.io.Serializable

data class Itinerary(
   val parkName:String,
   val address:String,
   val tripDate: String,
   val notes:String
): Serializable {}

