package br.com.mouzinho.starwarspopcode.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbRemoteKey(
    @PrimaryKey
    val id: Int,
    val nextKey: Int
)