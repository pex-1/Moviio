package practice.moviio

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import practice.moviio.di.DaggerAppComponent

class BaseApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}