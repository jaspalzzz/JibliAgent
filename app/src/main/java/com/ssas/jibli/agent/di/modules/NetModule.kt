package com.ssas.jibli.agent.di.modules

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ssas.jibli.agent.BuildConfig
import com.ssas.jibli.agent.network.ApiService
import com.ssas.jibli.agent.network.ApiServiceDinarPay
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
class NetModule {
	
	@Provides
	@Singleton
	fun provideGson(): Gson {
		val gsonBuilder = GsonBuilder()
		gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
		gsonBuilder.setLenient()
		return gsonBuilder.create()
	}
	
	@Provides
	@Singleton
	fun provideOkhttpClient(): OkHttpClient {
		val client = OkHttpClient.Builder()
		client.readTimeout(TIME_OUT, TimeUnit.MINUTES)
		client.connectTimeout(TIME_OUT, TimeUnit.MINUTES)
		val logging = HttpLoggingInterceptor()
		logging.level = HttpLoggingInterceptor.Level.BODY
		client.addInterceptor { chain ->
			val request = chain.request()
				.newBuilder()
				//  .addHeader("language", MApplication.language)
				
				.build()
			chain.proceed(request)
		}
		val protocols: MutableList<Protocol> = ArrayList()
		protocols.add(Protocol.HTTP_1_1)
		//protocols.add(Protocol.HTTP_2)
		client.protocols(protocols)
		client.addInterceptor(logging)
		return client.build()
	}
	
	@Provides
	@Singleton
	@Named("jibli")
	fun provideJibliRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
		return Retrofit.Builder()
			.addConverterFactory(GsonConverterFactory.create(gson))
			.baseUrl(BuildConfig.BASE_URL)
			.client(okHttpClient)
			.build()
	}
	
	@Provides
	@Singleton
	fun provideApiService(@Named("jibli") retrofit: Retrofit): ApiService {
		return retrofit.create(ApiService::class.java)
	}
	
	@Provides
	@Singleton
	@Named("dinarpay")
	fun provideDinarpayRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
		return Retrofit.Builder()
			.addConverterFactory(GsonConverterFactory.create(gson))
			.baseUrl(BuildConfig.DINAR_BASE_URL)
			.client(okHttpClient)
			.build()
	}
	
	@Provides
	@Singleton
	fun provideApiServiceDinarpay(@Named("dinarpay") retrofit: Retrofit): ApiServiceDinarPay {
		return retrofit.create(ApiServiceDinarPay::class.java)
	}
	
	companion object {
		const val TIME_OUT: Long = 2
	}
	
}
