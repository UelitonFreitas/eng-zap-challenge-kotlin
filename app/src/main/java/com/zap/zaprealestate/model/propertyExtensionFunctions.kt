package com.zap.zaprealestate.mainscreen

import com.zap.zaprealestate.model.BusinessType
import com.zap.zaprealestate.model.Property
import java.text.DecimalFormat

fun Property.isPropertyInExpectedRange(): Boolean {

    val minLongitude = -46.693419
    val maxLongitude = -46.641146

    val minLatitude = -23.568704
    val maxLatitude = -23.546686

    return this.run { latitude in minLatitude..maxLatitude && longitude in minLongitude..maxLongitude }
}

fun Property.thereIsLocation() = latitude != 0.0 && longitude != 0.0

fun Property.formattedBusinessTypeName() = when (this.businessType) {
    BusinessType.RENT -> "Apartamento para Aluguel"
    else -> "Apartamento para Venda"
}

fun Property.formattedPrice(): String =
    DecimalFormat("R\$: #,###.##").run {
        format(price.toLong())
    }