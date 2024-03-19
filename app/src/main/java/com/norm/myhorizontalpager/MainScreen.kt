package com.norm.myhorizontalpager

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen() {
    val images = remember {
        mutableStateListOf(
            "https://images.wallpaperscraft.ru/image/single/gorod_noch_panorama_117682_1080x1920.jpg",
            "https://images.wallpaperscraft.ru/image/single/tokio_nochnoj_gorod_neboskreby_121628_1080x1920.jpg",
            "https://images.wallpaperscraft.ru/image/single/ulitsa_gorod_zdaniia_198312_1080x1920.jpg",
            "https://images.wallpaperscraft.ru/image/single/gorod_vecher_snegopad_136478_1080x1920.jpg",
            "https://images.wallpaperscraft.ru/image/single/nochnoj_gorod_vid_sverhu_ogni_goroda_134887_1080x1920.jpg",
            "https://images.wallpaperscraft.ru/image/single/gorod_vid_sverhu_doroga_156925_1080x1920.jpg",
            "https://images.wallpaperscraft.ru/image/single/zheleznaia_doroga_poezd_stantsiia_134586_1080x1920.jpg",
        )
    }

    val pagerState = rememberPagerState(pageCount = { images.size })
    val matrix = remember {
        ColorMatrix()
    }

    Scaffold(
        modifier = Modifier.padding(vertical = 48.dp)
    ) {
        HorizontalPager(
            state = pagerState,
        ) { index ->
//            Log.d("MyLog", "${pagerState.currentPageOffsetFraction}")
            val pageOffset = (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction
            val imageSize by animateFloatAsState(
                targetValue = if (pageOffset != 0.0f) 0.75f else 1f,
                animationSpec = tween(durationMillis = 300), label = ""
            )

            LaunchedEffect(key1 = imageSize) {
                if (pageOffset != 0.0f) {
                    matrix.setToSaturation(0f)
                    Log.d("MyLog", "setToSaturation(0f)")
                } else {
                    matrix.setToSaturation(1f)
                    Log.d("MyLog", "setToSaturation(1f)")
                }
            }

//            doesn't work on "io.coil-kt:coil-compose:2.6.0" due to recomposition disabled
            AsyncImage(
//                modelEqualityDelegate = DefaultModelEqualityDelegate,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .graphicsLayer {
                        scaleX = imageSize
                        scaleY = imageSize
                    },
                model = ImageRequest.Builder(LocalContext.current)
                    .data(images[index])
                    .crossfade(true)
                    .build(),
                contentDescription = images[index].substringAfterLast("/"),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.colorMatrix(matrix)
            )
        }
    }
}