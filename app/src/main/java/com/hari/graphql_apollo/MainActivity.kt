package com.hari.graphql_apollo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {
    //apollo-codegen download-schema https://api.github.com/graphql --output schema.json --header "Authorization: Bearer 383ed5ce0174cea9fedcfb873e94d4b9fcc2b97e"]

    //https://help.github.com/en/articles/creating-a-personal-access-token-for-the-command-line
    val TOKEN = "";

    val client by lazy { setupApollo() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        client.query(FindQuery    //From the auto generated class
                .builder()
                .name("retrofit") //Passing required arguments
                .owner("square") //Passing required arguments
                .build())
                .enqueue(object : ApolloCall.Callback<FindQuery.Data>() {
                    override fun onFailure(e: ApolloException) {
                        //Log.info(e.message.toString())
                    }

                    override fun onResponse(response: Response<FindQuery.Data>) {
                        Log.d(" ZAGG", response.data()?.repository().toString())

                        runOnUiThread {
                            url.text = response.data()?.repository()?.url().toString()
                        }
                    }
                })
    }

    private fun setupApollo(): ApolloClient {
        val okHttp = OkHttpClient
                .Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val builder = original.newBuilder().method(original.method(),
                            original.body())
                    builder.addHeader("Authorization"
                            , "Bearer " + TOKEN)
                    chain.proceed(builder.build())
                }
                .build()
        return ApolloClient.builder()
                .serverUrl("https://api.github.com/graphql")
                .okHttpClient(okHttp)
                .build()
    }
}



