package com.example.sample.gwxlotteryproject.entity

data class EachColorEdit(
        val redInputVal: String,
        val blueInputVal: String,
        val pinkInputVal: String,
        val greenInputVal: String,
        val blackInputVal: String,
        val whiteInputVal: String,
        val yellowInputVal: String,
        val purpleInputVal: String,
        val orangeInputVal: String
) {

    private val colorInputs = listOf(
            redInputVal,
            blueInputVal,
            pinkInputVal,
            greenInputVal,
            blackInputVal,
            whiteInputVal,
            yellowInputVal,
            purpleInputVal,
            orangeInputVal
    )

    fun getList(): List<String> {
        return colorInputs
    }

    fun getIndexList(index: Int): String {
        return colorInputs[index]
    }
}