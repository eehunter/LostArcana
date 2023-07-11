package com.oyosite.ticon.lostarcana.data

import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableDataType

object SDKotlin{
    operator fun invoke(name: String?, type: SerializableDataType<*>?): SD = SD()(name, type)
    operator fun <T> invoke(name: String?, type: SerializableDataType<T>?, defaultValue: T): SD = SD()(name, type, defaultValue)

    class SD: SerializableData(){
        operator fun invoke(name: String?, type: SerializableDataType<*>?): SD { add(name,type); return this }
        operator fun <T> invoke(name: String?, type: SerializableDataType<T>?, defaultValue: T): SD { add(name,type,defaultValue); return this }
    }
}