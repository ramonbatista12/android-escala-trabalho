package com.example.escalatrabalho.admob

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.escalatrabalho.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView




@SuppressLint("SuspiciousIndentation")
@Composable
fun NativoAdmob(modifier: Modifier = Modifier,whindowSizeClass: WindowSizeClass){
    val NativeTag="NativeAd compose"
    val altura = with(LocalDensity.current){
                  120.dp.toPx()
    }
    val larguraComponivel = if(whindowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.EXPANDED) 0.4f
                            else 1f
    val context = LocalContext.current
    val native= remember { mutableStateOf<NativeAd?>(null) }
    if(native.value!=null)
      AndroidView(
        modifier = modifier.height(290.dp).fillMaxWidth(larguraComponivel).padding(5.dp)
                                          ,
        factory = { context ->
            NativeAdView(context).apply {
                val headlineView = TextView(context)
                val mediaview = MediaView(context)
                mediaview.also {

                    it.layoutParams=LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,altura.toInt())
                }
                val imageView = ImageView(context)
                imageView.also {
                    it.layoutParams=LinearLayout.LayoutParams(100,100)
                    it.setPadding(10,3,10,3)
                }
               val bodyView = TextView(context)
                val ad=ImageView(context)
                ad.also {
                    val drawable=context.getDrawable(R.drawable.ad_badge)
                    it.setImageDrawable(drawable)
                    it.setPadding(10,3,10,3)
                }
                val secundari =TextView(context)
                val buttonCompat= Button(context)
                val colunaAd = LinearLayout(context)
                colunaAd.orientation=LinearLayout.HORIZONTAL
                val colunaPrincipal = LinearLayout(context)
                colunaPrincipal.orientation=LinearLayout.VERTICAL
                val colunaSecundaria = LinearLayout(context)
                colunaSecundaria.orientation=LinearLayout.HORIZONTAL
                val colunaTextosecundario = LinearLayout(context)
                colunaTextosecundario.orientation=LinearLayout.VERTICAL
                colunaAd.addView(ad)
                colunaPrincipal.addView(colunaAd)
                colunaPrincipal.addView(colunaSecundaria)
                colunaSecundaria.addView(imageView)
                colunaSecundaria.addView(colunaTextosecundario)
                colunaTextosecundario.addView(headlineView)

                colunaTextosecundario.addView(bodyView)
                colunaPrincipal.addView(mediaview)
                colunaPrincipal.addView(buttonCompat)
                this.addView(colunaPrincipal)
                this.mediaView=mediaview
                this.imageView=imageView
                this.headlineView=headlineView
                this.bodyView=bodyView
                this.callToActionView=buttonCompat
                this.imageView=imageView
                this.advertiserView=ad


                native.value!!.icon.let {
                    if(it!=null)
                        imageView.setImageDrawable(it.drawable)

                }
                native.value!!.headline.let {
                    if(it!=null)
                        headlineView.text=it
                }
                native.value!!.body.let {
                    if(it!=null)
                        bodyView.text=it
                }
                native.value!!.callToAction.let {
                    if(it!=null)
                        buttonCompat.text=it
                }
                native.value!!.advertiser.let {
                    // secundari.text=it
                }

                native.value!!.mediaContent?.let {

                    mediaview.setMediaContent(it)
                }
                native.value.let {
                    /*  this.addView(headline)
                      this.addView(body)
                      this.addView(imageView)*/
                    if(native.value!!.mediaContent==null)
                        Log.i(NativeTag,"media content e null")
                    else Log.i(NativeTag,"media content nao e null")

                    this.setNativeAd(native.value!!)
                }

 } })
    val adRequest = AdRequest.Builder().build()
    val adLoader = AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110")
        .forNativeAd {nativeAd->
            native.value=nativeAd

        }
        .withAdListener(object : AdListener(){
            override fun onAdFailedToLoad(p0: LoadAdError) {
                Log.e(NativeTag,"erro ao carregar ad erro : ${p0.message} codigoerro: ${p0.code} respomse : ${p0.responseInfo}")
                native.value=null
                super.onAdFailedToLoad(p0)
            }
        })
        .withNativeAdOptions(NativeAdOptions.Builder()
            .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
            .setRequestMultipleImages(false)
            .setMediaAspectRatio(NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_UNKNOWN)
            .build())
        .build()
    adLoader.loadAd(adRequest)
    DisposableEffect(Unit) {
        onDispose {
           if(native.value!=null){
               native.value!!.destroy()
               native.value=null
               Log.i(NativeTag,"ad destruido")
           }
           }
        }


}

