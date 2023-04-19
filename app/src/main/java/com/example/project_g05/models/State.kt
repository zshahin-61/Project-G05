package com.example.project_g05.models


data class State(val name: String, val abbreviation: String) {
    val fullName: String
        get() = "$name ($abbreviation)"

    companion object {
        val SELECT_STATE = State("Select State", "")
        fun values(): Array<State> {
            return arrayOf(
                State("Alabama", "AL"),
                State("Alaska", "AK"),
                State("Arizona", "AZ"),
                State("Arkansas", "AR"),
                State("California", "CA"),
                State("Colorado", "CO"),
                State("Connecticut", "CT"),
                State("Delaware", "DE"),
                State("Florida", "FL"),
                State("Georgia", "GA"),
                State("Hawaii", "HI"),
                State("Idaho", "ID"),
                State("Illinois", "IL"),
                State("Indiana", "IN"),
                State("Iowa", "IA"),
                State("Kansas", "KS"),
                State("Kentucky", "KY"),
                State("Louisiana", "LA"),
                State("Maine", "ME"),
                State("Maryland", "MD"),
                State("Massachusetts", "MA"),
                State("Michigan", "MI"),
                State("Minnesota", "MN"),
                State("Mississippi", "MS"),
                State("Missouri", "MO"),
                State("Montana", "MT"),
                State("Nebraska", "NE"),
                State("Nevada", "NV"),
                State("New Hampshire", "NH"),
                State("New Jersey", "NJ"),
                State("New Mexico", "NM"),
                State("New York", "NY"),
                State("North Carolina", "NC"),
                State("North Dakota", "ND"),
                State("Ohio", "OH"),
                State("Oklahoma", "OK"),
                State("Oregon", "OR"),
                State("Pennsylvania", "PA"),
                State("Rhode Island", "RI"),
                State("South Carolina", "SC"),
                State("South Dakota", "SD"),
                State("Tennessee", "TN"),
                State("Texas", "TX"),
                State("Utah", "UT"),
                State("Vermont", "VT"),
                State("Virginia", "VA"),
                State("Washington", "WA"),
                State("West Virginia", "WV"),
                State("Wisconsin", "WI"),
                State("Wyoming", "WY")
            )
        }
    }
}