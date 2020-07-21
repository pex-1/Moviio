package practice.moviio.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import practice.moviio.BaseApplication
import practice.moviio.di.modules.AppModule
import practice.moviio.di.modules.MovieDbModule
import practice.moviio.di.modules.NetworkModule
import practice.moviio.di.modules.RepositoryModule
import practice.moviio.di.viewmodel.ViewModelFactoryModule
import practice.moviio.di.viewmodel.ViewModelsModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBuildersModule::class,
        NetworkModule::class,
        ViewModelFactoryModule::class,
        AppModule::class,
        MovieDbModule::class,
        ViewModelsModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent : AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}