@Composable
fun NativeEspandida(modifier: Modifier=Modifier){
    val NativeTag="NativeAd compose"
    val altura = with(LocalDensity.current){
        120.dp.toPx()
    }
    val largura = with(LocalDensity.current){


    val context = LocalContext.current
    val native= remember { mutableStateOf<NativeAd?>(null) }

    if(native.value!=null)
     AndroidView(modifier = modifier.height(200.dp).fillMaxWidth(1f).padding(5.dp),factory = {context->
         NativeAdView(context).apply {
             val bodyView = TextView(context)
             val headlineView = TextView(context)
             val iconeanuncio = ImageView(context)
             val mediaview = MediaView(context)
             val botaoDeacao= Button(context)
             val logoAd= ImageView(context).apply {
                 val drawable=context.getDrawable(R.drawable.ad_badge)
                 setImageDrawable(drawable)
                 setPadding(10,3,10,3)

             }
             val linhaPrincipal = LinearLayout(context)
                linhaPrincipal.orientation=LinearLayout.HORIZONTAL
             val colunaDostestos=LinearLayout(context)
             colunaDostestos.orientation=LinearLayout.VERTICAL
             val colunaBotao=LinearLayout(context)
             linhaPrincipal.addView(mediaview)
             linhaPrincipal.addView(iconeanuncio)
             colunaDostestos.addView(headlineView)
             colunaDostestos.addView(bodyView)
             colunaDostestos.addView(botaoDeacao)
             linhaPrincipal.addView(colunaDostestos)
             colunaBotao.addView(logoAd)

             linhaPrincipal.addView(colunaBotao)
             this.addView(linhaPrincipal)
             this.mediaView=mediaview
             this.bodyView=bodyView
             this.headlineView=headlineView
             this.callToActionView=botaoDeacao
             this.imageView=iconeanuncio
             this.advertiserView=logoAd
             this.setNativeAd(native.value!!)
             native.value!!.icon.let {
                 if(it!=null)
                 iconeanuncio.setImageDrawable(it.drawable)
             }
             native.value!!.body.let {
                 bodyView.text=it
             }
             native.value!!.headline.let {
                 headlineView.text=it
             }
             native.value!!.callToAction.let {
               botaoDeacao.text=it
             }
             native.value!!.mediaContent.let {
                 mediaview.setMediaContent(it)
             }



         }


     })


    val adRequest = AdRequest.Builder().build()
    val adLoader = AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110")
        .forNativeAd {nativeAd->
            native.value=nativeAd

        }
        .withAdListener(object : AdListener(){
            override fun onAdFailedToLoad(p0: LoadAdError) {
                Log.e(NativeTag,"erro ao carregar ad erro : ${p0.message} codigoerro: ${p0.code} respomse : ${p0.responseInfo}")
                native.value=null
                super.onAdFailedToLoad(p0)
            }
        })
        .withNativeAdOptions(NativeAdOptions.Builder()
            .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
            .setRequestMultipleImages(false)
            .setMediaAspectRatio(NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_UNKNOWN)
            .build())
        .build()
    adLoader.loadAd(adRequest)
    DisposableEffect(Unit) {
        onDispose {
            if(native.value!=null){
                native.value!!.destroy()
                native.value=null
                Log.i(NativeTag,"ad destruido")
            }
        }
    }


}}