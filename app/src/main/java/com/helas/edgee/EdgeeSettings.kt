package com.helas.edgee


class EdgeeSettings {
    var positionX: Float = 104.0f
    var positionY: Float = 50.0f

    var strokeWidth: Float = 0.0f
    var radius: Float = 0.0f

    var bgColor: Int = 0
    var onColor: Int = 0
    var offColor: Int = 0

    var bgColorHex: String = ""
    var onColorHex: String = ""
    var offColorHex: String = ""

    var startAngle: Float = 0.0f
    var endAngle: Float = 0.0f

    companion object {
        fun newInstance(): EdgeeSettings {
            return EdgeeSettings()
        }
    }
}
