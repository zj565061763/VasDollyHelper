package com.sd.app.vasdolly.moshi

import com.squareup.moshi.Moshi

val fMoshi: Moshi = Moshi.Builder()
    .addLast(FNullSafeKotlinJsonAdapterFactory())
    .build()