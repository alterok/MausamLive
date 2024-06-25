package com.alterok.mausamlive.core.data.local.model

import androidx.room.Embedded
import androidx.room.Relation


data class LocalityWithMausam(
    @Embedded
    val locality: LocalityEntity,
    @Relation(
        entity = MausamEntity::class,
        parentColumn = "localityId",
        entityColumn = "localityId"
    )
    val mausamData: MausamEntity,
)
