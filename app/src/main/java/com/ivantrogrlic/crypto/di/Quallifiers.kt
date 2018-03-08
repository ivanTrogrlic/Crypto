package com.ivantrogrlic.crypto.di

import javax.inject.Qualifier


/**
 * Created by ivantrogrlic on 02/03/2018.
 */

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CryptoKey

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationContext
