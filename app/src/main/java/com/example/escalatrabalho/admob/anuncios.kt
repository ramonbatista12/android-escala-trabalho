package com.example.escalatrabalho.admob

import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView


@Composable
fun NativoAdmob(modifier: Modifier = Modifier){
    val NativeTag="NativeAd compose"

    AndroidView(
        modifier = modifier.height(100.dp).padding(5.dp).clip(RoundedCornerShape(10.dp))
                                          .border(width = 0.5.dp,
                                                  color = androidx.compose.ui.graphics.Color.Black,
                                                  shape = RoundedCornerShape(10.dp)),
        factory = { context ->
            NativeAdView(context).apply {
                val adRequest = AdRequest.Builder().build()
                val adLoader = AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110")
                                               .forNativeAd {nativeAd->
                                                   val headline= TextView(context)
                                                   val body= TextView(context)
                                                   val imageView= ImageView(context)
                                                   imageView.layoutParams=LinearLayout.LayoutParams(
                                                       200,200,0f)
                                                   val layout= LinearLayout(context)
                                                   layout.orientation=LinearLayout.HORIZONTAL
                                                   val coluna =LinearLayout(context)
                                                   coluna.addView(body)
                                                   coluna.addView(headline)
                                                   layout.addView(imageView)
                                                   layout.addView(coluna)


                                                   nativeAd.body.let {
                                                       this.bodyView=body
                                                   }
                                                   nativeAd.headline.let {
                                                       this.headlineView=headline
                                                   }
                                                   nativeAd.images.let {


                                                   }
                                                   headline.text=nativeAd.headline
                                                   body.text=nativeAd.body
                                                   imageView.setImageDrawable(nativeAd.images.first().drawable)
                                                   nativeAd.let {
                                                     /*  this.addView(headline)
                                                       this.addView(body)
                                                       this.addView(imageView)*/
                                                       if(nativeAd.mediaContent==null)
                                                               Log.i(NativeTag,"media content e null")
                                                       else Log.i(NativeTag,"media content nao e null")
                                                       this.addView(layout)
                                                       this.setNativeAd(nativeAd)
                                                   }

                                               }
                                               .withAdListener(object : AdListener(){
                                                   override fun onAdFailedToLoad(p0: LoadAdError) {
                                                       Log.e(NativeTag,"erro ao carregar ad erro : ${p0.message} codigoerro: ${p0.code} respomse : ${p0.responseInfo}")
                                                       super.onAdFailedToLoad(p0)
                                                   }
                                               })
                                               .withNativeAdOptions(NativeAdOptions.Builder().build())
                                               .build()
                adLoader.loadAd(adRequest)






            } })
}