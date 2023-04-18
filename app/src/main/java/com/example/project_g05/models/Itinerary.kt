package com.example.project_g05.models

import com.google.type.Date

data class Itinerary(
   val parkName:String,
   val address:String,
   val tripDate: Date,
   val notes:String
){}

